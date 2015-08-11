package cn.chinat2t.stockgod.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import cn.chinat2t.stockgod.bean.KlineData;
import cn.chinat2t.stockgod.utils.CtLog;

public class StickChart extends GridChart {
	
	////////////默认值///////////////
	/** 显示纬线数 */
	public static final int DEFAULT_LATITUDE_NUM = 4;
	
	/** 显示纬线数 */
	public static final int DEFAULT_LONGTITUDE_NUM = 3;
	
	/** 柱条边框色 */
	public static final int DEFAULT_STICK_BORDER_COLOR = Color.RED;
	
	/** 柱条填充色 */
	public static final int DEFAULT_STICK_FILL_COLOR = Color.RED;

	/** 阴线填充色 */
	public static final int DEFAULT_NEGATIVE_STICK_FILL_COLOR = Color.parseColor("#ff0CA8CC");

	/** 柱条边框色 */
	private int stickBorderColor = DEFAULT_STICK_BORDER_COLOR ;

	/** 柱条填充色 */
	private int stickFillColor = DEFAULT_STICK_FILL_COLOR;
	
	/** 阴线填充色 */
	private int negativeStickFillColor = DEFAULT_NEGATIVE_STICK_FILL_COLOR;
	
	/** 显示纬线数 */
	private int latitudeNum = DEFAULT_LATITUDE_NUM;
	
	/** 显示经线数 */
	private int longtitudeNum = DEFAULT_LONGTITUDE_NUM;
	
	/** K线数据 */
	private List<KlineData> StickData;
	
	/** 图表中的�蜡烛线 */
	private int maxStickDataNum;

	/** K线显示最高价格 */
	protected float maxValue;

	/** K线显示最低价格 */
	protected float minValue;	
	
	private int startIndex = 0;
	
	private int type = 0;
	
	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public void setStartIndex(int startIndex) {
		this.startIndex = startIndex;
	}

//	private int selectIndex = 0;
	
	/////////////////构造函数///////////////

	public StickChart(Context context) {
		super(context);
	}

	public StickChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public StickChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	///////////////函数方程////////////////
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		initMaxPrice();
		drawSticks(canvas);
		initAxisY();
		initAxisX();

		super.onDraw(canvas);
	}
	
	private void initMaxPrice() {
		if(startIndex < 0) return;
		for(int i = 0 ; i < maxStickDataNum && i + startIndex < StickData.size(); i++){
			float high = (float) StickData.get(i + startIndex).getShouHigh();
			if(i == 0){
				this.maxValue = (float) high;
			}
			if (this.maxValue < high){
				this.maxValue = (float) high;
			}
		}
	}

	/**
	 * 获取X轴刻度位置,�?�??�最大1
	 * @param value
	 * @return
	 */
	@Override
	public String getAxisXGraduate(Object value){
		float graduate = Float.valueOf(super.getAxisXGraduate(value));
		int index = (int) Math.floor(graduate*maxStickDataNum);
		
		if(index >= maxStickDataNum){
			index = maxStickDataNum -1;
		}else if(index < 0){
			index = 0;
		}
		
		return StickData.get(index).getDate();
	}
	
	/**
	 * 获取Y轴刻度位置,�?�??�最大1
	 * @param value
	 * @return
	 */
	@Override
	public String getAxisYGraduate(Object value){
		float graduate = Float.valueOf(super.getAxisYGraduate(value));
		return  String.valueOf((int)Math.floor(graduate * (maxValue - minValue) + minValue));
	}
	
	/**
	 * 获得来自其他图�?��通知
	 */
	@Override
	public void notifyEvent(GridChart chart) {
		
		CandleStickChart candlechart = (CandleStickChart)chart;
		
		this.maxStickDataNum = candlechart.getMaxCandleSticksNum();
		this.startIndex = candlechart.getStartIndex();
//		selectIndex = candlechart.getSelectedIndex();
		clickPostX = candlechart.clickPostX;
		
		//不显示Y轴信息
		super.setDisplayCrossYOnTouch(false);
		//�?���??通知
		super.notifyEvent(chart);
		//对外�??通知
		super.notifyEventAll(this);
	}
	
	/**
	 * 初始化X轴
	 */
	protected void initAxisX() {
		List<String> TitleX = new ArrayList<String>();
		if(null != StickData && maxStickDataNum > 0){
			float average = maxStickDataNum / longtitudeNum;
			//�?��刻度
			for (int i = 0; i < longtitudeNum; i++) {
				int index = (int) Math.floor(i * average);
				if(index > maxStickDataNum-1){
					index = maxStickDataNum-1;
				}
				//追�??�?
				TitleX.add(StickData.get(index).getDate());
			}
			TitleX.add(StickData.get(maxStickDataNum-1).getDate());
		}
		super.setAxisXTitles(TitleX);
	}
	
	public int getSelectedIndex() {
		if(null == super.getTouchPoint()){
			return 0;
		}
		float graduate = Float.valueOf(super.getAxisXGraduate(super.getTouchPoint().x));
		int index = (int) Math.floor(graduate*maxStickDataNum);
		
		if(index >= maxStickDataNum){
			index = maxStickDataNum -1;
		}else if(index < 0){
			index = 0;
		}
		
		return index;
	}
	
	/**
	 * 多点触控事件
	 */
	protected void drawWithFingerMove() {
	}
	
	@Override
	protected void drawWithFingerClick(Canvas canvas) {
	}
	
	/**
	 * 初始化Y轴
	 */
	protected void initAxisY() {
		List<String> TitleY = new ArrayList<String>();
		float average = (maxValue - minValue) / latitudeNum;
		
		CtLog.d("stick y average = "+average);
		//X轴刻度
		for (int i = 0; i < latitudeNum; i++) {
			String value = String.valueOf((int)(minValue + i * average));
			
			if(value.length() < super.getAxisYMaxTitleLength()){
				while(value.length() < super.getAxisYMaxTitleLength()){
					value = new String(" ") + value;
				}
			}
			TitleY.add(value);
			CtLog.d("stick y value = "+value);
		}
		//X轴的值
		String value = String.valueOf((int)maxValue);
		if(value.length() < super.getAxisYMaxTitleLength()){
			while(value.length() < super.getAxisYMaxTitleLength()){
				value = new String(" ") + value;
			}
		}
		TitleY.add(value);

		super.setAxisYTitles(TitleY);
	}

	/**
	 * 绘制柱状线
	 * @param canvas
	 */
	float stickWidth;
	protected void drawSticks(Canvas canvas) {
		int offset = 3;
		// 蜡烛棒宽度
		float stickWidth = ((super.getWidth() - super.getAxisMarginLeft()-super.getAxisMarginRight() - 2 + offset) / maxStickDataNum) - offset;
		// 蜡烛棒起始绘制位置
		float stickX = super.getAxisMarginLeft() + 2;

		Paint mPaintStick = new Paint();
		mPaintStick.setColor(stickFillColor);

		Paint mPaintNegative = new Paint();
		mPaintNegative.setColor(negativeStickFillColor);

		Paint paint = null;
		if(null != StickData){
			
			//判断显示为方柱或显示为线条
			for (int i = startIndex; i < StickData.size(); i++) {
				KlineData ohlc = StickData.get(i);
				if(type == 1){
					paint = new Paint();
					paint.setColor(Color.YELLOW);
				}else{
					if(ohlc.getOpen() < ohlc.getClose()){
						paint = mPaintStick;
					}else{
						paint = mPaintNegative;
					}
				}
				if(ohlc.getClose() <= 0 && ohlc.getOpen() <= 0 && ohlc.getHigh() <= 0) break;
				float highY = (float) ((1f - (ohlc.getShouHigh() - minValue)
						/ (maxValue - minValue)) * (super.getHeight() - super
						.getAxisMarginBottom() - 2) - super.getAxisMarginTop() - 2);
				float lowY = (float) ((1f - (ohlc.getShouLow() - minValue)
						/ (maxValue - minValue)) * (super.getHeight() - super
						.getAxisMarginBottom() - 2) - super.getAxisMarginTop());
	
				//绘制数据宽度判断绘制直线或方柱
				if(stickWidth >= 2f){
					canvas.drawRect(stickX, highY+super.getAxisMarginTop() + 1, stickX + stickWidth, lowY+super.getAxisMarginTop(), paint);
				}else{
					canvas.drawLine(stickX, highY+super.getAxisMarginTop() + 1, stickX , lowY+super.getAxisMarginTop(), paint);
				}
				
				//X位移
				stickX = stickX + offset + stickWidth;
			}
		}
	}
	
	//Push数据绘制K线图
	public void pushData(KlineData entity){
		if(null != entity){
			//追加数据到数据列表
			addData(entity);
			//强制重画
			super.postInvalidate();
		}
	}
	
	//Push数据绘制K线图
	public void addData(KlineData entity){
		if(null != entity){
			//追加数据
			if(null == StickData || 0 == StickData.size() ){
				StickData = new ArrayList<KlineData>();
			}
			
			this.StickData.add(entity);
		}
	}
	
	//////////////属性GetterSetter/////////////////
	
	public List<KlineData> getStickData() {
		return StickData;
	}

	public void setStickData(List<KlineData> stickData) {
		//清空已有数据
		startIndex = 0;
		if(null != StickData){
			StickData.clear();
		}
		for(KlineData e :stickData){
			addData(e);
		}
	}

	public int getStickFillColor() {
		return stickFillColor;
	}

	public void setStickFillColor(int stickFillColor) {
		this.stickFillColor = stickFillColor;
	}

	public int getLatitudeNum() {
		return latitudeNum;
	}

	public void setLatitudeNum(int latitudeNum) {
		this.latitudeNum = latitudeNum;
	}

	public int getMaxStickDataNum() {
		return maxStickDataNum;
	}

	public void setMaxStickDataNum(int maxStickDataNum) {
		this.maxStickDataNum = maxStickDataNum;
	}

	public float getMaxValue() {
		return maxValue;
	}

	public void setMaxValue(float maxValue) {
		this.maxValue = maxValue;
	}

	public float getMinValue() {
		return minValue;
	}

	public void setMinValue(float minValue) {
		this.minValue = minValue;
	}

	public int getStickBorderColor() {
		return stickBorderColor;
	}

	public void setStickBorderColor(int stickBorderColor) {
		this.stickBorderColor = stickBorderColor;
	}

	public int getLongtitudeNum() {
		return longtitudeNum;
	}

	public void setLongtitudeNum(int longtitudeNum) {
		this.longtitudeNum = longtitudeNum;
	}
}
