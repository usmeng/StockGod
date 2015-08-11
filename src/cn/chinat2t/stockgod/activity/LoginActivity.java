package cn.chinat2t.stockgod.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import cn.chinat2t.stockgod.MainActivity;
import cn.chinat2t.stockgod.R;
import cn.chinat2t.stockgod.StockApplication;
import cn.chinat2t.stockgod.bean.UserBean;
import cn.chinat2t.stockgod.http.CommunicationManager;
import cn.chinat2t.stockgod.http.CommunicationResultListener;
import cn.chinat2t.stockgod.utils.PreferenceManager;
import cn.chinat2t.stockgod.utils.StringUtil;
import cn.chinat2t.stockgod.utils.ViewUtil;

public class LoginActivity extends Activity implements OnClickListener{
	
	private EditText editName;
	private EditText editPass;
	private Button btnLogin;
	private Button btnForgot;
	private CheckBox checkAuto;
	private CheckBox checkRember;
	private PreferenceManager preference;
	private ProgressDialog dialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login_layout);
		
		preference = PreferenceManager.getInstance(this);
		initView();
	}

	private void initView() {
		editName = (EditText) findViewById(R.id.login_editText_name);
		editPass = (EditText) findViewById(R.id.login_editText_pass);
		checkRember = (CheckBox) findViewById(R.id.login_checkBox_rember);
		checkAuto = (CheckBox) findViewById(R.id.login_checkBox_auto_login);
		
		btnLogin = (Button) findViewById(R.id.login_button_login);
		btnForgot = (Button) findViewById(R.id.login_button_forgot);
		
		btnLogin.setOnClickListener(this);
		btnForgot.setOnClickListener(this);
		checkRember.setOnClickListener(this);
		checkAuto.setOnClickListener(this);
		
		checkRember.setChecked(preference.isRemember());
		checkAuto.setChecked(preference.isLoginAuto());
		
		if(checkRember.isChecked()){
			editName.setText(preference.getAccount());
			editPass.setText(preference.getPass());
		}
		if(checkRember.isChecked() && checkAuto.isChecked()){
			dialog = (ProgressDialog) ViewUtil.showLoading(this);
			dialog.setCancelable(false);
			CommunicationManager.getInstance().login(preference.getAccount(), preference.getPass(), loginListener);
		}
	}

	@Override
	public void onClick(View v) {
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.login_button_login:
			String userNameValue = editName.getText().toString();
			if(!StringUtil.isNegUserName(userNameValue)){
				ViewUtil.showToast("ÓÃ»§Ãû´íÎó~", this);
				return;
			}
			String nameValue = editPass.getText().toString();
			if(!StringUtil.isNegPass(nameValue)){
				ViewUtil.showToast("ÃÜÂë´íÎó~", this);
				return;
			}
			dialog = (ProgressDialog) ViewUtil.showLoading(this);
			dialog.setCancelable(false);
			CommunicationManager.getInstance().login(
					editName.getText().toString(), 
					editPass.getText().toString(), 
					loginListener);
			break;
		case R.id.login_button_forgot:
			intent.setClass(this, ForgetActivity.class);
			startActivity(intent);
			break;
		case R.id.login_checkBox_rember:
			CheckBox check = (CheckBox) v;
			preference.putRemember(check.isChecked());
			break;
		case R.id.login_checkBox_auto_login:
			CheckBox check2 = (CheckBox) v;
			preference.putLoginAuto(check2.isChecked());
			break;
		}
	}
	
	private CommunicationResultListener loginListener = new CommunicationResultListener() {
		
		public void resultListener(byte result, final Object resultData) {
			Message message = mHandler.obtainMessage();
			message.obj = resultData;
			if(result == CommunicationManager.SUCCEED && token > 0){
				message.arg1 = 1;
			}else {
				message.arg1 = 0;
			}
			mHandler.sendMessage(message);
		};
	};
	
	Handler mHandler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case 1:
				UserBean user = (UserBean) msg.obj;
				UserBean.setUser(user);
				if(user.responsecode == 1){
					startActivity(new Intent(LoginActivity.this, MainActivity.class));
					if(checkRember.isChecked()){
						preference.putAccount(editName.getText().toString());
						preference.putPass(editPass.getText().toString());
					}
					finish();
					sendBroadcast(new Intent(LaunchActivity.FINSIH_ACTIVITY));
				}else{
					ViewUtil.showToast(user.msg, LoginActivity.this);
				}
				break;
			case 0:
				ViewUtil.showToast((String)msg.obj, LoginActivity.this);
				break;
			}
			ViewUtil.dismiss(dialog);
		};
	};
	
	@Override
	protected void onStop() {
		super.onStop();
		StockApplication.cancelToast();
	};
}
