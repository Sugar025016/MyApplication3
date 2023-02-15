package com.example.chapter05;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class LoginForgetActivity extends AppCompatActivity implements View.OnClickListener {

    private String mVerifycode;
    private EditText et_password_first;
    private EditText et_password_second;
    private EditText et_verifycode;
    private String mpassword = "111111";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_forget);
        String mPhone = getIntent().getStringExtra("phone");

        et_password_first = findViewById(R.id.et_password_first);
        et_password_second = findViewById(R.id.et_password_second);
        et_verifycode = findViewById(R.id.et_verifycode);
        findViewById(R.id.btn_verifycode).setOnClickListener(this);
        findViewById(R.id.btn_confirm).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_verifycode:
                mVerifycode = String.format("%06d", new Random().nextInt(999999));
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("XXXXXXXX///");
                builder.setMessage("****");
                builder.setPositiveButton("99", null);
                AlertDialog alertDialog = builder.create();
                alertDialog.show();
                break;
            case R.id.btn_confirm:
                String password_first = et_password_first.getText().toString();
                String password_second = et_password_second.getText().toString();
                if (password_first.length() < 6) {
                    Toast.makeText(this, "password mistake", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!password_first.equals(password_second)) {
                    Toast.makeText(this, "check password mistake", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!mpassword.equals(et_verifycode.getText().toString())) {
                    Toast.makeText(this, "verify code mistake", Toast.LENGTH_SHORT).show();
                    return;
                }

                Toast.makeText(this, "password change ok", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent();
                intent.putExtra("new_password",password_first);
                setResult(Activity.RESULT_OK,intent);
                finish();
                break;
        }

    }
}