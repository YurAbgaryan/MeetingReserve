package com.awasome.meetingreserve;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

/**
 * Created by yuri on 6/8/18.
 */

public class RegisterActivity extends AppCompatActivity{
	public static final int RESULT_CODE_SIGN_IN = 33;
	public static final int REQUEST_CODE_SIGN_IN = 22;
	private Button signIn;
	private GoogleSignInClient mGoogleSignInClient;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestServerAuthCode(getResources().getString(R.string.web_client_id))
				.requestEmail()
				.build();

		mGoogleSignInClient = GoogleSignIn.getClient(this, gso);
		signIn = findViewById(R.id.sign_in_btn);
		signIn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent signInIntent = mGoogleSignInClient.getSignInIntent();
				startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
			}
		});

		Log.d(MainActivity.TAG, "oncreate");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == REQUEST_CODE_SIGN_IN) {
			// The Task returned from this call is always completed, no need to attach
			// a listener.
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
			handleSignInResult(task);
		}
	}

	private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
		int resultCode = 33;
		try {
			GoogleSignInAccount account = completedTask.getResult(ApiException.class);
			String authCode = account.getServerAuthCode();

			// Signed in successfully, show authenticated UI.
			Log.d(MainActivity.TAG, "SIngn in success");
			String uid = UUID.randomUUID().toString();
			SharedPreferences.Editor editor = getSharedPreferences("mypref", MODE_PRIVATE).edit();
			editor.putString("uid", uid).apply();
			editor.putString("auth", authCode).apply();
			sendAuthCodeToBackEnd(authCode, uid);


		} catch (ApiException e) {
			resultCode = 22;
			// The ApiException status code indicates the detailed failure reason.
			// Please refer to the GoogleSignInStatusCodes class reference for more information.
			Log.w(MainActivity.TAG, "signInResult:failed code=" + e.getStatusCode());
		}
		finally {
			finish();
		}

	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(MainActivity.TAG, "Register destroyed");
	}

	private void sendAuthCodeToBackEnd(String authCode, String uid) {
		Log.d(MainActivity.TAG, authCode);
		RequestBody body = new RequestBody.Builder()
				.setEmail("blah@mail.ru")
				.setId(uid)
				.setToken(authCode)
				.build();

		ServerInterface serverInterface = ApiService.getInstance().getServiceInterface();
		Gson gson = new Gson();
		JsonObject object = gson.toJsonTree(body).getAsJsonObject();

		Call<JsonObject> call = serverInterface.sendToken(object);

		call.enqueue(new Callback<JsonObject>() {
			@Override
			public void onResponse(Call<JsonObject> call, Response<JsonObject> response) {
				Log.d(MainActivity.TAG, response.message() + "   " + response.toString());
			}

			@Override
			public void onFailure(Call<JsonObject> call, Throwable t) {
				Log.d(MainActivity.TAG, "failed:  " + t.getMessage());
			}
		});

	}
}
