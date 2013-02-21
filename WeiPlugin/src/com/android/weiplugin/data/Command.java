package com.android.weiplugin.data;

import java.io.Serializable;
import java.util.ArrayList;

import com.android.weiplugin.log.LogTools;

public class Command implements Serializable{

    /**
     * 
     */
    private static final long serialVersionUID = 5387561957481992882L;
    public static interface Item{
    	final String name="Comnand";
    	
        final String type="type";
        final String level="level";
        final String param="param";
        final String keyword="keyword";
        final String words="words";
        final String extra="extra";
        final String time="time";
        final String editble="editble";
        final String reusable="reusable";
        final String dirty="dirty";
    }
    
    
    
    public static interface Result{
        
        /**Command order */
        final String SUCCECSS = "success";
        final String NONE = "";

        /**Command order */
        final String FAILED = "failed";

        /**Command order */
        final String FILTE = "filte";
        final String ERROR = "error";
        final String HANDLE_AGAIN= "again";
        /**
         * 用于自定运行的命令
         * 确认是否需要判断是否存在该命令，存在则不添加，否则需要添加该命令
         * */
        final String HANDLE_CONFIRM= "confirm";
    }
    public static interface Type{
        
        /**Command order */
        final String TYPE_ORDER = "type_order";
        
        /**Command order */
        final String TYPE_MSG = "type_msg";
        
        /**Command order */
        final String TYPE_KEYWORD = "type_keyword";
    }
    /**
     * #{@link Type}
     * <p>Words.Type.TYPE_XXX
     * */
    public String type = "";
    
    public String param  = "";
    
    /**该命令能否被修改
     * 0可以修改，1不能修改
     * */
    public int editble= 0;
    
    /**该命令是否可以被重复
     * 0 可以重复，1不能重复
     * */
    public int reusable= 0;
    
    public String getParam() {
        return param;
    }

    public Command() {
        super();
        type = Type.TYPE_MSG;
    }

    
	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public long getTime() {
		return time;
	}

	public void setTime(long time) {
		this.time = time;
	}

	public String getKeyword() {
		return keyword;
	}

	public void setKeyword(String keyword) {
		this.keyword = keyword;
	}

	public String getWords() {
		return words;
	}

	public void setWords(String words) {
		this.words = words;
	}

	public String getExtra() {
		return extra;
	}

	public void setExtra(String extra) {
		this.extra = extra;
	}

	public Command clone() {
		// TODO Auto-generated method stub\
    	Command command = new Command();
    	command.type = type;
    	command.keyword = keyword;
    	command.editble = editble;
    	command.extra = extra;
    	command.level  = level;
    	command.param = param;
    	command.reusable = reusable;
    	command.words = words;
    	command.pluginType = pluginType;
//    	command.time = System.currentTimeMillis();
		return command;
	}


	public long level = 0;
    
	public int pluginType = 0;
	
    public long time = 0;
    

    public String keyword  = "";

    public String words  = "";
    
    public String extra  = "";

    
   


    public long getLevel() {
		return level;
	}

	public void setLevel(long level) {
		this.level = level;
	}

	@Override
	public String toString() {
		return "Command [type=" + type + ", param=" + param + ", editble="
				+ editble + ", reusable=" + reusable + ", level=" + level
				+ ", pluginType=" + pluginType + ", time=" + time
				+ ", keyword=" + keyword + ", words=" + words + ", extra="
				+ extra + "]";
	}


	public static final String PARAM_SPLIT = "#";
    /**
     * 没有考虑顺序问题
     * 分解参数
     * */
    public static ArrayList<String> splitParam(String data){
    	ArrayList<String> param = new ArrayList<String>();
    	String []sub= data.split(PARAM_SPLIT);

    	for(String sss:sub){
        	LogTools.log("add : "+sss);
        	param.add(sss);
    	}
    	return param;
    }
    
    
}
