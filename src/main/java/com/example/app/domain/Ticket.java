package com.example.app.domain;

import java.time.LocalDate;
import java.time.LocalTime;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Ticket {

	// チケットID
	private int id;
	
	private int gameId;
	
	// 試合の日付
	@NotBlank
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate gameDate;
	
	// 試合開始時間
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime startTime;

	// 開場開始時間
	@DateTimeFormat(pattern = "HH:mm")
	private LocalTime openTime;
	
	
	// 対戦チーム名
	@NotBlank
	private String vsTeamName;
	
	// 販売開始日
	@NotBlank
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate saleStartDate;
	
	// 販売終了日
	@NotBlank
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate saleEndDate;
	
	// 在庫数ステータス
	private String stockStatus;
	
	// 販売状況ステータス
	private String saleStatus;
	
	// 割引ステータス
	private String discountName;
	private Integer discountRate;
	
	// 席種
	@NotBlank
	private String seatTypeName;
	
	// 席の価格
	@NotBlank
	private int seatTypePrice;
	
	// 注文枚数
	private Integer orderNum;
	
	// 残り席数
	@NotBlank
	private Integer stockNum;
	
}
