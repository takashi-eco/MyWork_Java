package com.example.app.domain;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class Game {

	// 試合id
	private int id;

	// 試合の日付
	@NotBlank
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDateTime gameDate;

	// 対戦チーム名
	private String vsTeamName;

	// 販売開始日
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate saleStartDate;

	// 販売終了日
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate saleEndDate;

	// 販売状況ステータス
	private String dateStatus;
	
	// 子クラスのリスト
	private List<Ticket> tickets;
}
