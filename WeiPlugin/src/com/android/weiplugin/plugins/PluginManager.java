package com.android.weiplugin.plugins;

import java.util.ArrayList;
import java.util.Properties;

import android.content.Context;
import android.widget.LinearLayout;

import com.android.weiplugin.Global;
import com.android.weiplugin.action.Action;
import com.android.weiplugin.action.UIPlugin;
import com.android.weiplugin.actionloader.ActionLoader;
import com.android.weiplugin.data.Command;
import com.android.weiplugin.data.PluginEntry;
import com.android.weiplugin.log.LogTools;
import com.android.weiplugin.tools.PluginTools;
/**
 * manage all the actions ,kernel action ,..action and plugin acion and UI plugin action 
 * 插件管理，管理所有的插件
 * 
 * @author jixieshi@me.com 20121207 
 * forgive my poor English
 * */
public class PluginManager {

	public static final String TAG = "PluginManager";
	
	public static final String INIT_ALL_ACTION = "_init123456789";
	
    public PluginManager() {
		super();
//		mContext = context;
		initAction();
		// TODO Auto-generated constructor stub
		
	}
	Context mContext;

    ArrayList<Action> mKernelCacheActionList = null;
    ArrayList<UIPlugin> mUICacheActionList = null;
    ArrayList<Action> mSystemActionList = null;
    ArrayList<Action> mViewActionList = null;
    ArrayList<Action> mNormalActionList = null;


    public static int sequence = 1;
    /**
     * handle Kernel action first 
     * then System Actions
     * then Normal 
     * ...
     * 
     * */
    public void handleAction(Command result){
    	ArrayList<UIPlugin> uilists = getSystemUICacheActionList();
    	 for(Action action:uilists){
 	        Command res = action.action(result);
 	        if(null!=ResultAction.action(res)){
 	            break;//already handle this command
 	        }
 	    }
    	
    	ArrayList<Action> lists = getKernelCacheActionList();
    	ArrayList<Action> Systemlists = getSystemActionList();
    	ArrayList<Action> Normallists = getNormalActionList();
    	if(result.keyword.equals(INIT_ALL_ACTION)){
    		//save action entry to database
    		for(Action action:lists){
    			insertActionInfo(action,PluginEntry.Type.Kernel);
    		}
    		for(Action action:Systemlists){
    			insertActionInfo(action,PluginEntry.Type.System);
    		}
    		for(Action action:Normallists){
    			insertActionInfo(action,PluginEntry.Type.Normal);
    		}
    	}
	    handleActions(result, lists);

	    
        handleActions(result, Systemlists);
        
        handleActions(result, Normallists);
        
        /**处理外部加载的插件*/
        ActionLoader.handlePluginAction(ResultAction, result);
    }
	private void insertActionInfo(Action action,int type) {
		Command cmd = action.getMainCommand();
		if(null!=cmd){
			PluginEntry entry = new PluginEntry();
			entry.clsName =action.getClass().getName();
			entry.type =type ;
			entry.level = sequence;
			Properties pros = action.getPluginProperties();
			if(pros!=null){
				String name = (String)pros.get("name");
				entry.name = name==null?"":name;
				ArrayList<String> CommandList= (ArrayList<String>)pros.get("CommandList");
				int len = CommandList==null?0:CommandList.size();
				if(len>0){
					entry.pluginCommandList = CommandList;
				}
				ArrayList<String> nameList= (ArrayList<String>)pros.get("NameList");
				len = nameList==null?0:nameList.size();
				if(len>0){
					entry.pluginCommandNameList = nameList;
				}
				ArrayList<String> DiscriptionList= (ArrayList<String>)pros.get("DiscriptionList");
				len = DiscriptionList==null?0:DiscriptionList.size();
				if(len>0){
					entry.pluginCommandDiscriptionList = DiscriptionList;
				}
				ArrayList<Integer> ShowList= (ArrayList<Integer>)pros.get("ShowList");
				len = ShowList==null?0:ShowList.size();
				if(len>0){
					entry.pluginCommandShowList = ShowList;
				}
			}
			sequence = PluginTools.addPluginCommandToDatabase(entry);
//        			PluginTools.addPluginToDatabase(action.getClass().getName(), PluginEntry.Type.Kernel, sequence++, cmd,action.getName());
		}
	}
    public boolean handleUIAction(LinearLayout layout,Context context,Command cmd){
    	boolean flag = false;
    	ArrayList<UIPlugin> lists = getSystemUICacheActionList();
    	for(UIPlugin action:lists){
	        Command res = action.pluginUI(layout, context, cmd);
	        if(null!=ResultAction.action(res)){
	        	return true;
//	            break;//already handle this command
	        }
	    }
    	if(cmd.keyword.equals(INIT_ALL_ACTION)){
        	//save action entry to database
        	for(UIPlugin action:lists){
        		insertActionInfo(action,PluginEntry.Type.UI);
        	}
        }
    	flag = ActionLoader.runUIAction(ResultAction,context, layout,cmd);
    	return flag;
    }
    
    void initAction(){
        
        ArrayList<Action> lists = getKernelCacheActionList();
        ArrayList<UIPlugin> uiLists = getSystemUICacheActionList();
//        lists.add(loginAction);
        lists = getSystemActionList();
        lists.add(new RobotPlugin());
        uiLists.add(new WeidataUIPlugin());
//        uiLists.add(new TweetsPlugin());
//        lists.add(new SharePlugin());
//        lists.add(new SimsimiPlugin());
//        lists.add(new WeiDataPlugin());
        lists = getNormalActionList();
        lists.add(new ConfigPlugin());
        lists.add(mPluginAction);
    }

    public void addAction(Action actoin){
    	ArrayList<Action> lists =  getNormalActionList();
        lists.add(actoin);
    }
    
    /**获取系统中的UI插件*/
	ArrayList<UIPlugin> getSystemUICacheActionList(){
	    if(null==mUICacheActionList){
	    	mUICacheActionList = new ArrayList<UIPlugin>();
	    }
	    return mUICacheActionList;
	}
	ArrayList<Action> getKernelCacheActionList(){
		if(null==mKernelCacheActionList){
			mKernelCacheActionList = new ArrayList<Action>();
		}
		return mKernelCacheActionList;
	}
	ArrayList<Action> getNormalActionList(){
	    if(null==mNormalActionList){
	        mNormalActionList = new ArrayList<Action>();
	    }
	    return mNormalActionList;
	}
	ArrayList<Action> getViewActionList(){
	    if(null==mViewActionList){
	        mViewActionList = new ArrayList<Action>();
	    }
	    return mViewActionList;
	}
	ArrayList<Action> getSystemActionList(){
	    if(null==mSystemActionList){
	        mSystemActionList = new ArrayList<Action>();
	    }
	    return mSystemActionList;
	}
	
	/**
	 * take care the all the actions result,it is an action too
	 * 处理所有action的返回Command结果
	 * action 返回空表示成功处理该Command，null表示处理Command失败
	 * */
	public static Action ResultAction = new Action(){

        @Override
		public Command getMainCommand() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
        public Command action(Command ori) {
            if(ori==null){
                return null;
            }
            String result = ori.extra;
            if(result.equals(Command.Result.ERROR)){
                
            } else if(result.equals(Command.Result.SUCCECSS)){
                return new Command();
            } else if(result.equals(Command.Result.FILTE)){
                
            } else if(result.equals(Command.Result.FAILED)){
                
            }else if(result.equals(Command.Result.HANDLE_AGAIN)){
              //log
                /**参数为1时需要确认是否有该命令*/
//                if(null!=ori.param&&ori.param.equals(Command.Result.HANDLE_CONFIRM)){
//                	ori.param="";
//                    WeibotTools.addCommand(ori,true);
//                }else{
//                    WeibotTools.addCommand(ori);
//                }
              return new Command();
            }      
            return null;
        }

        @Override
        public String getName() {
            // TODO Auto-generated method stub
            return "result Action";
        }

		@Override
		public Properties getPluginProperties() {
			// TODO Auto-generated method stub
			return null;
		}
        
    };
    
    /**
     * handle action list
     * */
    private void handleActions(Command result, ArrayList<Action> lists) {
        for(Action action:lists){
	        Command res = action.action(result);
	        if(null!=ResultAction.action(res)){
	            break;//already handle this command
	        }
	    }
    }


    /**
     * handle plugin action
     * */
    Action mPluginAction = new Action(){
    	
        @Override
		public Command getMainCommand() {
			// TODO Auto-generated method stub
			return null;
		}
		@Override
        public Command action(Command ori) {
            Command result = new Command();
            /*if(ori.keyword.equals("_plugin")){
                LogTools.log("weibot doLogin");
                result.extra = Command.Result.SUCCECSS;
            }else */if(ori.keyword.equals("_reloadplugin")){
                LogTools.log("weibot doLogin");
                ActionLoader.reloadClassPath(Global.sContext);
                result.extra = Command.Result.SUCCECSS;
            }else if(ori.keyword.equals("_uiplugin")){
                LogTools.log("weibot doLogin");
//                ActionLoader.reloadClassPath(Globle.sContext);
//                result.extra = Command.Result.SUCCECSS;
            }
            return result;
        }
        @Override
        public String getName() {
            return "mActionPlugin";
        }
		@Override
		public Properties getPluginProperties() {
			// TODO Auto-generated method stub
			return null;
		}
    };
//	    Action shareAction = new Action(){};
//	    Action robotAction = new Action(){};

//	    Action loginAction = new Action(){};
}
