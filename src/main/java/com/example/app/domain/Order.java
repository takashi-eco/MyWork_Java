package com.example.app.domain;

import java.time.LocalDateTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class Order {

	// 注文ID
	private int id;
	
	private Integer userId;
	
	@NotBlank
	@Size(max = 10)
	private String name;
	
	@NotBlank
	@Email
	@Size(max = 100)
	private String email;
	
	@NotBlank
	@Size(min=7, max=7)
	private String zipcode;
	
	@NotBlank
	@Size(max=255)
	private String address;
	
	private int ticketId;
	
	private int price;
	
	private Integer purchasedNum;
	
	private Integer total;
	
	private int payment;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime purchasedAt;
	
}




