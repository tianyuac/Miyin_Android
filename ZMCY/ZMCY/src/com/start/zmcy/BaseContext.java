package com.start.zmcy;

import start.core.AppContext;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;

import com.start.core.DBManageDao;
import com.start.service.User;

/**
 * @author Start   
 * @Description: 全局application
 */
public class BaseContext extends AppContext {

	private User mUser;
    private static DBManageDao dbManager;
    private static  SQLiteDatabase mSQLiteDatabase;
	
    @Override
    public void onCreate() {
        super.onCreate();
        getDBManager();
    }
    
    @Override
	public Boolean isTestEnvironmental() {
		return true;
	}

	@Override
	public String getServerURL() {
		return isTestEnvironmental() ?
				"http://115.238.38.126:6666/http/HttpService":
					"http://account.chinacloudapp.cn:81/pwyl/http/HttpService";
	}
	
	@Override
	public String getStorageDirectory(String name){
		return Environment.getExternalStorageDirectory().getPath()+"/ancun/"+name+"/";
	 }

	/**
     * 获取数据库管理对象
     */
    public static DBManageDao getDBManager(){
		if(dbManager == null){
			dbManager = new DBManageDao(getContext());
		}
		return dbManager;
	}
    
    /**
     * 获取数据库操作类 
     */
    public static SQLiteDatabase getSQLiteDatabase() {
    	if(null == mSQLiteDatabase){
    		mSQLiteDatabase = getDBManager().getSQLiteDatabase();
    	}
    	return mSQLiteDatabase;
	}
	
	/**
     * 获取当前用户信息
     */
    public User currentUser(){
    	if(mUser==null){
    		mUser=User.getInstance();
    	}
    	return mUser;
    }
	
}
