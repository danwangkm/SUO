package wangda.sparkii.suo.activities;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

import wangda.sparkii.suo.concept.WordExpInfo;
import wangda.sparkii.suo.db.DictionaryDatabase;
import wangda.sparkii.suo.download.HttpDownloader;
import wangda.sparkii.suo.xml.MyContentHandler;
import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.widget.Toast;

public class DownLoadService extends Service {

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		System.out.println("DownLoadService: Service onBind");
		return null;
	}

	// Create a service
	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		System.out.println("DownLoadService: New download Service onCreate");
	}

	// Service activity
	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		// TODO Auto-generated method stub

		System.out.println("DownLoadService: Download Service onStartCommand");

		// 启动新线程
		DownloadThread downloadThread = new DownloadThread();
		Thread thread = new Thread(downloadThread);
		thread.start();
		this.stopService(intent);
		return START_NOT_STICKY;
	}

	/**
	 * 如果xml文件太大，可以新建一个线程来下载，避免阻塞
	 * 
	 * @author Wang Da
	 * 
	 */
	class DownloadThread implements Runnable {

		public void run() {
			// download XML
			String resultStr = downloadXML("http://52.91.35.111/testing.xml");
			System.out.println(resultStr);
			Message msg = msgHandler.obtainMessage();// use message to transmit information between new thread and main thread
			// parse the XML
			if (parse(resultStr)) {
				msg.what = 1;
				msgHandler.sendMessage(msg);
			}
			
		}

	}

	private final Handler msgHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Toast.makeText(DownLoadService.this, "update success",
						Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(DownLoadService.this, "update fail",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};

	/**
	 * down load the XML file
	 * 
	 * @param urlStr
	 * @return
	 */
	private String downloadXML(String urlStr) {
		HttpDownloader httpDownloader = new HttpDownloader();
		String result = httpDownloader.download(urlStr);
		return result;
	}

	/**
	 * parse the xml String and store the information into database
	 * 
	 * @param xmlStr
	 * @return
	 */
	private boolean parse(String xmlStr) {
		SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
		List<WordExpInfo> infos = new ArrayList<WordExpInfo>();
		// get database
		DictionaryDatabase dbHelper = new DictionaryDatabase(
				DownLoadService.this, "dictionary");
		SQLiteDatabase db = dbHelper.openDatabase(DownLoadService.this);
		//db.execSQL("ALTER TABLE dictionary CHARACTER SET = utf8 ;");
		//System.out.println(db.getPath());
		try {
			XMLReader xmlReader = saxParserFactory.newSAXParser()
					.getXMLReader();
			MyContentHandler myContentHandler = new MyContentHandler(infos);
			xmlReader.setContentHandler(myContentHandler);
			xmlReader.parse(new InputSource(new StringReader(xmlStr)));
			for (Iterator<WordExpInfo> iterator = infos.iterator(); iterator.hasNext();) {
				WordExpInfo wordInfo = (WordExpInfo) iterator.next();
				
				// decide whether the word is exist in the database. if not
				// insert the word
				Cursor cursor = db.query("dictionary", null, "expID = ?",
						new String[] { wordInfo.getExpId() }, null, null, null);
				if (!(cursor.moveToFirst())) {
					// insert wordInfo into database
					ContentValues values = new ContentValues();
					values.put("expID", wordInfo.getExpId());
					values.put("word", wordInfo.getName().toUpperCase());
					values.put("english", wordInfo.getExp());
					values.put("expUser", wordInfo.getExpUser());
					db.insert("dictionary", null, values);// execute insert
															// action
					System.out.println("DownLoadService: insert--------> " + wordInfo);
				} else {
					// update word exp,现在做的有点麻烦……
					db.delete("dictionary", "expID = ?",
							new String[] { wordInfo.getExpId() });
					ContentValues values = new ContentValues();
					values.put("expID", wordInfo.getExpId());
					values.put("word", wordInfo.getName().toUpperCase());
					values.put("english", wordInfo.getExp());
					values.put("expUser", wordInfo.getExpUser());
					db.insert("dictionary", null, values);// execute insert action
					System.out.println("DownLoadService: update " + wordInfo.getName() + " explanation");
				}
				cursor.close();
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			db.close();
			return false;
		}
		return true;
	}

	// Destroy a service
	@Override
	public void onDestroy() {
		// TODO Auto-generated method stub
		System.out.println("DownLoadService: Download Service onDestory");
		super.onDestroy();
	}
}
