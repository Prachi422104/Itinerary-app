package com.example.itineraryapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvUsernameDisplay;
    private TextInputEditText etEditUsername;
    private Button btnSaveProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Toolbar toolbar = findViewById(R.id.toolbar_profile);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        toolbar.setNavigationOnClickListener(v -> finish());

        tvUsernameDisplay = findViewById(R.id.tv_username_display);
        etEditUsername = findViewById(R.id.et_edit_username);
        btnSaveProfile = findViewById(R.id.btn_save_profile);

        // Load username
        SharedPreferences prefs = getSharedPreferences("AppPrefs", MODE_PRIVATE);
        String currentUsername = prefs.getString("username", "User");
        
        tvUsernameDisplay.setText(currentUsername);
        etEditUsername.setText(currentUsername);

        btnSaveProfile.setOnClickListener(v -> {
            String newUsername = etEditUsername.getText().toString().trim();
            if (newUsername.isEmpty()) {
                Toast.makeText(this, "Username cannot be empty", Toast.LENGTH_SHORT).show();
            } else {
                prefs.edit().putString("username", newUsername).apply();
                tvUsernameDisplay.setText(newUsername);
                Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
