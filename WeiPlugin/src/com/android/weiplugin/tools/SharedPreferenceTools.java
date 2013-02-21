package com.android.weiplugin.tools;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPreferenceTools {

	public static void saveString(Context context,String str,String value){
		SharedPreferences sharedPreferences = context.getSharedPreferences("weibo",Context.MODE_PRIVATE );
		Editor editor =sharedPreferences.edit();//获取编辑器
		editor.putString(str,value);
		editor.commit();//提交修改
	}
	
	public static String getString(Context context,String str){
		SharedPreferences sharedPreferences = context.getSharedPreferences("weibo",Context.MODE_PRIVATE );
		String value = sharedPreferences.getString(str, "");
		return value;
	}
	
	
}
