package com.amineprojs.mcchf;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class SignInActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextEmail, editTextPwd;
    Button buttonSignIn;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        editTextEmail = (EditText) findViewById(R.id.email);
        editTextPwd = (EditText) findViewById(R.id.pwd);
        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);

        buttonSignIn.setOnClickListener(this);
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
        return valid;
    }

    @Override
    public void onClick(View view) {
        if (view == buttonSignIn) {
            if (!checkFields()) {
                return;
            }
            String email = editTextEmail.getText().toString();
            String pwd = editTextPwd.getText().toString();
            sharedPreferences = getSharedPreferences(getPackageName() + ".userInfos", Context.MODE_PRIVATE);
            if (sharedPreferences.getString("email", "N/A").equals(email)
                    && sharedPreferences.getString("pwd", "N/A").equals(pwd)
                    && !email.equals("N/A")
                    && !pwd.equals("N/A")) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("status", "connected");
                editor.apply();
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finishAffinity();
            } else {
                Toast.makeText(this, "Email/Password incorrect !", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
