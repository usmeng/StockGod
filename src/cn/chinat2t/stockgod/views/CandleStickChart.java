package cn.chinat2t.stockgod.views;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.util.AttributeSet;
import android.util.FloatMath;
import android.view.MotionEvent;
import cn.chinat2t.stockgod.R;
import cn.chinat2t.stockgod.bean.KlineData;
import cn.chinat2t.stockgod.bean.LineEntity;
import cn.chinat2t.stockgod.bean.StringView;
import cn.chinat2t.stockgod.utils.CtLog;
import cn.chinat2t.stockgod.utils.DataUtil;
import cn.chinat2t.stockgod.utils.StringUtil;

public class CandleStickChart extends GridChart {

	private static final int STRICK_OFFSET = 3;

	// //////////默认值///////////////
	/** 显示纬线数 */
	public static final int DEFAULT_LATITUDE_NUM = 4;

	/** 显示纬线数 */
	public static final int DEFAULT_LONGTITUDE_NUM = 4;

	/** 阳线边红色 */
	public static final int DEFAULT_POSITIVE_STICK_BORDER_COLOR = Color.RED;

	/** 阳线填充色 */
	public static final int DEFAULT_POSITIVE_STICK_FILL_COLOR = Color.RED;

	/** 阴线边绿色 */
	public static final int DEFAULT_NEGATIVE_STICK_BORDER_COLOR = Color.GREEN;

	/** 阴线填充色 */
	public static final int DEFAULT_NEGATIVE_STICK_FILL_COLOR = Color
			.parseColor("#ff0CA8CC");

	/** 十字线颜色 */
	public static final int DEFAULT_CROSS_STICK_COLOR = DEFAULT_POSITIVE_STICK_BORDER_COLOR;

	/** 阳线颜色 */
	private int positiveStickBorderColor = DEFAULT_POSITIVE_STICK_BORDER_COLOR;

	/** 阳线填充色 */
	private int positiveStickFillColor = DEFAULT_POSITIVE_STICK_FILL_COLOR;

	/** 阴线颜色 */
	private int negativeStickBorderColor = DEFAULT_NEGATIVE_STICK_BORDER_COLOR;

	/** 阴线填充色 */
	private int negativeStickFillColor = DEFAULT_NEGATIVE_STICK_FILL_COLOR;

	/** 十字线颜色 */
	private int crossStickColor = DEFAULT_CROSS_STICK_COLOR;

	/** 显示纬线数 */
	private int latitudeNum = DEFAULT_LATITUDE_NUM;

	/** 显示经线数 */
	private int longtitudeNum = DEFAULT_LONGTITUDE_NUM;

	/** K线数据 */
	private List<KlineData> OHLCData;

	/** MA的几条线显示数据 */
	private List<LineEntity> lineList;

	/** 图表中多少个蜡烛线 */
	private int maxCandleSticksNum;

	/** K线显示最高价格 */
	private float maxPrice = 0;

	/** K线显示最低价格 */
	private float minPrice = 0;

	private int startIndex = 0; // 开始显示的位置

	private float stickWidth = 10; // 柱的宽度

	private boolean showCrossing = false;

	private boolean showCandleSticks;

	/** 是否显示全部 */
	private boolean displayAll = true;

	private float nowclose;

	public CandleStickChart(Context context) {
		super(context);
	}

	public CandleStickChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public CandleStickChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	// /////////////函数方法////////////////

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		initMaxAndMin();
		drawLines(canvas);
		drawCandleSticks(canvas);
		initAxisY();
		initAxisX();
		super.onDraw(canvas);
		drawLineTitle(canvas);
	}

	// 绘制平均线
	protected void drawLines(Canvas canvas) {
		if (!(null != this.lineList && this.lineList.size() > 0)) return;
		lineLength = ((getWidth() - getAxisMarginLeft() - getAxisMarginRight() + 1) / getMaxCandleSticksNum()) - 1;
		// 起始位置
		float startX;
		int size = lineList.size();
		// 逐条输出MA线
		for (int i = 0; i < size; i++) {
			LineEntity line = (LineEntity) lineList.get(i);
			if (line.isDisplay()) {
				Paint mPaint = new Paint();
				mPaint.setColor(line.getLineColor());
				mPaint.setAntiAlias(true);
				List<Float> lineData = line.getLineData();
				// 输出X线
				startX = super.getAxisMarginLeft() + lineLength / 2f;
				// 定义起始点
				PointF ptFirst = null;
				if (lineData != null) {
					float heigh = (super.getHeight() - super.getAxisMarginBottom() - super.getAxisMarginTop() - 2);
					for (int j = startIndex; j < lineData.size(); j++) {
						float value = lineData.get(j).floatValue();
						// 获取终点Y坐标
						float valueY = (float) ((1f - (value - getMinPrice()) / (getMaxPrice() - getMinPrice())) * heigh) + getAxisMarginTop();
						// 绘制线条
						if (j > startIndex) {
							canvas.drawLine(ptFirst.x, ptFirst.y, startX, valueY, mPaint);
						}
						// 重置起始点
						ptFirst = new PointF(startX, valueY);
						// X位移
						startX = startX + 1 + lineLength;
					}
				}
			}
		}
	}

	// //////////属性GetterSetter//////////////
	public boolean isDisplayAll() {
		return displayAll;
	}

	public void setDisplayAll(boolean displayAll) {
		this.displayAll = displayAll;
	}

	public List<LineEntity> getLineData() {
		return lineList;
	}

	public void setLineData(List<LineEntity> lineData) {
		this.lineList = lineData;
		if(lineData != null && lineData.size() > 0){
			setAxisMarginTop(20f);
		}
	}

	public void setMiddleValue(float closes) {
		nowclose = closes;
	}
	
	protected void initMaxAndMin() {
		if (startIndex < 0)
			return;
		for (int i = 0; i < maxCandleSticksNum && i + startIndex < OHLCData.size(); i++) {
			float low = (float) OHLCData.get(i + startIndex).getLow();
			float high = (float) OHLCData.get(i + startIndex).getHigh();
			if (i == 0) {
				this.minPrice = low;
				this.maxPrice = (float) high;
			}
			if (this.minPrice > low) {
				this.minPrice = (float) low;
			}

			if (this.maxPrice < high) {
				this.maxPrice = (float) high;
			}
		}
		// 计算MA线的最大值和最小值
		if (lineList != null && lineList.size() > 0) {
			for (int i = 0; i < lineList.size(); i++) {
				LineEntity line = (LineEntity) lineList.get(i);
				List<Float> lineData = line.getLineData();
				if (lineData != null) {
					for (int j = 0; j < maxCandleSticksNum && j + startIndex < lineData.size(); j++) {
						float value = lineData.get(j + startIndex).floatValue();
						if (value > getMaxPrice()) {
							setMaxPrice(value);
						} else if (value < getMinPrice()) {
							setMinPrice(value);
						}
					}
				}
			}
		}
	}

	/**
	 * 获取X轴刻度位置,�?�??�最大1
	 * 
	 * @param value
	 * @return
	 */
	@Override
	public String getAxisXGraduate(Object value) {
		float graduate = Float.valueOf(super.getAxisXGraduate(value));
		int index = (int) Math.floor(graduate * maxCandleSticksNum);

		if (index >= maxCandleSticksNum) {
			index = maxCandleSticksNum - 1;
		} else if (index < 0) {
			index = 0;
		}

		// 返回X轴值
		return OHLCData.get(startIndex + index).getDate();
	}

	public int getSelectedIndex() {
		if (null == super.getTouchPoint()) {
			return 0;
		}
		float graduate = Float.valueOf(super.getAxisXGraduate(super.getTouchPoint().x));
		int index = (int) Math.floor(graduate * maxCandleSticksNum);

		if (index >= maxCandleSticksNum) {
			index = maxCandleSticksNum - 1;
		} else if (index < 0) {
			index = 0;
		}

		return index;
	}

	/**
	 * 获取Y轴刻度位置,�?�??�最大1
	 * 
	 * @param value
	 * @return
	 */
	@Override
	public String getAxisYGraduate(Object value) {
		float graduate = Float.valueOf(super.getAxisYGraduate(value));
		return String.valueOf((int) Math.floor(graduate * (maxPrice - minPrice) + minPrice));
	}

	/**
	 * 多点触控事件
	 */
	protected void drawWithFingerMove() {
	}

	/**
	 * 初始化X轴
	 */
	protected void initAxisX() {
		if (startIndex < 0) return;
		List<String> TitleX = new ArrayList<String>();
		if (null != OHLCData) {
			float average = maxCandleSticksNum / longtitudeNum;
			// x刻度
			for (int i = 0; i < longtitudeNum; i++) {
				int index = (int) Math.floor(i * average);
				if (index >= maxCandleSticksNum) {
					index = maxCandleSticksNum - 1;
				}

				if (index + startIndex < OHLCData.size()) {
					TitleX.add(OHLCData.get(index + startIndex).getDate());
				} else {
					TitleX.add(OHLCData.get(OHLCData.size() - 1).getDate());
				}
			}
			if (startIndex + maxCandleSticksNum < OHLCData.size()) {
				TitleX.add(OHLCData.get(startIndex + maxCandleSticksNum).getDate());
			} else {
				TitleX.add(OHLCData.get(OHLCData.size() - 1).getDate());
			}
		}
		super.setAxisXTitles(TitleX);
	}

	/**
	 * 初始化Y轴
	 */
	protected void initAxisY() {
		if (startIndex < 0) return;
		List<String> TitleY = new ArrayList<String>();
		float average = (maxPrice - minPrice) / latitudeNum;
		// 刻度
		for (int i = 0; i < latitudeNum; i++) {
			String value = String.format("%.2f", minPrice + i * average);
			if (value.length() < super.getAxisYMaxTitleLength()) {
				while (value.length() < super.getAxisYMaxTitleLength()) {
					value = " " + value;
				}
			}
			TitleY.add(value);
		}
		// 最大值
		String value = String.format("%.2f", maxPrice);
		if (value.length() < super.getAxisYMaxTitleLength()) {
			while (value.length() < super.getAxisYMaxTitleLength()) {
				value = " " + value;
			}
		}
		TitleY.add(value);

		super.setAxisYTitles(TitleY);
	}

	/**
	 * 绘制蜡烛线
	 * 
	 * @param canvas
	 */
	protected void drawCandleSticks(Canvas canvas) {
		if (!showCandleSticks) return;
		int offset = STRICK_OFFSET;
		// 蜡烛棒宽度
		stickWidth = ((super.getWidth() - super.getAxisMarginLeft() - super.getAxisMarginRight() + offset) / maxCandleSticksNum) - offset;
		// 蜡烛棒起始绘制位置
		float stickX = super.getAxisMarginLeft() + 1;

		Paint mPaintPositive = new Paint();
		mPaintPositive.setColor(positiveStickFillColor);

		Paint mPaintNegative = new Paint();
		mPaintNegative.setColor(negativeStickFillColor);

		Paint mPaintCross = new Paint();
		mPaintCross.setColor(crossStickColor);

		if (null != OHLCData) {
			float heigh = super.getHeight() - super.getAxisMarginBottom() - super.getAxisMarginTop();
			for (int i = startIndex; i < OHLCData.size(); i++) {
				KlineData ohlc = OHLCData.get(i);
				float openY = (float) ((1f - (ohlc.getOpen() - minPrice) / (maxPrice - minPrice)) * heigh);
				float highY = (float) ((1f - (ohlc.getHigh() - minPrice) / (maxPrice - minPrice)) * heigh);
				float lowY = (float) ((1f - (ohlc.getLow() - minPrice) / (maxPrice - minPrice)) * heigh);
				float closeY = (float) ((1f - (ohlc.getClose() - minPrice) / (maxPrice - minPrice)) * heigh);

				// 计算和生产K线中阴线和阳线
				if (ohlc.getOpen() < ohlc.getClose()) {
					// 阳线
					// 根据宽度判断是否绘制立柱
					if (stickWidth >= 2f) {
						canvas.drawRect(stickX,
								closeY + super.getAxisMarginTop(), stickX + stickWidth,
								openY + super.getAxisMarginTop(),
								mPaintPositive);
					}
					canvas.drawLine(stickX + stickWidth / 2f,
							highY + super.getAxisMarginTop(), stickX + stickWidth / 2f,
							lowY + super.getAxisMarginTop(), mPaintPositive);
				} else if (ohlc.getOpen() > ohlc.getClose()) {
					// 阴线
					// 根据宽度判断是否绘制立柱
					if (stickWidth >= 2f) {
						canvas.drawRect(stickX,
								openY + super.getAxisMarginTop(), stickX + stickWidth,
								closeY + super.getAxisMarginTop(),
								mPaintNegative);
					}
					canvas.drawLine(stickX + stickWidth / 2f,
							highY + super.getAxisMarginTop(), stickX + stickWidth / 2f,
							lowY + super.getAxisMarginTop(), mPaintNegative);
				} else {
					// 十字线
					// 根据宽度判断是否绘制横线
					if (stickWidth >= 2f) {
						canvas.drawLine(stickX,
								closeY + super.getAxisMarginTop(), stickX + stickWidth,
								openY + super.getAxisMarginTop(), mPaintCross);
					}
					canvas.drawLine(stickX + stickWidth / 2f,
							highY + super.getAxisMarginTop(), stickX + stickWidth / 2f,
							lowY + super.getAxisMarginTop(), mPaintCross);
				}

				// X位移
				stickX = stickX + offset + stickWidth;
			}
		}
	}

	// Push数据绘制K线图
	public void pushData(KlineData entity) {
		if (null != entity) {
			// 追加数据到数据列表
			addData(entity);
			// 强制重画
			super.postInvalidate();
		}
	}

	public void addData(KlineData entity) {
		if (null != entity) {
			// 追加数据
			if (null == OHLCData || 0 == OHLCData.size()) {
				OHLCData = new ArrayList<KlineData>();
			}

			this.OHLCData.add(entity);
		}
	}

	// ////////////属性GetterSetter/////////////////

	public List<KlineData> getOHLCData() {
		return OHLCData;
	}

	public void setOHLCData(List<KlineData> data) {
		startIndex = 0;
		// 删除已有数据
		if (null != OHLCData) {
			OHLCData.clear();
		}
		for (KlineData e : data) {
			addData(e);
		}
	}

	public int getPositiveStickBorderColor() {
		return positiveStickBorderColor;
	}

	public void setPositiveStickBorderColor(int positiveStickBorderColor) {
		this.positiveStickBorderColor = positiveStickBorderColor;
	}

	public int getPositiveStickFillColor() {
		return positiveStickFillColor;
	}

	public void setPositiveStickFillColor(int positiveStickFillColor) {
		this.positiveStickFillColor = positiveStickFillColor;
	}

	public int getNegativeStickBorderColor() {
		return negativeStickBorderColor;
	}

	public void setNegativeStickBorderColor(int negativeStickBorderColor) {
		this.negativeStickBorderColor = negativeStickBorderColor;
	}

	public int getNegativeStickFillColor() {
		return negativeStickFillColor;
	}

	public void setNegativeStickFillColor(int negativeStickFillColor) {
		this.negativeStickFillColor = negativeStickFillColor;
	}

	public int getCrossStickColor() {
		return crossStickColor;
	}

	public void setCrossStickColor(int crossStickColor) {
		this.crossStickColor = crossStickColor;
	}

	public int getLatitudeNum() {
		return latitudeNum;
	}

	public void setLatitudeNum(int latitudeNum) {
		this.latitudeNum = latitudeNum;
	}

	public int getMaxCandleSticksNum() {
		return maxCandleSticksNum;
	}

	public void setMaxCandleSticksNum(int maxCandleSticksNum) {
		this.maxCandleSticksNum = maxCandleSticksNum;
	}

	public float getMaxPrice() {
		return maxPrice;
	}

	public void setMaxPrice(float maxPrice) {
		this.maxPrice = maxPrice;
	}

	public float getMinPrice() {
		return minPrice;
	}

	public void setMinPrice(float minPrice) {
		this.minPrice = minPrice;
	}

	public int getLongtitudeNum() {
		return longtitudeNum;
	}

	public void setLongtitudeNum(int longtitudeNum) {
		this.longtitudeNum = longtitudeNum;
	}

	public int getStartIndex() {
		return startIndex;
	}

	public boolean isShowCandleSticks() {
		return showCandleSticks;
	}

	public void setShowCandleSticks(boolean sShowCandleSticks) {
		this.showCandleSticks = sShowCandleSticks;
	}

	private final int NONE = 0;
	private final int ZOOM = 1;
	private final int DOWN = 2;
	private final int LONGPRESS = 3;
	private final int SINGLEMOVE = 4;

	private float olddistance = 0f;
	private float newdistance = 0f;

	private int TOUCH_MODE;
	private PointF startPoint = new PointF();
	private PointF currentPoint = new PointF();
	private boolean isDisplayPopMune = true;
	private boolean isView;

	@Override
	public boolean onTouchEvent(MotionEvent event) {

		final float MIN_LENGTH = (super.getWidth() / 40) < 5 ? 5 : (super.getWidth() / 50);

		switch (event.getAction() & MotionEvent.ACTION_MASK) {
		// 设置拖拉模�?
		case MotionEvent.ACTION_DOWN:
			CtLog.d("candle", "ACTION_DOWN--------------------");
			TOUCH_MODE = DOWN;
			startPoint.set(event.getX(), event.getY());
			final PointF downP = new PointF(event.getX(), event.getY());
			this.postDelayed(new Runnable() {
				@Override
				public void run() {
					if (TOUCH_MODE == DOWN && spacing(startPoint, downP) == 0) {
						setTouchPoint(startPoint);
						TOUCH_MODE = LONGPRESS;
						refresh();
					}
				}
			}, 500l);

			break;
		case MotionEvent.ACTION_UP:
		case MotionEvent.ACTION_POINTER_UP:
			CtLog.d("candle", "ACTION_UP--------------------");
			TOUCH_MODE = NONE;
			setTouchPoint(null);
			showCrossing = false;
			return super.onTouchEvent(event);
			// 设置多点触摸模�?
		case MotionEvent.ACTION_POINTER_DOWN:
			CtLog.d("candle", "ACTION_POINTER_DOWN--------------------");
			if (!showCrossing) {
				olddistance = spacing(event);
				if (olddistance > MIN_LENGTH) {
					TOUCH_MODE = ZOOM;
				}
			}
			break;
		case MotionEvent.ACTION_MOVE:
			// CtLog.d("candle", "ACTION_MOVE--------------------");
			currentPoint.set(event.getX(), event.getY()); // 设置当前手指的坐标
			if (TOUCH_MODE == ZOOM) {
				newdistance = spacing(event);
				if (newdistance > MIN_LENGTH && Math.abs(newdistance - olddistance) > MIN_LENGTH) {

					if (newdistance > olddistance) {
						zoomIn();
					} else {
						zoomOut();
					}
					// 重置距离
					olddistance = newdistance;
					super.postInvalidate();
					super.notifyEventAll(this);
				}
			} else if (TOUCH_MODE == DOWN) {
				move(event);
			} else if (TOUCH_MODE == LONGPRESS) { // 长按后移动，显示十字线
				isView = true;
				float distance = currentPoint.x - startPoint.x;
				if (Math.abs(distance) >= stickWidth) {
					startPoint.x = currentPoint.x;
					super.postInvalidate();
					super.notifyEventAll(this);
				}
			}
			break;
		}
		return true;
	}

	public void move(MotionEvent event) {
		float distance = event.getX() - startPoint.x;
		if (Math.abs(distance) >= stickWidth) {
			startPoint.x = event.getX();
			int step = (int) (Math.abs(distance) / stickWidth);
			if (distance < 0) {
				startIndex += step;
				if (startIndex + maxCandleSticksNum > OHLCData.size()) {
					startIndex = OHLCData.size() - maxCandleSticksNum - 1;
				}
			} else if (distance > 0) {
				startIndex -= step;
				if (startIndex < 0) {
					startIndex = 0;
				}
			}
			if (startIndex < 0) {
				startIndex = 0;
			}
			if (updateListener != null) {
				updateListener.update(startIndex + maxCandleSticksNum, isView);
			}
			super.postInvalidate();
			super.notifyEventAll(this);
		}
	}

	public void setStartIndex(int startIndex) {
		if (startIndex >= 0)
			this.startIndex = startIndex;
	}

	protected void zoomIn() {
		if (maxCandleSticksNum > 23) {
			maxCandleSticksNum = maxCandleSticksNum - 3;
		}
	}

	protected void zoomOut() {
		int maxNum = (OHLCData.size() - 1) > 150 ? 150 : OHLCData.size() - 1;
		if (maxCandleSticksNum < maxNum) {
			maxCandleSticksNum = maxCandleSticksNum + 3;
			if (startIndex + maxCandleSticksNum > OHLCData.size()) {
				startIndex = OHLCData.size() - maxCandleSticksNum - 1;
			}
		}
		if (startIndex < 0) {
			startIndex = 0;
		}
	}

	private void refresh() {
		super.postInvalidate();
		super.notifyEventAll(this);
	}

	// 计算移动距离
	private float spacing(MotionEvent event) {
		float x = event.getX(0) - event.getX(1);
		float y = event.getY(0) - event.getY(1);
		return FloatMath.sqrt(x * x + y * y);
	}

	private float spacing(PointF p1, PointF p2) {
		float x = p1.x - p2.x;
		float y = p1.y - p2.y;
		return FloatMath.sqrt(x * x + y * y);
	}

	@Override
	protected void drawWithFingerClick(Canvas canvas) {
		if (getTouchPoint() == null || !isDisplayPopMune) return;
		showCrossing = true;
		Paint mPaint = new Paint();
		mPaint.setColor(Color.CYAN);
		// 水平线长度
		float lineHLength = getWidth() - getAxisMarginLeft() - getAxisMarginRight() - 2;
		// 垂直线高度
		float lineVLength = getHeight() - getAxisMarginTop() - getAxisMarginBottom() - 2f;

		clickPostX = getTouchPoint().x;
		clickPostY = getTouchPoint().y;
		CtLog.d("candleStick clickPostX = " + clickPostX);

		int selectIndex = getSelectedIndex();
		if (OHLCData == null) return;
		KlineData ohlc = OHLCData.get(startIndex + selectIndex);
		if(showCandleSticks){
			clickPostX = super.getAxisMarginLeft() + stickWidth / 2 + selectIndex * (stickWidth + STRICK_OFFSET);
		}else{
			clickPostX = super.getAxisMarginLeft() + lineLength / 2 + selectIndex * (lineLength + 1);
		}
		clickPostY = (float) ((1f - (ohlc.getOpen() - minPrice) / (maxPrice - minPrice)) * lineVLength) + getAxisMarginTop();

		CtLog.d("candle", "selectIndex = " + selectIndex + "    clickPostX = " + clickPostX + "   clickPostY = " + clickPostY);
		if (clickPostX > 0) {
			// 显示纵线
			canvas.drawLine(clickPostX, getAxisMarginTop(), clickPostX, lineVLength + getAxisMarginTop(), mPaint);
		}
		if (clickPostY > 0) {
			// 显示横线
			canvas.drawLine(super.getAxisMarginLeft(), clickPostY,
					super.getAxisMarginLeft() + lineHLength, clickPostY, mPaint);
		}
		KlineData oldOhlc = null;
		if (startIndex + selectIndex - 1 > 0 && startIndex + selectIndex - 1 < OHLCData.size()) {
			oldOhlc = OHLCData.get(startIndex + selectIndex - 1);
		} else {
			oldOhlc = ohlc;
		}
		drawPopMune(canvas, clickPostX, ohlc, oldOhlc);
	}

	public void drawPopMune(Canvas canvas, float pointX, KlineData ohlc,
			KlineData oldOhlc) {
		List<StringView> list = new ArrayList<StringView>();
		if (type == 1) {
			list.add(new StringView("日期："));
			list.add(new StringView(StringUtil.getTime(ohlc.getDate()), Color.WHITE));
			list.add(new StringView("价格："));
			if (ohlc.noeprice > oldOhlc.noeprice) {
				list.add(new StringView(DataUtil.getDoubleString(ohlc.navprice), Color.RED));
			} else {
				list.add(new StringView(DataUtil.getDoubleString(ohlc.navprice), Color.GREEN));
			}
			list.add(new StringView("涨跌："));
			double zhangdie = (ohlc.navprice - oldOhlc.navprice) / oldOhlc.navprice * 100;
			if (zhangdie > 0) {
				list.add(new StringView(DataUtil.getDoubleString(zhangdie) + "%", Color.RED));
			} else {
				list.add(new StringView(DataUtil.getDoubleString(zhangdie) + "%", Color.GREEN));
			}
			list.add(new StringView("成交："));
			list.add(new StringView(ohlc.shou + "", Color.YELLOW));
		} else {
			list.add(new StringView("日期："));
			list.add(new StringView(StringUtil.getKlineDate(ohlc.getDate()), Color.WHITE));
			if (!"0000".equals(ohlc.getTime())) {
				list.add(new StringView("时间："));
				list.add(new StringView(StringUtil.getKlineTime(ohlc.getTime()), Color.WHITE));
			}
			list.add(new StringView("开盘："));
			if (ohlc.getOpen() >= oldOhlc.getOpen()) {
				list.add(new StringView(DataUtil.getDoubleString(ohlc.getOpen()), Color.RED));
			} else {
				list.add(new StringView(DataUtil.getDoubleString(ohlc.getOpen()), Color.GREEN));
			}
			list.add(new StringView("最高："));
			if (ohlc.getHigh() >= oldOhlc.getHigh()) {
				list.add(new StringView(DataUtil.getDoubleString(ohlc.getHigh()), Color.RED));
			} else {
				list.add(new StringView(DataUtil.getDoubleString(ohlc.getHigh()), Color.GREEN));
			}
			list.add(new StringView("最低："));
			if (ohlc.getLow() >= oldOhlc.getLow()) {
				list.add(new StringView(DataUtil.getDoubleString(ohlc.getLow()), Color.RED));
			} else {
				list.add(new StringView(DataUtil.getDoubleString(ohlc.getLow()), Color.GREEN));
			}
			list.add(new StringView("收盘："));
			if (ohlc.getClose() >= oldOhlc.getClose()) {
				list.add(new StringView(DataUtil.getDoubleString(ohlc.getClose()), Color.RED));
			} else {
				list.add(new StringView(DataUtil.getDoubleString(ohlc.getClose()), Color.GREEN));
			}
			list.add(new StringView("成交量："));
			list.add(new StringView(DataUtil.getDoubleString(ohlc.getShouHigh() / 10000) + "万", Color.YELLOW));
			list.add(new StringView("涨跌幅："));
			double zhangdie = (ohlc.close - oldOhlc.close) / oldOhlc.close * 100;
			if (zhangdie > 0) {
				list.add(new StringView(DataUtil.getDoubleString(zhangdie) + "%", Color.RED));
			} else {
				list.add(new StringView(DataUtil.getDoubleString(zhangdie) + "%", Color.GREEN));
			}
		}
		float fontsize = list.get(0).fontSize;
		float width = (list.get(0).text.length() * 3) * fontsize;
		if (pointX + width + getAxisMarginLeft() + getAxisMarginRight() > getWidth()) {
			pointX = pointX - width - 4f;
		} else {
			pointX = pointX + 4f;
		}
		Paint mPaint = new Paint();
		mPaint.setColor(Color.parseColor("#cc6B542A"));
		System.out.println("getAxisMarginTop(): " + getAxisMarginTop());
		canvas.drawRect(pointX - 4f, getAxisMarginTop(), pointX + width + 4f,
				getAxisMarginTop() + list.size() / 2 * (fontsize + 2f), mPaint);
		float newPointX = 0;
		for (int i = 0; i < list.size(); i++) {
			StringView stringView = list.get(i);
			mPaint.setColor(stringView.color);
			mPaint.setTextSize(stringView.fontSize);
			if (i % 2 == 0) {
				canvas.drawText(stringView.text, pointX, getAxisMarginTop() + (i / 2 + 1) * fontsize + 2, mPaint);
				newPointX = pointX + stringView.text.length() * fontsize;
			} else {
				canvas.drawText(stringView.text, newPointX, getAxisMarginTop() + (i / 2 + 1) * fontsize + 2, mPaint);
			}
		}
	}

	public void drawLineTitle(Canvas canvas) {
		if (!(null != this.lineList && this.lineList.size() > 0)) return;
		int size = lineList.size();
			//逐条输出MA线的标题
		for (int i = 0; i < size; i++) {
			LineEntity line = (LineEntity)lineList.get(i);
			if(line.isDisplay()){
				Paint paint = new Paint();
				paint.setColor(line.getLineColor());
				paint.setAntiAlias(true);
				List<Float> lineData = line.getLineData();
				if(lineData != null){
					float wid = getWidth() / size / 4 * (i * 4 + 1);
					float hei = line.getFontSize();
					paint.setTextSize(getResources().getDimension(R.dimen.font_size_medium));
					int index = 0;
					if(showCrossing){
						index = startIndex + getSelectedIndex();
					}else{
						index = startIndex + maxCandleSticksNum;
					}
					if(index < lineData.size()) {
						String value = String.format("%.2f", lineData.get(index).floatValue());
						canvas.drawText(line.getTitle() + "=" + value, wid, hei, paint);
					}else{
						String value = String.format("%.2f", lineData.get(lineData.size() - 1).floatValue());
						canvas.drawText(line.getTitle() + "=" + value, wid, hei, paint);
					}
				}
			}
		}
	}

	public void setLineType(int type) {
		this.type = type;
	}

	private UpdateListener updateListener;

	private float lineLength;

	public UpdateListener getUpdateListener() {
		return updateListener;
	}

	public void setUpdateListener(UpdateListener updateListener) {
		this.updateListener = updateListener;
	}

	public interface UpdateListener {
		public void update(int position, boolean isView);
	}

	public boolean isDisplayPopMune() {
		return isDisplayPopMune;
	}

	public void setDisplayPopMune(boolean isDisplayPopMune) {
		this.isDisplayPopMune = isDisplayPopMune;
	}

}
