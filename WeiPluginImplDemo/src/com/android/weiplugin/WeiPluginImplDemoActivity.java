package com.android.weiplugin;

import java.util.Properties;

import android.app.Activity;
import android.os.Bundle;

import com.android.weiplugin.action.PluginAction;
import com.android.weiplugin.action.PluginTools;
import com.android.weiplugin.data.PluginCommand;

public class WeiPluginImplDemoActivity extends Activity implements  PluginAction{
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
    }

    public WeiPluginImplDemoActivity() {
        super(); 
        mPluginTools = new PluginTools();
        mPluginTools.addPluginClass(ActionImpl.class, PluginCommand.Type.Normal);
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