package com.android.weiplugin.tools;

import java.io.File;

import android.os.Environment;
import android.os.StatFs;

public class SDCardTools {
    
    public static boolean ExistSDCard() {
        if (android.os.Environment.getExternalStorageState().equals(
          android.os.Environment.MEDIA_MOUNTED)) {
         return true;
        } else
         return false;
       }
    public static  long getSDAllSize(){
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory(); 
        StatFs sf = new StatFs(path.getPath()); 
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize(); 
        //获取所有数据块数
        long allBlocks = sf.getBlockCount();
        //返回SD卡大小
        //return allBlocks * blockSize; //单位Byte
        //return (allBlocks * blockSize)/1024; //单位KB
        return (allBlocks * blockSize)/1024/1024; //单位MB
      }   
    public static  long getSDFreeSize(){
        //取得SD卡文件路径
        File path = Environment.getExternalStorageDirectory(); 
        StatFs sf = new StatFs(path.getPath()); 
        //获取单个数据块的大小(Byte)
        long blockSize = sf.getBlockSize(); 
        //空闲的数据块的数量
        long freeBlocks = sf.getAvailableBlocks();
        //返回SD卡空闲大小
        //return freeBlocks * blockSize;  //单位Byte
        //return (freeBlocks * blockSize)/1024;   //单位KB
        return (freeBlocks * blockSize)/1024 /1024; //单位MB
      }   
}
