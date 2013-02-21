package com.android.weibot.apis.thread;

import android.os.HandlerThread;

public class AsyncHandlerFactory {
    public  class Mode{
    	private static final int MODE_NONE = 3;
        public final static int  MODE_NET = MODE_NONE+4;
        public final static int MODE_IO = MODE_NONE+5;
        public final static int MODE_BODY = MODE_NONE+6;
        public final static int MODE_THINK = MODE_NONE+7;
        public final static int MODE_LOG =MODE_NONE+8;
    }
    
   public static AsynHandler createAsynHandler(int mode,AsynHandler.CallBack callBack){

        HandlerThread looperThread = new HandlerThread(""+mode,
                android.os.Process.THREAD_PRIORITY_BACKGROUND);
        looperThread.start();
        AsynHandler handler = new AsynHandler(looperThread.getLooper());
        handler.setMode(mode);
        handler.setCallBack(callBack);
        return handler;
        
    }
}
