package com.example.itineraryapp;

import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.example.itineraryapp.db.DatabaseHelper;
import com.example.itineraryapp.models.ActivityModel;
import com.google.android.material.textfield.TextInputEditText;

import java.util.Calendar;
import java.util.Locale;

public class AddActivityActivity extends AppCompatActivity {

    private int tripId;
    private TextInputEditText etTitle, etTime, etNotes;
    private Button btnSave;
    private DatabaseHelper dbHelper;
    private final Calendar calendar = Calendar.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_activity);

        tripId = getIntent().getIntExtra("tripId", -1);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        dbHelper = new DatabaseHelper(this);

        etTitle = findViewById(R.id.et_title);
        etTime = findViewById(R.id.et_time);
        etNotes = findViewById(R.id.et_notes);
        btnSave = findViewById(R.id.btn_save_activity);

        etTime.setOnClickListener(v -> showTimePicker());

        btnSave.setOnClickListener(v -> saveActivity());
    }

    private void showTimePicker() {
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this, R.style.CustomDatePickerDialog,
                (view, hourOfDay, minuteOfHour) -> {
                    String amPm = (hourOfDay >= 12) ? "PM" : "AM";
                    int displayHour = (hourOfDay > 12) ? hourOfDay - 12 : (hourOfDay == 0 ? 12 : hourOfDay);
                    etTime.setText(String.format(Locale.getDefault(), "%02d:%02d %s", displayHour, minuteOfHour, amPm));
                }, hour, minute, false);
        timePickerDialog.show();
    }

    private void saveActivity() {
        if (tripId == -1) {
            Toast.makeText(this, "Error: Invalid Trip", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }

        String title = etTitle.getText() != null ? etTitle.getText().toString().trim() : "";
        String time = etTime.getText() != null ? etTime.getText().toString().trim() : "";
        String notes = etNotes.getText() != null ? etNotes.getText().toString().trim() : "";

        if (title.isEmpty() || time.isEmpty()) {
            Toast.makeText(this, "Title and Time are required", Toast.LENGTH_SHORT).show();
            return;
        }

        ActivityModel activity = new ActivityModel();
        activity.setTripId(tripId);
        activity.setTitle(title);
        activity.setTime(time);
        activity.setNotes(notes);
        activity.setDayNum(1); // Default to Day 1 for manually added items

        long result = dbHelper.insertActivity(activity);
        if (result != -1) {
            Toast.makeText(this, "Activity Added Successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Error Adding Activity", Toast.LENGTH_SHORT).show();
        }
    }
}
