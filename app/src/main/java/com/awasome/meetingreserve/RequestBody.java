package com.awasome.meetingreserve;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by yuri on 6/8/18.
 */

public class RequestBody {
	@SerializedName("email")
	private String email;

	@SerializedName("token")
	private String token;

	@SerializedName("start_time")
	private Long start_time;

	@SerializedName("end_time")
	private Long end_time;

	@SerializedName("participants")
	private ArrayList<String> participants;

	@SerializedName("location")
	private String location;

	public static class Builder {
		private String email;
		private String token;
		private Long start_time;
		private Long end_time;
		private ArrayList<String> participants;
		private String location;

		public Builder() {

		}

		public Builder setEmail(String email) {
			this.email = email;
			return this;
		}

		public Builder setStartTime(Long start_time) {
			this.start_time = start_time;
			return this;
		}

		public Builder setEndTime(Long end_time) {
			this.end_time = end_time;
			return this;
		}

		public Builder setParticipants(ArrayList<String> participants) {
			this.participants = participants;
			return this;
		}

		public Builder setToken(String token) {
			this.token = token;
			return this;
		}

		public Builder setLocation(String location) {
			this.location = location;
			return this;
		}

		public RequestBody build() {
			return new RequestBody(this);
		}
	}

	private RequestBody() {

	}

	private RequestBody(Builder builder) {
		this.email = builder.email;
		this.token = builder.token;
		this.location = builder.location;
		this.start_time = builder.start_time;
		this.end_time = builder.end_time;
		this.participants = builder.participants;
	}
}
