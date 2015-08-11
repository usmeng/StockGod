package cn.chinat2t.stockgod.bean;

import java.util.List;

public class StockKlineBean {
	public int    id;
	public String sname;  //": "\u534e\u590f\u94f6\u884c",
	public String stklabel;  //": "SH600015",
	public String code;
	public double closes;  //": 6.6,
	public double high;  //": 6.62,
	public double low;  //": 6.46,
	public double opens;  //": 6.57,
	public double nowprice;  //": 6.52,
	public double zhangdie;  //": -0.08,
	public double zhangfu;  //": -1.21,
	public double vol;  //": 295426,
	public double amount;  //": 19320.86,
	public int    sellvol;  //": 155423,
	public int    buyvol;  //": 140003,
	public double weicha;  //": 6664,
	public double weibi;  //": 0,
	public int    member;  //": null,
	public int    maxstock;  //": null,
	
	public List<SellBuyBean> sellList;
//	public List<SellBuyBean> buyList;
}
