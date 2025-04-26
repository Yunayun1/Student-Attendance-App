package com.example.studentattendancemanagementapp;


import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class JoinClassActivity extends AppCompatActivity {

    EditText joinCodeInput;
    Button joinBtn;

    SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_class);

        joinCodeInput = findViewById(R.id.joinCode);
        Button joinBtn = findViewById(R.id.joinBtn);

        prefs = getSharedPreferences("ClassPrefs", MODE_PRIVATE);

        joinBtn.setOnClickListener(v -> {
            String code = joinCodeInput.getText().toString().trim();

            if (code.isEmpty()) {
                Toast.makeText(this, "Please enter a code", Toast.LENGTH_SHORT).show();
                return;
            }

            if (prefs.contains(code)) {
                Toast.makeText(this, "Joined class successfully!", Toast.LENGTH_SHORT).show();
                // You can save join status here if needed
            } else {
                Toast.makeText(this, "Class code not found!", Toast.LENGTH_SHORT).show();
            }
        });
    }
}