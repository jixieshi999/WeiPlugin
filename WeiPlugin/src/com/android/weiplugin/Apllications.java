package com.android.weiplugin;

import android.app.Application;
import android.content.res.Configuration;
import android.os.Environment;

import com.android.weiplugin.R;
import com.android.weiplugin.config.Configs;
import com.android.weiplugin.log.LogTools;
import com.android.weiplugin.tools.DebugTools;
import com.android.weiplugin.tools.FilePathTools;
import com.android.weiplugin.tools.SDCardTools;

public class Apllications extends Application {

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        // TODO Auto-generated method stub
        super.onConfigurationChanged(newConfig);
    }

    @Override
    public void onCreate() {
        // TODO Auto-generated method stub
        LogTools.log("application onCreate ");
        init();
//        CrashHandler crashHandler = CrashHandler.getInstance();  
//        crashHandler.init(getApplicationContext()); 
        super.onCreate();
    }
    void init(){
//        Configs.DEBUG = getResources().getBoolean(R.bool.debug);
        Configs.DEBUG = true;
        Global.init(getApplicationContext());
        if(SDCardTools.ExistSDCard()){
            Configs.EXTERNAL_PATH =  Environment.getExternalStorageDirectory().getPath();
        }else{
            Configs.EXTERNAL_PATH =  getFilesDir().getAbsolutePath();
        }
        DebugTools.log("external path ："+Configs.EXTERNAL_PATH );
        FilePathTools.initPath(this);
//        ConfigTools.initProperty(this);
//        bHuman human = ConfigTools.readpropertyFromConfig();
//        if(human==null/*||human.getId().equals("")*/){
//        	FilePathTools.initPath(getResources(),getResources().getString(R.string.path_config),getResources().getString(R.string.filename_config));
//        	ConfigTools.writeDefaultConfigtoFile(human);
//        	human = ConfigTools.readpropertyFromConfig();
//        }
//		System.setProperty("weibo4j.oauth.consumerKey", Weibo.CONSUMER_KEY);
//    	System.setProperty("weibo4j.oauth.consumerSecret", Weibo.CONSUMER_SECRET);
    }
    
    @Override
    public void onTerminate() {
        // TODO Auto-generated method stub
        super.onTerminate();
    }

}
