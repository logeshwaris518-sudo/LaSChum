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

public class SignupActivity extends AppCompatActivity {

    private static final String BASE_URL = "https://guileless-blot-syrup.ngrok-free.dev/";

    private EditText editTextUsername, editTextPassword, editTextConfirmPassword;
    private Button buttonCreateAccount;
    private TextView textViewError, textViewBackToLogin;
    private ApiService apiService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        editTextUsername = findViewById(R.id.editTextUsername);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        buttonCreateAccount = findViewById(R.id.buttonCreateAccount);
        textViewError = findViewById(R.id.textViewError);
        textViewBackToLogin = findViewById(R.id.textViewBackToLogin);

        apiService = RetrofitClient.getClient(BASE_URL).create(ApiService.class);

        buttonCreateAccount.setOnClickListener(v -> attemptSignup());
        textViewBackToLogin.setOnClickListener(v -> {
            startActivity(new Intent(SignupActivity.this, LoginActivity.class));
            finish();
        });
    }

    private void attemptSignup() {
        String username = editTextUsername.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
            showError("Please fill in all fields");
            return;
        }

        if (!password.equals(confirmPassword)) {
            showError("Passwords do not match");
            return;
        }

        AuthRequest request = new AuthRequest(username, password);

        apiService.signup(request).enqueue(new Callback<AuthResponse>() {
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
        Intent intent = new Intent(SignupActivity.this, RoleSelectionActivity.class);
        intent.putExtra("username", username);
        startActivity(intent);
        finish();
    }
}