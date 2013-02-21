package com.android.weiplugin.action;

/**
 * use to add lot of plugin with plugin lib(one apk file)
 * <p>
 * <code>
 *  <p>PluginTools pluginTools = new PluginTools();
 *  <p>pluginTools.addPluginClass(clsName,type).addPluginClass(clsName,type).addPluginClass(clsName,type);
 *  <p>String clsnames = pluginTools.getPluginClassName();
 *  <p>String types =  pluginTools.getPluginClassName();
 * </code>
 * <p>
 * <p>
 * @author jixieshi@me.com 20121207
 * */
public class PluginTools {

	StringBuilder sbCls ;
	StringBuilder sbType ;
	
	public PluginTools() {
		super();
		// TODO Auto-generated constructor stub
		sbCls = new StringBuilder();
		sbType = new StringBuilder();
	}

	public PluginTools addPluginClass(Class cls,int type){
		if(cls==null){
			System.out.println("addClass the param cls is null !!!");
			return this;
			
//			throw new NullPointerException("the param cls is null !!!");
		}
		if(type<0){
			System.out.println("addClass the illegal param ,type < 0 !!!");
			return this;
//			throw new NullPointerException("get illegal param ,type < 0 !!!");
		}
		sbCls.append(cls.getName()).append("|");
		sbType.append(type).append("|");
		return this;
	}
	public String getPluginClassName(){
		return sbCls.toString();
	}
	public String getPluginTypeName(){
		return sbType.toString();
	}
}
