package wangda.sparkii.suo.view.login;

import org.json.JSONException;

import wangda.sparkii.suo.activities.Home;
import wangda.sparkii.suo.model.LoginService;
import wangda.sparkii.suo.model.util.ToastHelper;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import wangda.sparkii.suo.R;

public class Register extends Activity {
	private Button bt_Register = null;
	private Button bt_Back = null;
	private EditText edit_username = null;
	private EditText edit_email = null;
	private EditText edit_password = null;
	private EditText edit_password_confirm = null;
	private String username = null;
	private String password = null;
	private String passwordConfirm = null;
	private String email = null;
	
	private Intent service = null;
	private LoginService loginService = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.register);
		
		edit_username = (EditText) findViewById(R.id.editText_username);
		edit_email = (EditText) findViewById(R.id.editText_email);
		edit_password = (EditText) findViewById(R.id.editText_password);
		edit_password_confirm = (EditText) findViewById(R.id.editText_password_confirm);
		bt_Register = (Button) findViewById(R.id.register);
		bt_Back = (Button) findViewById(R.id.back);
		bt_Back.setOnClickListener(new bt_BackListener());
		bt_Register.setOnClickListener(new bt_RegisterListener());
		
		loginService = new LoginService();
	}
	
	class bt_RegisterListener implements android.view.View.OnClickListener {

		public void onClick(View v) {
			// TODO Auto-generated method stub
			username = edit_username.getText().toString();
			email = edit_email.getText().toString();
			password = edit_password.getText().toString();
			passwordConfirm = edit_password_confirm.getText().toString();
						
			if(username!=null && password!=null && email!=null){
				if(username.equals(""))
					ToastHelper.showToast(Register.this, "Please Input the Username");
				else if(password.equals(""))
					ToastHelper.showToast(Register.this, "Please Input the Password");
				else if(email.equals(""))
					ToastHelper.showToast(Register.this, "Please Input the Email");
				else if (!password.equals(passwordConfirm))
					ToastHelper.showToast(Register.this, "Your Password Should Be the Same as Your Confirmed Password");
				else {
					service = new Intent();
					service.setClass(Register.this, LoginService.class);
					startService(service);
					try {
						loginService.register(Register.this, username, password, email, "register");
					} catch (JSONException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		}
		
	}
	
	public void registerSuccessfully(){
		
		ToastHelper.showToast(Register.this, "Register Sucessfully");
		loginService.saveUsernameAndPassword(username,password, Register.this);
		Intent intent=new Intent();
		intent.putExtra("username", username);
		
		intent.setClass(Register.this, Home.class);
		Register.this.startActivity(intent);
		finish();
	}
	
	public void registerFail(){		
		ToastHelper.showToast(Register.this, "This Username Has Been Used, Please Change");
	}

	class bt_BackListener implements android.view.View.OnClickListener {
		public void onClick(View v) {
			// TODO Auto-generated method stub
			Intent intent=new Intent();
			intent.setClass(Register.this, Login.class);
			Register.this.startActivity(intent);
			finish();
		}
	}
}