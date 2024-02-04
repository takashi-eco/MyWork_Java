package com.example.app.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;

import lombok.Data;

@Data
public class History {

	private Integer id;
	private String userId;
	private LocalDate gameDate;
	private String vsTeamName;
	private String seatTypeName;
	private Integer purchasedNum;
	private Integer price;
	private Integer total;
	private Integer payment;
	private LocalDateTime purchasedAt;
	
}
