package cn.chinat2t.stockgod.http;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.os.Handler;
import cn.chinat2t.stockgod.bean.AccountStockBean;
import cn.chinat2t.stockgod.bean.ChargeBean;
import cn.chinat2t.stockgod.bean.ChooseJuBean;
import cn.chinat2t.stockgod.bean.FightJuBean;
import cn.chinat2t.stockgod.bean.FriendBean;
import cn.chinat2t.stockgod.bean.GameResultBean;
import cn.chinat2t.stockgod.bean.GameStockBean;
import cn.chinat2t.stockgod.bean.GuessJuBean;
import cn.chinat2t.stockgod.bean.ImageBean;
import cn.chinat2t.stockgod.bean.ImageListBean;
import cn.chinat2t.stockgod.bean.KLineBean;
import cn.chinat2t.stockgod.bean.KlineData;
import cn.chinat2t.stockgod.bean.PanKouBean;
import cn.chinat2t.stockgod.bean.ResponsedBean;
import cn.chinat2t.stockgod.bean.RoomBean;
import cn.chinat2t.stockgod.bean.RoomXiangBean;
import cn.chinat2t.stockgod.bean.SaiGuoBean;
import cn.chinat2t.stockgod.bean.SaiZhiBean;
import cn.chinat2t.stockgod.bean.SaiguoXiangBean;
import cn.chinat2t.stockgod.bean.SelfStockBean;
import cn.chinat2t.stockgod.bean.SellBuyBean;
import cn.chinat2t.stockgod.bean.SellBuyStock;
import cn.chinat2t.stockgod.bean.SellBuyStockBean;
import cn.chinat2t.stockgod.bean.ShouYiBean;
import cn.chinat2t.stockgod.bean.ShouYiRank;
import cn.chinat2t.stockgod.bean.StockInfoBean;
import cn.chinat2t.stockgod.bean.StockKlineBean;
import cn.chinat2t.stockgod.bean.UserBean;
import cn.chinat2t.stockgod.bean.UserDaoJu;
import cn.chinat2t.stockgod.bean.UserFenSi;
import cn.chinat2t.stockgod.bean.UserGuanZhu;
import cn.chinat2t.stockgod.bean.WeiTuoStockBean;
import cn.chinat2t.stockgod.utils.CtLog;
import cn.chinat2t.stockgod.utils.StringUtil;

public class JsonParserManager {

	private static JsonParserManager mParser;

	private JsonParserManager() {
	}

	public static JsonParserManager getInstance() {
		if (mParser == null) {
			mParser = new JsonParserManager();
		}
		return mParser;
	}

	public void parserKindsOfStock(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			JSONObject diaryObj = new JSONObject(result);
			List<StockInfoBean> stockList = new ArrayList<StockInfoBean>();
			if(!diaryObj.isNull("1")){
				JSONArray jsonArray = diaryObj.getJSONArray("1");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject = jsonArray.getJSONObject(i);
					StockInfoBean bean = new StockInfoBean();
					bean.stklabel = paraseString(jsonObject, "stklabel");
					bean.code = StringUtil.getCode(bean.stklabel);
					bean.code = StringUtil.getCode(bean.stklabel);
					bean.sname = paraseString(jsonObject, "sname");
					bean.stocktype = paraseInt(jsonObject, "stocktype");
					bean.nowprice = paraseDouble(jsonObject, "nowprice");
					bean.closes = paraseDouble(jsonObject, "closes");
					bean.zhangdie = paraseDouble(jsonObject, "zhangdie");
					bean.zhangfu = paraseDouble(jsonObject, "zhangfu");
					stockList.add(bean);
				}
			}
			if (listener != null) {
				  listener.resultListener(CommunicationManager.SUCCEED, stockList);
			}

		} catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "股票数据解析错误！");
			e.printStackTrace();
		}
	
	}
	
	public void parserSelfChooseOfStock(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			List<SelfStockBean> listStock = new ArrayList<SelfStockBean>();
			if(diaryObj.length() > 0){
				JSONArray names = diaryObj.names();
				for (int i = 0; i < names.length(); i++) {
					String name = names.getString(i);
					if(!"responsecode".equals(name) && !"msg".equals(name)){
						JSONArray selfStockArray = diaryObj.getJSONArray(name);
						for (int j = 0; j < selfStockArray.length(); j++) {
							SelfStockBean bean = new SelfStockBean();
							JSONObject object = (JSONObject) selfStockArray.get(j);
							bean.stklabel = paraseString(object, "stklabel");
							bean.code = StringUtil.getCode(bean.stklabel);
							bean.sname = paraseString(object, "sname");
							bean.way = paraseInt(object, "way");
							bean.id = paraseInt(object, "id");
							bean.zhangdie = paraseDouble(object, "zhangdie");
							if(bean.zhangdie == 0){
								bean.zhangdie = 0;
							}
							listStock.add(bean);
						}
					}
				}
			}
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, listStock);
			}

		} catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "自选数据解析错误！");
			e.printStackTrace();
		}
	
	}

	public void parserAccountOfStock(String result, CommunicationResultListener listener) {

		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			UserBean user = UserBean.getInstance();
			user.listStock = new ArrayList<AccountStockBean>();
			if(diaryObj.length() > 0){
				JSONArray names = diaryObj.names();
				for (int i = 0; i < names.length(); i++) {
					String name = names.getString(i);
					if(!"responsecode".equals(name) && !"msg".equals(name)){
						if("0".equals(name)){
							JSONArray acconutStockArray = diaryObj.getJSONArray(name);
							for (int j = 0; j < acconutStockArray.length(); j++) {
								AccountStockBean bean = new AccountStockBean();
								JSONObject object = (JSONObject) acconutStockArray.get(j);
								bean.closes = paraseDouble(object, "closes");
								bean.sid = paraseString(object, "sid");
								bean.stklabel = bean.sid;
								bean.sname = paraseString(object, "sname");
								bean.sprice = paraseDouble(object, "sprice");
								bean.zhangfu = paraseDouble(object, "zhangfu");
								bean.member = paraseInt(object, "member");
								bean.shizhi = paraseDouble(object, "shizhi");
								user.listStock.add(bean);
							}
						}else if("1".equals(name)){
							JSONArray info = diaryObj.getJSONArray(name);
							JSONObject object = (JSONObject) info.get(0);
//							user.id = paraseInt(object, "");
							user.uid = paraseInt(object, "uid");
							user.fund = paraseDouble(object, "fund");
							user.gold = paraseDouble(object, "gold");
							user.lottery = paraseInt(object, "lottery");
							user.stockmoney = paraseDouble(object, "stockmoney");
							user.zonge = paraseDouble(object, "zonge");
							user.jiaoyici = paraseInt(object, "jiaoyici");
							user.yingkuilv = paraseDouble(object, "yingkuilv");
						}
					}
				}
			}
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, user);
			}

		} catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "市场数据解析错误！");
			e.printStackTrace();
		}
	
	}

	public void parserWeiTuoStock(String result, CommunicationResultListener listener) {

		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			List<WeiTuoStockBean> listStock = new ArrayList<WeiTuoStockBean>();
			if(!diaryObj.isNull("1")){
				JSONArray selfStockArray = diaryObj.getJSONArray("1");
				for (int j = 0; j < selfStockArray.length(); j++) {
					WeiTuoStockBean bean = new WeiTuoStockBean();
					JSONObject object = (JSONObject) selfStockArray.get(j);
					
					bean.id = paraseInt(object, "id");
					bean.sid = paraseString(object, "sid");
					bean.code = StringUtil.getCode(bean.sid);
					bean.member = paraseInt(object, "member");
					bean.sellflag = paraseString(object, "sellflag");
					bean.sprice = paraseDouble(object, "sprice");
					bean.sname = paraseString(object, "sname");
					bean.sellstatus = paraseString(object, "sellstatus");
					bean.chedanyes = paraseString(object, "chedanyes");
					bean.type = paraseInt(object, "type");
					listStock.add(bean);
				}
			}
		
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, listStock);
			}
		} catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "委托数据解析错误！");
			e.printStackTrace();
		}
	
	}
	

	public void parserSellAccountStock(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			String code = diaryObj.getString("responsecode");
			String msg = diaryObj.getString("msg");
			
			if (listener != null) {
				listener.resultsListener(CommunicationManager.SUCCEED, code, msg);
				listener.resultListener(CommunicationManager.SUCCEED, msg);
			}

		} catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "数据解析错误！");
			e.printStackTrace();
		}
	
	}

	public void parserEditSelfStock(String result, CommunicationResultListener listener) {

		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			String res = "";
			if(!diaryObj.isNull("result")){
				res = diaryObj.getString("result");
			}
			String code = diaryObj.getString("responsecode");
			String msg = diaryObj.getString("msg");
			
			if (listener != null) {
				listener.resultsListener(CommunicationManager.SUCCEED, res, code, msg);
			}

		} catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "数据解析错误！");
			e.printStackTrace();
		}
	
	}
	

	public void parserSellWeiTuoStock(String result, CommunicationResultListener listener) {

		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			String code = diaryObj.getString("responsecode");
			String msg = diaryObj.getString("msg");
			
			if (listener != null) {
				listener.resultsListener(CommunicationManager.SUCCEED, code, msg);
			}

		} catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "数据解析错误！");
			e.printStackTrace();
		}
	
	}
	
	private StockInfoBean initStock(StockInfoBean stockBean, JSONObject object) throws JSONException {
		if(stockBean == null || object == null) return null;
		stockBean.sname = paraseString(object, "ssname");
		stockBean.closes = paraseDouble(object, "preclose");
		stockBean.zhangdie = paraseDouble(object, "zhangdie");
		stockBean.nowprice = paraseDouble(object, "nowprice");
		
		stockBean.id = paraseInt(object, "id");
		stockBean.date = paraseString(object, "date");
		stockBean.time = paraseString(object, "time");
		stockBean.block = paraseString(object, "block");
		stockBean.precisions = paraseInt(object, "precisions");
		stockBean.shou = paraseInt(object, "shou");
		stockBean.isbuy = paraseInt(object, "isbuy");
		stockBean.stklabel = object.getString("stklabel");
		stockBean.code = StringUtil.getCode(stockBean.stklabel);
		stockBean.opens = paraseDouble(object, "opens");
		stockBean.high = paraseDouble(object, "high");
		stockBean.low = paraseDouble(object, "low");
		stockBean.amount = paraseDouble(object, "amount");
		stockBean.nowv = paraseInt(object, "nowv");
		stockBean.preclose = paraseDouble(object, "preclose");
		stockBean.avprice = paraseDouble(object, "avprice");
		stockBean.sellvol = paraseDouble(object, "sellvol");
		stockBean.buyvol = paraseDouble(object, "buyvol");
		stockBean.changehand = paraseDouble(object, "changehand");
		stockBean.weicha = paraseDouble(object, "weicha");
		stockBean.weibi = paraseDouble(object, "weibi");
		stockBean.liangbi = paraseDouble(object, "liangbi");
		stockBean.zhangfu = paraseDouble(object, "zhangfu");
		stockBean.zhenfu = paraseDouble(object, "zhenfu");
		stockBean.shiyinglv = paraseDouble(object, "shiyinglv");
		stockBean.zongguben = paraseDouble(object, "zongguben");
		stockBean.liutonggu = paraseDouble(object, "liutonggu");
		stockBean.stocktype = paraseInt(object, "stocktype");
		if(stockBean instanceof AccountStockBean){
			((AccountStockBean)stockBean).cost_price = paraseDouble(object, "cost_price");
			((AccountStockBean)stockBean).self_shuliang = paraseDouble(object, "self_shuliang");
		}
		return stockBean;
	}
	
	public String paraseString(JSONObject object, String param) throws JSONException{
		if(!object.isNull(param)){
			return object.getString(param);
		}
		return "";
	}
	
	public int paraseInt(JSONObject object, String param) throws JSONException{
		if(!object.isNull(param)){
			String str = object.getString(param);
			if(str != null && str.length() > 0)
			return Integer.parseInt(str);
		}
		return 0;
	}
	
	public double paraseDouble(JSONObject object, String param) throws JSONException{
		double d = 0.00;
		if(!object.isNull(param)){
			String str = object.getString(param);
			if(str != null && str.length() > 0) {
				d =  object.getDouble(param);
			}
		}
		return d;
	}

	public void parserStock(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			StockKlineBean stock = new StockKlineBean();
			JSONObject diaryObj = new JSONObject(result);
			stock.sname = paraseString(diaryObj, "sname");
			stock.stklabel = paraseString(diaryObj, "stklabel");
			stock.code = StringUtil.getCode(stock.stklabel);
			stock.nowprice = paraseDouble(diaryObj, "nowprice");
			stock.high = paraseDouble(diaryObj, "high");
			stock.low = paraseDouble(diaryObj, "low");
			stock.opens = paraseDouble(diaryObj, "opens");
			stock.closes = paraseDouble(diaryObj, "closes");
			stock.zhangdie = paraseDouble(diaryObj, "zhangdie");
			stock.zhangfu = paraseDouble(diaryObj, "zhangfu");
			stock.amount = paraseDouble(diaryObj, "amount");
			stock.vol   =  paraseInt(diaryObj, "vol");
			stock.sellvol = paraseInt(diaryObj, "sellvol");
			stock.buyvol = paraseInt(diaryObj, "buyvol");
			stock.weibi = paraseDouble(diaryObj, "weibi");
			stock.weicha = paraseDouble(diaryObj, "weicha");
			stock.member = paraseInt(diaryObj, "member");
			stock.maxstock = paraseInt(diaryObj, "maxstock");
			if(!diaryObj.isNull("0")){
				JSONArray array = diaryObj.getJSONArray("0");
				stock.sellList = new ArrayList<SellBuyBean>();
				for (int i = array.length() - 1; i >=0 ; i--) {
					SellBuyBean bean = new SellBuyBean();
					JSONObject object = array.getJSONObject(i);
					bean.pricesell = paraseDouble(object, "pricesell");
					bean.volsell = paraseInt(object, "volsell");
					bean.flag = SellBuyBean.STOCK_SELL;
					bean.num = i + 1;
					stock.sellList.add(bean);
				}
			}
			if(!diaryObj.isNull("1")){
				JSONArray array = diaryObj.getJSONArray("1");
				for (int i = 0; i < array.length(); i++) {
					SellBuyBean bean = new SellBuyBean();
					JSONObject object = array.getJSONObject(i);
					bean.pricesell = paraseDouble(object, "pricebuy");
					bean.volsell = paraseInt(object, "volbuy");
					bean.flag = SellBuyBean.STOCK_BUY;
					bean.num = 5 - i;
					stock.sellList.add(bean);
				}
			}
		if (listener != null) {
			listener.resultListener(CommunicationManager.SUCCEED, stock);
		}

		} catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "数据解析错误！");
			e.printStackTrace();
		}
	}

	public void parserSellBuyStock(String result, CommunicationResultListener listener) {

		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			SellBuyStockBean sellBuy = new SellBuyStockBean();
			sellBuy.sellList = new ArrayList<SellBuyStock>();
			sellBuy.buyList = new ArrayList<SellBuyStock>();
			
			JSONObject diaryObj = new JSONObject(result);
			JSONArray names = diaryObj.names();
			for (int i = 0; i < names.length(); i++) {
				String name = names.getString(i);
				if(!"responsecode".equals(name) && !"msg".equals(name)){
					if("0".equals(name)){
						JSONObject object = new JSONObject(diaryObj.getString(name));
						sellBuy.beStock = new StockInfoBean();
						initStock(sellBuy.beStock, object);
					}else if("zuidagu".equals(name)){
						sellBuy.zuidagu = diaryObj.getInt("zuidagu");
					}else if("chiyougu".equals(name)){
						sellBuy.chiyougu = diaryObj.getInt("chiyougu");
					}else{
						JSONArray buyList = diaryObj.getJSONArray(name);
						for (int j = 0; j < buyList.length(); j++) {
							JSONObject buy = buyList.getJSONObject(j);
							SellBuyStock stock = initSellBuyStock(buy);
							stock.member = j;
							if("1".equals(name)){
								stock.sellFlag = SellBuyBean.STOCK_BUY;
								sellBuy.buyList.add(stock);
							}else if("2".equals(name)){
								stock.sellFlag = SellBuyBean.STOCK_SELL;
								sellBuy.sellList.add(stock);
							}
							sellBuy.averprice = buy.getDouble("sprice");
						}
					}
				}
				
			}
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, sellBuy);
			}

		} catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "数据解析错误！");
			e.printStackTrace();
		}
	}

	private SellBuyStock initSellBuyStock(JSONObject buy) throws JSONException {
		SellBuyStock stock = new SellBuyStock();
		stock.id = paraseInt(buy, "id");
		stock.sid = paraseInt(buy, "sid");
		stock.uid = paraseInt(buy, "uid");
		stock.slang = paraseInt(buy, "slang");
		stock.sprice = paraseDouble(buy, "sprice");
		stock.oldprice = paraseDouble(buy, "oldprice");
		stock.bilv = paraseDouble(buy, "bilv");
		stock.chengjiaoe = paraseDouble(buy, "chengjiaoe");
		return stock;
	}
	
	public void parserKLine(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONArray diaryObj = new JSONArray(result);
			KLineBean kLine = new KLineBean();
			kLine.kline = new ArrayList<KlineData>();

			if(diaryObj.length() > 0){
				JSONArray array = diaryObj.getJSONArray(0);
				for (int j = 0; j < array.length(); j++) {
					JSONObject object = array.getJSONObject(j);
					KlineData entity = new KlineData();
					entity.id = paraseInt(object, "id");
					entity.shou = paraseDouble(object, "shou");
					String time = paraseString(object, "yearm") + paraseString(object, "dayhour");
					if(time.length() > 8){
						entity.yearm = /*paraseString(object, "yearm");*/ time.substring(0, 8);
						entity.dayhour = /*paraseString(object, "dayhour");*/ time.substring(8);
					}
					entity.open = paraseDouble(object, "open");
					entity.close = paraseDouble(object, "close");
					entity.high = paraseDouble(object, "high");
					entity.low = paraseDouble(object, "low");
					entity.jine = paraseDouble(object, "jine");
					kLine.kline.add(entity);
				}
			}
			if (listener != null) {
				  listener.resultListener(CommunicationManager.SUCCEED, kLine);
			}

		} catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "K线数据解析错误！");
			e.printStackTrace();
		}
	}
	
	public void parserFenShiLine(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONArray diaryObj = new JSONArray(result);
			KLineBean kLine = new KLineBean();
			kLine.kline = new ArrayList<KlineData>();
			
			if(diaryObj.length() > 0){
				JSONArray array = diaryObj.getJSONArray(0);
				for (int j = 0; j < array.length(); j++) {
					JSONObject object = array.getJSONObject(j);
					KlineData entity = new KlineData();
					entity.id = paraseInt(object, "id");
					entity.shou = paraseDouble(object, "shou");
					entity.yearm = paraseString(object, "yearm");
					entity.noeprice = paraseDouble(object, "noeprice");
					entity.navprice = paraseDouble(object, "navprice");
					entity.high = paraseDouble(object, "noeprice");
					entity.low = paraseDouble(object, "noeprice");
					entity.jine = paraseDouble(object, "jine");
					kLine.kline.add(entity);
				}
			}
			if (listener != null) {
				  listener.resultListener(CommunicationManager.SUCCEED, kLine);
			}

		} catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "K线数据解析错误！");
			e.printStackTrace();
		}
	}

	public void parserGoldRooms(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			List<RoomBean> rooms = new ArrayList<RoomBean>();

			if(!diaryObj.isNull("result")){
				JSONObject jsonObject = diaryObj.getJSONObject("result");
				JSONArray names = jsonObject.getJSONArray("roomlist");
				for (int i = 0; i < names.length(); i++) {
					JSONObject object = names.getJSONObject(i);

					RoomBean room = new RoomBean();
					room.id = paraseInt(object, "roomid");
					room.name = paraseString(object, "roomname");
					room.type = paraseInt(object, "type");
					room.saizhi = paraseString(object, "saizhi");
					room.level = paraseInt(object, "level");
					room.ext = paraseString(object, "ext");
					room.ext2 = paraseString(object, "ext2");
					room.ext3 = paraseString(object, "ext3");
					room.value = paraseInt(object, "value");
					room.fangjianjieshao = paraseString(object, "fangjianjieshao");
					rooms.add(room);
					CtLog.d("data = " + room.toString());
				}
			}
			if (listener != null) {
				  listener.resultListener(CommunicationManager.SUCCEED, rooms);
			}

		} catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "房间数据解析错误！");
			e.printStackTrace();
		}
	}
	
	public void parserSaiZhiList(String result, CommunicationResultListener listener) {

		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			List<SaiZhiBean> saizhiList = new ArrayList<SaiZhiBean>();

			if(!diaryObj.isNull("result")){
				JSONArray names = diaryObj.getJSONArray("result");
				for (int i = 0; i < names.length(); i++) {
					JSONObject object = names.getJSONObject(i);

					SaiZhiBean saizhi = new SaiZhiBean();
					saizhi.id = paraseInt(object, "id");
					saizhi.name = paraseString(object, "name");
					saizhi.content = paraseString(object, "content");
					saizhiList.add(saizhi);
					CtLog.d("data = " + saizhi.toString());
				}
			}
			if (listener != null) {
				  listener.resultListener(CommunicationManager.SUCCEED, saizhiList);
			}

		} catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "赛制数据解析错误！");
			e.printStackTrace();
		}
	
	}
	
	public void parserHasSkyEye(String result, CommunicationResultListener listener){

		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			boolean hasSkyEye = false;
			if("1".equals(diaryObj.getString("responsecode"))){
				hasSkyEye = true;
			}
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, hasSkyEye);
			}
		} catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "判断是否有天眼卡数据解析错误！");
			e.printStackTrace();
		}
	
	}
	
	public void parserGuessGame(String result, CommunicationResultListener listener) {

		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			GuessJuBean room = new GuessJuBean();
			if(!diaryObj.isNull("result")){
				JSONObject roomBean = diaryObj.getJSONObject("result");
				room.juID = paraseInt(roomBean, "jid");
				room.rid = paraseInt(roomBean, "rid");
				room.sid = paraseInt(roomBean, "sid");
				room.maxgold = paraseInt(roomBean, "maxgold");
				room.mingold = paraseInt(roomBean, "mingold");
				room.date = paraseInt(roomBean, "date");
				room.day = paraseInt(roomBean, "day");
				room.low = paraseInt(roomBean, "low");
				room.up = paraseInt(roomBean, "up");
				room.sort = paraseInt(roomBean, "sort");
				room.status = paraseInt(roomBean, "status");
				room.stock = paraseString(roomBean, "stock");
				room.goldf = paraseInt(roomBean, "goldf");
				room.golds = paraseInt(roomBean, "golds");
				room.goldt = paraseInt(roomBean, "goldt");
				room.bl =  paraseDouble(roomBean, "bl");
				room.csxs = paraseDouble(roomBean, "csxs");
				room.jlxs = paraseDouble(roomBean, "jlxs");
				room.endtime = paraseString(roomBean, "endtime");
				room.starttime = paraseString(roomBean, "starttime");
				room.rise = paraseInt(roomBean, "rise");
				room.fall = paraseInt(roomBean, "fall");
				room.num = paraseInt(roomBean, "num");
				room.bisaiid = paraseInt(roomBean, "bisaiid");
				room.isplay = paraseInt(roomBean, "isplay");
				room.isprop = paraseInt(roomBean, "isprop");
				room.stockname = paraseString(roomBean, "stockname");
			}
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, room);
			}
		} catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "猜涨跌数据解析错误！");
			e.printStackTrace();
		}
	}

	public void parserChooseGame(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			ChooseJuBean room = new ChooseJuBean();
			if(!diaryObj.isNull("result")){
				JSONObject roomBean = diaryObj.getJSONObject("result");
				room.juID = paraseInt(roomBean, "jid");
				room.rid = paraseInt(roomBean, "rid");
				room.sid = paraseInt(roomBean, "sid");
				room.maxgold = paraseInt(roomBean, "maxgold");
				room.mingold = paraseInt(roomBean, "mingold");
				room.date = paraseInt(roomBean, "date");
				room.day = paraseInt(roomBean, "day");
				room.low = paraseInt(roomBean, "low");
				room.up = paraseInt(roomBean, "up");
				room.sort = paraseInt(roomBean, "sort");
				room.status = paraseInt(roomBean, "status");
				room.stock = paraseString(roomBean, "stock");
				room.goldf = paraseInt(roomBean, "goldf");
				room.golds = paraseInt(roomBean, "golds");
				room.goldt = paraseInt(roomBean, "goldt");
				room.bl =  paraseDouble(roomBean, "bl");
				room.csxs = paraseDouble(roomBean, "csxs");
				room.jlxs = paraseDouble(roomBean, "jlxs");
				room.endtime = paraseString(roomBean, "endtime");
				room.starttime = paraseString(roomBean, "starttime");
				room.rise = paraseInt(roomBean, "rise");
				room.fall = paraseInt(roomBean, "fall");
				room.num = paraseInt(roomBean, "num");
				room.bisaiid = paraseInt(roomBean, "bisaiid");
				room.isplay = paraseInt(roomBean, "isplay");
				room.isprop = paraseInt(roomBean, "isprop");
				room.stockname = paraseString(roomBean, "stockname");
				room.stockList = new ArrayList<GameStockBean>();
				for (int i = 1; i < 4; i++) {
					if (!roomBean.isNull(i+"")) {
						JSONObject array = roomBean.getJSONObject(i+"");
						GameStockBean gameStock = new GameStockBean();
						gameStock.id = paraseString(array, "id");
						gameStock.ssname = paraseString(array, "sname");
						gameStock.preclose = paraseString(array, "preclose");
						gameStock.zhangdie = paraseString(array, "zhangdie");
						gameStock.nowprice = paraseString(array, "nowprice");
						room.stockList.add(gameStock);
					}
				}
			}
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, room);
			}
		} catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "选股竞技数据解析错误！");
			e.printStackTrace();
		}
	}
	
	public void parserFightGame(String result, CommunicationResultListener listener){
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			RoomXiangBean roomXiang = new RoomXiangBean();
			if(!diaryObj.isNull("0")){
				JSONObject object = diaryObj.getJSONObject("0");
				
			}
			if (!diaryObj.isNull("1")) {
				JSONArray array = diaryObj.getJSONArray("1");
			}
		}catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "闪电对决数据解析错误！");
			e.printStackTrace();
		}
	}
	
	public void parserGuanZhuList(String result, CommunicationResultListener listener) {

		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			List<UserGuanZhu> list = new ArrayList<UserGuanZhu>();
			if(!diaryObj.isNull("result")){
				JSONArray array = diaryObj.getJSONArray("result");
				for (int i = 0; i < array.length(); i++) {
					UserGuanZhu guanzhu = new UserGuanZhu();
					JSONObject object = array.getJSONObject(i);
					guanzhu.id = paraseInt(object, "id");
					guanzhu.uid = paraseInt(object, "uid");
					guanzhu.guanzhuflag = paraseInt(object, "guanzhuflag");
					guanzhu.beiuid = paraseInt(object, "beiuid");
					guanzhu.level = paraseInt(object, "level");
					guanzhu.uptime = paraseString(object, "uptime");
					guanzhu.usertype = paraseInt(object, "usertype");
					guanzhu.nickname = paraseString(object, "nickname");
					
					list.add(guanzhu);
				}
			}
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, list);
			}
		}catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "关注列表数据解析错误！");
			e.printStackTrace();
		}
	
	}

	public void parserFenSiList(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			List<UserFenSi> list = new ArrayList<UserFenSi>();
			if(!diaryObj.isNull("result")){
				JSONArray array = diaryObj.getJSONArray("result");
				for (int i = 0; i < array.length(); i++) {
					UserFenSi guanzhu = new UserFenSi();
					JSONObject object = array.getJSONObject(i);
					guanzhu.id = paraseInt(object, "id");
					guanzhu.uid = paraseInt(object, "uid");
					guanzhu.guanzhuflag = paraseInt(object, "guanzhuflag");
					guanzhu.bierenid = paraseInt(object, "bierenid");
					guanzhu.level = paraseInt(object, "level");
					guanzhu.zhuizongflag = paraseInt(object, "zhuizongflag");
					guanzhu.ext = paraseString(object, "ext");
					guanzhu.nickname = paraseString(object, "nickname");
					guanzhu.fensiid = paraseInt(object, "fensiid");
					guanzhu.vip = paraseInt(object, "vip");
					
					list.add(guanzhu);
				}
			}
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, list);
			}
		}catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "粉丝列表数据解析错误！");
			e.printStackTrace();
		}
		
	}

	public void parserZuiZongList(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			List<FriendBean> list = new ArrayList<FriendBean>();
			if(!diaryObj.isNull("result")){
				JSONArray array = diaryObj.getJSONArray("result");
				for (int i = 0; i < array.length(); i++) {
					FriendBean guanzhu = new FriendBean();
					JSONObject object = array.getJSONObject(i);
					guanzhu.id = paraseInt(object, "id");
					guanzhu.uid = paraseInt(object, "uid");
					guanzhu.friendid = paraseInt(object, "friendid");
					guanzhu.level = paraseInt(object, "level");
					guanzhu.nickname = paraseString(object, "nickname");
					guanzhu.uptime = paraseString(object, "uptime");
					guanzhu.usertype = paraseInt(object, "usertype");
					list.add(guanzhu);
				}
			}
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, list);
			}
		}catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "追踪列表数据解析错误！");
			e.printStackTrace();
		}
	}

	public void parserDongTaiList(String result, CommunicationResultListener listener) {

		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			List<FriendBean> list = new ArrayList<FriendBean>();
			if(!diaryObj.isNull("result")){
				JSONArray array = diaryObj.getJSONArray("result");
				for (int i = 0; i < array.length(); i++) {
					FriendBean guanzhu = new FriendBean();
					JSONObject object = array.getJSONObject(i);
					guanzhu.id = paraseInt(object, "id");
					guanzhu.uid = paraseInt(object, "uid");
					guanzhu.friendid = paraseInt(object, "friendid");
					guanzhu.level = paraseInt(object, "level");
					guanzhu.nickname = paraseString(object, "nickname");
					guanzhu.uptime = paraseString(object, "uptime");
					guanzhu.usertype = paraseInt(object, "usertype");
					list.add(guanzhu);
				}
			}
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, list);
			}
		}catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "动态列表数据解析错误！");
			e.printStackTrace();
		}
	
	}

	/*public void parserUserDaoJuList(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			List<UserDaoJu> list = new ArrayList<UserDaoJu>();
			if(!diaryObj.isNull("result")){
				JSONArray array = diaryObj.getJSONArray("result");
				for (int i = 0; i < array.length(); i++) {
					UserDaoJu guanzhu = new UserDaoJu();
					JSONObject object = array.getJSONObject(i);
					guanzhu.id = paraseInt(object, "id");
					guanzhu.uid = paraseInt(object, "uid");
					guanzhu.begingtime = paraseInt(object, "begingtime");
					guanzhu.endtime = paraseInt(object, "endtime");
					guanzhu.propsid = paraseInt(object, "propsid");
					guanzhu.useflag = paraseInt(object, "useflag");
					guanzhu.usechangdi = paraseInt(object, "usechangdi");
					guanzhu.shuliang = paraseInt(object, "shuliang");
					guanzhu.name = paraseString(object, "propname");
					guanzhu.instruction = paraseString(object, "instruction");
					guanzhu.icon = paraseString(object, "icon");
					guanzhu.price = paraseDouble(object, "price");
					guanzhu.max = paraseInt(object, "max");
					guanzhu.valid = paraseInt(object, "valid");
					guanzhu.overlap = paraseInt(object, "overlap");
					guanzhu.gn = paraseInt(object, "gn");
					list.add(guanzhu);
				}
			}
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, list);
			}
		}catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "道具列表数据解析错误！");
			e.printStackTrace();
		}
	}*/

	public void parserDaoJuList(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			List<UserDaoJu> list = new ArrayList<UserDaoJu>();
			if(!diaryObj.isNull("result")){
				JSONArray array = diaryObj.getJSONArray("result");
				for (int i = 0; i < array.length(); i++) {
					UserDaoJu guanzhu = new UserDaoJu();
					JSONObject object = array.getJSONObject(i);
					guanzhu.id = paraseInt(object, "id");
					guanzhu.name = paraseString(object, "name");
					guanzhu.price = paraseDouble(object, "price");
					guanzhu.icon = paraseString(object, "icon");
					guanzhu.max = paraseInt(object, "max");
					guanzhu.valid = paraseInt(object, "valid");
					guanzhu.instruction = paraseString(object, "instruction");
					guanzhu.begingtime = paraseInt(object, "begingtime");
					guanzhu.endtime = paraseInt(object, "endtime");
					guanzhu.shuliang = paraseInt(object, "shuliang");
					guanzhu.propsid = paraseInt(object, "propsid");
					/*guanzhu.uid = paraseInt(object, "uid");
					guanzhu.useflag = paraseInt(object, "useflag");
					guanzhu.usechangdi = paraseInt(object, "usechangdi");
					guanzhu.overlap = paraseInt(object, "overlap");
					guanzhu.gn = paraseInt(object, "gn");*/
					list.add(guanzhu);
				}
			}
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, list);
			}
		}catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "道具列表数据解析错误！");
			e.printStackTrace();
		}
	}
	
	public void parserDuiHuanList(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			List<UserDaoJu> list = new ArrayList<UserDaoJu>();
			if(!diaryObj.isNull("result")){
				JSONArray array = diaryObj.getJSONArray("result");
				for (int i = 0; i < array.length(); i++) {
					UserDaoJu guanzhu = new UserDaoJu();
					JSONObject object = array.getJSONObject(i);
					guanzhu.id = paraseInt(object, "id");
					guanzhu.lottery = paraseInt(object, "lottery");
					guanzhu.value = paraseInt(object, "value");
					guanzhu.type = paraseInt(object, "type");
					guanzhu.name = paraseString(object, "name");
					guanzhu.icon = paraseString(object, "icon");
					list.add(guanzhu);
				}
			}
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, list);
			}
		}catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "道具列表数据解析错误！");
			e.printStackTrace();
		}
	}
	
	public void parserShouYi(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			JSONObject diaryObj = new JSONObject(result);
			ShouYiRank rank = new ShouYiRank();
			rank.responsecode = paraseInt(diaryObj, "responsecode");
			rank.msg = paraseString(diaryObj, "msg");
			if(!diaryObj.isNull("jiangpin")){
				JSONArray array = diaryObj.getJSONArray("jiangpin");
				rank.daoJu = new ArrayList<UserDaoJu>();
				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonObject = array.getJSONObject(i);
					UserDaoJu daoju = new UserDaoJu();
					daoju.id = paraseInt(jsonObject, "id");
					daoju.name = paraseString(jsonObject, "name");
					daoju.icon = paraseString(jsonObject, "icon");
					daoju.num = paraseInt(jsonObject, "num");
					rank.daoJu.add(daoju);
				}
			}
			rank.gold = paraseInt(diaryObj, "gold");
			rank.expvalue = paraseInt(diaryObj, "expvalue");
			rank.fund = paraseDouble(diaryObj, "fund");
			if(!diaryObj.isNull("result")){
				JSONArray array = diaryObj.getJSONArray("result");
				rank.allSYList = new ArrayList<ShouYiBean>();
				for (int i = 0; i < array.length(); i++) {
					JSONObject jsonObject = array.getJSONObject(i);
					ShouYiBean bean = new ShouYiBean();
					bean.uid = paraseInt(jsonObject, "uid");
					bean.nickname = paraseString(jsonObject, "nickname");
					bean.attention = paraseInt(jsonObject, "attention");
					bean.lastpaiming = paraseInt(jsonObject, "lastpaiming");
					bean.my = paraseInt(jsonObject, "my");
					bean.paiming = paraseInt(jsonObject, "paiming");
					bean.ranking = paraseInt(jsonObject, "ranking");
					bean.reward = paraseInt(jsonObject, "reward");
					bean.shouyi = paraseDouble(jsonObject, "shouyi");
					bean.trace = paraseInt(jsonObject, "trace");
					rank.allSYList.add(bean);
				}
			}
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, rank);
			}
		} catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "收益排行数据解析错误！");
			e.printStackTrace();
		}
	}
	/*
	public void parserShouYiRank(String result, CommunicationResultListener listener) {

		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			ShouYiRank rank = new ShouYiRank();
			rank.responsecode = paraseInt(diaryObj, "responsecode");
			rank.msg = paraseString(diaryObj, "msg");
			if(!diaryObj.isNull("result")){
				JSONObject array = diaryObj.getJSONObject("result");
				JSONArray names = array.names();
				for (int i = 0; i < names.length(); i++) {
					JSONObject jsonObject = array.getJSONObject(names.getString(i));
					if(i == -1){
						rank.allSYList = new ArrayList<ShouYiBean>();
						getRankList(rank.allSYList, jsonObject);
					}else if(i == 0){
						rank.monthSYList = new ArrayList<ShouYiBean>();
						getRankList(rank.monthSYList, jsonObject);
					}else if(i == 1){
						rank.weekSYList = new ArrayList<ShouYiBean>();
						getRankList(rank.weekSYList, jsonObject);
					}else if(i == 2){
						rank.daySYList = new ArrayList<ShouYiBean>();
						getRankList(rank.daySYList, jsonObject);
					}
				}
			}
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, rank);
			}
		}catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "收益排行数据解析错误！");
			e.printStackTrace();
		}
	
	}*/

	/*private void getRankList(List<ShouYiBean> list, JSONObject array) throws JSONException {
		JSONArray names = array.names();
		for (int j = 0; j < names.length(); j++) {
			JSONObject jsonObject = array.getJSONObject(names.getString(j));
			ShouYiBean bean = new ShouYiBean();
			bean.id = paraseInt(jsonObject, "id");
			bean.jingjitype = paraseInt(jsonObject, "jingjitype");
			bean.lingquflag = paraseInt(jsonObject, "lingquflag");
			bean.shouyijine = paraseDouble(jsonObject, "shouyijine");
			bean.shouyitype = paraseInt(jsonObject, "shouyitype");
			bean.uid = paraseInt(jsonObject, "uid");
			bean.update = paraseInt(jsonObject, "update");
			bean.uptime = paraseString(jsonObject, "uptime");
			bean.nickname = paraseString(jsonObject, "nickname");
			bean.guanzhu = paraseInt(jsonObject, "guanzhu");
			bean.zhuizong = paraseInt(jsonObject, "zhuizong");
			
			list.add(bean);
		}
	}*/

	public void parserRegUser(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			UserBean user = UserBean.getNewInstance();
			user.responsecode = paraseInt(diaryObj, "responsecode");
			user.msg = paraseString(diaryObj, "msg");
			if (!diaryObj.isNull("result")) {
				JSONObject jsonObject = diaryObj.getJSONObject("result");
				user.uid = paraseInt(jsonObject, "uid");
				user.account = paraseString(jsonObject, "account");
				user.nickname = paraseString(jsonObject, "nickname");
				user.pic = paraseString(jsonObject, "pic");
				user.level = paraseInt(jsonObject, "level");
				user.experience = paraseInt(jsonObject, "experience");
				user.vip = paraseInt(jsonObject, "vip");
				user.acceleration = paraseInt(jsonObject, "acceleration");
				user.phone = paraseString(jsonObject, "phone");
				user.email = paraseString(jsonObject, "email");
				user.onlinedays = paraseInt(jsonObject, "onlinedays");
				user.experiencevalue = paraseInt(jsonObject, "experiencevalue");
				user.sex = paraseInt(jsonObject, "sex");
				user.address = paraseString(jsonObject, "address");
				user.pwd = paraseString(jsonObject, "pwd");
				user.usertype = paraseInt(jsonObject, "usertype");
				user.zhucetime = paraseString(jsonObject, "zhucetime");
				user.stockmoney = paraseDouble(jsonObject, "stockmoney");
				if(!jsonObject.isNull("list")){
					JSONObject list = jsonObject.getJSONObject("list");
					user.fund = paraseDouble(list, "fund");
					user.gold = paraseDouble(list, "gold");
					user.jiaoyici = paraseInt(list, "jiaoyici");
				}
			}
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, user);
			}
		}catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "用户详情数据解析错误！");
			e.printStackTrace();
		}
	}
	
	public void parserUserInfor(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			UserBean user = UserBean.getNewInstance();
			user.responsecode = paraseInt(diaryObj, "responsecode");
			user.msg = paraseString(diaryObj, "msg");
			if(!diaryObj.isNull("result")){
				JSONArray jsonArray = diaryObj.getJSONArray("result");
				for (int i = 0; i < jsonArray.length(); i++) {
					if(i == 0){
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						user.uid = paraseInt(jsonObject, "uid");
						user.account = paraseString(jsonObject, "account");
						user.nickname = paraseString(jsonObject, "nickname");
						user.pic = paraseString(jsonObject, "pic");
						user.level = paraseInt(jsonObject, "level");
						user.experience = paraseInt(jsonObject, "experience");
						user.vip = paraseInt(jsonObject, "vip");
						user.acceleration = paraseInt(jsonObject, "acceleration");
						user.phone = paraseString(jsonObject, "phone");
						user.email = paraseString(jsonObject, "email");
						user.onlinedays = paraseInt(jsonObject, "onlinedays");
						user.experiencevalue = paraseInt(jsonObject, "experiencevalue");
						user.sex = paraseInt(jsonObject, "sex");
						user.address = paraseString(jsonObject, "address");
						user.pwd = paraseString(jsonObject, "pwd");
						user.daymail = paraseString(jsonObject, "daymail");
						user.freeze = paraseInt(jsonObject, "freeze");
						user.userphototype = paraseInt(jsonObject, "userphototype");
						user.usertype = paraseInt(jsonObject, "usertype");
						user.zhucetime = paraseString(jsonObject, "zhucetime");
						user.uplogintime = paraseString(jsonObject, "uplogintime");
						user.friend = paraseString(jsonObject, "friend");
						user.trace = paraseString(jsonObject, "trace");
						user.attention = paraseString(jsonObject, "attention");
					}else if(i == 1){
						JSONObject jsonObject = jsonArray.getJSONObject(i);
						user.uid = paraseInt(jsonObject, "uid");
						user.fund = paraseDouble(jsonObject, "fund");
						user.lottery = paraseInt(jsonObject, "lottery");
						user.gold = paraseDouble(jsonObject, "gold");
						user.stockmoney = paraseDouble(jsonObject, "stockmoney");
						user.zonge = paraseDouble(jsonObject, "zonge");
						user.jiaoyici = paraseInt(jsonObject, "jiaoyici");
						user.yingkuilv = paraseDouble(jsonObject, "yingkuilv");
					}else if(i == 2){
						
					}else if(i == 3){
						user.listStock = new ArrayList<AccountStockBean>();
						JSONArray array = jsonArray.getJSONArray(i);
						for (int j = 0; j < array.length(); j++) {
							JSONArray arrays = array.getJSONArray(j);
							JSONObject object = arrays.getJSONObject(0);
							AccountStockBean stockBean = new AccountStockBean();
							initStock(stockBean , object);
							user.listStock.add(stockBean);
						}
					}
				}
			}
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, user);
			}
		}catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "用户详情数据解析错误！");
			e.printStackTrace();
		}
	
	}

	public void parserUserStr(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			UserBean user = UserBean.getNewInstance();
			user.responsecode = paraseInt(diaryObj, "responsecode");
			user.msg = paraseString(diaryObj, "msg");
			if(!diaryObj.isNull("result")){
				JSONObject object = diaryObj.getJSONObject("result");
				user.usertype = paraseInt(object, "usertype");
				if(user.usertype == UserBean.USER_LIN_SHI){
					user.uid = paraseInt(object, "youkeid");
				}else{
					user.uid = paraseInt(object, "uid");
				}
				user.sex = paraseInt(object, "sex");
				user.nickname = paraseString(object, "nickname");
				user.account = paraseString(object, "account");
				
				user.pwd = paraseString(object, "pwd");
				user.vip = paraseInt(object, "vip");
				user.level = paraseInt(object, "level");
				user.acceleration = paraseInt(object, "acceleration");
				user.experience = paraseInt(object, "experience");
				user.experiencevalue = paraseInt(object, "experiencevalue");
				user.phone = paraseString(object, "phone");
				user.address = paraseString(object, "address");
				user.email = paraseString(object, "email");
				user.dayemail = paraseString(object, "dayemail");
				user.freeze = paraseInt(object, "freeze");
				user.userphototype = paraseInt(object, "userphototype");
				user.trace = paraseString(object, "trace");
				user.pic = paraseString(object, "pic");
				user.attention = paraseString(object, "attention");
				user.friend = paraseString(object, "friend");
				
				user.gold = paraseDouble(object, "gold");
				user.fund = paraseDouble(object, "fund");
				user.lottery = paraseInt(object, "lottery");
			}
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, user);
			}
		}catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "用户数据解析错误！");
			e.printStackTrace();
		}
	
	}

	public void parserPlayer(String str, final CommunicationResultListener listener) {
		final UserBean user = UserBean.getNewInstance();
		user.nickname = "小蜜桃";
		user.responsecode = 1;
		user.msg = "玩家已进入";
		new Handler().postDelayed(new Runnable() {
			
			@Override
			public void run() {
				listener.resultListener(CommunicationManager.SUCCEED, user);
			}
		}, 2000);
	}

	public void parserFightJu(String result, CommunicationResultListener listener) {
		try {
			CtLog.d("获取游戏的局：" + result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			if(!diaryObj.isNull("gid")){
				listener.resultListener(CommunicationManager.SUCCEED, paraseInt(diaryObj, "gid"));
			}else{
				listener.resultListener(CommunicationManager.FAIL, "获取局失败");
			}
		}catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "闪电对决数据解析错误！");
			e.printStackTrace();
		}
	}

	public void parserPlayerShouyi(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			double shouyi = 0;
			System.out.println("****************** 请求对手收益： " + result + " **************");
			JSONArray diaryObj = new JSONArray(result);
			JSONObject jsonObject = diaryObj.getJSONObject(0);
			if(!jsonObject.isNull("shouyi")){
				shouyi = paraseDouble(jsonObject, "shouyi");
			}
			listener.resultListener(CommunicationManager.SUCCEED, shouyi);
		}catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "玩家收益数据解析错误！");
			e.printStackTrace();
		}
	
	}

	public void parserResultOfFight(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}

			System.out.println("----------请求闪电对决的result---------------" + result);
			GameResultBean bean = new GameResultBean();
			JSONObject diaryObj = new JSONObject(result);
			if(!diaryObj.isNull("type")){
				bean.type = paraseInt(diaryObj, "type");
				bean.dianjuan = paraseInt(diaryObj, "dianjuan");
				bean.shouyi = paraseInt(diaryObj, "shouyi");
			}
			if(!diaryObj.isNull("0")){
				JSONObject object = diaryObj.getJSONObject("0");
				bean.jiegou = paraseInt(object, "jiegou");
				bean.msg = paraseString(object, "msg");
			}
			if(!diaryObj.isNull("1")){
				JSONObject object = diaryObj.getJSONObject("1");
				bean.suode = paraseInt(object, "suode");
			}
			if(!diaryObj.isNull("2")){
				JSONArray jsonArray = diaryObj.getJSONArray("2");
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject obj1 = jsonArray.getJSONObject(i);
					if(i == 0){
						bean.nickname1 = paraseString(obj1, "nickname");
						bean.pic1 = paraseString(obj1, "pic");
						bean.shouyiLv1 = paraseDouble(obj1, "shouyilv");
					}else if(i == 1){
						bean.nickname2 = paraseString(obj1, "nickname");
						bean.pic2 = paraseString(obj1, "pic");
						bean.shouyiLv2 = paraseDouble(obj1, "shouyilv");
					}
				}
			}
			listener.resultListener(CommunicationManager.SUCCEED, bean);
		}catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "获取闪电对决天眼卡数据解析错误！");
			e.printStackTrace();
		}
	
	}

	public void parserCanSai(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			GameResultBean bean = new GameResultBean();
			JSONObject diaryObj = new JSONObject(result);
			bean.yujishouyi = paraseDouble(diaryObj, "yujishouyi");
			bean.responsecode = paraseInt(diaryObj, "responsecode");
			bean.playcode = paraseInt(diaryObj, "playcode");
			bean.msg = paraseString(diaryObj, "msg");
			listener.resultListener(CommunicationManager.SUCCEED, bean);
		}catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "获取参赛结果数据解析错误！");
			e.printStackTrace();
		}
	}
	
	public void parserFightSkyEye(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			int status = 0;
			JSONObject diaryObj = new JSONObject(result);
			if(!diaryObj.isNull("status")){
				status = paraseInt(diaryObj, "status");
			}
			listener.resultListener(CommunicationManager.SUCCEED, status);
		}catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "获取闪电对决天眼卡数据解析错误！");
			e.printStackTrace();
		}
	
	}
	
	public void parserWaitingPlayer(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			int status = 0;
			JSONObject diaryObj = new JSONObject(result);
			if(!diaryObj.isNull("status")){
				status = paraseInt(diaryObj, "status");
			}
			listener.resultListener(CommunicationManager.SUCCEED, status);
		}catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "匹配玩家数据解析错误！");
			e.printStackTrace();
		}
	
	}

	public void parserPlayerAndStock(String result, CommunicationResultListener listener) {
		try {
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			FightJuBean bean = new FightJuBean();
			bean.klineList = new ArrayList<KlineData>();
			JSONArray diaryObj = new JSONArray(result);
			for (int i = 0; i < diaryObj.length(); i++) {
				JSONArray jsonArray = diaryObj.getJSONArray(i);
				if(i == 0 || i == 1){
					for (int j = 0; j < jsonArray.length(); j++) {
						JSONObject jsonObject = jsonArray.getJSONObject(j);
						KlineData kline = new KlineData();
						kline.id = paraseInt(jsonObject, "id");
						kline.open = paraseDouble(jsonObject, "open");
						kline.high = paraseDouble(jsonObject, "high");
						kline.yearm = paraseString(jsonObject, "yearm");
						kline.dayhour = paraseString(jsonObject, "dayhour");
						kline.low = paraseDouble(jsonObject, "low");
						kline.close = paraseDouble(jsonObject, "close");
						kline.shou = paraseDouble(jsonObject, "shou");
						kline.jine = paraseDouble(jsonObject, "jine");
						kline.xianflag = paraseInt(jsonObject, "xianflag");
						kline.sid = paraseInt(jsonObject, "sid");
						kline.kid = paraseInt(jsonObject, "kid");
						kline.status = paraseInt(jsonObject, "status");
						bean.klineList.add(kline );
					}
				}else{
					for (int j = 0; j < jsonArray.length(); j++) {
						String string = jsonArray.getString(j);
						CtLog.d("--------------------" + string);
						if(string != null && string.length() > 0 && !"null".equals(string)){
							JSONObject jsonObject = jsonArray.getJSONObject(j);
							if(j == 0){
								bean.id = paraseInt(jsonObject, "id");
								bean.csje = paraseInt(jsonObject, "csje");
								bean.csxs = paraseDouble(jsonObject, "csxs");
								bean.bl = paraseDouble(jsonObject, "bl");
								bean.jlxs = paraseDouble(jsonObject, "jlxs");
								bean.rid = paraseInt(jsonObject, "rid");
								bean.sid = paraseInt(jsonObject, "sid");
								bean.uids = paraseString(jsonObject, "uids");
								bean.yasuo = paraseInt(jsonObject, "yasuo");
								bean.goldf = paraseInt(jsonObject, "goldf");
							} else if(j == 1){
								bean.myPic = paraseString(jsonObject, "pic");
								bean.myUid = paraseInt(jsonObject, "uid");
							}else if(j == 2){
								bean.yourPic = paraseString(jsonObject, "pic");
								bean.yourUid = paraseInt(jsonObject, "uid");
								bean.yourNickname = paraseString(jsonObject, "nickname");
							}
						}
					}
				}
			}
			listener.resultListener(CommunicationManager.SUCCEED, bean);
		}catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "获取玩家和股票数据解析错误！");
			e.printStackTrace();
		}
	}

	public void parserMingXi(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			List<PanKouBean> list = new ArrayList<PanKouBean>();
			if(!diaryObj.isNull("kline")){
				JSONArray array = diaryObj.getJSONArray("kline");
				for (int j = 0; j < array.length(); j++) {
					JSONObject object = (JSONObject) array.get(j);
					PanKouBean bean = new PanKouBean();
					bean.nowv = paraseDouble(object, "nowv");
					bean.close = paraseDouble(object, "close");
					bean.time = paraseString(object, "time");
					bean.isbuy = paraseDouble(object, "isbuy");
					list.add(bean);
				}
			}
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, list);
			}
		} catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "盘口数据解析错误！");
			e.printStackTrace();
		}
	
	}

	public void parserSaiGuo(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			SaiguoXiangBean xiang = new SaiguoXiangBean();
			xiang.saiguoList = new ArrayList<SaiGuoBean>();
			xiang.responsecode = paraseInt(diaryObj, "responsecode");
			xiang.msg = paraseString(diaryObj, "msg");
			if(!diaryObj.isNull("0")){
				JSONArray array = diaryObj.getJSONArray("0");
				for (int j = 0; j < array.length(); j++) {
					JSONObject object = (JSONObject) array.get(j);
					SaiGuoBean bean = new SaiGuoBean();
					bean.sname = paraseString(object, "sname");
					bean.id = paraseInt(object, "id");
					bean.result = paraseInt(object, "result");
					bean.sid = paraseString(object, "sid");
					bean.time = paraseString(object, "time");
					bean.name = paraseString(object, "name");
					bean.bunko = paraseString(object, "bunko");
					bean.detail = paraseInt(object, "detail");
					bean.dianjuan = paraseInt(object, "dianjuan");
					xiang.saiguoList.add(bean);
				}
			}
			if(!diaryObj.isNull("1")){
				JSONObject object = diaryObj.getJSONObject("1");
				xiang.lv = paraseDouble(object, "lv");
				xiang.p = paraseInt(object, "p");
				xiang.s = paraseInt(object, "s");
				xiang.y = paraseInt(object, "y");
			}
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, xiang);
			}
		} catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "盘口数据解析错误！");
			e.printStackTrace();
		}
	}

	public void parserDeleteSaiGuo(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			int code = paraseInt(diaryObj, "responsecode");
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, code);
			}
		} catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "盘口数据解析错误！");
			e.printStackTrace();
		}
	}

	public void parserResponse(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			ResponsedBean bean = new ResponsedBean();
			bean.responsecode = paraseInt(diaryObj, "responsecode");
			bean.msg = paraseString(diaryObj, "msg");
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, bean);
			}

		} catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "数据解析错误！");
			e.printStackTrace();
			
		}
	}

	public void parserChargeResult(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			ChargeBean bean = new ChargeBean();
			bean.responsecode = paraseInt(diaryObj, "responsecode");
			bean.msg = paraseString(diaryObj, "msg");
			if(bean.responsecode != 11)
			if(!diaryObj.isNull("result")){
				JSONObject object = diaryObj.getJSONObject("result");
				bean.gold = paraseInt(object, "gold");
				bean.zeng = paraseInt(object, "zeng");
				bean.payMoney = paraseInt(object, "payMoney");
				bean.tn = paraseString(object, "tn");
			}
			bean.code = paraseInt(diaryObj, "code");
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, bean);
			}
		} catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "数据解析错误！");
			e.printStackTrace();
		}
	}

	public void parserImageList(String result, CommunicationResultListener listener) {
		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			
			JSONObject diaryObj = new JSONObject(result);
			ImageListBean listBean = new ImageListBean();
			listBean.imageList = new ArrayList<ImageBean>();
			listBean.responsecode = paraseInt(diaryObj, "responsecode");
			listBean.msg = paraseString(diaryObj, "msg");
			if(!diaryObj.isNull("result")){
				JSONArray array = diaryObj.getJSONArray("result");
				for (int i = 0; i < array.length(); i++) {
					JSONObject object = array.getJSONObject(i);
					ImageBean bean = new ImageBean();
					bean.id = paraseInt(object, "id");
					bean.path = paraseString(object, "path");
					bean.name = paraseString(object, "name");
					bean.descp = paraseString(object, "descp");
					listBean.imageList.add(bean);
				}
			}
			if (listener != null) {
				listener.resultListener(CommunicationManager.SUCCEED, listBean);
			}
		} catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "数据解析错误！");
			e.printStackTrace();
		}
	}

	public void parserResultJiang(String result, CommunicationResultListener listener) {

		try {
			CtLog.d(result);
			if(result == null ) {
				listener.resultListener(CommunicationManager.FAIL, "获取数据失败！");
				return;
			}
			JSONObject diaryObj = new JSONObject(result);
			if (listener != null) {
//				listener.resultListener(CommunicationManager.SUCCEED, listBean);
			}
		} catch (JSONException e) {
			if (listener != null)
				listener.resultListener(CommunicationManager.FAIL, "数据解析错误！");
			e.printStackTrace();
		}
	
	}

}
