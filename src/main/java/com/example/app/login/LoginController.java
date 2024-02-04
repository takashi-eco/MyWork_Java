package com.example.app.login;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.example.app.domain.Login;
import com.example.app.domain.User;
import com.example.app.service.UserService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class LoginController {

	private final UserService userService;
	private final HttpSession session;
	
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute(new Login());
		return "login";
	}
	
	@PostMapping("/login")
	public String login(
			@Valid Login login,
			Errors errors
			) throws Exception {
		if(errors.hasErrors()) {
			return "login";
		}

		// 正しいIDとパスワードの組み合わせか確認
		User user = userService.getUserByLoginId(login.getLoginId());
		if(user == null || !login.isCorrectPassword(user.getLoginPass())) {
			errors.rejectValue("loginId", "error.incorrect_id_or_password");
			return "login";
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
		
		return "redirect:/user/menu";
		
	}
	
	@GetMapping("/logout")
	public String logout(
			RedirectAttributes ra) {
		session.removeAttribute("loginStatus");
		ra.addFlashAttribute("message", "ログアウトしました。");
		return "redirect:/login";
	}
}









