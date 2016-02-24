package wangda.sparkii.suo.view.login;

import java.util.Map;

import org.json.JSONException;

import wangda.sparkii.suo.activities.Home;
import wangda.sparkii.suo.model.LoginService;
import wangda.sparkii.suo.model.util.ConnectionHelper;
import wangda.sparkii.suo.model.util.ToastHelper;

import wangda.sparkii.suo.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class Login extends Activity {
	private Button bt_Login = null;
	private Button bt_Register = null;
	private EditText edit_usernameOrEmail = null;
	private EditText edit_password = null;
	private String usernameOrEmail = null;
	private String password = null;
	
	private Intent service = null;
	private LoginService loginService = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
		
		edit_usernameOrEmail = (EditText) findViewById(R.id.editText_username);
		edit_password = (EditText) findViewById(R.id.editText_password);
		bt_Login = (Button) findViewById(R.id.login);
		bt_Register = (Button) findViewById(R.id.register);
		bt_Login.setOnClickListener(new bt_LoginListener());
		bt_Register.setOnClickListener(new bt_RegisterListener());
		
		loginService = new LoginService();
		
		service = new Intent();
		service.setClass(Login.this, LoginService.class);
		startService(service);
		
		Map<String, String> map = loginService.readFile(Login.this);  
		  if (map != null) {  
			  edit_usernameOrEmail.setText(map.get("user"));  
			  edit_password.setText(map.get("password"));  
		} 
		
		try{
		    
	        ToastHelper.showToast(Login.this, "Checking Network");
	        boolean isInternetPresent = loginService.isConnectingToInternet(getApplicationContext());

	        //boolean isInternetPresent =true; 
	        if(!isInternetPresent){
	        	ToastHelper.showToast(Login.this, "No Network Connection");
	        	ConnectionHelper.dialog(Login.this);
	        }
	        else
	        	ToastHelper.showToast(Login.this, "Internet Works");
	        }catch(Exception e){
	        	// TODO Auto-generated catch block
				e.printStackTrace();
	        }
	}
	
	class bt_LoginListener implements android.view.View.OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			
			usernameOrEmail = edit_usernameOrEmail.getText().toString();
			password = edit_password.getText().toString();
			
			if(usernameOrEmail!=null && password!=null){
				if(usernameOrEmail.equals(""))
					ToastHelper.showToast(Login.this, "Please Input the Username");
				else if(password.equals(""))
					ToastHelper.showToast(Login.this, "Please Input the Password");
				else {
					service = new Intent();
					service.setClass(Login.this, LoginService.class);
					startService(service);
					try {
						loginService.login(Login.this, usernameOrEmail, password, "login");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
	public void loginSuccessfully(){
		
		ToastHelper.showToast(Login.this, "Login Sucessful");
		loginService.saveUsernameAndPassword(usernameOrEmail,password, Login.this);
		Intent intent=new Intent();
		intent.putExtra("username", usernameOrEmail);

		intent.setClass(Login.this, Home.class);
		Login.this.startActivity(intent);
		finish();
	}
	
	public void loginFail(){		
		ToastHelper.showToast(Login.this, "Wrong User Information or Password");
	}
	
	class bt_RegisterListener implements android.view.View.OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(Login.this, Register.class);
			Login.this.startActivity(intent);
			finish();
		}
	}
}
