package cn.chinat2t.stockgod.activity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.chinat2t.stockgod.MainActivity;
import cn.chinat2t.stockgod.R;
import cn.chinat2t.stockgod.bean.FightJuBean;
import cn.chinat2t.stockgod.bean.GameResultBean;
import cn.chinat2t.stockgod.bean.KlineData;
import cn.chinat2t.stockgod.bean.LineEntity;
import cn.chinat2t.stockgod.bean.UserBean;
import cn.chinat2t.stockgod.framework.BaseGroup;
import cn.chinat2t.stockgod.http.CommunicationManager;
import cn.chinat2t.stockgod.http.CommunicationResultListener;
import cn.chinat2t.stockgod.utils.DataUtil;
import cn.chinat2t.stockgod.utils.HttpUtil;
import cn.chinat2t.stockgod.utils.StringUtil;
import cn.chinat2t.stockgod.utils.TimerManager;
import cn.chinat2t.stockgod.utils.TimerManager.TimerListener;
import cn.chinat2t.stockgod.utils.ViewUtil;
import cn.chinat2t.stockgod.views.MACandleStickChart;
import cn.chinat2t.stockgod.views.StickChart;

/**
 * 收益闪电对决
 * @author Administrator
 * Dear Samantha,
 * Thanks for your message.
 */
public class FastFightActivity extends Activity implements OnClickListener {

	public final static int FIGHT_EYE = 1;
	public final static int FIGHT_WAITING = 2;
	public final static int FIGHT_JU = 3;
	public final static int FIGHT_PLAYER = 4;
	public final static int FIGHT_GET_RESULT = 5;
	public final static int FIGHT_GET_PLAYER_SHOUYI = 6;
	
	public static final int GAME_WIN = 1;
	public static final int GAME_VAERAGE = 2;
	public static final int GAME_LOSE = 3;
	
	public int gameTimeLenght = 90;
	public final int startIndex = 50;
	
	private Button sellButton;
	private TextView myLvTV;
	private TextView yourLvTV;
	private TextView nowGuJiaTV;
	private TextView yourName;
	private ImageView playerPhoto;
	private ImageView myPhoto;
	
//	private List<OHLCEntity> ohlc = new ArrayList<OHLCEntity>();
	private List<KlineData> lineList = new ArrayList<KlineData>();
	private List<KlineData> lineList2 = new ArrayList<KlineData>();
	private MACandleStickChart macandlestickchart;
	private StickChart stickchart;
	
	private TimerManager waitingTimer;	
	private TimerManager gameTimer;
	private UserBean userMain;
	private UserBean userPlayer;
	private FightJuBean gameBean;
	private int saizhiID;
	private int roomID;
	private int juID;
	private int gameStockIndex;
	private double currentStockPrice;
	private double initJine;
	private boolean keepAgain = true;
	public  static  int gameStatus;
	public  static  int GAME_NOT_START = 0;
	public  static  int GAME_READY = 1;
	public  static  int GAME_ONGOING = 2;
	public  static  int GAME_OVER = 3;
	private boolean isForceQuit;
	private boolean isPlayerForceQuit;
	private boolean isForceOutSide;
	private View gameView;
	private View gameIntroduceView;
	private ImageView num2;
	private ImageView num3;
	private ImageView num4;
	private TextView myName;
	private TextView lastSellPriceTV;
	private TextView gameDesc;
	private LinearLayout resultLayou;
	private LinearLayout gameLayou;
	private TextView lastSellTV;
	private Dialog newLoading;
	private TextView myResultName;
	private TextView yourResultName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.fastfight_layout);
		roomID = getIntent().getIntExtra("roomId", 1);
		saizhiID = getIntent().getIntExtra("saizhiID", 1);
		
		
		userMain = UserBean.getInstance();
		userMain.gameGushu = 0;
		userMain.gameJine = 0;
		userMain.gameShouyi = 0;
		userPlayer = UserBean.getNewInstance();
		initViews();
		
		gameStatus = GAME_NOT_START;
		MainActivity.setTitleName("闪电对决");
		if(HttpUtil.isConnection(this)){
			// 1,  开启游戏局
			CommunicationManager.getInstance().getFightJu(userMain.uid, roomID, saizhiID, getJuListener);
		}else{
			ViewUtil.showDialog(getParent(), "网络连接不可用", null);
		}
	}
	
	private void initViews(){
		sellButton = (Button) findViewById(R.id.fight_sell_button);
		num2 = (ImageView)findViewById(R.id.first_2_num);
		num3 = (ImageView)findViewById(R.id.first_3_num);
		num4 = (ImageView)findViewById(R.id.first_4_num);
		myLvTV = (TextView) findViewById(R.id.fight_my_shouyi);
		yourLvTV = (TextView) findViewById(R.id.fight_player_shouyi);
		nowGuJiaTV = (TextView) findViewById(R.id.fight_current_stock_price);
		lastSellPriceTV = (TextView) findViewById(R.id.fight_last_sell_price);
		lastSellTV = (TextView) findViewById(R.id.fight_textView12);
		yourName = (TextView) findViewById(R.id.fight_your_name);
		myName = (TextView) findViewById(R.id.fight_my_name);
		myResultName = (TextView) findViewById(R.id.result_my_id);
		yourResultName = (TextView) findViewById(R.id.result_your_id);
		gameDesc = (TextView) findViewById(R.id.fight_game_descri);
		gameDesc.setText(String.format(gameDesc.getText().toString(), (roomID - 1) * 100));
		myName.setText(userMain.nickname);
		resultLayou = (LinearLayout) findViewById(R.id.fight_result_layout);
		gameLayou = (LinearLayout) findViewById(R.id.linearLayout4);
		playerPhoto = (ImageView) findViewById(R.id.fight_your_pic);
		myPhoto = (ImageView) findViewById(R.id.fight_my_pic);
		macandlestickchart = (MACandleStickChart) findViewById(R.id.kline_stock_tu1);
		stickchart = (StickChart) findViewById(R.id.kline_stock_tu2);
	
		gameView = findViewById(R.id.fight_game_layout);
		gameIntroduceView = findViewById(R.id.fight_game_introduce_layout);
		sellButton.setOnClickListener(this);
		
		initMACandleStickChart();
	}
	
	private void initMACandleStickChart() {
		macandlestickchart.setShowCandleSticks(true);
		macandlestickchart.setLatitudeNum(4);
		macandlestickchart.setLongtitudeNum(4);
		macandlestickchart.setBackgroudColor(Color.TRANSPARENT);
		macandlestickchart.setDisplayPopMune(false);
		macandlestickchart.setDisplayAxisXTitle(false);
		
		stickchart.setLatitudeNum(2);
		stickchart.setLongtitudeNum(4);
		stickchart.setDisplayAxisXTitle(false);
		stickchart.setBackgroudColor(Color.TRANSPARENT);
		
		macandlestickchart.notifyEvent(stickchart);
	}
	
	private TimerListener gameListener = new TimerListener() {

		@Override
		public void start() {
			// 刷新对手的形象
			updatePlayerInfo();
			gameStatus = GAME_ONGOING;
			System.out.println("--------------------开始游戏-----------------------");
		}
		
		@Override
		public void pause() {
		}

		@Override
		public void finish() {
			System.out.println("--------------------游戏结束-----------------------");
			if(gameStatus == GAME_ONGOING){
				dealResult();
			}
		}

		@Override
		public void update(int time) {
			if(gameStatus == GAME_ONGOING) {
				System.out.println("--------------------游戏界面更新-----------------------time: " + time);
				gameStockIndex++;
				// 刷新股票数据
				lineList2.clear();
				updateStockData();
				// 刷新游戏数据界面
				updateGameData(time);
				if(time % 3 == 0){
					// 获取对方收益
					System.out.println("--------------------获取对方收益请求-----------------------times: " + time/3);
					CommunicationManager.getInstance().getPlayerShouYiLv(juID, userMain.uid, 
							userMain.gameShouyi, userPlayer.uid, getPlayerShouYiLvListener);
				}
			}
		}

	};
	
	private int[] imgRes = {R.drawable.num_0, R.drawable.num_1, R.drawable.num_2, R.drawable.num_3, R.drawable.num_4, 
			R.drawable.num_5, R.drawable.num_6, R.drawable.num_7, R.drawable.num_8, R.drawable.num_9 };
	
	private void updateGameData(int time) {
		currentStockPrice = gameBean.klineList.get(gameStockIndex + startIndex).close;
		String timeStr = StringUtil.formatTime(gameTimeLenght - time);
		num2.setBackgroundResource(imgRes[timeStr.charAt(1) - 48]);
		num3.setBackgroundResource(imgRes[timeStr.charAt(3) - 48]);
		num4.setBackgroundResource(imgRes[timeStr.charAt(4) - 48]);
		nowGuJiaTV.setText(currentStockPrice + "");
//		lastGuJiaTV.setText(lastSellPrice + "");
		if(userMain.gameGushu != 0){
			userMain.gameJine = currentStockPrice * userMain.gameGushu;
		}
	}
	
	private void updateStockData() {
		for (int i = gameStockIndex; (i < gameStockIndex + startIndex) && (i < lineList.size()); i++) {
			lineList2.add(lineList.get(i));
		}
		int length = lineList.size() > startIndex ? startIndex : lineList.size();
		macandlestickchart.setMaxCandleSticksNum(length);
		macandlestickchart.setLineData(getLineEntityList()); // 设置MA均线
		macandlestickchart.setOHLCData(lineList2);           // 蜡烛图样的数值
		macandlestickchart.invalidate();                     // 更新视图
		
		stickchart.setMaxStickDataNum(length);
		stickchart.setStickData(lineList2);
		stickchart.invalidate();
	}
	
	private TimerListener waitingTimeLister = new TimerListener() {
		
		@Override
		public void start() {
			System.out.println("--------------------开始等待玩家-----------------------");
			gameStatus = GAME_READY;  // 游戏进入准备状态
		}
		
		@Override
		public void pause() {}
		
		@Override
		public void finish() {
			System.out.println("--------------------停止等待玩家-----------------------");
			// 通知后台删除该局
			System.out.println("--------------------通知后台删除该局-----------------------");
			if(gameStatus == GAME_READY){
				CommunicationManager.getInstance().getPlayer(saizhiID, roomID, userMain.uid, juID, 0, getPlayerListener);
			}
		}

		@Override
		public void update(int time) {
			if(keepAgain){
				// 第一次匹配之后才能进行第二次匹配
				keepAgain = false;
				// 3，轮询向服务器匹配玩家
				System.out.println("--------------------继续等待玩家-----------------------times: " + time);
				CommunicationManager.getInstance().waitingPlayer(juID, waitingListener);
			}
		}

	};
	
	class ImageAsyncTask extends AsyncTask<String, Integer, Bitmap>{
		
		private View view;

		public ImageAsyncTask(View view) {
			this.view = view;
		}
		@Override
		protected Bitmap doInBackground(String... params) {
			return ViewUtil.downloadBitmap(params[0]);
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			if(result != null){
				view.setBackgroundDrawable(new BitmapDrawable(result));
			}else{
				view.setBackgroundResource(R.drawable.user_men);
			}
		}
	}

	Handler mHandler = new Handler(){

		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case FIGHT_WAITING:
				int status = (Integer) msg.obj;
				if (status == 1) {
					System.out.println("--------------------匹配对手成功-----------------------");
					keepAgain = false;
					// 匹配对手成功，等待线程结束
					waitingTimer.pause();
					// 获取对手信息
					System.out.println("--------------------请求获取游戏及对手的信息-----------------------");
					CommunicationManager.getInstance().getPlayer(saizhiID, roomID, userMain.uid, juID, status, getPlayerListener);
				} else {
					// 匹配失败，继续下一次轮询匹配
					System.out.println("--------------------匹配失败，继续下一次轮询匹配-----------------------");
					keepAgain = true;
				}
				break;
				
			case FIGHT_JU:
				System.out.println("-----------------创局成功，创建循环线程开始等待玩家进入----------------------");
				juID = (Integer) msg.obj;
				//  2，创局成功，创建循环线程开始等待玩家进入
				waitingTimer = new TimerManager(1000, 3);
				waitingTimer.setUnLimit(true);
				waitingTimer.setTimerListener(waitingTimeLister);
				waitingTimer.start();
				break;
			case FIGHT_PLAYER:
				System.out.println("-----------------请求获取游戏及对手的信息-成功----------------------");
				gameBean = (FightJuBean) msg.obj;
				lineList = gameBean.klineList;
				userPlayer.uid = gameBean.yourUid;
				userPlayer.pic = gameBean.yourPic;
				userPlayer.nickname = gameBean.yourNickname;
				initJine = gameBean.csje;
				userMain.gameJine = gameBean.csje;
				userMain.pic = gameBean.myPic;
				gameTimeLenght = gameBean.yasuo;;
				currentStockPrice = gameBean.klineList.get(gameStockIndex + startIndex).close;
				new ImageAsyncTask(myPhoto).execute(userMain.pic);
				new ImageAsyncTask(playerPhoto).execute(userPlayer.pic);
				// 显示游戏界面
				gameIntroduceView.setVisibility(View.GONE);
				gameView.setVisibility(View.VISIBLE);
				// 启动游戏线程
				System.out.println("-----------------启动游戏线程----------------------");
				gameTimer = new TimerManager(1000, gameTimeLenght);
				gameTimer.setTimerListener(gameListener);
				gameTimer.start();
				break;
			case FIGHT_GET_RESULT:
				System.out.println("-----------------获得比赛结果----------------------");
			    GameResultBean bean = (GameResultBean) msg.obj;
			    if(bean.type == 1){
					gameStatus = GAME_OVER;
			    	System.out.println("竞技结果：" + bean.suode);
			    	showGameResult(bean);
			    }else if(gameStatus != GAME_OVER){
			    	new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							// 继续请求游戏结果
							CommunicationManager.getInstance().getResultOfFight(juID, userMain, userPlayer, getResultLisener);
						}
					}, 1000);
			    }
				break;
			case FIGHT_GET_PLAYER_SHOUYI:
				if(gameStatus == GAME_ONGOING){
					System.out.println("-----------------获得对方收益结果----------------------");
					userPlayer.gameShouyi = (Double) msg.obj;
					System.out.println("对手的收益：" + userPlayer.gameShouyi);
					System.out.println("自己的收益：" + userMain.gameShouyi);
					if(userPlayer.gameShouyi <= -200){
						userPlayer.gameShouyi = -200;
						isPlayerForceQuit = true;
						
						System.out.println("-----------------对方已强退----------------------");
						dealResult();
					}
					myLvTV.setText(userMain.gameShouyi + "%");
					if(userPlayer.gameShouyi > -200)
						yourLvTV.setText(userPlayer.gameShouyi + "%");
				}

				break;
			}
		}

	};

	private double getShouYiLv(double currentJine){
		if(initJine == 0) return 0;
		BigDecimal b = new BigDecimal((currentJine - initJine) / initJine * 100);
		userMain.gameShouyi = b.setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
		if(userMain.gameShouyi == 0) return 0;
		return userMain.gameShouyi;
	}
	
	private void updatePlayerInfo() {
//		playerPhoto.setImageResource(R.drawable.user_women);
		yourName.setText(userPlayer.nickname);
	}
	
	private double lastSellPrice;
	private double lastBuyPrice;
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.fight_sell_button:
			if(gameStatus == GAME_OVER) return;
			if("买入".equals(sellButton.getText().toString())){
				userMain.gameGushu = (int) (userMain.gameJine / currentStockPrice);
				sellButton.setText("卖出");
				sellButton.setBackgroundResource(R.drawable.btn_blue_active);
				lastSellTV.setText("成本价:");
				lastSellPriceTV.setText(lastSellPrice + "");
				lastBuyPrice = currentStockPrice;
			} else{
				userMain.gameJine = userMain.gameGushu * currentStockPrice;
				getShouYiLv(userMain.gameJine);
				userMain.gameGushu = 0;
				lastSellPrice = currentStockPrice;
				lastSellPriceTV.setText(lastBuyPrice + "");
				myLvTV.setText(userMain.gameShouyi + "%");
				lastSellTV.setText("上次买入价格:");
				sellButton.setText("买入");
				sellButton.setBackgroundResource(R.drawable.btn_yellow_active);
			}
			break;
		}
	}
	
	private void showForceExit(){
		View view = LayoutInflater.from(this).inflate(R.layout.guess_dialog_munu, null);
		TextView tx1 = (TextView) view.findViewById(R.id.textView2);
		TextView tx2 = (TextView) view.findViewById(R.id.textView3);
		Button button = (Button) view.findViewById(R.id.guess_confirm);
		Button chongzhi = (Button) view.findViewById(R.id.guess_chongzhi);
		
		final Dialog showDialog = ViewUtil.showDialog(getParent(), view);
		chongzhi.setVisibility(View.VISIBLE);
		chongzhi.setText("回到比赛");
		chongzhi.setOnClickListener(new OnClickListener() {
				
			@Override
			public void onClick(View v) {
				isForceQuit = false;
				isForceOutSide = false;
				showDialog.cancel();
			}
		});
		button.setText("离开比赛");
		tx1.setText("您正在参加比赛，如果离开比赛要损失" + gameBean.goldf + "个金币，确定要离开比赛么？");
		tx2.setVisibility(View.INVISIBLE);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				System.out.println("-----------------确定强退----------------------");
				showDialog.cancel();
				userMain.gameShouyi = -200;
				gameStatus = GAME_OVER;
				isForceQuit = true;
				dealResult();
//				CommunicationManager.getInstance().getResultOfFight(juID, userMain, userPlayer, getResultLisener);
				if(isForceOutSide){
					CompetitiveGroup.getInstance().back();
					Intent intent = new Intent();
					intent.setClassName("cn.chinat2t.stockgod", "cn.chinat2t.stockgod.activity." + BaseGroup.tag);
					CompetitiveGroup.getInstance().switchActivity(BaseGroup.tag, intent , -1, -1);
				}else{
					onBackPressed();
				}
			}
		});
		showDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				isForceOutSide = false;
			}
		});
	}
	
	/*private void showWaitingLoser(String message) {
		if(gameIsGoing) return;
		View view = LayoutInflater.from(this).inflate(R.layout.guess_dialog_munu, null);
		TextView tx1 = (TextView) view.findViewById(R.id.textView2);
		TextView tx2 = (TextView) view.findViewById(R.id.textView3);
		Button button = (Button) view.findViewById(R.id.guess_confirm);
		Button chongzhi = (Button) view.findViewById(R.id.guess_chongzhi);

		System.out.println("-----------------等待超时，匹配失败----------------------");
		chongzhi.setVisibility(View.VISIBLE);
		chongzhi.setText("退出游戏");
		button.setText("继续等待");
		tx1.setText(message);
		tx2.setVisibility(View.GONE);

		final Dialog showDialog = ViewUtil.showDialog(getParent(), view);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog.cancel();
				// 重新等待
				System.out.println("-----------------开启新的等待线程----------------------");
				waitingTimer = new TimerManager(1000, 3);
				waitingTimer.setUnLimit(true);
				waitingTimer.setTimerListener(waitingTimeLister);
				waitingTimer.start();
			}
		});
		chongzhi.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog.cancel();
				// 退出游戏界面
				gameIsGoing = false;
				onBackPressed();
				System.out.println("-----------------退出游戏界面----------------------");
			}
		});
	}*/
	
	private CommunicationResultListener waitingListener = new CommunicationResultListener() {

		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED && token > 0){
				Message msg = mHandler.obtainMessage();
				msg.what = result;
				msg.arg1 = FIGHT_WAITING;
				msg.obj = resultData;
				mHandler.sendMessage(msg);
			}else{
				// 4 匹配失败，继续等待
				keepAgain = true;
			}
		};
	};
	
	private CommunicationResultListener getResultLisener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED && token > 0){
				Message msg = mHandler.obtainMessage();
				msg.what = result;
				msg.arg1 = FIGHT_GET_RESULT;
				msg.obj = resultData;
				mHandler.sendMessage(msg);
				System.out.println("----------------getResultLisener succuss--------------");
			} else {
				System.out.println("----------------getResultLisener failure--------------");
			}
			/*else{
				ViewUtil.showToast((String)resultData, FastFightActivity.this);
			}*/
		};
	};
	
	private CommunicationResultListener getPlayerShouYiLvListener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED && token > 0){
				Message msg = mHandler.obtainMessage();
				msg.what = result;
				msg.arg1 = FIGHT_GET_PLAYER_SHOUYI;
				msg.obj = resultData;
				mHandler.sendMessage(msg);
			}/*else{
				ViewUtil.showToast((String)resultData, FastFightActivity.this);
			}*/
		};
	};
	
	private CommunicationResultListener getPlayerListener = new CommunicationResultListener() {

		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED && token > 0){
				Message msg = mHandler.obtainMessage();
				msg.what = result;
				msg.arg1 = FIGHT_PLAYER;
				msg.obj = resultData;
				mHandler.sendMessage(msg);
			} /*else {
				ViewUtil.showToast((String)resultData, FastFightActivity.this);
			}*/
		};
	};
	
	private CommunicationResultListener getJuListener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED && token > 0){
				Message msg = mHandler.obtainMessage();
				msg.what = result;
				msg.arg1 = FIGHT_JU;
				msg.obj = resultData;
				mHandler.sendMessage(msg);
			}/*else{
				ViewUtil.showToast((String)resultData, FastFightActivity.this);
			}*/
		};
	};
	
	private void showGameResult(GameResultBean bean) {
		if(isForceQuit) return;
		ViewUtil.dismiss(newLoading);
		resultLayou.setVisibility(View.VISIBLE);
		gameLayou.setVisibility(View.GONE);
		TextView myId = (TextView) resultLayou.findViewById(R.id.result_my_id);
		TextView yourId = (TextView) resultLayou.findViewById(R.id.result_your_id);
		TextView myShouyi = (TextView) resultLayou.findViewById(R.id.result_my_shouyi);
		TextView yourShouyi = (TextView) resultLayou.findViewById(R.id.result_your_shouyi);
		TextView goldLoser = (TextView) resultLayou.findViewById(R.id.fight_gold_loser);
		TextView goldGet = (TextView) resultLayou.findViewById(R.id.fight_gold_get);
		TextView fundGet = (TextView) resultLayou.findViewById(R.id.fight_fund_get);
		TextView jiangGet = (TextView) resultLayou.findViewById(R.id.fight_jiang_loser);
		TextView goldName = (TextView) resultLayou.findViewById(R.id.fight_gold_name);
//		TextView fundName = (TextView) resultLayou.findViewById(R.id.fight_fund_name);
//		TextView jiangName = (TextView) resultLayou.findViewById(R.id.fight_jiang_name);
		View myImage = resultLayou.findViewById(R.id.result_my_image);
		View yourImage = resultLayou.findViewById(R.id.result_your_image);
		myShouyi.setText(userMain.gameShouyi + "%");
		yourShouyi.setText(userPlayer.gameShouyi + "%");
		myId.setText(userMain.nickname);
		yourId.setText(userPlayer.nickname);
		goldLoser.setText((roomID - 1) * 100 + "");
		int type = bean.jiegou;
		if(isPlayerForceQuit){
			userMain.gold = userMain.gold + bean.suode;
			myImage.setBackgroundResource(R.drawable.saiguo_win);
			yourImage.setBackgroundResource(R.drawable.saiguo_false);
			yourShouyi.setText("对方强退");
		}else if(isForceQuit){
			userMain.gold = userMain.gold - bean.suode;
			myImage.setBackgroundResource(R.drawable.saiguo_false);
			yourImage.setBackgroundResource(R.drawable.saiguo_win);
			myShouyi.setText("您强退");
			goldName.setText("失去：");
			myResultName.setTextColor(getResources().getColor(R.color.green));
			myShouyi.setTextColor(getResources().getColor(R.color.green));
			yourResultName.setTextColor(getResources().getColor(R.color.red));
			yourShouyi.setTextColor(getResources().getColor(R.color.red));
		}else if(type == GAME_WIN){
			goldName.setText("得到：");
			userMain.gold = userMain.gold + bean.suode;
			myImage.setBackgroundResource(R.drawable.saiguo_win);
			yourImage.setBackgroundResource(R.drawable.saiguo_false);
			myResultName.setTextColor(getResources().getColor(R.color.red));
			myShouyi.setTextColor(getResources().getColor(R.color.red));
			yourResultName.setTextColor(getResources().getColor(R.color.green));
			yourShouyi.setTextColor(getResources().getColor(R.color.green));
		}else if(type == GAME_LOSE){
			userMain.gold = userMain.gold - bean.suode;
			myImage.setBackgroundResource(R.drawable.saiguo_false);
			yourImage.setBackgroundResource(R.drawable.saiguo_win);
			goldName.setText("失去：");
//			fundName.setText("得到：");
//			jiangName.setText("得到：");
			myResultName.setTextColor(getResources().getColor(R.color.green));
			myShouyi.setTextColor(getResources().getColor(R.color.green));
			yourResultName.setTextColor(getResources().getColor(R.color.red));
			yourShouyi.setTextColor(getResources().getColor(R.color.red));
		}else if(type == GAME_VAERAGE){
			goldName.setText("失去：");
			myImage.setBackgroundResource(R.drawable.saiguo_ping);
			yourImage.setBackgroundResource(R.drawable.saiguo_ping);
			myResultName.setTextColor(getResources().getColor(R.color.white));
			myShouyi.setTextColor(getResources().getColor(R.color.white));
			yourResultName.setTextColor(getResources().getColor(R.color.white));
			yourShouyi.setTextColor(getResources().getColor(R.color.white));
		}
		goldGet.setText(bean.shouyi + "");
		fundGet.setText(bean.dianjuan + "");
		jiangGet.setText(bean.dianjuan + "");
		resultLayou.findViewById(R.id.button1).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(!isForceQuit)
					onBackPressed();
			}
		});
		sendBroadcast(new Intent(MainActivity.UPDATE_USER_INFOR));
	}
	
	private List<LineEntity> getLineEntityList() {
		List<LineEntity> lines = new ArrayList<LineEntity>();

		// 计算5日均线
		LineEntity MA5 = new LineEntity();
		MA5.setTitle("MA5");
		MA5.setLineColor(Color.WHITE);
		MA5.setLineData(DataUtil.initMA(5, lineList2));
		lines.add(MA5);

		// 计算10日均线
		LineEntity MA10 = new LineEntity();
		MA10.setTitle("MA10");
		MA10.setLineColor(Color.YELLOW);
		MA10.setLineData(DataUtil.initMA(10, lineList2));
		lines.add(MA10);

		// 计算25日均线
		LineEntity MA20 = new LineEntity();
		MA20.setTitle("MA20");
		MA20.setLineColor(Color.parseColor("#ffCB61B9"));
		MA20.setLineData(DataUtil.initMA(20, lineList2));
		lines.add(MA20);
		return lines;
	}

	@Override
	public void onBackPressed() {
		if(gameStatus == GAME_ONGOING){
			showForceExit();
			System.out.println("-----------------弹出强退界面----------------------");
		}else{
			if(waitingTimer != null){
				waitingTimer.finish();
			}
			CompetitiveGroup.getInstance().back();
			System.out.println("-----------------back键退出游戏----------------------");
		}
	}
	
	/*@Override
	protected void onStop() {
		super.onStop();
		if(gameStatus == GAME_ONGOING){
			showForceExit();
			isForceOutSide = true;
			System.out.println("****************-----------------游戏被强退----------****************");
		}else{
			if(waitingTimer != null){
				waitingTimer.finish();
			}
		}
		isForceOutSide = false;
		if(gameStatus == GAME_ONGOING){
			userMain.gameShouyi = -200;
			isForceQuit = true;
			System.out.println("-----------------游戏终止，获取结果----------------------");
			dealResult();
		}else{
			if(waitingTimer != null){
				waitingTimer.finish();
			}
		}
	}*/

	/** 处理结果*/
	private void dealResult() {
		System.out.println("-----------------处理结果----------------------");
		if(gameTimer != null)
			gameTimer.pause();
		System.out.println("-----------------发送收益率到服务器----------------------");
		CommunicationManager.getInstance().getPlayerShouYiLv(juID, userMain.uid, 
				userMain.gameShouyi, userPlayer.uid, getPlayerShouYiLvListener);
		if(!isForceQuit){
			if(!(newLoading != null && newLoading.isShowing())){
				newLoading = ViewUtil.showNewLoading(getParent());
				((ProgressDialog)newLoading).setMessage("正在计算结果，请稍后...");
			}
		}
		CommunicationManager.getInstance().getResultOfFight(juID, userMain, userPlayer, getResultLisener);
	}
	
	@Override
	public void onContentChanged() {
		MainActivity.setTitleName("闪电对决");
	};
	
	@Override
	public void onDetachedFromWindow() {
		System.out.println("****************-----------------onDetachedFromWindow----------****************");
		if(gameStatus == GAME_ONGOING){
			showForceExit();
			isForceOutSide = true;
			System.out.println("****************-----------------游戏被强退----------****************");
		}else{
			if(waitingTimer != null){
				waitingTimer.finish();
			}
		}
	}
	
}
