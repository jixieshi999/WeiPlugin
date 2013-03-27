package com.android.weiplugin.action;

import java.util.Properties;

import com.android.weiplugin.data.Command;

/**
 * plugin to handle command action 
 * 公共接口
 * @author jixieshi@me.com 20121111
 * */
public interface Action {

	/**
	 * 公共接口，通过传入参数，调用该接口，实现不同的功能
	 * @param ori  in ori 
	 * @return weibot.data.Command not ori
	 * */
	Command action(Command ori);
	

	/**
	 * return the command that this action ready to deal with main command
	 * <p>如果为null表示该action 不会保存到数据库中
	 * */
	Command getMainCommand();
	
	/**
	 * plugin description*/
	String getName();

	/**layout properties
	 * <p>实现该接口的一些属性
	 * */
	Properties getPluginProperties();
}
