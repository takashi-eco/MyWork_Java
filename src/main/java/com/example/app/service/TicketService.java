package com.example.app.service;

import java.util.List;

import com.example.app.domain.Order;
import com.example.app.domain.PaymentType;
import com.example.app.domain.Ticket;

public interface TicketService {

	// チケットリスト表示
	List<Ticket> getGamesList() throws Exception;
	// チケット席種選択リスト
	List<Ticket> getTicketsByGameId(Integer id) throws Exception;
	
	// チケット枚数選択
	Ticket getTicketByTicketId(Integer id) throws Exception;
	List<PaymentType> getPaymentTypeList() throws Exception;
	
	// 確定注文を登録
	void addOrder(Order order) throws Exception;
	// 購入枚数分を在庫数から減算
	void subOrderNumFromStockNum(Ticket ticket) throws Exception;
	Ticket checkStockNum(Integer id) throws Exception;
	// ページ分割機能用
	List<Ticket> getGamesListByPage(int page, int numPerPage) throws Exception;
	int getTotalPages(int numPerPage) throws Exception;
	
	// 天気を問い合わせる
	void askWeather() throws Exception;
}
