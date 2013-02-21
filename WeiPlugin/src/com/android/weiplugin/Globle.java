package com.android.weiplugin;

import android.app.Activity;
import android.content.Context;

import com.android.weiplugin.config.Configs;
import com.android.weiplugin.database.DatabaseHelper;

public class Globle {

    /**
     * application context 
     * */
	public static Context sContext;
	/**
	 * activity context
	 * */
	public static Activity sActivity;

	public static DatabaseHelper mDatabaseHelper;
	static void init(Context context){
		sContext = context;

        mDatabaseHelper = new DatabaseHelper(sContext, Configs.DATABASENAME, null, 1);
	}
}
