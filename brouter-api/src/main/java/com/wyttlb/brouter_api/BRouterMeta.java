package com.wyttlb.brouter_api;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;

import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * 存放路由跳转相关的信息
 */
public class BRouterMeta {
    /**
     * 上下文
     */
    Context mContext;
    /**
     * 跳转链接
     */
    String mUrl;

    /**
     * 跳转目标类
     */
    Class<?> mDestCls;

    /**
     * 请求码
     */
    int requestCode = -1;

    /**
     * 传递的bundle信息
     */
    Bundle mBundle;

    /**
     * activity intent flag
     */
    int intentFlag = -1;

    /**
     * 动画配置
     */
    ActivityOptionsCompat activityOptionsCompat;

    /**
     * 回调
     */
    IRouterNavigationCallback callback;


    public BRouterMeta(Context context, String url, Class<?> destinationClass) {
        this.mContext = context;
        this.mUrl = url;
        this.mDestCls = destinationClass;
    }

    // generator getter and setter


    public Context getContext() {
        return mContext;
    }

    public void setContext(Context mContext) {
        this.mContext = mContext;
    }

    public String getURl() {
        return mUrl;
    }

    public void setURl(String mURl) {
        this.mUrl = mURl;
    }

    public Class<?> getDestCls() {
        return mDestCls;
    }

    public void setDestCls(Class<?> mDestCls) {
        this.mDestCls = mDestCls;
    }

    public int getRequestCode() {
        return requestCode;
    }

    public BRouterMeta setRequestCode(int requestCode) {
        this.requestCode = requestCode;
        return this;
    }

    public Bundle getBundle() {
        return mBundle;
    }

    public BRouterMeta setBundle(Bundle bundle) {
        this.mBundle = bundle;
        return this;
    }

    public int getIntentFlag() {
        return intentFlag;
    }

    public BRouterMeta setIntentFlag(int intentFlag) {
        this.intentFlag = intentFlag;
        return this;
    }

    public ActivityOptionsCompat getActivityOptionsCompat() {
        return activityOptionsCompat;
    }

    public BRouterMeta setActivityOptionsCompat(ActivityOptionsCompat activityOptionsCompat) {
        this.activityOptionsCompat = activityOptionsCompat;
        return this;
    }

    public IRouterNavigationCallback getCallback() {
        return callback;
    }

    public BRouterMeta setCallback(IRouterNavigationCallback callback) {
        this.callback = callback;
        return this;
    }

    // generator getter and setter end

    //bundle扩展

    /**
     * Inserts a String value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a String, or null
     * @return current
     */
    public BRouterMeta withString(@Nullable String key, @Nullable String value) {
        mBundle.putString(key, value);
        return this;
    }

    /**
     * Inserts a Boolean value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a boolean
     * @return current
     */
    public BRouterMeta withBoolean(@Nullable String key, boolean value) {
        mBundle.putBoolean(key, value);
        return this;
    }

    /**
     * Inserts a short value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key   a String, or null
     * @param value a short
     * @return current
     */
    public BRouterMeta withShort(@Nullable String key, short value) {
        mBundle.putShort(key, value);
        return this;
    }

    /**
     * Inserts an int value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key   a String, or null
     * @param value an int
     * @return current
     */
    public BRouterMeta withInt(@Nullable String key, int value) {
        mBundle.putInt(key, value);
        return this;
    }

    /**
     * Inserts a long value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key   a String, or null
     * @param value a long
     * @return current
     */
    public BRouterMeta withLong(@Nullable String key, long value) {
        mBundle.putLong(key, value);
        return this;
    }

    /**
     * Inserts a double value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key   a String, or null
     * @param value a double
     * @return current
     */
    public BRouterMeta withDouble(@Nullable String key, double value) {
        mBundle.putDouble(key, value);
        return this;
    }

    /**
     * Inserts a float value into the mapping of this Bundle, replacing
     * any existing value for the given key.
     *
     * @param key   a String, or null
     * @param value a float
     * @return current
     */
    public BRouterMeta withFloat(@Nullable String key, float value) {
        mBundle.putFloat(key, value);
        return this;
    }


    /**
     * Inserts a Parcelable value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a Parcelable object, or null
     * @return current
     */
    public BRouterMeta withParcelable(@Nullable String key, @Nullable Parcelable value) {
        mBundle.putParcelable(key, value);
        return this;
    }

    /**
     * Inserts a Serializable value into the mapping of this Bundle, replacing
     * any existing value for the given key.  Either key or value may be null.
     *
     * @param key   a String, or null
     * @param value a Serializable object, or null
     * @return current
     */
    public BRouterMeta withSerializable(@Nullable String key, @Nullable Serializable value) {
        mBundle.putSerializable(key, value);
        return this;
    }

    /**
     * 跳转
     * @return
     */
    public Object navigation() {
        return navigation(null);
    }

    /**
     * 跳转
     * @return
     */
    public Object navigation(Context context) {
        return navigation(context, null);
    }

    /**
     * 跳转
     * @return
     */
    public Object navigation(Context context, IRouterNavigationCallback callback) {
        return BRouter.getInstance().navigation(context, this, callback);
    }


}
