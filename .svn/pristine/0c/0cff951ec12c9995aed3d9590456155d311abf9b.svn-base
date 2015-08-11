package cn.chinat2t.stockgod.utils;

import android.app.Activity;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;


public class HttpUtil {
	
	
	public static boolean isConnection(Context context){
		ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Activity.CONNECTIVITY_SERVICE);
		NetworkInfo networkInfo = manager.getActiveNetworkInfo();
		if(networkInfo != null){
			return networkInfo.isAvailable();
		}
		return false;
	}
}
