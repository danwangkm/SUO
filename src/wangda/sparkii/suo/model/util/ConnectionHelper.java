package wangda.sparkii.suo.model.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.provider.Settings;

public class ConnectionHelper {
	 public static boolean isConnectingToInternet(Context _context){
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
	  }
	 
	 public static void dialog(final Context context){
			// TODO Auto-generated method stub
		
			 AlertDialog.Builder builder = new Builder(context);
			  builder.setMessage("没有网络连接,无法更新词库，您是否希望先连接wifi并重新进入缩缩缩");
			  
			  builder.setTitle("提示");

			  builder.setPositiveButton("网络设置", new OnClickListener() {

			   public void onClick(DialogInterface dialog, int which) {
			    dialog.dismiss();
			    context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
			    ((Activity) context).finish();//这里处理的不是很好，用户设置完wifi之后程序需要重启，目前这是解决设置完wifi之后autotextview无法使用的方法
			   }
			  });

			  builder.setNegativeButton("暂时不用", new OnClickListener() {

			   public void onClick(DialogInterface dialog, int which) {
			    dialog.dismiss();
			   }
			  });

			  builder.create().show();
		
		}
}
