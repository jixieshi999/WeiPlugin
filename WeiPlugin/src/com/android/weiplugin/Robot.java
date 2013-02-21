package com.android.weiplugin;

import android.content.Context;
import android.graphics.PixelFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.WindowManager;
import android.view.WindowManager.LayoutParams;

/**
 * ��Ҫ
 * <uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW"/>
 * <uses-permission android:name="android.permission.SYSTEM_OVERLAY_WINDOW" /> 
 * 
 * */
public class Robot {
    
    
    Context mContext;
    
    WindowManager wm = null;
    WindowManager.LayoutParams wmParams = null;
    View view;
    private float mTouchStartX;
    private float mTouchStartY;
    private float x;
    private float y;
    
    boolean show;
    
    public Robot(Context context) {
        super();
        // TODO Auto-generated constructor stub
        mContext = context;
        initView(context);
        initLayout();
    }

	private void initView(Context context) {
		view = LayoutInflater.from(context).inflate(R.layout.top_view, null);
        view.setOnTouchListener(new OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                    // ��ȡ�����Ļ����꣬������Ļ���Ͻ�Ϊԭ��
                    x = event.getRawX();
                    // 25��ϵͳ״̬���ĸ߶�,Ҳ����ͨ��õ�׼ȷ��ֵ���Լ�΢��������
                    y = event.getRawY()-25 ;
                    switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:
                            // ��ȡ���View����꣬���Դ�View���Ͻ�Ϊԭ��
                            mTouchStartX = event.getX();
                            mTouchStartY = event.getY()+view.getHeight()/2;
                            break;
                    case MotionEvent.ACTION_MOVE:
                            updateViewPosition();
                            break;
                    case MotionEvent.ACTION_UP:
                            updateViewPosition();
                            mTouchStartX = mTouchStartY = 0;
                            break;
                    }
                    return true;
            }

    });
	}

    private void createView() {
        wm.addView(view, wmParams);
    }

	private void initLayout() {
		wm = (WindowManager) mContext.getApplicationContext().getSystemService("window");
        // ����LayoutParams(ȫ�ֱ�������ز���
        
        wmParams = new LayoutParams(LayoutParams.FILL_PARENT,
                LayoutParams.WRAP_CONTENT, LayoutParams.TYPE_SYSTEM_ERROR,
                LayoutParams.FLAG_NOT_FOCUSABLE, PixelFormat.TRANSPARENT);
        //layoutParams.gravity = Gravity.RIGHT|Gravity.BOTTOM; //���ʼ�����½���ʾ
       
        wmParams.type = WindowManager.LayoutParams.TYPE_PHONE;// �������ṩ���û���������������Ӧ�ó����Ϸ���������״̬������
        wmParams.flags |= WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE;// �������κΰ����¼�
        wmParams.gravity = Gravity.LEFT | Gravity.TOP; // ������������Ͻ�
        // ����Ļ���Ͻ�Ϊԭ�㣬����x��y��ʼֵ
        wmParams.x = 0;
        wmParams.y = 0;
        // ������ڳ������
        wmParams.width = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        wmParams.format = PixelFormat.RGBA_8888;
	}

    private void removewRobot(){
    	wm.removeView(view);
    }
    public void hideRobot(){
    	if(show){
    		removewRobot();
    		show = false;
    	}
    }
    public void showRobot(){
    	if(!show){
    		createView();
    		show = true;
    	}
    }
    private void updateViewPosition() {
        // ���¸�������λ�ò���
        wmParams.x = (int) (x - mTouchStartX);
        wmParams.y = (int) (y - mTouchStartY);
        wm.updateViewLayout(view, wmParams);
    }
    public void updateRobot(int x,int y){
    	showRobot();
    	updateViewPosition(x,y);
    }
    private  void updateViewPosition(int x,int y) {
    	
    	wmParams.x = (int) Math.max(0, (x - mTouchStartX));
    	wmParams.y = (int) Math.max(0, (y - mTouchStartY));
    	wm.updateViewLayout(view, wmParams);
    }
    
}
