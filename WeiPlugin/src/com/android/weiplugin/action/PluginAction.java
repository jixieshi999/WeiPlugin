package com.android.weiplugin.action;

import java.util.ArrayList;
import java.util.Properties;

import com.android.weiplugin.data.PluginEntry;


/**
 * implements to get plugin info 
 * @author jixieshi@me.com 20121111
 * */
public interface PluginAction {

	String getPluginName();
	String getPluginClassName();
	String getPluginVersion();
	String getPluginExtra();
	String getPluginType();
	Properties getPluginProperties();
}
