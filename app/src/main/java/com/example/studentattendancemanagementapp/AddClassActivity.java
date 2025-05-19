package com.example.studentattendancemanagementapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class AddClassActivity extends AppCompatActivity {

    EditText nameClassInput, limitAbsentInput, createCodeInput;
    Button createBtn;

    SharedPreferences prefs;
    SharedPreferences.Editor editor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_class);

        nameClassInput = findViewById(R.id.nameClass);
        limitAbsentInput = findViewById(R.id.limitAbsent);
        createCodeInput = findViewById(R.id.createCode);
        createBtn = findViewById(R.id.btnCreate); // FIXED: removed "Button" keyword

        prefs = getSharedPreferences("ClassPrefs", MODE_PRIVATE);
        editor = prefs.edit();

        createBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nameClass = nameClassInput.getText().toString().trim();
                String limitAbsent = limitAbsentInput.getText().toString().trim();
                String createCode = createCodeInput.getText().toString().trim();

                if (nameClass.isEmpty() || limitAbsent.isEmpty() || createCode.isEmpty()) {
                    Toast.makeText(AddClassActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (prefs.contains(createCode)) {
                    Toast.makeText(AddClassActivity.this, "Code already exists. Please use a different code.", Toast.LENGTH_LONG).show();
                    return;
                }

                String classInfo = nameClass + ";" + limitAbsent;
                editor.putString(createCode, classInfo);
                editor.apply();

                Toast.makeText(AddClassActivity.this, "Class created successfully!", Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}
