package cn.chinat2t.stockgod.bean;

public class UserDaoJu {
	public int id;
	public int uid;
	public int num;
	public int propsid;
	public int useflag;
	public int usechangdi;
	public int lottery;
	public int value;
	public int type;
	public int shuliang;
	public int max;
	public int valid;
	public int overlap;
	public int gn;
	public int bitmapRes;
	public int isHot;
	public int begingtime;
	public int endtime;
	public String name;
	public String instruction;
	public String icon;
	public String caoZuo;
	public double price;
	
	public UserDaoJu() {
	}
	
	public UserDaoJu(int bitmapRes, String cardName, String cardDesc, double price, String caozuo) {
		this.bitmapRes = bitmapRes;
		this.name = cardName;
		this.instruction = cardDesc;
		this.price = price;
		this.caoZuo = caozuo;
	}
}
