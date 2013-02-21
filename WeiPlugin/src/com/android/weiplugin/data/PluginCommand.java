package com.android.weiplugin.data;




public class PluginCommand {

	public interface Type{
		int UI = 1;
		int Kernel = 2;
		int System = 3;
		int Normal = 4;
		int Weak = 5;
		int Setting = 6;
		int TestweiboLogin = 201;
		int TestSend = 202;
	}
	public String clsName = null;
	public int level ,type;
	public String destription;
	public String name;
	/**
	 * 
	 * 0  means not show,1 mean show on main screen scroll bar */
	public int show =0;

	@Override
	public String toString() {
		return "PluginEntry [clsName=" + clsName + ", level=" + level
				+ ", type=" + type + ", destription=" + destription + ", name="
				+ name + ", show=" + show+" ,Command = "+cmd==null?"":cmd.toString() + "]";
	}
	public Command cmd = null;
}
