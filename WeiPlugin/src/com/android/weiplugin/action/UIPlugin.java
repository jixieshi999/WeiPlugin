package com.android.weiplugin.action;

import java.util.Properties;

import com.android.weiplugin.data.Command;


import android.content.Context;
import android.widget.LinearLayout;

/**
 * UI plugin 
 * <p>UI公共接口
 * @author jixieshi@me.com 20121111
 * */
public interface UIPlugin extends Action{

	/** 
	 * in layout ,and out layout
	 * 通过主程序传入view，和命令来实现插件显示内容
	 * */
	Command pluginUI(LinearLayout layout,Context context,Command cmd);

	
	void clear();
}
