package com.android.weiplugin.tools;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import android.content.Context;
import android.content.res.Resources;
import android.os.Environment;

import com.android.weiplugin.R;
import com.android.weiplugin.config.Configs;
import com.android.weiplugin.log.LogTools;


public class FilePathTools {

    public static String TAG = "FilePathTools";
    
    public static String sLogPath = "";
    public static String sSerializablePath = "";
    public static String sRootPath = "";
    public static String sConfigFilePath = "";
    public static String sConfigPath = "";
    public static String sWordsPath = "";
    public static String sImagePath = "";
    public static String sWordsEatPath = "";
    public static String sWordsDrinkPath = "";
    public static String sWordsMoodPath = "";
    public static String sWordsSleepPath = "";
    public static String sWordsSpeakPath = "";
    public static String sPlugin = "";
    public static String soutPlugin = "";
    
    private static Context mContext;
    
    public static void initPath(Context context){
        mContext = context.getApplicationContext();
        Resources res = mContext .getResources();
        
        //init root path
        sRootPath = Configs.EXTERNAL_PATH+File.separator+res.getString(R.string.path_root)+File.separator;
        
        makeFilePathExist(sRootPath);
        
        
//        sImagePath = sRootPath+res.getString(R.string.path_image)+File.separator;
//        makeFilePathExist(sImagePath);
        

        if(SDCardTools.ExistSDCard()){
//            Configs.EXTERNAL_PATH =  Environment.getExternalStorageDirectory().getPath();
        }else{
        }
        /**鱿鱼4.1后谷歌禁止冲外部目录加载apk或者dex，所以插件只能放到程序内部目录中*/
        soutPlugin=  context.getFilesDir().getAbsolutePath()+File.separator+res.getString(R.string.path_plugin)+File.separator;
        makeFilePathExist(soutPlugin);
        sPlugin = sRootPath+res.getString(R.string.path_plugin)+File.separator;
        makeFilePathExist(sPlugin);
        
        
        initLogPath(res);
        
        sConfigPath = sRootPath+res.getString(R.string.path_config)+File.separator;
        makeFilePathExist(sConfigPath);
        
        sConfigFilePath = sConfigPath+res.getString(R.string.filename_config);
//        sConfigFilePath = initPath(res,res.getString(R.string.path_config),res.getString(R.string.filename_config));
//        sSerializablePath = initPath(res,res.getString(R.string.path_config),"ser.txt");
//        sConfigPath = Configs.EXTERNAL_PATH+File.separator+res.getString(R.string.path_root)+File.separator+res.getString(R.string.path_config)+File.separator+res.getString(R.string.filename_config);

        
//        sWordsPath = initPath(res,res.getString(R.string.path_word),res.getString(R.string.filename_word));
        
//        Configs.EXTERNAL_PATH+File.separator+res.getString(R.string.path_root)+File.separator+res.getString(R.string.path_word)+File.separator+res.getString(R.string.filename_word);
        
        
    }

    public  static void initLogPath(){

        Resources res = mContext .getResources();
        initLogPath(res);
    }
    
    /**
     * @param res
     */
    private static void initLogPath(Resources res) {
        initPath(res,res.getString(R.string.path_log),res.getString(R.string.filename_log));
        
        sLogPath = Configs.EXTERNAL_PATH+File.separator+res.getString(R.string.path_root)+File.separator+res.getString(R.string.path_log)+File.separator+res.getString(R.string.filename_log);
    }
    
    public static String initPath(Resources res,String pathname,String name) {

        String path = Configs.EXTERNAL_PATH+File.separator+res.getString(R.string.path_root)+File.separator+pathname+File.separator;
        String filePath = path+name;
        try{
            LogTools.log("mkdir path: "+path+" filePath: "+filePath);
            makeFilePathExist(path);
            File file = new File(filePath);
            if(!file.exists()){
                boolean succes = file.createNewFile();
                if(!succes){
                    LogTools.log("mkdir failed "+filePath);
                }
                if(!file.exists()){
                    LogTools.log("mkdir failed exist");
                }
            }
        }catch(Exception e){
            LogTools.log("",e);
        }
        return filePath;

    }

    /**
     * @param path
     */
    public static boolean makeFilePathExist(String path) {
        File dir =new File(path);
        if(!dir.exists()){
            boolean succes = dir.mkdir();
            if(!succes){
                LogTools.log("mkdir failed "+path);
            }
            return succes;
        }
        return false;
    }
    public static boolean makeFileExist(String path) throws IOException {
    	File dir =new File(path);
    	if(!dir.exists()){
    		boolean succes = dir.createNewFile();
    		if(!succes){
    			LogTools.log("createNewFile failed "+path);
    		}
    		return succes;
    	}
    	return false;
    }

    /**save Serializable object*/
	public static void saveOAuth(Object obj) {

        File f = new File(sSerializablePath);
        if (!f.exists()) {
        }else{
            DebugTools.log( "Serializa file   exist and delete");
            f.delete();
        }
        Resources res = mContext .getResources();
        initPath(res,res.getString(R.string.path_config),"ser.txt");
//            FilePathTools.initLogPath();
        f = new File(sSerializablePath);
        if (!f.exists()) {
            DebugTools.log( "after create Serializa file not exist");
            return;
        }
        
		try {
			FileOutputStream fs = new FileOutputStream(sSerializablePath,false);//not append ,just orriwte
			ObjectOutputStream os = new ObjectOutputStream(fs);
			os.writeObject(obj);
			os.close();
		} catch (Exception ex) {
        	DebugTools.log(ex);
		}
	}

    
    
}

