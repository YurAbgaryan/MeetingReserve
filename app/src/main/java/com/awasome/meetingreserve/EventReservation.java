package com.awasome.meetingreserve;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;

/**
 * Created by yuri on 6/8/18.
 */

public class EventReservation extends AppCompatActivity {
	private Button dateBtn;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_event);

		dateBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				pickAndSendDate();
			}
		});
	}

	private void pickAndSendDate() {
		//DatePickerDialog dialog = new DatePickerDialog(this);

	}
}
