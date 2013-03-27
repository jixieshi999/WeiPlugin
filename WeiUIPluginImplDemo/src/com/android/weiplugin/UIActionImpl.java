package com.android.weiplugin;

import java.util.ArrayList;
import java.util.Properties;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.animation.RotateAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.weibot.apis.thread.AsynHandler;
import com.android.weibot.apis.thread.AsyncHandlerFactory;
import com.android.weiplugin.action.Action;
import com.android.weiplugin.action.UIPlugin;
import com.android.weiplugin.data.Command;

public class UIActionImpl implements UIPlugin {

    public static String TAG = "ActionImpl";
    
    public static String CMD_TEST_ACTION = "_uishowLog";
    public static String CMD_TEST_ACTION_2 = "_uishowLog_a";
    public static String CMD_TEST_UIACTION = "_uiactionshowLog";
    public static String CMD_TEST_UIACTION_2 = "_uiactionshowLog_a";
    

    LinearLayout mLayout;
    Context mContext;
    AsynHandler mAsynHandler ;
    
    Toast toast=null;
    
    public UIActionImpl() {
        super();
        mAsynHandler = AsyncHandlerFactory.createAsynHandler(AsyncHandlerFactory.Mode.MODE_NET, mCallBack);
    }


    AsynHandler.CallBack mCallBack = new AsynHandler.CallBack(){

        public void handleMessage(Message msg) {
            Message msgge =  null;
            switch (msg.what) {
            case MSG_GET_RSS:
                if(null!=handler){
                    handler.sendEmptyMessage(MSG_DIALOG_SHOW);
                    msgge = handler.obtainMessage(MSG_GET_RSS);
                    handler.sendMessage(msgge);
                }
                break;
            case MSG_GET_RSS_DATA:
                if(null!=handler){
                    handler.sendEmptyMessage(MSG_DIALOG_SHOW);
                    msgge = handler.obtainMessage(MSG_GET_RSS_DATA);
                    handler.sendMessage(msgge);
                }
                break;

            default:
                break;
            }
        }
        
    };
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
        CommandList.add(CMD_TEST_UIACTION);
        CommandList.add(CMD_TEST_UIACTION_2);
        proper.put("CommandList", CommandList);
        
        CommandList = new ArrayList<String>();
        CommandList.add("ui1");
        CommandList.add("ui2");
        CommandList.add("UI界面1");
        CommandList.add("UI界面2");
        proper.put("NameList", CommandList);
        
        CommandList = new ArrayList<String>();
        CommandList.add("ui1");
        CommandList.add("ui2");
        CommandList.add("UI界面1");
        CommandList.add("UI界面2");
        proper.put("DiscriptionList", CommandList);
        
        ArrayList<Integer> ShowList = new ArrayList<Integer>();
        ShowList.add(0);
        ShowList.add(0);
        ShowList.add(0);
        ShowList.add(0);
        proper.put("ShowList", ShowList);
        return proper;
    }



    final int MSG_GET_RSS = 12;
    final int MSG_GET_RSS_DATA = 16;
    final int MSG_DIALOG_DISMISS = 13;
    final int MSG_DIALOG_SHOW = 14;
    
    public void clear() {
        
    }

    Handler handler=null;
    public Command pluginUI(LinearLayout arg0, Context arg1, Command arg2) {

        Command result = new Command();
        if(arg2.keyword.equals(CMD_TEST_UIACTION)){
            Log.v(TAG, "log from actionimpl,action "+CMD_TEST_UIACTION);

            resetHandler();
            mLayout = arg0;
            mContext = arg1;
            Message msg = mAsynHandler.obtainMessage(MSG_GET_RSS_DATA);
            msg.obj = arg2.param;
            mAsynHandler.sendMessage(msg);
            result.param="test return param";
            result.extra = Command.Result.SUCCECSS;
        }else if(arg2.keyword.equals(CMD_TEST_UIACTION_2)){
            Log.v(TAG, "log from actionimpl,action 2 "+CMD_TEST_UIACTION_2);

            resetHandler();
            mLayout = arg0;
            mContext = arg1;
            Message msg = mAsynHandler.obtainMessage(MSG_GET_RSS);
            msg.obj = arg2.param;
            mAsynHandler.sendMessage(msg);
            
            result.param="test return param 2";
            result.extra = Command.Result.SUCCECSS;
        }
        return result;
    }


    /**
     * 重设主Handler
     * */
    private void resetHandler() {
        handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                TranslateAnimation anim = null;
                switch (msg.what) {
                case MSG_DIALOG_SHOW:
                    if(null==toast){
                        toast = Toast.makeText(mContext, "loading", Toast.LENGTH_LONG);
                    }
                    toast.setText("loading");
                    toast.show();
                    break;
                case MSG_DIALOG_DISMISS:
                    if(null==toast){
                        toast = Toast.makeText(mContext, "end loading", Toast.LENGTH_LONG);
                    }
                    toast.setText("end loading");
                    toast.show();
                    break;
                case MSG_GET_RSS:
                    mLayout.removeAllViews();
                    TextView tv = new TextView(mContext);
                    tv.setText("uiaction2");
                      anim = new TranslateAnimation(0, 100, 0, 1);
                    anim.setDuration(1000);
                    tv.startAnimation(anim);
                    mLayout.addView(tv);
                    break;
                case MSG_GET_RSS_DATA:
//                    mLayout.removeAllViews();
                    TextView tv2 = new TextView(mContext);
                    tv2.setText("uiaction1");
                      anim = new TranslateAnimation(200, 0, 1, 1);
                    anim.setDuration(1000);
                    tv2.startAnimation(anim);
                    mLayout.addView(tv2);
                    break;
                }
                super.handleMessage(msg);
            }
            
        };
    }
}
