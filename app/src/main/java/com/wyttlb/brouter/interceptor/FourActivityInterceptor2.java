package com.wyttlb.brouter.interceptor;

import android.widget.Toast;

import com.wyttlb.brouter.utils.Constant;
import com.wyttlb.brouter_api.BRouterMeta;
import com.wyttlb.brouter_api.IRouteInteceptor;
import com.wyttlb.brouter_api.ServiceProvider;
/**
 * 注意：添加拦截器的同时，请手动在
 * resources/META-INF/services/com.wyttlb.brouter_api.IRouteInteceptor文件
 * 中添加对应的类，供Java SPI扫描
 * 后续可以考虑使用gradle插件自动做操作
 */
@ServiceProvider(value = IRouteInteceptor.class, alias = Constant.FOUR_ACTIVITY, priority = 2)
public class FourActivityInterceptor2 implements IRouteInteceptor{
    @Override
    public boolean intercept(BRouterMeta meta) {

        Toast.makeText(meta.getContext(), "拦截器2说：滚,别嘿了", Toast.LENGTH_SHORT).show();

        return true;
    }
}
