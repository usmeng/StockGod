package cn.chinat2t.stockgod.views;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import cn.chinat2t.stockgod.R;
import cn.chinat2t.stockgod.bean.KLineBean;
import cn.chinat2t.stockgod.bean.KlineData;
import cn.chinat2t.stockgod.bean.LineEntity;
import cn.chinat2t.stockgod.http.CommunicationManager;
import cn.chinat2t.stockgod.http.CommunicationResultListener;
import cn.chinat2t.stockgod.utils.DataUtil;

public class KLineView extends LinearLayout{

	private View view;
	private TextView ma5;
	private TextView ma10;
	private TextView ma25;
	private KLineBean kLineBean;
//	private KlineViewHolder viewHolder;
	private MACandleStickChart macandlestickchart;
	private StickChart stickchart;
	private List<KlineData> lineList = new ArrayList<KlineData>();
	private List<Float> list;
	private List<Float> list2;
	private List<Float> list3;
	
/*	private class KlineViewHolder{
		public LinearLayout layou;
		public TextView     date;
		public TextView     time;
		public TextView     open;
		public TextView     high;
		public TextView     close;
		public TextView     low;
		public TextView     shou;
		public TextView     zhang;
	}*/

	public KLineView(Context context) {
		super(context);
		
		view = LayoutInflater.from(getContext()).inflate(R.layout.candle_stick_chart, null);
		addView(view, new LinearLayout.LayoutParams(-1, -2));
		
		initViews();
		
		CommunicationManager.getInstance().getDayLine("SH600000", dayResultListener);
	}

	private void initViews() {
		macandlestickchart = (MACandleStickChart) view.findViewById(R.id.kline_stock_tu1);
		macandlestickchart.setShowCandleSticks(true);
		macandlestickchart.setMaxCandleSticksNum(50);
		macandlestickchart.setLatitudeNum(4);
		macandlestickchart.setLongtitudeNum(4);
		macandlestickchart.setBackgroudColor(Color.TRANSPARENT);
		
		stickchart = (StickChart) view.findViewById(R.id.kline_stock_tu2);
		stickchart.setMaxStickDataNum(50);
		stickchart.setLatitudeNum(2);
		stickchart.setLongtitudeNum(4);
		stickchart.setDisplayAxisXTitle(false);
		stickchart.setBackgroudColor(Color.TRANSPARENT);
		
		macandlestickchart.addNotify(stickchart);
		
		/*viewHolder = new KlineViewHolder();
		viewHolder.layou = (LinearLayout) view.findViewById(R.id.kline_info_layout);
		viewHolder.layou.setVisibility(View.GONE);
		viewHolder.date = (TextView) view.findViewById(R.id.kline_date);
		viewHolder.time = (TextView) view.findViewById(R.id.kline_time);
		viewHolder.open = (TextView) view.findViewById(R.id.kline_open);
		viewHolder.high = (TextView) view.findViewById(R.id.kline_high);
		viewHolder.close = (TextView) view.findViewById(R.id.kline_close);
		viewHolder.low = (TextView) view.findViewById(R.id.kline_low);
		viewHolder.shou = (TextView) view.findViewById(R.id.kline_liang);
		viewHolder.zhang = (TextView) view.findViewById(R.id.kline_zhangdie);*/
	}

	private CommunicationResultListener dayResultListener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			switch (result) {
			case CommunicationManager.FAIL:
				break;
				
			case CommunicationManager.SUCCEED:
				if(token > 0){
					Message msg = mHandler.obtainMessage();
					msg.arg1 = 1;
					msg.obj = resultData;
					mHandler.sendMessage(msg);
				}
				break;
			}
		};
	};

	private void updateMAStr(int position) {
		if(position >= lineList.size() || position < 0) return;
		DecimalFormat format = new DecimalFormat("#.00");
		if(ma5 != null) ma5.setText(("MA5=" + format.format(list.get(position))));
		if(ma10 != null) ma10.setText("MA10=" + format.format(list2.get(position)));
		if(ma25 != null) ma25.setText("MA25=" + format.format(list3.get(position)));
	};
	
	private List<LineEntity> getLineEntityList() {
		List<LineEntity> lines = new ArrayList<LineEntity>();

		list = DataUtil.initMA(5, lineList);
		LineEntity MA5 = new LineEntity();
		MA5.setTitle("MA5");
		MA5.setLineColor(Color.rgb(222, 63, 209));
		MA5.setLineData(list);
		lines.add(MA5);
		
		list2 = DataUtil.initMA(10, lineList);
		LineEntity MA10 = new LineEntity();
		MA10.setTitle("MA10");
		MA10.setLineColor(Color.RED);
		MA10.setLineData(list2);
		lines.add(MA10);

		list3 = DataUtil.initMA(20, lineList);
		LineEntity MA25 = new LineEntity();
		MA25.setTitle("MA25");
		MA25.setLineColor(Color.rgb(52, 129, 1));
		MA25.setLineData(list3);
		lines.add(MA25);
		return lines;
	}
	
	Handler mHandler = new Handler(){

		public void handleMessage(android.os.Message msg) {
			switch (msg.arg1) {
			case 1:
				kLineBean = (KLineBean) msg.obj;
				lineList = kLineBean.kline;
				if (lineList == null) return;
				macandlestickchart.setOHLCData(lineList);
				macandlestickchart.setLineData(getLineEntityList());
				int length = lineList.size() > 50 ? 50 : lineList.size();
				macandlestickchart.setMaxCandleSticksNum(length);
				macandlestickchart.setStartIndex(lineList.size() - length);
				macandlestickchart.invalidate();

				stickchart.setStickData(lineList);
				stickchart.setMaxStickDataNum(length);
				stickchart.setStartIndex(lineList.size() - length);
				stickchart.invalidate();

				updateMAStr(lineList.size() - 1);

				break;
			}
		}

	};
}