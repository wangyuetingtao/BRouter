package com.wyttlb.brouter.interceptor;

import com.wyttlb.brouter.utils.Constant;
import com.wyttlb.brouter_api.BRouterMeta;
import com.wyttlb.brouter_api.IRouteInteceptor;
import com.wyttlb.brouter_api.ServiceProvider;
/**
 * 注意：添加拦截器的同时，请手动在
 * resources/META-INF/services/com.xxx.IRouteInteceptor
 * 中添加对应的类，供Java SPI扫描
 * 后续可以考虑使用gradle插件自动做操作
 */
@ServiceProvider(value = IRouteInteceptor.class, alias = Constant.THIRD_ACTIVITY, priority = 1)
public class ThirdActivityInterceptor implements IRouteInteceptor{
    @Override
    public boolean intercept(BRouterMeta meta) {

        return false;
    }
}
