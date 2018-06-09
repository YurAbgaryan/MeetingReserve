package com.awasome.meetingreserve;

/**
 * Created by yuri on 6/9/18.
 */

public class RoomModel {
	private String name;
	private boolean status;
	private String eventName;
	private String time;

	public RoomModel(String name, boolean status, String time, String eventName) {
		this.name = name;
		this.status = status;
		this.time = time;
		this.eventName = eventName;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public boolean isStatus() {
		return status;
	}

	public void setStatus(boolean status) {
		this.status = status;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}
}
