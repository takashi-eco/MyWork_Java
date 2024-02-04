package com.example.app.mapper;

import java.util.List;

import com.example.app.domain.User;

public interface UserMapper {

	List<User> selectAll() throws Exception;
	User selectByLoginId(String loginId) throws Exception;
	void insertAdmin(User user) throws Exception;
	void update(User user) throws Exception;
	void delete(Integer id) throws Exception;
	User selectByUserId(Integer id) throws Exception;
}
