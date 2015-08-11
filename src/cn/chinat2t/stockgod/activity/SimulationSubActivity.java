package cn.chinat2t.stockgod.activity;

import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.chinat2t.stockgod.R;
import cn.chinat2t.stockgod.bean.KLineBean;
import cn.chinat2t.stockgod.bean.KlineData;
import cn.chinat2t.stockgod.bean.LineEntity;
import cn.chinat2t.stockgod.bean.SellBuyBean;
import cn.chinat2t.stockgod.bean.StockInfoBean;
import cn.chinat2t.stockgod.bean.StockKlineBean;
import cn.chinat2t.stockgod.bean.UserBean;
import cn.chinat2t.stockgod.http.CommunicationManager;
import cn.chinat2t.stockgod.http.CommunicationResultListener;
import cn.chinat2t.stockgod.utils.Constant;
import cn.chinat2t.stockgod.utils.DataUtil;
import cn.chinat2t.stockgod.utils.TimerManager;
import cn.chinat2t.stockgod.utils.TimerManager.TimerListener;
import cn.chinat2t.stockgod.utils.ViewUtil;
import cn.chinat2t.stockgod.views.CandleStickChart.UpdateListener;
import cn.chinat2t.stockgod.views.FenShiView;
import cn.chinat2t.stockgod.views.MACandleStickChart;
import cn.chinat2t.stockgod.views.StickChart;
import cn.chinat2t.stockgod.views.StockSellView;

public class SimulationSubActivity extends Activity implements OnCheckedChangeListener, OnClickListener{

	public static final int KLINE_PARAM_DAY = 0;
	public static final int GET_STOCK_BY_ID = 1;

	public static final int KLINE_TYPE_DAY = 0;
	public static final int KLINE_TYPE_MONTH = 1;
	
	private MACandleStickChart macandlestickchart;
	private StickChart stickchart;
	private Dialog showLoading;
	private Dialog showNewLoading;
	private StockKlineBean stockKlineBean;
	/**  K线图的布局 */
	private View kLineView;
	private ViewBackPressedListener muneListener;
	private TextView name;
	private TextView nowprice;
	private TextView zhangfu;
	private TextView zhangfuLV;
	private TextView top;
	private TextView open;
	private TextView total;
	private TextView close;
	private TextView low;
	private TextView sum;
	private TextView ma5;
	private TextView ma10;
	private TextView ma25;
	private List<Float> list;
	private List<Float> list2;
	private List<Float> list3;
	
	/**  K线图等的容器 */
	private LinearLayout contentLayout;
	
	private RadioGroup mGroup;
	private Button upBtn;
	
	private Intent intent;
	private StockInfoBean stockInfoBean;
	private TimerManager timeManager;
	private FenShiView fenshiView;
	private StockSellView stockSellView;
	private List<KlineData> lineList = new ArrayList<KlineData>();

	private boolean isUpdate;
	private int index;
	private UserBean userBean;
	private TimerManager stockUpdater;
	private RadioButton rBtn2;
	private RadioButton rBtn0;
	private RadioButton rBtn1;
	private RadioButton rBtn3;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.simulation_kline_layout);
		intent = getIntent();
		Serializable serializableExtra = intent.getSerializableExtra(Constant.string.INTENT_SIMULATION_VALUE);
		if(serializableExtra instanceof StockInfoBean){
			stockInfoBean = (StockInfoBean) serializableExtra;
		}

		userBean = UserBean.getInstance();
		initViews();
		index = intent.getIntExtra(Constant.string.INTENT_SIMULATION_ACTION, 0);
		if(index == Constant.id.INTENT_SIMULATION_BUY){
			onCheckedChanged(mGroup, R.id.kline_radio_button2);
			rBtn2.setChecked(true);
		}else if(index == Constant.id.INTENT_SIMULATION_SELL){
			onCheckedChanged(mGroup, R.id.kline_radio_button3);
			rBtn3.setChecked(true);
		}else{
			onCheckedChanged(mGroup, R.id.kline_radio_button0);
		}

		showLoading = ViewUtil.showLoading(getParent());
		CommunicationManager.getInstance().getStockByUid(
				stockInfoBean.stklabel, userBean.uid, getStockResultListener);
		stockUpdater = new TimerManager(3000, 30);
		stockUpdater.setUnLimit(true);
		stockUpdater.setTimerListener(new TimerListener() {
			
			@Override
			public void update(int time) {
				CommunicationManager.getInstance().getStockByUid(
						stockInfoBean.stklabel, userBean.uid, getStockResultListener);
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

	private void initViews(){
		contentLayout = (LinearLayout) findViewById(R.id.kline_content_layout);
		mGroup = (RadioGroup) findViewById(R.id.kline_menu_layout);
		mGroup.setOnCheckedChangeListener(this);
		View view = findViewById(R.id.kine_stock_info_layout);
		name = (TextView) view.findViewById(R.id.kline_stock_name);
		nowprice = (TextView) view.findViewById(R.id.kline_stock_cur);
		zhangfu = (TextView) view.findViewById(R.id.kline_stock_data1);
		zhangfuLV = (TextView) view.findViewById(R.id.kline_stock_data2);
		open = (TextView) view.findViewById(R.id.kline_stock_today2);
		top = (TextView) view.findViewById(R.id.kline_stock_top2);
		total = (TextView) view.findViewById(R.id.kline_stock_total2);
		close = (TextView) view.findViewById(R.id.kline_stock_yesterday2);
		low = (TextView) view.findViewById(R.id.kline_stock_lowest2);
		sum = (TextView) view.findViewById(R.id.kline_stock_sum2);
		
		rBtn0 = ((RadioButton)findViewById(R.id.kline_radio_button0));
		rBtn1 = ((RadioButton)findViewById(R.id.kline_radio_button1));
		rBtn2 = ((RadioButton)findViewById(R.id.kline_radio_button2));
		rBtn3 = ((RadioButton)findViewById(R.id.kline_radio_button3));
	}

	private void initValue(StockKlineBean stockInfoBean) {
		name.setText(stockInfoBean.sname + " " + stockInfoBean.code);
		nowprice.setText(DataUtil.getDoubleString(stockInfoBean.nowprice));
		zhangfu.setText(DataUtil.getDoubleString(stockInfoBean.zhangdie));
		if(stockInfoBean.zhangdie < 0){
			zhangfu.setTextColor(getResources().getColor(R.color.green));
		}else{
			zhangfu.setTextColor(getResources().getColor(R.color.red));
		}
		zhangfuLV.setText(DataUtil.getDoubleString(stockInfoBean.zhangfu) + "%");
		if(stockInfoBean.zhangfu < 0){
			zhangfuLV.setTextColor(getResources().getColor(R.color.green));
		}else{
			zhangfuLV.setTextColor(getResources().getColor(R.color.red));
		}
		open.setText(DataUtil.getDoubleString(stockInfoBean.opens));
		top.setText(DataUtil.getDoubleString(stockInfoBean.high));
		total.setText(DataUtil.getDoubleString(stockInfoBean.vol / 10000) + "万");
		close.setText(DataUtil.getDoubleString(stockInfoBean.closes));
		low.setText(DataUtil.getDoubleString(stockInfoBean.low));
		sum.setText(DataUtil.getDoubleString(stockInfoBean.amount / 10000) + "亿");
		
		if(fenshiView != null){
			fenshiView.updateStock(stockInfoBean);
		}
		if(stockSellView != null){
			stockSellView.updateSellBuy(stockInfoBean);
		}
	}
	
	private CommunicationResultListener getStockResultListener = new CommunicationResultListener() {
		
		// 被JsonParserManager中的parserMonthLine()调用
		public void resultListener(byte result, Object resultData) {
			switch (result) {
			case CommunicationManager.FAIL:
				ViewUtil.dismiss(showLoading);
//				ViewUtil.showToast((String)resultData, SimulationSubActivity.this);
				break;
				
			case CommunicationManager.SUCCEED:
				if(token > 0){
					Message msg = mHandler.obtainMessage();
					msg.what = result;
					msg.arg1 = GET_STOCK_BY_ID;
					msg.obj = resultData;
					mHandler.sendMessage(msg);
				}
				break;
			}
		};
	};
	
	private CommunicationResultListener dayResultListener = new CommunicationResultListener() {
		
		// 被JsonParserManager中的parserMonthLine()调用
		public void resultListener(byte result, Object resultData) {
			switch (result) {
			case CommunicationManager.FAIL:
				ViewUtil.dismiss(showNewLoading);
//				ViewUtil.showToast((String)resultData, SimulationSubActivity.this);
				break;
				
			case CommunicationManager.SUCCEED:
				if(token > 0){
					Message msg = mHandler.obtainMessage();
					msg.what = result;
					msg.arg1 = KLINE_PARAM_DAY;
					msg.obj = resultData;
					mHandler.sendMessage(msg);
				}
				break;
			}
			isUpdate = false;
		};
	};
	
	Handler mHandler = new Handler(){


		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case KLINE_PARAM_DAY:
				isUpdate = false;
				KLineBean kLine = (KLineBean) msg.obj;
				lineList = kLine.kline;
				if (lineList == null) return;
				int length = lineList.size() > 50 ? 50 : lineList.size();
				macandlestickchart.setMaxCandleSticksNum(length);
				macandlestickchart.setLatitudeNum(4);
				macandlestickchart.setLongtitudeNum(4);
				// 为chart2增加均线
				macandlestickchart.setLineData(getLineEntityList()); // 设置MA均线
				macandlestickchart.setOHLCData(lineList); // 蜡烛图样的数值
				macandlestickchart.setStartIndex(lineList.size() - length);
				macandlestickchart.invalidate(); // 更新视图

				stickchart.setMaxStickDataNum(length);
				stickchart.setLatitudeNum(2);
				stickchart.setLongtitudeNum(4);
				stickchart.setStickData(lineList);
				stickchart.setStartIndex(lineList.size() - length);
				stickchart.invalidate();
				updateMAStr(lineList.size() - 1);
				
				ViewUtil.dismiss(showNewLoading);
				break;

			case GET_STOCK_BY_ID:
				ViewUtil.dismiss(showLoading);
				stockKlineBean = (StockKlineBean) msg.obj;
				stockKlineBean.id = stockInfoBean.id;
				initValue(stockKlineBean);
				break;
			}
		};
	};
	
	private void initMACandleStickChart(){
		macandlestickchart = (MACandleStickChart) kLineView.findViewById(R.id.kline_stock_tu1);
		macandlestickchart.setShowCandleSticks(true);
		macandlestickchart.setBackgroudColor(Color.TRANSPARENT);
		macandlestickchart.setUpdateListener(new UpdateListener() {
			@Override
			public void update(int position, boolean isView) {
				updateMAStr(position);
			}
		});
	}

	private void updateMAStr(int position) {
		if(position >= list.size() || position < 0) return;
		DecimalFormat format = new DecimalFormat("#.00");
		if(ma5 != null) ma5.setText(("MA5=" + format.format(list.get(position))));
		if(ma10 != null) ma10.setText("MA10=" + format.format(list2.get(position)));
		if(ma25 != null) ma25.setText("MA25=" + format.format(list3.get(position)));
	}
	
	private List<LineEntity> getLineEntityList() {
		List<LineEntity> lines = new ArrayList<LineEntity>();

		list = DataUtil.initMA(5, lineList);
		LineEntity MA5 = new LineEntity();
		MA5.setTitle("MA5");
		MA5.setLineColor(Color.WHITE);
		MA5.setLineData(list);
		lines.add(MA5);
		
		list2 = DataUtil.initMA(10, lineList);
		LineEntity MA10 = new LineEntity();
		MA10.setTitle("MA10");
		MA10.setLineColor(Color.parseColor("#ffF1F929"));
		MA10.setLineData(list2);
		lines.add(MA10);

		list3 = DataUtil.initMA(20, lineList);
		LineEntity MA25 = new LineEntity();
		MA25.setTitle("MA20");
		MA25.setLineColor(Color.parseColor("#ffC14AB8"));
		MA25.setLineData(list3);
		lines.add(MA25);
		return lines;
	}
	
	private void initStickChart(){

		stickchart = (StickChart) kLineView.findViewById(R.id.kline_stock_tu2);
		stickchart.setAxisMarginRight(1);
		stickchart.setLatitudeNum(2);
		stickchart.setLongtitudeNum(4);
		stickchart.setDisplayAxisXTitle(false);
		stickchart.setBackgroudColor(Color.TRANSPARENT);
	}

	@Override
	public void onBackPressed() {
//		if(stockSellView.isSelling) return;
		if(muneListener != null && muneListener.isShow()){
			muneListener.dismiss();
		}else{
			if(timeManager != null){
				timeManager.finish();
			}
			if(stockUpdater != null){
				stockUpdater.finish();
			}
			CompetitiveGroup.getInstance().back();
		}
	};

	private RadioButton onSelectedRBtn;
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		if(onSelectedRBtn != null)
			onSelectedRBtn.setBackgroundResource(R.drawable.main_top_tag_default);
		switch (checkedId) {
		case R.id.kline_radio_button0:
			kLineView = View.inflate(SimulationSubActivity.this, R.layout.kline_layout, null);
			if(contentLayout != null){
				contentLayout.removeAllViews();
			}
			contentLayout.addView(kLineView, new LinearLayout.LayoutParams(-1, -2));
			kLineView.findViewById(R.id.kline_day_btn).setOnClickListener(this);
			kLineView.findViewById(R.id.kline_week_btn).setOnClickListener(this);
			kLineView.findViewById(R.id.kline_month_btn).setOnClickListener(this);
			kLineView.findViewById(R.id.kline_five_btn).setOnClickListener(this);
			kLineView.findViewById(R.id.kline_30_btn).setOnClickListener(this);
			kLineView.findViewById(R.id.kline_60_btn).setOnClickListener(this);
			ma5 = (TextView) kLineView.findViewById(R.id.ma_5);
			ma10 = (TextView) kLineView.findViewById(R.id.ma_10);
			ma25 = (TextView) kLineView.findViewById(R.id.ma_20);
			ma5.setTextColor(Color.rgb(222, 63, 209));
			ma10.setTextColor(Color.RED);
			ma25.setTextColor(Color.rgb(52, 129, 1));
			refreshKline();
			onClick(kLineView.findViewById(R.id.kline_day_btn));  // 获取数据
			onSelectedRBtn = rBtn0;
			break;
			
		case R.id.kline_radio_button1:
			//  分时线的View
			if(contentLayout != null){
				contentLayout.removeAllViews();
			}
			fenshiView = new FenShiView(stockKlineBean, this);
			contentLayout.addView(fenshiView);
			onSelectedRBtn = rBtn1;
			break;
			
		case R.id.kline_radio_button2:
			if(userBean.usertype == UserBean.USER_LIN_SHI){
				ViewUtil.showToast("临时用户不能操作，请先注册。", this);
				if(onSelectedRBtn != null){
					onSelectedRBtn.setBackgroundResource(R.drawable.main_top_tag_active);
					rBtn2.setBackgroundResource(R.drawable.main_top_tag_default);
				}
				return;
			}
			if(stockKlineBean != null && !isEnable(stockKlineBean.stklabel)){
				onSelectedRBtn.setBackgroundResource(R.drawable.main_top_tag_active);
				rBtn2.setBackgroundResource(R.drawable.main_top_tag_default);
				return;
			}
			if(contentLayout != null){
				contentLayout.removeAllViews();
			}
			stockSellView = new StockSellView(this, stockKlineBean, SellBuyBean.STOCK_BUY);
			contentLayout.addView(stockSellView);
			onSelectedRBtn = rBtn2;
			break;
			
		case R.id.kline_radio_button3:
			if(userBean.usertype == UserBean.USER_LIN_SHI){
				ViewUtil.showToast("临时用户不能操作，请先注册。", this);
				if(onSelectedRBtn != null){
					onSelectedRBtn.setBackgroundResource(R.drawable.main_top_tag_active);
					rBtn3.setBackgroundResource(R.drawable.main_top_tag_default);
				}
				return;
			}
			if(stockKlineBean != null && !isEnable(stockKlineBean.stklabel)){
				onSelectedRBtn.setBackgroundResource(R.drawable.main_top_tag_active);
				rBtn3.setBackgroundResource(R.drawable.main_top_tag_default);
				return;
			}
			if(stockKlineBean != null && stockKlineBean.member == 0){
				if(onSelectedRBtn != null){
					onSelectedRBtn.setBackgroundResource(R.drawable.main_top_tag_active);
					rBtn3.setBackgroundResource(R.drawable.main_top_tag_default);
				}
				return;
			}
			if(contentLayout != null){
				contentLayout.removeAllViews();
			}
			stockSellView = new StockSellView(this, stockKlineBean, SellBuyBean.STOCK_SELL);
			contentLayout.addView(stockSellView);
			onSelectedRBtn = rBtn3;
			break;
		}
		onSelectedRBtn.setBackgroundResource(R.drawable.main_top_tag_active);
	}

	private String[] stockNames = {"SH000002", "SH000001", "SZ399107", "SZ399001",
			"SZ399005", "SZ399006", "SH000003", "SZ399108"};
	
	public boolean isEnable(String code){
		for (int i = 0; i < stockNames.length; i++) {
			if(stockNames[i].equals(code)){
				return false;
			}
		}
		return true;
	}
	
	private void refreshKline() {
		initMACandleStickChart();
		initStickChart();
		macandlestickchart.addNotify(stickchart);
	}

	@Override
	public void onClick(View v) {
		if(upBtn == (Button)v && !isUpdate) return;
		if(stockInfoBean == null) return;
		changeUpButton(v);
		dayResultListener.setToken(0);
		if(!isUpdate){
			showNewLoading = ViewUtil.showNewLoading(getParent());
		}
		CommunicationManager manager = CommunicationManager.getInstance();
		int timeLength = 60* 30;
		switch (v.getId()) {
		case R.id.kline_day_btn:
			manager.getDayLine(stockInfoBean.stklabel, dayResultListener);
			timeLength = 24 * 60* 60;
			break;
		case R.id.kline_week_btn:
			manager.getWeekLine(stockInfoBean, dayResultListener);
//			timeLength = 24 * 60* 60 * 7;
			timeLength = 0;
			break;
		case R.id.kline_month_btn:
			manager.getMonthLine(stockInfoBean, dayResultListener);
//			timeLength = 24 * 60* 60 * 30;
			timeLength = 0;
			break;
		case R.id.kline_five_btn:
			manager.get5Line(stockInfoBean, dayResultListener);
			timeLength = 60* 5;
			break;
		case R.id.kline_30_btn:
			manager.get30Line(stockInfoBean, dayResultListener);
			timeLength = 60* 30;
			break;
		case R.id.kline_60_btn:
			manager.get60Line(stockInfoBean, dayResultListener);
			timeLength = 60* 60;
			break;
		}
		if(!isUpdate){
			if(timeManager != null && !timeManager.isPause())
				timeManager.finish();
			startTimeManager(timeLength);
		}
	}
	
	private void startTimeManager(int timeLength) {
		if(timeLength <= 0) return;
		timeManager = new TimerManager(timeLength * 1000, 24 * 60 * 60 * 1000);
		timeManager.setTimerListener(new TimerListener() {
			
			@Override
			public void update(int time) {
				ViewUtil.dismiss(showLoading);
				isUpdate = true;
				onClick(upBtn);
				System.out.println("&&&&&&&&&&&&&&&&&更新股票数据哦&&&&&&&&&&&&&&&&&");
			}
			
			@Override
			public void start() {
				isUpdate = true;
			}
			
			@Override
			public void pause() {
				isUpdate = false;
			}
			
			@Override
			public void finish() {
				System.out.println("****************关闭股票更新计时器****************~~");
				isUpdate = false;
			}
		});
		timeManager.start();
	}

	public void setOnViewBackPressedListener(ViewBackPressedListener listener){
		this.muneListener = listener;
	}
	
	public interface ViewBackPressedListener{
		public void dismiss();
		
		public boolean isShow();
	}

	public StockInfoBean getStockInfoBean() {
		return stockInfoBean;
	}
	
	private void changeUpButton(View v) {
		if(upBtn != null){
			upBtn.setBackgroundResource(R.drawable.main_tag_default);
			upBtn.setTextColor(getResources().getColorStateList(R.color.white));
		}
		upBtn = (Button) v;
		upBtn.setBackgroundResource(R.drawable.main_tag_active);
		upBtn.setTextColor(getResources().getColorStateList(R.color.black));
	}

	@Override
	public void onDetachedFromWindow() {
//		if(stockSellView.isSelling) return;
		if(timeManager != null && !timeManager.isPause()){
			timeManager.finish();
		}
		if(stockUpdater != null)
		stockUpdater.finish();
	}
}