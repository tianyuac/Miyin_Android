package com.start.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.start.service.bean.WordItem;
import com.start.xinkuxue.BaseContext;

public class WordService {
	
	public static final String DIRPATH="data";
	public static final String AUDIOPATH="audio_word";
	public static final String EXAMPLEIMAGEPATH="image_example";
	public static final String MEMORYIMAGEPATH="image_memory";
	
	public static final String DBEXTENSIONNAME="words.db";
	public static final String PICTUREEXTENSION=".jpg";
	public static final String AUDIOEXTENSION=".mp3";
	
	private SQLiteDatabase mSQLiteDatabase;

	public WordService() throws Exception {
		String dbFileName = BaseContext.getInstance().getStorageDirectory(DIRPATH)+DBEXTENSIONNAME;
		File dbFile=new File(dbFileName);
		if(dbFile.exists()){
			mSQLiteDatabase = SQLiteDatabase.openOrCreateDatabase(dbFileName, null);
		}
		if(mSQLiteDatabase==null){
			throw new Exception("数据为空，请联系管理员");
		}
	}
	
	//获取单词讲解音频路径
	public String getAudioPath(String name){
		return BaseContext.getInstance().getStorageDirectory(DIRPATH)+AUDIOPATH+"/"+name+AUDIOEXTENSION;
	}
	
	//获取例句（图片）路径
	public String getExampleImagePath(String name){
		return BaseContext.getInstance().getStorageDirectory(DIRPATH)+EXAMPLEIMAGEPATH+"/"+name+PICTUREEXTENSION;
	}
	
	//获取图形记忆法（图片）路径
	public String getMemoryImagePath(String name){
		return BaseContext.getInstance().getStorageDirectory(DIRPATH)+MEMORYIMAGEPATH+"/"+name+PICTUREEXTENSION;
	}
	
	/**
	 * 获取单词总数
	 * @return
	 */
	public Long getWordCount(){
        String str="select count(id)  from "+WordItem.TABLENAME;
        Cursor cursor = mSQLiteDatabase.rawQuery(str,null);
        cursor.moveToFirst();
        return cursor.getLong(0);
	}
	
	public WordItem findById(int id) {
		Cursor cursor = mSQLiteDatabase.query(WordItem.TABLENAME,
				new String[] { "id", "englishName", "phoneticSymbols",
				"chineseSignificance", "exampleEnglish", "exampleChinese",
				"fillProblem", "fillAnswer", "memoryMethodA",
				"memoryMethodB"},"id=?", new String[]{String.valueOf(id)}, null, null, null);
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
					return ci;
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
		return null;
	}
	
	public List<WordItem> findAll() {
		List<WordItem> channelItems = new ArrayList<WordItem>();
		Cursor cursor = mSQLiteDatabase.query(WordItem.TABLENAME,
				new String[] { "id", "englishName", "phoneticSymbols",
				"chineseSignificance", "exampleEnglish", "exampleChinese",
				"fillProblem", "fillAnswer", "memoryMethodA",
				"memoryMethodB"},null, null, null, null, null);
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
					channelItems.add(ci);
				} while (cursor.moveToNext());
			}
		} finally {
			cursor.close();
		}
		return channelItems;
	}
	
}