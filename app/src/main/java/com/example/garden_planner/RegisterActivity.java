package com.example.garden_planner;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.garden_planner.databinding.ActivityRegisterBinding;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class RegisterActivity extends AppCompatActivity {

    private ActivityRegisterBinding binding;

    public static final String TAG = "RegisterActivity";
    EditText etEmail;
    EditText etUsername;
    EditText etPassword;
    EditText etConfirmPassword;
    TextView tvMessage;
    Button btCreateAccount;
    ImageView ivLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegisterBinding.inflate(getLayoutInflater());

        View view = binding.getRoot();
        setContentView(view);

        etEmail = binding.etEmail;
        etUsername = binding.etUsername;
        etPassword = binding.etPassword;
        etConfirmPassword = binding.etConfirmPassword;
        tvMessage = binding.tvMessage;
        btCreateAccount = binding.btCreateAccount;
        ivLogo = binding.ivLogo;

        btCreateAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "onClick login button");
                String email = etEmail.getText().toString();
                String username = etUsername.getText().toString();
                String password = etPassword.getText().toString();
                String checkPassword = etConfirmPassword.getText().toString();
                makeAccount(email, username, password, checkPassword);
            }
        });

    }

    private void makeAccount(String email, String username, String password, String checkPassword) {
        if (!checkPassword.equals(password)) {
            Toast.makeText(RegisterActivity.this, "The passwords aren't equal to each other! Retype your password.", Toast.LENGTH_SHORT).show();
            Log.i(TAG, checkPassword);
            Log.i(TAG, password);
            return;
        }
        registerAccount(email, username, password);
    }

    private void registerAccount(String email, String username, String password) {
        ParseUser user = new ParseUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);
        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e != null){
                    // TODO: better error handling
                    Toast.makeText(RegisterActivity.this, "Issue with registering :(", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, "Issue with register", e);
                    return;
                }
                try {
                    user.save();
                    GardenMethodHelper.loginUser(username, password, RegisterActivity.this);
                } catch (ParseException i) {
                    e.printStackTrace();
                    Log.e(TAG, "couldn't make account", i);
                    Toast.makeText(RegisterActivity.this, "Issue with creating account :(", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
}
}