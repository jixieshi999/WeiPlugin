package com.android.weiplugin;

import java.util.Properties;

import com.android.weibot.apis.thread.AsynHandler;
import com.android.weibot.apis.thread.AsyncHandlerFactory;
import com.android.weiplugin.action.PluginTools;
import com.android.weiplugin.data.PluginCommand;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Message;
import android.widget.LinearLayout;

public class WeiUIPluginImplDemoActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public WeiUIPluginImplDemoActivity() {
        super(); 
        mPluginTools = new PluginTools();
        mPluginTools.addPluginClass(UIActionImpl.class, PluginCommand.Type.UI);
    }
    PluginTools mPluginTools;
    public String getPluginClassName() {
        // TODO Auto-generated method stub
        return mPluginTools.getPluginClassName();
    }

    public String getPluginExtra() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getPluginName() {
        // TODO Auto-generated method stub
        return null;
    }

    public Properties getPluginProperties() {
        // TODO Auto-generated method stub
        return null;
    }

    public String getPluginType() {
        // TODO Auto-generated method stub
        return mPluginTools.getPluginTypeName();
    }

    public String getPluginVersion() {
        // TODO Auto-generated method stub
        return null;
    }
    
}