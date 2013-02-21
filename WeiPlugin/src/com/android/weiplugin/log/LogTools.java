package com.android.weiplugin.log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Properties;

import android.util.Log;

import com.android.weiplugin.config.Configs;
import com.android.weiplugin.tools.FilePathTools;

public class LogTools {

    public static String TAG = "LogTools";
    public static String SPE = " : ";
    
    public static void log(String log){
        log(TAG,log);
    }
    public static void log(Object log){
    	log(TAG,""+log);
    }
    public static void log(String tag,String log){
        if(Configs.DEBUG){
            Log.v(tag, log);
        }
    }
    public static void log(String log,Throwable e){
        if(Configs.DEBUG){
            Log.e(TAG, log,e);
        }
    }
    public static void log(Throwable e){
    	if(Configs.DEBUG){
    		Log.e(TAG, "",e);
    	}
    }
    
    public static void logToFile(String className,Throwable e){
        logToFile(className,"------------------error--begin----------------------");
        String error = Log.getStackTraceString(e);
        logToFile(className,error);
        logToFile(className,"------------------error--end------------------------");
    }
    
    public static  void saveDeviceProperties(Properties pro){
    	 File f = new File(FilePathTools.sLogPath);
         if (!f.exists()) {
             log( "file not exist");
             FilePathTools.initLogPath();
             f = new File(FilePathTools.sLogPath);
             if (!f.exists()) {
                 return;
             }
         }
         FileOutputStream fs = null;
         try
         {
             fs = new FileOutputStream(f, true);
             logToFile("", "-----------------info-------------------", fs);
         	 pro.store(fs, "");
         	 logToFile("", "-----------------info-------------------", fs);
         }
         catch (Exception e) {
             log("error",e);
         }finally{
             if(null!=fs){
                 try {
                     fs.close();
                 } catch (IOException e) {
                 }
             }
         }
    }
    /**
     * �ַ���׷�ӵķ�ʽд����־
     * @param s
     */
    public static void logToFile(String className,String s)
    {
        File f = new File(FilePathTools.sLogPath);
        if (!f.exists()) {
            log( "file not exist");
            FilePathTools.initLogPath();
            f = new File(FilePathTools.sLogPath);
            if (!f.exists()) {
                return;
            }
        }
        FileOutputStream fs = null;
        try
        {
            fs = new FileOutputStream(f, true);
            // Date d = new Date();
            // byte[] dateBuffer = d.toString().getBytes();
            // fs.write(dateBuffer);
            // fs.write('\n');
            logToFile(className, s, fs);
        }
        catch (Exception e) {
            log("error",e);
        }finally{
            if(null!=fs){
                try {
                    fs.close();
                } catch (IOException e) {
                }
            }
        }
    }
	private static void logToFile(String className, String s,
			FileOutputStream fs) throws UnsupportedEncodingException,
			IOException {
		byte[] buffer;
		buffer = (getCurrentDate()+" "+className+SPE).getBytes("utf-8");
		fs.write(buffer, 0, buffer.length);
		
		buffer = s.getBytes("utf-8");
		fs.write(buffer, 0, buffer.length);
		fs.write('\n');
	}
    
    /**
     * ɾ��ǰ��־
     * @return  ���ɹ�ɾ������ļ����?���ڵ�����£�����true�����򷵻�false
     */
    public static boolean delete()
    {
        File f = new File(FilePathTools.sLogPath);
        if(f.exists())
        {
            return f.delete();
        }
        
        return true;
    }
    
    /**
     * ȡ�õ�ǰʱ��
     * @return
     */
    public static String getCurrentDate()
    {
        String ly_time="";
        try{
             SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
             ly_time = sdf.format(new java.util.Date());
            
        }catch (Exception e) {
            ly_time = System.currentTimeMillis()+"";
        }
        return ly_time;
    }
    

}
