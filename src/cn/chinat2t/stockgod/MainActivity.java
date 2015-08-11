package cn.chinat2t.stockgod;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.TabHost;
import android.widget.TextView;
import cn.chinat2t.stockgod.activity.CompetitiveActivity;
import cn.chinat2t.stockgod.activity.CompetitiveGroup;
import cn.chinat2t.stockgod.activity.FriendActivity;
import cn.chinat2t.stockgod.activity.HelpActivity;
import cn.chinat2t.stockgod.activity.LaunchActivity;
import cn.chinat2t.stockgod.activity.MyInforActivity;
import cn.chinat2t.stockgod.activity.NewsActivity;
import cn.chinat2t.stockgod.activity.RankActivity;
import cn.chinat2t.stockgod.bean.UserBean;
import cn.chinat2t.stockgod.http.CommunicationManager;
import cn.chinat2t.stockgod.http.CommunicationResultListener;
import cn.chinat2t.stockgod.utils.PreferenceManager;
import cn.chinat2t.stockgod.utils.StringUtil;
import cn.chinat2t.stockgod.utils.ViewUtil;

public class MainActivity extends TabActivity implements OnClickListener{

	public static final int USER_INFO = 12;
	private TabHost tabHost;
	private RadioButton button0,button1,button2,button3;
	private Intent mStockIntent;  // 主界面
	private Intent mHelpIntent;   // 帮助 
	private Intent mFriendIntent; // 好友
	private Intent mRankIntent;   // 排行榜
	private Intent mNewsIntent;   // 消息 
	private Intent intent;
	public final String TAB_STOCK = "StockActivity";
	public final String TAB_HELP = "HelpActivity";
	public final String TAB_FRIEND = "FriendActivity";
	public final String TAB_RANK = "RankActivity";
	public final String TAB_NEWS = "NewsActivity";

	public final static String USER_QUIT = "cn.chinat2t.stockgod.user_quit";
	public final static String APP_QUIT = "cn.chinat2t.stockgod.app_quit";
	public final static String UPDATE_USER_INFOR = "cn.chinat2t.stockgod.update_user_infor";
	public final static String UPDATE_TASK = "cn.chinat2t.stockgod.update_task";
	public final static String UPDATE_NEW_MESSAGE = "cn.chinat2t.stockgod.update_new_message";

	public static final String TAB_INDEX = "tab_index";
	public static final int TAB_STOCK_INDEX = 0;
	public static final int TAB_HELP_INDEX = 1;
	public static final int TAB_FRIEND_INDEX = 2;
	public static final int TAB_RANK_INDEX = 3;
	public static final int TAB_NEWS_INDEX = 4;
	public static TextView title;
	private static Button rightButton;
	private static Button leftButton;
	private Button userLevel;
	private TextView userName;
	private ProgressBar progressBar;
	private TextView jiangdu;
	private TextView jq;
	private TextView gold;
	private TextView zijin;
	private TextView userVip;
	public static RelativeLayout userInfo;
	public static TextView headToast;
	public UserBean user;
	public static MainActivity intance;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        intance = this;
        this.tabHost = getTabHost();

        user = UserBean.getInstance();
        initViews();
        
        prepareIntent();
		setupIntent();
		
		updateUserInfo(user);
		updateTastInfo();
		updateBroadcast();
		
		IntentFilter filter = new IntentFilter(UPDATE_USER_INFOR);
		filter.addAction(USER_QUIT);
		filter.addAction(APP_QUIT);
		registerReceiver(broadcastReceiver, filter);
    }
    
    
    public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if(UPDATE_USER_INFOR.equals(intent.getAction())){
				System.out.println("收到通知，发送请求获取新的user");
				CommunicationManager.getInstance().getUserInfo(user.uid, userInforListener);
				updateTastInfo();
				updateBroadcast();
			}else if(USER_QUIT.equals(intent.getAction())){
				PreferenceManager instance = PreferenceManager.getInstance(MainActivity.this);
				instance.putAccount("");
				instance.putPass("");
				instance.putLoginAuto(false);
				instance.putRemember(false);
				startActivity(new Intent(MainActivity.this, LaunchActivity.class));
				MainActivity.this.finish();
			}else if(APP_QUIT.equals(intent.getAction())){
				MainActivity.this.finish();
			}
		}

	};
	
	private CommunicationResultListener userInforListener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED && token > 0){
				Message message = handler.obtainMessage();
				message.arg1 = USER_INFO;
				message.obj = resultData;
				handler.sendMessage(message);
			}
		};
	};

	Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.arg1) {
			case USER_INFO:
				UserBean userBean = (UserBean) msg.obj;
				System.out.println("更新结果: 新的金额：" + userBean.fund);
				updateUserInfo(userBean);
				break;
			}
			return false;
		}
	});
	
    private void initViews(){
    	button0 = (RadioButton) findViewById(R.id.radio_button0);
    	button1 = (RadioButton) findViewById(R.id.radio_button1);
    	button2 = (RadioButton) findViewById(R.id.radio_button2);
    	button3 = (RadioButton) findViewById(R.id.radio_button3);
    	
    	button0.setOnClickListener(this);
    	button1.setOnClickListener(this);
    	button2.setOnClickListener(this);
    	button3.setOnClickListener(this);
    	
    	title = (TextView) findViewById(R.id.title_center_title);
    	rightButton = (Button) findViewById(R.id.title_right_button);
    	leftButton = (Button) findViewById(R.id.title_left_button);
    	rightButton.setOnClickListener(this);
    	leftButton.setOnClickListener(this);
    	
    	userInfo = (RelativeLayout) findViewById(R.id.head_info_layout);
    	userInfo.setOnClickListener(this);
    	userInfo.findViewById(R.id.head_info_vip).setOnClickListener(this);
    	userLevel = (Button) userInfo.findViewById(R.id.head_info_vip);
		userName = (TextView) userInfo.findViewById(R.id.head_info_name);
		userVip = (TextView) userInfo.findViewById(R.id.main_vip_textView);
		progressBar = (ProgressBar) userInfo.findViewById(R.id.head_progress_bar);
		jiangdu = (TextView) userInfo.findViewById(R.id.head_info_jingdu_value);
		jq = (TextView) userInfo.findViewById(R.id.head_info_jq_value);
		gold = (TextView) userInfo.findViewById(R.id.head_info_jb_value);
		zijin = (TextView) userInfo.findViewById(R.id.head_info_zj_value);
    	headToast = (TextView) findViewById(R.id.head_broad_toast);
    }
    
    public static void setTitleName(String name){
    	if(title != null){
    		if("炒股竞技".equals(name)){
    			title.setBackgroundResource(R.drawable.main_title_bg);
    			leftButton.setBackgroundResource(R.drawable.btn_saiguo_selector);
    			title.setText("");
    		}else{
    			leftButton.setBackgroundResource(R.drawable.btn_main_selector);
    			title.setBackgroundResource(R.drawable.title);
    			title.setText(name);
    		}
    	}
    }

	public static void updateTastInfo() {
		if(userInfo != null){
			ImageView taskStatus = (ImageView) userInfo.findViewById(R.id.head_task_status);
			taskStatus.setImageResource(0);
			View jiangStatus = userInfo.findViewById(R.id.head_jiang_layout);
			jiangStatus.setVisibility(View.VISIBLE);
			ImageView jiangType = (ImageView) userInfo.findViewById(R.id.head_jiang_type);
			jiangType.setImageResource(R.drawable.main_foud_icon);
			TextView jiangAmount = (TextView) userInfo.findViewById(R.id.head_jiang_amount);
			jiangAmount.setText("1000");
    		TextView jiangjin = (TextView) userInfo.findViewById(R.id.head_info_jiangjin);
    		jiangjin.setText("奖励 :"+ "");
		}
	}
	
	public static void updateBroadcast(){
		if(headToast != null){
			headToast.setText("欢迎使用大众股神，祝您好运！");
		}
	}
	
	public void updateUserInfo(UserBean user) {
		if (user == null) return;
		if (UserBean.USER_LIN_SHI == user.usertype) {
			userName.setText("临时用户");
		} else {
			userName.setText(user.nickname);
		}
		jiangdu.setText(user.experience + "/" + 100);
		jq.setText(StringUtil.goldView(user.lottery));
		zijin.setText(StringUtil.goldView(user.fund));
		gold.setText(StringUtil.goldView(user.gold));
		userLevel.setText(user.level + "");
		userVip.setText("VIP" + user.vip);
		progressBar.setProgress(20/*user.experience*/);
		UserBean.setUser(user);
	}
    
    @Override
    protected void onDestroy() {
    	super.onDestroy();
    	unregisterReceiver(broadcastReceiver);
    }
    
    private void prepareIntent() {
    	mStockIntent = new Intent(MainActivity.this, CompetitiveGroup.class);
    	mStockIntent.putExtra(TAB_INDEX, TAB_STOCK_INDEX);
    	
    	mHelpIntent = new Intent(MainActivity.this, CompetitiveGroup.class);
    	mHelpIntent.putExtra(TAB_INDEX, TAB_HELP_INDEX);
    	
    	mFriendIntent = new Intent(MainActivity.this, CompetitiveGroup.class);
    	mFriendIntent.putExtra(TAB_INDEX, TAB_FRIEND_INDEX);
    	
    	mRankIntent = new Intent(MainActivity.this, CompetitiveGroup.class);
    	mRankIntent.putExtra(TAB_INDEX, TAB_RANK_INDEX);
    	
    	mNewsIntent = new Intent(MainActivity.this, CompetitiveGroup.class);
    	mNewsIntent.putExtra(TAB_INDEX, TAB_NEWS_INDEX);
	}

	private void setupIntent() {
		tabHost.addTab(buildTabSpec(TAB_STOCK, TAB_STOCK, R.drawable.bottom_bar_friend_active, mStockIntent));
		tabHost.addTab(buildTabSpec(TAB_HELP, TAB_HELP, R.drawable.bottom_bar_friend_active, mHelpIntent));
		tabHost.addTab(buildTabSpec(TAB_FRIEND, TAB_FRIEND, R.drawable.bottom_bar_friend_active, mFriendIntent));
		tabHost.addTab(buildTabSpec(TAB_RANK, TAB_RANK, R.drawable.bottom_bar_friend_active, mRankIntent));
		tabHost.addTab(buildTabSpec(TAB_NEWS, TAB_NEWS, R.drawable.bottom_bar_friend_active, mNewsIntent));
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.radio_button0:
			CompetitiveGroup.getInstance().pop("FriendActivity");
			CompetitiveGroup.getInstance().pop("RankActivity");
			CompetitiveGroup.getInstance().pop("NewsActivity");
			intent = new Intent(this, HelpActivity.class);
			CompetitiveGroup.getInstance().switchActivity("HelpActivity", intent, -1, -1);
			break;

		case R.id.radio_button1:
			if(user.usertype == UserBean.USER_LIN_SHI){
				ViewUtil.showLinShiDialog(this, "您是临时用户,请注册");
				return;
			}
			CompetitiveGroup.getInstance().pop("HelpActivity");
			CompetitiveGroup.getInstance().pop("RankActivity");
			CompetitiveGroup.getInstance().pop("NewsActivity");
			intent = new Intent(this, FriendActivity.class);
			CompetitiveGroup.getInstance().switchActivity("FriendActivity", intent, -1, -1);
			break;
			
		case R.id.radio_button2:
			CompetitiveGroup.getInstance().pop("HelpActivity");
			CompetitiveGroup.getInstance().pop("FriendActivity");
			CompetitiveGroup.getInstance().pop("NewsActivity");
			intent = new Intent(this, RankActivity.class);
			CompetitiveGroup.getInstance().switchActivity("RankActivity", intent, -1, -1);
			break;
			
		case R.id.radio_button3:
			CompetitiveGroup.getInstance().pop("HelpActivity");
			CompetitiveGroup.getInstance().pop("RankActivity");
			CompetitiveGroup.getInstance().pop("FriendActivity");
			intent = new Intent(this, NewsActivity.class);
			CompetitiveGroup.getInstance().switchActivity("NewsActivity", intent, -1, -1);
			break;
		case R.id.title_right_button:
//			intent = new Intent(this, TestKlineActivity.class);
//			CompetitiveGroup.getInstance().switchActivity("TestKlineActivity", intent, -1, -1);
//			Toast.makeText(this, "亲，正在开发哦~~", Toast.LENGTH_SHORT).show();
			break;
		case R.id.title_left_button:
			String name = title.getText().toString();
			if("".equals(name)){
				if(user.usertype == UserBean.USER_LIN_SHI){
					ViewUtil.showLinShiDialog(this, "您是临时用户,请注册");
					return;
				}
				intent = new Intent(this, MyInforActivity.class);
				intent.putExtra("flag", 2);
				CompetitiveGroup.getInstance().switchActivity("MyInforActivity", intent, -1, -1);
			}else{
				intent = new Intent(this, CompetitiveActivity.class);
				CompetitiveGroup.getInstance().switchActivity("CompetitiveActivity", intent, -1, -1);
			}
			break;
		case R.id.head_info_vip:
		case R.id.head_info_layout:
			if(user.usertype == UserBean.USER_LIN_SHI){
				ViewUtil.showLinShiDialog(this, "您是临时用户,请注册");
				return;
			}
			intent = new Intent(this, MyInforActivity.class);
			CompetitiveGroup.getInstance().switchActivity("MyInforActivity", intent, -1, -1);
			break;
		}
		
	}
	
	private TabHost.TabSpec buildTabSpec(String tag, String resLabel, int resID,
			final Intent content) {
		return this.tabHost.newTabSpec(tag).setIndicator(resLabel, getResources().getDrawable(resID))
				.setContent(content);
	}
	
	@Override
	public void onBackPressed() {
		exitApp();
	}
	
	public void exitApp(){
		new AlertDialog.Builder(this).setTitle("确定退出股神").setPositiveButton("确定", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				MainActivity.this.finish();
				System.exit(0);
			}
		}).setNegativeButton("取消", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				
			}
		}).create().show();
	}
	

	private OnActivityResultListener onActivityResultListener;
	
	public interface OnActivityResultListener{
		void onActivityResult(int requestCode, int resultCode, Intent data);
	}
	
	public void setOnActivityResultListener(OnActivityResultListener onActivityResultListener){
		this.onActivityResultListener = onActivityResultListener;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(onActivityResultListener != null){
			onActivityResultListener.onActivityResult(requestCode, resultCode, data);
		}
	}
}
