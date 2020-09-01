package com.wyttlb.brouter;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.wyttlb.brouter_annotation.annotation.Route;

@Route(path = "main/mainActivity", name="test")
public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}