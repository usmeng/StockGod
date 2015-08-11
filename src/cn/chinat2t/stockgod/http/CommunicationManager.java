package cn.chinat2t.stockgod.http;

import android.util.Log;
import cn.chinat2t.stockgod.bean.SelfStockBean;
import cn.chinat2t.stockgod.bean.StockInfoBean;
import cn.chinat2t.stockgod.bean.StockKlineBean;
import cn.chinat2t.stockgod.bean.UserBean;
import cn.chinat2t.stockgod.utils.StringUtil;


public class CommunicationManager {
	private final String TAG = "CommunicationManager";
	private final int PAGESIZE = 10;
	public static final byte FAIL = 0;
	public static final byte SUCCEED = 1;
    
	private static CommunicationManager instance;
	private NetworkManager networkManager;
	private JsonParserManager jsonParserManager;

	public CommunicationManager() {
		if (networkManager == null)
			networkManager = NetworkManager.getInstance();

		if (jsonParserManager == null)
			jsonParserManager = JsonParserManager.getInstance();
	}

	public static CommunicationManager getInstance() {
		if (instance == null)
			instance = new CommunicationManager();
		return instance;
	}
	
	public void eidtSelfStock(int userID, String stockId, String sname, int type, final CommunicationResultListener listener){
		if (listener == null) { return; }
		String url = ServerAddress.EDIT_SELF_OF_STOCK;
		String prama = ServerAddress.EDIT_SELF_OF_STOCK_PARAM;
		int index = SelfStockBean.ADD;
		if(type == 1){
			index = SelfStockBean.DELETE;
		}
		url = url + String.format(prama, userID, stockId, index, sname);

		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserEditSelfStock(str, listener);
			}
		});
	}

	public void sellAccountStock(int userID, String stockId, int amount, 
			double price, double nowPrice, final CommunicationResultListener listener) {
		if (listener == null) { return; }

		String url = ServerAddress.SELL_ACCOUNT_OF_STOCK;
		String prama = ServerAddress.SELL_ACCOUNT_OF_STOCK_PARAM;
		url = url + String.format(prama, userID, stockId, amount, price, nowPrice);

		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserSellAccountStock(str, listener);
			}
		});
	}

	public void buyAccountStock(int userID, String stockId, int member, 
			double price, double nowPrice, final CommunicationResultListener listener) {
		if (listener == null) { return; }

		String url = ServerAddress.Buy_ACCOUNT_OF_STOCK;
		String prama = ServerAddress.Buy_ACCOUNT_OF_STOCK_PARAM;
		url = url + String.format(prama, userID, stockId, member, price, nowPrice);

		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserSellAccountStock(str, listener);
			}
		});
	}
	
	public void getKindsOfStock(int type, int page, final CommunicationResultListener listener){
		if (listener == null) {return;}
		String url = ServerAddress.GET_KINDS_OF_STOCK;
		String prama = ServerAddress.GET_KINDS_OF_STOCK_PARAM;
		url = url + String.format(prama, type, page, 10);

		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserKindsOfStock(str, listener);
			}
		});
	}

	public void getAccountStock(UserBean user, final CommunicationResultListener listener) {
		if (user == null || listener == null) { return; }

		String url = ServerAddress.GET_ACCOUNT_OF_STOCK;
		String prama = ServerAddress.GET_ACCOUNT_OF_STOCK_PARAM;
		url = url + String.format(prama, user.uid);

		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserAccountOfStock(str, listener);
			}
		});
	}
	

	public void getSelfStock(UserBean user, final CommunicationResultListener listener) {
		if (user == null || listener == null) { return; }
		String url = ServerAddress.GET_SELF_OF_STOCK;
		String prama = ServerAddress.GET_SELF_OF_STOCK_PARAM;
		url = url + String.format(prama, user.uid);

		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserSelfChooseOfStock(str, listener);
			}
		});
	}
	
	public void getWeiTuoStock(UserBean user, final CommunicationResultListener listener) {
		if (user == null || listener == null) { return; }
		String url = ServerAddress.GET_WEITUO_OF_STOCK;
		String prama = ServerAddress.GET_WEITUO_OF_STOCK_PARAM;
		url = url + String.format(prama, user.uid);

		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserWeiTuoStock(str, listener);
			}
		});
	}

	private void getKLine(String url, final CommunicationResultListener listener) {
		if (listener == null) { return; }
		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				Log.d(TAG, "str:" + str);
				jsonParserManager.parserKLine(str, listener);
			}
		});
	}
	
	public void getDayLine(String stock, final CommunicationResultListener listener) {
		String url = ServerAddress.GET_KLINE_DAY;
		String param = ServerAddress.GET_KLINE_PARAM;
		url = url + String.format(param, stock);
		getKLine(url, listener);
	}
	
	public void getWeekLine(StockInfoBean stock, final CommunicationResultListener listener) {
		String url = ServerAddress.GET_KLINE_WEEK;
		String param = ServerAddress.GET_KLINE_PARAM;
		url = url + String.format(param, stock.stklabel);
		getKLine(url, listener);
	}
	
	public void get5Line(StockInfoBean stock, final CommunicationResultListener listener) {
		String url = ServerAddress.GET_KLINE_5;
		String param = ServerAddress.GET_KLINE_PARAM;
		url = url + String.format(param, stock.stklabel);
		getKLine(url, listener);
	}
	
	public void get30Line(StockInfoBean stock, final CommunicationResultListener listener) {
		String url = ServerAddress.GET_KLINE_30;
		String param = ServerAddress.GET_KLINE_PARAM;
		url = url + String.format(param, stock.stklabel);
		getKLine(url, listener);
	}
	
	public void get60Line(StockInfoBean stock, final CommunicationResultListener listener) {
		String url = ServerAddress.GET_KLINE_60;
		String param = ServerAddress.GET_KLINE_PARAM;
		url = url + String.format(param, stock.stklabel);
		getKLine(url, listener);
	}

	public void getMonthLine(StockInfoBean stock, final CommunicationResultListener listener) {
		String url = ServerAddress.GET_KLINE_MONTH;
		String param = ServerAddress.GET_KLINE_PARAM;
		url = url + String.format(param, stock.stklabel);
		getKLine(url, listener);
	}
	
	public void getFenShiLine(StockKlineBean stock, final CommunicationResultListener listener) {
		if (listener == null) { return; }
		String url = ServerAddress.GET_KLINE_FENSHI;
		String param = ServerAddress.GET_KLINE_PARAM;
		url = url + String.format(param, stock.stklabel);

		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				Log.d(TAG, "str:" + str);
				jsonParserManager.parserFenShiLine(str, listener);
			}
		});
	}
	
	public void cancelWeiTuoStock(int userID, String stockID, int id, final CommunicationResultListener listener) {
		if (listener == null) { return; }
		String url = ServerAddress.EDIT_SELL_WEITUO_STOCK;
		String prama = ServerAddress.EDIT_SELL_WEITUO_STOCK_PARAM;
		url = url + String.format(prama, userID, stockID, id);

		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserSellWeiTuoStock(str, listener);
			}
		});
	}

	public void getSearchAccountStock(String value, int userID, int page, final CommunicationResultListener listener) {
		if (listener == null) { return; }
		String url = ServerAddress.SEARCH_ACCOUNT_OF_STOCK;
		String prama = ServerAddress.SEARCH_ACCOUNT_OF_STOCK_PARAM;
		url = url + String.format(prama, value, userID, page);

		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserSelfChooseOfStock(str, listener);
			}
		});
	}

	public void getStockSellBuyList(int userId, int stockId, final CommunicationResultListener listener) {

		if (listener == null) {
			return;
		}

		String url = ServerAddress.GET_SELL_BUY_STOCK;
		String prama = ServerAddress.GET_SELL_BUY_STOCK_PARAM;
		url = url + String.format(prama, userId, stockId);

		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserSellBuyStock(str, listener);
			}
		});
	}

	public void getGoldRoom(final CommunicationResultListener listener) {
		if (listener == null) { return; }
		String url = ServerAddress.GET_GOLD_ROOM;

		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserGoldRooms(str, listener);
			}
		});
	}

	public void getSaiZhiList(int userID, int roomID, final CommunicationResultListener listener){

		if (listener == null) { return; }
		String url = ServerAddress.GET_SAI_ZHI;
		String prama = ServerAddress.GET_SAI_ZHI_PARAM;
		url = url + String.format(prama, userID, roomID);
		
		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserSaiZhiList(str, listener);
			}
		});
	}
	
	public void getHasSkyEye(int userId, int roomId, int saiZhiId, int JuID, final CommunicationResultListener listener) {
		if (listener == null)	return;

		String url = ServerAddress.GET_STOCK_SKY_EYE;
		String prama = ServerAddress.GET_STOCK_SKY_EYE_PARAM;
		url = url + String.format(prama, userId, roomId, saiZhiId, JuID);

		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				jsonParserManager.parserHasSkyEye(str, listener);
			}
		});
	}
	
	public void getGuessJu(int userID, int saizhiID, int roomID, final CommunicationResultListener listener) {
		if (listener == null) { return; }

		String url = ServerAddress.GET_GUESS_GAME;
		String prama = ServerAddress.GET_GUESS_GAME_PARAM;
		url = url + String.format(prama, userID, saizhiID, roomID, 1);

		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				jsonParserManager.parserGuessGame(str, listener);
			}
		});
	}

	public void getChooseJu(int userID, int saizhiID, int roomID, final CommunicationResultListener listener) {

		if (listener == null) { return; }

		String url = ServerAddress.GET_CHOOSE_GAME;
		String prama = ServerAddress.GET_CHOOSE_GAME_PARAM;
		url = url + String.format(prama, userID, saizhiID, roomID, 2);

		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				jsonParserManager.parserChooseGame(str, listener);
			}
		});
	}

	/** 注册*/
	public void reg(int sex, String name, String pass, String email, final CommunicationResultListener listener){
		String url = ServerAddress.demo2.REG;
		String prama = ServerAddress.demo2.REG_PARAM;
		url = url + String.format(prama, name, StringUtil.md5(pass), email, sex);
		if(listener == null) return;
		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserRegUser(str, listener);
			}
		});
	}
	
	/** 登陆*/
	public void login(String username, String pass, final CommunicationResultListener listener) {
		String url = ServerAddress.demo2.LOGIN;
		String prama = ServerAddress.demo2.LOGIN_PARAM;
		url = url + String.format(prama, username, StringUtil.md5(pass));
		httpAction(url, listener);	
	}
	
	/** 充值密码*/
	public void restPass(int userID, String pass, String newpassword, final CommunicationResultListener listener) {
		String url = ServerAddress.demo2.UPDATE_USER_PASS;
		String prama = ServerAddress.demo2.UPDATE_USER_PASS_PARAM;
		url = url + String.format(prama, userID, newpassword, pass);
		httpActions(url, listener);
	}
	
	/** 快速登陆*/
	public void fastLogin(String name, String pass, final CommunicationResultListener listener) {
		String url = ServerAddress.demo2.LOGIN_FAST;
		String prama = ServerAddress.demo2.LOGIN_FAST_PARAM;
		url = url + String.format(prama, name, pass, pass);
		httpAction(url, listener);
	}
	
	/** 修改昵称*/
	public void reSetNameAction(int userId, String nickName, final CommunicationResultListener listener){
		String url = ServerAddress.demo2.UPDATE_USER_NAME;
		String prama = ServerAddress.demo2.UPDATE_USER_NAME_PARAM;
		url = url + String.format(prama, userId, nickName);
		httpActions(url, listener);
	}
	
	/** 找回密码*/
	public void foundPass(String email, String nickName, CommunicationResultListener listener){
		String url = ServerAddress.demo2.FOUND_PASS;
		String prama = ServerAddress.demo2.FOUND_PASS_PARAM;
		url = url + String.format(prama, email, nickName);
		httpAction(url, listener);
	}
	
	/** 找回密码*/
	public void foundPass(int userId, CommunicationResultListener listener){
		String url = ServerAddress.demo2.FOUND_PASS;
		String prama = ServerAddress.demo2.FOUND_PASS_PARAM2;
		url = url + String.format(prama, userId);
		httpActions(url, listener);
	}
	
	public void httpActions(String url, final CommunicationResultListener listener){
		if(listener == null) return;
		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserResponse(str, listener);
			}
		});
	}
	
	public void httpAction(String url, final CommunicationResultListener listener){
		if(listener == null) return;
		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "网络异常，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserUserStr(str, listener);
			}
		});
	}

	public void httpListAction(String url, final int type, final CommunicationResultListener listener) {
		
		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				if(type == 0){
					jsonParserManager.parserGuanZhuList(str, listener);
				}else if(type == 1){
					jsonParserManager.parserFenSiList(str, listener);
				}else if(type == 2){
					jsonParserManager.parserZuiZongList(str, listener);
				}else if(type == 3){
					jsonParserManager.parserDongTaiList(str, listener);
				}else if(type == 5){
					jsonParserManager.parserDaoJuList(str, listener);
				}/*else if(type == 6){
					jsonParserManager.parserDongTaiList(str, listener);
				}*/
			}
		});
	
	}
	/** 获取vip列表*//*
	public void getVipList(int userId, CommunicationResultListener listener){
		String url = ServerAddress.demo2.GET_VIP_LIST;
		String prama = ServerAddress.demo2.GET_VIP_LIST_PARAM;
		url = url + String.format(prama, userId);
		httpAction(url, listener);
	}
	
	*//** 修改VIP等级*//*
	public void editVipLevel(int userId, int vipId, CommunicationResultListener listener){
		String url = ServerAddress.demo2.EDIT_VIP;
		String prama = ServerAddress.demo2.EDIT_VIP_PARAM;
		url = url + String.format(prama, userId, vipId);
		httpAction(url, listener);
	}
	
	*//** 领取奖励*//*
	public void getLift(int userId, int jiangId, CommunicationResultListener listener){
		String url = ServerAddress.demo2.LING_JIANG;
		String prama = ServerAddress.demo2.LING_JIANG_PARAM;
		url = url + String.format(prama, userId, jiangId);
		httpAction(url, listener);
	}
	
	*//** 获取道具详情*//*
	public void getDaoJuXiang(int userId, int daoJuId, CommunicationResultListener listener){
		String url = ServerAddress.demo2.GET_DAOJU_LIST;
		String prama = ServerAddress.demo2.GET_DAOJU_LIST_PARAM;
		url = url + String.format(prama, userId, daoJuId);
		httpAction(url, listener);
	}
	*/
	
	/** 金币兑换资金*/
	public void exchangeGoldToFund(int userId, String gold, CommunicationResultListener listener){
		String url = ServerAddress.demo2.GOLD_TO_FUND;
		String prama = ServerAddress.demo2.GOLD_TO_FUND_PARAM;
		url = url + String.format(prama, userId, gold);
		httpAction(url, listener);
	}
	
	/** 获取道具列表*/
	public void getUserDaoJuList(int userId, CommunicationResultListener listener){
		String url = ServerAddress.demo2.GET_USER_DAOJU_LIST;
		String prama = ServerAddress.demo2.GET_USER_DAOJU_LIST_PARAM;
		url = url + String.format(prama, userId);
		httpListAction(url, 5, listener);
	}
	
	/** 获取被追踪的牛人列表*/
	public void getZhuiZongList(int uid, CommunicationResultListener listener) {
		String url = ServerAddress.demo2.GET_ZHUI_ZONG_LIST;
		String prama = ServerAddress.demo2.GET_ZHUI_ZONG_LIST_PARAM;
		url = url + String.format(prama, uid);
		httpListAction(url, 2, listener);
	}
	
	/** 获取被追踪的牛人*/
	public void zhuiZongFriend(int userId, int zhuiZhongId, CommunicationResultListener listener){
		String url = ServerAddress.demo2.ZHUI_ZONG;
		String prama = ServerAddress.demo2.ZHUI_ZONG_PARAM;
		url = url + String.format(prama, userId, zhuiZhongId);
		httpAction(url, listener);
	}
	
	/** 获取粉丝列表*/
	public void getFansList(int userId, CommunicationResultListener listener){
		String url = ServerAddress.demo2.GET_FANS;
		String prama = ServerAddress.demo2.GET_FANS_PARAM;
		url = url + String.format(prama, userId);
		httpListAction(url, 1, listener);
	}
	
	/** 获取动态列表*/
	public void getDongTaiList(int userId, CommunicationResultListener listener) {
		String url = ServerAddress.demo2.GET_DONG_TAI_LIST;
		String prama = ServerAddress.demo2.GET_DONG_TAI_LIST_PARAM;
		url = url + String.format(prama, userId);
		httpListAction(url, 3, listener);
	}

	/** 获取关注列表*/
	public void getGuanZhuList(int userId, CommunicationResultListener listener){
		String url = ServerAddress.demo2.GET_GUANZHU;
		String prama = ServerAddress.demo2.GET_GUANZHU_PARAM;
		url = url + String.format(prama, userId);
		httpListAction(url, 0, listener);
	}

	public void cancelGuanZhu(int myUserId, int userId, CommunicationResultListener listener) {
		String url = ServerAddress.demo2.CANCEL_GUANZHU;
		String prama = ServerAddress.demo2.CANCEL_GUANZHU_PARAM;
		url = url + String.format(prama, myUserId, userId);
		httpAction(url, listener);
	}
	

	public void guanZhuFriend(int myUserId, int userId, CommunicationResultListener listener) {
		String url = ServerAddress.demo2.START_GUANZHU;
		String prama = ServerAddress.demo2.START_GUANZHU_PARAM;
		url = url + String.format(prama, myUserId, userId);
		httpAction(url, listener);
	}

	public void addFriend(int uid, int fid, String type, CommunicationResultListener listener){
		String url = ServerAddress.demo2.ADD_FRIEND;
		String prama = ServerAddress.demo2.ADD_FRIEND_PARAM;
		url = url + String.format(prama, uid, fid, type);
		httpActions(url, listener);
	}
	
	public void delFriend(int uid, int fid, String type, CommunicationResultListener listener){
		String url = ServerAddress.demo2.DEL_FRIEND;
		String prama = ServerAddress.demo2.ADD_FRIEND_PARAM;
		url = url + String.format(prama, uid, fid, type);
		httpActions(url, listener);
	}
	
	/*public void getMoniStockRank(int uid, int type, final CommunicationResultListener listener) {
		if(listener == null) return;
		String url = ServerAddress.demo2.GET_SHOUYI_RANK;
		String prama = ServerAddress.demo2.GET_SHOUYI_RANK_PARAM;
		url = url + String.format(prama, uid, type);
		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserShouYiRank(str, listener);
			}
		});
	}*/

	/*public void editNickName(int uid, String newName, CommunicationResultListener listener) {
		String url = ServerAddress.demo2.UPDATE_USER_NAME;
		String prama = ServerAddress.demo2.RESET_NAME_PARAM;
		url = url + String.format(prama, uid, newName);
		httpAction(url, listener);
	}*/

	public void lingJiang(int uid, int jiangId, String type, final CommunicationResultListener listener) {
		String url = ServerAddress.demo2.LING_JIANG;
		String prama = ServerAddress.demo2.LING_JIANG_PARAM;
		url = url + String.format(prama, uid, jiangId, type);
		httpActions(url, listener);
//		listener.setToken(System.currentTimeMillis());
		/*networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserResultJiang(str, listener);
			}
		});*/
	}

	/** 获得用户信息*/
	public void getUserInfo(int userId, final CommunicationResultListener listener){
		String url = ServerAddress.demo2.USER_INFO;
		String prama = ServerAddress.demo2.USER_INFO_PARAM;
		url = url + String.format(prama, userId);
		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserUserInfor(str, listener);
			}
		});
	}

	public void canSaiGuessGame(int userID, int gold, int bisaiID, int isprop, int value, int roomID, 
			final CommunicationResultListener listener) {
		String url = ServerAddress.CAN_SAI_GUESS;
		String prama = ServerAddress.CAN_SAI_GUESS_PARAM;
		url = url + String.format(prama, userID, gold, bisaiID, isprop, value, roomID);

		if(listener == null) return;
		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserCanSai(str, listener);
			}
		});
	
	
	}

	public void canSaiChooseGame(int userID, int value, int bisaiID, int isProp,
			String stockID, int roomID, final CommunicationResultListener listener) {
		if(listener == null) return;
		String url = ServerAddress.CAN_SAI_CHOOSE;
		String prama = ServerAddress.CAN_SAI_CHOOSE_PARAM;
		url = url + String.format(prama, userID, value, bisaiID, isProp, stockID, roomID);
		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserCanSai(str, listener);
			}
		});
	}

	public void getStockByUid(String id, int uid, final CommunicationResultListener listener) {
		String url = ServerAddress.GET_STOCK_SINGLE;
		String prama = ServerAddress.GET_STOCK_SINGLE_PARAM;
		url = url + String.format(prama, id, uid);
		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserStock(str, listener);
			}
		});
	}

	public void waitingPlayer(int gid, final CommunicationResultListener listener) {
		String url = ServerAddress.WAITING_PLAYER;
		String prama = ServerAddress.WAITING_PLAYER_PARAM;
		url = url + String.format(prama, gid);
		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserWaitingPlayer(str, listener);
			}
		});
	}

	public void getFightJu(int uid, int roomID, int saizhiID, final CommunicationResultListener listener) {
		String url = ServerAddress.GET_FIGHT_GAME;
		String param = ServerAddress.GET_FIGHT_GAME_PARAM;
		url = url + String.format(param, saizhiID, roomID, uid);
		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserFightJu(str, listener);
			}
		});
	}

	public void getPlayer(int saizhiID, int roomID, int uid, int juID,
			int status, final CommunicationResultListener listener) {
		String url = ServerAddress.GET_GAME_PLAYER;
		String param = ServerAddress.GET_GAME_PLAYER_PARAM;
		url = url + String.format(param, saizhiID, roomID, uid, juID, status);
		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserPlayerAndStock(str, listener);
			}
		});
	}

	public void getFightStock(String stklabel, CommunicationResultListener listener) {
		String url = ServerAddress.GET_KLINE_5;
		String param = ServerAddress.GET_KLINE_PARAM;
		url = url + String.format(param, stklabel);
		getKLine(url, listener);
	}

	public void getResultOfFight(int juID, UserBean userMain,
			UserBean userPlayer, final CommunicationResultListener listener) {
		String url = ServerAddress.GET_RESULT_FIGHT;
		String param = ServerAddress.GET_RESULT_FIGHT_PARAM;
		url = url + String.format(param, juID, userMain.uid, userPlayer.uid, userMain.gameShouyi);
		System.out.println("----------请求结果的URL: " + url);
		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserResultOfFight(str, listener);
			}
		});
	
	
	}

	public void getPlayerShouYiLv(int juID, int uid, double gameShouyi,
			int uid2, final CommunicationResultListener listener) {

		String url = ServerAddress.GET_PLAYER_SHOUYI;
		String param = ServerAddress.GET_PLAYER_SHOUYI_PARAM;
		url = url + String.format(param, juID, uid, gameShouyi, uid2);
		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserPlayerShouyi(str, listener);
			}
		});
	
	}

	public void getFightSkyEye(int juID, int uid, int uid2, final CommunicationResultListener listener) {
		String url = ServerAddress.GET_FIGHT_SKYEYE;
		String param = ServerAddress.GET_FIGHT_SKYEYE_PARAM;
		url = url + String.format(param, juID, uid, uid2);
		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserFightSkyEye(str, listener);
			}
		});
	}

	public void getMingxi(String stklabel, final CommunicationResultListener listener) {
		String url = ServerAddress.GET_FEN_BI;
		String param = ServerAddress.GET_FEN_BI_PARAM;
		url = url + String.format(param, stklabel);
		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserMingXi(str, listener);
			}
		});
	}

	public void getGamestatisticAction(int uid, final CommunicationResultListener listener) {
		String url = ServerAddress.demo2.GET_SAI_GUO;
		String param = ServerAddress.demo2.GET_SAI_GUO_PARAM;
		url = url + String.format(param, uid);
		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserSaiGuo(str, listener);
			}
		});
	}

	public void delGamestatisticAction(int uid, int id, final CommunicationResultListener listener) {
		String url = ServerAddress.demo2.DELETE_SAI_GUO;
		String param = ServerAddress.demo2.DELETE_SAI_GUO_PARAM;
		url = url + String.format(param, uid, id);
		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserDeleteSaiGuo(str, listener);
			}
		});
	}

	public void getDaojuList(int currentPage, int type, final CommunicationResultListener listener) {
		String url = String.format(ServerAddress.demo2.GET_DAOJU_LIST, type);
		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserDaoJuList(str, listener);
			}
		});
	
	}

	public void getDuiHuanList(int uid, final CommunicationResultListener listener) {
		String url = ServerAddress.demo2.GET_DUIHUAN_LIST;
		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserDuiHuanList(str, listener);
			}
		});
	}
	
	public void buyDaoJu(int uid, int id, int shuliang, final CommunicationResultListener listener) {
		String url = ServerAddress.demo2.BUY_DAOJU;
		String param = ServerAddress.demo2.BUY_DAOJU_PARAM;
		url = url + String.format(param, uid, id, shuliang);
		listener.setToken(System.currentTimeMillis());
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserResponse(str, listener);
			}
		});
	}

	public void chargeForPhone(int uid, String haoValue, String passValue, int chargeValue,
			final CommunicationResultListener listener) {
		String url = ServerAddress.demo2.CHARGE_FOR_PHONE;
		String param = ServerAddress.demo2.CHARGE_FOR_PHONE_PARAM;
		listener.setToken(System.currentTimeMillis());
		url = url + String.format(param, uid, chargeValue, chargeValue, haoValue, passValue);
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserChargeResult(str, listener);
			}
		});
	}

	public void updateGold(int userID, int code, String CN, final CommunicationResultListener listener) {
		String url = ServerAddress.demo2.CHARGE_FOR_YINLIAN;
		String param = ServerAddress.demo2.CHARGE_FOR_YINLIAN_PARAM;
		listener.setToken(System.currentTimeMillis());
		url = url + String.format(param, userID, code, CN);
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserChargeResult(str, listener);
			}
		});
	
	}

	public void mallChange(int uid, int daoJuId, int type, String numStr, final CommunicationResultListener listener) {
		String url = ServerAddress.demo2.MALL_CHANGE;
		String param = ServerAddress.demo2.MALL_CHANGE_PARAM;
		listener.setToken(System.currentTimeMillis());
		url = url + String.format(param, uid, numStr, daoJuId, type);
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserResponse(str, listener);
			}
		});
	}

	public void getYinLianTN(int uid, int chargeValue, final CommunicationResultListener listener) {
		String url = ServerAddress.demo2.CHARGE_FOR_YINLIAN_TN;
		String param = ServerAddress.demo2.CHARGE_FOR_YINLIAN_TN_PARAM;
		listener.setToken(System.currentTimeMillis());
		url = url + String.format(param, uid, chargeValue);
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserChargeResult(str, listener);
			}
		});
	}

	public void getImageList(final CommunicationResultListener listener) {
		String url = ServerAddress.demo2.GET_IMAGE_LIST;
		String param = ServerAddress.demo2.GET_IMAGE_LIST_PARAM;
		listener.setToken(System.currentTimeMillis());
		url = url + String.format(param, 1, 20);  
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserImageList(str, listener);
			}
		});
	}

	public void mallImageChange(int uid, int daoJuId, final CommunicationResultListener listener) {

		String url = ServerAddress.demo2.GET_IMAGE_CONFIRM;
		String param = ServerAddress.demo2.GET_IMAGE_CONFIRM_PARAM;
		listener.setToken(System.currentTimeMillis());
		url = url + String.format(param, uid, daoJuId);  
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserResponse(str, listener);
			}
		});
	
	}

	public void chargeForAlipay(int uid, int chargeValue2, int resultType, String alipayInfo,
			final CommunicationResultListener listener) {
		String url = ServerAddress.demo2.CHARGE_FOR_ALIPAY;
		String param = ServerAddress.demo2.CHARGE_FOR_ALIPAY_PARAM;
		listener.setToken(System.currentTimeMillis());
		url = url + String.format(param, uid, chargeValue2, alipayInfo, resultType);  
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserChargeResult(str, listener);
			}
		});
	
	}

	public void getJingJiStockRank(int uid, String type, int page, final CommunicationResultListener listener) {
		String url = ServerAddress.demo2.GET_SHOUYI_RANK2;
		String param = ServerAddress.demo2.GET_SHOUYI_RANK_PARAM2;
		listener.setToken(System.currentTimeMillis());
		url = url + String.format(param, uid, type, page);  
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserShouYi(str, listener);
			}
		});
	
	}
	
	public void getMoniStockRank(int uid, String type, int page, final CommunicationResultListener listener) {
		String url = ServerAddress.demo2.GET_SHOUYI_RANK3;
		String param = ServerAddress.demo2.GET_SHOUYI_RANK_PARAM2;
		listener.setToken(System.currentTimeMillis());
		url = url + String.format(param, uid, type, page);  
		networkManager.requestGetString(url, new NetworkRequestListener() {
			@Override
			public void resultString(String str) {
				if (str == null || str.equals("")) {
					listener.resultListener(FAIL, "请求失败，请重试!");
					return;
				}
				// 获取到json字符串后转入json解析
				jsonParserManager.parserShouYi(str, listener);
			}
		});
		
	}

}
