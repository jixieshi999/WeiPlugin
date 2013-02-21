package com.android.weiplugin.config;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.AccessControlException;
import java.util.Properties;

import com.android.weiplugin.tools.FilePathTools;

import android.os.Environment;

/**
 * 系统配置类
 * **/
public class Configs {

	public static final String ACTION_RSS = "com.rss.rss";
	
    private static final String TAG = "Config";
    
//    public static boolean DEBUG = true;
    
    public static  String EXTERNAL_PATH = null;
//    public static  String EXTERNAL_PATH = Environment.getExternalStorageDirectory().getPath();

	public static final String DATABASENAME = "weibot.db";
	
    /**test*/
    public static boolean DEBUG ;

    private static Properties defaultProperty;

    static {
        init();
    }

    /*package*/ public static void init() {
        defaultProperty = new Properties();
        defaultProperty.setProperty("weibot.debug", "true");
        defaultProperty.setProperty("weibot.ui.skin", "0");
        
//        defaultProperty.setProperty("weibot.weidata.baseUrl", "http://tweets.seraph.me/search/");
        try {
            // Android platform should have dalvik.system.VMRuntime in the classpath.
            // @see http://developer.android.com/reference/dalvik/system/VMRuntime.html
            Class.forName("dalvik.system.VMRuntime");
            defaultProperty.setProperty("weibot.dalvik", "true");
        } catch (ClassNotFoundException cnfe) {
            defaultProperty.setProperty("weibot.dalvik", "false");
        }
        DALVIK = getBoolean("weibot.dalvik");
    }

    public static final String weibotProps = "weibot.properties.txt";
    public static boolean loadProperties(){
    	
        boolean loaded = loadProperties(defaultProperty, FilePathTools.sConfigPath +  weibotProps)/* ||
                loadProperties(defaultProperty, Configuration.class.getResourceAsStream("/WEB-INF/" + weibotProps)) ||
                loadProperties(defaultProperty, Configuration.class.getResourceAsStream("/" + weibotProps))*/;
//        Debug.dLog("load property,"+loaded);
        return loaded;
    }
    private static boolean loadProperties(Properties props, String path) {
        try {
            File file = new File(path);
            if(file.exists() && file.isFile()){
                props.load(new FileInputStream(file));
                return true;
            }
        } catch (Exception ignore) {
        }
        return false;
    }

    private static boolean loadProperties(Properties props, InputStream is) {
        try {
            props.load(is);
            return true;
        } catch (Exception ignore) {
        }
        return false;
    }

    private static boolean DALVIK;


    public static boolean isDalvik() {
        return DALVIK;
    }

    public static void setPropery(String key,String value){
    	//weibot.weidata.downloadPicture
    	defaultProperty.put(key, value);
    } 
    /**
     * 0,1...
     * @see #getSkinSwitch()
     * */
    public static void setSkinPropery(String value){
    	setPropery("weibot.ui.skin", value);
    }
     
 

    /**
     * 皮肤，暂时只支持，0,白底黑字，1黑底白字
     * 默认 0
     * */
    public static int getSkinSwitch() {
    	return getIntProperty("weibot.ui.skin");
    }

    public static boolean getBoolean(String name) {
        String value = getProperty(name);
        return Boolean.valueOf(value);
    }

    public static int getIntProperty(String name) {
        String value = getProperty(name);
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }

    public static int getIntProperty(String name, int fallbackValue) {
        String value = getProperty(name, String.valueOf(fallbackValue));
        try {
            return Integer.parseInt(value);
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }

    public static long getLongProperty(String name) {
        String value = getProperty(name);
        try {
            return Long.parseLong(value);
        } catch (NumberFormatException nfe) {
            return -1;
        }
    }

    public static String getProperty(String name) {
        return getProperty(name, null);
    }

    public static String getProperty(String name, String fallbackValue) {
        String value;
        try {
            value = System.getProperty(name, fallbackValue);
            if (null == value) {
                value = defaultProperty.getProperty(name);
            }
            if (null == value) {
                String fallback = defaultProperty.getProperty(name + ".fallback");
                if (null != fallback) {
                    value = System.getProperty(fallback);
                }
            }
        } catch (AccessControlException ace) {
            // Unsigned applet cannot access System properties
            value = fallbackValue;
        }
        return replace(value);
    }

    /**
     * 将 xxx{ccc}中的ccc再次获取property
     * */
    private static String replace(String value) {
        if (null == value) {
            return value;
        }
        String newValue = value;
        int openBrace = 0;
        if (-1 != (openBrace = value.indexOf("{", openBrace))) {
            int closeBrace = value.indexOf("}", openBrace);
            if (closeBrace > (openBrace + 1)) {
                String name = value.substring(openBrace + 1, closeBrace);
                if (name.length() > 0) {
                    newValue = value.substring(0, openBrace) + getProperty(name)
                            + value.substring(closeBrace + 1);

                }
            }
        }
        if (newValue.equals(value)) {
            return value;
        } else {
            return replace(newValue);
        }
    }


    /**
     * 获取是否debug模式
     * */
    public static boolean getDebug() {
        return getBoolean("weibot.debug");

    }

    
}
