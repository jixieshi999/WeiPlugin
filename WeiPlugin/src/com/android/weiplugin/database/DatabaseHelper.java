package com.android.weiplugin.database;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteCursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

import com.android.weiplugin.R;
import com.android.weiplugin.actionloader.PluginEntry;
import com.android.weiplugin.data.Command;
import com.android.weiplugin.log.Debug;
import com.android.weiplugin.log.LogTools;
import com.android.weiplugin.tools.DebugTools;


public class DatabaseHelper extends SQLiteOpenHelper {

	static final private String TAG="DatabaseHelper ";
	
	Context mContext;

	public DatabaseHelper(Context context, String name, CursorFactory factory,
			int version) {
		super(context, name, factory, version);
		mContext = context;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
	    
	    //批量建多张表
	    String[] sql = mContext.getString(R.string.sql_create_tables).split(";");
        db.beginTransaction();
        try {
            // Create tables & test data
            execMultipleSQL(db, sql);
            db.setTransactionSuccessful();
        } catch (SQLException e) {
        	Debug.dLog("eee",e);
        } finally {
            db.endTransaction();
        }
        
		// TODO Auto-generated method stub
//	    String str ="";
////	    str = "";//drop table if exist Configs,Dirctionarys,Plugins,PluginsCommand,WeiData,Comnand,PendingCommand
////	    db.execSQL(str);
//		
//		str = "create table Configs( ID integer PRIMARY KEY,property vacher(20),key vacher(20) not null,data varcher(30));";
//		db.execSQL(str);
//		str = "create table Dirctionarys( ID integer PRIMARY KEY,key vacher(20) not null,value vacher(50));";
//		db.execSQL(str);
//		
//		str = "create table Plugins( className vacher(100) PRIMARY KEY,type int,level int,name vacher(20),destription vacher(100),cmd vacher(20),show int default 0 );";
//		db.execSQL(str);
//		str = "create table PluginsCommand( className vacher(100) not null," +
//				"type int,level int,name vacher(20),destription vacher(100)," +
//				"cmd vacher(20) not null,show int default 0,primary key (className,cmd)  );";//primary key (className,cmd)
//		db.execSQL(str);
//
//		str = "create table WeiData( ID integer PRIMARY KEY autoincrement," +
//		        "key vacher(50),content  vacher(255),title vacher(100),time long,remark vacher(100),oriTime vacher(50)," +
//		        "type int ,sequence int default 0,field vacher(50),wgroup vacher(50) );";//primary key (className,cmd)
//		db.execSQL(str);
//		
//		
//		String commandTable = " ( ID integer PRIMARY KEY," +
//				""+Command.Item.time+" vacher(50),"
//				+Command.Item.level+" integer,"
//				+Command.Item.words+" vacher(100),"
//				+Command.Item.editble+" vacher(1) default 0,"
//				+Command.Item.reusable+" vacher(1) default 0,"
//				+Command.Item.type+" vacher(20),"
//				+Command.Item.param+" vacher(20),"
//				+Command.Item.extra+" vacher(50),"
//				+Command.Item.dirty+" vacher(1),"
//				+Command.Item.keyword+" vacher(50) not null);";
//		str = "create table "+Command.Item.name+commandTable;
//		db.execSQL(str);
//		
//		str = "create table "+PendingCommand.name+commandTable;
//		db.execSQL(str);
	}
	/**
     * Execute all of the SQL statements in the String[] array
     * 
     * @param db
     *            The database on which to execute the statements
     * @param sql
     *            An array of SQL statements to execute
     */
    private void execMultipleSQL(SQLiteDatabase db, String[] sql) {
    	String str =null;
        for (String s : sql){
        	str = s.trim(); 
        	if (str.length() > 0){
        		Debug.dLog("eee",str);
        		if(str.startsWith("#")){
        		}else{
        			db.execSQL(str);
        		}
        	}
        }
    }
    /**
     * 查找字段是否存在
     * @param colName
     * @return
     */
    public boolean columnIsExist(String colName, String tableName){
        boolean isExist = false;
        try{
            String sql = "select  *  from " + tableName;
            SQLiteCursor cursor = this.query(sql);
            isExist =  cursor.getColumnIndex(colName)>-1;
            cursor.close();
        }catch(Exception ex){
            // ex.printStackTrace();
        }
        return isExist;
    }
    
    
    
    /**
     * 查找某表是否存在
     * @param tableName
     * @return
     */
    public boolean tableIsExist(String tableName){
        boolean isExist = false;
        if(tableName == null){
            return false;
        }
        try{
            String sql = "select count(*) from  Sqlite_master  where type ='table' and name ='"+tableName.trim()+"' ";
            SQLiteCursor cursor = this.query(sql);
            while(cursor.moveToNext()){
                int count = cursor.getInt(0);   
                if(count > 0){
                    isExist = true;
                }
            }
            cursor.close();
        }catch(Exception ex){
            ex.printStackTrace();
        }
        return isExist;
    }
    public synchronized android.database.sqlite.SQLiteCursor query(String SQL) {
        // getDB();
        SQLiteDatabase db = getWritableDatabase();
        // SQLiteDatabase db = this.getReadableDatabase();
        if(db.isOpen()){
            SQLiteCursor cursor = (android.database.sqlite.SQLiteCursor) db
            .rawQuery(SQL, null);
            return cursor;
        }else{
            return null;
        }
    
    }
	/**
	 * insert table name
	 * */
	public void addDiractionarys(String name,String value){
		String sql =" insert into Dirctionarys(key,value) values(?,?);";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(sql, new Object[]{name,value});
	}
	
	
	
	public Cursor getWeiDataGroupID(String groupKey){
		String sql =" select ID from  WeiDataGroup where key = ? ;";
		SQLiteDatabase db = getWritableDatabase();
		return db.rawQuery(sql, new String[]{groupKey});
	}
	public Cursor getWeiDataGroupData(int id){
		String sql =" select sequence from  WeiDataGroup where ID = ? ;";
		SQLiteDatabase db = getWritableDatabase();
		return db.rawQuery(sql, new String[]{""+id});
	}
	
	public void insertWeiDataGroup(Object []objs){
		String sql =" insert into  WeiDataGroup(key,title,time,remark,oriTime,type,sequence) values(?,?,?,?,?,?,?) ;";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(sql, objs);
	}
	
	public void deleteWeiData(String key){
		String sql =" delete from WeiData where key = ? ;";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(sql, new Object[]{key});
	}
	public void deleteWeiData(){
		String sql =" delete from WeiData ;";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(sql);
	}
	public Cursor getWeiDataGroupList(){
//	    String sql =" select DISTINCT wgroup from WeiData ";
	    String sql =" select title,ID from WeiDataGroup  order by sequence desc";
	    SQLiteDatabase db = getWritableDatabase();
	    return db.rawQuery(sql,null);
	}
	public Cursor getWeiDataGroupKeyList(){
//	    String sql =" select DISTINCT wgroup from WeiData ";
		String sql =" select key,ID,title from WeiDataGroup  order by sequence desc";
		SQLiteDatabase db = getWritableDatabase();
		return db.rawQuery(sql,null);
	}
	public Cursor getWeiDataGroup(int startPos,int count,String group){
	    String sql =" select content,title,time,remark,oriTime,Key,field,wgroup from WeiData "
	    		+" where wgroup = ? "
	    		+" order by sequence "
	    		+" LIMIT ?,? ";
	    SQLiteDatabase db = getWritableDatabase();
	    return db.rawQuery(sql, new String[]{group,startPos+"",count+""});
	}
	/**
	 * 
	 * @deprecated ,instead of getWeiDataGroupByIDList
	 * */
	public Cursor getWeiDataGroupGroupKeyList(int startPos,int count,ArrayList<String> groupList){
		StringBuilder sb = new StringBuilder();
		if (groupList.size() > 0) {
			sb.append("where wgroup in(");
			for (String group : groupList) {
				sb.append("'").append(group).append("'").append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append(")");
		}
		String sql =" select content,title,time,remark,oriTime,Key,field,wgroup from WeiData "
				+sb.toString()
				+" order by sequence "
				+" LIMIT ?,? ";
		SQLiteDatabase db = getWritableDatabase();
		Debug.dLog("getWeiDataGroup : "+sql);
		return db.rawQuery(sql, new String[]{startPos+"",count+""});
	}
	/**
	 * 根据组ID来获取相对应的数据
	 * */
	public Cursor getWeiDataGroupByIDList(int startPos,int count,ArrayList<Integer> groupList){
		StringBuilder sb = new StringBuilder();
		if (groupList.size() > 0) {
			sb.append("where WeiDataGroupID in(");
			for (Integer group : groupList) {
				sb.append("'").append(group).append("'").append(",");
			}
			sb.deleteCharAt(sb.length()-1);
			sb.append(")");
		}
		String sql =" select content,title,time,remark,oriTime,Key,field,wgroup from WeiData "
				+sb.toString()
				+" order by ID desc"
				+" LIMIT ?,? ";
		SQLiteDatabase db = getWritableDatabase();
		Debug.dLog("getWeiDataGroup : "+sql);
		return db.rawQuery(sql, new String[]{startPos+"",count+""});
	}
	
	public  Cursor getWeiData(int startPos,int count){
	    String sql =" select content,title,time,remark,oriTime,Key,field,wgroup from WeiData "
	    +" order by ID  desc"
	    +" LIMIT ?,? ";
	    SQLiteDatabase db = getWritableDatabase();
	    return db.rawQuery(sql, new String[]{startPos+"",count+""});
	}
	/**
	 * @deprecated  use {@link DatabaseHelper.addPlugin(PluginEntry)}
	 * */
	public void addPlugin(Object[] objs){
		String sql =" insert into Plugins(className,type,level,cmd,name) values(?,?,?,?,?);";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(sql, objs);
	}
	
	public void addPluginsCommand(Object[] objs){
		String sql =" insert into PluginsCommand(className,type,level,cmd,name,destription,show) values(?,?,?,?,?,?,?);";
		SQLiteDatabase db = getWritableDatabase();
		DebugTools.log(sql+" | ");
		DebugTools.log(objs);
		db.execSQL(sql, objs);
	}
	public Cursor ReadTable(String name){
		String sql =" select * from  "+name+";";
		SQLiteDatabase db = getWritableDatabase();
		DebugTools.log(sql+" | ");
		return db.rawQuery(sql, null);
	}
	public void addPluginsCommand(PluginEntry entry){
		String sql =" insert into PluginsCommand(className,type,level,name,destription,show,cmd) values(?,?,?,?,?,?,?);";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(sql, new Object[]{entry.clsName,entry.type,entry.level,entry.name,entry.destription,entry.show,entry.cmd.keyword});
	}
	public void addPlugin(PluginEntry entry){
		String sql =" insert into Plugins(className,type,level,name,destription,show,cmd) values(?,?,?,?,?,?,?);";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(sql, new Object[]{entry.clsName,entry.type,entry.level,entry.name,entry.destription,entry.show,entry.cmd.keyword});
	}
	public boolean isPluginExist(PluginEntry entry){
		boolean flag = isPluginExist(entry.clsName);
		return flag;
	}
	public boolean isPluginExist(String clsName){
		boolean flag = false;
		String sql =" select Count(*) from  Plugins where className = ? ;";
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[]{clsName});
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		if(count>0){
			flag = true;
		}
		return flag;
	}
	public boolean isPluginCommandExist(String clsName,String cmd){
		boolean flag = false;
		String sql =" select Count(*) from  PluginsCommand where className = ? and cmd =? ;";
		SQLiteDatabase db = getWritableDatabase();
		Cursor cursor = db.rawQuery(sql, new String[]{clsName,cmd});
		cursor.moveToFirst();
		int count = cursor.getInt(0);
		if(count>0){
			flag = true;
		}
		cursor.close();
		return flag;
	}
	public void updatePlugin(String className,int show,int level){
		String sql =" update Plugins set level = ? , show = ? where className = ? ;";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(sql, new Object[]{level,show,className});
	}
	public void updatePluginCommand(String className,String cmd,String show,String level){
		String sql =" update PluginsCommand set level = ? , show = ? where className = ? and cmd = ?;";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(sql, new Object[]{level,show,className,cmd});
	}
	public void updatePlugin(PluginEntry entry){
		String sql =" update Plugins set level = ? , show = ? where className = ? ;";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(sql, new Object[]{entry.level,entry.show,entry.clsName});
	}
	public Cursor readPlugin(){
		String sql =" select className,type,level,name,destription,cmd,show from PluginsCommand order by level ";
		SQLiteDatabase db = getWritableDatabase();
//		db.execSQL(sql, new Object[]{className,type,level});
		return db.rawQuery(sql, null);
	}
	public Cursor readShowPlugin(){
		String sql =" select className,type,level,name,destription,cmd,show from PluginsCommand where show = 0 order by level ";
		SQLiteDatabase db = getWritableDatabase();
//		db.execSQL(sql, new Object[]{className,type,level});
		return db.rawQuery(sql, null);
	}
	
	public void addDiractionarys(String name,String key,String value){
		String sql ="insert into Configs(property,key,data) values(?,?,?);";
		SQLiteDatabase db = getWritableDatabase();
		db.execSQL(sql, new Object[]{name,key,value});
	}
	
	public void addWords(Command word,String name){
		SQLiteDatabase db = getWritableDatabase();
		
		String sql ="insert into "+Command.Item.name+"(time,level,words,type,param,extra,keyword) values(?,?,?,?,?,?,?);";
		db.execSQL(sql, new Object[]{word.time,word.level,word.words,word.type,word.param,word.extra,word.keyword});
	}
	
	public int updateTables(String tableName,ContentValues values,String where,String[]objs){
		SQLiteDatabase db = getWritableDatabase();
		return db.update(tableName, values, where, objs);
	}
	
	/**
	 * 判断该命令是否已经存储在数据库中
	 * @param flag ,true means query command,dirty = 1,false means not contain dirty value;
	 * */
	public boolean isPendingWordsExist(Command cmd,boolean flag){
	    // select 
	    StringBuilder sb= new StringBuilder();
	    sb.append("SELECT count(*) from PendingCommand ");
	    sb.append(" where keyword ='"+cmd.keyword+"' ");
	    if(flag){
	        sb.append(" and dirty = 1 ");
	    }
	    int count = getTableCount(sb.toString());
	    
	    return count>0;
	}
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		
	}
	
    public  int getTableCount(String sql){
        SQLiteDatabase db = getWritableDatabase();
        return getTableCount(sql,db);
    }
    public  int getTableCount(String sql,SQLiteDatabase db){
        int countNum = 0; // 行数
        Cursor cursor = db.rawQuery(sql,null);
        while (cursor.moveToNext()) {
            countNum = cursor.getInt(0);
        }
        cursor.close();
        return countNum;
    }
	
}
