package com.wyttlb.brouter.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;

import android.content.BroadcastReceiver;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.wyttlb.brouter.R;
import com.wyttlb.brouter.utils.Constant;
import com.wyttlb.brouter_annotation.annotation.Route;
import com.wyttlb.brouter_api.BRouter;
import com.wyttlb.brouter_api.BRouterMeta;
import com.wyttlb.brouter_api.IRouterNavigationCallback;

@Route(path = Constant.MAIN_ACTIVITY, name="main")
public class MainActivity extends AppCompatActivity {

    Button btnGoSecond;
    Button btnGoThird;
    Button btnGoSecondWithParam;
    Button btnGoThirdWithResult;
    Button btnGoFourWithInterceptorMulti;
    Button btnGoThirdWithInterceptorOne;
    Button btnGoFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnGoSecond = findViewById(R.id.btn_go_second);
        btnGoThird = findViewById(R.id.btn_go_third);
        btnGoSecondWithParam = findViewById(R.id.btn_go_second_with_param);
        btnGoThirdWithResult = findViewById(R.id.btn_go_third_with_result);
        btnGoFourWithInterceptorMulti = findViewById(R.id.btn_go_four_with_interceptor_multi);
        btnGoThirdWithInterceptorOne = findViewById(R.id.btn_go_third_with_interceptor_one);
        btnGoFragment = findViewById(R.id.btn_go_fragment);

        btnGoSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BRouter.getInstance().build(Constant.SECOND_ACTIVITY).navigation(MainActivity.this, new IRouterNavigationCallback() {
                    @Override
                    public void onSuccess(BRouterMeta meta) {
                        Toast.makeText(MainActivity.this, "跳转成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onFailed(BRouterMeta meta) {
                        Toast.makeText(MainActivity.this, "跳转失败", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        btnGoThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BRouter.getInstance().build(Constant.THIRD_ACTIVITY).navigation(MainActivity.this);
            }
        });

        btnGoSecondWithParam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BRouter.getInstance().build(Constant.SECOND_ACTIVITY)
                        .setBundle(new Bundle())
                        .withString("name", "张三")
                        .withBoolean("married", false)
                        .withInt("age", 28)
                        .setActivityOptionsCompat(ActivityOptionsCompat.makeCustomAnimation(MainActivity.this, android.R.anim.fade_in, android.R.anim.fade_out))
                        .navigation(MainActivity.this);
            }
        });

        btnGoThirdWithResult.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BRouter.getInstance().build(Constant.THIRD_ACTIVITY)
                        .setRequestCode(20).navigation(MainActivity.this);

            }
        });

        btnGoFourWithInterceptorMulti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Main:我说嘿你说嘿", Toast.LENGTH_SHORT).show();
                BRouter.getInstance().build(Constant.FOUR_ACTIVITY)
                        .navigation(MainActivity.this);
            }
        });

        btnGoThirdWithInterceptorOne.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BRouter.getInstance().build(Constant.THIRD_ACTIVITY)
                        .navigation(MainActivity.this);
            }
        });

        btnGoFragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment fragment = (Fragment) BRouter.getInstance().build(Constant.TEST_FRAGMENT).navigation(MainActivity.this);
                Toast.makeText(MainActivity.this, "fragment实例=" + fragment, Toast.LENGTH_SHORT).show();

            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case 20:
                Toast.makeText(MainActivity.this, "返回code=" + resultCode, Toast.LENGTH_LONG).show();
                break;
            default:
                break;
        }
    }
}