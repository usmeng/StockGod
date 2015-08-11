package cn.chinat2t.stockgod.bean;

public class NewsBean {
	
	public int id;
	public String name;
	public int type;
	public String time;
	public String content;
	public int readFlag;
	public boolean isSelect;
	
	public NewsBean(String name, String time, String content) {
		this.name = name;
		this.time = time;
		this.content = content;
	}
}
