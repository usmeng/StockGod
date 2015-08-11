package cn.chinat2t.stockgod.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.chinat2t.stockgod.MainActivity;
import cn.chinat2t.stockgod.R;
import cn.chinat2t.stockgod.bean.AccountStockBean;
import cn.chinat2t.stockgod.bean.SelfStockBean;
import cn.chinat2t.stockgod.bean.StockInfoBean;
import cn.chinat2t.stockgod.bean.UserBean;
import cn.chinat2t.stockgod.bean.WeiTuoStockBean;
import cn.chinat2t.stockgod.http.CommunicationManager;
import cn.chinat2t.stockgod.http.CommunicationResultListener;
import cn.chinat2t.stockgod.utils.Constant;
import cn.chinat2t.stockgod.utils.DataUtil;
import cn.chinat2t.stockgod.utils.StringUtil;
import cn.chinat2t.stockgod.utils.TimerManager;
import cn.chinat2t.stockgod.utils.ViewUtil;
import cn.chinat2t.stockgod.utils.TimerManager.TimerListener;

public class SimulationActivity extends Activity implements OnCheckedChangeListener, OnClickListener{

	private static final int KINDS_OF_STOCKS = 0;
	private static final int ACCOUNT_OF_STOCKS = 1;
	private static final int WEITUO_OF_STOCKS = 2;
	private static final int SELF_OF_STOCKS = 3;
	private static final int SEARCH_SELF_OF_STOCKS = 4;
	
	private View marketView;
	private View accountView;
	private View selfChooseView;
	private View weiTuoView;
	private ListView marketListView;
	private ListView weiTuoListView;
	private ListView selfChooseListView;
	private ListView accountListView;
	
	private MarketAdapter mAdapter;
	private WeituoAdapter wAdapter;
	private SelfChooseAdapter selfAdapter;
	private AccountAdapter aAdapter;
	private List<StockInfoBean> marketList;
	private List<AccountStockBean> accountList;
	private List<WeiTuoStockBean> weituoList;
	private List<SelfStockBean> selfList;
	private RadioGroup marketGroup;
	private RadioGroup StockGroup;
	private RadioButton selectedRbtn;
	private boolean isRearch;
	private int flag = 0;
	private int currentPage = 1;
	private int currentPage2 = 1;
	
	private CommunicationManager communicationManager;
	private Dialog showLoading;
	private TimerManager stockUpdater;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.simulation_layout);
		flag = getIntent().getIntExtra("flag", 0);
		communicationManager = CommunicationManager.getInstance();

		user = UserBean.getInstance();
		initData();
		initViews();
		
		stockUpdater = new TimerManager(3000, 30);
		stockUpdater.setUnLimit(true);
		stockUpdater.setTimerListener(new TimerListener() {
			
			@Override
			public void update(int time) {
				refreshAccountStock();
			}
			@Override
			public void start() {}
			@Override
			public void pause() {}
			@Override
			public void finish() {}
		});
		stockUpdater.start();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MainActivity.setTitleName("模拟炒股");
	}

	@Override
	public void onContentChanged() {
		MainActivity.setTitleName("模拟炒股");
	}
	
	private void initData(){
		marketList = new ArrayList<StockInfoBean>();
		weituoList = new ArrayList<WeiTuoStockBean>();
		accountList = new ArrayList<AccountStockBean>();
		selfList = new ArrayList<SelfStockBean>();
	}
	
	private void refreshMarketStock() {
		communicationManager.getKindsOfStock(type, currentPage, new CommunicationResultListener() {
			
			@Override
			public void resultListener(byte result, Object resultData) {
				super.resultListener(result, resultData);
				switch (result) {
				case CommunicationManager.FAIL:
					ViewUtil.dismiss(showLoading);
					break;

				case CommunicationManager.SUCCEED:
					Message msg = handler.obtainMessage();
					msg.obj = resultData;
					msg.what = KINDS_OF_STOCKS;
					handler.sendMessage(msg);
					break;
				}
			}
		});
	}
	
	Handler handler = new Handler(new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.what) {
			case KINDS_OF_STOCKS:
				List<StockInfoBean> addList = (List<StockInfoBean>) msg.obj;
				if(addList.size() == 0){
					textView.setText("没有更多了哦~");
				}else{
					textView.setText("加载更多");
					marketList.addAll(addList);
					mAdapter.setData(marketList);
					mAdapter.notifyDataSetInvalidated();
				}
				progressBar.setVisibility(View.INVISIBLE);
				view.setVisibility(View.VISIBLE);
				break;
				
			case ACCOUNT_OF_STOCKS:
				UserBean user = (UserBean) msg.obj;
				accountZiChan.setText(String.valueOf(user.fund));
				accountZongYingkui.setText(String.valueOf(user.zonge));
				accountShiZhi.setText(String.valueOf(user.stockmoney));
				accountJiaoYi.setText(String.valueOf(user.jiaoyici));
				accountYingkuiLv.setText(DataUtil.getDoubleString(user.yingkuilv) + "%");
				accountList = user.listStock;
				aAdapter.setData(accountList);
				aAdapter.notifyDataSetChanged();
				break;
				
			case SEARCH_SELF_OF_STOCKS:
				List<SelfStockBean> addSelfList = (List<SelfStockBean>) msg.obj;
				if(addSelfList.size() == 0){
					textView2.setText("没有更多了~");
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							textView2.setVisibility(View.GONE);
						}
					}, 1000);
				}else{
					textView2.setText("加载更多");
					textView2.setVisibility(View.VISIBLE);
					selfList.addAll(addSelfList);
					selfAdapter.setData(selfList);
					selfAdapter.notifyDataSetChanged();
				}
				progressBar2.setVisibility(View.INVISIBLE);
				break;
				
			case SELF_OF_STOCKS:
				selfList.clear();
				selfList = (List<SelfStockBean>) msg.obj;
				view2.setVisibility(View.INVISIBLE);
				selfAdapter.setData(selfList);
				selfAdapter.notifyDataSetChanged();
				break;
				
			case WEITUO_OF_STOCKS:
				weituoList = (List<WeiTuoStockBean>) msg.obj;
				wAdapter.setData(weituoList);
				wAdapter.notifyDataSetChanged();
				break;
				
			case -1:
				if(msg.arg1 == SelfStockBean.ADD){
					selfAdapter.notifyDataSetChanged();
				}else{
					selfList.remove((SelfStockBean)msg.obj);
					selfAdapter.setData(selfList);
					selfAdapter.notifyDataSetChanged();
				}
				break;
				
			case -2:
				WeiTuoStockBean bean = (WeiTuoStockBean) msg.obj;
//				ViewUtil.showDialog(getParent(), bean.sname + "撤销成功！", null);
				weituoList.remove(bean);
				wAdapter.setData(weituoList);
				wAdapter.notifyDataSetChanged();
				sendBroadcast(new Intent(MainActivity.UPDATE_USER_INFOR));
				break;
			case -3:
				AccountStockBean accountBean = (AccountStockBean) msg.obj;
				ViewUtil.showToast(accountBean.sname + "已卖出", SimulationActivity.this);
				accountList.remove(accountBean);
//				aAdapter.setData(accountList);
				aAdapter.notifyDataSetChanged();
				break;
			}
			
			ViewUtil.dismiss(showLoading);
			return false;
		}
	});
	private ProgressBar progressBar;
	private View view2;
	private TextView textView2;
	private ProgressBar progressBar2;
	
	private void initViews(){
		StockGroup = ((RadioGroup)this.findViewById(R.id.simulation_menu_layout));
		StockGroup.setOnCheckedChangeListener(this);
		
		accountView = findViewById(R.id.simulation_account_layout);
		accountListView = (ListView) findViewById(R.id.simulation_account_list);
		accountZiChan = (TextView) findViewById(R.id.simulation_account_zc);
		accountZongYingkui = (TextView) findViewById(R.id.simulation_account_yk);
		accountShiZhi = (TextView) findViewById(R.id.simulation_account_sz);
		accountJiaoYi = (TextView) findViewById(R.id.simulation_account_jy);
		accountYingkuiLv = (TextView) findViewById(R.id.simulation_account_ykl);
		findViewById(R.id.account_follow_niuren).setOnClickListener(this);
		aAdapter = new AccountAdapter();
		accountListView.setAdapter(aAdapter);
		accountListView.setOnItemClickListener(accountItemClick);
		
		marketView = findViewById(R.id.simulation_market_layout);
		marketListView = (ListView) findViewById(R.id.simulation_market_list);
		view = LayoutInflater.from(this).inflate(R.layout.loading_more_item, null);
		textView = (TextView) view.findViewById(R.id.loading_text);
		progressBar = (ProgressBar) view.findViewById(R.id.loading_progressBar);
		progressBar.setVisibility(View.INVISIBLE);
		view.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				currentPage++;
				progressBar.setVisibility(View.VISIBLE);
				refreshMarketStock();
			}
		});
		view.setVisibility(View.INVISIBLE);
		marketListView.addFooterView(view);
		mAdapter = new MarketAdapter();
		marketListView.setAdapter(mAdapter);
		marketListView.setOnItemClickListener(marketItemClick);
		marketGroup = (RadioGroup) findViewById(R.id.kind_stock_market);
		marketGroup.setOnCheckedChangeListener(this);
		
		selfChooseView = findViewById(R.id.simulation_self_layout);
		selfChooseListView = (ListView) findViewById(R.id.simulation_self_list);
		view2 = LayoutInflater.from(this).inflate(R.layout.loading_more_item, null);
		textView2 = (TextView) view2.findViewById(R.id.loading_text);
		progressBar2 = (ProgressBar) view2.findViewById(R.id.loading_progressBar);
		progressBar2.setVisibility(View.INVISIBLE);
		view2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				currentPage2++;
				progressBar2.setVisibility(View.VISIBLE);
				searchStock();
			}
		});
		view2.setVisibility(View.INVISIBLE);
		selfChooseListView.addFooterView(view2);
		selfAdapter = new SelfChooseAdapter();
		selfChooseListView.setAdapter(selfAdapter);
		selfChooseListView.setOnItemClickListener(selfChooseItemClick);

		searchBtn = (Button) findViewById(R.id.self_search_button);
		searchValue = (EditText) findViewById(R.id.self_search_value);
		/*searchValue.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) { }
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				currentPage2 = 1;
				if(s.toString().length() == 0){
					isRearch = false;
//					showLoading = ViewUtil.showLoading(getParent());
					view2.setVisibility(View.INVISIBLE);
					communicationManager.getSelfStock(user, selfStockResultListener);
				}else{
					selfList.clear();
					searchStock();
				}
			}
		});*/
		searchBtn.setOnClickListener(this);
		
		weiTuoView = findViewById(R.id.simulation_weituo_layout);
		weiTuoListView = (ListView) findViewById(R.id.simulation_weituo_list);
		wAdapter = new WeituoAdapter();
		weiTuoListView.setAdapter(wAdapter);
		weiTuoListView.setOnItemClickListener(weiTuoItemClick);
		
		if(flag == 1){
			((RadioButton)findViewById(R.id.simulation_radio_button1)).setChecked(true);
//			onCheckedChanged(StockGroup, R.id.simulation_radio_button1);
		} else if(flag == 2) {
			((RadioButton)findViewById(R.id.simulation_radio_button2)).setChecked(true);
//			onCheckedChanged(StockGroup, R.id.simulation_radio_button2);
		}else{
//			((RadioButton)findViewById(R.id.simulation_radio_button0)).setChecked(true);
			onCheckedChanged(StockGroup, R.id.simulation_radio_button0);
		}
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {

		case R.id.self_search_button:
			currentPage2 = 1;
			selfList.clear();
			searchStock();
			break;
			
		case R.id.account_follow_niuren:
			Intent intent = new Intent(SimulationActivity.this, RankActivity.class);
			CompetitiveGroup.getInstance().switchActivity("RankActivity", intent , -1, -1);
			break;
		}
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}

	private void searchStock() {
		String value = searchValue.getText().toString();
		if(value != null && value.length() > 0){
//			showLoading = ViewUtil.showLoading(getParent());
			CommunicationManager.getInstance().getSearchAccountStock(value, 
					user.uid, currentPage2, new CommunicationResultListener() {
				
				@Override
				public void resultListener(byte result, Object resultData) {
					super.resultListener(result, resultData);
					switch (result) {
						case CommunicationManager.FAIL:
							ViewUtil.dismiss(showLoading);
							isRearch = false;
							break;

						case CommunicationManager.SUCCEED:
							Message message = handler.obtainMessage();
							message.what = SEARCH_SELF_OF_STOCKS;
							message.obj = resultData;
							handler.sendMessage(message);
							isRearch = true;
							break;
						}
					}
				}
			);
		}
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {

		RadioButton rBtn = (RadioButton) findViewById(checkedId);
//		if(selectedRbtn != null && rBtn != selectedRbtn){
//			selectedRbtn.setTextColor(Color.WHITE);
//		}
		selectedRbtn = rBtn;
//		selectedRbtn.setTextColor(Color.BLACK);
		switch (group.getId()) {
		case R.id.simulation_menu_layout:
			isRearch = false;
			if(checkedId == R.id.simulation_radio_button0){         //市场
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(rBtn.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				showMarket();
//				refreshMarketStock(1);
			}else if(checkedId == R.id.simulation_radio_button1){   //帐户
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(rBtn.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				if(user.usertype == UserBean.USER_LIN_SHI){
					ViewUtil.showLinShiDialog(getParent(), "您是临时用户,请注册");
					((RadioButton)findViewById(R.id.simulation_radio_button0)).setChecked(true);
					return;
				}
				showAccount();
				showLoading = ViewUtil.showLoading(getParent());
				refreshAccountStock();
			} else if(checkedId == R.id.simulation_radio_button2){  //自选
				if(user.usertype == UserBean.USER_LIN_SHI){
					ViewUtil.showLinShiDialog(getParent(), "您是临时用户,请注册");
					((RadioButton)findViewById(R.id.simulation_radio_button0)).setChecked(true);
					return;
				}
				showSelfChoose();
				refreshSelfStock();
			} else if(checkedId == R.id.simulation_radio_button3){  //委托
				InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
				imm.hideSoftInputFromWindow(rBtn.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
				if(user.usertype == UserBean.USER_LIN_SHI){
					ViewUtil.showLinShiDialog(getParent(), "您是临时用户,请注册");
					((RadioButton)findViewById(R.id.simulation_radio_button0)).setChecked(true);
					return;
				}
				showWeiTuo();
				refreshWeiTuoStock();
			}
			break;
		case R.id.kind_stock_market:
			currentPage = 1;
			view.setVisibility(View.INVISIBLE);
			if(marketList != null && marketList.size() > 0)
				marketList.clear();
			if(checkedId == R.id.simulation_market_radio1){
				type = 1;
			} else if(checkedId == R.id.simulation_market_radio2){
				type = 2;
			} else if(checkedId == R.id.simulation_market_radio3){  
				type = 3;
			} else if(checkedId == R.id.simulation_market_radio4){ 
				type = 4;
			} else if(checkedId == R.id.simulation_market_radio5){ 
				type = 5;
			}
			showLoading = ViewUtil.showLoading(getParent());
			refreshMarketStock();
			break;
		}
	}

	private void refreshWeiTuoStock() {
		showLoading = ViewUtil.showLoading(getParent());
		communicationManager.getWeiTuoStock(user, new CommunicationResultListener() {
			
			@Override
			public void resultListener(byte result, Object resultData) {
				super.resultListener(result, resultData);
				switch (result) {
				case CommunicationManager.FAIL:
					ViewUtil.dismiss(showLoading);
					break;

				case CommunicationManager.SUCCEED:
					Message message = handler.obtainMessage();
					message.what = WEITUO_OF_STOCKS;
					message.obj = resultData;
					handler.sendMessage(message);
					break;
				}
			}
		
		});
	}
	
	private CommunicationResultListener selfStockResultListener = new CommunicationResultListener() {
		
		@Override
		public void resultListener(byte result, Object resultData) {
			super.resultListener(result, resultData);
			switch (result) {
			case CommunicationManager.FAIL:
				ViewUtil.dismiss(showLoading);
//				ViewUtil.showToast((String)resultData, SimulationActivity.this);
				break;

			case CommunicationManager.SUCCEED:
				Message message = handler.obtainMessage();
				message.what = SELF_OF_STOCKS;
				message.obj = resultData;
				handler.sendMessage(message);
				break;
			}
		}
	};
	
	private void refreshSelfStock() {
		showLoading = ViewUtil.showLoading(getParent());
		communicationManager.getSelfStock(user, selfStockResultListener);
	}

	private void refreshAccountStock() {
		communicationManager.getAccountStock(user, new CommunicationResultListener() {
			
			@Override
			public void resultListener(byte result, Object resultData) {
				super.resultListener(result, resultData);
				switch (result) {
				case CommunicationManager.FAIL:
					ViewUtil.dismiss(showLoading);
//					ViewUtil.showToast((String)resultData, SimulationActivity.this);
					break;

				case CommunicationManager.SUCCEED:
					Message message = handler.obtainMessage();
					message.what = ACCOUNT_OF_STOCKS;
					message.obj = resultData;
					handler.sendMessage(message);
					break;
				}
			}
		
		});
	}

	private void showAccount(){
		marketView.setVisibility(View.GONE);
		selfChooseView.setVisibility(View.GONE);
		accountView.setVisibility(View.VISIBLE);
		weiTuoView.setVisibility(View.GONE);
	}
	
	private void showMarket(){
		marketView.setVisibility(View.VISIBLE);
		weiTuoView.setVisibility(View.GONE);
		selfChooseView.setVisibility(View.GONE);
		accountView.setVisibility(View.GONE);
		((RadioButton)this.findViewById(R.id.simulation_market_radio1)).setChecked(true);
		
		mAdapter.setData(marketList);
		mAdapter.notifyDataSetChanged();
	}
	
	private void showWeiTuo(){
		marketView.setVisibility(View.GONE);
		selfChooseView.setVisibility(View.GONE);
		accountView.setVisibility(View.GONE);
		weiTuoView.setVisibility(View.VISIBLE);
	}
	
	private void showSelfChoose(){
		if(searchValue.getText().toString().length() > 0)
			searchValue.setText("");
		marketView.setVisibility(View.GONE);
		selfChooseView.setVisibility(View.VISIBLE);
		weiTuoView.setVisibility(View.GONE);
		accountView.setVisibility(View.GONE);
	}
	
	class MarketAdapter extends BaseAdapter{

		private List<StockInfoBean> mList = null;
		
		public void setData(List<StockInfoBean> mList){
			this.mList = mList;
		}
		
		@Override
		public int getCount() {
			if(mList != null) return mList.size();
			return 0;
		}

		@Override
		public Object getItem(int arg0) {
			return mList.get(arg0);
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			MarketHolder holder = null;
			if(arg1 == null){
				arg1 = View.inflate(SimulationActivity.this, R.layout.market_listitem_layout, null);
				holder = new MarketHolder();
				holder.tv1 = (TextView) arg1.findViewById(R.id.listitem_text1);
				holder.tv2 = (TextView) arg1.findViewById(R.id.listitem_text2);
				holder.tv3 = (TextView) arg1.findViewById(R.id.listitem_text3);
				holder.tv4 = (TextView) arg1.findViewById(R.id.listitem_text4);
				holder.tv5 = (TextView) arg1.findViewById(R.id.listitem_text5);
				arg1.setTag(holder);
			} else {
				holder = (MarketHolder) arg1.getTag();
			}
			StockInfoBean info = mList.get(arg0);
			holder.tv1.setText(info.sname);
			holder.tv2.setText(info.code);
			holder.tv3.setText(DataUtil.getDoubleString(info.closes));
			if(info.zhangfu > 0){
				holder.tv4.setTextColor(Color.RED);
				holder.tv5.setTextColor(Color.RED);
			} else if(info.zhangfu == 0){
				holder.tv4.setTextColor(Color.WHITE);
				holder.tv5.setTextColor(Color.WHITE);
			} else {
				holder.tv4.setTextColor(Color.GREEN);
				holder.tv5.setTextColor(Color.GREEN);
			}
			if(arg0 % 2 == 0) arg1.setBackgroundColor(getResources().getColor(R.color.itemcolor0));
			else arg1.setBackgroundColor(getResources().getColor(R.color.itemcolor1));
			holder.tv4.setText(DataUtil.getDoubleString(info.zhangfu) + "%");
			holder.tv5.setText(DataUtil.getDoubleString(info.zhangdie));
			return arg1;
		}
		
	}
	
	class SelfChooseAdapter extends BaseAdapter{
		
		private List<SelfStockBean> mList = new ArrayList<SelfStockBean>();
		
		public void setData(List<SelfStockBean> mList){
			this.mList = mList;
		}
		
		@Override
		public int getCount() {
			return mList.size();
		}
		
		@Override
		public Object getItem(int arg0) {
			return arg0;
		}
		
		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		
		@Override
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
			SelfHolder holder = null;
			if(arg1 == null){
				arg1 = View.inflate(SimulationActivity.this, R.layout.self_listitem_layout, null);
				holder = new SelfHolder();
				holder.tv1 = (TextView) arg1.findViewById(R.id.selflistitem_text1);
				holder.tv2 = (TextView) arg1.findViewById(R.id.selflistitem_text2);
				holder.tv3 = (TextView) arg1.findViewById(R.id.selflistitem_text3);
				holder.iv = (ImageView) arg1.findViewById(R.id.selflistitem_opt);
				arg1.setTag(holder);
			} else {
				holder = (SelfHolder) arg1.getTag();
			}
			if(arg0 % 2 == 0) arg1.setBackgroundColor(getResources().getColor(R.color.itemcolor0));
			else arg1.setBackgroundColor(getResources().getColor(R.color.itemcolor1));
			final SelfStockBean bean = mList.get(arg0);
			holder.tv1.setText(bean.code);
			holder.tv2.setText(bean.sname);
			if(bean.way == 2){
				holder.iv.setImageResource(R.drawable.btn_jia_selector);
			}else{
				holder.iv.setImageResource(R.drawable.btn_jian_selector);
			}
			if(bean.zhangdie > 0){
				holder.tv3.setTextColor(Color.RED);
			} else if(bean.zhangdie == 0){
				holder.tv3.setTextColor(Color.WHITE);
			} else {
				holder.tv3.setTextColor(Color.GREEN);
			}
			holder.tv3.setText(DataUtil.getDoubleString(bean.zhangdie));
			holder.iv.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					showLoading = ViewUtil.showLoading(getParent());
					CommunicationManager.getInstance().eidtSelfStock(user.uid, bean.stklabel, bean.sname, bean.way,
						new CommunicationResultListener() {
						@Override
						public void resultsListener(byte result, Object... resultData) {
							super.resultsListener(result, resultData);
							if(result == CommunicationManager.SUCCEED && "10".equals(resultData[1])){
								Message message = handler.obtainMessage();
								message.what = -1;
								if(bean.way == 2){
									message.arg1 = SelfStockBean.ADD;
									bean.way = 1;
								} else {
									message.obj = bean;
									message.arg1 = SelfStockBean.DELETE;
									bean.way = 2;
								}
								handler.sendMessage(message);
							}else{
								ViewUtil.showDialog(getParent(), (String)resultData[2], null);
								ViewUtil.dismiss(showLoading);
							}
						}
					});
				
				}
			});
			return arg1;
		}
		
	}
	
	class AccountAdapter extends BaseAdapter{
		
		private List<AccountStockBean> mList = new ArrayList<AccountStockBean>();
		
		public void setData(List<AccountStockBean> mList){
			this.mList = mList;
		}
		
		@Override
		public int getCount() {
			return mList.size();
		}
		
		@Override
		public Object getItem(int arg0) {
			return arg0;
		}
		
		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		
		@Override
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
			AccountHolder holder = null;
			if(arg1 == null){
				arg1 = View.inflate(SimulationActivity.this, R.layout.account_listitem_layout, null);
				holder = new AccountHolder();
				holder.tv1 = (TextView) arg1.findViewById(R.id.stock_text1);
				holder.tv2 = (TextView) arg1.findViewById(R.id.stock_text2);
				holder.tv3 = (TextView) arg1.findViewById(R.id.stock_text3);
				holder.tv4 = (TextView) arg1.findViewById(R.id.stock_text4);
				holder.tv5 = (TextView) arg1.findViewById(R.id.stock_text5);
				holder.btn6 = (Button) arg1.findViewById(R.id.stock_button6);
				arg1.setTag(holder);
			} else {
				holder = (AccountHolder) arg1.getTag();
			}
			if(arg0 != 0){
				if(arg0 % 2 == 0) arg1.setBackgroundColor(getResources().getColor(R.color.itemcolor0));
				else arg1.setBackgroundColor(getResources().getColor(R.color.itemcolor1));
			}
			final AccountStockBean bean = mList.get(arg0);
			bean.stklabel = bean.sid;
			holder.tv1.setText(bean.sname);
			holder.tv2.setText(StringUtil.getCode(bean.stklabel));
			holder.tv3.setText(DataUtil.getDoubleString(bean.sprice));
			holder.tv4.setText(DataUtil.getDoubleString(bean.closes));
			holder.tv5.setText(DataUtil.getDoubleString(bean.zhangfu) + "%");
			holder.tv3.setTextColor(getResources().getColor(R.color.black));
			holder.tv4.setTextColor(getResources().getColor(R.color.black));
			if(bean.zhangfu > 0){
				holder.tv5.setTextColor(getResources().getColor(R.color.red));
			} else if(bean.zhangfu == 0){
				holder.tv5.setTextColor(getResources().getColor(R.color.white));
			} else {
				holder.tv5.setTextColor(getResources().getColor(R.color.green));
			}
			holder.btn6.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(SimulationActivity.this, SimulationSubActivity.class);
					intent.putExtra(Constant.string.INTENT_SIMULATION_ACTION, Constant.id.INTENT_SIMULATION_SELL);
					intent.putExtra(Constant.string.INTENT_SIMULATION_VALUE, bean);
					CompetitiveGroup.getInstance().switchActivity("SimulationSubActivity", intent , -1, -1);
				}
			});
			return arg1;
		}
		
	}
	
	class AccountHolder{
		public TextView tv1;
		public TextView tv2;
		public TextView tv3;
		public TextView tv4;
		public TextView tv5;
		public Button btn6;
	}
	
	class WeituoAdapter extends BaseAdapter{
		
		private List<WeiTuoStockBean> mList = new ArrayList<WeiTuoStockBean>();
		
		public void setData(List<WeiTuoStockBean> mList){
			this.mList = mList;
		}
		
		@Override
		public int getCount() {
			return mList.size();
		}
		
		@Override
		public Object getItem(int arg0) {
			return arg0;
		}
		
		@Override
		public long getItemId(int arg0) {
			return arg0;
		}
		
		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			WeiTuoHolder holder = null;
			if(arg1 == null){
				arg1 = View.inflate(SimulationActivity.this, R.layout.weituo_listitem_layout, null);
				holder = new WeiTuoHolder();
				holder.tv1 = (TextView) arg1.findViewById(R.id.weituo_list_text1);
				holder.tv2 = (TextView) arg1.findViewById(R.id.weituo_list_text2);
				holder.tv3 = (TextView) arg1.findViewById(R.id.weituo_list_text3);
				holder.tv4 = (TextView) arg1.findViewById(R.id.weituo_list_text4);
				holder.tv5 = (TextView) arg1.findViewById(R.id.weituo_list_text5);
				holder.tv6 = (TextView) arg1.findViewById(R.id.weituo_list_text6);
				holder.tv7 = arg1.findViewById(R.id.weituo_list_text7);
				arg1.setTag(holder);
			} else {
				holder = (WeiTuoHolder) arg1.getTag();
			}
			if(arg0 != 0){
				if(arg0 % 2 == 0) arg1.setBackgroundColor(getResources().getColor(R.color.itemcolor0));
				else arg1.setBackgroundColor(getResources().getColor(R.color.itemcolor1));
			}
			final WeiTuoStockBean bean = mList.get(arg0);
			holder.tv1.setText(bean.sname);
			holder.tv2.setText(bean.code);
			holder.tv3.setText(bean.sellflag);
			holder.tv4.setText(bean.sellstatus);
			holder.tv5.setText(String.valueOf(bean.sprice));
			holder.tv6.setText(String.valueOf(bean.member));
			if(bean.type != 0){
				holder.tv7.setVisibility(View.INVISIBLE);
			}else{
				holder.tv7.setVisibility(View.VISIBLE);
			}
			holder.tv7.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					showLoading = ViewUtil.showLoading(getParent());
					CommunicationManager.getInstance().cancelWeiTuoStock(user.uid, bean.sid, bean.id,
						new CommunicationResultListener() {
							@Override
							public void resultsListener(byte result, Object... resultData) {
								if(result == CommunicationManager.SUCCEED){
									if("1".equals(resultData[0])){
										super.resultsListener(result, resultData);
										Message message = handler.obtainMessage();
										message.what = -2;
										message.obj = bean;
										handler.sendMessage(message);
									}
								}else{
									ViewUtil.dismiss(showLoading);
								}
							}
						});
				}
			});
			return arg1;
		}
		
	}
	
	class WeiTuoHolder{
		public TextView tv1;
		public TextView tv2;
		public TextView tv3;
		public TextView tv4;
		public TextView tv5;
		public TextView tv6;
		public View tv7;
	}
	
	class MarketHolder{
		public TextView tv1;
		public TextView tv2;
		public TextView tv3;
		public TextView tv4;
		public TextView tv5;
	}
	
	class SelfHolder{
		public TextView tv1;
		public TextView tv2;
		public TextView tv3;
		public ImageView iv;
	}
	
	@Override
	public void onBackPressed() {
		if(isRearch){
			searchValue.setText("");
		}else{
			CompetitiveGroup.getInstance().back();
		}
		if(stockUpdater != null){
			stockUpdater.finish();
		}
	};
	
	@Override
	public void onDetachedFromWindow() {
		if(stockUpdater != null)
		stockUpdater.finish();
		isRearch = false;
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(searchBtn.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	};
	
	OnItemClickListener marketItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int positon,
				long id) {
			Intent intent = new Intent(SimulationActivity.this, SimulationSubActivity.class);
			StockInfoBean stockInfoBean = marketList.get(positon);
			intent.putExtra(Constant.string.INTENT_SIMULATION_VALUE, stockInfoBean);
			CompetitiveGroup.getInstance().switchActivity("SimulationSubActivity", intent, -1, -1);
		}
	};
	
	OnItemClickListener accountItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int positon,
				long id) {
			Intent intent = new Intent(SimulationActivity.this, SimulationSubActivity.class);
			AccountStockBean stockInfoBean = accountList.get(positon);
			intent.putExtra(Constant.string.INTENT_SIMULATION_VALUE, stockInfoBean);
			CompetitiveGroup.getInstance().switchActivity("SimulationSubActivity", intent, -1, -1);
		}
	};
	
	OnItemClickListener weiTuoItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int positon,
				long id) {
			Intent intent = new Intent(SimulationActivity.this, SimulationSubActivity.class);
			StockInfoBean stockInfoBean = weituoList.get(positon);
			intent.putExtra(Constant.string.INTENT_SIMULATION_VALUE, stockInfoBean);
			CompetitiveGroup.getInstance().switchActivity("SimulationSubActivity", intent, -1, -1);
		}
	};
	
	OnItemClickListener selfChooseItemClick = new OnItemClickListener() {

		@Override
		public void onItemClick(AdapterView<?> arg0, View view, int positon,
				long id) {
			Intent intent = new Intent(SimulationActivity.this, SimulationSubActivity.class);
			StockInfoBean stockInfoBean = selfList.get(positon);
			intent.putExtra(Constant.string.INTENT_SIMULATION_VALUE, stockInfoBean);
			CompetitiveGroup.getInstance().switchActivity("SimulationSubActivity", intent, -1, -1);
		}
	};
	
	private EditText searchValue;
	private TextView accountZiChan;
	private TextView accountZongYingkui;
	private TextView accountShiZhi;
	private TextView accountJiaoYi;
	private TextView accountYingkuiLv;
	private UserBean user;
	private Button searchBtn;
	private TextView textView;
	private int type = 1;
	private View view;
}
