package cn.chinat2t.stockgod.bean;

import java.util.ArrayList;
import java.util.List;

public class LineEntity {
	
	/** 均线的数据*/
	public List<Float> lineData;
	
	/**均线的标题*/
	private String title;
	
	/** 均线的颜色*/
	private int lineColor;
	
	private float fontSize = 14f;
	
	public float getFontSize() {
		return fontSize;
	}

	public void setFontSize(float fontSize) {
		this.fontSize = fontSize;
	}

	/** 均线是否显示*/
	private boolean display = true;

	public List<Float> getLineData() {
		return lineData;
	}

	public void setLineData(List<Float> lineData) {
		this.lineData = lineData;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getLineColor() {
		return lineColor;
	}

	public void setLineColor(int lineColor) {
		this.lineColor = lineColor;
	}

	public boolean isDisplay() {
		return display;
	}

	public void setDisplay(boolean display) {
		this.display = display;
	}
	
	public LineEntity() {
		super();
	}

	public LineEntity(List<Float> lineData, String title, int lineColor) {
		super();
		this.lineData = lineData;
		this.title = title;
		this.lineColor = lineColor;
	}
	
	public void put(float value){
		if (null == lineData){
			lineData = new ArrayList<Float>();
		}
		lineData.add(value);
	}
}
