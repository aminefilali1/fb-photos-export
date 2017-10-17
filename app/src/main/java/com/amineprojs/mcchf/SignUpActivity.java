package com.amineprojs.mcchf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmail, editTextPwd, editTextPwdConfirm;
    Button buttonSignUp, buttonSignIn;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        checkIfConnected();

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPwd = (EditText) findViewById(R.id.pwd);
        editTextPwdConfirm = (EditText) findViewById(R.id.pwdConfirm);
        buttonSignUp = (Button) findViewById(R.id.buttonSignUp);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);

        buttonSignUp.setOnClickListener(this);
        buttonSignIn.setOnClickListener(this);
    }

    private void checkIfConnected() {
        Intent intent;
        sharedPreferences = getSharedPreferences(getPackageName() + ".userInfos", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("status", "N/A").equals("connected")) {
            intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == buttonSignUp) {
            if (!checkFields()) {
                return;
            }
            sharedPreferences = getSharedPreferences(getPackageName() + ".userInfos", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("email", editTextEmail.getText().toString());
            editor.putString("pwd", editTextPwd.getText().toString());
            editor.putString("status", "connected");
            editor.apply();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        }
        if (view == buttonSignIn) {
            Intent intent = new Intent(this, SignInActivity.class);
            startActivity(intent);
        }
    }

    private boolean checkFields() {
        boolean valid = true;
        if (editTextEmail.getText().toString().isEmpty()) {
            editTextEmail.setError("Email required");
            valid = false;
        }
        if (editTextPwd.getText().toString().isEmpty()) {
            editTextPwd.setError("Password required");
            valid = false;
        }
        if (editTextPwdConfirm.getText().toString().isEmpty()) {
            editTextPwdConfirm.setError("Password confirmation required");
            valid = false;
        }
        if (!editTextPwd.getText().toString().equals(editTextPwdConfirm.getText().toString())) {
            editTextPwdConfirm.setError("Passwords don't match");
            valid = false;
        }
        return valid;
    }
}
