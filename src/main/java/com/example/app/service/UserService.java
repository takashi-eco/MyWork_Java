package com.example.app.service;

import java.util.List;

import com.example.app.domain.History;
import com.example.app.domain.User;

public interface UserService {

	User getUserByLoginId(String logingId) throws Exception;
	User getUserByUserId(Integer id) throws Exception;
	List<History> getOrderListByUserId(Integer id) throws Exception;
}
