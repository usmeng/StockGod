package cn.chinat2t.stockgod.bean;

import java.io.Serializable;
import java.util.List;

public class RoomBean implements Serializable{
	private static final long serialVersionUID = 1L;
	
	public int id;
    public String name;
    public int type;
    public String saizhi;
    public int level;
    public int value;
    public String ext;
    public String ext2;
    public String ext3;
    public String fangjianjieshao;
//    public String duiyingname;
    
    public int rid;// "1",
    public int sid;// "1",
    public int juID;
    public String stock;// "146091",
    public int day;// "1",
    public String condense;// "",
    public String scope;// "",
    public String much;// null,
    public String sort;// null,
    public String pid;// null,
    public int up;// "50",
    public int low;// "2",
    public int time;// "60",
    public String status;// null,
    public int date;// "1",
    public int yasuo;// "30",
    public int mingold;// ���ֵ    
    public int maxgold;// ���ֵ
    public int goldf;// �ʽ�1
    public int golds;// �ʽ�2
    public int goldt;// �ʽ�3
    public String yazhu;// 
    public int jlxs;// "21",
    public int bl;// "3",
    public int csxs;// "34"
    public int rise;
    public int fall;
    public int num;
    public String starttime;
    public String endtime;
    public String stockname;
    
    public List<StockInfoBean> stockList;
    public List<GameStockBean> gameList;
    public StockInfoBean stockBean;

}
