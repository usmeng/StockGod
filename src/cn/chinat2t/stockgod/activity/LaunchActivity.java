package cn.chinat2t.stockgod.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.chinat2t.stockgod.MainActivity;
import cn.chinat2t.stockgod.R;
import cn.chinat2t.stockgod.StockApplication;
import cn.chinat2t.stockgod.bean.UserBean;
import cn.chinat2t.stockgod.http.CommunicationManager;
import cn.chinat2t.stockgod.http.CommunicationResultListener;
import cn.chinat2t.stockgod.utils.HttpUtil;
import cn.chinat2t.stockgod.utils.PreferenceManager;
import cn.chinat2t.stockgod.utils.ViewUtil;

public class LaunchActivity extends Activity implements OnClickListener{
	
	public static final int DIALOG_SETTING = 0;
	public static final String FINSIH_ACTIVITY = "FINSIH_ACTIVITY";
	private Button login;
	private Button reg;
	private Button quickLogin;
	private ProgressDialog showDialog;
	private Dialog dialog;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.lauch_layout);
		
		login = (Button) findViewById(R.id.lauch_button2);
		reg = (Button) findViewById(R.id.lauch_button3);
		quickLogin = (Button) findViewById(R.id.lauch_button1);
		
		login.setOnClickListener(this);
		reg.setOnClickListener(this);
		quickLogin.setOnClickListener(this);
		

		IntentFilter filter = new IntentFilter(FINSIH_ACTIVITY);
		registerReceiver(broadcastReceiver, filter);
	}
	
	public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if(FINSIH_ACTIVITY.equals(intent.getAction())){
				finish();
			}
		}
	};
	
	@Override
	protected void onResume() {
		super.onResume();
		if(HttpUtil.isConnection(this)){
			showDialog = (ProgressDialog) ViewUtil.showLoading(this);
			showDialog.setCancelable(false);
			mHandler.postDelayed(new Runnable() {
					
					@Override
					public void run() {
						mHandler.post(runnable);
					}
				}, 2000);
		}else{
			reg.setVisibility(View.INVISIBLE);
			login.setVisibility(View.INVISIBLE);
			quickLogin.setVisibility(View.INVISIBLE);
			showDialog(DIALOG_SETTING);
		}
	}
	
	@Override
    protected Dialog onCreateDialog(int id) {
		switch (id) {
        case DIALOG_SETTING:
        	dialog = null;
   			View view = LayoutInflater.from(this).inflate(R.layout.guess_dialog_munu, null);
   			TextView tx1 = (TextView) view.findViewById(R.id.textView2);
   			TextView tx2 = (TextView) view.findViewById(R.id.textView3);
   			Button button = (Button) view.findViewById(R.id.guess_confirm);
   			tx1.setText("网络连接不可用，请确认打开网络后再次启动！");
   			tx2.setVisibility(View.GONE);
   			button.setText("设置网络");
   			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent wifiSettingsIntent = new Intent("android.settings.WIFI_SETTINGS"); 
					startActivity(wifiSettingsIntent); 
					dialog.cancel();
				}
			});
   			
            dialog = new Dialog(this, R.style.selectorDialog);
            dialog.setContentView(view);
            dialog.show();
            return dialog;
        }
        return null;
        
    }

	private Handler mHandler = new Handler();
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			PreferenceManager instance = PreferenceManager.getInstance(LaunchActivity.this);
			if(instance.isLoginAuto()){
				CommunicationManager.getInstance().login(instance.getAccount(), instance.getPass(),
						new CommunicationResultListener() {
							@Override
							public void resultListener(byte result,
									Object resultData) {
								super.resultListener(result, resultData);
								if(result == CommunicationManager.SUCCEED && token > 0){
									Message message = handler.obtainMessage();
									message.arg1 = 1;
									message.obj = resultData;
									handler.sendMessage(message);
								}else{
									ViewUtil.dismiss(showDialog);
//									ViewUtil.showToast((String)resultData, LaunchActivity.this);
								}
							}
						});
			}else{
				reg.setVisibility(View.VISIBLE);
				login.setVisibility(View.VISIBLE);
				quickLogin.setVisibility(View.VISIBLE);
				ViewUtil.dismiss(showDialog);
			}
		}
	};

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.lauch_button3:
			intent.setClass(this, RegActivity.class);
			startActivity(intent);
			break;

		case R.id.lauch_button2:
			intent.setClass(this, LoginActivity.class);
			startActivity(intent);
			break;
			
		case R.id.lauch_button1:
			showDialog = (ProgressDialog) ViewUtil.showLoading(this);
			showDialog.setCancelable(false);
			CommunicationManager.getInstance().fastLogin("123456", "123456", fastLoginListener);
			break;
		}
		
	}
	
	private CommunicationResultListener fastLoginListener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED && token > 0){
				Message message = handler.obtainMessage();
				message.arg1 = 0;
				message.obj = resultData;
				handler.sendMessage(message);
			}else {
				ViewUtil.dismiss(showDialog);
//				ViewUtil.showToast((String)resultData, LaunchActivity.this);
			}
		};
	};
	
	Handler handler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			UserBean user = null;
			switch (msg.arg1) {
			case 0:
			case 1:
				user = (UserBean) msg.obj;
				if(user.responsecode == 1){
					UserBean.setUser(user);
					Intent intent = new Intent(LaunchActivity.this, MainActivity.class);
					intent.putExtra(UserBean.USER_OBJECT, user);
					startActivity(intent);
					finish();
				}else{

					View view = LayoutInflater.from(LaunchActivity.this).inflate(R.layout.guess_dialog_munu, null);
					TextView tx1 = (TextView) view.findViewById(R.id.textView2);
					TextView tx2 = (TextView) view.findViewById(R.id.textView3);
					Button button = (Button) view.findViewById(R.id.guess_confirm);
					Button chongzhi = (Button) view.findViewById(R.id.guess_chongzhi);
					tx2.setVisibility(View.GONE);
					final Dialog showDialog = ViewUtil.showDialog(LaunchActivity.this, view);
					chongzhi.setVisibility(View.GONE);
					button.setText("确定");
					tx1.setText("登陆出错，请重新登陆");
					tx2.setVisibility(View.GONE);
					button.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							reg.setVisibility(View.VISIBLE);
							login.setVisibility(View.VISIBLE);
							quickLogin.setVisibility(View.VISIBLE);
							PreferenceManager.getInstance(LaunchActivity.this).putLoginAuto(false);
							PreferenceManager.getInstance(LaunchActivity.this).putPass("");
							showDialog.cancel();
						}
					});
				
				}
				ViewUtil.dismiss(showDialog);
				break;
			}
		};
	};

	@Override
	protected void onStop() {
		super.onStop();
		StockApplication.cancelToast();
	};
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		unregisterReceiver(broadcastReceiver);
	}
}
