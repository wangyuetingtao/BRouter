package com.wyttlb.brouter.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.wyttlb.brouter.R;
import com.wyttlb.brouter.utils.Constant;
import com.wyttlb.brouter_annotation.annotation.Route;

@Route(path= Constant.THIRD_ACTIVITY, name="third")
public class ThirdActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_third);
        setResult(789);
    }
}