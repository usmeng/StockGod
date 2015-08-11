package cn.chinat2t.stockgod.http;

public class ServerAddress {

	public static final String IP = "http://101.227.251.197:808";
//	public static final String LOCAL_IP = "http://192.168.1.46/wareen";
//	public static final String PORT = "8080/";

	public static final String LOGIN = IP + "/api/login.php?";
	public static final String GET_USER_INFO = IP + "/api/user.php?";
	public static final String GET_KLINE_MONTH = IP + "/api/duquyuexian.php?";
	public static final String GET_KLINE_DAY = IP + "/api/duqudayxian.php?";
	public static final String GET_KLINE_WEEK = IP + "/api/duquzhouxian.php?";
	public static final String GET_KLINE_5 = IP + "/api/duquwufenxian.php?";
	public static final String GET_KLINE_30 = IP + "/api/duqusanshifenxian.php?";
	public static final String GET_KLINE_60 = IP + "/api/duquliushifenxian.php?";
	public static final String GET_KLINE_FENSHI = IP + "/api/duqufenshixian.php?";
	public static final String GET_KLINE_PARAM = "stockid=%s";

	public static final String GET_KINDS_OF_STOCK = IP + "/api/stocknewtype.php?";
	public static final String GET_KINDS_OF_STOCK_PARAM = "type=%s&page=%s&per=%s";
	public static final String GET_ACCOUNT_OF_STOCK = IP + "/api/getmystock.php?";
	public static final String GET_ACCOUNT_OF_STOCK_PARAM = "userid=%s";
	public static final String GET_SELF_OF_STOCK = IP + "/api/getmyselfs.php?";
	public static final String GET_SELF_OF_STOCK_PARAM = "userid=%s";
	public static final String GET_WEITUO_OF_STOCK = IP + "/api/getmyweis.php?";
	public static final String GET_WEITUO_OF_STOCK_PARAM = "userid=%s";
	public static final String GET_SELL_BUY_STOCK = IP + "/api/sellbuylist.php?";
	public static final String GET_SELL_BUY_STOCK_PARAM = "userid=%s&stockid=%s";
	public static final String GET_PANKOU_STOCK = IP + "/api/stockpanming.php?";
	public static final String GET_PANKOU_STOCK_PARAM = "stockid=%s&userid=%s";
	
	public static final String EDIT_SELF_OF_STOCK = IP + "/api/editmyselfs.php?";
	public static final String EDIT_SELF_OF_STOCK_PARAM = "userid=%s&stockid=%s&edit=%s&sname=%s";
	public static final String EDIT_SELL_WEITUO_STOCK = IP + "/api/editstockwei.php?";
	public static final String EDIT_SELL_WEITUO_STOCK_PARAM = "userid=%s&stockid=%s&id=%s";
	public static final String SELL_ACCOUNT_OF_STOCK = IP + "/api/usermaichu.php?";  
	public static final String SELL_ACCOUNT_OF_STOCK_PARAM = "userid=%s&stockid=%s&smember=%s&price=%s&stockprice=%s";
	public static final String Buy_ACCOUNT_OF_STOCK = IP + "/api/stockbuy.php?";
	public static final String Buy_ACCOUNT_OF_STOCK_PARAM = "userid=%s&stockid=%s&smember=%s&price=%s&stockprice=%s";
	public static final String SEARCH_ACCOUNT_OF_STOCK = IP + "/api/checkstock.php?";
	public static final String SEARCH_ACCOUNT_OF_STOCK_PARAM = "codename=%s&userid=%s&page=%s";
	
	//-------------------------------------------------------------------------------------
	public static final String GET_GOLD_ROOM = IP + "/api/roomselconfig.php?";  

	public static final String GET_SAI_ZHI = IP + "/api/saizhi.php?";  
	public static final String GET_SAI_ZHI_PARAM = "userid=%s&roomid=%s";
	
	public static final String GET_GUESS_NUMBER = IP + "/api/zhangdiecount.php?";
	public static final String GET_GUESS_NUMBER_PARAM = "userid=%s&roomid=%s";

	public static final String GET_STOCK_GUESS = IP + "/api/zhengdiesel.php?";
	public static final String GET_STOCK_GUESS_PARAM = "typeid=%s&userid=%s";
	
	public static final String GET_STOCK_SKY_EYE = IP + "/api/propsuse.php?";
	public static final String GET_STOCK_SKY_EYE_PARAM = "userid=%s&roomid=%s&saizhiid=%s&juid=%s";
	
	public static final String GET_FIGHT_SKYEYE = IP + "/api/sdprop.php?";
	public static final String GET_FIGHT_SKYEYE_PARAM = "gid=%s&ownuid=%s&otheruid=%s";
	
	public static final String GET_JING_JI_BUY = IP + "/api/jingjibuy.php?";  
	public static final String GET_JING_JI_BUY_PARAM = "userid=%s&stockid=%s&type=%s&zijin=%s&stockprice=%s";
	
	public static final String GET_GAME_CONFIG = IP + "/api/gameconfig.php?";
	public static final String GET_GAME_CONFIG_PARAM = "typeid=%s";
	
	public static final String GET_GAME_JU = IP + "/api/getinningsAction.php?";
	public static final String GET_GAME_JU_PARAM = "userid=%s&saizhiid=%s&roomid=%s&gametype=%s";
	
	public static final String GET_GUESS_GAME = IP + "/api/getcaizdAction.php?";
	public static final String GET_GUESS_GAME_PARAM = "userid=%s&saizhiid=%s&roomid=%s&gametype=%s";
	
	public static final String GET_FIGHT_GAME = IP + "/api/battle.php?";
	public static final String GET_FIGHT_GAME_PARAM = "sid=%s&rid=%s&uid=%s";
	
	public static final String GET_RESULT_FIGHT = IP + "/api/over.php?";
	public static final String GET_RESULT_FIGHT_PARAM = "gid=%s&ownuid=%s&otheruid=%s&ownsy=%s";
	
	public static final String GET_GAME_PLAYER = IP + "/api/battledata.php?";
	public static final String GET_GAME_PLAYER_PARAM = "sid=%s&rid=%s&uid=%s&gid=%s&status=%s";
	
	public static final String GET_PLAYER_SHOUYI = IP + "/api/upyields.php?"; 
	public static final String GET_PLAYER_SHOUYI_PARAM = "gid=%s&ownuid=%s&ownsy=%s&otheruid=%s";
	
	public static final String WAITING_PLAYER = IP + "/api/gamestatus.php?";
	public static final String WAITING_PLAYER_PARAM = "gid=%s";
	
	public static final String GET_CHOOSE_GAME = IP + "/api/getxuangjjAction.php?";
	public static final String GET_CHOOSE_GAME_PARAM = "userid=%s&saizhiid=%s&roomid=%s&gametype=%s";

	public static final String GET_CAN_SAI = IP + "/api/getyazhuAction.php?";
	public static final String GET_CAN_SAI_PARAM = "juid=%s&userid=%s&saizhiid=%s&roomid=%s&yazhue=%s";
	
	public static final String CAN_SAI_GUESS = IP + "/api/getyazhuAction.php?";
	public static final String CAN_SAI_GUESS_PARAM = "userid=%s&gold=%s&bisaiid=%s&isprop=%s&caizhangdie=%s&roomid=%s";
	
	public static final String CAN_SAI_CHOOSE = IP + "/api/getxuanyazhuAction.php?";
	public static final String CAN_SAI_CHOOSE_PARAM = "userid=%s&gold=%s&bisaiid=%s&isprop=%s&stockid=%s&roomid=%s";
	
	public static final String GET_STOCK_SINGLE = IP + "/api/wangjifeng.php?";
	public static final String GET_STOCK_SINGLE_PARAM = "id=%s&uid=%s";

	public static final String GET_FEN_BI = IP + "/api/duqufenbixian.php?";
	public static final String GET_FEN_BI_PARAM = "stockid=%s";

	public static final int STOCK_CODE_1 = 1;
	public static final int STOCK_CODE_2 = 2;
	public static final int STOCK_CODE_3 = 3;
	public static final int STOCK_CODE_4 = 4;
	public static final int STOCK_CODE_5 = 5;
	
	public static final class demo2{
		public static final String DELETE_SAI_GUO = IP + "/api/DelGamestatisticAction.php?";
		public static final String DELETE_SAI_GUO_PARAM = "userid=%s&id=%s";

		public static final String GET_SAI_GUO = IP + "/api/GetGamestatisticAction.php?";
		public static final String GET_SAI_GUO_PARAM = "userid=%s";
		
		public static final String REG = IP + "/api/registerAction.php?";
		public static final String REG_PARAM = "username=%s&password=%s&email=%s&sex=%s";
		
		public static final String LOGIN = IP + "/api/loginAction.php?";
		public static final String LOGIN_PARAM = "username=%s&password=%s";
		
		public static final String LOGIN_FAST = IP + "/api/fastloginAction.php?";
		public static final String LOGIN_FAST_PARAM = "nickname=%s&password=%s&resetpwd=%s";
		
		/*public static final String RESET_PASS = IP + "/api/repasswordAction.php?";
		public static final String RESET_PASS_PARAM = "userid=%s&password=%s&newpassword=%s&repassword=%s";
		
		public static final String RESET_NAME = IP + "/api/resetnameAction.php?";
		public static final String RESET_NAME_PARAM = "userid=%s&nickname=%s";*/
		
		public static final String FOUND_PASS = IP + "/api/findpwdAction.php?";
		public static final String FOUND_PASS_PARAM = "email=%s&username=%s";
		public static final String FOUND_PASS_PARAM2 = "userid=%s";

		public static final String USER_INFO = IP + "/api/userAction.php?";
		public static final String USER_INFO_PARAM = "userid=%s";

		public static final String GET_VIP_LIST = IP + "/api/getviplistAction.php?";
		public static final String GET_VIP_LIST_PARAM = "userid=%s";
		
		public static final String EDIT_VIP = IP + "/api/edituservipAction.php?";
		public static final String EDIT_VIP_PARAM = "userid=%s&vipid=%s";
		
		public static final String GOLD_TO_FUND = IP + "/api/goldduifundAction.php?";
		public static final String GOLD_TO_FUND_PARAM = "userid=%s&gold=%s";
		
		public static final String GET_USER_DAOJU_LIST = IP + "/api/GetUserProps.php?";
		public static final String GET_USER_DAOJU_LIST_PARAM = "userid=%s";
		
		public static final String GET_DAOJU_XIANG = IP + "/api/userdaojuxiangAction.php?";
		public static final String GET_DAOJU_XIANG_PARAM = "daojuid=%s&userid=%s";
		
		public static final String GET_ZHUI_ZONG_LIST = IP + "/api/zhuizulistAction.php?";
		public static final String GET_ZHUI_ZONG_LIST_PARAM = "userid=%s";
		
		public static final String ZHUI_ZONG = IP + "/api/zhuizonguserAction.php?";
		public static final String ZHUI_ZONG_PARAM = "userid=%s&zhuizongid=%s";
		
		public static final String GET_FANS = IP + "/api/userfensiAction.php?";
		public static final String GET_FANS_PARAM = "userid=%s";
		
		public static final String GET_GUANZHU = IP + "/api/userguanzhuAction.php?";
		public static final String GET_GUANZHU_PARAM = "userid=%s";
		
		public static final String CANCEL_GUANZHU = IP + "/api/editguanzhuAction.php?";
		public static final String CANCEL_GUANZHU_PARAM = "userid=%s&guanzhuid=%s";
		
		public static final String START_GUANZHU = IP + "/api/addguanzhuAction.php?";
		public static final String START_GUANZHU_PARAM = "userid=%s&guanzhuid=%s";
		
		public static final String ADD_FRIEND = IP + "/api/AddFriend.php?";
		public static final String DEL_FRIEND = IP + "/api/DellFriend.php?";
		public static final String ADD_FRIEND_PARAM = "userid=%s&firendid=%s&type=%s";
		
		public static final String GET_SHOUYI_RANK = IP + "/api/paihangjiluAction.php?";
		public static final String GET_SHOUYI_RANK_PARAM = "userid=%s&type=%s";
		
		public static final String GET_SHOUYI_RANK2 = IP + "/api/GetRankingCompetitive.php?";
		public static final String GET_SHOUYI_RANK3 = IP + "/api/GetRankingStock.php?";
		public static final String GET_SHOUYI_RANK_PARAM2 = "userid=%s&type=%s&page=%s";

		public static final String LING_JIANG = IP + "/api/ReceiveReward.php?";  
		public static final String LING_JIANG_PARAM = "userid=%s&receivetype=%s&type=%s";
		
		public static final String LING_JIANG_LIST = IP + "/api/userlinglistAction.php?";  
		public static final String LING_JIANG_LIST_PARAM = "userid=%s";
		
		public static final String LING_JIANG_XIANG = IP + "/api/pailinguserAction.php?";  
		public static final String LING_JIANG_XIANG_PARAM = "userid=%s";
		
		public static final String GET_DONG_TAI_LIST = IP + "/api/userfriendAction.php?";
		public static final String GET_DONG_TAI_LIST_PARAM = "userid=%s";
		
		public static final String GET_DAOJU_LIST = IP + "/api/GetProps.php?type=%s";
		public static final String GET_DUIHUAN_LIST = IP + "/api/GetChange.php";
		
		public static final String BUY_DAOJU = IP + "/api/BuyProps.php?";
		public static final String BUY_DAOJU_PARAM = "userid=%s&propsid=%s&prosnum=%s";
		
		public static final String UPDATE_USER_NAME = IP + "/api/UpdateUser.php?";
		public static final String UPDATE_USER_NAME_PARAM = "UserId=%s&Type=Name&Name=%s";
		
		public static final String UPDATE_USER_PASS = IP + "/api/UpdateUser.php?";
		public static final String UPDATE_USER_PASS_PARAM = "UserId=%s&Type=PassWord&PassWord=%s&OldPassWord=%s";
		
		public static final String CHARGE_FOR_PHONE = IP + "/api/MobileRecharge.php?";
		public static final String CHARGE_FOR_PHONE_PARAM = "userid=%s&payMoney=%s&parValue=%s&sn=%s&password=%s";
		
		public static final String CHARGE_FOR_YINLIAN = IP + "/api/paymentOK.php?";
		public static final String CHARGE_FOR_YINLIAN_PARAM = "userid=%s&code=%s&tn=%s";
		
		public static final String CHARGE_FOR_YINLIAN_TN = IP + "/api/payment.php?";
		public static final String CHARGE_FOR_YINLIAN_TN_PARAM = "userid=%s&money=%s";
		
		public static final String MALL_CHANGE = IP + "/api/change.php?";
		public static final String MALL_CHANGE_PARAM = "userid=%s&uphone=%s&changeid=%s&type=%s";
		
		public static final String GET_IMAGE_LIST = IP + "/api/GetSystemImage.php?";
		public static final String GET_IMAGE_LIST_PARAM = "page=%s&per=%s";
		
		public static final String GET_IMAGE_CONFIRM = IP + "/api/ChangeUserCard.php?";
		public static final String GET_IMAGE_CONFIRM_PARAM = "userid=%s&cardid=%s";
		
		public static final String CHARGE_FOR_ALIPAY = IP + "/api/Alipay.php?";
		public static final String CHARGE_FOR_ALIPAY_PARAM = "userid=%s&money=%s&ordernumber=%s&type=%s";
	}

}
