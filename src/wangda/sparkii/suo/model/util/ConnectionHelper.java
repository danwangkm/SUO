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
			  builder.setMessage("û����������,�޷����´ʿ⣬���Ƿ�ϣ��������wifi�����½���������");
			  
			  builder.setTitle("��ʾ");

			  builder.setPositiveButton("��������", new OnClickListener() {

			   public void onClick(DialogInterface dialog, int which) {
			    dialog.dismiss();
			    context.startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));
			    ((Activity) context).finish();//���ﴦ��Ĳ��Ǻܺã��û�������wifi֮�������Ҫ������Ŀǰ���ǽ��������wifi֮��autotextview�޷�ʹ�õķ���
			   }
			  });

			  builder.setNegativeButton("��ʱ����", new OnClickListener() {

			   public void onClick(DialogInterface dialog, int which) {
			    dialog.dismiss();
			   }
			  });

			  builder.create().show();
		
		}
}
