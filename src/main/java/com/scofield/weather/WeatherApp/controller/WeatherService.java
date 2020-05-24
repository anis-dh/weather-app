package com.scofield.weather.WeatherApp.controller;

import java.io.IOException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import elemental.json.JsonException;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

@Service
public class WeatherService {

	private OkHttpClient client;
	private Response response;
	private String cityName;
	private String unit;

	public JSONObject getWeather() throws JsonException {

		client = new OkHttpClient();
		Request request = new Request.Builder().url("https://api.openweathermap.org/data/2.5/weather?q=" + getCityName()
				+ "&units=" + getUnit() + "&appid=2402e1f424788f996bae75359122b2dc").build();
		try {
			response = client.newCall(request).execute();
			return new JSONObject(response.body().string());
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public JSONArray returnWeatherArray() {
		JSONArray weatherJsonArray = getWeather().getJSONArray("weather");
		return weatherJsonArray;
	}

	public JSONObject returnMainObject() {
		return getWeather().getJSONObject("main");
	}

	public JSONObject returnWindObject() {
		return getWeather().getJSONObject("wind");
	}

	public JSONObject returnSunSet() {
		return getWeather().getJSONObject("sys");
	}

	private String getCityName() {
		return cityName;
	}

	public void setCityName(String cityName) {
		this.cityName = cityName;
	}

	private String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

}
