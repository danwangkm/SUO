package wangda.sparkii.suo.activities;

import wangda.sparkii.suo.R;

import wangda.sparkii.suo.db.DictionaryAdapter;
import wangda.sparkii.suo.db.DictionaryDatabase;
import wangda.sparkii.suo.view.login.Login;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class Home extends Activity{
	private TextView welcomeMsg = null;
	private Button myButton = null;
    private AutoCompleteTextView myWord = null;
    
    private String username;
    
    public final static String DATABASE_PATH = android.os.Environment
            .getExternalStorageDirectory().getAbsolutePath()
            + "/dictionary";
    public final static String DATABASE_FILENAME = "dictionary.db";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        
        Intent intent = getIntent();
		username = intent.getStringExtra("username");
        
        //initiate the layout items
        myWord = (AutoCompleteTextView)findViewById(R.id.autoCompleteTextView1);
        myWord.setThreshold(1);
        myButton = (Button)findViewById(R.id.button1);
        myButton.setOnClickListener(new MyButtonListener());
        welcomeMsg = (TextView)findViewById(R.id.welcome_msg);
        if (username == null)	username = "";
        welcomeMsg.setText(welcomeMsg.getText()+username); 
        
      //create the dictionary
        DictionaryDatabase dbHelper = new DictionaryDatabase(Home.this, "dictionary");
		final SQLiteDatabase db = dbHelper.openDatabase(Home.this);
		
		myWord.addTextChangedListener(new TextWatcher(){ 
			public void afterTextChanged(Editable s)
		    {
		    	Cursor cursor_autoText = db.rawQuery("select distinct word as _id from dictionary where word like ? limit 6",new String[]{ s.toString() + "%" });
				DictionaryAdapter dictionaryAdapter = new DictionaryAdapter(Home.this, cursor_autoText, true);
				myWord.setAdapter(dictionaryAdapter);
		    }

			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
				// TODO Auto-generated method stub
				
			}

			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
				// TODO Auto-generated method stub
				
			} 
		}); 

        /*
        //Create a service to download XML
        System.out.println("create a new service---from Home");
        Intent intent = new Intent();
		intent.setClass(Home.this, DownLoadService.class);
		startService(intent);
		*/
		
		//Check the network link
        try{
        //Toast.makeText(Home.this, "Checking", Toast.LENGTH_SHORT).show();
        
        boolean isInternetPresent = isConnectingToInternet(getApplicationContext());

        //boolean isInternetPresent =true; 
        isInternetPresent = true;
        if(isInternetPresent){
        	//Create a service to download XML
            System.out.println("Home: create a new downloard service");
        	intent = new Intent();
        	intent.setClass(Home.this, DownLoadService.class);
        	startService(intent);
        }
        else{
        	//Toast.makeText(Home.this, "There is no Network", Toast.LENGTH_SHORT).show();
        	dialog();
        	
        }
        }catch(Exception e){
        	Toast.makeText(Home.this, "Checking Error! Please restart the app", Toast.LENGTH_SHORT).show(); 
        }
        
    }
    private  void dialog(){
		
		// TODO Auto-generated method stub
	
		 AlertDialog.Builder builder = new Builder(Home.this);
		  builder.setMessage("没有WiFi连接,无法更新词库，您是否希望先连接wifi");
		  
		  builder.setTitle("提示");

		  builder.setPositiveButton("网络设置", new OnClickListener() {

		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		    startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
		    finish();//这里处理的不是很好，用户设置完wifi之后程序需要重启，目前这是解决设置完wifi之后autotextview无法使用的方法
		   }
		  });

		  builder.setNegativeButton("暂时不用", new OnClickListener() {

		   public void onClick(DialogInterface dialog, int which) {
		    dialog.dismiss();
		   }
		  });

		  builder.create().show();
	
	}
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_home, menu);
        return true;
    }
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.login:
            	Intent intent = new Intent();
    			intent.setClass(this, Login.class);
    			this.startActivity(intent);
    			finish();
            	break;
            case R.id.exit:
                finish();
                break;
        }
        return true;
    }
    
    /*public boolean isConnectingToInternet(Context _context){
        ConnectivityManager connectivity = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
          if (connectivity != null)
          {
              NetworkInfo[] info = connectivity.getAllNetworkInfo();
              if (info != null){
                  for (int i = 0; i < info.length; i++)
                      if (info[i].getState() == NetworkInfo.State.CONNECTED)
                      {
                         return true;
                      }
              }
          }
         return false;
    }*/
    
    public boolean isConnectingToInternet(Context _context){
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
		  Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();
	  }
	  return false;
    }
    
    class MyButtonListener implements android.view.View.OnClickListener{
    	
		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			//search word
			/*
			if(isConnectingToInternet(getApplicationContext())){
				//Create a service to download XML
		        System.out.println("create a new service---from Home");
				Intent intent = new Intent();
	        	intent.setClass(Home.this, DownLoadService.class);
	        	startService(intent);
			}
			*/
			Intent intent=new Intent();
			intent.putExtra("word", myWord.getText().toString());
			intent.putExtra("username", username);
			intent.setClass(Home.this, WordList.class);
			Home.this.startActivity(intent);
			finish();
		}
    	
    	
    }
    
}
