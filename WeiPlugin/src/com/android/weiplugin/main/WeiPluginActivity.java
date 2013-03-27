package com.android.weiplugin.main;

import java.util.ArrayList;
import java.util.Map;
import java.util.Properties;
import java.util.Random;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.weiplugin.BaseActivity;
import com.android.weiplugin.R;
import com.android.weiplugin.action.Action;
import com.android.weiplugin.data.Command;
import com.android.weiplugin.data.PluginCommand;
import com.android.weiplugin.data.PluginEntry;
import com.android.weiplugin.log.LogTools;
import com.android.weiplugin.plugins.PluginManager;
import com.android.weiplugin.tools.DebugTools;
import com.android.weiplugin.tools.PluginTools;

/**
 * 主程序
 * */
public class WeiPluginActivity  extends BaseActivity implements Action{
    /** Called when the activity is first created. */

    public static final String TAG = "weibot";
    
    View layoutContainerSearch;
    TextView mBtnAdd;
//  Button mBtnClear;
    EditText mEdtWords;
    TextView mTxtView;
    
    StringBuilder mTxt ;
    LinearLayout mLayout ;
    
    LinearLayout mBottomBarScroll ;
    Button mBtnToolBarLeft;
    LinearLayout mContentLayout ;
    boolean mScreenOnAll = true;
    boolean mMiddleBarShow = false;
    
    public static Context context;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        initLayout();
        context = this;

        Command cmd = new Command();
        cmd.keyword = PluginManager.INIT_ALL_ACTION;
        handleUICommand(cmd);
        getPluginManager().handleAction(cmd);
        
        ArrayList<Command> result = null;
    }

    void initLayout(){
        
        mTxt = new StringBuilder();
        
        mBtnAdd = (TextView)findViewById(R.id.btnAdd);
        findViewById(R.id.linear_contain_search).setVisibility(View.GONE);
//      mBtnAdd.onTouchEvent(event)
//      mBtnClear = (Button)findViewById(R.id.btnClear);
        mEdtWords = (EditText)findViewById(R.id.edtWords);
        mBtnAdd.setOnClickListener(mOnClickListener);
//      mBtnClear.setOnClickListener(mOnClickListener);
        
        
        mBtnToolBarLeft = (Button)findViewById(R.id.tool_bar_left);
        mBtnToolBarLeft.setText("刷新");
        mContentLayout = (LinearLayout)findViewById(R.id.content_layout);
        mBtnToolBarLeft.setOnClickListener(mOnClickListener);
        
        mBottomBarScroll = (LinearLayout)findViewById(R.id.bottom);
        
        //暂时将右按钮隐藏起来
//      mBtnToolBarRight.setVisibility(View.INVISIBLE);
        mBtnToolBarLeft.setText("");
        
    }
    
    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public Command action(Command ori) {
        if(ori.keyword.equals(PluginManager.INIT_ALL_ACTION)){
            //save action entry to database
            DebugTools.log("main INIT_ALL_ACTION");
            handler.sendEmptyMessage(MSG_LOAD_UI_PLUGIN);
        }
        return null;
    }

    @Override
    public Command getMainCommand() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

    @Override
    public Properties getPluginProperties() {
        return null;
    }

    @Override
    protected void onStart() {
        updateShowEntryCommandData();
        super.onStart();
    }

    public void performClick(){
        DebugTools.log("perform click ");
        
        performClick(458,100);
    }



    public static final int MSG_PERFORM_CLICK = 2;
    public static final int MSG_LOAD_UI_PLUGIN = 3;
    
    
    Handler handler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
            case MSG_PERFORM_CLICK:
                performClick();
                break;
            case MSG_LOAD_UI_PLUGIN:
//              loadUIPlugin(new Command());
                updateShowEntryCommandData();
                break;

            default:
                break;
            }
            super.handleMessage(msg);
        }};

//      public void loadUIPlugin(Command cmd){
//          
//          mLayout.removeAllViews();
//          ActionLoader.runUIAction(PluginManager.ResultAction,this, mContentLayout,cmd);
//          
//      }
    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        
        @Override
        public void onClick(View v) {
            switch(v.getId()){ 
            case R.id.btnAdd:
//              performClick();
//              handler.sendEmptyMessageDelayed(MSG_PERFORM_CLICK, 1000);
                String str = mEdtWords.getText().toString();
                DebugTools.log("add : "+str);
                LogTools.logToFile("main", "add command : "+str);
                break;
            case R.id.tool_bar_left:
                Command cmd = new Command();
                cmd.keyword="asd";
                boolean flag = handleUICommand(cmd);
                break;
                
            }
            if (v instanceof CommandButton){
                CommandButton btn = (CommandButton)v;
                DebugTools.log("BottomButton onclick : "+btn.getText().toString());
                Command cmd = btn.getCommand();
                if(null==cmd){
                    return;
                }
                int type = cmd.pluginType;
                switch (type) {
                case PluginEntry.Type.UI:
                    boolean flag = handleUICommand(cmd);
                    if(!flag){
//                      loadUIPlugin(cmd);
                    }
                    break;
                default:
                    getPluginManager().handleAction(cmd);
                    break;
                }
            }
            
        }

    };
    public boolean handleUICommand(Command cmd) {
        boolean flag = handleUIAction(mContentLayout, WeiPluginActivity.this, cmd);
        return flag;
    }

    protected boolean handleUIAction(LinearLayout layout,Context context,Command cmd){
            return getPluginManager().handleUIAction(layout, context, cmd);
    }
    protected void addAction(Action actoin){
        getPluginManager().addAction(actoin);
    }
    PluginManager getPluginManager(){
        if(null==mPluginManager){
            mPluginManager = new PluginManager();
        }
        return mPluginManager;
    }
    public void initAction(Action actoin){
        mPluginManager = getPluginManager();
        mPluginManager.addAction(actoin);
    }
    
    Context mContext;

    PluginManager mPluginManager;
    
    @Override
    protected void onStop() {
        super.onStop();
    }

    int BOTTOM_BUTTON = 90;
    private void updateShowEntryCommandData() {
        DisplayMetrics dm = new DisplayMetrics();  
        getWindowManager().getDefaultDisplay().getMetrics(dm);  
        Command command =null;
//        tv.setText("屏幕分辨率为:"+dm.widthPixels+" * "+dm.heightPixels);   
        int tmp  = dm.widthPixels/5;
        BOTTOM_BUTTON = tmp<=0?BOTTOM_BUTTON:tmp;
        
        ArrayList<PluginCommand> mPluginEntryList=PluginTools.readShowPluginFromDatabase();
        Map<String, String> it = null;
        mBottomBarScroll.removeAllViews();
        for(PluginCommand pluginEntry:mPluginEntryList){
            command = new Command();
            command.keyword=pluginEntry.cmd.keyword;
            command.words = pluginEntry.name;
            command.pluginType = pluginEntry.type;
            addCommandBtn(command);
        }
            
//        command = new Command();
//        command.keyword="_setting";
//        command.words = getString(R.string.setting);
//        command.pluginType = PluginEntry.Type.Setting;
//        addCommandBtn( command);
        
    }

    private void addCommandBtn(Command cmd) {
        LinearLayout.LayoutParams param = null;
        
        CommandButton btn = new CommandButton(this,cmd);
        btn.setText(cmd.words);
//      btn.type = type;
//      btn.setCommand(cmd);
        param = new LinearLayout.LayoutParams(BOTTOM_BUTTON, LinearLayout.LayoutParams.FILL_PARENT);
        param.leftMargin = 0;
        
        mBottomBarScroll.addView(btn,param);
        btn.setOnClickListener(mOnClickListener);
//        btn.setBackgroundResource(R.drawable.aa_botton_selector);
//      btn.setBackgroundResource(R.drawable.aa_botton_gray_selector);
        btn.setGravity(Gravity.CENTER);
        btn.setTextColor(Color.BLACK);
    }
    

    static class CommandButton extends TextView{
        
        public interface Type{
            int TAB_LOCAL = 1;
            int TAB_PLUGIN = 4;
            int SETTING =2;
            int COMMAND =3;
        }
        
        Command mCommand ;
        public void setCommand(Command command) {
            mCommand = command;
        }

        public Command getCommand(){
            Command command =null;
            if(null!=mCommand){
                command = mCommand.clone();
            }
            return command;
        }
        
        public int type ;
        
        public CommandButton(Context context,Command command) {
            super(context);
            type = Type.TAB_LOCAL;
            mCommand = command;
        }

        public CommandButton(Context context, AttributeSet attrs, int defStyle) {
            super(context, attrs, defStyle);
            type = Type.TAB_LOCAL;
        }

        public CommandButton(Context context, AttributeSet attrs) {
            super(context, attrs);
            type = Type.TAB_LOCAL;
        }
        
    }
}