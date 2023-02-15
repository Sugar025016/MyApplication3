package com.example.chapter05;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ViewUtils;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.chapter05.util.ViewUtil;

import java.util.Random;

public class LoginMainActivity extends AppCompatActivity implements RadioGroup.OnCheckedChangeListener, View.OnClickListener {

    private TextView tv_password;
    private EditText et_password;
    private Button btn_forget;
    private Button btn_login;
    private CheckBox ck_remember;
    private EditText et_phone;
    private RadioButton rb_password;
    private RadioButton rb_verifycode;
    private ActivityResultLauncher<Intent> register;

    private String mpassword="111111";
    private String mVerifycode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_main);
        RadioGroup rb_login = findViewById(R.id.rg_login);
        tv_password= findViewById(R.id.tv_password);
        et_phone= findViewById(R.id.et_phone);
        et_password= findViewById(R.id.et_password);
        btn_forget= findViewById(R.id.btn_forget);
        ck_remember= findViewById(R.id.ck_remember);
        btn_forget= findViewById(R.id.btn_forget);
        rb_password= findViewById(R.id.rb_password);
        rb_verifycode= findViewById(R.id.rb_verifycode);
        btn_login= findViewById(R.id.btn_login);

        rb_login.setOnCheckedChangeListener(this);
        et_phone.addTextChangedListener(new HideTextWatcher(et_phone,11));
        et_password.addTextChangedListener(new HideTextWatcher(et_password,6));
        btn_forget.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        register = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                Intent intent = result.getData();
                if(intent != null && result.getResultCode() == Activity.RESULT_OK){
                    mpassword = intent.getStringExtra("new_password");
                }

            }
        });

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId) {
        switch (checkedId){
            case  R.id.rb_password:
                tv_password.setText(getString(R.string.login_password));
                et_password.setHint(getString(R.string.input_password));
                btn_forget.setText(getString(R.string.forget_password));
                ck_remember.setVisibility(View.VISIBLE);
                break;

            case  R.id.rb_verifycode:
                tv_password.setText(getString(R.string.verifycode));
                et_password.setHint(getString(R.string.input_verifycode));
                btn_forget.setText(getString(R.string.get_verifycode));
                ck_remember.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void onClick(View v) {
        String s  =et_phone.getText().toString();

        if(s.length()<11){
            Toast.makeText(this,"pleas input XXXXXXX",Toast.LENGTH_SHORT).show();
            return;
        }
        switch  (v.getId()){
            case R.id.btn_forget:
                if(rb_password.isChecked()){
                    Intent intent = new Intent(this, LoginForgetActivity.class);
                    intent.putExtra("phone",s);
                    register.launch(intent);
                }else if(!rb_verifycode.isChecked()){
                     mVerifycode = String.format("%06d", new Random().nextInt(999999));
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("XXXXXXXX///");
                    builder.setMessage("****");
                    builder.setPositiveButton("99",null);
                    AlertDialog alertDialog = builder.create();
                    alertDialog.show();
                }
                break;
            case R.id.btn_login:
                if(rb_password.isChecked()){
                    if(!mpassword.equals(et_password.getText().toString())){
                        Toast.makeText(this, "XXXXXX", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }else if(rb_verifycode.isChecked()){
                    if(!mVerifycode.equals(et_password.getText().toString())){
                        Toast.makeText(this, "XXXXXX", Toast.LENGTH_SHORT).show();
                        return;
                    }
                }
                loginSuccess();
                break;

        }

    }

    private void loginSuccess() {
        String desc = String.format("000", et_phone.getText().toString());
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("888");
        builder.setMessage(desc);

        builder.setPositiveButton("12", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finish();
            }
        });

        builder.setNegativeButton("5466",null);
        AlertDialog alertDialog = builder.create();
        alertDialog.show();

    }

    private class HideTextWatcher implements TextWatcher {
        private EditText mView;
        private int mMaxLength;

        public HideTextWatcher(EditText v, int maxLength) {
            this.mView=v;
            this.mMaxLength=maxLength;
        }

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.toString().length() == mMaxLength){
                ViewUtil.hideOneInputMethod(LoginMainActivity.this,mView);
            }
        }
    }
}