package com.awasome.meetingreserve;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by yuri on 6/8/18.
 */

public class MainActivity extends AppCompatActivity {
	public static final int RC_SIGN_IN = 123;
	public static final int RC_CAMERA = 111;
	public static final String TAG = "lalala";
	public static String userToken = null;

	private FirebaseAuth mAuth;
	private GoogleSignInOptions gso;

	private Button signOutBtn;
	private Intent signInIntent;

	private static String EMAIL = "email";
	private static String START_TIME = "start_time";
	private static String END_TIME = "end_time";
	private static String PARTICIPANTS = "participants";
	private static String LOCATION = "location";

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		if (GoogleSignIn.getLastSignedInAccount(this) != null) {
			startActivity(new Intent(this, MainFlowActivity.class));
		}
		findViewById(R.id.btn_action).setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				startActivity(new Intent(MainActivity.this, MainFlowActivity.class));
			}
		});

		gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestScopes(new Scope(Scopes.DRIVE_APPFOLDER))
				.requestServerAuthCode(getResources().getString(R.string.web_client_id))
				.requestEmail()
				.build();

		if (GoogleSignIn.getLastSignedInAccount(this) == null) {
			Log.d(TAG, "user not signin");
			startActivityForResult(new Intent(this, RegisterActivity.class), RC_SIGN_IN);
		}

		signOutBtn = findViewById(R.id.sign_out_btn);
		signOutBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				//mAuth.signOut();

				GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);
				mGoogleSignInClient.signOut()
						.addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
							@Override
							public void onComplete(@NonNull Task<Void> task) {
								Log.d(TAG, "user signed out");
								SharedPreferences.Editor editor = getSharedPreferences("mypref", MODE_PRIVATE).edit();
								editor.putString("uid", null).apply();
								startActivityForResult(new Intent(MainActivity.this, CameraActivity.class), RC_SIGN_IN);
							}
						});
			}
		});
		ArrayList<String> participants = new ArrayList<>();
		participants.add("sdasd");
		participants.add("sdasd");
		participants.add("sdasd");
		participants.add("sdasd");

		SharedPreferences editor = getSharedPreferences("mypref", MODE_PRIVATE);
		String uid = editor.getString("uid", null);
		String auth = editor.getString("auth", null);


		ServerInterface serverInterface = ApiService.getInstance().getServiceInterface();
		Log.d(TAG, "Uid: " + uid);
		RequestBody requestBody = new RequestBody.Builder()
				.setEmail("yuri.abgaryan@picsart.com")
				.setEndTime(System.currentTimeMillis())
				.setStartTime(System.currentTimeMillis())
				.setParticipants(participants)
				.setLocation("Van Gogh")
				.setId(uid)
				.setToken(auth)
				.build();

		Gson gson = new Gson();
		JsonObject object = gson.toJsonTree(requestBody).getAsJsonObject();


		Call<JsonObject> call = serverInterface.reserveRoom(object);
		Log.d(TAG, call.request() + "   " + call.request().body().toString());
		call.enqueue(new Callback<JsonObject>() {
			@Override
			public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
				if (response != null) {
					Log.d(TAG, "resonse: " + response + response.toString());
				}
			}

			@Override
			public void onFailure(Call<JsonObject> call, Throwable t) {
				Log.d(TAG, "failed: " + t.getMessage());
			}
		});

	}

	@Override
	protected void onStart() {
		super.onStart();
	}

//	private void openSignInActivity() {
//		List<AuthUI.IdpConfig> providers = Arrays.asList(
//				new AuthUI.IdpConfig.EmailBuilder().build(),
//				new AuthUI.IdpConfig.GoogleBuilder().build());
//
//		signInIntent = AuthUI.getInstance()
//				.createSignInIntentBuilder()
//				.setAvailableProviders(providers)
//				.setLogo(R.drawable.group)
//				.setTheme(R.style.Theme_Material_Gradient)
//				.build();
//
//		startActivityForResult(signInIntent, RC_SIGN_IN);
//	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

			IdpResponse response = IdpResponse.fromResultIntent(data);
			Log.d(TAG, "response Signin");

			if (requestCode == RC_SIGN_IN) {
				if (GoogleSignIn.getLastSignedInAccount(this) != null) {
					startActivityForResult(new Intent(this, CameraActivity.class), RC_CAMERA);
				}
//				Log.d(TAG, "signinSuceed");
//				// Successfully signed in
//				FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
//				if (user != null) {
//					user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
//						@Override
//						public void onComplete(@NonNull Task<GetTokenResult> task) {
//							if (task.isSuccessful()) {
//								userToken = task.getResult().getToken();
//								Log.d(TAG, userToken);
//								sendUserToken();
//								startActivityForResult(new Intent(MainActivity.this, CameraActivity.class), RC_CAMERA);
//								Log.d(TAG, "autorization successed");
//							} else {
//								Log.d(TAG, "autorization failed");
//							}
//						}
//					});
//				}

			} else if (requestCode == RC_CAMERA) {
				Toast.makeText(this, "URAAAAAAAAAA", Toast.LENGTH_SHORT);
			}
				Log.d("lalala", "signin failed");
	}

	private void sendUserToken() {
		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		RequestBody body = new RequestBody.Builder()
				.setEmail(user.getEmail())
				.setToken(userToken)
				.build();

		ServerInterface serverInterface = ApiService.getInstance().getServiceInterface();
		Gson gson = new Gson();
		JsonObject object = gson.toJsonTree(body).getAsJsonObject();

		Call<JsonObject> call = serverInterface.sendToken(object);

		call.enqueue(new Callback<JsonObject>() {
			@Override
			public void onResponse(@NonNull Call<JsonObject> call, @NonNull Response<JsonObject> response) {
				Log.d(TAG, response.message() + "   " + response.toString());
			}

			@Override
			public void onFailure(@NonNull Call<JsonObject> call, @NonNull Throwable t) {
				Log.d(TAG, "failed:  " + t.getMessage());
			}
		});
	}
}
