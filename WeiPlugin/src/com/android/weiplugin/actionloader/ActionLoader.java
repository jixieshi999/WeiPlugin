package com.android.weiplugin.actionloader;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;

import com.android.weiplugin.Global;
import com.android.weiplugin.action.Action;
import com.android.weiplugin.action.UIPlugin;
import com.android.weiplugin.data.Command;
import com.android.weiplugin.data.PluginEntry;
import com.android.weiplugin.log.Debug;
import com.android.weiplugin.log.LogTools;
import com.android.weiplugin.plugins.PluginManager;
import com.android.weiplugin.tools.DebugTools;
import com.android.weiplugin.tools.FilePathTools;
import com.android.weiplugin.tools.PluginTools;

import dalvik.system.DexClassLoader;

/**
 * tools to load apk,load plugins
 * <p>加载程序外部的 插件，以.apk为后缀
 * @author jixieshi@me.com 20121207 
 * forgive my poor English
 * */
public class ActionLoader {

	public static final String TAG = "";
	
	static Map<PluginEntry,Class> mPluginClass =null;
	static Map<String,Object> mPluginInstance =null;
	/**
	 * 此处需要注意，每一个class 对应一个loader加载的Command class，如果不同的loader加载的class使用同一个command类的话会报
	 * ,02-21 17:04:05.206: E/DebugTools(32541): java.lang.IllegalArgumentException: 
        argument 3 should have type com.android.weiplugin.data.Command, got com.android.weiplugin.data.Command
	 * */
	static Map<String,Class> mclassCommandList =null;
	static Class mContextClass;
	static Class mLayoutClass;
	static Class classCommand;
	static public String CommandName = "com.android.weiplugin.data.Command";
	static public String LayoutName = "android.widget.LinearLayout";
	static public String ContextName = "android.content.Context";
	
	/*
	 * ��ѯlibĿ¼�µ������ļ����
	 */
	private static String[] listFileNames() {
		File file_directory = new File(FilePathTools.sPlugin);
		mBasePluginPath = file_directory.getAbsolutePath();
		LogTools.log("url : "+mBasePluginPath);
		return file_directory.list();
	}

	static Class getCommandClass(String clsName){
        if(null==mclassCommandList){
            mclassCommandList = new HashMap<String, Class>();
        }
		if(null==classCommand){
//			classCommand = getLoadClass(loader, CommandName);
		}
		return mclassCommandList.get(clsName);
	}
	static Object getCommandClassInstance(String clsName){
	    Class cmdcls = getCommandClass(clsName);
	    Object obj=null;
	    if(null!=cmdcls){
	        try {
                obj = cmdcls.newInstance();
            } catch (Exception e) {
                Debug.dLog(e);
            }
	    }else{
	        obj = new Command();
	    }
	    return  obj;
	}
	static String mBasePluginPath = "";


	static ArrayList<String> mClassPath;
//	static ArrayList<String> xmlName = new ArrayList<String>();

	/**
	 * handle command to plugin action
	 * <p>执行非UI类型的接口方法
	 * */
	public static void handlePluginAction(Action action,Command param) {

		if(mPluginClass==null){
			reloadClassPath(Global.sContext);
		}
        runAction(action, param);
        
	}

	public static ArrayList<String> getClassPath(){
		if(null==mClassPath){
			mClassPath = new ArrayList<String>();
		}
		if(mClassPath.size()<=0 ){
			loadDexPath();
		}
		return mClassPath;
	}
	
	/**
	 * reload the plugin path 
	 * clear cache
	 * */
	public static ArrayList<String> reloadClassPath(Context context){
		if(null==mClassPath){
			mClassPath = new ArrayList<String>();
		}
//		mClassPath.clear();
		clearChche();
		loadDexPath();
		loadPluginClass(context);
		return mClassPath;
	}
	
	static void clearChche(){
		if(null!=mClassPath){
			mClassPath.clear();
		}
		if(null!=mPluginClass){
			mPluginClass.clear();
		}
		if(null!=mPluginInstance){
			mPluginInstance.clear();
		}

	}
	/**
	 * 处理插件apk中的UI插件,执行插件中实现的UI接口
	 * */
	public static boolean runUIAction(Action action,Context context,LinearLayout layout,Command cmd) {
		boolean flag = false;
		PluginEntry entry ;
		String path;
		if(null==mPluginClass){
			reloadClassPath(Global.sContext);
		}
		if(null==mPluginClass){
			DebugTools.log("no UI plugins ");
			return false;
		}
		Command recieve = new Command();
		Set<PluginEntry>  setEntry = mPluginClass.keySet();
		Iterator<PluginEntry>  it = setEntry.iterator();
		try{
			while(it.hasNext()){
				entry = it.next();
				if(entry.type==PluginEntry.Type.UI){
					
					Class classActionImpl = mPluginClass.get(entry);
					Object instanceActionImpl = getLoadClassInstance(classActionImpl,entry.clsName);
					handleUIPlugin( context, layout, classActionImpl, instanceActionImpl,cmd,recieve,entry);

					if(null!=action.action(recieve)){
		                return true;//already handle this command
		            }
			    	if(cmd.keyword.equals(PluginManager.INIT_ALL_ACTION)){
			        	//save action entry to database
			        		entry.level = PluginManager.sequence++;
			        		PluginTools.addPluginToDatabase(entry);
			        }
				}
			}
		}catch (Exception e) {
			DebugTools.log(e);
			// TODO: handle exception
		}
		return flag;
	}
	
	
	/**
	 * run plugin action with cache data
	 * */
	 static void runAction(Action action, Command data) {
		PluginEntry entry ;
		if(null==mPluginClass){
			return;
		}
		Command recieve = new Command();
		Set<PluginEntry>  setEntry = mPluginClass.keySet();
		Iterator<PluginEntry>  it = setEntry.iterator();
		try{
			while(it.hasNext()){
				entry = it.next();
				if(entry.type!=PluginEntry.Type.UI){
					
					Class classActionImpl = mPluginClass.get(entry);
					Object instanceActionImpl = getLoadClassInstance(classActionImpl,entry.clsName);
					handleCommandAction(data, recieve, classActionImpl, 
							instanceActionImpl,entry);
					if(null!=action.action(recieve)){
		                return;//already handle this command
		            }
				}
			}
		}catch (Exception e) {
			DebugTools.log(e);
			// TODO: handle exception
		}
//		return recieve;
	}
	
	/**
	 * handle the plugin action
	 * <p>通过反射执行，接口中的 action方法
	 * */
	private static void handleCommandAction(Command data, Command recieve,
			Class classActionImpl,  Object instanceActionImpl,PluginEntry entry )
			throws IllegalAccessException, InstantiationException,
			NoSuchMethodException, InvocationTargetException {
		//load action param
//		Object instanceCommand = getLoadClassInstance(classCommand,CommandName);
        Object instanceCommand = getCommandClassInstance(entry.clsName);
		
		copyCommand2Object(data,  instanceCommand,getCommandClass(entry.clsName));

		// getName
		Method methodGetName = classActionImpl
				.getDeclaredMethod("getName");
		methodGetName.setAccessible(true);
		Object returnValue = methodGetName.invoke(instanceActionImpl,
				(Object[]) null);

		LogTools.log("rturn: " + (String) returnValue);

		if(null==entry.cmd){
			methodGetName = classActionImpl
					.getDeclaredMethod("getMainCommand");
			methodGetName.setAccessible(true);
			returnValue = methodGetName.invoke(instanceActionImpl,
					(Object[]) null);
			if(null==returnValue){
			    
			}else{
			    Command mainCommand = new Command();
			    copyObject2Command(mainCommand, returnValue,getCommandClass(entry.clsName));
			    entry.cmd = mainCommand;
			}
		}
		
		// invoke action
		Method methodAction = instanceActionImpl.getClass()
				.getDeclaredMethod("action",
						new Class[] { classCommand });
		methodAction.setAccessible(true);
		returnValue = methodAction.invoke(instanceActionImpl,
				new Object[] { instanceCommand });

		//recieve Command that Method action() returned
		copyObject2Command(recieve,  returnValue,getCommandClass(entry.clsName));
//				Field fielKeyword = classCommand.getDeclaredField("extra");
//				String values = (String) fielKeyword.get(returnValue);
		LogTools.log("reflect action " + recieve);
	}
	
	/**
	 * handle the ui plugin for test 
	 * */
	private static void handleUIPlugin(Context  context, LinearLayout layout,
			Class classActionImpl,  Object instanceActionImpl,Command data,Command result,PluginEntry entry)
					throws IllegalAccessException, InstantiationException,
					NoSuchMethodException, InvocationTargetException, ClassNotFoundException {

//		Object instanceCommand = getLoadClassInstance(classCommand,CommandName);
        Object instanceCommand = getCommandClassInstance(entry.clsName);
//				Object instanceCommand = classCommand.newInstance();
		
		copyCommand2Object(data,  instanceCommand,getCommandClass(entry.clsName));
		
		// getName
		Method methodGetName = classActionImpl
				.getDeclaredMethod("getPluginProperties");
		methodGetName.setAccessible(true);
		Object returnValue = methodGetName.invoke(instanceActionImpl,
				(Object[]) null);

//		LogTools.log("activity get plugin class name : "+(String)returnValue);
		Properties pluginProperties = (Properties)returnValue;
		DebugTools.log("get UI plugin properties : "+pluginProperties);
		
		if(null==entry.cmd){
			methodGetName = classActionImpl
					.getDeclaredMethod("getMainCommand");
			methodGetName.setAccessible(true);
			returnValue = methodGetName.invoke(instanceActionImpl,
					(Object[]) null);
			
			Command mainCommand = new Command();
			copyObject2Command(mainCommand, returnValue,getCommandClass(entry.clsName));
			entry.cmd = mainCommand;
		}
		// invoke action
		Method methodAction = instanceActionImpl.getClass()
				.getDeclaredMethod("pluginUI",
						new Class[] { mLayoutClass,mContextClass,classCommand });
		methodAction.setAccessible(true);
		returnValue = methodAction.invoke(instanceActionImpl,
				new Object[] { layout,context,instanceCommand });

		copyObject2Command(result,  returnValue,getCommandClass(entry.clsName));
		//recieve Command that Method action() returned
//		copyParam2Command(recieve, classCommand, returnValue);
//				Field fielKeyword = classCommand.getDeclaredField("extra");
//				String values = (String) fielKeyword.get(returnValue);
		LogTools.log("reflect action " +" UI : "+returnValue);
	}


    /**
     * 将Command类中的数据copy到 不同ClassLoader加载的Command Object中
     * <p>是通过反射来实现
     * **/
	private static void copyCommand2Object(Command data,
			 Object instanceCommand,Class CommandClass)
					{
		try{
			copyCommandParam(data,instanceCommand,1,CommandClass);
		}catch (Exception e) {
			LogTools.log("reflect",e);
		}
		
	}
    /**
     * 将不同ClassLoader加载的Command Object中的数据copy到 Command类 中
     * <p>是通过反射来实现
     * **/
	private static void copyObject2Command(Command data,
			 Object instanceCommand,Class CommandClass)
	{
		try{
			copyCommandParam(data,instanceCommand,2,CommandClass);
		}catch (Exception e) {
			LogTools.log("reflect",e);
		}
		
	}
	/**
	 * copy command data to instanceCommand use reflect 
	 * <p>此处是否可以考虑使用序列化（command实现序列化），不用反射，待验证，优化
	 * @param data ori data
	 * @param classCommand load Command class
	 * @param instanceCommand recive data,equals of return value
	 * @param type 1 means copy data to instanceCommand
	 * <p> 2 means copy instanceCommand to data 
	 * */
	private static void copyCommandParam(Command data,
			 Object instanceCommand,int type,Class CommandClass)
			throws NoSuchFieldException, IllegalAccessException {
		Field []FielCommand= CommandClass.getDeclaredFields();
		Field []fields = Command.class.getDeclaredFields();
//		testReflectField(Command.class, data);
//		instanceCommand = classActionImpl.cast(data);
		for(Field field:fields){
			String fieldName = field.getName();
			if(Modifier.isFinal(field.getModifiers())){
				LogTools.log("reflect","copy param final field,Name: "+fieldName);
				continue;
			}
			Object obj = field.get(data);
			LogTools.log("reflect","copy param fieldName: "+fieldName);
			LogTools.log("reflect","copy param fieldName value: "+obj);
			for(Field fieldCommand:FielCommand){
				String fieldCommandName = fieldCommand.getName();
				if(fieldName.equals(fieldCommandName)){
						LogTools.log("reflect","copy param fieldCommandName: "+fieldCommandName);
						if(1==type){
//						Object str = fieldCommand.get(instanceCommand);
//						LogTools.log("reflect","copy param befor fieldCommandName value: "+str);
							fieldCommand.set(instanceCommand, obj);
							LogTools.log("reflect","copy param after fieldCommandName value: "+obj);
							
						}else{
							Object str = fieldCommand.get(instanceCommand);
							LogTools.log("reflect","copy param befor fieldCommandName value: "+obj);
							field.set(data, str);
							LogTools.log("reflect","copy param after fieldCommandName value: "+str);
						}
					break;
				}
			}
		}
//		testReflectField(classCommand, instanceCommand);
		return ;
	}
	/**
	 * get load class  from cache map
	 * */
	private static Class getLoadClass(DexClassLoader loader,
			PluginEntry entry) throws ClassNotFoundException {
		if(null==mPluginClass){
			mPluginClass =new HashMap<PluginEntry,Class>();
		}
		String pluginClassName = entry.clsName;
		Class classActionImpl = mPluginClass.get(pluginClassName);
		if(classActionImpl ==null){
			classActionImpl =  loader.loadClass(pluginClassName);
			mPluginClass.put(entry, classActionImpl);
//			PluginTools.addPluginToDatabase(pluginClassName, PluginEntry.Type.Normal, 2);
		}
//		PluginTools.readPluginFromDatabase();
		return classActionImpl;
	}
	private static Class getLoadClass(DexClassLoader loader,
			String clsName) throws ClassNotFoundException {
		Class classImpl =  loader.loadClass(clsName);
		return classImpl;
	}
	/**
	 * get load class instance from cache map
	 * */
	private static Object getLoadClassInstance(Class pluginClass,String pluginClassName) throws IllegalAccessException, InstantiationException  {
		if(null==mPluginInstance){
			mPluginInstance =new HashMap<String,Object>();
		}
		Object instanceActionImpl = mPluginInstance.get(pluginClassName);
		if(instanceActionImpl ==null){
			instanceActionImpl =  pluginClass.newInstance();
			mPluginInstance.put(pluginClassName, instanceActionImpl);
		}
		return instanceActionImpl;
	}

	/**
	 * get plugin class and type from apk activity,
	 * that activity must have implements {@link weibot.actionPluginAction}
	 * <p>通过加载android apk包，获取第一个activity名称，然后加载该activity，得到配置信息（插件的类名等）
	 * <p>功能就是读取配置信息，返回一个PluginEntry实体，待优化
	 * return plugin class entry with arraylist data
	 * */
	private static ArrayList<PluginEntry> getPluginClassName(Context context, String path,
			DexClassLoader loader) throws ClassNotFoundException,
			IllegalAccessException, InstantiationException,
			NoSuchMethodException, InvocationTargetException {
		ArrayList<PluginEntry> list = new ArrayList<PluginEntry>();
		
		String pluginClassName = null;
		PackageInfo pkgInfo = context.getPackageManager()
				.getPackageArchiveInfo(path, 1);

		if ((pkgInfo.activities != null)
				&& (pkgInfo.activities.length > 0)) {
			String activityname = pkgInfo.activities[0].name;
			Log.d(TAG, "activityname = " + activityname);

			Class activityMain = loader.loadClass(activityname);
			Object instanceMainActivity = activityMain.newInstance();

			//getPluginClassName
			Method methodGetName = activityMain.getDeclaredMethod(
					"getPluginClassName");
			methodGetName.setAccessible(true);
			Object returnValue = methodGetName.invoke(instanceMainActivity, (Object[])null);
			LogTools.log("activity get plugin class name : "+(String)returnValue);
			pluginClassName = (String)returnValue;
//			String []pluginClassList = pluginClassName.split("|"); 
			
			
			Method methodGetType = activityMain.getDeclaredMethod(
					"getPluginType");
			methodGetType.setAccessible(true);
			Object returnValues = methodGetType.invoke(instanceMainActivity, (Object[])null);
			LogTools.log("activity get plugin class type : "+(String)returnValues);
			
			methodGetName = activityMain.getDeclaredMethod(
					"getPluginProperties");
			methodGetName.setAccessible(true);
			returnValue = methodGetName.invoke(instanceMainActivity, (Object[])null);
//			LogTools.log("activity get plugin class name : "+(String)returnValue);
			Properties pluginProperties = (Properties)returnValue;
			DebugTools.log(pluginProperties);

//			entry.clsName = pluginClassName;
//			entry.type  = (Integer)returnValues;
			String [] pluginClassList = pluginClassName.split("\\|");
	    	String [] tyList = ((String)returnValues).split("\\|");
	    	int size = pluginClassList.length;
	    	PluginEntry entry = null;
	    	for(int i=0;i<size;i++){
	    		entry = new PluginEntry();
	    		entry.clsName = pluginClassList[i];
	    		entry.type = Integer.valueOf(tyList[i]);
	    		list.add(entry);
	    	}
	    	for(String s:pluginClassList){
	    		System.out.println(s);
	    		Debug.dLog(s);
	    	}
	    	for(String s:tyList){
	    		System.out.println(s);
	    		Debug.dLog(s);
	    	}
		}
		return list;
	}
	/**
	 * 将插件信息插入到数据库中
	 * **/
	private static void insertActionInfo(Properties pros,int type,int sequence,String clsName) {
            PluginEntry entry = new PluginEntry();
            entry.clsName =clsName;
            entry.type =type ;
            entry.level = sequence;
            if(pros!=null){
                String name = (String)pros.get("name");
                entry.name = name==null?"unknown":name;
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
//                  PluginTools.addPluginToDatabase(action.getClass().getName(), PluginEntry.Type.Kernel, sequence++, cmd,action.getName());
    }
	
	private static void testReflect(Class classActionImpl) {
		Method []methods = classActionImpl.getDeclaredMethods();
		for(Method method :methods){
			LogTools.log("reflect","method : "+method.getName());
			Class [] params = method.getParameterTypes();
			for(Class param:params){
				LogTools.log("reflect","param method : "+param.getName());
				
			}
			
			Class returnType = method.getReturnType();
			LogTools.log("reflect","returnType : "+returnType.getName());
			
			if(method.getName().equals("action")){

				method.setAccessible(true);
//							Object returnValue = method.invoke(instanceActionImpl, new Object[] { instanceCommand });
			}
		}
		
	}
	
	/**
	 * just for test the reflect field
	 * */
	private static void testReflectField(Class classActionImpl,Object instance) {

		LogTools.log("reflect","copy param start-----------------------------------------");
		Field[]methods = classActionImpl.getDeclaredFields();
		for(Field method :methods){
			try {
				LogTools.log("reflect","method : "+method.getName());
				Class  param = method.getType();
				LogTools.log("reflect","getType : "+param.getName());
				
				
				Object returnType;
				returnType = method.get(instance);
				LogTools.log("reflect","value : "+returnType);
			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				LogTools.log(e);
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				LogTools.log(e);
			}
			
			
		}

		LogTools.log("reflect","copy param end-----------------------------------------");
	}
	
	

	public static void loadDexPath() {
		String fileNames[] = listFileNames();
		if(null==fileNames){
		    return;
		}
		for (String filename : fileNames) {
			LogTools.log("get plugin list name : " + filename);

			if (filename.endsWith(".apk")) {
			    String filePath = mBasePluginPath + File.separator + filename;
			    if(!mClassPath.contains(filePath)){
			        mClassPath.add(filePath);
			    }
//				mClassPath.add(mBasePluginPath + File.separator + filename);
			}/* else if (filename.endsWith(".xml")) {
				xmlName.add(filename);
			}*/
		}
		return ;
	}

	/**
	 * 加载apk文件
	 * */
	public static void loadPluginClass(Context context) {

		ArrayList<String> classPath = getClassPath();
		for (String path : classPath) {
			DexClassLoader loader = new DexClassLoader(path,
					FilePathTools.soutPlugin, null,ClassLoader.getSystemClassLoader().getParent());
//			DexClassLoader localDexClassLoader = new DexClassLoader(path,
//					FilePathTools.sPlugin, null, ClassLoader.getSystemClassLoader().getParent());
			
			try {
				ArrayList<PluginEntry> entryList = getPluginClassName(context, path, loader);
				int sequence = 5;
				for(PluginEntry entry :entryList){
					
					String pluginClassName = entry.clsName;
					if(pluginClassName==null||pluginClassName.equals("")){
						DebugTools.log("load plugin class is null !!!");
						break;
//						throw new NullPointerException("plugin class is null !!!");
					}
					if(entry.type==PluginEntry.Type.UI){

					    /**
					     * 此处需要注意，每一个class 对应一个loader加载的Command class，如果不同的loader加载的class使用同一个command类的话会报
					     * ,02-21 17:04:05.206: E/DebugTools(32541): java.lang.IllegalArgumentException: 
					        argument 3 should have type com.android.weiplugin.data.Command, got com.android.weiplugin.data.Command
					        *
					        *同理mContextClass和mLayoutClass也需要一一对应？？？
					     * */
						if(mContextClass==null){
							mContextClass = getLoadClass(loader, ContextName);
						}
						if(mLayoutClass==null){
							mLayoutClass = getLoadClass(loader, LayoutName);
						}
						classCommand = getLoadClass(loader, CommandName);
					}else{
						if(classCommand==null){
						}
						classCommand = getLoadClass(loader, CommandName);
						
					}
					if(null==mclassCommandList){
					    mclassCommandList = new HashMap<String, Class>();
					}
					mclassCommandList.put(pluginClassName, classCommand);
					Class classActionImpl = getLoadClass(loader, entry);
//				Class classActionImpl = loader.loadClass(pluginClassName);
//				Class classCommand = getLoadClass(loader, CommandName);
//				Class classCommand = loader.loadClass("weibot.data.Command");
					Object instanceActionImpl = getLoadClassInstance(classActionImpl,pluginClassName);
//				Object instanceActionImpl = classActionImpl.newInstance();

		            Method methodGetName = classActionImpl.getDeclaredMethod(
		                    "getPluginProperties");
		            methodGetName.setAccessible(true);
		            Object returnValue = methodGetName.invoke(instanceActionImpl, (Object[])null);
//		          LogTools.log("activity get plugin class name : "+(String)returnValue);
		            Properties pluginProperties = (Properties)returnValue;
		            insertActionInfo(pluginProperties,entry.type,sequence++,entry.clsName);
				}
			}catch (Exception e) {
				DebugTools.log(e);
			}
			
		}}

//	private static URL lib_url = ClassLoader.getSystemClassLoader()
//			.getResource("plugin");
//	private static URLClassLoader loader = null;
//	private static URLClassLoader loader = null;
}



