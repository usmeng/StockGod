package cn.chinat2t.stockgod.bean;

import android.graphics.Color;

public class StringView {
	
	public String text;
	public float fontSize = 14f;
	public int color = Color.WHITE;
	
	public StringView(String text, float fontSize, int color) {
		this.text = text;
		this.fontSize = fontSize;
		this.color = color;
	}
	
	public StringView(String text, int color) {
		this.text = text;
		this.color = color;
	}
	
	public StringView(String text) {
		this.text = text;
	}
}
