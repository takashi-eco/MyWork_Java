package com.example.app.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.app.domain.History;
import com.example.app.domain.User;
import com.example.app.mapper.OrderMapper;
import com.example.app.mapper.UserMapper;

@Service
@Transactional
public class UserServiceImpl implements UserService {

	@Autowired
	UserMapper userMapper;
	
	@Autowired
	OrderMapper orderMapper;
	
	@Override
	public User getUserByLoginId(String logingId) throws Exception {
		return userMapper.selectByLoginId(logingId);
	}

	@Override
	public User getUserByUserId(Integer id) throws Exception {
		return userMapper.selectByUserId(id);
	}

	@Override
	public List<History> getOrderListByUserId(Integer id) throws Exception {
		return orderMapper.selectOrderListByUserId(id);
	}

}
