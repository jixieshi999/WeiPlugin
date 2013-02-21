package com.android.weiplugin.actionloader;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Properties;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.os.Bundle;
import android.util.Log;

import com.android.weiplugin.action.Action;
import com.android.weiplugin.data.Command;
import com.android.weiplugin.log.LogTools;
import com.android.weiplugin.tools.DebugTools;
import com.android.weiplugin.tools.FilePathTools;

import dalvik.system.DexClassLoader;

public class ClassLoaderTest {

	public static final String TAG = "";
	
	/*
	 * ��ѯlibĿ¼�µ������ļ����
	 */
	private static String[] listFileNames() {
		File file_directory = new File(FilePathTools.sPlugin);
		url_pa = file_directory.getAbsolutePath();
		LogTools.log("url : "+url_pa);
		return file_directory.list();
	}

	static String url_pa = "";


	static ArrayList<String> className = new ArrayList<String>();
	static ArrayList<String> xmlName = new ArrayList<String>();

	public static void testDexLoader(Context context) {
		// DexClassLoader loader = new DexClassLoader(url_pa, url_pa, url_pa,
		// loader);

		// WClassLoader loader = new WClassLoader(urls);
		loadDexPath();
		for (String path : className) {
			// DexClassLoader loader = new
			// DexClassLoader(path,"data/data/",null,Globle.sContext.getClassLoader());
			DexClassLoader loader = new DexClassLoader(path,
					FilePathTools.sPlugin, null,ClassLoader.getSystemClassLoader());
			// DexClassLoader loader = new
			// DexClassLoader(path,"data/data/weibot.plugin",null,Globle.sContext.getClassLoader());
			try {
				
				
//				DebugTools.log(context);
				String pluginClassName = "";
				getPluginClassName(context, path, loader);
				LogTools.log("action load");
//				Class<?> classActionImpl = loader.loadClass("weibot.plugin.MainActivity");
				Class classActionImpl = loader.loadClass("weibot.plugin.ActionImpl");
				Class classCommand = loader.loadClass("weibot.data.Command");
				Object instanceActionImpl = classActionImpl.newInstance();
				Object instanceCommand = classCommand.newInstance();
				
				Field FielKeyword = classCommand.getDeclaredField("keyword");
				String keyword = (String)FielKeyword.get(instanceCommand);
				keyword = "jixieshi";
//				Class.forName("weibot.data.Command");
				Command data = new Command();
				data.keyword = "liudehua";
				data.level = 11;
				
				/*if(instanceActionImpl instanceof Action){
					Action action = (Action) instanceActionImpl;
					// String s = action.action("jerry");
					Command result = action.action(data);
					LogTools.log(result.keyword);
					LogTools.log(result.extra);
					LogTools.log("instance of action ");
					
				}else{*/
					testReflect(classActionImpl);
					
					//getName
					Method methodGetName = classActionImpl.getDeclaredMethod(
							"getName");
					methodGetName.setAccessible(true);
					Object returnValue = methodGetName.invoke(instanceActionImpl, (Object[])null);

					LogTools.log("rturn: "+(String)returnValue);
					
					//invoke action
					Method methodAction =  instanceActionImpl.getClass().getDeclaredMethod(
							"action", new Class[] { classCommand });
					methodAction.setAccessible(true);
					returnValue = methodAction.invoke(instanceActionImpl, new Object[] { instanceCommand });

					FielKeyword = classCommand.getDeclaredField("extra");
					keyword = (String)FielKeyword.get(returnValue);
					LogTools.log("reflect action "+keyword);
					
					
					LogTools.log("reflect action ");
					
			} catch (ClassNotFoundException e) {
				// TODO Auto-generated catch block
				LogTools.log(e);
				e.printStackTrace();
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				LogTools.log(e);
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				LogTools.log(e);
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (Exception e) {
				LogTools.log(e);
			}

		}

	}

	private static String getPluginClassName(Context context, String path,
			DexClassLoader loader) throws ClassNotFoundException,
			IllegalAccessException, InstantiationException,
			NoSuchMethodException, InvocationTargetException {
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
			
			
			methodGetName = activityMain.getDeclaredMethod(
					"getPluginProperties");
			methodGetName.setAccessible(true);
			returnValue = methodGetName.invoke(instanceMainActivity, (Object[])null);
//			LogTools.log("activity get plugin class name : "+(String)returnValue);
			Properties pluginProperties = (Properties)returnValue;
			DebugTools.log(pluginProperties);
			
		}
		return pluginClassName;
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
	
	public void LoadAPK(Bundle paramBundle, String dexpath, String dexoutputpath,Context context) {
		ClassLoader localClassLoader = ClassLoader.getSystemClassLoader();
		DexClassLoader localDexClassLoader = new DexClassLoader(dexpath,
				dexoutputpath, null, localClassLoader);
		try {
			PackageInfo plocalObject = context.getPackageManager()
					.getPackageArchiveInfo(dexpath, 1);

			if ((plocalObject.activities != null)
					&& (plocalObject.activities.length > 0)) {
				String activityname = plocalObject.activities[0].name;
				Log.d(TAG, "activityname = " + activityname);

				Class localClass = localDexClassLoader.loadClass(activityname);
				Constructor localConstructor = localClass
						.getConstructor(new Class[] {});
				Object instance = localConstructor.newInstance(new Object[] {});
				Log.d(TAG, "instance = " + instance);

				Method localMethodSetActivity = localClass.getDeclaredMethod(
						"setActivity", new Class[] { Activity.class });
				localMethodSetActivity.setAccessible(true);
				localMethodSetActivity.invoke(instance, new Object[] { this });

				Method methodonCreate = localClass.getDeclaredMethod(
						"onCreate", new Class[] { Bundle.class });
				methodonCreate.setAccessible(true);
				methodonCreate.invoke(instance, new Object[] { paramBundle });
			}
			return;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	public static URLClassLoader getURLClassLoader() {

		if (loader == null) {
			String fileNames[] = listFileNames();
			for (String filename : fileNames) {
				LogTools.log("get plugin list name : "+filename);
				
				if (filename.endsWith(".apk")) {
					className.add(filename);

				} else if (filename.endsWith(".xml")) {
					xmlName.add(filename);
				}
			}
			if (className.size() > 0) {
				URL urls[] = new URL[className.size()];
				int size = className.size();
				for (int i = 0; i < size; i++) {
					try {
						urls[i] = new URL("file:" + url_pa +File.separator
								+ className.get(i));
						LogTools.log(urls[i].toString());
					} catch (MalformedURLException e) {
						System.out.println(e);
						LogTools.log(e);
						throw new RuntimeException("����libĿ¼��jar�ļ���?", e);
					}
				}

				loader = new URLClassLoader(urls,Thread.currentThread().getContextClassLoader());
				
			} else {

				LogTools.log("get plugin list is null");
				LogTools.logToFile("actionloader", "get plugin list is null");
			}
		}
		return loader;
	}

	public static void loadDexPath() {
		String fileNames[] = listFileNames();
		for (String filename : fileNames) {
			LogTools.log("get plugin list name : " + filename);

			if (filename.endsWith(".apk")) {
				className.add(url_pa + File.separator + filename);
			}/* else if (filename.endsWith(".xml")) {
				xmlName.add(filename);
			}*/
		}
		return ;
	}

	private static URL lib_url = ClassLoader.getSystemClassLoader()
			.getResource("plugin");
	private static URLClassLoader loader = null;
//	private static URLClassLoader loader = null;
}
