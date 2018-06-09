package com.awasome.meetingreserve;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.firebase.ui.auth.AuthUI;
import com.firebase.ui.auth.IdpResponse;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GetTokenResult;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by yuri on 6/8/18.
 */

public class MainActivity extends AppCompatActivity {
	public static final int RC_SIGN_IN = 123;
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

		Intent intent = new Intent(this, RoomActivity.class);
		startActivity(intent);

		mAuth = FirebaseAuth.getInstance();
		mAuth.addAuthStateListener(new FirebaseAuth.AuthStateListener() {
			@Override
			public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
				if (firebaseAuth.getCurrentUser() == null) {
					Log.d(TAG, "starting register");
					startActivityForResult(new Intent(MainActivity.this, RegisterActivity.class), RC_SIGN_IN);
				}
			}
		});
		gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();


		signOutBtn = findViewById(R.id.sign_out_btn);
		signOutBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				mAuth.signOut();

				GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(MainActivity.this, gso);
				mGoogleSignInClient.signOut()
						.addOnCompleteListener(MainActivity.this, new OnCompleteListener<Void>() {
							@Override
							public void onComplete(@NonNull Task<Void> task) {
								Log.d(TAG, "user signed out");
							}
						});
			}
		});
		ArrayList<String> participants = new ArrayList<>();
		participants.add("sdasd");
		participants.add("sdasd");
		participants.add("sdasd");
		participants.add("sdasd");


		Retrofit retrofit = ApiFactory.getInstance().getRetrofit(ApiFactory.BASE_URL);
		ServerInterface serverInterface = retrofit.create(ServerInterface.class);

		RequestBody requestBody = new RequestBody.Builder()
				.setEmail("blah@mail.ru")
				.setEndTime(System.currentTimeMillis())
				.setStartTime(System.currentTimeMillis())
				.setParticipants(participants)
				.setLocation("Van Gogh")
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
		FirebaseUser currentUser = mAuth.getCurrentUser();
		if (currentUser == null) {
			//startActivity(new Intent(this, RegisterActivity.class));
		}
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

			if (resultCode == RegisterActivity.RESULT_CODE_SIGN_IN) {
				Log.d(TAG, "signinSuceed");
				// Successfully signed in
				FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
				user.getIdToken(true).addOnCompleteListener(new OnCompleteListener<GetTokenResult>() {
					@Override
					public void onComplete(@NonNull Task<GetTokenResult> task) {
						if (task.isSuccessful()) {
							userToken = task.getResult().getToken();
							Log.d(TAG, userToken);
							sendUserToken();
							Log.d(TAG, "autorization successed");
						} else {
							Log.d(TAG, "autorization failed");
						}
					}
				});

			} else {
				Log.d("lalala", "signin failed");
			}
	}

	private void sendUserToken() {
		FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
		RequestBody body = new RequestBody.Builder()
				.setEmail(user.getEmail())
				.setToken(userToken)
				.build();
		Retrofit retrofit = ApiFactory.getInstance().getRetrofit(ApiFactory.BASE_URL);
		ServerInterface serverInterface = retrofit.create(ServerInterface.class);
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
