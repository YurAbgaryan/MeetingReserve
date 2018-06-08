package com.awasome.meetingreserve;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.YuvImage;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.util.SparseArray;
import android.view.View;

import com.awasome.meetingreserve.camera.CameraSourcePreview;
import com.awasome.meetingreserve.camera.GraphicOverlay;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.Detector;
import com.google.android.gms.vision.Frame;
import com.google.android.gms.vision.face.Face;
import com.google.android.gms.vision.face.FaceDetector;

import org.opencv.android.Utils;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.imgcodecs.Imgcodecs;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.UUID;

import static org.opencv.imgcodecs.Imgcodecs.CV_IMWRITE_PAM_FORMAT_GRAYSCALE;

public class CameraActivity extends AppCompatActivity {
	private static final String TAG = "FaceTracker";

	private CameraSource mCameraSource = null;

	private CameraSourcePreview mPreview;
	private GraphicOverlay mGraphicOverlay;
	File folder;

	private static final int RC_HANDLE_GMS = 9001;
	// permission request codes need to be < 256
	private static final int RC_HANDLE_CAMERA_PERM = 2;

	//==============================================================================================
	// Activity Methods
	//==============================================================================================

	/**
	 * Initializes the UI and initiates the creation of a face detector.
	 */
	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);
		setContentView(R.layout.activity_camera);
		folder = new File(getApplicationContext().getFilesDir(), "faces");
		if (!folder.exists()) {
			folder.mkdirs();
		}

		mPreview = findViewById(R.id.preview);
		mGraphicOverlay = findViewById(R.id.faceOverlay);

		// Check for the camera permission before accessing the camera.  If the
		// permission is not granted yet, request permission.
		int rc = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA);
		if (rc == PackageManager.PERMISSION_GRANTED) {
			createCameraSource();
		} else {
			requestCameraPermission();
		}
	}

	/**
	 * Handles the requesting of the camera permission.  This includes
	 * showing a "Snackbar" message of why the permission is needed then
	 * sending the request.
	 */
	private void requestCameraPermission() {
		Log.w(TAG, "Camera permission is not granted. Requesting permission");

		final String[] permissions = new String[]{Manifest.permission.CAMERA};

		if (!ActivityCompat.shouldShowRequestPermissionRationale(this,
				Manifest.permission.CAMERA)) {
			ActivityCompat.requestPermissions(this, permissions, RC_HANDLE_CAMERA_PERM);
			return;
		}

		final Activity thisActivity = this;

		View.OnClickListener listener = new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				ActivityCompat.requestPermissions(thisActivity, permissions,
						RC_HANDLE_CAMERA_PERM);
			}
		};

		Snackbar.make(mGraphicOverlay, R.string.permission_camera_rationale,
				Snackbar.LENGTH_INDEFINITE)
				.setAction(R.string.ok, listener)
				.show();
	}

	/**
	 * Creates and starts the camera.  Note that this uses a higher resolution in comparison
	 * to other detection examples to enable the barcode detector to detect small barcodes
	 * at long distances.
	 */
	private void createCameraSource() {

		Context context = getApplicationContext();
		// You can use your own settings for your detector
		FaceDetector detector = new FaceDetector.Builder(context)
				.setClassificationType(FaceDetector.ALL_CLASSIFICATIONS)
				.setProminentFaceOnly(true)
				.build();

		// This is how you merge myFaceDetector and google.vision detector
		MyFaceDetector myFaceDetector = new MyFaceDetector(detector, new Runnable() {
			@Override
			public void run() {
				mCameraSource.release();
				finish();
			}
		});


		if (!detector.isOperational()) {
			// Note: The first time that an app using face API is installed on a device, GMS will
			// download a native library to the device in order to do detection.  Usually this
			// completes before the app is run for the first time.  But if that download has not yet
			// completed, then the above call will not detect any faces.
			//
			// isOperational() can be used to check if the required native library is currently
			// available.  The detector will automatically become operational once the library
			// download completes on device.
			Log.w(TAG, "Face detector dependencies are not yet available.");
		}

		mCameraSource = new CameraSource.Builder(context, myFaceDetector)
				.setRequestedPreviewSize(640, 480)
				.setFacing(CameraSource.CAMERA_FACING_FRONT)
				.setAutoFocusEnabled(true)
				.setRequestedFps(30.0f)
				.build();
	}

	/**
	 * Restarts the camera.
	 */
	@Override
	protected void onResume() {
		super.onResume();

		startCameraSource();
	}

	/**
	 * Stops the camera.
	 */
	@Override
	protected void onPause() {
		super.onPause();
		mPreview.stop();
	}

	/**
	 * Releases the resources associated with the camera source, the associated detector, and the
	 * rest of the processing pipeline.
	 */
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (mCameraSource != null) {
			mCameraSource.release();
		}
	}

	/**
	 * Callback for the result from requesting permissions. This method
	 * is invoked for every call on {@link #requestPermissions(String[], int)}.
	 * <p>
	 * <strong>Note:</strong> It is possible that the permissions request interaction
	 * with the user is interrupted. In this case you will receive empty permissions
	 * and results arrays which should be treated as a cancellation.
	 * </p>
	 *
	 * @param requestCode  The request code passed in {@link #requestPermissions(String[], int)}.
	 * @param permissions  The requested permissions. Never null.
	 * @param grantResults The grant results for the corresponding permissions
	 *                     which is either {@link PackageManager#PERMISSION_GRANTED}
	 *                     or {@link PackageManager#PERMISSION_DENIED}. Never null.
	 * @see #requestPermissions(String[], int)
	 */
	@Override
	public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
		if (requestCode != RC_HANDLE_CAMERA_PERM) {
			Log.d(TAG, "Got unexpected permission result: " + requestCode);
			super.onRequestPermissionsResult(requestCode, permissions, grantResults);
			return;
		}

		if (grantResults.length != 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
			Log.d(TAG, "Camera permission granted - initialize the camera source");
			// we have permission, so create the camerasource
			createCameraSource();
			return;
		}

		Log.e(TAG, "Permission not granted: results len = " + grantResults.length +
				" Result code = " + (grantResults.length > 0 ? grantResults[0] : "(empty)"));

		DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int id) {
				finish();
			}
		};

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Face Tracker sample")
				.setMessage(R.string.no_camera_permission)
				.setPositiveButton(R.string.ok, listener)
				.show();
	}

	//==============================================================================================
	// Camera Source Preview
	//==============================================================================================

	/**
	 * Starts or restarts the camera source, if it exists.  If the camera source doesn't exist yet
	 * (e.g., because onResume was called before the camera source was created), this will be called
	 * again when the camera source is created.
	 */
	private void startCameraSource() {

		// check that the device has play services available.
		int code = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(
				getApplicationContext());
		if (code != ConnectionResult.SUCCESS) {
			Dialog dlg =
					GoogleApiAvailability.getInstance().getErrorDialog(this, code, RC_HANDLE_GMS);
			dlg.show();
		}

		if (mCameraSource != null) {
			try {
				mPreview.start(mCameraSource, mGraphicOverlay);
			} catch (IOException e) {
				Log.e(TAG, "Unable to start camera source.", e);
				mCameraSource.release();
				mCameraSource = null;
			}
		}
	}

	//==============================================================================================
	// Graphic Face Tracker
	//==============================================================================================

	class MyFaceDetector extends Detector<Face> {
		private Detector<Face> mDelegate;
		private FaceDetectorHelper helper;
		private Mat currentMat = new Mat();
		private Handler handler;
		private Runnable callback;

		MyFaceDetector(Detector<Face> delegate, Runnable callback) {
			mDelegate = delegate;
			helper = new FaceDetectorHelper(getApplicationContext());
			HandlerThread thread = new HandlerThread("cool thread", Thread.NORM_PRIORITY);
			thread.start();
			handler = new Handler(thread.getLooper());
			this.callback = callback;
		}

		@Override
		public SparseArray<Face> detect(final Frame frame) {
			YuvImage yuvImage = new YuvImage(frame.getGrayscaleImageData().array(), ImageFormat.NV21, frame.getMetadata().getWidth(), frame.getMetadata().getHeight(), null);
			ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
			yuvImage.compressToJpeg(new Rect(0, 0, frame.getMetadata().getWidth(), frame.getMetadata().getHeight()), 100, byteArrayOutputStream);
			byte[] jpegArray = byteArrayOutputStream.toByteArray();
			final Bitmap frameBitmap = BitmapFactory.decodeByteArray(jpegArray, 0, jpegArray.length);
			handler.post(new Runnable() {
				@Override
				public void run() {
					Matrix matrix = new Matrix();
					matrix.postRotate(90);
					Bitmap rotatedBitmap = Bitmap.createBitmap(frameBitmap, 0, 0, frameBitmap.getWidth(), frameBitmap.getHeight(), matrix, true);
					Utils.bitmapToMat(rotatedBitmap, currentMat);
					if (helper.detect(currentMat) == 1) {
						Imgcodecs.imwrite(new File(folder, UUID.randomUUID() + ".jpg").getAbsolutePath(), currentMat, new MatOfInt(CV_IMWRITE_PAM_FORMAT_GRAYSCALE));
						if (folder.list().length == 21 && callback != null) {
							callback.run();
						}
					}
					Log.d(TAG, "face count" + helper.detect(currentMat));
					handler.removeCallbacksAndMessages(null);
				}
			});

			return mDelegate.detect(frame);
		}

		@Override
		public void setProcessor(Processor<Face> processor) {
			super.setProcessor(processor);
			mDelegate.setProcessor(processor);
		}

		@Override
		public void receiveFrame(Frame frame) {
			super.receiveFrame(frame);
			Log.d(TAG, "recive ");
			mDelegate.receiveFrame(frame);
		}

		public boolean isOperational() {
			return mDelegate.isOperational();
		}

		public boolean setFocus(int id) {
			return mDelegate.setFocus(id);
		}
	}

}