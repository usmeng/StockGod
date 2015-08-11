package cn.chinat2t.stockgod.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import cn.chinat2t.stockgod.R;

public class RectView extends View{

	private int size;
	private int color;
	private String title = "";
	private float regWidth = 40;
	private float regHeight;
	private float maxValue = 100;
	
	public RectView(Context context) {
		super(context);
	}
	
	public RectView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public RectView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	@Override
	public void draw(Canvas canvas) {
		super.draw(canvas);
		onDraw(canvas);
	}
	
	@Override
	protected void onDraw(Canvas canvas) {
		super.onDraw(canvas);
		clear(canvas);
		Paint paint = new Paint();
		paint.setColor(getResources().getColor(R.color.zg_bg_color));
		canvas.drawPaint(paint);
		if(color == 0)
			paint.setColor(getResources().getColor(R.color.red));
		else paint.setColor(getResources().getColor(R.color.green));
		int measuredWidth = getMeasuredWidth();
		int measuredHeight= getMeasuredHeight();
		float height = 0;
		if(maxValue != 0){
			height = (regHeight / maxValue) * measuredHeight * 0.8f;
		}
		
		RectF rect = new RectF();
		rect.set((measuredWidth - regWidth) / 2, measuredHeight, (measuredWidth + regWidth) / 2, measuredHeight - height);
		canvas.drawRect(rect , paint);
		
		paint.setColor(getResources().getColor(R.color.puple));
		paint.setTextSize(getResources().getDimension(R.dimen.padding_medium));
		canvas.drawText(title, (measuredWidth - regWidth / 2) / 2, measuredHeight - height - 20, paint);
	}
	
	public void clear(Canvas canvas){
		Paint paint = new Paint();
		paint.setXfermode(new PorterDuffXfermode(Mode.CLEAR));
		canvas.drawPaint(paint);
		paint.setXfermode(new PorterDuffXfermode(Mode.SRC));
	}
	
	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		setMeasuredDimension(measureWidth(widthMeasureSpec),
				measureHeight(heightMeasureSpec));
	}
	
	private int measureHeight(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else if (specMode == MeasureSpec.AT_MOST) {
			result = Math.min(result, specSize);
		}
		return result;
	}
	
	private int measureWidth(int measureSpec) {
		int result = 0;
		int specMode = MeasureSpec.getMode(measureSpec);
		int specSize = MeasureSpec.getSize(measureSpec);

		if (specMode == MeasureSpec.EXACTLY) {
			result = specSize;
		} else if (specMode == MeasureSpec.AT_MOST) {
			result = Math.min(result, specSize);
		}
		return result;
	}
	
	@Override
	protected void onLayout(boolean changed, int left, int top, int right,
			int bottom) {
		super.onLayout(changed, left, top, right, bottom);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return super.onKeyDown(keyCode, event);
	}

	public int getLength() {
		return size;
	}

	public void setLength(int length) {
		this.size = length;
	}

	public void setColor(int color) {
		this.color = color;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public float getRectWidth() {
		return regWidth;
	}

	public void setRectWidth(int width) {
		this.regWidth = width;
	}

	public float getRegHeight() {
		return regHeight;
	}

	public void setRegHeight(int regHeight) {
		this.regHeight = regHeight;
	}

	public void setMaxValue(int maxValue) {
		this.maxValue = maxValue;
	}

}
