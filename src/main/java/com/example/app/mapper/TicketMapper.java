package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.app.domain.Ticket;

public interface TicketMapper {

	List<Ticket> selectAllGames() throws Exception;
	String selectSaleStatus() throws Exception;
	List<Ticket> selectByGameId(Integer id) throws Exception;
	Ticket selectByTicketId(Integer id) throws Exception;
	List<String> selectStockStatusByTicketId(Integer id);
	void updateStockNum(Ticket ticket) throws Exception;
	Ticket selectStockNumByticketId(Integer id) throws Exception;
	// ページ分割機能用
	Long count() throws Exception;
	List<Ticket> selectLimitedGames(@Param("offset") int offset, 
							   @Param("limit") int limit) throws Exception;
}
