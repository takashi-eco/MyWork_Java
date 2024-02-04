package com.example.app.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.Login;
import com.example.app.domain.Order;
import com.example.app.domain.Ticket;
import com.example.app.domain.User;
import com.example.app.login.LoginStatus;
import com.example.app.service.TicketService;
import com.example.app.service.UserService;
import com.example.app.validation.LoginGroup;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/ticket")
@RequiredArgsConstructor
public class TicketController {

	private static final int NUM_PER_PAGE = 6;

	private final TicketService ticketService;
	private final UserService userService;
	private final HttpSession session;

	// 試合選択画面

	@GetMapping({ "/", "/list" })
	public String list(
			@RequestParam(name = "page", defaultValue = "1") Integer page,
			Model model) throws Exception {
		
		model.addAttribute(new Login());
		
		// ログイン済みか判定
		if(session.getAttribute("loginStatus") != null) {
			LoginStatus loginStatus = (LoginStatus) session.getAttribute("loginStatus");
			Integer id = loginStatus.getId();
//			System.out.println(id);
			model.addAttribute("userId", id);
			model.addAttribute("isUser", true);
		} else {
			model.addAttribute("isUser", false);
		}
		
		// openweathermapに問い合わせ
		ticketService.askWeather();
		

		// チケットリスト
		model.addAttribute("gameList",
				ticketService.getGamesListByPage(page, NUM_PER_PAGE));
		
			
		
		// 
		model.addAttribute("page", page);
		
		int totalPages = ticketService.getTotalPages(NUM_PER_PAGE);
		model.addAttribute("totalPages", totalPages);

		return "ticket/list";
	}
	
	@GetMapping("/logout")
	public String logout(
			RedirectAttributes ra) {
		session.removeAttribute("loginStatus");
		ra.addFlashAttribute("message", "ログアウトしました。");
		return "redirect:/ticket/list";
	}
	

	@PostMapping({"/", "/list"})
	public String listAndLoginPost(
			@Validated (LoginGroup.class) Login login,
			Errors errors,
			Model model) throws Exception {
		if(errors.hasErrors()) {
			return "ticket/list";
		}
		
		// 正しいIDとパスワードの組み合わせか確認
				User user = userService.getUserByLoginId(login.getLoginId());
				if(user == null || !login.isCorrectPassword(user.getLoginPass())) {
					errors.rejectValue("loginId", "error.incorrect_id_or_password");
					return "ticket/list";
				}
				
		LoginStatus loginStatus = LoginStatus.builder()
				.id(user.getId())
				.name(user.getName())
				.loginId(user.getLoginId())
				.authority(user.getRole())
				.build();
		session.setAttribute("loginStatus", loginStatus);

		System.out.println(loginStatus);
		/*
		 *  ユーザー権限(role)で振り分ける
		 *  1:ユーザー権限
		 *  10:管理者権限
		 */
		int r = user.getRole();
		if( r == 10 ) {
			return "redirect:/admin/menu";
		}
		
		
		return "redirect:/ticket/list";
	}
	
	// 席種選択画面

	@GetMapping("/detail/{id}")
	public String detail(
			@PathVariable Integer id,
			RedirectAttributes rd,
			Model model) throws Exception {
		// 販売期日終了の場合の処理
		List<Ticket> ticketList = ticketService.getTicketsByGameId(id);
		
		for (Ticket ticket : ticketList) {
			String saleEnd = "販売終了";
			if(ticket.getSaleStatus().equals(saleEnd)) {
//				System.out.println(ticket);
				rd.addFlashAttribute("statusMessage", "選択された試合のチケットは、販売を終了いたしました");
				return "redirect:/ticket/list";
			}
			
		}
		
//		List<Ticket> ticketList = ticketService.getTicketsByGameId(id);
		// 会員ユーザー向け割引の表示
		if(session.getAttribute("loginStatus") != null) {
			model.addAttribute("isUser", true);
			for (Ticket ticket : ticketList) {
				Integer discountRate = ticket.getDiscountRate();
				Integer price = ticket.getSeatTypePrice();
				Integer discountedPrice = price - (price * discountRate / 100);
				ticket.setSeatTypePrice(discountedPrice);
			}
		}
		model.addAttribute("ticketList", ticketList);
//		System.out.println(ticketList);
		 
		return "ticket/detail";
	}

	// 枚数選択画面

	@GetMapping("/purchase/{id}")
	public String purchase(
			@PathVariable Integer id,
			RedirectAttributes rd,
			Model model) throws Exception {
		// 予定枚数終了時の処理
		Ticket ticket = ticketService.getTicketByTicketId(id);
		
		if(ticket.getStockNum() == 0) {
			rd.addFlashAttribute("statusMessage", "選択された席種は、予定枚数を終了いたしました");
			return "redirect:/ticket/detail/{id}";
		}
		
		// 会員ユーザー向け割引の表示
		if(session.getAttribute("loginStatus") != null) {
			Integer discountRate = ticket.getDiscountRate();
			Integer price = ticket.getSeatTypePrice();
			Integer discountedPrice = price - (price * discountRate / 100);
			ticket.setSeatTypePrice(discountedPrice);
		}
		System.out.println(ticket);
		model.addAttribute("ticket", ticket);
		session.setAttribute("ticket", ticket);
		return "ticket/purchase";
	}
	// 選択した枚数を受け取る
	@PostMapping("/purchase/{id}")
	public String purchasePost(
			@PathVariable Integer id,
			@RequestParam("orderNum") Integer orderNum,
			Model model) throws Exception {
		Ticket ticket = (Ticket) session.getAttribute("ticket");
;
		
		ticket.setOrderNum(orderNum);
		model.addAttribute("ticket", ticket);

		session.setAttribute("ticket", ticket);

				// セッションの中身確認
//				Ticket t = (Ticket) session.getAttribute("ticket");
//				System.out.println(t);

		return "redirect:/ticket/purchaseConf";
	}

	// 購入確認ボタン

	@GetMapping("/purchaseConf")
	public String purchaseConf(
			Model model) {
		Ticket ticket = (Ticket) session.getAttribute("ticket");

		model.addAttribute("ticket", ticket);

		Integer price = ticket.getSeatTypePrice();
		Integer total = price * ticket.getOrderNum();
		model.addAttribute("total", total);

		Order order = new Order();
		order.setTicketId(ticket.getId());
		order.setPrice(ticket.getSeatTypePrice());
		order.setPurchasedNum(ticket.getOrderNum());
		order.setTotal((Integer) model.getAttribute("total"));

		session.setAttribute("order", order);

		// セッションの中身確認
		Order order1 = (Order) session.getAttribute("order");
		System.out.println(order1);
		return "ticket/purchaseConf";
	}

	// 注文者情報入力画面

	@GetMapping("/orderRegister")
	public String orderRegisterGet(Model model) throws Exception {
		Order order = new Order();
		
		// セッション(ユーザーID含む)を取得して、会員情報をフォームに表示
		LoginStatus loginStatus = new LoginStatus();
		if(session.getAttribute("loginStatus") != null) {
			loginStatus = (LoginStatus) session.getAttribute("loginStatus");
			String loginId = loginStatus.getLoginId();
//			System.out.println(loginId);
			User user = userService.getUserByLoginId(loginId);
			session.setAttribute("user", user);
//			System.out.println(user);
			
			order.setUserId(user.getId());
			order.setName(user.getName());
			order.setEmail(user.getLoginId());
			order.setZipcode(user.getZipcode());
			order.setAddress(user.getAddress());
			
		}
		
		model.addAttribute("order", order);
		model.addAttribute("paymentTypeList", ticketService.getPaymentTypeList());
		return "ticket/orderRegister";
	}

	@PostMapping("/orderRegister")
	public String orderRegistPost(
			@Valid @ModelAttribute("order") Order order,
			RedirectAttributes rd,
			Errors errors,
			Model model) throws Exception {
		// バリデーションエラーの場合、フォームを再表示
		if (errors.hasErrors()) {
			// エラー内容の捕捉
			List<ObjectError> objList = errors.getAllErrors();
			for (ObjectError obj : objList) {
				System.out.println(obj.toString());
			}
			model.addAttribute("paymentTypeList", ticketService.getPaymentTypeList());
			return "ticket/orderRegister";
		}
		
		Order registerOrder = (Order) session.getAttribute("order");
		
		if(registerOrder == null) {
			rd.addFlashAttribute("statusMessage", "もう一度チケットを選びなおしてください");
			return "redirect:/ticket/list";
		}
		// ユーザーログイン済みか判定(ログアウトを表示するため)
		model.addAttribute("isUser", false);
		if(session.getAttribute("user") != null) {
			User user = (User) session.getAttribute("user");
			model.addAttribute("isUser",true);
			
			registerOrder.setUserId(user.getId());
		}
		
		// DBに登録するデータ生成
		registerOrder.setName(order.getName());
		registerOrder.setEmail(order.getEmail());
		registerOrder.setZipcode(order.getZipcode());
		registerOrder.setAddress(order.getAddress());
		registerOrder.setPayment(order.getPayment());
		registerOrder.setPurchasedAt(LocalDateTime.now().withNano(0));
		
		// 引く前に在庫数を確認
		Ticket checkedTicket = ticketService.checkStockNum(registerOrder.getTicketId());
		Integer checkedStockNum = (Integer) checkedTicket.getStockNum();
		Ticket orderTicket = (Ticket) session.getAttribute("ticket");
		Integer orderNum = (Integer) orderTicket.getOrderNum();
		
		if ( orderNum > checkedStockNum ) {
			rd.addFlashAttribute("statusMessage", "選択されたチケットは、予定枚数を終了いたしました");
			return "redirect:/ticket/list";
		}
		
		// 在庫数から購入個数を引く
		orderTicket.setStockNum(checkedStockNum -= orderNum) ;
		System.out.println(orderTicket);
		
		// 購入枚数だけ減算した在庫数をDB(ticketsテーブル)登録
		ticketService.subOrderNumFromStockNum(orderTicket);
		// 注文情報をDB登録
		ticketService.addOrder(registerOrder);
		System.out.println(registerOrder);
		
		session.removeAttribute("ticket");
		session.removeAttribute("order");
		return "ticket/purchaseDone";
	}

	

}
