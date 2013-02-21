package com.android.weibot.apis.thread;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.android.weiplugin.log.LogTools;



/**
 * @author jixieshi@me.com 20121108
 * asyn handler
 * */
public class AsynHandler  extends Handler {


	public final int MSG_QUIT = 342;
	
    public int mMode;
    
    CallBack mCallBack;
    public CallBack getCallBack() {
		return mCallBack;
	}

	public void setCallBack(CallBack callBack) {
		this.mCallBack = callBack;
	}


	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		AsynHandler other = (AsynHandler) obj;
		if (mMode != other.mMode)
			return false;
		return true;
	}

	public interface CallBack{
    	void handleMessage(Message msg) ;
    }
    public int getMode() {
		return mMode;
	}

	public void setMode(int mode) {
		this.mMode = mode;
	}

	public AsynHandler(Looper looper) {
        super(looper);
    }

	@Override
	public void handleMessage(Message msg) {
		// TODO Auto-generated method stub
		switch (msg.what) {
		case MSG_QUIT:
            LogTools.logToFile(getClass().getName(), mMode+" handler  quit ");
			getLooper().quit();
			break;

		default:
			break;
		}
		if(null!=mCallBack){
			mCallBack.handleMessage(msg);
		}
		super.handleMessage(msg);
	}

	public void quit(){
		sendEmptyMessage(MSG_QUIT);
	}

}
