package com.iot.mobile_labs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.util.regex.Pattern;

public class SignupActivity extends AppCompatActivity {

    private EditText inputEmail, inputPassword, inputName, inputPhoneNumber;
    private ProgressBar progressBar;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        //Get Firebase auth instance
        auth = FirebaseAuth.getInstance();

        Button btnSignIn = findViewById(R.id.sign_in_button);
        Button btnSignUp = findViewById(R.id.sign_up_button);
        inputEmail = findViewById(R.id.email);
        inputPassword = findViewById(R.id.password);
        inputName = findViewById(R.id.name);
        inputPhoneNumber = findViewById(R.id.phone_number);
        progressBar = findViewById(R.id.progressBar);
        final TextInputLayout emailInputLayout = findViewById(R.id.email_input_layout);
        final TextInputLayout passwordInputLayout = findViewById(R.id.password_input_layout);
        final TextInputLayout nameInputLayout = findViewById(R.id.name_input_layout);
        final TextInputLayout phoneNumberInputLayout = findViewById(R.id.phone_number_input_layout);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = inputEmail.getText().toString().trim();
                String password = inputPassword.getText().toString().trim();
                final String name = inputName.getText().toString().trim();
                String phoneNumber = inputPhoneNumber.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailInputLayout.setError("Enter email!");
                    return;
                }

                if(!isValidEMail(email)){
                    emailInputLayout.setError("Enter valid email!");
                    return;
                }

                emailInputLayout.setError(null);

                if (TextUtils.isEmpty(password)) {
                    passwordInputLayout.setError("Enter password!");
                    return;
                }

                if (password.length() < 8) {
                    passwordInputLayout.setError("Password too short, enter minimum 8 characters!");
                    return;
                }

                passwordInputLayout.setError(null);

                if (TextUtils.isEmpty(name)) {
                    nameInputLayout.setError("Enter your name!");
                    return;
                }

                nameInputLayout.setError(null);

                if (TextUtils.isEmpty(phoneNumber)) {
                    phoneNumberInputLayout.setError("Enter phone number!");
                    return;
                }

                if(!isValidMobile(phoneNumber)){
                    phoneNumberInputLayout.setError("Enter valid phone number!");
                    return;
                }

                phoneNumberInputLayout.setError(null);

                progressBar.setVisibility(View.VISIBLE);
                //create user
                auth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                progressBar.setVisibility(View.GONE);
                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(SignupActivity.this, "Authentication failed." + task.getException(),
                                            Toast.LENGTH_SHORT).show();
                                } else {
                                    startActivity(new Intent(SignupActivity.this, MainActivity.class).putExtra("name", name));
                                    finish();
                                }
                            }
                        });

            }
        });
    }

    private boolean isValidMobile(final String phone) {
        return Pattern.compile("\\+\\d{10,12}").matcher(phone).matches();
    }

    private boolean isValidEMail(final String eMail) {
        return Pattern.compile(getString(R.string.email_regex)).matcher(eMail).matches();
    }

    @Override
    protected void onResume() {
        super.onResume();
        progressBar.setVisibility(View.GONE);
    }
}