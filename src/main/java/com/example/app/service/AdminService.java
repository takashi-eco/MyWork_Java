package com.example.app.service;

import java.util.List;

import com.example.app.domain.Order;
import com.example.app.domain.OrderDetail;
import com.example.app.domain.User;
import com.example.app.domain.UserRole;

public interface AdminService {

	List<User> getUserList() throws Exception;
	void addAdmin(User user) throws Exception;
	List<Order> getOrdersList() throws Exception;
	OrderDetail getOrderDetail(Integer id) throws Exception;
	List<UserRole> getUserRoleList() throws Exception;
}
