package cn.chinat2t.stockgod.http;

import java.io.InputStream;

import android.view.View;
public interface IListener {
	public void resultString(String str);
	
	public void resultBytes(byte[] bytes);
	
	public void resultInputStream(InputStream ins);
	public void error(int errorCode);
	
	public void resultInputStream(InputStream ins, View view);
}
