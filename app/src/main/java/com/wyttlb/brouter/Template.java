package com.wyttlb.brouter;

import java.util.HashMap;
import java.util.Map;

public class Template {
    private static final HashMap<String, Class<?>> ROUTE_MAP = new HashMap();

    static {
        ROUTE_MAP.put("main/mainActivity", MainActivity.class);
        ROUTE_MAP.put("main/DetailActivity", DetailActivity.class);

    }
    public static Map<String, Class<?>> getRouteMap() {
        return ROUTE_MAP;
    }
}
