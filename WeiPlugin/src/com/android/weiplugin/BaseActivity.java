package com.android.weiplugin;

import android.app.Activity;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.view.WindowManager;

import com.android.weiplugin.config.Configs;
import com.android.weiplugin.tools.DebugTools;


/**
 * auto run test
 * */
public class BaseActivity extends Activity implements OnClickListener{

	
	public boolean onTouchEvent(MotionEvent event) {
		if (event.getAction() == MotionEvent.ACTION_DOWN) {
			showXY(event.getX(), event.getY());
		}
		return super.onTouchEvent(event);

	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		switch (Configs.getSkinSwitch()) {
		case 0:
			this.setTheme(R.style.Theme_Default);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
			this.getWindow().getDecorView().setBackgroundResource(R.drawable.default_bg);
			this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN  |
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN); 
			break;
		case 1:
			this.setTheme(R.style.Theme_Default_light);
			requestWindowFeature(Window.FEATURE_NO_TITLE);
//			this.getWindow().getDecorView().setBackgroundResource(R.drawable.default_bg);
			this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN  |
					WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
			break;
		default:
			break;
		}
	}

	/**
	 * 模拟点击事件
	 * 
	 * erform click action 
	 * */
    public void performClick(int x,int y){
//		getWindow().
//		dispatchTouchEvent
		MotionEvent ex  = MotionEvent.obtain(System.currentTimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, x, y, 1);
		MotionEvent ex1  = MotionEvent.obtain(System.currentTimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_UP, x, y, 1);
		dispatchTouchEvent(ex);
		dispatchTouchEvent(ex1);
	}
	private void showXY(float x, float y) {

		if (x > 170 && y > 230 && x < 180 && y < 251) {

//			dosaming();
		}else{
//			tv.setText("x坐标：" + x + " y坐标：" + y);
			
		}
		DebugTools.log("------------------x: "+x+" y: "+y);
	}

	@Override
	public void onClick(View v) {
	}
}
