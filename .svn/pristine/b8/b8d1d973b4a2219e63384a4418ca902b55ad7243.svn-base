package cn.chinat2t.stockgod.views;

import java.util.List;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import cn.chinat2t.stockgod.R;

public class CircleMapView extends View{

	private List<Integer> values;
	
	public List<Integer> getValues() {
		return values;
	}

	public void setValues(List<Integer> values) {
		this.values = values;
	}

	public CircleMapView(Context context) {
		super(context);
	}
	
	public CircleMapView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public CircleMapView(Context context, AttributeSet attrs, int defStyle) {
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
		Paint paint = new Paint();
		int radius = getMeasuredHeight();
		paint.setColor(getResources().getColor(R.color.red));
		canvas.drawArc(new RectF(20, 0, radius + 20, radius), 0, 60, true, paint);
		int width = getMeasuredWidth()/2;
		canvas.drawRect(new RectF(width, radius / 2 - 30, width + 20, radius / 2 - 10), paint);
		
		paint.setColor(getResources().getColor(R.color.yello));
		canvas.drawArc(new RectF(20, 0, radius + 20, radius), 60, 120, true, paint);
		canvas.drawRect(new RectF(width, radius / 2, width + 20, radius / 2 + 20), paint);
		
		paint.setColor(getResources().getColor(R.color.green));
		canvas.drawArc(new RectF(20, 0, radius + 20, radius), 180, 100, true, paint);
		canvas.drawRect(new RectF(width, radius / 2 + 30, width + 20, radius / 2 + 50), paint);
		
		paint.setColor(getResources().getColor(R.color.blue));
		canvas.drawArc(new RectF(20, 0, radius + 20, radius), 280, 80, true, paint);
		canvas.drawRect(new RectF(width, radius / 2 + 60, width + 20, radius / 2 + 80), paint);
		
		paint.setTextSize(20);
		paint.setColor(getResources().getColor(R.color.black));
		canvas.drawText("主力流入", width + 40, radius / 2 - 10, paint);
		canvas.drawText("主力流出", width + 40, radius / 2 + 20, paint);
		canvas.drawText("散户流入", width + 40, radius / 2 + 50, paint);
		canvas.drawText("散户流出", width + 40, radius / 2 + 80, paint);
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

}