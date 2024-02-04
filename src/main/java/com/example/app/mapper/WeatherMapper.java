package com.example.app.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.example.app.domain.Weather;

public interface WeatherMapper {

	public Weather selectWeatherUpdateAt() throws Exception;

	public void updateWeather(@Param("weatherList") List<Weather> weatherList) throws Exception;

}
