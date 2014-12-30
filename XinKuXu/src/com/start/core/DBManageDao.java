package com.start.core;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.start.service.bean.WordItem;

public  class DBManageDao {

	private Context mContext;
	private SQLiteDBHelper mDBHelper;
	private SQLiteDatabase mSQLiteDatabase;
	
	public DBManageDao(Context context) {
		this.mContext=context;
		this.mDBHelper = new SQLiteDBHelper(context);
		this.mSQLiteDatabase = this.mDBHelper.getWritableDatabase();
	}
	
	public Context getContext(){
		return mContext;
	}
	
	public SQLiteDBHelper getDBHelper() {
		return mDBHelper;
	}

	public SQLiteDatabase getSQLiteDatabase() {
		return mSQLiteDatabase;
	}
	
	public void deleteAllChannelItem() {
		getSQLiteDatabase().delete(WordItem.TABLENAME, null, null);
	}
	
	public void saveWordItem(WordItem ci){
		ContentValues values = new ContentValues();
		values.put("id", ci.getId());
		values.put("englishName", ci.getEnglishName());
		values.put("phoneticSymbols", ci.getPhoneticSymbols());
		values.put("chineseSignificance", ci.getChineseSignificance());
		values.put("exampleEnglish", ci.getExampleEnglish());
		values.put("exampleChinese", ci.getExampleChinese());
		values.put("fillProblem", ci.getFillProblem());
		values.put("fillAnswer", ci.getFillAnswer());
		values.put("memoryMethodA", ci.getMemoryMethodA());
		values.put("memoryMethodB", ci.getMemoryMethodB());
		values.put("exampleImage", ci.getExampleImage());
		values.put("memoryImage", ci.getMemoryImage());
		values.put("englishAudio", ci.getEnglishAudio());
		getSQLiteDatabase().insert(WordItem.TABLENAME, null, values);
	}
	
	public List<WordItem> findAllWordItem() {
		List<WordItem> channelItems = new ArrayList<WordItem>();
		Cursor cursor = getSQLiteDatabase().query(WordItem.TABLENAME,
				new String[] { "id", "englishName", "phoneticSymbols",
				"chineseSignificance", "exampleEnglish", "exampleChinese",
				"fillProblem", "fillAnswer", "memoryMethodA",
				"memoryMethodB", "exampleImage", "memoryImage","englishAudio"},null, null, null, null, null);
		try {
			if (cursor.moveToFirst()) {
				do {
					WordItem ci = new WordItem();
					ci.setId(cursor.getString(cursor.getColumnIndex("id")));
					ci.setEnglishName(cursor.getString(cursor.getColumnIndex("englishName")));
					ci.setPhoneticSymbols(cursor.getString(cursor.getColumnIndex("phoneticSymbols")));
					ci.setChineseSignificance(cursor.getString(cursor.getColumnIndex("chineseSignificance")));
					ci.setExampleEnglish(cursor.getString(cursor.getColumnIndex("exampleEnglish")));
					ci.setExampleChinese(cursor.getString(cursor.getColumnIndex("exampleChinese")));
					ci.setFillProblem(cursor.getString(cursor.getColumnIndex("fillProblem")));
					ci.setFillAnswer(cursor.getString(cursor.getColumnIndex("fillAnswer")));
					ci.setMemoryMethodA(cursor.getString(cursor.getColumnIndex("memoryMethodA")));
					ci.setMemoryMethodB(cursor.getString(cursor.getColumnIndex("memoryMethodB")));
					ci.setExampleImage(cursor.getString(cursor.getColumnIndex("exampleImage")));
					ci.setMemoryImage(cursor.getString(cursor.getColumnIndex("memoryImage")));
					ci.setEnglishAudio(cursor.getString(cursor.getColumnIndex("englishAudio")));
					channelItems.add(ci);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
		return channelItems;
	}

}