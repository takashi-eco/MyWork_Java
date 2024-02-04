package com.example.app.mapper;

import java.util.List;

import com.example.app.domain.UserRole;

public interface UserRoleMapper {

	List<UserRole> selectAll() throws Exception;
}
