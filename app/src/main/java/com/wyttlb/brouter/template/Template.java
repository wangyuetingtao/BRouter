package com.wyttlb.brouter.template;

import com.wyttlb.brouter.activity.MainActivity;
import com.wyttlb.brouter.activity.SecondActivity;

import java.util.HashMap;
import java.util.Map;

/**
 * 模板类代码，用于写java poet代码时，参考
 * 自动生成类代码时，都需要先写出模板类代码，再使用工具生成
 * @author wyttlb
 */
public class Template {
    private static final HashMap<String, Class<?>> ROUTE_MAP = new HashMap();

    static {
        ROUTE_MAP.put("main/MainActivity", MainActivity.class);
        ROUTE_MAP.put("main/DetailActivity", SecondActivity.class);

    }
    public static Map<String, Class<?>> getRouteMap() {
        return ROUTE_MAP;
    }
}
