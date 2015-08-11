package cn.chinat2t.stockgod.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.chinat2t.stockgod.R;
import cn.chinat2t.stockgod.StockApplication;
import cn.chinat2t.stockgod.bean.UserBean;
import cn.chinat2t.stockgod.http.CommunicationManager;
import cn.chinat2t.stockgod.http.CommunicationResultListener;
import cn.chinat2t.stockgod.utils.PreferenceManager;
import cn.chinat2t.stockgod.utils.StringUtil;
import cn.chinat2t.stockgod.utils.ViewUtil;

public class RegActivity extends Activity implements OnCheckedChangeListener, OnClickListener{
	
	private RadioGroup sexRadioGroup;
	private EditText userName;
	private EditText pass;
	private EditText passCon;
	private EditText email;
	private String userNameValue;
	private String passValue;
	private String passValue2;
	private String emailValue;
	private int sex = 1;
	private boolean isOK_1;
	private boolean isOK_2;
	private boolean isOK_3;
	private boolean isOK_4;
	private Button regButton;
	private TextView toastText1;
	private TextView toastText2;
	private TextView toastText3;
	private TextView toastText4;
	private ProgressDialog showLoading;
	private ImageView touXiang;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.reg_layout);
		
		sexRadioGroup = (RadioGroup) findViewById(R.id.reg_sex_radiogroup);
		touXiang = (ImageView) findViewById(R.id.reg_imageView1);
		sexRadioGroup.setOnCheckedChangeListener(this);
		
		userName = (EditText) findViewById(R.id.reg_editText_username);
		pass = (EditText) findViewById(R.id.reg_editText_pass);
		passCon = (EditText) findViewById(R.id.reg_editText_pass_confirm);
		email = (EditText) findViewById(R.id.reg_editText_email);
		
		toastText1 = (TextView) findViewById(R.id.textView1);
		toastText2 = (TextView) findViewById(R.id.textView2);
		toastText3 = (TextView) findViewById(R.id.textView3);
		toastText4 = (TextView) findViewById(R.id.textView4);
		
		userName.addTextChangedListener(new BaseTextWatcher(R.id.reg_editText_username));
		pass.addTextChangedListener(new BaseTextWatcher(R.id.reg_editText_pass));
		passCon.addTextChangedListener(new BaseTextWatcher(R.id.reg_editText_pass_confirm));
		email.addTextChangedListener(new BaseTextWatcher(R.id.reg_editText_email));
		
		regButton = (Button) findViewById(R.id.reg_button1);
		regButton.setOnClickListener(this);
		regButton.setEnabled(false);
		userName.setFocusable(true);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.reg_checkBox1:
			sex = 1;
			touXiang.setImageResource(R.drawable.user_men);
			break;

		case R.id.reg_checkBox2:
			sex = 2;
			touXiang.setImageResource(R.drawable.user_women);
			break;
		}
	}
	
	public class BaseTextWatcher implements TextWatcher{
		
		private int id;

		public BaseTextWatcher(int id) {
			this.id = id;
		}

		@Override
		public void afterTextChanged(Editable s) {
			String str = s.toString();
			switch (id) {
			case R.id.reg_editText_username:
				userNameValue = str;
				toastText1.setVisibility(View.VISIBLE);
				if (!StringUtil.isNegUserName(userNameValue)) {
					toastText1.setText("· 用户名不符合规定");
					toastText1.setTextColor(Color.parseColor("#D43834"));
					isOK_1 = false;
					regButton.setEnabled(false);
					return;
				}
				toastText1.setText("· 该用户名可以使用");
				toastText1.setTextColor(Color.parseColor("#649844"));
				isOK_1 = true;
				break;
			case R.id.reg_editText_pass:
				toastText2.setVisibility(View.VISIBLE);
				passValue = str;
				if (!StringUtil.isNegPass(passValue)) {
					toastText2.setText("· 密码不符合规定");
					toastText2.setTextColor(Color.parseColor("#D43834"));
					isOK_2 = false;
					regButton.setEnabled(false);
					return;
				}
				if(passValue.equals(passValue2)){
					toastText3.setText("· 密码确认正确");
					toastText3.setTextColor(Color.parseColor("#649844"));
					isOK_3 = true;
				}else if(StringUtil.isNegPass(passValue2)){
					toastText3.setText("· 密码前后不一致");
					toastText3.setTextColor(Color.parseColor("#D43834"));
					isOK_3 = false;
					regButton.setEnabled(false);
					return;
				}
				toastText2.setText("· 该密码可用");
				toastText2.setTextColor(Color.parseColor("#649844"));
				isOK_2 = true;
				break;
			case R.id.reg_editText_pass_confirm:
				toastText3.setVisibility(View.VISIBLE);
				passValue2 = str;
				if (!passValue2.equals(passValue)) {
					toastText3.setText("· 密码前后不一致");
					toastText3.setTextColor(Color.parseColor("#D43834"));
					isOK_3 = false;
					regButton.setEnabled(false);
					return;
				}
				toastText3.setText("· 密码确认正确");
				toastText3.setTextColor(Color.parseColor("#649844"));
				isOK_3 = true;
				break;
			case R.id.reg_editText_email:
				toastText4.setVisibility(View.VISIBLE);
				emailValue = str;
				if (!StringUtil.isNegEmail(emailValue)) {
					toastText4.setText("· 邮箱输入错误");
					toastText4.setTextColor(Color.parseColor("#D43834"));
					isOK_4 = false;
					regButton.setEnabled(false);
					return;
				}
				toastText4.setText("· 恭喜，该邮箱可用");
				toastText4.setTextColor(Color.parseColor("#649844"));
				isOK_4 = true;
				break;
			}
			if(isOK_1 && isOK_2 && isOK_3 && isOK_4)
				regButton.setEnabled(true);
			else regButton.setEnabled(false);
		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}

		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		
	}

	@Override
	public void onClick(View v) {
		if(isOK_1 && isOK_2 && isOK_3 && isOK_4){
			showLoading = (ProgressDialog) ViewUtil.showLoading(this);
			showLoading.setCancelable(false);
			CommunicationManager.getInstance().reg(sex, userNameValue, passValue, emailValue, regListener);
		}
	}
	
	private static final int REG_CONFIRM = 1;
	private static final int REG_FAILUER = 0;
	private static final int REG_CHECK_EMAIL = 2;
	
	private CommunicationResultListener regListener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			Message message = mHandler.obtainMessage();
			message.obj = resultData;
			if(result == CommunicationManager.SUCCEED && token > 0){
				message.arg1 = REG_CONFIRM;
			}else{
				message.arg1 = REG_FAILUER;
			}
			mHandler.sendMessage(message);
		};
	};
	
	Handler mHandler = new Handler(){
		
		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case REG_FAILUER:
				ViewUtil.showToast((String)msg.obj, RegActivity.this);
				break;
			case REG_CONFIRM:
				UserBean user = (UserBean) msg.obj;
				UserBean.setUser(user);
				if(user.responsecode == 1){
					PreferenceManager preference = PreferenceManager.getInstance(RegActivity.this);
					preference.putAccount(user.account);
					preference.putPassMD5("");
					preference.putPass("");
					preference.putRegisteTime(user.zhucetime);
					preference.putRemember(true);
					preference.putLoginAuto(false);
					preference.putEmail(user.email);
					
					View view = LayoutInflater.from(RegActivity.this).inflate(R.layout.guess_dialog_munu, null);
					TextView tx1 = (TextView) view.findViewById(R.id.textView2);
					TextView tx2 = (TextView) view.findViewById(R.id.textView3);
					Button button = (Button) view.findViewById(R.id.guess_confirm);
					Button chongzhi = (Button) view.findViewById(R.id.guess_chongzhi);
					
					final Dialog showDialog = ViewUtil.showDialog(RegActivity.this, view);
					chongzhi.setVisibility(View.GONE);
					button.setText("确定");
					tx1.setText("注册成功！");
					tx2.setText("系统赠送" + (int)user.fund + "资金");
					showDialog.setOnDismissListener(new OnDismissListener() {
						
						@Override
						public void onDismiss(DialogInterface dialog) {
							finish();
						}
					});
					button.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							showDialog.cancel();
							Intent intent = new Intent(RegActivity.this, LoginActivity.class);
							startActivity(intent);
							finish();
						}
					});
				}else{
					ViewUtil.showToast(user.msg, RegActivity.this);
				}
				break;
			}
			ViewUtil.dismiss(showLoading);
		};
	};

	@Override
	protected void onStop() {
		super.onStop();
		StockApplication.cancelToast();
	};
}
