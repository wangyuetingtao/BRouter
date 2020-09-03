package com.wyttlb.brouter.fragment;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.wyttlb.brouter.utils.Constant;
import com.wyttlb.brouter_annotation.annotation.Route;

@Route(path= Constant.TEST_FRAGMENT, name="fragment")
public class TestFragment extends Fragment {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
}
