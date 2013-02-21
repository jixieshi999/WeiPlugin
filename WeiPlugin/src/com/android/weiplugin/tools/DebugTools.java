package com.android.weiplugin.tools;

import java.util.ArrayList;


import android.util.Log;

import com.android.weiplugin.config.Configs;
import com.android.weiplugin.data.Command;

/**
 * DebugTools
 * 
 * add by jixieshi@me.com 20121107
 * */
public class DebugTools {
	public static  String TEST_RSS_ADD = "http://feed.xiaohuayoumo.com/";

	public static final String TAG  = "DebugTools";
	
	public static void log(String str){
		log(TAG, str);
	}
	public static void log(Object str){
		log(TAG, "obj : "+str);
	}
	public static void log(Object[] list){
		for(Object str:list)
			log(TAG, "obj : "+str);
	}
	public static void log(String tag,String str){
		if(Configs.DEBUG){
			Log.d(tag, str);
		}
	}
	public static void log(String tag,String str,Throwable e){
		if(Configs.DEBUG){
			Log.e(tag, str,e);
		}
	}
	public static void log(Throwable e){
		log(TAG,"",e);
	}
	public static void log(String tag,Throwable e){
		log(tag,"",e);
	}
	
	
	/**
     * @return
     */
    public static ArrayList<Command> getTestWordsList(String name) {
        ArrayList<Command> lists =new ArrayList<Command>();
        Command word ;
        int num = 8;
        if(name.equals("eat")){
            
                word = new Command();
                word.setWords("i m so hungry ");
                word.setKeyword("eat");
                word.setType(Command.Type.TYPE_MSG);
                word.setTime(System.currentTimeMillis());
                lists.add(word);
                
                word = new Command();
                word.setWords("will u give me some eat");
                word.setKeyword("eat");
                word.setType(Command.Type.TYPE_MSG);
                word.setTime(System.currentTimeMillis());
                lists.add(word);
                
                word = new Command();
                word.setWords("please help me, i m hungry");
                word.setKeyword("eat");
                word.setType(Command.Type.TYPE_MSG);
                word.setTime(System.currentTimeMillis());
                lists.add(word);
                
                word = new Command();
                word.setWords("god save me ");
                word.setKeyword("eat");
                word.setType(Command.Type.TYPE_MSG);
                word.setTime(System.currentTimeMillis());
                lists.add(word);
                
                word = new Command();
                word.setWords("i can eat pig now ");
                word.setKeyword("eat");
                word.setType(Command.Type.TYPE_MSG);
                word.setTime(System.currentTimeMillis());
                lists.add(word);
                

                

                word = new Command();
                word.setWords("i m so happy");
                word.setKeyword("mood");
                word.setExtra("happy");
                word.setType(Command.Type.TYPE_MSG);
                word.setTime(System.currentTimeMillis());
                lists.add(word);
                
                word = new Command();
                word.setWords("i m so glad");
                word.setKeyword("mood");
                word.setExtra("happy");
                word.setType(Command.Type.TYPE_MSG);
                word.setTime(System.currentTimeMillis());
                lists.add(word);
                
                word = new Command();
                word.setWords("i m not good");
                word.setKeyword("mood");
                word.setExtra("sad");
                word.setType(Command.Type.TYPE_MSG);
                word.setTime(System.currentTimeMillis());
                lists.add(word);
                
                word = new Command();
                word.setWords("i feel so bad");
                word.setKeyword("mood");
                word.setExtra("sad");
                word.setType(Command.Type.TYPE_MSG);
                word.setTime(System.currentTimeMillis());
                lists.add(word);
                
                word = new Command();
                word.setWords("i m dead man");
                word.setKeyword("mood");
                word.setExtra("sad");
                word.setType(Command.Type.TYPE_MSG);
                word.setTime(System.currentTimeMillis());
                lists.add(word);
                
                word = new Command();
                word.setWords("i m tired");
                word.setKeyword("sleep");
                word.setType(Command.Type.TYPE_MSG);
                word.setTime(System.currentTimeMillis());
                lists.add(word);
                
                word = new Command();
                word.setWords("i m gona to bed");
                word.setKeyword("sleep");
                word.setType(Command.Type.TYPE_MSG);
                word.setTime(System.currentTimeMillis());
                lists.add(word);
                
                word = new Command();
                word.setWords("must spleep now");
                word.setKeyword("sleep");
                word.setType(Command.Type.TYPE_MSG);
                word.setTime(System.currentTimeMillis());
                lists.add(word);
                
                word = new Command();
                word.setWords("my dear bed im coming");
                word.setKeyword("sleep");
                word.setType(Command.Type.TYPE_MSG);
                word.setTime(System.currentTimeMillis());
                lists.add(word);
                
                word = new Command();
                word.setWords("i m dieing on my bed ");
                word.setKeyword("sleep");
                word.setType(Command.Type.TYPE_MSG);
                word.setTime(System.currentTimeMillis());
                lists.add(word);
        }else if(name.equals("mood")){

            word = new Command();
            word.setWords("i m so happy");
            word.setKeyword("mood");
            word.setExtra("happy");
            word.setType(Command.Type.TYPE_MSG);
            word.setTime(System.currentTimeMillis());
            lists.add(word);
            
            word = new Command();
            word.setWords("i m so glad");
            word.setKeyword("mood");
            word.setExtra("happy");
            word.setType(Command.Type.TYPE_MSG);
            word.setTime(System.currentTimeMillis());
            lists.add(word);
            
            word = new Command();
            word.setWords("i m not good");
            word.setKeyword("mood");
            word.setExtra("sad");
            word.setType(Command.Type.TYPE_MSG);
            word.setTime(System.currentTimeMillis());
            lists.add(word);
            
            word = new Command();
            word.setWords("i feel so bad");
            word.setKeyword("mood");
            word.setExtra("sad");
            word.setType(Command.Type.TYPE_MSG);
            word.setTime(System.currentTimeMillis());
            lists.add(word);
            
            word = new Command();
            word.setWords("i m dead man");
            word.setKeyword("mood");
            word.setExtra("sad");
            word.setType(Command.Type.TYPE_MSG);
            word.setTime(System.currentTimeMillis());
            lists.add(word);
        }else if(name.equals("sleep")){
            word = new Command();
            word.setWords("i m tired");
            word.setKeyword("sleep");
            word.setType(Command.Type.TYPE_MSG);
            word.setTime(System.currentTimeMillis());
            lists.add(word);
            
            word = new Command();
            word.setWords("i m gona to bed");
            word.setKeyword("sleep");
            word.setType(Command.Type.TYPE_MSG);
            word.setTime(System.currentTimeMillis());
            lists.add(word);
            
            word = new Command();
            word.setWords("must spleep now");
            word.setKeyword("sleep");
            word.setType(Command.Type.TYPE_MSG);
            word.setTime(System.currentTimeMillis());
            lists.add(word);
            
            word = new Command();
            word.setWords("my dear bed im coming");
            word.setKeyword("sleep");
            word.setType(Command.Type.TYPE_MSG);
            word.setTime(System.currentTimeMillis());
            lists.add(word);
            
            word = new Command();
            word.setWords("i m dieing on my bed ");
            word.setKeyword("sleep");
            word.setType(Command.Type.TYPE_MSG);
            word.setTime(System.currentTimeMillis());
            lists.add(word);
        }else{
            for(int i=0;i<num;i++){
                word = new Command();
                word.setWords("asd - "+i);
                word.setKeyword("test - "+i);
                word.setLevel(123+i);
                word.setType(Command.Type.TYPE_MSG);
                word.setTime(System.currentTimeMillis());
                lists.add(word);
            }
        }
        return lists;
    }
}
