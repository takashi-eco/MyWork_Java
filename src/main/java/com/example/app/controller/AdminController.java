package com.example.app.controller;

import java.util.List;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.User;
import com.example.app.service.AdminService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

	private final AdminService service;
	private final HttpSession session;
	
	// 管理者ホームの表示
	@GetMapping({ "/", "/menu"})
	public String menu() {
		return "admin/menu";
	}
	
	@GetMapping("/logout")
	public String logout(
			RedirectAttributes ra) {
		session.removeAttribute("loginStatus");
		ra.addFlashAttribute("message", "ログアウトしました。");
		return "redirect:/login";
	}
	
	// 会員一覧の表示
	@GetMapping("/users")
	public String userslist(Model model) throws Exception {
		
		model.addAttribute("users", service.getUserList());
		
		return "admin/users";
	}
	
	// 管理者登録フォームの表示
	@GetMapping("/addAdmin")
	public String add(Model model) throws Exception {
		User user = new User();
		model.addAttribute("user", user);
		
		
		return "admin/addAdmin";
	}
	
	// 管理者登録
	@PostMapping("/addAdmin")
	public String add(
			@Valid User user,
			RedirectAttributes ra,
			Errors errors,
			Model model
			) throws Exception {
		if(errors.hasErrors()) {
			// エラー内容の捕捉
			List<ObjectError> objList = errors.getAllErrors();
				for (ObjectError obj : objList) {
					System.out.println(obj.toString());
			}
			
			return "admin/addAdmin";
		}
		
		String loginPass = user.getLoginPass();
		String hashedLoginPass = BCrypt.hashpw(loginPass, BCrypt.gensalt());
		
		user.setLoginPass(hashedLoginPass);
		service.addAdmin(user);
		System.out.println(user);
		ra.addFlashAttribute("message", "管理者登録を完了しました");
		return "redirect:/admin/menu";
	}
	
	// 注文履歴一覧の表示
	@GetMapping("/ordersList")
	public String ordersList(Model model) throws Exception {
		model.addAttribute("ordersList", service.getOrdersList());
		return "admin/ordersList";
	}
	
	// 注文詳細の表示
	@GetMapping("/orderDetail/{id}")
	public String orderDetail(
			@PathVariable Integer id,
			Model model) throws Exception {
		model.addAttribute("orderDetail", service.getOrderDetail(id));
		return "admin/orderDetail";
	}
	
	
	// 試合日程追加
	@GetMapping("/editGame")
	public String editGameGet(
			Model model
			) {
		
		return "admin/editGame";
	}
}





