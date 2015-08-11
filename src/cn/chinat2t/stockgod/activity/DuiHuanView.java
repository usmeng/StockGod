package cn.chinat2t.stockgod.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.chinat2t.stockgod.MainActivity;
import cn.chinat2t.stockgod.R;
import cn.chinat2t.stockgod.bean.ResponsedBean;
import cn.chinat2t.stockgod.bean.UserBean;
import cn.chinat2t.stockgod.bean.UserDaoJu;
import cn.chinat2t.stockgod.http.CommunicationManager;
import cn.chinat2t.stockgod.http.CommunicationResultListener;
import cn.chinat2t.stockgod.utils.ViewUtil;

public class DuiHuanView extends LinearLayout implements OnClickListener{
	
	private LayoutInflater inflater;
	private RelativeLayout layout;
	private Activity context;
	private UserDaoJu userDaoju;
	private TextView descText;
	private EditText numValue;

	public DuiHuanView(Context context, UserDaoJu userDaoju) {
		super(context);
		this.userDaoju = userDaoju;
		this.context = (Activity) context;

		inflater = LayoutInflater.from(context);
		layout = (RelativeLayout) inflater.inflate(R.layout.recharge_exchange_layout, null);
		addView(layout, new LinearLayout.LayoutParams(-1, -1));
		initView();
	}

	protected void initView() {
		Button btn = (Button) findViewById(R.id.button1);
		btn.setOnClickListener(this);
		btn.setText("确定");
		descText = (TextView) findViewById(R.id.textView5);
		numValue = (EditText) findViewById(R.id.editText3);
		if(userDaoju.type == 1){
			descText.setText("我们会尽快为您的手机充值");
		}else if(userDaoju.type == 2){
			descText.setText("电子券的序列号将发送到您输入的手机号码上");
		}
	}

	@Override
	public void onClick(View v) {
		String numStr = numValue.getText().toString();
		if(numStr.length() == 0){
			ViewUtil.showToast("手机号不能为空", context.getParent());
			return;
		}
		showDialog(numStr);
	}
	
	private Dialog showLoading;
	public void showDialog(final String numStr){
		View view = LayoutInflater.from(context).inflate(R.layout.guess_dialog_munu, null);
		TextView tx0 = (TextView) view.findViewById(R.id.textView1);
		TextView tx1 = (TextView) view.findViewById(R.id.textView2);
		TextView tx2 = (TextView) view.findViewById(R.id.textView3);
		Button button = (Button) view.findViewById(R.id.guess_confirm);
		Button chongzhi = (Button) view.findViewById(R.id.guess_chongzhi);
		
		final Dialog showDialog = ViewUtil.showDialog(context.getParent(), view);
		chongzhi.setVisibility(View.VISIBLE);
		chongzhi.setText("取消");
		button.setText("确定");
		tx0.setText("您输入的号码是：");
		tx1.setText(numStr);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog.cancel();
				showLoading = ViewUtil.showLoading(context.getParent());
				CommunicationManager.getInstance().mallChange(UserBean.getInstance().uid, 
						userDaoju.id, userDaoju.type, numStr, changeListener);
			}
		});
		chongzhi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog.cancel();
			}
		});
	}
	
	private CommunicationResultListener changeListener = new CommunicationResultListener() {
		@Override
		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED){
				Message message = handler.obtainMessage();
				message.arg1 = 0;
				message.obj = resultData;
				handler.sendMessage(message);
			}else{
				ViewUtil.dismiss(showLoading);
			}
		};
	};
	
	private Handler handler = new Handler(new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.arg1) {
			case 0:
				ResponsedBean bean = (ResponsedBean) msg.obj;
				ViewUtil.showDialog(context.getParent(), bean.msg, null);
				if(bean.responsecode == 10){
					Intent intent = new Intent(MallActivity.UPDATE_MALL_INFOR);
					intent.putExtra("ACTION_TYPE", MallActivity.MALL_DUIHUAN);
					context.sendBroadcast(intent);
					
					context.sendBroadcast(new Intent(MainActivity.UPDATE_USER_INFOR));
				}
				break;

			case 1:
				break;
			}
			ViewUtil.dismiss(showLoading);
			return false;
		}
	});
	
}
