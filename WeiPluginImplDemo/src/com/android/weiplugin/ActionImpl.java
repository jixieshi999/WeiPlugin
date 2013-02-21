package com.android.weiplugin;

import java.util.ArrayList;
import java.util.Properties;

import android.util.Log;

import com.android.weiplugin.action.Action;
import com.android.weiplugin.data.Command;

public class ActionImpl implements Action {

    public static String TAG = "ActionImpl";
    
    public static String CMD_TEST_ACTION = "_showLog";
    public static String CMD_TEST_ACTION_2 = "_showLog_a";
    
    
    public Command action(Command arg0) {
        Command result = new Command();
        // TODO Auto-generated method stub
        if(arg0.keyword.equals(CMD_TEST_ACTION)){
            Log.v(TAG, "log from actionimpl,action ");
            result.param="test return param";
            result.extra = Command.Result.SUCCECSS;
        }else if(arg0.keyword.equals(CMD_TEST_ACTION_2)){
            Log.v(TAG, "log from actionimpl,action 2 ");
            result.param="test return param 2";
            result.extra = Command.Result.SUCCECSS;
        }
        return result;
    }


    public String getName() {
        // TODO Auto-generated method stub
        return null;
    }


    public Command getMainCommand() {
        Command result = new Command();
        result.keyword = CMD_TEST_ACTION;
        // TODO Auto-generated method stub
        return result;
    }
    public Properties getPluginProperties() {
        Properties proper = new Properties();
        proper.put("name", "actionImpl");
        ArrayList<String> CommandList = new ArrayList<String>();
        CommandList.add(CMD_TEST_ACTION);
        CommandList.add(CMD_TEST_ACTION_2);
        proper.put("CommandList", CommandList);
        
        CommandList = new ArrayList<String>();
        CommandList.add("action实现1");
        CommandList.add("action实现2");
        proper.put("NameList", CommandList);
        
        CommandList = new ArrayList<String>();
        CommandList.add("action实现1");
        CommandList.add("action实现2");
        proper.put("DiscriptionList", CommandList);
        
        ArrayList<Integer> ShowList = new ArrayList<Integer>();
        ShowList.add(0);
        ShowList.add(0);
        proper.put("ShowList", ShowList);
        return proper;
    }
}
