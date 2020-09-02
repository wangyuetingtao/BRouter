package com.wyttlb.brouter;

import android.app.Application;

import com.wyttlb.brouter_api.BRouter;

public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        BRouter.getInstance().initial(this);
    }
}
