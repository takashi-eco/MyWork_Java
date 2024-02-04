package com.example.app.mapper;

import java.util.List;

import com.example.app.domain.History;

public interface HistoryMapper {

	List<History> selectAll() throws Exception;
}
