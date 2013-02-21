package com.android.weiplugin.action;

import java.util.Properties;

import com.android.weiplugin.data.Command;

/**
 * plugin to handle command action 
 * @author jixieshi@me.com 20121111
 * */
public interface Action {

	/**
	 * 
	 * @param ori  in ori 
	 * @return weibot.data.Command not ori
	 * */
	Command action(Command ori);
	

	/**
	 * return the command that this action ready to deal with main command
	 * 如果为null表示该action 不会保存到数据库中
	 * */
	Command getMainCommand();
	
	/**
	 * plugin description*/
	String getName();

	/**layout properties*/
	Properties getPluginProperties();
}
