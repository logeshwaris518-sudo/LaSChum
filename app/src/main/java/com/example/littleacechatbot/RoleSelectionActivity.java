package com.example.littleacechatbot;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class RoleSelectionActivity extends AppCompatActivity {

    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_role_selection);

        username = getIntent().getStringExtra("username");

        Button buttonSchool = findViewById(R.id.buttonSchool);
        Button buttonParent = findViewById(R.id.buttonParent);

        buttonSchool.setOnClickListener(v -> goToChat("school"));
        buttonParent.setOnClickListener(v -> goToChat("parent"));
    }

    private void goToChat(String role) {
        Intent intent = new Intent(RoleSelectionActivity.this, MainActivity.class);
        intent.putExtra("username", username);
        intent.putExtra("role", role);
        startActivity(intent);
        finish();
    }
}