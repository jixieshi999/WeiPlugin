package com.android.weiplugin.plugins;

import java.util.Properties;


import com.android.weiplugin.action.Action;
import com.android.weiplugin.config.Configs;
import com.android.weiplugin.data.Command;

/**
 * 载入配置文件接口实现
 * **/
public class ConfigPlugin implements Action{

    public static final String TAG = "SettingPlugin";
    
    public static final String CMD_CONFIG_LOAD_ROPERTY = "_config_load_property";
    
    @Override
    public Command action(Command ori) {
        Command result = new Command();
        if(ori.keyword.equals(CMD_CONFIG_LOAD_ROPERTY)){
            Configs.loadProperties();
            result.extra = Command.Result.SUCCECSS;
        } 
        return result;
    }

    @Override
    public Command getMainCommand() {
//        Command result = new Command();
//        result.keyword = CMD_CONFIG_LOAD_ROPERTY;
        return null;
    }

    @Override
    public String getName() {
        // TODO Auto-generated method stub
        return CMD_CONFIG_LOAD_ROPERTY;
    }

	@Override
	public Properties getPluginProperties() {
		// TODO Auto-generated method stub
		return null;
	}

}
