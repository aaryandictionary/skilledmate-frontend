package com.app.miniproject.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.util.List;
import java.util.Set;

public class DataProcessor {

    public static final String PROFILE_COMPLETE = "profile_complete";
    public static final String USER_ID = "user_id";
    public static final String MOBILE = "mobile";
    public static final String NAME = "name";
    public static final String EMAIL = "email";
    public static final String COLLEGE_ID = "college_id";
    public static final String COLLEGE = "college";
    public static final String USER_IMAGE = "user_image";
    public static final String SKILLS_LIST = "skills";

    public static final String SHARE_TYPE = "share_type";
    public static final String SHARE_ID = "share_id";
    public static final String SHARE_VIEWED = "share_viewed";


    private SharedPreferences prefs;
    private SharedPreferences.Editor editor;

    private static Context mCxt;

    private static DataProcessor INSTANCE=null;

    private DataProcessor(SharedPreferences preferences) {
        this.prefs = preferences;
        this.editor = preferences.edit();
    }

    public static synchronized DataProcessor getInstance(Context context){
        mCxt=context;
        if (INSTANCE==null){
            INSTANCE=new DataProcessor(context.getSharedPreferences("mini_project",Context.MODE_PRIVATE));
        }

        return INSTANCE;
    }

    public void deleteAll(){
        editor.clear();
        editor.apply();
    }

    public  void saveString( String key,String value) {
        editor.putString(key,value);
        editor.apply();
    }

    public  void saveLong( String key,Long value) {
        editor.putLong(key,value);
        editor.apply();
    }

    public  void saveInteger( String key,int value) {
        editor.putInt(key,value);
        editor.apply();
    }

    public void saveList(String key, Set<String> value){
        editor.putStringSet(key,value);
        editor.apply();
    }

    public Set<String>getList(String key){
        return prefs.getStringSet(key,null);
    }

    public  String getString(String key) {
        return prefs.getString(key,"");
    }
    public  int getInteger(String key) {
        return prefs.getInt(key,0);
    }

    public void saveBoolean(String key,boolean value){
    editor.putBoolean(key,value);
    editor.apply();
    }

    public boolean getBoolean(String key){
        return prefs.getBoolean(key,false);
    }
    public long getLong(String key){
        return prefs.getLong(key,0);
    }

    public boolean isConnected(){
        boolean connected = false;
        ConnectivityManager connectivityManager = (ConnectivityManager)mCxt.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED) {
            //we are connected to a network
            connected = true;
        } else
            connected = false;
        return connected;
    }



}
