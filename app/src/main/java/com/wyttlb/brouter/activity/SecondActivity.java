package com.wyttlb.brouter.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.TextView;

import com.wyttlb.brouter.R;
import com.wyttlb.brouter.utils.Constant;
import com.wyttlb.brouter_annotation.annotation.Route;

@Route(path= Constant.SECOND_ACTIVITY, name="second")
public class SecondActivity extends AppCompatActivity {
    TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);

        textView = findViewById(R.id.tv_param);

        Intent intent = getIntent();

        if (intent != null && !TextUtils.isEmpty(intent.getStringExtra("name"))) {
            String name = intent.getStringExtra("name");
            boolean isMarried = intent.getBooleanExtra("married", false);
            int age = intent.getIntExtra("age", 0);

            textView.setText( String.format("%s今年%d岁，是否结婚=%b",name, age, isMarried));
        }
    }
}