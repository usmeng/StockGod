package cn.chinat2t.stockgod.bean;

public class SelfStockBean extends AccountStockBean{
	public static int DELETE = 2;
	public static int ADD = 1;
	
	public String sellflag;   //表示是否已卖出
	public String zixuanflag; //表示自选状态
	public int way;
}
