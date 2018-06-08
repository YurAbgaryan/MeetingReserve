package com.awasome.meetingreserve;

import android.content.Context;
import android.util.Log;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.objdetect.CascadeClassifier;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

class FaceDetectorHelper {
	private static final String TAG = FaceDetectorHelper.class.getSimpleName();
	private CascadeClassifier face_cascade;
	private static final int BUFFER_SIZE = 8 * 1024;
	private MatOfRect faces = new MatOfRect();

	public FaceDetectorHelper(Context context) {
		File faceCascadeFile = new File(context.getFilesDir(), "haarcascade_frontalface_default.xml");
		if (!faceCascadeFile.exists()) {
			copyFileFromAssets(context, faceCascadeFile, "haarcascade_frontalface_default.xml");
		}
		face_cascade = new CascadeClassifier(faceCascadeFile.getAbsolutePath());
		if (face_cascade.empty()) {
			System.out.println("--(!)Error loading A\n");
		} else {
			System.out.println("Face classifier loooaaaaaded up");
		}
	}

	public static void copyFileFromAssets(Context context, File internalStoragePath, String fileName) {
		BufferedInputStream bufferedInputStream = null;
		OutputStream dexWriter = null;
		if (!internalStoragePath.exists()) {
			internalStoragePath.getParentFile().mkdirs();
			try {
				internalStoragePath.createNewFile();
			} catch (IOException e) {
				Log.e(TAG, e.toString());
			}
		}
		try {
			bufferedInputStream = new BufferedInputStream(context.getAssets().open(fileName));
			dexWriter = new BufferedOutputStream(new FileOutputStream(internalStoragePath));
			byte[] buf = new byte[BUFFER_SIZE];
			int len;
			while ((len = bufferedInputStream.read(buf, 0, BUFFER_SIZE)) > 0) {
				dexWriter.write(buf, 0, len);
			}
			dexWriter.flush();
		} catch (IOException e) {
			Log.e(TAG, e.toString());
		} finally {
			if (dexWriter != null) {
				try {
					dexWriter.close();
				} catch (IOException ex) {
					Log.e(TAG, ex.toString());
				}
			}
			if (bufferedInputStream != null) {
				try {
					bufferedInputStream.close();
				} catch (IOException ex) {
					Log.e(TAG, ex.toString());
				}
			}
		}
	}

	public int detect(Mat inputFrame) {
		if(faces == null){
			faces = new MatOfRect();
		}
		face_cascade.detectMultiScale(inputFrame, faces);
		return faces.toArray().length;
	}
}