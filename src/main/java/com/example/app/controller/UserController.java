package com.example.app.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.example.app.domain.History;
import com.example.app.domain.User;
import com.example.app.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

	private final UserService userService;
	
	
	@GetMapping("/menu/{id}")
	public String menu(
			@PathVariable Integer id,
			Model model
			) throws Exception {
		User user = userService.getUserByUserId(id);
		model.addAttribute("userId", user.getId());
		
		return "user/menu";
	}
	
	@GetMapping("/history/{id}")
	public String history (
			@PathVariable Integer id,
			Model model
			) throws Exception {
		model.addAttribute("isUser", true);
		model.addAttribute("userId", id);
		
		List<History> historyList = userService.getOrderListByUserId(id);
		model.addAttribute("historyList", historyList);
		System.out.println(historyList);
		return "user/history";
		
	}
}
