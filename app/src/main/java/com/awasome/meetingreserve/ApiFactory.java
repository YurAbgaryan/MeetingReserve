package com.awasome.meetingreserve;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by yuri on 6/8/18.
 */

public class ApiFactory {
	public static final String BASE_URL = "http://192.168.40.207:8080/";
	private static ApiFactory instance;

	public static ApiFactory getInstance() {
		if (instance == null) {
			instance = new ApiFactory();
		}

		return instance;
	}

	public Retrofit getRetrofit(String baseUrl) {
		Retrofit retrofit = new Retrofit.Builder()
				.baseUrl(baseUrl)
				.client(getHTTPClient())
				.addConverterFactory(GsonConverterFactory.create())
				.build();

		return retrofit;
	}

	private OkHttpClient getHTTPClient() {
		return new OkHttpClient.Builder()
				.connectTimeout(20, TimeUnit.SECONDS)
				.writeTimeout(10, TimeUnit.SECONDS)
				.readTimeout(20, TimeUnit.SECONDS)
				.build();
	}
}