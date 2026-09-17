package com.example.littleacechatbot;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://guileless-blot-syrup.ngrok-free.dev/";

    private EditText editTextUsername, editTextPassword;
    private Button buttonLogin, buttonSignup;
    private TextView textViewError;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        buttonLogin = findViewById(R.id.buttonLogin);
        buttonSignup = findViewById(R.id.buttonSignup);
        textViewError = findViewById(R.id.textViewError);

        apiService = RetrofitClient.getClient(BASE_URL).create(ApiService.class);

        buttonLogin.setOnClickListener(v -> attemptLogin());
        buttonSignup.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, SignupActivity.class));
        });
    }

    private void attemptLogin() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty()) {
            showError("Please enter username and password");
            return;
        }

        AuthRequest request = new AuthRequest(username, password);

        apiService.login(request).enqueue(new Callback<AuthResponse>() {
            @Override
            public void onResponse(Call<AuthResponse> call, Response<AuthResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    AuthResponse authResponse = response.body();
                    if ("success".equals(authResponse.getStatus())) {
                        goToRoleSelection(username);
                    } else {
                        showError(authResponse.getMessage());
                    }
                } else {
                    showError("Something went wrong. Please try again.");
                }
            }

            @Override
            public void onFailure(Call<AuthResponse> call, Throwable t) {
                showError("Couldn't reach server. Check your connection.");
            }
        });
    }

    private void showError(String message) {
        textViewError.setText(message);
        textViewError.setVisibility(View.VISIBLE);
    }

    private void goToRoleSelection(String username) {
        Intent intent = new Intent(LoginActivity.this, RoleSelectionActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
}