package cn.chinat2t.stockgod.bean;

import java.io.Serializable;
import java.util.List;

public class UserBean implements Serializable{
	
	public static final String USER_OBJECT = "user_name";
	
	private static final long serialVersionUID = 1L;
	public static final int USER_LIN_SHI = 1;
	public static final int USER_NORMAL = 2;
	public static final int USER_VIP = 3;
	
	public int    uid;       // 用户ID
	public double fund;      // 表示资金
	public double gold;      // 表示金币
	public int    lottery;   // 表示奖券
	public double stockmoney;// 股票的价值
	public double zonge;     // 表示交易总盈亏
	public int    jiaoyici;  // 表示交易次数
	public double yingkuilv; // 表示 盈亏率
	public double gameJine;  // 游戏时的金额数
	public double gameShouyi;// 游戏时的收益率
	public int    gameGushu; // 游戏时的股数
	public List<AccountStockBean> listStock;  // 用户持有的股票

	public String account; // "",
	public String nickname;//: "11111112",
	public String pic;	   //: null,
	public int level;   //: "0",
	public int experience;//: "0",
	public int vip;     //: "0",
	public int acceleration;//: "10",
	public String phone;   //: null,
	public String email;   //: "11111112@163.com",
	public int onlinedays;//: null,
	public int experiencevalue;//: null,
	public int sex;     //: "1",
	public String address; //: null,
	public String pwd;     //: "MTExMTExMTI=",
	public String daymail; //: "5",
	public int freeze;  //: "0",
	public int userphototype;//: "1",
	public int usertype = 2;//: "2",
	public String zhucetime;//: "1372763547",
	public String uplogintime;//: "1372763547"
	
	public String msg;
	public int    responsecode;
	public String dayemail;
	public String trace;
	public String attention;
	public String friend;
	private static UserBean user;
	
//	public List<UserGuanZhu> guanZhuList;
//	public List<UserFenSi> fenSiList;
//	public List<UserZhuiZhong> zuiZongList;
	
	private UserBean(){}
	
	public static UserBean getInstance(){
		if(user == null){
			user = new UserBean();
		}
		return user;
	}
	
	public static UserBean getNewInstance(){
		return new UserBean();
	}

	public static void setUser(UserBean user) {
		UserBean.user = user;
	}

}
