package cn.chinat2t.stockgod.bean;


public class DJBean {
	public int    bitmapRes;
	public String cardName;
	public String cardDesc;
	public String price;
	public String caozuo;
	
	public DJBean() {
	}

	public DJBean(int bitmapRes, String cardName, String cardDesc, String price) {
		this.bitmapRes = bitmapRes;
		this.cardName = cardName;
		this.cardDesc = cardDesc;
		this.price = price;
	}
	
	public DJBean(int bitmapRes, String cardName, String cardDesc, String price, String caozuo) {
		this.bitmapRes = bitmapRes;
		this.cardName = cardName;
		this.cardDesc = cardDesc;
		this.price = price;
		this.caozuo = caozuo;
	}
}
