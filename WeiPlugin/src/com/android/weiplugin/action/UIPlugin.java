package com.android.weiplugin.action;

import java.util.Properties;

import com.android.weiplugin.data.Command;


import android.content.Context;
import android.widget.LinearLayout;

/**
 * UI plugin 
 * @author jixieshi@me.com 20121111
 * */
public interface UIPlugin extends Action{

	/** 
	 * in layout ,and out layout
	 * */
	Command pluginUI(LinearLayout layout,Context context,Command cmd);

	
	void clear();
}
