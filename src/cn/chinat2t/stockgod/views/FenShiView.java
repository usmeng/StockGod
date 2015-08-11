package cn.chinat2t.stockgod.views;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.chinat2t.stockgod.R;
import cn.chinat2t.stockgod.bean.KLineBean;
import cn.chinat2t.stockgod.bean.KlineData;
import cn.chinat2t.stockgod.bean.LineEntity;
import cn.chinat2t.stockgod.bean.PanKouBean;
import cn.chinat2t.stockgod.bean.SellBuyBean;
import cn.chinat2t.stockgod.bean.StockKlineBean;
import cn.chinat2t.stockgod.http.CommunicationManager;
import cn.chinat2t.stockgod.http.CommunicationResultListener;
import cn.chinat2t.stockgod.utils.DataUtil;
import cn.chinat2t.stockgod.utils.StringUtil;
import cn.chinat2t.stockgod.utils.ViewUtil;

public class FenShiView extends LinearLayout {

	private View timeView;
	private LinearLayout timeLineLayout;
	private LayoutInflater mInflater;
	private View fenjiaView;
	private View fenshiView;
	private Button upBtn;

	private ListView pankouListView;
	private ListView mingxiListView;
	private ListView fenjiaListView;
	private ListView zijinListView;
	private TextView waipan;
	private TextView neipan;
	private TextView weibi;
	private TextView weicha;
	private List<Float> list;
	private List<Float> list3;

	private PanKouAdapter pankouAdapter;
	private MingXiAdapter mingxiAdapter;
	
	private StickChart stickchart;
	private FenShiLineView macandlestickchart;
	
	private List<KlineData> lineList = new ArrayList<KlineData>();
	private StockKlineBean stock;
	private View mingxiView;
	private View pankouView;
	private Dialog showLoading;
	
	public void updateStock(StockKlineBean stock) {
		updatePankou(stock);
	}

	public FenShiView(StockKlineBean stock, Context context) {
		super(context);
		this.stock = stock;
		mInflater = LayoutInflater.from(context);
		timeView = mInflater.inflate(R.layout.kline_time_layout, null);
		addView(timeView);
		
		timeLineLayout = (LinearLayout) timeView.findViewById(R.id.time_line_layout);
		timeView.findViewById(R.id.kline_time_btn1).setOnClickListener(onClickListener);
		timeView.findViewById(R.id.kline_time_btn2).setOnClickListener(onClickListener);
		timeView.findViewById(R.id.kline_time_btn3).setOnClickListener(onClickListener);
		timeView.findViewById(R.id.kline_time_btn4).setOnClickListener(onClickListener);
		timeView.findViewById(R.id.kline_time_btn5).setOnClickListener(onClickListener);
		timeView.findViewById(R.id.kline_time_btn6).setOnClickListener(onClickListener);
		
		initFenShiView();
		initPankouView();
		initMingXiView();
		initFenJiaView();
		onClickListener.onClick(timeView.findViewById(R.id.kline_time_btn1));
	}

	private void initFenShiView() {
		fenshiView = mInflater.inflate(R.layout.line_stickchart, null);
		macandlestickchart = (FenShiLineView) fenshiView.findViewById(R.id.kline_stock_tu1);
		stickchart = (StickChart) fenshiView.findViewById(R.id.kline_stock_tu2);
		refreshStickChart();
	}

	private void initFenJiaView() {
		fenjiaView = mInflater.inflate(R.layout.system_list_view, null);
		fenjiaListView = (ListView) fenjiaView.findViewById(R.id.list);
		fenjiaListView.addHeaderView(mInflater.inflate(R.layout.fenjia_listitem_layout, null));
	}

	private void initMingXiView() {
		mingxiView = mInflater.inflate(R.layout.system_list_view, null);
		mingxiListView = (ListView) mingxiView.findViewById(R.id.list);
		mingxiListView.addHeaderView(mInflater.inflate(R.layout.mingxi_listitem_layout, null));
		mingxiAdapter = new MingXiAdapter();
		mingxiListView.setAdapter(mingxiAdapter);
	}
	
	private void refreshStickChart() {
		initMACandleStickChart();
		initStickChart();
//		macandlestickchart.addNotify(stickchart);
	}
	
	private void initMACandleStickChart(){
		macandlestickchart.setLineType(1);
		macandlestickchart.setLatitudeNum(4);
		macandlestickchart.setLongtitudeNum(4);
		macandlestickchart.setShowCandleSticks(false);
		macandlestickchart.setMiddleValue((float)stock.closes);
		macandlestickchart.setBackgroudColor(Color.TRANSPARENT);
	}
	
	private void initStickChart(){
		stickchart.setBackgroudColor(Color.TRANSPARENT);
		stickchart.setLatitudeNum(2);
		stickchart.setLongtitudeNum(4);
		stickchart.setDisplayAxisXTitle(false);
		stickchart.setType(1);
	}
	
	private CommunicationResultListener listener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			switch (result) {
			case CommunicationManager.FAIL:
				ViewUtil.dismiss(showLoading);
				break;
				
			case CommunicationManager.SUCCEED:
				if(token > 0){
					Message msg = mHandler.obtainMessage();
					msg.arg1 = 0;
					msg.obj = resultData;
					mHandler.sendMessage(msg);
				}
				break;
			}
		};
	};
	
	private List<LineEntity> getLineEntityList() {
		List<LineEntity> lines = new ArrayList<LineEntity>();
		
		list = DataUtil.initFenShi(1, lineList);
		LineEntity MA5 = new LineEntity();
		MA5.setTitle("现价线");
		MA5.setLineColor(getResources().getColor(R.color.white));
		MA5.setLineData(list);
		lines.add(MA5);
		
		list3 = DataUtil.initFenShi(0, lineList);
		LineEntity MA25 = new LineEntity();
		MA25.setTitle("均价线");
		MA25.setLineColor(getResources().getColor(R.color.yello));
		MA25.setLineData(list3);
		lines.add(MA25);
		return lines;
	}
	
	Handler mHandler = new Handler(){

		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case 0:
				KLineBean kLine = (KLineBean) msg.obj;
				lineList = kLine.kline;
				if (lineList == null) return;
				lineList = initList(lineList);
				// 为chart2增加均线
				int length = lineList.size() - 1;
				macandlestickchart.setMaxCandleSticksNum(length);
				macandlestickchart.setLineData(getLineEntityList());
				macandlestickchart.setOHLCData(lineList); // 蜡烛图样的数值
				macandlestickchart.setStartIndex(0);
				macandlestickchart.invalidate(); // 更新视图

				stickchart.setMaxStickDataNum(length);
				stickchart.setStickData(lineList);
				stickchart.setStartIndex(0);
				stickchart.invalidate();
				break;
				
			case 2:
				mingxiAdapter.setData((List<PanKouBean>) msg.obj);
				mingxiAdapter.notifyDataSetChanged();
				break;
			}
			ViewUtil.dismiss(showLoading);
		}

	};
	
	private List<KlineData> initList(List<KlineData> lineList) {
		for (int i = lineList.size() - 1; i < 240; i++) {
			KlineData object = new KlineData();
			if(i == 239){
				object.yearm = "150000";
			}
			lineList.add(object);
		}
		return lineList;
	};
	
	OnClickListener onClickListener = new OnClickListener() {

		private CircleMapView circle;

		@Override
		public void onClick(View v) {
			if(stock == null) return;
			switch (v.getId()) {
			case R.id.kline_time_btn1:
				changeUpButton(v);
				if (timeLineLayout != null)
					timeLineLayout.removeAllViews();
				timeLineLayout.addView(fenshiView);
				if(stock != null){
					showLoading = ViewUtil.showLoading(((Activity)getContext()).getParent());
					CommunicationManager.getInstance().getFenShiLine(stock, listener);
				}
				break;
			case R.id.kline_time_btn2:
				changeUpButton(v);
				if (timeLineLayout != null)
					timeLineLayout.removeAllViews();
				timeLineLayout.addView(pankouView);
				updatePankou(stock);
				break;
			case R.id.kline_time_btn3:
			case R.id.kline_time_btn5:
				changeUpButton(v);
				if (timeLineLayout != null)
					timeLineLayout.removeAllViews();
				timeLineLayout.addView(mingxiView);
				if(stock == null) return;
				showLoading = ViewUtil.showLoading(((Activity)getContext()).getParent());
				CommunicationManager.getInstance().getMingxi(stock.stklabel, new CommunicationResultListener() {
					
					public void resultListener(byte result, Object resultData) {
						switch (result) {
						case CommunicationManager.FAIL:
							ViewUtil.dismiss(showLoading);
							break;
							
						case CommunicationManager.SUCCEED:
							if(token > 0){
								Message msg = mHandler.obtainMessage();
								msg.arg1 = 2;
								msg.obj = resultData;
								mHandler.sendMessage(msg);
							}
							break;
						}
					};
				});
				break;
			case R.id.kline_time_btn4:
				changeUpButton(v);
				if (timeLineLayout != null)
					timeLineLayout.removeAllViews();
				timeLineLayout.addView(fenjiaView);
				List<FenJiaBean> fenjiaList = new ArrayList<FenJiaBean>();
				fenjiaList.add(new FenJiaBean(2.28, 1999, 33.23, 23.25, 20));
				fenjiaList.add(new FenJiaBean(2.28, 1999, 33.23, 23.25, 20));
				fenjiaList.add(new FenJiaBean(2.28, 1999, 33.23, 23.25, 20));
				fenjiaList.add(new FenJiaBean(2.28, 1999, 33.23, 23.25, 20));
				fenjiaList.add(new FenJiaBean(2.28, 1999, 33.23, 23.25, 20));
				fenjiaList.add(new FenJiaBean(2.28, 1999, 33.23, 23.25, 20));
				FenJiaAdapter fenjiaAdapter = new FenJiaAdapter(fenjiaList);
				fenjiaListView.setAdapter(fenjiaAdapter);
				break;
			case R.id.kline_time_btn6:
				changeUpButton(v);
				if (timeLineLayout != null)
					timeLineLayout.removeAllViews();
				View zijinView = mInflater.inflate(R.layout.zijin_time_layout, null);
				timeLineLayout.addView(zijinView);
				zijinListView = (ListView) zijinView.findViewById(R.id.list);
				circle = (CircleMapView) zijinView.findViewById(R.id.zijin_tu);
				circle.invalidate();
				List<ZiJinBean> zijinList = new ArrayList<ZiJinBean>();
				zijinList.add(new ZiJinBean("主力", 135, 33.23, 450.5, 20));
				ZiJinAdapter zijinAdapter = new ZiJinAdapter(zijinList);
				zijinListView.addHeaderView(mInflater.inflate(R.layout.zijin_listitem_layout, null));
				zijinListView.setAdapter(zijinAdapter);
				break;
			}
		}

	};
	
	private void initPankouView() {
		pankouView = mInflater.inflate(R.layout.kline_time_pankou_view, null);
		waipan = (TextView) pankouView.findViewById(R.id.pankou_wai_pan_value);
		neipan = (TextView) pankouView.findViewById(R.id.pankou_nei_pan_value);
		weibi = (TextView) pankouView.findViewById(R.id.pankou_wei_bi_value);
		weicha = (TextView) pankouView.findViewById(R.id.pankou_wei_cha_value);
		pankouListView = (ListView) pankouView.findViewById(R.id.pankou_list);
		pankouAdapter = new PanKouAdapter();
		pankouListView.setAdapter(pankouAdapter);
	}
	
	int maxSell = 0;
	int maxBuy = 0;
	private void updatePankou(StockKlineBean stock) {
		if(stock == null) return;
		this.stock = stock;
		waipan.setText(stock.sellvol + "手");
		neipan.setText(stock.buyvol + "手");
		weibi.setText(stock.weibi + "%");
		maxBuy = getMax(stock.sellList/*, SellBuyBean.STOCK_BUY*/);
		maxSell = maxBuy;
//		maxSell = getMax(stock.sellList/*, SellBuyBean.STOCK_SELL*/);
		weicha.setText(String.valueOf(stock.weicha));
		pankouAdapter.setData(stock.sellList);
		pankouAdapter.notifyDataSetChanged();
	}
	
	private int getMax(List<SellBuyBean> list/*, int type*/){
		int max = 0;
		for (int i = 0; i < list.size(); i++) {
			SellBuyBean bean = list.get(i);
			if (bean.volsell > max) {
				max = bean.volsell;
			}
		}
		return max;
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

	class PanKouAdapter extends BaseAdapter{
		
        List<SellBuyBean> mList = new ArrayList<SellBuyBean>();
        
        public PanKouAdapter() {
		}
        
        public void setData(List<SellBuyBean> mList){
        	this.mList = mList;
        }
        
		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MarketHolder holder = null;
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.pankou_listitem_layout, null);
				holder = new MarketHolder();
				holder.tv1 = (TextView) convertView.findViewById(R.id.listitem_text1);
				holder.tv2 = (TextView) convertView.findViewById(R.id.listitem_text2);
				holder.tv3 = (TextView) convertView.findViewById(R.id.listitem_text3);
				holder.tv4 = (ItemView) convertView.findViewById(R.id.listitem_image4);
				holder.tv4.setVisibility(View.VISIBLE);
				convertView.setTag(holder);
			} else {
				holder = (MarketHolder) convertView.getTag();
			}
			if(position != 0){
				if(position % 2 == 0) convertView.setBackgroundColor(getResources().getColor(R.color.itemcolor0));
				else convertView.setBackgroundColor(getResources().getColor(R.color.itemcolor1));
			}
			SellBuyBean panKouBean = mList.get(position);
			if(panKouBean.flag == SellBuyBean.STOCK_SELL){
				holder.tv1.setText("卖" + panKouBean.num + "(元/手)");
			}else{
				holder.tv1.setText("买" + (panKouBean.num) + "(元/手)");
			}
			holder.tv2.setText(String.valueOf(panKouBean.pricesell));
			holder.tv3.setText(String.valueOf(panKouBean.volsell));
			int maxLength = 0;
			if(panKouBean.flag == SellBuyBean.STOCK_SELL){
				maxLength = maxSell;
			}else{
				maxLength = maxBuy;
			}
			holder.tv4.setLength(100 - (int)(panKouBean.volsell * 100 / maxLength));
			if(panKouBean.pricesell > stock.closes){
				holder.tv4.setColor(getResources().getColor(R.color.red));
				holder.tv2.setTextColor(getResources().getColor(R.color.red));
			}else if(panKouBean.pricesell < stock.closes){
				holder.tv4.setColor(getResources().getColor(R.color.green));
				holder.tv2.setTextColor(getResources().getColor(R.color.green));
			}else{
				holder.tv2.setTextColor(getResources().getColor(R.color.white));
				holder.tv4.setColor(getResources().getColor(R.color.white));
			}
			holder.tv4.invalidate();
			return convertView;
		}
		
	}
	
	class MingXiAdapter extends BaseAdapter{
		
		List<PanKouBean> mList = new ArrayList<PanKouBean>();
        
        public MingXiAdapter() {
		}
        
        public void setData(List<PanKouBean> mList){
        	this.mList = mList;
        }
        
		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MingXiHolder holder = null;
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.mingxi_listitem_layout, null);
				holder = new MingXiHolder();
				holder.tv1 = (TextView) convertView.findViewById(R.id.listitem_text1);
				holder.tv2 = (TextView) convertView.findViewById(R.id.listitem_text2);
				holder.tv3 = (TextView) convertView.findViewById(R.id.listitem_text3);
				holder.tv4 = (TextView) convertView.findViewById(R.id.listitem_text4);
				holder.tv5 = (TextView) convertView.findViewById(R.id.listitem_text5);
				convertView.findViewById(R.id.under_line).setVisibility(View.GONE);

				convertView.setTag(holder);
			} else {
				holder = (MingXiHolder) convertView.getTag();
			}
			if(position % 2 == 0) convertView.setBackgroundColor(getResources().getColor(R.color.itemcolor0));
			else convertView.setBackgroundColor(getResources().getColor(R.color.itemcolor1));
			PanKouBean bean = mList.get(position);
			holder.tv1.setText(StringUtil.getTime(bean.time));
			holder.tv2.setText(String.valueOf(bean.close));
			holder.tv3.setText(String.valueOf((int)bean.nowv));
			holder.tv4.setText(String.format("%.2f", bean.close * bean.nowv / 100));
			String str = "";
			if(bean.isbuy == -2 || bean.isbuy == 2){
				str = "中性";
				holder.tv2.setTextColor(getResources().getColor(R.color.white));
				holder.tv5.setTextColor(getResources().getColor(R.color.white));
			}else if(bean.isbuy > 0){
				str = "买盘";
				holder.tv2.setTextColor(getResources().getColor(R.color.red));
				holder.tv5.setTextColor(getResources().getColor(R.color.red));
			}else if(bean.isbuy < 0){
				str = "卖盘";
				holder.tv2.setTextColor(getResources().getColor(R.color.green));
				holder.tv5.setTextColor(getResources().getColor(R.color.green));
			}
			holder.tv5.setText(str);
			return convertView;
		}
		
	}
	
	class FenJiaAdapter extends BaseAdapter{
		
        List<FenJiaBean> mList;
        
        public FenJiaAdapter(List<FenJiaBean> mList) {
        	this.mList = mList;
		}
        
		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			FenJiaHolder holder = null;
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.fenjia_listitem_layout, null);
				holder = new FenJiaHolder();
				holder.tv1 = (TextView) convertView.findViewById(R.id.listitem_text1);
				holder.tv2 = (TextView) convertView.findViewById(R.id.listitem_text2);
				holder.tv3 = (TextView) convertView.findViewById(R.id.listitem_text3);
				holder.tv4 = (TextView) convertView.findViewById(R.id.listitem_text4);
				holder.img5 = (ItemView) convertView.findViewById(R.id.listitem_image5);
				convertView.findViewById(R.id.under_line).setVisibility(View.GONE);
				convertView.setTag(holder);
			} else {
				holder = (FenJiaHolder) convertView.getTag();
			}
			if(position % 2 == 0) convertView.setBackgroundColor(getResources().getColor(R.color.itemcolor0));
			else convertView.setBackgroundColor(getResources().getColor(R.color.itemcolor1));
			FenJiaBean fenJiaBean = mList.get(position);
			holder.tv1.setText(String.valueOf(fenJiaBean.price));
			holder.tv2.setText(String.valueOf(fenJiaBean.liang));
			holder.tv3.setText(String.valueOf(fenJiaBean.maipan));
			holder.tv4.setText(String.valueOf(fenJiaBean.chengjiao));
			holder.img5.setLength((int) fenJiaBean.chengjiao);
			holder.img5.invalidate();
			return convertView;
		}
		
	}
	
	class ZiJinAdapter extends BaseAdapter{
		
        List<ZiJinBean> mList;
        
        public ZiJinAdapter(List<ZiJinBean> mList) {
        	this.mList = mList;
		}
        
		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MingXiHolder holder = null;
			if(convertView == null){  
				convertView = mInflater.inflate(R.layout.mingxi_listitem_layout, null);
				holder = new MingXiHolder();
				holder.tv1 = (TextView) convertView.findViewById(R.id.listitem_text1);
				holder.tv2 = (TextView) convertView.findViewById(R.id.listitem_text2);
				holder.tv3 = (TextView) convertView.findViewById(R.id.listitem_text3);
				holder.tv4 = (TextView) convertView.findViewById(R.id.listitem_text4);
				holder.tv5 = (TextView) convertView.findViewById(R.id.listitem_text5);
				convertView.setTag(holder);
			} else {
				holder = (MingXiHolder) convertView.getTag();
			}
			if(position != 0){
				if(position % 2 == 0) convertView.setBackgroundColor(getResources().getColor(R.color.itemcolor0));
				else convertView.setBackgroundColor(getResources().getColor(R.color.itemcolor1));
			}
			ZiJinBean zijinBean = mList.get(position);
			holder.tv1.setText(zijinBean.leixing);
			holder.tv2.setText(String.valueOf(zijinBean.liuru));
			holder.tv3.setText(String.valueOf(zijinBean.bilieru));
			holder.tv4.setText(String.valueOf(zijinBean.liuchu));
			holder.tv5.setText(String.valueOf(zijinBean.biliechu));
			return convertView;
		}
		
	}
	
	class StockAdapter extends BaseAdapter{
		
        List<StockBean> mList;
        
        public StockAdapter(List<StockBean> mList) {
        	this.mList = mList;
		}
        
		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			MingXiHolder holder = null;
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.mingxi_listitem_layout, null);
				holder = new MingXiHolder();
				holder.tv1 = (TextView) convertView.findViewById(R.id.listitem_text1);
				holder.tv2 = (TextView) convertView.findViewById(R.id.listitem_text2);
				holder.tv3 = (TextView) convertView.findViewById(R.id.listitem_text3);
				holder.tv4 = (TextView) convertView.findViewById(R.id.listitem_text4);
				holder.tv5 = (TextView) convertView.findViewById(R.id.listitem_text5);
				convertView.setTag(holder);
			} else {
				holder = (MingXiHolder) convertView.getTag();
			}

			StockBean zijinBean = mList.get(position);
			holder.tv1.setText(zijinBean.name);
			holder.tv2.setText(String.valueOf(zijinBean.value1));
			holder.tv3.setText(String.valueOf(zijinBean.value2));
			holder.tv4.setText(String.valueOf(zijinBean.value3));
			holder.tv5.setVisibility(View.GONE);
			return convertView;
		}
		
	}
	
	class MarketHolder{
		TextView tv1;
		TextView tv2;
		TextView tv3;
		ItemView tv4;
	}
	
	class MingXiHolder{
		TextView tv1;
		TextView tv2;
		TextView tv3;
		TextView tv4;
		TextView tv5;
	}
	
	class FenJiaHolder{
		TextView tv1;
		TextView tv2;
		TextView tv3;
		TextView tv4;
		ItemView img5;
	}
	
	class FenJiaBean{
		double price;
		int liang;
		double maipan;
		double chengjiao;
		int length;
		
		public FenJiaBean(double price, int liang, double maipan,
				double chengjiao, int length) {
			super();
			this.price = price;
			this.liang = liang;
			this.maipan = maipan;
			this.chengjiao = chengjiao;
			this.length = length;
		}

	}
	
	class ZiJinBean{
		String leixing;
		int liuru;
		double bilieru;
		double liuchu;
		int biliechu;
		public ZiJinBean(String leixing, int liuru, double bilieru,
				double liuchu, int biliechu) {
			super();
			this.leixing = leixing;
			this.liuru = liuru;
			this.bilieru = bilieru;
			this.liuchu = liuchu;
			this.biliechu = biliechu;
		}
		
	}
	
	class StockBean{
		String name;
		int value1;
		double value2;
		int value3;
		
		public StockBean(String name, int value1, double value2, int value3) {
			super();
			this.name = name;
			this.value1 = value1;
			this.value2 = value2;
			this.value3 = value3;
		}
		
	}
}