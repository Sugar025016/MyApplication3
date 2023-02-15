package com.example.myapplication3;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import com.example.myapplication3.util.ViewUtil;

public class MainActivity5_listener extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_activity5_listener);
        EditText EditText1 = findViewById(R.id.EditText1);
        EditText EditText2 = findViewById(R.id.EditText2);
        EditText1.addTextChangedListener(new HideTextWatcher(EditText1,6));
        EditText2.addTextChangedListener(new HideTextWatcher(EditText2,11));
    }

    private class HideTextWatcher implements TextWatcher {
        private EditText mView;
        private int mMaxLength;
        public HideTextWatcher(EditText v, int mMaxLength) {
            this.mView =v;
            this.mMaxLength = mMaxLength;
        }
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {}

        @Override
        public void afterTextChanged(Editable s) {
            String s1 = s.toString();
            if(s1.length() == mMaxLength){
                ViewUtil.hideOneInputMethod(MainActivity5_listener.this,mView);
            }
        }
    }
}