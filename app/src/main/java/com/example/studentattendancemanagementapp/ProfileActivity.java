package com.example.studentattendancemanagementapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.*;

public class ProfileActivity extends AppCompatActivity {
    private String uid; // Store UID here for reuse

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // 1) Toolbar + Up arrow
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) ab.setDisplayHomeAsUpEnabled(true);

        // 2) Wire up the TextViews
        TextView tvName = findViewById(R.id.tvProfileName);
        TextView tvDept = findViewById(R.id.tvProfileDept);

        // 3) Grab the UID passed from HomePage (or fallback to FirebaseAuth)
        uid = getIntent().getStringExtra("uid");
        if (uid == null || uid.isEmpty()) {
            uid = FirebaseAuth.getInstance().getCurrentUser() != null
                    ? FirebaseAuth.getInstance().getCurrentUser().getUid()
                    : null;
        }

        if (uid != null) {
            DatabaseReference ref = FirebaseDatabase.getInstance()
                    .getReference("users")
                    .child(uid);
            ref.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override public void onDataChange(@NonNull DataSnapshot snap) {
                    User user = snap.getValue(User.class);
                    if (user != null) {
                        tvName.setText(user.fullName);
                        tvDept.setText(user.role);
                    }
                }

                @Override public void onCancelled(@NonNull DatabaseError err) {
                    Toast.makeText(ProfileActivity.this,
                            "Load failed: " + err.getMessage(),
                            Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(this, "User not logged in", Toast.LENGTH_SHORT).show();
            finish(); // End if there's no UID
        }

        // 4) Hook the “Edit Profile” row (LinearLayout) instead of a button
        View menuEdit = findViewById(R.id.menuEditProfile);
        menuEdit.setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("uid", uid);
            startActivity(intent);
        });

        // 5) Other menu items...
        findViewById(R.id.menuNotification).setOnClickListener(v ->
                Toast.makeText(this, "Notification clicked", Toast.LENGTH_SHORT).show()
        );
        findViewById(R.id.menuMyRequest).setOnClickListener(v ->
                Toast.makeText(this, "My Request clicked", Toast.LENGTH_SHORT).show()
        );
        findViewById(R.id.menuChangePassword).setOnClickListener(v ->
                Toast.makeText(this, "Change Password clicked", Toast.LENGTH_SHORT).show()
        );

        // 6) Sign out
        MaterialButton btnSignOut = findViewById(R.id.btnSignOut);
        btnSignOut.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            finish(); // returns to LoginActivity
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
