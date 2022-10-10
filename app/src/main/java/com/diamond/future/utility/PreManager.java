package com.diamond.future.utility;

import android.content.Context;
import android.content.SharedPreferences;

public class PreManager {
    Context context;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    public PreManager(Context context) {
        this.context = context;
        sharedPreferences=context.getSharedPreferences(Constant.PREF_NAME,Context.MODE_PRIVATE);
        editor=sharedPreferences.edit();
    }
    public void setAuthToken(String adhharNumber)
    {
        editor.putString(Constant.AUTH_TOKEN,adhharNumber).apply();

    }
    public  String getAuthToken()
    {
        return  sharedPreferences.getString(Constant.AUTH_TOKEN,"");
    }
    public void setPassword(String password)
    {
        editor.putString(Constant.PASSWORD,password).apply();

    }
    public  String getPassword()
    {
        return  sharedPreferences.getString(Constant.PASSWORD,"");
    }
    public void setPhNumber(String password)
    {
        editor.putString(Constant.PHNUMBER,password).apply();

    }
    public  String getphNumber()
    {
        return  sharedPreferences.getString(Constant.PHNUMBER,"");
    }
    public void setDeviceId(String password)
    {
        editor.putString(Constant.DEVICE_ID,password).apply();

    }
    public  String getDeviceId()
    {
        return  sharedPreferences.getString(Constant.DEVICE_ID,"");
    }
    public void setDeviceVersion(String password)
    {
        editor.putString(Constant.DEVICE_VERSION,password).apply();

    }
    public  String getDeviceVerson()
    {
        return  sharedPreferences.getString(Constant.DEVICE_VERSION,"");
    }
    public void setDeviceName(String password)
    {
        editor.putString(Constant.DEVICE_NAME,password).apply();

    }
    public void setLOCATION(String Location)
    {
        editor.putString(Constant.LOCATION,Location).apply();

    }
    public  String getLOCATION()
    {
        return  sharedPreferences.getString(Constant.LOCATION,"");
    }
    public  String getDeviceName()
    {
        return  sharedPreferences.getString(Constant.DEVICE_NAME,"");
    }
    public void setLogin(boolean status)
    {
        editor.putBoolean(Constant.LOGIN,status).apply();
    }
    public boolean isLogin()
    {
        return sharedPreferences.getBoolean(Constant.LOGIN,false);
    }
    public void setPushnotification(boolean status)
    {
        editor.putBoolean(Constant.PUSHNOTIFICATION,status).apply();
    }
    public boolean isPushnotification()
    {
        return sharedPreferences.getBoolean(Constant.PUSHNOTIFICATION,false);
    }
    public void setFirstTime(boolean status)
    {
        editor.putBoolean(Constant.FIRSTOPEN,status).apply();
    }
    public boolean isFirsttime()
    {
        return sharedPreferences.getBoolean(Constant.FIRSTOPEN,false);
    }
}
