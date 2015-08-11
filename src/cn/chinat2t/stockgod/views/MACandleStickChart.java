package cn.chinat2t.stockgod.views;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;


public class MACandleStickChart extends CandleStickChart {
	
//	/** 是否显示全部 */
//	private boolean displayAll = true;
//	
//	/** MA的几条线显示数据 */
//	private List<LineEntity> lineData;
//	private Canvas canvas;

	public MACandleStickChart(Context context) {
		super(context);
	}

	public MACandleStickChart(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public MACandleStickChart(Context context, AttributeSet attrs) {
		super(context, attrs);
	}
	
	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
	}

	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
//		this.canvas = canvas;
//		if(null != this.lineData && this.lineData.size() > 0){
//			setAxisMarginTop(20f);
//		}
		
//		if(null != this.lineData && this.lineData.size() > 0){
//			drawLines(canvas);
//		}
	}
	
	/*//绘制平均线
	protected void drawLines(Canvas canvas){
		// 点线距离
		float lineLength = ((super.getWidth() - super.getAxisMarginLeft()-super.getAxisMarginRight()) / super.getMaxCandleSticksNum()) - 1;
		// 起始位置
		float startX;
		
		int size = lineData.size();
		//逐条输出MA线
		for (int i = 0; i < size; i++) {
			LineEntity line = (LineEntity)lineData.get(i);
			if(line.isDisplay()){
				Paint mPaint = new Paint();
				mPaint.setColor(line.getLineColor());
				mPaint.setAntiAlias(true);
				List<Float> lineData = line.getLineData();
				//输出X线
				startX = super.getAxisMarginLeft() + lineLength / 2f;
				//定义起始点
				PointF ptFirst = null;
				if(lineData !=null){
					float heigh = (super.getHeight() - super.getAxisMarginBottom() - super.getAxisMarginTop());
					for(int j = 0 ; j < super.getMaxCandleSticksNum() && j + getStartIndex() < lineData.size(); j++){
						float value = lineData.get(j + getStartIndex()).floatValue();
						//获取终点Y坐标
						float valueY = (float) ((1f - (value - getMinPrice()) / (getMaxPrice() - getMinPrice())) * heigh) + getAxisMarginTop();
						
						//绘制线条
						if (j > 0){
							canvas.drawLine(ptFirst.x, ptFirst.y, startX, valueY, mPaint);
						}
						//重置起始点
						ptFirst = new PointF(startX , valueY);
						//X位移
						startX = startX + 1 + lineLength;
					}
				    //  输出标题
					float wid = getAxisMarginLeft() + (getWidth() - (size - i) * getWidth()/size);
					float hei = line.getFontSize();
					canvas.drawText(line.getTitle() + "=" + line.lineData.get(i), 
							wid, hei, mPaint);
				}
			}
		}
	}*/
	
	/*////////////属性GetterSetter//////////////
	public boolean isDisplayAll() {
		return displayAll;
	}

	public void setDisplayAll(boolean displayAll) {
		this.displayAll = displayAll;
	}

	public List<LineEntity> getLineData() {
		return lineData;
	}

	public void setLineData(List<LineEntity> lineData) {
		this.lineData = lineData;
	}*/
}
