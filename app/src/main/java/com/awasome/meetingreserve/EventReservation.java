package com.awasome.meetingreserve;

import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by yuri on 6/8/18.
 */

public class EventReservation extends AppCompatActivity {
	private Button saveBtn;
	private EditText datePicker, timePicker;

	@Override
	protected void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reservation);
		saveBtn = findViewById(R.id.save_btn);
		datePicker = findViewById(R.id.date_picker);
		timePicker = findViewById(R.id.time_picker);
		saveBtn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Toast.makeText(EventReservation.this, "Thank you for setting Meeting", Toast.LENGTH_SHORT).show();
			}
		});
	}

	private void pickAndSendDate() {
		//DatePickerDialog dialog = new DatePickerDialog(this);

	}
}
