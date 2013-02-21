package com.android.weiplugin.tools;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.nio.charset.CharsetEncoder;
import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;


import com.android.weiplugin.data.Command;
import com.android.weiplugin.log.LogTools;

public class WordsTools {
    
    public static final String TAG = "WordsTools";

    public static Command getWordsFromJsonString(String jsonstring){
        Command word =null;
        try {
            JSONObject jsonObject = new JSONObject(jsonstring);
            word = getWordsFromJsonObject(jsonObject);
        } catch (JSONException e) {
            LogTools.logToFile(TAG, e);
        }
        return word;
    }
    public static Command getWordsFromJsonObject(JSONObject json){
        Command word = new Command();
        
        try {
//            word.setWords();
            word.words = json.getString(Command.Item.words);
//            word.setWords(json.getString("test"));//test error
//            word.setKeyword();
            word.keyword = json.getString(Command.Item.keyword);
//            word.setLevel();
            word.level = json.getLong(Command.Item.level);
            word.time = json.getLong(Command.Item.time);
//            setTime();
            word.type = json.getString(Command.Item.type);
            word.extra = json.getString(Command.Item.extra);
//            word.setType();
//            word.setExtra();
            word.param = json.getString(Command.Item.param);
//            word.setParam();
            
        } catch (JSONException e) {
            LogTools.logToFile(TAG, e);
        }
        
        
        return word;
    }
    public static JSONObject getJsonObjectFromWords(Command word ){
        JSONObject json = new JSONObject();
        try {
            json.put(Command.Item.extra, word.extra);
            json.put(Command.Item.keyword, word.keyword);
            json.put(Command.Item.level, word.level);
            json.put(Command.Item.time, word.time);
            json.put(Command.Item.type, word.type);
            json.put(Command.Item.param, word.param);
            json.put(Command.Item.words, word.words);
        } catch (JSONException e) {
                LogTools.logToFile(TAG, e);
        }
        return json;
    }
    
    static  void writeWords(JSONObject json, FileOutputStream fs)
    throws JSONException, IOException{
        String mys = json.toString();

        byte[] buffer;
        buffer = mys.getBytes("GB2312");
        fs.write(buffer, 0, buffer.length);
        fs.write('\n');
    }
    
     static void writeWordsToFile(Command word ,FileOutputStream fs) throws IOException {
        try {
            writeWords(getJsonObjectFromWords(word),fs);
        } catch (JSONException e1) {
            LogTools.logToFile("ConfigTools", "prase json error "
                    + e1);
        }
    }
    
    public static void writeWordsToFile(Command word,String filePath) {
        
        ArrayList<Command> lists = new ArrayList<Command>(1);
        lists.add(word);
        writeWordsToFile(lists,filePath);
        
        lists.clear();
        lists = null;
    }
    public static void writeWordsToFile(ArrayList<Command> lists,String filePath) {
        
        FileOutputStream fs = null;
        try {
            
            File file = new File(filePath);
            if (!file.exists()) {
                LogTools.log(filePath+"  not exist ");
                return;
            }else{
                
            }
            fs = new FileOutputStream(file, true);
            for(Command  word:lists){
                writeWordsToFile(word, fs);
            }
            
        } catch (Exception e) {
            LogTools.logToFile("ConfigTools", "prase json io error "
                    + e );
        } finally {
            try {
                if (null != fs) {
                    fs.close();
                }
            } catch (Exception e) {
            }
        }
        
    }
    
    public static ArrayList<Command> readWordsFromFile(String filePath){
        ArrayList<Command> lists =new ArrayList<Command>();

        BufferedReader  reader =null;
        BufferedInputStream in  =null;
        FileInputStream fis  =null;
        try {
            
            File file = new File(filePath);
            if (!file.exists()) {
                LogTools.log(filePath+"  not exist ");
                return null;
            }else{
                
            }
            fis = new FileInputStream(file);
            in = new BufferedInputStream(fis); 
            
            in.mark(4);   
            byte[] first3bytes = new byte[3];   
            in.read(first3bytes);   
            in.reset();   
            if (first3bytes[0] == (byte) 0xEF && first3bytes[1] == (byte) 0xBB  
                    && first3bytes[2] == (byte) 0xBF) {// utf-8   
                reader = new BufferedReader(new InputStreamReader(in, "utf-8"));    
                LogTools.logToFile("ConfigTools", "read file encoding utf-8");
            } else if (first3bytes[0] == (byte) 0xFF  
                    && first3bytes[1] == (byte) 0xFE) {   
                reader = new BufferedReader(  new InputStreamReader(in, "unicode"));  
                LogTools.logToFile("ConfigTools", "read file encoding unicode");  
            } else if (first3bytes[0] == (byte) 0xFE  
                    && first3bytes[1] == (byte) 0xFF) {   
                reader = new BufferedReader(new InputStreamReader(in,  "utf-16be"));  
                LogTools.logToFile("ConfigTools", "read file encoding utf-16be");  
            } else if (first3bytes[0] == (byte) 0xFF  
                    && first3bytes[1] == (byte) 0xFF) {   
                reader = new BufferedReader(new InputStreamReader(in,  "utf-16le"));   
                LogTools.logToFile("ConfigTools", "read file encoding utf-16le");
            } else {
                reader = new BufferedReader(new InputStreamReader(in, "GBK"));   
                LogTools.logToFile("ConfigTools", "read file encoding GBK");
            } 
            String s1;
            
            Command word = null;
            while((s1 = reader.readLine()) != null) {
                LogTools.log(s1);
                word = getWordsFromJsonString(s1);
                if(null!=word){
                    lists.add(word);
                }
            }
            
        } catch (Exception e) {
            LogTools.logToFile("ConfigTools", "prase json io error "
                    + e );
        } finally {
            try {
                if (null != reader) {
                    reader.close();
                }
                if (null != in) {
                    in.close();
                }
                if (null != fis) {
                    fis.close();
                }
            } catch (Exception e) {
            }
        }
        
        return lists;
    }
    
}
