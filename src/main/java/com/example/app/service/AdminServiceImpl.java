package com.example.app.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.Order;
import com.example.app.domain.OrderDetail;
import com.example.app.domain.User;
import com.example.app.domain.UserRole;
import com.example.app.mapper.OrderMapper;
import com.example.app.mapper.UserMapper;
import com.example.app.mapper.UserRoleMapper;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(rollbackFor = Exception.class)
@RequiredArgsConstructor
public class AdminServiceImpl implements AdminService {

	private final UserMapper userMapper;
	private final UserRoleMapper userRoleMapper;
	private final OrderMapper orderMapper;
	
	@Override
	public List<User> getUserList() throws Exception {
		return userMapper.selectAll();
	}

	@Override
	public void addAdmin(User user) throws Exception {
		userMapper.insertAdmin(user);
		
	}

	@Override
	public List<Order> getOrdersList() throws Exception {
		return orderMapper.selectSummaryAll();
	}

	@Override
	public OrderDetail getOrderDetail(Integer id) throws Exception {
		return orderMapper.selectOrderDetailByOrderId(id);
	}

	@Override
	public List<UserRole> getUserRoleList() throws Exception {
		return userRoleMapper.selectAll();
	}

}
