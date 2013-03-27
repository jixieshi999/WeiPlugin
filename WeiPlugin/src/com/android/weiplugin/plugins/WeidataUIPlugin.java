package com.android.weiplugin.plugins;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnMultiChoiceClickListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.weibot.apis.thread.AsynHandler;
import com.android.weibot.apis.thread.AsyncHandlerFactory;
import com.android.weiplugin.R;
import com.android.weiplugin.action.PluginAction;
import com.android.weiplugin.action.PluginTools;
import com.android.weiplugin.action.UIPlugin;
import com.android.weiplugin.config.Configs;
import com.android.weiplugin.data.Command;
import com.android.weiplugin.data.PluginEntry;
import com.android.weiplugin.log.Debug;
import com.android.weiplugin.tools.FilePathTools;

public class WeidataUIPlugin implements UIPlugin,PluginAction,OnItemClickListener,AsynHandler.CallBack,OnClickListener{

//	public final String RSSFEEDOFCHOICE = "http://www.ibm.com/developerworks/views/rss/customrssatom.jsp?zone_by=XML&zone_by=Java&zone_by=Rational&zone_by=Linux&zone_by=Open+source&zone_by=WebSphere&type_by=Tutorials&search_by=&day=1&month=06&year=2007&max_entries=20&feed_by=rss&isGUI=true&Submit.x=48&Submit.y=14";
//	public final String RSSFEEDOFCHOICE = "http://www.xinhuanet.com/world/news_world.xml";
//	public final String RSSFEEDOFCHOICE = "http://feed.xiaohuayoumo.com/";
//	public final String RSSFEEDOFCHOICE = "http://feed.xiaohuayoumo.com/";
//	public final String RSSFEEDOFCHOICE = "http://weiborss.com/software/reader/rss.jsp?name=http://weibo.com/kaifulee";
    //http://weiborss.com/
	public final String RSSFEEDOFCHOICE = "http://weiborss.com/software/reader/rss.jsp?name=瞬间笑抽你";
	//http://weiborss.com/software/reader/rss.jsp?name=%E7%9E%AC%E9%97%B4%E7%AC%91%E6%8A%BD%E4%BD%A0
	//http://rssing.sinaapp.com/?uid=1774934752
	
	public final String tag = "RSSReader";

	public static String CMD_SHOW_LIST = "_data_list";
	public static String CMD_UPDATE_WEIDATA_END = "_update_weidata_End";
	
	PluginTools pluginTool = null;
	
	final int Page_Type_TXT = 0;
	final int Page_Type_PIC = 1;
	final int MSG_Full_Screen = 3;
	final int MSG_Normal_Screen = 4;
	final int MSG_UPDATE_WEIDATA_END = 5;
	
	static int scroolPos = 0;
	
	/**
	 * <p>Page_Type_TXT
	 * <p>Page_Type_PIC
	 * */
	ArrayList<Integer> selectTypePos = new ArrayList<Integer>();
	ArrayList<Integer> selectGroupPos = new ArrayList<Integer>();
	boolean initSelectType = true;
	boolean initSelectGroup = true;
	
	
	
    public WeidataUIPlugin() {
		super();
		pluginTool = new PluginTools()
		.addPluginClass(getClass(), PluginEntry.Type.UI);
		mAsynHandler = AsyncHandlerFactory.createAsynHandler(AsyncHandlerFactory.Mode.MODE_NET, this);
		selectTypePos.add(0);
		// TODO Auto-generated constructor stub
	}
    
    
	TextView mTxtGroup ,mTxtType;
    View mLayoutContainer = null;

    
    @Override
	public void onClick(View v) {
    	String []dataTypeArray  = null;
    	
	}

    EditText edt = null;
    ListView mDialogListView;
    
    @Override
	public void clear() {
    	mLayout = null;
    	mContext = null;
    	if(null!=mAsynHandler){
    		mAsynHandler.quit();
    	}
	}
    final int MSG_GET_RSS = 12;
    final int MSG_GET_RSS_DATA = 16;
    final int MSG_UPDATE_RSS = 13;
    final int MSG_DIALOG_SHOW = 14;
    final int MSG_DIALOG_DISMISS= 15;
    
    Toast toast = null;
    Handler handler;
	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
	    Message msgg=null;
		switch (msg.what) {
		case MSG_GET_RSS_DATA:
			if(null!=handler){
//				String keyword = (String)msg.obj;
//				handler.sendEmptyMessage(MSG_DIALOG_SHOW);
//				msgg = handler.obtainMessage(MSG_UPDATE_RSS);
//				msgg.obj = WeiDataTools.getWeiDataByGroup(null);
//				handler.sendEmptyMessage(MSG_DIALOG_DISMISS);
//				handler.sendMessage(msgg);
			}
		    break;

		default:
			break;
		}
	}
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
    	
	}
	
	@Override
	public String getPluginName() {
		return null;
	}
	
	@Override
	public String getPluginClassName() {
		return pluginTool.getPluginClassName();
	}
	@Override
	public String getPluginVersion() {
		return null;
	}
	@Override
	public String getPluginExtra() {
		return null;
	}
	@Override
	public String getPluginType() {
		return pluginTool.getPluginTypeName();
	}
	LinearLayout mLayout;
	Context mContext;
	AsynHandler mAsynHandler ;
	@Override
	public Command pluginUI(LinearLayout layout, Context context, Command cmd) {
		if(cmd.keyword.equals(CMD_SHOW_LIST)){
			handler = new Handler(){
				@Override
				public void handleMessage(Message msg) {
					switch (msg.what) {
					case MSG_UPDATE_WEIDATA_END:
//					    setFullScreen(false);
					    break;
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
					}
					super.handleMessage(msg);
				}
		    	
		    };
			mLayout = layout;
			mContext = context;
//			mAsynHandler.sendEmptyMessage(MSG_GET_RSS_DATA);
			Message msg = mAsynHandler.obtainMessage(MSG_GET_RSS_DATA);
			msg.obj = cmd.param;
			mAsynHandler.sendMessage(msg);
			Command command = new Command();
			command.extra = Command.Result.SUCCECSS;
			return command;
		}else{
		}
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public Properties getPluginProperties() {
	    Properties proper = new Properties();
	    proper.put("name", "段子");
	    ArrayList<String> CommandList = new ArrayList<String>();
	    CommandList.add(CMD_SHOW_LIST);
	    proper.put("CommandList", CommandList);
        CommandList = new ArrayList<String>();
        CommandList.add("段子");
        proper.put("NameList", CommandList);
		return proper;
	}
	@Override
	public Command getMainCommand() {
		Command cmd = new Command ();
		cmd.keyword = CMD_SHOW_LIST;
		return cmd;
	}
	@Override
	public Command action(Command ori) {
        Command result = new Command();
//        if(ori.keyword.equals(WeibotActivity.CMD_Full_Screen)){
//        	if(null!=handler){
//        		handler.sendEmptyMessage(MSG_Full_Screen);
//        	}
//        }else if(ori.keyword.equals(WeibotActivity.CMD_Normal_Screen)){
//        	if(null!=handler){
//        		handler.sendEmptyMessage(MSG_Normal_Screen);
//        	}
//        }  
        return result;
    }
	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return "段子";
	}
    
}

