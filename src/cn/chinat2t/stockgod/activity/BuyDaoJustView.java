package cn.chinat2t.stockgod.activity;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
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

public class BuyDaoJustView extends LinearLayout implements OnClickListener{
	
	protected static final int GET_DAOJU_SUCCESS = 0;
	private LayoutInflater inflater;
	private RelativeLayout layout;
	private EditText numDaoJu;
	private EditText goldDaoJu;
	private TextView desDaoJu;
	private TextView priceDaoJu;
	private TextView nameDaoJu;
	private UserBean userBean;
	private UserDaoJu daoJu;
	private ImageView image;
	private int type;
	private MallActivity context;
	private ProgressDialog showLoading;
	private Button addBtn;
	private Button jianBtn;
	private Bitmap bitmap;
	
	public BuyDaoJustView(Context context){
		super(context);
		this.context = (MallActivity) context;
	}
	
	public BuyDaoJustView(Context context, UserDaoJu djBean, int type, Bitmap bitmap) {
		super(context);
		this.bitmap = bitmap;
		this.context = (MallActivity) context;
		
		this.daoJu = djBean;
		this.type = type;
		userBean = UserBean.getInstance();
		inflater = LayoutInflater.from(context);
		layout = (RelativeLayout) inflater.inflate(R.layout.buy_daoju_layout, null);
		addView(layout, new LinearLayout.LayoutParams(-1, -1));
		
		initView();
	}

	protected void initView() {
		addBtn = (Button)findViewById(R.id.button1);
		addBtn.setOnClickListener(this);
		jianBtn = (Button)findViewById(R.id.button2);
		jianBtn.setOnClickListener(this);
		jianBtn.setEnabled(false);
		findViewById(R.id.button3).setOnClickListener(this);
		findViewById(R.id.button4).setOnClickListener(this);
		numDaoJu = (EditText) findViewById(R.id.editText1);
		goldDaoJu = (EditText) findViewById(R.id.editText2);
		nameDaoJu = (TextView) findViewById(R.id.textView1);
		desDaoJu = (TextView) findViewById(R.id.textView2);
		priceDaoJu = (TextView) findViewById(R.id.textView4);
		image = (ImageView) findViewById(R.id.imageView1);
		
		nameDaoJu.setText(daoJu.name);
		desDaoJu.setText(daoJu.instruction);
		priceDaoJu.setText((int)daoJu.price + "金币");
		numDaoJu.setText("" + ++daoJu.shuliang);
		numDaoJu.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				String numStr = s.toString();
				if(numStr.length() == 0){
					daoJu.shuliang = 1;
					jianBtn.setEnabled(false);
					numDaoJu.setText(daoJu.shuliang + "");
				}else if(numStr.length() > 7){
					daoJu.shuliang = daoJu.max;
					numDaoJu.setText(daoJu.shuliang + "");
					addBtn.setEnabled(false);
				}else{
					daoJu.shuliang = Integer.parseInt(numStr);
					if(daoJu.shuliang > daoJu.max){
						daoJu.shuliang = daoJu.max;
						numDaoJu.setText(daoJu.shuliang + "");
						addBtn.setEnabled(false);
					}else{
						addBtn.setEnabled(true);
						jianBtn.setEnabled(true);
					}
					if(daoJu.shuliang == 1){
						jianBtn.setEnabled(false);
					}else if(daoJu.shuliang == daoJu.max){
						addBtn.setEnabled(false);
					}
				}
				goldDaoJu.setText(daoJu.shuliang * (int)daoJu.price + "");
			}
		});
		goldDaoJu.setText(daoJu.shuliang * (int)daoJu.price + "");
		if(bitmap != null){
			image.setBackgroundDrawable(new BitmapDrawable(bitmap));
		}else{
			image.setBackgroundResource(R.drawable.djk);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button1:
			daoJu.shuliang = Integer.parseInt(numDaoJu.getText().toString());
			if(daoJu.shuliang < daoJu.max){
				numDaoJu.setText("" + ++daoJu.shuliang);
				goldDaoJu.setText(daoJu.shuliang * (int)daoJu.price + "");
				addBtn.setEnabled(true);
			}else{
				addBtn.setEnabled(false);
			}
			break;
		case R.id.button2:
			daoJu.shuliang = Integer.parseInt(numDaoJu.getText().toString());
			if(daoJu.shuliang > 1){
				numDaoJu.setText("" + --daoJu.shuliang);
				goldDaoJu.setText(daoJu.shuliang * (int)daoJu.price + "");				
				jianBtn.setEnabled(true);
			}else{
				jianBtn.setEnabled(false);
			}
			break;
		case R.id.button3:
			showLoading = (ProgressDialog) ViewUtil.showLoading(context.getParent());
			showLoading.setMessage("正在交易，请稍后...");
			CommunicationManager.getInstance().buyDaoJu(userBean.uid, daoJu.id, daoJu.shuliang, new CommunicationResultListener() {
				@Override
				public void resultListener(byte result, Object resultData) {
					if(result == CommunicationManager.SUCCEED){
						Message message = handler.obtainMessage();
						message.arg1 = GET_DAOJU_SUCCESS;
						message.obj = resultData;
						handler.sendMessage(message);
					}else{
						ViewUtil.dismiss(showLoading);
					}
				}
			});
			break;
			
		case R.id.button4:
			sendBroadcast();
			break;
		}
	}
	
	Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.arg1) {
			case GET_DAOJU_SUCCESS:
				ResponsedBean bean = (ResponsedBean)msg.obj;
				View view = LayoutInflater.from(getContext()).inflate(R.layout.guess_dialog_munu, null);
				final Dialog dialog = ViewUtil.showDialog(((Activity)getContext()).getParent(), view);
				TextView tx0 = (TextView) view.findViewById(R.id.textView1);
				TextView tx1 = (TextView) view.findViewById(R.id.textView2);
				TextView tx2 = (TextView) view.findViewById(R.id.textView3);
				Button button = (Button) view.findViewById(R.id.guess_confirm);
				Button chongzhi = (Button) view.findViewById(R.id.guess_chongzhi);
				button.setOnClickListener(new OnClickListener() {
   					
	   				@Override
	   				public void onClick(View v) {
	   					dialog.cancel();
	   					((Activity)getContext()).onBackPressed();
	   				}
	   			});
				if(bean.responsecode == 10){
					tx1.setText("恭喜购买成功，您可以进入到我的信息界面上使用");
					context.sendBroadcast(new Intent(MainActivity.UPDATE_USER_INFOR));
				} else {
		   			chongzhi.setVisibility(View.VISIBLE);
		   			chongzhi.setOnClickListener(new OnClickListener() {
	   					
		   				@Override
		   				public void onClick(View v) {
		   					dialog.cancel();
		   					((RadioButton)context.findViewById(R.id.radiobtn1)).setChecked(false);
		   					((RadioButton)context.findViewById(R.id.radiobtn4)).setChecked(true);
		   					type = MallActivity.MALL_CHONGZHI;
		   					sendBroadcast();
		   				}

		   			});
		   			tx0.setText("您的金额不足");
		   			tx1.setText("请您充值");
				}
				break;
			}
			ViewUtil.dismiss(showLoading);
			return false;
		}
	});
	
	private void sendBroadcast() {
		Intent intent = new Intent(MallActivity.UPDATE_MALL_INFOR);
		intent.putExtra("ACTION_TYPE", type);
		context.sendBroadcast(intent);
	}
}
