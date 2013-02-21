package com.android.weibot.apis.thread;

import java.util.ArrayList;

import android.os.Message;


/**
 * @author jixieshi@me.com 20121108
 * manage handler
 * */
public  class HandlerPool {

//	private static HandlerPool sInstance;
    private ArrayList<AsynHandler> mHandlers;
    
//	public static HandlerPool getInstance(){
//		if(null==sInstance){
//			sInstance = new  HandlerPool();
//		}
//		return sInstance;
//	}
	
      public HandlerPool() {
		super();
		 init();
	}

	public void addHandler(AsynHandler handler){
		mHandlers.add(handler);
	}
    
	public void sendMessage(Message msg,int mode){
		AsynHandler handler = getHandler(mode);
		if(null!=handler){
			handler.sendMessage(msg);
		}
	}
	
	public void removeHandler(int mode){
    	 for(AsynHandler handler:mHandlers){
    		 if(handler.getMode()==mode){
    			 handler.quit();
    			 mHandlers.remove(handler);
    		 }
    	 }
     }
	public AsynHandler getHandler(int mode){
   	 for(AsynHandler handler:mHandlers){
   		 if(handler.getMode()==mode){
   			 return handler;
   		 }
   	 }
   	 return null;
    }
     protected void init(){
    	 if(null==mHandlers){
    		 mHandlers = new ArrayList<AsynHandler>();
    	 }
     }
    
     public void clear(){
    	 if(null!=mHandlers){
    		 mHandlers.clear();
    	 }
     }
     private void release(){
    	 clear();
     }

	@Override
	protected void finalize() throws Throwable {
		// TODO Auto-generated method stub
		release();
		super.finalize();
	}
    
    
    
}
