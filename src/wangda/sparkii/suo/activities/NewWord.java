package wangda.sparkii.suo.activities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import wangda.sparkii.suo.R;

import wangda.sparkii.suo.upload.HandleToServer;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class NewWord extends Activity {
	private Button conButton = null;
	private Button backButton = null;
	private EditText myEnglish = null;
	private EditText myExp = null;
	private EditText myName = null;
	private TextView myWord = null;

	private String word = "";
	private String user = "";
	private String exp = "";
	private String english = "";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.newone);

		// get word that needs more explanation
		Intent intent = getIntent();
		String word = intent.getStringExtra("word");
		String username = intent.getStringExtra("username");

		// initiate the layout items
		myWord = (TextView) findViewById(R.id.textView3);
		myWord.setText(word);
		myEnglish = (EditText) findViewById(R.id.editText_englishexp);
		myExp = (EditText) findViewById(R.id.editText_userexp);
		myName = (EditText) findViewById(R.id.editText_username);
		myName.setText(username);
		conButton = (Button) findViewById(R.id.button3);
		conButton.setOnClickListener(new MyConButtonListener());
		backButton = (Button) findViewById(R.id.button4);
		backButton.setOnClickListener(new MyBackButtonListener());
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_home, menu);
		return true;
	}

	class MyBackButtonListener implements android.view.View.OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			// finish, turn back to word searching activity
			finish();
		}
	}

	class MyConButtonListener implements android.view.View.OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			if(isConnectingToWIFI(getApplicationContext())){
			// insert a new explanation

			// get input information
			word = myWord.getText().toString();
			english = myEnglish.getText().toString();
			exp = myExp.getText().toString();
			user = myName.getText().toString();

			if (!(english.equals("")) && !(exp.equals("")) && !(user.equals(""))) {
				
				uploadConfirmation(word, user, english, exp);

				/*
				 * //get database DictionaryDatabase dbHelper = new
				 * DictionaryDatabase(NewWord.this, "dictionary");
				 * SQLiteDatabase db = dbHelper.getWritableDatabase();
				 * ContentValues values = new ContentValues();
				 * 
				 * values.put("id", 1);//provide an id to indicate the
				 * explanation values.put("word", word);
				 * values.put("explanation", exp); values.put("user", user);
				 * System.out.println("insert--------> word " + word + "exp " +
				 * exp + "user " + user); db.insert("dictionary", null,
				 * values);//execute insert action
				 */
		

			} else
				Toast.makeText(NewWord.this, "Please input complete information", Toast.LENGTH_SHORT).show();
			}
			else
				{
				dialog();
				}
		}

		

	}

	class UploadThread implements Runnable {

		public void run() {
			
				try {
					// connect the server
					Socket socket = new Socket("52.91.35.111", 1503);
					HandleToServer uploader = new HandleToServer(socket);
					
					// Combine English with Chinese explanation
					exp = english + ", " + exp;
					
					// Uploading
					uploader.uploadExp(word, user, exp);

					// get feedback from server
					InputStreamReader input = new InputStreamReader(socket.getInputStream(), "utf8");
					BufferedReader bufIn = new BufferedReader(input);
					String feedback = bufIn.readLine();

					// give feedback to user
					Message msg = msgHandler.obtainMessage();// use message to transmit information between new thread and main thread
					if (feedback.equalsIgnoreCase("OK")) {
						msg.what = 1;
						socket.close();
						// finish, turn back to word searching activity
						finish();
					} else {
						msg.what = 0;
						socket.close();
					}
					msgHandler.sendMessage(msg);

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					Message msg = msgHandler.obtainMessage();
					msg.what = 0;
					msgHandler.sendMessage(msg);
				}		
		}
		

	}

	private  void uploadConfirmation(String word, String user, String english, String exp){
		
		// TODO Auto-generated method stub
	
		 AlertDialog.Builder builder = new Builder(NewWord.this);
		  builder.setMessage("英文全称:" + english + "\n" +
				  			"中文解释:" + exp + "\n" +
				  			"用户名:" + user );
		  
		  builder.setTitle("上传词条" + word);

		  builder.setNegativeButton("取消", new OnClickListener() {
			   public void onClick(DialogInterface dialog, int which) {
			    dialog.dismiss();
			   }
			  });
		  
		  builder.setPositiveButton("上传", new OnClickListener() {
		   public void onClick(DialogInterface dialog, int which) {
				// start an upload thread
				UploadThread uploadThread = new UploadThread();
				Thread thread = new Thread(uploadThread);
				thread.start();
		   }
		  });

		  builder.create().show();
	
	}
	
	private  void dialog(){
		
		// TODO Auto-generated method stub
	
		 AlertDialog.Builder builder = new Builder(NewWord.this);
		  builder.setMessage("没有WiFi连接");
		  
		  builder.setTitle("提示");

		  builder.setPositiveButton("网络设置", new OnClickListener() {

		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
		   }
		  });

		  builder.setNegativeButton("取消", new OnClickListener() {

		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		   }
		  });

		  builder.create().show();
	
	}
	
	private final Handler msgHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case 1:
				Toast.makeText(NewWord.this, "Thanks!", Toast.LENGTH_SHORT).show();
				break;
			default:
				Toast.makeText(NewWord.this,
						"Sorry, upload fail. Please try again",
						Toast.LENGTH_SHORT).show();
				break;
			}
		}
	};
	
	  public boolean isConnectingToWIFI(Context _context){
	        //wifi manager
		  try{
	        WifiManager wifi = (WifiManager) _context.getSystemService(Context.WIFI_SERVICE);    
	        	//if there is a wifi, then return true;
	       
	          if (wifi.isWifiEnabled())
	          {
	        	 
	              return true;
	          }
	         return false;
		  }catch(Exception e){
			  Toast.makeText(NewWord.this, e.toString(), Toast.LENGTH_SHORT).show();
		  }
		  return false;
	    }
	 
	//whether open the NETWORK setting or cancel ~
		
}
