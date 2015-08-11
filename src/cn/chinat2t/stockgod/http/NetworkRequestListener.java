package cn.chinat2t.stockgod.http;

import java.io.InputStream;

import android.view.View;

public abstract class NetworkRequestListener implements IListener {

	public void error(int errorCode) {

	}

	public void resultBytes(byte[] bytes) {

	}

	public void resultInputStream(InputStream ins) {

	}

	public void resultString(String str) {

	}

	public void resultInputStream(InputStream ins, View view) {

	}

}
