package com.wyttlb.brouter_api;

/**
 * 路由拦截器接口
 */
public interface IRouteInteceptor {
    boolean intercept(BRouterMeta meta);
}
