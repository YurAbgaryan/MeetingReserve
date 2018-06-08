package com.awasome.meetingreserve;

/**
 * Created by yuri on 6/8/18.
 */

import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.*;

public interface ServerInterface {

	@Headers("Content-Type: application/json")
	@FormUrlEncoded
	@POST("/reserve")
	Call<JsonObject> reserveRoom(
			@Field("email") String email,
			@Field("start_time") String start_time,
			@Field("end_time") String end_time,
			@Field("participants") ArrayList<String> participants
	);

	@Headers("Content-Type: application/json")
	@POST("/reserve")
	Call<JsonObject> reserveRoom(@retrofit2.http.Body JsonObject body);

	@Headers("Content-Type: application/json")
	@POST("/signin")
	Call<JsonObject> sendToken(@retrofit2.http.Body JsonObject body);
}