package com.awasome.meetingreserve;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;

import static com.awasome.meetingreserve.MainActivity.RC_SIGN_IN;

/**
 * Created by yuri on 6/8/18.
 */

public class RegisterActivity extends AppCompatActivity{
	public static final int RESULT_CODE_SIGN_IN = 33;
	private Button signIn;
	private GoogleSignInClient mGoogleSignInClient;


	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);

		GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
				.requestIdToken(getString(R.string.default_web_client_id))
				.requestEmail()
				.build();

		mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

		signIn = findViewById(R.id.sign_in_btn);
		signIn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent signInIntent = mGoogleSignInClient.getSignInIntent();
				startActivityForResult(signInIntent, RC_SIGN_IN);
			}
		});

		Log.d(MainActivity.TAG, "oncreate");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (requestCode == RC_SIGN_IN) {
			// The Task returned from this call is always completed, no need to attach
			// a listener.
			Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
			handleSignInResult(task);
		}
	}

	private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
		try {
			GoogleSignInAccount account = completedTask.getResult(ApiException.class);

			// Signed in successfully, show authenticated UI.
			Log.d(MainActivity.TAG, "SIngn in success");
			finish();
		} catch (ApiException e) {
			// The ApiException status code indicates the detailed failure reason.
			// Please refer to the GoogleSignInStatusCodes class reference for more information.
			Log.w(MainActivity.TAG, "signInResult:failed code=" + e.getStatusCode());
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		Log.d(MainActivity.TAG, "Register destroyed");
	}
}
