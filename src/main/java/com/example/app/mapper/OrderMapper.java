package com.example.app.mapper;

import java.util.List;

import com.example.app.domain.History;
import com.example.app.domain.Order;
import com.example.app.domain.OrderDetail;

public interface OrderMapper {

	List<Order> selectSummaryAll() throws Exception;
	void insert(Order order) throws Exception;
	OrderDetail selectOrderDetailByOrderId(Integer id) throws Exception;
	List<History> selectOrderListByUserId(Integer id) throws Exception;
}
