package com.android.weiplugin.log;

import android.util.Log;
import android.widget.EditText;

/**
 * <p>for debug test
 * <p>dLog(*)��debug��������
 * <p>log(*)����debug��������
 * ������ֲ
 * */
public class Debug {
    
    static String TAG = "DebugTools";

    public static  boolean DEBUG = true; 
    public static void dLog(String tag,String str){
        if(!DEBUG){
            return;
        }

        Log.v(tag, str);
        
    }
    public static void log(String tag,String str){
        
        Log.v(tag, str);
        
    }
    public static void dLog(String str){
        if(str.equals("��")){
            dLog("error ");
        }
        dLog(TAG, str);
    }
    public static void dLog(Object o){
        dLog(TAG, o.toString());
    }
    public static void dLog(Object[] os){
        for(Object o:os){
            dLog(TAG, o.toString());
        }
    }
    public static void dLog(Throwable t){
        if(!DEBUG){
            return;
        }
        
        Log.e(TAG, "",t);
        
    }
    public static void log(String str){
        log(TAG, str);
    }
    
    /**
     * init user for debug 
     * */
    public static void initUserForTest(EditText user,EditText pass){
        if(!DEBUG){
            return;
        }
        if(user!=null&&pass!=null){
//            pass.setText(AppSetting.PASSWORD);
//            user.setText(AppSetting.USER);
//            dLog("p : "+AppSetting.PASSWORD+" u: "+AppSetting.USER);
        }
        
    }
    public static void dLog(String tag,String str,Exception e){
        if(!DEBUG){
            return;
        }
        
        Log.e(tag, str,e);
        
    }
    
    /**
     * easy to modify control
     * */
    public static void log(String tag,String str,Exception e){
        
        Log.e(tag, str,e);
        
    }
    
    public static void dLog(String tag,Exception e){
        dLog(tag, "",e);
    }
    public static void dLog(Exception e){
        dLog(TAG, "",e);
    }
    public static void dLogCursor(Exception e){
        dLog("sfa_cursor", "cursor exception ",e);
    }
    
    public static void log( Exception e){
        log(TAG, "",e);
    }
    public static void log(String tag,Exception e){
        log(tag, "",e);
    }
}
