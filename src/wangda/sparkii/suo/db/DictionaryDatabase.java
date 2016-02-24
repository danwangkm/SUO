package wangda.sparkii.suo.db;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;

import wangda.sparkii.suo.R;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DictionaryDatabase extends SQLiteOpenHelper {

	private static final int VERSION = 1;
	public final static String DATABASE_PATH = android.os.Environment
			.getExternalStorageDirectory().getAbsolutePath() + "/dictionary";
	public final static String DATABASE_FILENAME = "dictionary.db";

	public DictionaryDatabase(Context context, String name,
			CursorFactory factory, int version) {
		super(context, name, factory, version);
		// TODO Auto-generated constructor stub
	}

	public DictionaryDatabase(Context context, String name) {
		this(context, name, VERSION);
	}

	public DictionaryDatabase(Context context) {
		this(context, "dictionary");
	}

	public DictionaryDatabase(Context context, String name, int version) {
		this(context, name, null, version);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		// TODO Auto-generated method stub
		System.out.println("Create a Database");
		db.execSQL("create table dictionary(expID int, word varchar(20), english varchar(255), "+
		"chinese varchar(255), category varchar(255), expUser varchar(255), exp_1 varchar(255), "+
		"exp_2 varchar(255), exp_3 varchar(255), exp_4 varchar(255), exp_5 varchar(255), good int default 0)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		// TODO Auto-generated method stub
		System.out.println("update a Database from " + oldVersion + " to "
				+ newVersion);
	}

	public boolean deleteDatabase(Context context) {
		System.out.println("delete a Database");
		return context.deleteDatabase("dictionary");
	}

	public SQLiteDatabase openDatabase(Context context) {
		try {
			// ���dictionary.db�ļ��ľ���·��
			String databaseFilename = DATABASE_PATH + "/" + DATABASE_FILENAME;
			File dir = new File(DATABASE_PATH);
			// ���/sdcard/dictionaryĿ¼�����ڣ��������Ŀ¼
			if (!dir.exists())
				dir.mkdir();
			// �����/sdcard/dictionaryĿ¼�в����� dictionary.db�ļ������res\rawĿ¼�и�������ļ��� SD����Ŀ¼��/sdcard/dictionary��
			if (!(new File(databaseFilename)).exists()) {
				
				// ��÷�װdictionary.db�ļ���InputStream����
				InputStream is = context.getResources().openRawResource(
						R.raw.dictionary);
				FileOutputStream fos = new FileOutputStream(databaseFilename);
				byte[] buffer = new byte[8192];
				int count = 0;
				// ��ʼ����dictionary.db�ļ�
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
				}

				fos.close();
				is.close();
			}
			// ��/sdcard/dictionaryĿ¼�е�dictionary.db�ļ�
			SQLiteDatabase database = SQLiteDatabase.openOrCreateDatabase(
					databaseFilename, null);
			
			return database;
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * ��������ģ����ѯ
	 * 
	 * @param word
	 * @return
	 */
	public Cursor query(String word) {

		SQLiteDatabase db = this.getReadableDatabase();

		return db.rawQuery(
				"select word as _id from dictionary where word like ?",
				new String[] { word.toString() + "%" });

	}
}
