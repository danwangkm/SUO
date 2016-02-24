package wangda.sparkii.suo.model;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;


import wangda.sparkii.suo.model.util.ConnectionHelper;
import wangda.sparkii.suo.model.util.ToastHelper;
import wangda.sparkii.suo.upload.HandleToServer;
import wangda.sparkii.suo.view.login.Login;
import wangda.sparkii.suo.view.login.Register;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

public class LoginService extends Service{
	private int server_port = 1503;
	private String server_ip = "52.91.35.111";
	private InputStreamReader input = null;
	private BufferedReader bufIn = null;
	
	//private	Activity currentActivity;
	private Login login = null;
	private Register register = null;
	
	private LoginThread loginThread;
	
	@Override
	public IBinder onBind(Intent arg0) {
		// TODO Auto-generated method stub
		return null;
	}
	
	// Create a service
		@Override
		public void onCreate() {
			// TODO Auto-generated method stub
			super.onCreate();
			System.out.println("LoginService: New download Service onCreate");
		}

		// Service activity
		@Override
		public int onStartCommand(Intent intent, int flags, int startId) {
			// TODO Auto-generated method stub
			System.out.println("LoginService: Download Service onStartCommand");
			
			
			this.stopService(intent);
			
			return START_NOT_STICKY;
		}
		
		// Destroy a service
		@Override
		public void onDestroy() {
			// TODO Auto-generated method stub
			System.out.println("LoginService: Download Service onDestory");
			super.onDestroy();
		}
		
		public void login(Activity login, String username, String password, String flag) throws JSONException {
			startLoginThread(login);
			if (loginThread != null){
				loginThread.login(username, password, flag);
			}else
				Log.i("LoginThread", "Login");
		}
		
		public void register(Activity register, String username, String password, String email, String flag) throws JSONException {
			startLoginThread(register);
			if (loginThread != null){
				loginThread.register(username, password, email, flag);
			}else
				Log.i("LoginThread", "Register");
		}
		
		public boolean isConnectingToInternet(Context _context){      
		    return  ConnectionHelper.isConnectingToInternet(_context);  
		}
		 
		private void startLoginThread(Activity currentActivity){
			
			if(currentActivity instanceof Login) 
				this.login = (Login) currentActivity;
			else if (currentActivity instanceof Register) 
				this.register = (Register) currentActivity;
			else
				return;
			
			// start an login thread
			loginThread = new LoginThread();
			Thread thread = new Thread(loginThread);
			thread.start();
		}
		
		
		class LoginThread extends Thread{
			
			protected void login(String usernameOrEmail, String password, String flag) throws JSONException {
				
				//construct json and sent it to Server
				//JSONObject json=new JSONObject();
				//JSONArray jsonMembers = new JSONArray();
				JSONObject user = new JSONObject();
				user.put("flag", "login");
				user.put("usernameOrEmail", usernameOrEmail);
				user.put("password", password);
				//user.put("email","10371443@qq.com");
				//jsonMembers.put(member1);
				//json.put("users", jsonMembers);
				this.run(user.toString());	
			}
			
			protected void register(String username, String password, String email, String flag) throws JSONException {

				JSONObject user = new JSONObject();
				user.put("flag", "register");
				user.put("username", username);
				user.put("password", password);
				user.put("email",email);
				this.run(user.toString());	
			}
			
			public void run(String jsonString) {
				super.run();

				try {
					// connect the server
					Socket socket = new Socket(server_ip, server_port);
					
					HandleToServer uploader = new HandleToServer(socket);

					uploader.uploadLogin(jsonString);

					// get feedback from server
					input = new InputStreamReader(socket.getInputStream(), "utf8");
					bufIn = new BufferedReader(input);
					String feedback = bufIn.readLine();
					
					System.out.println("LoginService: feedback from server "+feedback);
					// give feedback to user
					if (feedback.equalsIgnoreCase("OK")) {
						if (login!=null)
							login.loginSuccessfully();
						if (register!=null)
							register.registerSuccessfully();
					} else {
						if (login!=null)
							login.loginFail();
						if (register!=null)
							register.registerFail();
					}
					socket.close();

				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
					ToastHelper.showToast(LoginService.this, "Sorry, login fail. Please try again");
				}	
		    }			
		}
		
		/**
		 * use to store user name and password of login and register for convinience
		 * @param username
		 * @param password
		 * @param context
		 * @return
		 */
		public boolean saveUsernameAndPassword(String username, String password, Context context){
			 //上下文对象的api    
			   try {  
			    //通过 openFileOutput()方法获取一个文件 的输出流对象  
			    FileOutputStream fos = context.openFileOutput("privacy.txt", Context.MODE_PRIVATE);  
			 
			    //拼接用户名与密码  
			    String result = username + ":" +password; 	   
			    //写入  
			    fos.write(result.getBytes());  
			    fos.flush();  
			    fos.close();  	     
			   } catch (Exception e) {  
		      
			   e.printStackTrace();  
			return false;  
			}  
			return true;  
		}
		
		/**
		 * use to reload user name and password for convinience
		 * @param context
		 * @return
		 */
		public Map<String, String> readFile(Context context){  
		    
			  Map<String ,String> map = null;  
			    
			  try {  
			   FileInputStream fis = context.openFileInput("privacy.txt");  
			   String value = getValue(fis);  
			   String values[] = value.split(":");  
			     
			   if(values.length >0){  
			    map = new HashMap<String, String>();  
			    map.put("user", values[0]);  
			    map.put("password", values[1]);  
			   }  
			    
			  } catch (Exception e) {  
			     
			   e.printStackTrace();  
			  }  
			    
			  return map;  
		}
		
		public static String getValue(FileInputStream fis)throws Exception{  
			  //字节 流输出流对象  
			  ByteArrayOutputStream stream = new ByteArrayOutputStream();  
			  byte[] buffer = new byte[1024];  
			  int length = -1;  
			  while((length = fis.read(buffer)) != -1){  
			   stream.write(buffer, 0, length);  
			  }  
			  stream.flush();  
			  stream.close();  
			  String value = stream.toString();  
			    
			  return value;  
			 }
}
