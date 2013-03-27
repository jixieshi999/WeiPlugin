package com.android.weiplugin.plugins;

import java.util.ArrayList;
import java.util.Properties;
import java.util.Random;

import android.content.Intent;
import android.view.KeyEvent;
import android.widget.Toast;

import com.android.weiplugin.Global;
import com.android.weiplugin.Robot;
import com.android.weiplugin.action.Action;
import com.android.weiplugin.data.Command;
import com.android.weiplugin.log.LogTools;

/**
 * 控制接口的实现，可以播放音乐，显示悬浮窗口等，可扩展实现
 * **/
public class RobotPlugin implements Action {

    Robot robot = null;
    Robot getRobot(){
        if(robot==null){
            robot = new Robot(Global.sContext);
        }
        return robot;
    }
    void next() {
    	sengMediaIntent(KeyEvent.KEYCODE_MEDIA_NEXT);
		Toast.makeText(Global.sContext, "next", Toast.LENGTH_LONG).show();
    }
    void sengMediaIntent(int keyCode){
		// create a new intent specifically aimed at the current registered
		// listener
		// 构造一个Intent对象 ，并且赋予Action和KeyEvent
		Intent targetedIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
		// 构造一个KeyEvent对象
		KeyEvent keyEvent = new KeyEvent(KeyEvent.ACTION_DOWN,keyCode);
		// 作为附加值添加至mbIntent对象中
		targetedIntent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);

		// 手动发送该广播至目标对象去处理，该广播不再是系统发送的了
		Global.sContext.sendBroadcast(targetedIntent, null);

		targetedIntent = new Intent(Intent.ACTION_MEDIA_BUTTON);
		keyEvent = new KeyEvent(KeyEvent.ACTION_UP,keyCode);
		// 作为附加值添加至mbIntent对象中
		targetedIntent.putExtra(Intent.EXTRA_KEY_EVENT, keyEvent);
		Global.sContext.sendBroadcast(targetedIntent, null);
    }
	void play() {
		sengMediaIntent(KeyEvent.KEYCODE_MEDIA_PLAY_PAUSE);
		Toast.makeText(Global.sContext, "play", Toast.LENGTH_LONG).show();
	}
    
    @Override
    public Command action(Command ori) {
        Command result = new Command();
        if(ori.keyword.equals("_RobotShow")){
            getRobot().showRobot();
            result.extra = Command.Result.SUCCECSS;
        }else if(ori.keyword.equals("_RobotHide")){
            getRobot().hideRobot();
            result.extra = Command.Result.SUCCECSS;
        }else if(ori.keyword.equals("_next")){
            next();
            result.extra = Command.Result.SUCCECSS;
        }else if(ori.keyword.equals("_play")){
            play();
            result.extra = Command.Result.SUCCECSS;
        }else if(ori.keyword.equals("_showtoast")){
            String name = ori.param;
            if(null==name||name.equals("")){
                name =getRandomToast(0);
            }
            if(isNumeric(name)){
                int key = Integer.valueOf(name);
                name = getRandomToast(key);
            }
            Toast.makeText(Global.sContext, name, Toast.LENGTH_LONG).show();
            result.extra = Command.Result.SUCCECSS;
        }else if(ori.keyword.equals("_RobotUpdate")){
            ArrayList<String> params = Command.splitParam(result.getParam());
            int x=0,y=0;
            try{
                switch(params.size()){
                case 0:
                    throw new Exception("no robot position params ");
//                  break;
                case 2:
                    y = Integer.parseInt(params.get(1));
                case 1:
                    x = Integer.parseInt(params.get(0));
                    break;
                }
            }catch (Exception e) {
                LogTools.log("get robot position error", e);
            }
            getRobot().updateRobot(x, y);
            result.extra = Command.Result.SUCCECSS;
        }
        return result;
    }
    @Override
    public String getName() {
        return "robot人";
    }
    public static boolean isNumeric(String str){ 
        for (int i = str.length();--i>=0;){ 
        if (!Character.isDigit(str.charAt(i))){
        return false; 
        } 
        }
        return true; 
        } 
    /**
     * 测试随机显示toast
     * */
    String getRandomToast(int key){
        StringBuilder sb = new StringBuilder();
        Random ran = new Random(System.currentTimeMillis());
        if(key==0){
            key = ran.nextInt(20);
        }
        switch (key) {
        case 1:
            sb.append("-_-!");
            break;
        case 2:
            sb.append("#_#!");
            break;
        case 3:
            sb.append("#_-!");
            break;
        case 4:
            sb.append("-_&!");
            break;
        case 5:
            sb.append("%_&!");
            break;
        case 6:
            sb.append("$_$");
            break;
        default:
            sb.append("^_^!");
            break;
        }
        return sb.toString();
    }
    
	@Override
	public Command getMainCommand() {
        Command result = new Command();
        result.keyword = "_RobotShow";
		// TODO Auto-generated method stub
		return result;
	}
	@Override
	public Properties getPluginProperties() {
	    Properties proper = new Properties();
	    proper.put("name", "robot人");
	    ArrayList<String> CommandList = new ArrayList<String>();
	    CommandList.add("_RobotShow");
	    CommandList.add("_RobotHide");
	    CommandList.add("_play");
	    CommandList.add("_next");
	    proper.put("CommandList", CommandList);
	    
	    CommandList = new ArrayList<String>();
	    CommandList.add("显示Robot");
	    CommandList.add("隐藏Robot");
	    CommandList.add("播放");
	    CommandList.add("下一曲");
	    proper.put("NameList", CommandList);
	    
	    CommandList = new ArrayList<String>();
	    CommandList.add("显示Robot");
	    CommandList.add("隐藏Robot");
	    CommandList.add("调用播放器播放音乐");
	    CommandList.add("下一曲");
	    proper.put("DiscriptionList", CommandList);
	    

	    /**此处用来控制是否默认显示在列表当中，0表示显示，1表示隐藏**/
	    ArrayList<Integer> ShowList = new ArrayList<Integer>();
	    ShowList.add(0);
	    ShowList.add(0);
	    ShowList.add(0);
	    ShowList.add(0);
	    proper.put("ShowList", ShowList);
		return proper;
	}
}
