package cn.chinat2t.stockgod.http;

import java.util.ArrayList;
import java.util.List;

import android.view.View;


public class RequestPackage {

	public static final byte METHOD_ID_1 = 1;
	public static final byte METHOD_ID_2 = 2;
	public static final byte METHOD_ID_3 = 3;
	public static final byte METHOD_ID_4 = 4;
	public static final byte METHOD_ID_5 = 5;
	public static final byte METHOD_ID_6 = 6;
	public static final byte METHOD_ID_7 = 7;
	public static final byte METHOD_ID_8 = 8;
	public static final byte METHOD_ID_9 = 9;
	public static final byte METHOD_ID_10 = 10;
	public static final byte METHOD_ID_11 = 11;
	public static final byte METHOD_ID_12 = 12;
	public static final byte METHOD_ID_13 = 13;
	public static final byte METHOD_ID_14 = 14;
	public static final byte METHOD_ID_15 = 15;
	public static final byte METHOD_ID_16 = 16;
	public static final byte METHOD_ID_17 = 17;
	public static final byte METHOD_ID_18 = 18;
	public static final byte METHOD_ID_19 = 19;
	public static final byte METHOD_ID_20 = 20;
	public static final byte METHOD_ID_21 = 21;
	public static final byte METHOD_ID_22 = 22;
	public static final byte METHOD_ID_23 = 23;
	public static final byte METHOD_ID_24 = 24;
	public static final byte METHOD_ID_25 = 25;
	public static final byte METHOD_ID_26 = 26;
	public static final byte METHOD_ID_27 = 27;

	public byte choose_method_id = 0;
	public IListener listener;
	public List<Object> params = new ArrayList<Object>();
	
	public View view;
}
