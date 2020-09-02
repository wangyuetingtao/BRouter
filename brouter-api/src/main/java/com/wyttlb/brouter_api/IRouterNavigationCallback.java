package com.wyttlb.brouter_api;

public interface IRouterNavigationCallback {
    void onSuccess(BRouterMeta meta);

    void onFailed(BRouterMeta meta);
}
