package wangda.sparkii.suo.model.util;

import android.content.Context;
import android.widget.Toast;

public class ToastHelper {

	public static void showToast(Context context, String string){
		Toast.makeText(context, string, Toast.LENGTH_SHORT).show();
	}
	
}
