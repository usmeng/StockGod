package cn.chinat2t.stockgod.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import cn.chinat2t.stockgod.R;
import cn.chinat2t.stockgod.StockApplication;
import cn.chinat2t.stockgod.bean.UserBean;
import cn.chinat2t.stockgod.http.CommunicationManager;
import cn.chinat2t.stockgod.http.CommunicationResultListener;
import cn.chinat2t.stockgod.utils.StringUtil;
import cn.chinat2t.stockgod.utils.ViewUtil;

public class ForgetActivity extends Activity implements OnClickListener{

	private EditText name;
	private EditText email;
	private Dialog showLoading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.forget_layout);
		
		initView();
	}

	private void initView() {
		findViewById(R.id.found_pass_button1).setOnClickListener(this);
		name = (EditText) findViewById(R.id.found_pass_editText1);
		email = (EditText) findViewById(R.id.found_pass_editText2);
	}

	private CommunicationResultListener loginListener = new CommunicationResultListener() {
	
		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED && token > 0){
				Message message = mHandler.obtainMessage();
				message.arg1 = 0;
				message.obj = resultData;
				mHandler.sendMessage(message);
			}else{
				ViewUtil.dismiss(showLoading);
			}
		};
	};
	
	private Dialog dialog;
	Handler mHandler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case 0:
				ViewUtil.dismiss(showLoading);
				final UserBean user = (UserBean) msg.obj;
				View view = LayoutInflater.from(ForgetActivity.this).inflate(R.layout.guess_dialog_munu, null);
	   			TextView tx1 = (TextView) view.findViewById(R.id.textView1);
	   			TextView tx2 = (TextView) view.findViewById(R.id.textView2);
	   			tx2.setVisibility(View.GONE);
	   			Button button = (Button) view.findViewById(R.id.guess_confirm);
	   			button.setOnClickListener(new OnClickListener() {
	   					
	   				@Override
	   				public void onClick(View v) {
	   					dialog.cancel();
	   					if(user.responsecode == 10){
	   						finish();
	   					}
	   				}
	   			});
	   			button.setText("确定");
	   			tx1.setText(user.msg);
	   			
	            dialog = new Dialog(ForgetActivity.this, R.style.selectorDialog);
	            dialog.setContentView(view);
	            dialog.show();
				break;
			}
		};
	};
	
	@Override
	public void onClick(View v) {
		String nameValue = name.getText().toString();
		String emailValue = email.getText().toString();
		if(StringUtil.isNegUserName(nameValue) && StringUtil.isNegEmail(emailValue)){
			showLoading = ViewUtil.showLoading(this);
			CommunicationManager.getInstance().foundPass(emailValue, nameValue, loginListener);
		}else{
			ViewUtil.showToast("用户名或邮箱错误！", this);
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		StockApplication.cancelToast();
	};
}
