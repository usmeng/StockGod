package cn.chinat2t.stockgod.activity;

import java.util.ArrayList;
import java.util.List;

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
import android.widget.TextView;
import cn.chinat2t.stockgod.MainActivity;
import cn.chinat2t.stockgod.R;
import cn.chinat2t.stockgod.bean.ChooseJuBean;
import cn.chinat2t.stockgod.bean.GameResultBean;
import cn.chinat2t.stockgod.bean.GameStockBean;
import cn.chinat2t.stockgod.bean.UserBean;
import cn.chinat2t.stockgod.http.CommunicationManager;
import cn.chinat2t.stockgod.http.CommunicationResultListener;
import cn.chinat2t.stockgod.utils.ViewUtil;

/**
 * 选股竞技
 * @author Administrator
 */
public class ChooseActivity extends Activity implements OnClickListener{
	private static final int DIALOG_FAILURE = 0;
	private static final int DIALOG_SUCCESS = 1;
	private static final int GAME_XIANG = 2;
	private static final int CAN_SAI = 3;

	private List<View> stockViewList = new ArrayList<View>();
	private List<GameStockBean> gameList = new ArrayList<GameStockBean>();
	private GameStockBean chooseGameStock;
	private ChooseJuBean gameBean;
	protected GameResultBean bean;
	private TextView textTitle;

	private Dialog dialog = null;
	private EditText setAuto;
	private TextView gameTitle;
	private Button canSaiBtn;
	private Button button1;
	private Button button2;
	private Button button3;
	private Button onClick;
	private View chooseArrow1;
	private View chooseArrow2;
	private View chooseArrow3;
	private View view1;
	private View view2;
	private View view3;
	private int roomID;
	private int saizhiID = 3;
	private String zijinStr;
	private Dialog showLoading;
	private UserBean userBean;
	private int setGold;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.choose_layout);
		userBean = UserBean.getInstance();
		initViews();
		roomID = getIntent().getIntExtra("roomId", 1);
		saizhiID = getIntent().getIntExtra("saizhiID", 1);
		if (roomID == 1) {
			zijinStr = "资金";
			gameTitle.setText("参赛资金");
		} else {
			zijinStr = "金币";
			gameTitle.setText("参赛金币");
		}
		MainActivity.setTitleName("选股竞技");
		showLoading = ViewUtil.showLoading(getParent());
		CommunicationManager.getInstance().getChooseJu(userBean.uid, saizhiID,
				roomID, gameXianglistener);

//		onClick(view1);
		onClickButton1();
	}

	private void onClickButton1() {
		view1.setBackgroundResource(R.drawable.choose_select_bg);
		view2.setBackgroundResource(R.drawable.choose_unselect_bg);
		view3.setBackgroundResource(R.drawable.choose_unselect_bg);
		chooseArrow1.setVisibility(View.VISIBLE);
	}

	private CommunicationResultListener gameXianglistener = new CommunicationResultListener() {

		public void resultListener(byte result, Object resultData) {
			if (CommunicationManager.SUCCEED == result) {
				Message message = mHandler.obtainMessage();
				message.arg1 = GAME_XIANG;
				message.what = result;
				message.obj = resultData;
				mHandler.sendMessage(message);
			} else {
				ViewUtil.dismiss(showLoading);
			}
		};
	};

	private void initViews() {
		textTitle = (TextView) findViewById(R.id.choose_info);
		view1 = findViewById(R.id.choose_stock_1);
		view2 = findViewById(R.id.choose_stock_2);
		view3 = findViewById(R.id.choose_stock_3);
		chooseArrow1 = view1.findViewById(R.id.choose_arrow);
		chooseArrow2 = view2.findViewById(R.id.choose_arrow);
		chooseArrow3 = view3.findViewById(R.id.choose_arrow);
		view1.setOnClickListener(this);
		view2.setOnClickListener(this);
		view3.setOnClickListener(this);

		setAuto = (EditText) findViewById(R.id.guess_set_auto_value);
		setAuto.addTextChangedListener(textWatcher);
//		radioGroup = (LinearLayout) findViewById(R.id.choose_btn_layout);
//		radioGroup.setOnCheckedChangeListener(this);
		button1 = (Button) findViewById(R.id.guess_set_button1);
		button1.setOnClickListener(this);
		button2 = (Button) findViewById(R.id.guess_set_button2);
		button2.setOnClickListener(this);
		button3 = (Button) findViewById(R.id.guess_set_button3);
		button3.setOnClickListener(this);
		canSaiBtn = (Button) findViewById(R.id.guess_set_confirm);
		gameTitle = (TextView) findViewById(R.id.text1);
		canSaiBtn.setOnClickListener(this);
		canSaiBtn.setEnabled(false);

		stockViewList.add(view1);
		stockViewList.add(view2);
		stockViewList.add(view3);
	}

	private TextWatcher textWatcher = new TextWatcher() {

		@Override
		public void onTextChanged(CharSequence s, int start, int before,
				int count) {

		}

		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {

		}

		@Override
		public void afterTextChanged(Editable s) {
			if (gameBean == null)
				return;
			if (s.toString().length() > 0) {
				if(onClick != null)
					onClick.setBackgroundResource(R.drawable.grey_btn);
				onClick = null;
				if (s.toString().length() <= 9) {
					setGold = Integer.parseInt(s.toString());
				}
				canSaiBtn.setEnabled(true);
			} else {
				canSaiBtn.setEnabled(false);
			}
		}
	};

	private CommunicationResultListener canSaiListener = new CommunicationResultListener() {

		public void resultListener(byte result, Object resultData) {
			if (result == CommunicationManager.SUCCEED && token > 0) {
				Message msg = mHandler.obtainMessage();
				msg.what = result;
				msg.arg1 = CAN_SAI;
				msg.obj = resultData;
				mHandler.sendMessage(msg);
			} else {
				ViewUtil.dismiss(showLoading);
				new Handler().post(new Runnable() {
					
					@Override
					public void run() {
						canSaiBtn.setEnabled(true);	
					}
				});
			}
		};
	};

	Handler mHandler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case GAME_XIANG:
				if (msg.obj instanceof ChooseJuBean) {
					gameBean = (ChooseJuBean) msg.obj;
					gameList = gameBean.stockList;
					String day = "明天";
					if(gameBean.day == 2){
						day = "后天";
					}else if(gameBean.day > 2){
						day = gameBean.day + "天后";
					}
					textTitle.setText(String.format(textTitle.getText().toString(), day));
					for (int i = 0; i < stockViewList.size() && i < gameList.size(); i++) {
						View view = stockViewList.get(i);
						GameStockBean stock = gameList.get(i);
						TextView name = (TextView) view.findViewById(R.id.choose_stock_name);
						TextView code = (TextView) view.findViewById(R.id.choose_stock_code);
						TextView price = (TextView) view.findViewById(R.id.choose_stock_price);
						TextView zdlv = (TextView) view.findViewById(R.id.choose_stock_zhangdielv);
						name.setText(stock.ssname);
						code.setText(stock.preclose + "");
						price.setText(stock.nowprice + "");
						zdlv.setText(stock.zhangdie + "%");
						double zhangfu = Double.parseDouble(stock.zhangdie);
						if (zhangfu > 0) {
							zdlv.setTextColor(getResources().getColor(R.color.red));
						} else if (zhangfu == 0) {
							zdlv.setTextColor(getResources().getColor(R.color.white));
						} else {
							zdlv.setTextColor(getResources().getColor(R.color.green));
						}
					}
					chooseGameStock = gameBean.stockList.get(0);
					if(onClick != null)
						onClick.setBackgroundResource(R.drawable.grey_btn);
					onClick = null;
					button1.setText(gameBean.goldf + "");
					button2.setText(gameBean.golds + "");
					button3.setText(gameBean.goldt + "");
				}
				break;
			case CAN_SAI:
				bean = (GameResultBean) msg.obj;
				if (bean.responsecode == 1) {
					sendBroadcast(new Intent(MainActivity.UPDATE_USER_INFOR));
					showDialog(DIALOG_SUCCESS);
				} else if (bean.responsecode == 4) {
					showDialog(DIALOG_FAILURE);
				} else {
					ViewUtil.showDialog(getParent(), bean.msg, null);
					CommunicationManager.getInstance().getChooseJu(
							userBean.uid, saizhiID, roomID, gameXianglistener);
				}
				canSaiBtn.setEnabled(true);	
				break;
			}

			ViewUtil.dismiss(showLoading);
		};
	};

	private long clickTime;

	@Override
	public void onClick(View v) {
		if(userBean.usertype == UserBean.USER_LIN_SHI){
			ViewUtil.showLinShiDialog(getParent(), "您是临时用户不能参加比赛,请注册");
			return;
		}
		switch (v.getId()) {
		case R.id.guess_set_button1:
			setGold = gameBean.goldf;
			if(onClick != button1){
				button1.setBackgroundResource(R.drawable.btn_gold_yellow);
				onClick = button1;
			} else {
				onClick = null;
				button1.setBackgroundResource(R.drawable.grey_btn);
			}
			button2.setBackgroundResource(R.drawable.grey_btn);
			button3.setBackgroundResource(R.drawable.grey_btn);
			clearAutoText();
			break;
		case R.id.guess_set_button2:
			setGold = gameBean.golds;
			if(onClick != button2){
				button2.setBackgroundResource(R.drawable.btn_gold_yellow);
				onClick = button2;
			} else {
				onClick = null;
				button2.setBackgroundResource(R.drawable.grey_btn);
			}
			button1.setBackgroundResource(R.drawable.grey_btn);
			button3.setBackgroundResource(R.drawable.grey_btn);
			clearAutoText();
			break;
		case R.id.guess_set_button3:
			setGold = gameBean.goldt;
			if(onClick != button3){
				button3.setBackgroundResource(R.drawable.btn_gold_yellow);
				onClick = button3;
			} else {
				onClick = null;
				button3.setBackgroundResource(R.drawable.grey_btn);
			}
			button1.setBackgroundResource(R.drawable.grey_btn);
			button2.setBackgroundResource(R.drawable.grey_btn);
			clearAutoText();
			break;
		case R.id.choose_stock_1:
			view1.setBackgroundResource(R.drawable.choose_select_bg);
			view2.setBackgroundResource(R.drawable.choose_unselect_bg);
			view3.setBackgroundResource(R.drawable.choose_unselect_bg);
			chooseArrow1.setVisibility(View.VISIBLE);
			chooseArrow2.setVisibility(View.INVISIBLE);
			chooseArrow3.setVisibility(View.INVISIBLE);
			if (gameList != null && gameList.size() >= 3) {
				chooseGameStock = gameList.get(0);
			}
			break;
		case R.id.choose_stock_2:
			view2.setBackgroundResource(R.drawable.choose_select_bg);
			view1.setBackgroundResource(R.drawable.choose_unselect_bg);
			view3.setBackgroundResource(R.drawable.choose_unselect_bg);
			chooseArrow2.setVisibility(View.VISIBLE);
			chooseArrow1.setVisibility(View.INVISIBLE);
			chooseArrow3.setVisibility(View.INVISIBLE);
			if (gameList != null && gameList.size() >= 3) {
				chooseGameStock = gameList.get(1);
			}
			break;
		case R.id.choose_stock_3:
			view3.setBackgroundResource(R.drawable.choose_select_bg);
			view2.setBackgroundResource(R.drawable.choose_unselect_bg);
			view1.setBackgroundResource(R.drawable.choose_unselect_bg);
			chooseArrow3.setVisibility(View.VISIBLE);
			chooseArrow2.setVisibility(View.INVISIBLE);
			chooseArrow1.setVisibility(View.INVISIBLE);
			if (gameList != null && gameList.size() >= 3) {
				chooseGameStock = gameList.get(2);
			}
			break;
		case R.id.guess_set_confirm:
			long time = System.currentTimeMillis();
			if (time - clickTime > 1000) {
				clickTime = time;
			} else return;
			if (gameBean.isplay == 1) {
				ViewUtil.showDialog(getParent(), "您已经玩过了哦！", null);
			} else if (setGold < gameBean.mingold || setGold > gameBean.maxgold) {
				ViewUtil.showDialog(getParent(), "输入金额应在" + gameBean.mingold
						+ "~" + gameBean.maxgold + "之间", null);
			} else {
				canSaiBtn.setEnabled(false);
				CommunicationManager.getInstance().canSaiChooseGame(
						userBean.uid, setGold, gameBean.bisaiid,
						gameBean.isprop, chooseGameStock.id, roomID,
						canSaiListener);
			}
			break;
		}
	}

	@Override
	protected Dialog onCreateDialog(int id) {
		View view = LayoutInflater.from(this).inflate(R.layout.guess_dialog_munu, null);
		TextView tx0 = (TextView) view.findViewById(R.id.textView1);
		TextView tx1 = (TextView) view.findViewById(R.id.textView2);
		TextView tx2 = (TextView) view.findViewById(R.id.textView3);
		Button button = (Button) view.findViewById(R.id.guess_confirm);
		Button chongzhi = (Button) view.findViewById(R.id.guess_chongzhi);
		button.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				dialog.cancel();
				onBackPressed();
			}
		});
		switch (id) {
		case DIALOG_FAILURE:
			chongzhi.setVisibility(View.VISIBLE);
			chongzhi.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					dialog.cancel();
					Intent intent = new Intent(ChooseActivity.this, MallActivity.class);
   					intent.putExtra("flag", 1);
   					CompetitiveGroup.getInstance().switchActivity("MallActivity", intent, -1, -1);
				}
			});
			tx2.setVisibility(View.GONE);
			button.setText("取消");
			tx0.setText("您的参赛" + zijinStr + "不足");
			tx1.setText("您可以充值后继续参加此技能");
			break;
		case DIALOG_SUCCESS:
			chongzhi.setVisibility(View.GONE);
			button.setText("确定");
			tx0.setText("恭喜您参赛成功");
			tx1.setVisibility(View.GONE);
			tx1.setText(String.format("您若胜利将获得%s" + zijinStr, bean != null ? bean.yujishouyi : 0));
			tx1.setMovementMethod(LinkMovementMethod.getInstance());
			tx2.setText(String.format("您今天已参加了%s次选股竞技",
					bean != null ? bean.playcode : 0));
			break;
		}
		dialog = new Dialog(getParent(), R.style.selectorDialog);
		dialog.setContentView(view);
		dialog.show();
		return dialog;
	}

	private void clearAutoText() {
		if(setAuto.getText().toString().length() > 0){
			setAuto.setText("");
			setAuto.setHint("手动输入");
			setAuto.clearFocus();
		}
		if(onClick != null)
			canSaiBtn.setEnabled(true);
		else canSaiBtn.setEnabled(false);
	}

	@Override
	public void onBackPressed() {
		CompetitiveGroup.getInstance().back();
	}

	@Override
	public void onContentChanged() {
		MainActivity.setTitleName("选股竞技");
	}
}
