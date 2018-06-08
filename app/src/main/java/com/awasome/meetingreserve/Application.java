package com.awasome.meetingreserve;

public class Application  extends android.app.Application{
	@Override
	public void onCreate() {
		super.onCreate();
		System.loadLibrary("opencv_java4");
	}
}
