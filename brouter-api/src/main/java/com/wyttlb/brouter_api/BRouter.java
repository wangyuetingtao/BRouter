package com.wyttlb.brouter_api;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.wyttlb.brouter_annotation.utils.Consts;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.ServiceLoader;
import java.util.Set;

/**
 * 静态内部类单例,提供路由常用api
 * @author wyttlb
 */
public class BRouter {

    private BRouter() {}

    private static class Singleton {
        private static final BRouter INSTANCE = new BRouter();
    }

    public static BRouter getInstance() {
        return Singleton.INSTANCE;
    }

    private Context mContext;
    /**是否初始化完成*/
    private boolean isInitial;

    /**路由集合，读取apt编译期生成的路由表*/
    private Map<String, Class<?>> mRouteMaps = new HashMap<>();

    /**拦截器*/
    private Map<String, List<IRouteInteceptor>> mInteceptors = new HashMap<>();

    public void initial(Context context) {
        if (isInitial) return;
        //默认为application的context，也可以自己替换
        mContext = context.getApplicationContext();

        //读取apt生成的路由表
        try {
            //反射找到apt生成的类
          Class cls =  Class.forName(Consts.FACADE_PACKAGE + "." + Consts.GENERATE_CLASS_NAME);
          Method method = cls.getDeclaredMethod(Consts.METHOD_NAME);
          HashMap<String, Class<?>> aptMaps = (HashMap<String, Class<?>>)method.invoke(cls.newInstance());
          mRouteMaps.putAll(aptMaps);
        }catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

        //初始化拦截器
        //由于拦截器不是apt生成的，因此可以使用java原生的spi api扫描MATA-INF文件实现
        //缺点是每增加一个拦截器，需要手动配置一下
        ServiceLoader<IRouteInteceptor> provides = ServiceLoader.load(IRouteInteceptor.class);
        Iterator<IRouteInteceptor> iterator = provides.iterator();

        while (iterator.hasNext()) {
           IRouteInteceptor interceptor = iterator.next();
           String alias = interceptor.getClass().getAnnotation(ServiceProvider.class).alias();
           if (TextUtils.isEmpty(alias)) {
               alias = "root";
           }
           List<IRouteInteceptor> listInteceptor = mInteceptors.get(alias);
           if (listInteceptor == null) {
               listInteceptor = new ArrayList<>();
               mInteceptors.put(alias, listInteceptor);
           }
           listInteceptor.add(interceptor);
        }

        //提前对拦截器做优先级排序
        for (String key : mInteceptors.keySet()) {
            if (mInteceptors.get(key).size() > 1) {
                Collections.sort(mInteceptors.get(key), (Comparator<Object>) (t1, t2) -> {
                    int priority1 = t1.getClass().getAnnotation(ServiceProvider.class).priority();
                    int priority2 = t2.getClass().getAnnotation(ServiceProvider.class).priority();

                    return (priority1 - priority2) % 2;
                });
            }
        }

        isInitial = true;
    }

    /**
     * 创建跳转地址
     * @param url
     * @return 路由信息
     */
    public BRouterMeta build(String url) {
        return new BRouterMeta(mContext, url, mRouteMaps.get(url));
    }

    public Object navigation(Context context, BRouterMeta bRouterMeta, IRouterNavigationCallback callback) {
        boolean isIntercept = handleIntercept(bRouterMeta);
        //如果拦截，就不进行跳转
        if (isIntercept) {
            return null;
        }
        Class<?> targetCls = bRouterMeta.mDestCls;

        //判断类型
        if (isInstanceOf(targetCls, Activity.class)) {
            navigate2Activity(context, bRouterMeta, callback);
        }

        /**
         * 只支持Android x
         */
        if (isInstanceOf(targetCls, Fragment.class)) {
           return navigate2Fragment(bRouterMeta, callback);
        }

        return null;

    }

    private boolean isInstanceOf(Class<?> sourceCls, Class<?> targetCls) {
        if (sourceCls.getCanonicalName() == targetCls.getCanonicalName()) {
            return true;
        } else if (sourceCls.getSuperclass() == null) {
            return false;
        }
        return isInstanceOf(sourceCls.getSuperclass(), targetCls);
    }

    private boolean handleIntercept(BRouterMeta meta) {
        //获取当前路由的拦截器,相同url可能有多个拦截器,优先级高的先被调用
        List<IRouteInteceptor> interceptors = mInteceptors.get(meta.getURl());
        if (interceptors != null && interceptors.size() > 0) {
            for (IRouteInteceptor interceptor : interceptors) {
                if(interceptor.intercept(meta)) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 跳转到activity
     * @param conetxt
     * @param meta
     * @param callback
     */
    private void navigate2Activity(Context conetxt, BRouterMeta meta, IRouterNavigationCallback callback) {
       final Context currentContext =  (conetxt == null) ? mContext : conetxt;
        Intent intent = new Intent(currentContext, meta.mDestCls);
        if (meta.getBundle() != null) {
            intent.putExtras(meta.getBundle());
        }
        // 设置flags.
        int flags = meta.getIntentFlag();
        if (-1 != flags) {
            intent.setFlags(flags);
        } else if (!(currentContext instanceof Activity)) {
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        //尝试跳转
        try {
            if (meta.getRequestCode() >= 0) {
                if (currentContext instanceof Activity) {
                    ActivityCompat.startActivityForResult((Activity) currentContext, intent, meta.getRequestCode(), meta.getActivityOptionsCompat() != null ? meta.getActivityOptionsCompat().toBundle() : null);
                } else {
                    throw new IllegalAccessException("context should be an activity");
                }
            } else {
                ActivityCompat.startActivity(currentContext, intent, meta.getActivityOptionsCompat() != null ? meta.getActivityOptionsCompat().toBundle() : null);
            }

            if (null != callback) {
                callback.onSuccess(meta);
            }
        } catch (Exception e) {
            e.printStackTrace();
            if (null != callback) {
                callback.onFailed(meta);
            }
        }



    }

    /**
     * 获得fragment
     */
    private Object navigate2Fragment(BRouterMeta meta, IRouterNavigationCallback callback) {
        Class fragmentMeta = meta.mDestCls;
        try {
            Object instance = fragmentMeta.getConstructor().newInstance();
            if (instance instanceof Fragment) {
                ((Fragment) instance).setArguments(meta.getBundle());
            }
            if (callback != null) {
                callback.onSuccess(meta);
            }
            return instance;
        } catch (Exception ex) {
            return null;
        }
    }
}
