package cn.chinat2t.stockgod.http;


import android.view.View;

/**
 *net listener
 */
public abstract class CommunicationResultListener {
	
	private final String TAG = "CommunicationResultListener";
	public long token = 0;
	public Object tag;
	
	public CommunicationResultListener() {
	}
	
	public CommunicationResultListener(Object tag) {
		this.tag = tag;
	}
	
	public void setToken(long t){
		token = t;
	}
	
	public void resultListener(byte result,String status,Object resultData){
	}
	
	public void resultListener(byte result, Object resultData){
	}
	
	public void resultListener(byte result, Object resultData, View v) {
	}
	
	public void resultsListener(byte result, Object... resultData){
	}
}
