package cn.chinat2t.stockgod;

import android.app.Application;
import android.app.Notification;
import android.content.Context;
import android.widget.Toast;

public class StockApplication extends Application {
	private static Notification mNotification;
	private static Context mContext;
	public static Toast mToast;

	public static Context getCustomedApplicationContext() {
		return mContext;
	}

	public static void setmNotification(Notification mNotification) {
		StockApplication.mNotification = mNotification;
	}

	public static Notification getmNotification() {
		return mNotification;
	}

	public void onCreate() {
		mContext = getApplicationContext();
		super.onCreate();
	}

	static int times = 0;
	static int times1 = 0;
	public static Toast showToast(Context ctx, String text) {
		if (mToast == null) {
			System.out.println("showToast create times: " + times++);
			mToast = Toast.makeText(ctx, text, Toast.LENGTH_SHORT);
		} else {
			System.out.println("showToast show times: " + times1++);
			mToast.setText(text);
			mToast.setDuration(Toast.LENGTH_SHORT);
		}
		mToast.show();
		return mToast;
	}

	public static void cancelToast() {
		if (mToast != null) {
			mToast.cancel();
		}
	}

	public static void onBackPressed() {
		cancelToast();
	}

}
