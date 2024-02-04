package com.example.app.domain;

import java.time.LocalDateTime;

import lombok.Data;

@Data
public class OrderDetail {

	private String name;
	private String ticketTitle;
	private Integer price;
	private Integer purchasedNum;
	private Integer total;
	private String payment;
	private LocalDateTime purchasedAt;
}
