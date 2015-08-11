package cn.chinat2t.stockgod.activity;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.chinat2t.stockgod.MainActivity;
import cn.chinat2t.stockgod.R;
import cn.chinat2t.stockgod.StockApplication;
import cn.chinat2t.stockgod.bean.GameResultBean;
import cn.chinat2t.stockgod.bean.GuessJuBean;
import cn.chinat2t.stockgod.bean.UserBean;
import cn.chinat2t.stockgod.http.CommunicationManager;
import cn.chinat2t.stockgod.http.CommunicationResultListener;
import cn.chinat2t.stockgod.utils.ViewUtil;
import cn.chinat2t.stockgod.views.RectView;
/**
 * 猜涨跌
 * @author Administrator
 */
public class GuessActivity extends Activity implements OnClickListener, OnCheckedChangeListener {

	private static final int DIALOG_FAILURE = 0;
	private static final int DIALOG_SUCCESS = 1;
	private static final int SKY_EYE = 2;
	private static final int GAME_JU = 4;
	private static final int CAN_SAI = 5;
	
	private GuessJuBean guessStock;
	private EditText setAuto;
	private TextView riseWenHao;
	private TextView fallWenHao;
	private TextView guessTitle;
	private RectView riseImage;
	private RectView fallImage;	
	private Dialog dialog;
	private Button button1;
	private Button button2;
	private Button button3;
	private Button onClick;
	private Button skyEyeButton;
	private View eyeText;
	private String zijinStr;
	private int selectIndex;
	
	private boolean hasSkyEye;
	private int roomID;
	private int saizhiID;
	private int setGold;
	private RadioGroup guessRadioGroup;
	private LinearLayout radioGroup;
	private Dialog showLoading;
	private UserBean userBean;
	private GameResultBean bean;
	private Button canSaiButn;
	private boolean isSelect1;
	private boolean isSelect2;
	private TextView gameTitle;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.guess_layout);
		
		userBean = UserBean.getInstance();
		
		roomID = getIntent().getIntExtra("roomId", 1);
		saizhiID = getIntent().getIntExtra("saizhiID", 1);
		initVeiws();
		if(roomID == 1){ 
			zijinStr = "资金";
			gameTitle.setText("参赛资金");
		}else{ 
			zijinStr = "金币"; 
			gameTitle.setText("参赛金币");
		}
		MainActivity.setTitleName("猜涨跌");
		showLoading = ViewUtil.showLoading(getParent());
		CommunicationManager.getInstance().getGuessJu(userBean.uid, saizhiID, roomID, gameJulistener);
	}

	private void initVeiws() {
		guessTitle = (TextView) findViewById(R.id.guess_info);
		riseWenHao = (TextView) findViewById(R.id.guess_rise_wenhao);
		fallWenHao = (TextView) findViewById(R.id.guess_fall_wenhao);
		riseWenHao.setVisibility(View.VISIBLE);
		fallWenHao.setVisibility(View.VISIBLE);
		riseImage = (RectView) findViewById(R.id.guess_rise_image);
		fallImage = (RectView) findViewById(R.id.guess_fall_image);
		setAuto = (EditText) findViewById(R.id.guess_set_auto_value);
		setAuto.addTextChangedListener(textWatcher);
		eyeText = findViewById(R.id.guess_eye_text);
		skyEyeButton = (Button) findViewById(R.id.guess_sky_eye);
		skyEyeButton.setOnClickListener(this);
		radioGroup = (LinearLayout)findViewById(R.id.choose_btn_layout);
//		radioGroup.setOnCheckedChangeListener(this);
		button1 = (Button) findViewById(R.id.guess_set_button1);
		button2 = (Button) findViewById(R.id.guess_set_button2);
		button3 = (Button) findViewById(R.id.guess_set_button3);
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		gameTitle = (TextView) findViewById(R.id.text1);
		canSaiButn = (Button) findViewById(R.id.guess_set_confirm);
		canSaiButn.setOnClickListener(this);
		canSaiButn.setEnabled(false);
		
		guessRadioGroup = (RadioGroup) findViewById(R.id.guess_main_action);
		guessRadioGroup.setOnCheckedChangeListener(this);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if(userBean.usertype == UserBean.USER_LIN_SHI){
			((RadioButton)findViewById(checkedId)).setChecked(false);
			ViewUtil.showLinShiDialog(getParent(), "您是临时用户不能参加比赛,请注册");
			return;
		}
		if(guessStock == null) return;
		switch (checkedId) {
		case R.id.guess_rise_button:
			selectIndex = 1;
			isSelect1 = true;
			break;

		case R.id.guess_fall_button:
			selectIndex = 2;
			isSelect1 = true;
			break;
		}
		updateCanSaiBtnStatus();
	}

	private void clearAutoText() {
		if(setAuto.getText().toString().length() > 0){
			setAuto.setText("");
			setAuto.setHint("手动输入");
			setAuto.clearFocus();
		}
	}
	
	private TextWatcher textWatcher = new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count, int after) {
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			if(guessStock == null) return;
			if(s.toString().length() > 0){
//				setAuto.requestFocus();
				if(onClick != null)
					onClick.setBackgroundResource(R.drawable.grey_btn);
				onClick = null;
				
				isSelect2 = true;
				if(s.toString().length() <= 9){
					setGold = Integer.parseInt(s.toString());
				}
			}else{
				isSelect2 = false;
			}
			updateCanSaiBtnStatus();
		}

	};

	private void updateCanSaiBtnStatus() {
		if(isSelect2 && isSelect1){
			canSaiButn.setEnabled(true);
		}else{
			canSaiButn.setEnabled(false);
		}
	}
	
	private CommunicationResultListener gameJulistener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED){
				Message message = mHandler.obtainMessage();
				message.arg1 = GAME_JU;
				message.obj = resultData;
				mHandler.sendMessage(message);
			}else{
				ViewUtil.dismiss(showLoading);
//				ViewUtil.showToast((String)resultData, GuessActivity.this);
			}
		};
	};
	
	private CommunicationResultListener hasEyeListener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED){
				Message message = mHandler.obtainMessage();
				message.arg1 = SKY_EYE;
				message.obj = resultData;
				mHandler.sendMessage(message);
			}else{
				new Handler().post(new Runnable() {
					
					@Override
					public void run() {
						skyEyeButton.setClickable(true);
					}
				});
				ViewUtil.dismiss(showLoading);
//				ViewUtil.showToast((String)resultData, GuessActivity.this);
			}
		};
	};
	
	private CommunicationResultListener canSaiListener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED){
				Message message = mHandler.obtainMessage();
				message.arg1 = CAN_SAI;
				message.obj = resultData;
				mHandler.sendMessage(message);
			}else{
				ViewUtil.dismiss(showLoading);
				new Handler().post(new Runnable() {
					
					@Override
					public void run() {
						canSaiButn.setEnabled(true);	
					}
				});
			}
		};
	};
	
	Handler mHandler = new Handler(){


		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case GAME_JU:
				if(msg.obj instanceof GuessJuBean){
					guessStock = (GuessJuBean) msg.obj;
					String string = guessTitle.getText().toString();
					String time = guessStock.day == 1?"明天":guessStock.day +"天后";
					guessTitle.setText(String.format(string, guessStock.stockname, time));
					button1.setText(guessStock.goldf + "");
					button2.setText(guessStock.golds + "");
					button3.setText(guessStock.goldt + "");
					if(onClick != null)
						onClick.setBackgroundResource(R.drawable.grey_btn);
					onClick = null;
					if(guessStock.isprop == 1){
						viewRoomNum();
					}
				}
				break;
				
			case SKY_EYE:
				if(msg.obj instanceof Boolean){
					hasSkyEye = (Boolean) msg.obj;
					if(hasSkyEye) viewRoomNum();
					else {
//						ViewUtil.showDialog(getParent(), "您还没有天眼卡", null);
						View view2 = LayoutInflater.from(GuessActivity.this).inflate(R.layout.guess_dialog_munu, null);
			        	TextView tx11 = (TextView) view2.findViewById(R.id.textView1);
			   			TextView tx12 = (TextView) view2.findViewById(R.id.textView2);
			   			Button button2 = (Button) view2.findViewById(R.id.guess_confirm);
			   			Button chongzhi2 = (Button) view2.findViewById(R.id.guess_chongzhi);
						button2.setText("商城");
						tx11.setText("您还没有天眼卡");
						tx12.setText("请到商城购买");
						button2.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
			   					dialog.cancel();
			   					Intent intent = new Intent(GuessActivity.this, MallActivity.class);
			   					CompetitiveGroup.getInstance().switchActivity("MallActivity", intent, -1, -1);
							}
						});
						chongzhi2.setText("取消");
						chongzhi2.setVisibility(View.VISIBLE);
						chongzhi2.setOnClickListener(new OnClickListener() {
							@Override
							public void onClick(View v) {
			   					dialog.cancel();
							}
					});
						dialog = new Dialog(getParent(), R.style.selectorDialog);
			            dialog.setContentView(view2);
			            dialog.show();
					}
				}
				skyEyeButton.setClickable(true);
				break;
				
			case CAN_SAI:
				bean = (GameResultBean) msg.obj;
				if(bean.responsecode == 1){
					sendBroadcast(new Intent(MainActivity.UPDATE_USER_INFOR));
					showDialog(DIALOG_SUCCESS);
				}else if(bean.responsecode == 4){
					showDialog(DIALOG_FAILURE);
				}else {
					ViewUtil.showDialog(getParent(), bean.msg, null);
					CommunicationManager.getInstance().getGuessJu(userBean.uid, saizhiID, roomID, gameJulistener);
				}
				canSaiButn.setEnabled(true);
				break;
			}
			ViewUtil.dismiss(showLoading);
		}
	};
	
	private long clickTime = 0;
	@Override
	public void onClick(View v) {
		if(userBean.usertype == UserBean.USER_LIN_SHI){
			ViewUtil.showLinShiDialog(getParent(), "您是临时用户不能参加比赛,请注册");
			return;
		}
		if(guessStock == null) return;
		long time = System.currentTimeMillis();
		switch (v.getId()) {
		case R.id.guess_sky_eye:
			if(time - clickTime > 1000){
				clickTime = time;
			}else return;
			if(userBean.usertype == UserBean.USER_LIN_SHI){
				ViewUtil.showLinShiDialog(getParent(), "您是临时用户不能参加比赛,请注册");
				return;
			}
			skyEyeButton.setClickable(false);
			CommunicationManager.getInstance().getHasSkyEye(
				userBean.uid, roomID, saizhiID, guessStock.bisaiid, hasEyeListener);
			break;
		case R.id.guess_set_confirm:
			if(time - clickTime > 1000){
				clickTime = time;
			}else return;
			if(userBean.usertype == UserBean.USER_LIN_SHI){
				ViewUtil.showLinShiDialog(getParent(), "您是临时用户不能参加比赛,请注册");
				return;
			}
			if(guessStock.isplay == 1){
				ViewUtil.showDialog(getParent(), "你已经参赛过了！", null);
			}else if(setGold < guessStock.mingold || setGold > guessStock.maxgold){
				ViewUtil.showDialog(getParent(), "输入金额应在" + guessStock.mingold + "~"  + guessStock.maxgold + "之间", null);
			}else{
				showLoading = ViewUtil.showLoading(getParent());
				canSaiButn.setEnabled(false);
				CommunicationManager.getInstance().canSaiGuessGame(userBean.uid, setGold, 
						guessStock.bisaiid, guessStock.isprop, selectIndex, roomID, canSaiListener);
			}
			break;
		case R.id.guess_set_button1:
			setGold = guessStock.goldf;
			clearAutoText();
			if(onClick != button1){
				button1.setBackgroundResource(R.drawable.yellow_btn);
				isSelect2 = true;
				onClick = button1;
			} else {
				isSelect2 = false;
				onClick = null;
				button1.setBackgroundResource(R.drawable.grey_btn);
			}
			button2.setBackgroundResource(R.drawable.grey_btn);
			button3.setBackgroundResource(R.drawable.grey_btn);
			updateCanSaiBtnStatus();
			break;
			
		case R.id.guess_set_button2:
			setGold = guessStock.golds;
			clearAutoText();
			if(onClick != button2){
				button2.setBackgroundResource(R.drawable.yellow_btn);
				isSelect2 = true;
				onClick = button2;
			} else {
				isSelect2 = false;
				onClick = null;
				button2.setBackgroundResource(R.drawable.grey_btn);
			}
			button1.setBackgroundResource(R.drawable.grey_btn);
			button3.setBackgroundResource(R.drawable.grey_btn);
			updateCanSaiBtnStatus();
			break;
			
		case R.id.guess_set_button3:
			setGold = guessStock.goldt;
			clearAutoText();
			if(onClick != button3){
				button3.setBackgroundResource(R.drawable.yellow_btn);
				isSelect2 = true;
				onClick = button3;
			} else {
				isSelect2 = false;
				onClick = null;
				button3.setBackgroundResource(R.drawable.grey_btn);
			}
			button2.setBackgroundResource(R.drawable.grey_btn);
			button1.setBackgroundResource(R.drawable.grey_btn);
			updateCanSaiBtnStatus();
			break;
		}
	}
	
	private void viewRoomNum() {
		if(guessStock == null) return;
		riseImage.setMaxValue(guessStock.rise > guessStock.fall?guessStock.rise:guessStock.fall);
		fallImage.setMaxValue(guessStock.rise > guessStock.fall?guessStock.rise:guessStock.fall);
		
		riseWenHao.setVisibility(View.INVISIBLE);
		riseImage.setVisibility(View.VISIBLE);
		riseImage.setRegHeight(guessStock.rise);
//		riseImage.setColor(getResources().getColor(R.color.red));
		riseImage.setColor(0);
		riseImage.setTitle(String.valueOf(guessStock.rise));
		riseImage.invalidate();
		
		fallWenHao.setVisibility(View.INVISIBLE);
		fallImage.setVisibility(View.VISIBLE);
//		fallImage.setColor(getResources().getColor(R.color.green));
		fallImage.setColor(1);
		fallImage.setRegHeight(guessStock.fall);
		fallImage.setTitle(String.valueOf(guessStock.fall));
		fallImage.invalidate();
		
		skyEyeButton.setVisibility(View.INVISIBLE);
		eyeText.setVisibility(View.INVISIBLE);
	}

	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_FAILURE:
   			View view = LayoutInflater.from(this).inflate(R.layout.guess_dialog_munu, null);
   			TextView tx1 = (TextView) view.findViewById(R.id.textView1);
   			TextView tx2 = (TextView) view.findViewById(R.id.textView2);
   			Button button = (Button) view.findViewById(R.id.guess_confirm);
   			Button chongzhi = (Button) view.findViewById(R.id.guess_chongzhi);
   			chongzhi.setVisibility(View.VISIBLE);
   			chongzhi.setOnClickListener(new OnClickListener() {
   					
   				@Override
   				public void onClick(View v) {
   					dialog.cancel();
   					Intent intent = new Intent(GuessActivity.this, MallActivity.class);
   					intent.putExtra("flag", 1);
   					CompetitiveGroup.getInstance().switchActivity("MallActivity", intent, -1, -1);
   				}
   			});
   			button.setOnClickListener(new OnClickListener() {
   					
   				@Override
   				public void onClick(View v) {
   					dialog.cancel();
					onBackPressed();
   				}
   			});
   			button.setText("确定");
   			tx1.setText("您的参赛" + zijinStr + "不足");
   			tx2.setText("您可以充值后继续参加此技能");
   			
            dialog = new Dialog(getParent(), R.style.selectorDialog);
            dialog.setContentView(view);
            dialog.show();
            break;
        case DIALOG_SUCCESS:
        	View view2 = LayoutInflater.from(this).inflate(R.layout.guess_dialog_munu, null);
        	TextView tx11 = (TextView) view2.findViewById(R.id.textView1);
   			TextView tx12 = (TextView) view2.findViewById(R.id.textView2);
   			TextView tx22 = (TextView) view2.findViewById(R.id.textView3);
   			Button button2 = (Button) view2.findViewById(R.id.guess_confirm);
   			Button chongzhi2 = (Button) view2.findViewById(R.id.guess_chongzhi);
			chongzhi2.setVisibility(View.GONE);
			button2.setText("确定");
			tx11.setText("恭喜您参赛成功");
			tx12.setVisibility(View.GONE);
			tx12.setText(String.format("您若胜利将获得%s" + zijinStr, bean!=null?bean.yujishouyi:0));
			tx12.setMovementMethod(LinkMovementMethod.getInstance());
			tx22.setText(String.format("您今天已参加了%s次猜涨跌", bean!=null?bean.playcode:0));
			tx22.setVisibility(View.VISIBLE);
			button2.setOnClickListener(new OnClickListener() {
					
				@Override
				public void onClick(View v) {
					dialog.cancel();
					onBackPressed();
				}
			});
			dialog = new Dialog(getParent(), R.style.selectorDialog);
            dialog.setContentView(view2);
            dialog.show();
        }
        return dialog;
    }

	@Override
	public void onBackPressed() {
		CompetitiveGroup.getInstance().back();
	}
	
	@Override
	public void onContentChanged() {
		MainActivity.setTitleName("猜涨跌");
	}
	
	@Override
	public void onDetachedFromWindow() {
		StockApplication.cancelToast();
	}
}
