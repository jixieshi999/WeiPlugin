package com.android.weiplugin.tools;

import java.util.ArrayList;


import android.database.Cursor;

import com.android.weiplugin.Global;
import com.android.weiplugin.data.Command;
import com.android.weiplugin.data.PluginCommand;
import com.android.weiplugin.data.PluginEntry;

public class PluginTools {

	
	public static void updatePluginToDatabase(String className,int  typeOrshow,int level,Command cmd){ 

		boolean flag = Global.mDatabaseHelper.isPluginExist(className);
		if(flag){
			Global.mDatabaseHelper.updatePlugin(className, typeOrshow, level);
		}else{
			if(cmd==null){
				return;
			}
            Object []objs = new Object[]{className,typeOrshow ,level,cmd.keyword};
			Global.mDatabaseHelper.addPlugin(objs);
//			Globle.mDatabaseHelper.addPlugin(entry);
		}
    }
	public static void addPluginToDatabase(String className,int  typeOrshow,int level,Command cmd,String name){ 
		
		boolean flag = Global.mDatabaseHelper.isPluginExist(className);
		if(flag){
//			Globle.mDatabaseHelper.updatePlugin(className, typeOrshow, level);
		}else{
			if(cmd==null){
				return;
			}
			Object []objs = new Object[]{className,typeOrshow ,level,cmd.keyword,name};
			Global.mDatabaseHelper.addPlugin(objs);
//			Globle.mDatabaseHelper.addPlugin(entry);
		}
	}
	/**
	 * 将entry实体的Commandlist插入数据库
	 *  
	 * */
	public static int addPluginCommandToDatabase(PluginEntry entry){ 
		String clsName = entry.clsName;
		if(null==entry.pluginCommandList){
			return 0;
		}
		int level = entry.level;
//		testPrintTable("PluginsCommand");
		int i=0;
		for(String cmd:entry.pluginCommandList){
			boolean flag = Global.mDatabaseHelper.isPluginCommandExist(clsName, cmd) ;
			if(flag){
//			Globle.mDatabaseHelper.updatePlugin(className, typeOrshow, level);
			}else{
				String discription = "";
				if(null!=entry.pluginCommandDiscriptionList){
					discription=entry.pluginCommandDiscriptionList.get(i);
				}
				String name = cmd;
				if(null!=entry.pluginCommandNameList){
					name=entry.pluginCommandNameList.get(i);
				}
				int show = 0;
				if(null!=entry.pluginCommandShowList){
					show=entry.pluginCommandShowList.get(i);
				}
				Object []objs = new Object[]{clsName ,entry.type,level++,cmd,name,discription,show};
				try{
					Global.mDatabaseHelper.addPluginsCommand(objs);
				}catch (Exception e) {
					DebugTools.log(e);
				}
//			Globle.mDatabaseHelper.addPlugin(entry);
			}
			i++;
		}
		return level;
	}
	private static void testPrintTable(String tablename) {
		Cursor cursor = Global.mDatabaseHelper.ReadTable(tablename);
		try{
			while(cursor.moveToNext()){
				String []names = cursor.getColumnNames();
				if(names!=null){
					for(String name:names){
						try{
							
							String values = cursor.getString(cursor.getColumnIndex(name));
							DebugTools.log("col name : "+name+" values: "+values);
						}catch (Exception e) {
							DebugTools.log(e);
						}
					}
				}
			}
		}catch (Exception e) {
			DebugTools.log(e);
		}finally{
			cursor.close();
		}
	}
	
	public static void updatePluginCommandToDatabase(String clsName,String cmd,String show,String level){ 
		boolean flag =true;
//		boolean flag = Globle.mDatabaseHelper.isPluginExist(entry);
		if(show==null){
			show = "1";
		}
		if(level==null){
			level = "10";
		}
		if(flag){
			Global.mDatabaseHelper.updatePluginCommand(clsName,cmd,show,level);
		}else{
//			Globle.mDatabaseHelper.addPlugin(entry);
		}
	}
	public static void addPluginToDatabase(PluginEntry entry){ 
		boolean flag = Global.mDatabaseHelper.isPluginExist(entry);
		if(flag){
//			Globle.mDatabaseHelper.updatePlugin(entry);
		}else{
			if(entry.cmd==null){
				return;
			}
			Global.mDatabaseHelper.addPlugin(entry);
		}
	}
//	public static void addPluginToDatabase(PluginEntry entry){ 
//		
//		Globle.mDatabaseHelper.addPlugin(entry);
//	}
    
    public static void readPlugin(String className){ 
    	
//    	Globle.mDatabaseHelper.addPlugin(className, type, level);
    }
    public static ArrayList<PluginCommand> readShowPluginFromDatabase(){ 
    	return readPluginFromDatabase(2);
    }
    
    public static ArrayList<PluginCommand> readAllPluginFromDatabase(){ 
    	return readPluginFromDatabase(1);
    }
    /**
     * @param type 1读取所有插件，2 读取显示的插件
     * */
    public static ArrayList<PluginCommand> readPluginFromDatabase(int type){
    	Cursor cursor =null;
    	switch (type) {
		case 1:
			cursor = Global.mDatabaseHelper.readPlugin();
			break;
		case 2:
			cursor = Global.mDatabaseHelper.readShowPlugin();
			break;
		default:
			return null;
		}
    	ArrayList<PluginCommand> list = new ArrayList<PluginCommand>();
    	try{
    		PluginEntry enry = null;
    		while(cursor.moveToNext()){
    			enry = new PluginEntry();
    			enry.clsName = cursor.getString(0);
    			enry.type= cursor.getInt(1);
    			enry.level = cursor.getInt(2);
    			enry.name = cursor.getString(3);
    			enry.destription = cursor.getString(4);
    			enry.cmd = new Command();
    			enry.cmd.keyword = cursor.getString(5);
    			enry.show = cursor.getInt(6);
    			if(enry.name==null||enry.name.equals("")){
    				enry.name = enry.cmd.keyword;
    			}
    			list.add(enry);
    			DebugTools.log("read plugin ,clsName: "+enry);
    		}
    	}catch (Exception e) {
    		DebugTools.log(e);
			// TODO: handle exception
		}finally{
			cursor.close();
		}
    	return list;
    }
    
}
