package com.main.garden_planner;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.garden_planner.databinding.ActivityLoginBinding;
import com.parse.ParseUser;

public class LoginActivity extends AppCompatActivity {
    private ActivityLoginBinding binding;

    EditText etUsername;
    EditText etPassword;
    Button btLogin;
    Button btRegister;
    TextView tvLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        if (ParseUser.getCurrentUser() != null){
            GardenMethodHelper.goMainActivity(LoginActivity.this);
        }

        etUsername = binding.etUsername;
        etPassword = binding.etPassword;
        btLogin = binding.btLogin;
        btRegister = binding.btRegister;
        tvLogin = binding.tvLogin;


        btLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                GardenMethodHelper.loginUser(username, password, LoginActivity.this);
            }
        });

        btRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goRegister();
            }
        });
    }

    private void goRegister() {
        Intent i = new Intent(this, RegisterActivity.class);
        startActivity(i);
    }
}