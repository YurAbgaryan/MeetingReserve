package com.awasome.meetingreserve;

/**
 * Created by yuri on 6/8/18.
 */

import com.google.gson.JsonObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

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

	@Multipart
	@POST("/image")
	Call<Void> postImage(@Part MultipartBody.Part filePart);
}