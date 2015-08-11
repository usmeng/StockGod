package cn.chinat2t.stockgod.utils;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.chinat2t.stockgod.bean.KlineData;

public class DataUtil {
	
	public static List<Float> initMA(int days, List<KlineData> ohlc){
		
		if (ohlc == null || days < 2){
			return null;
		}
		
        List<Float> MA5Values = new ArrayList<Float>();
        
    	float sum = 0;
    	float avg = 0;
        for(int i = 0 ; i < ohlc.size(); i++){
        	float close =(float)ohlc.get(i).getClose();
        	if(i< days){
        		sum = sum + close;
        		avg = sum / (i + 1f);
        	}else{
        		sum = sum + close - (float)ohlc.get(i-days).getClose();
        		avg = sum / days;
        	}
        	MA5Values.add(avg);
        }
        
        return MA5Values;
	}

	public static List<Float> initFenShi(int type, List<KlineData> lineList) {
		if(lineList == null) return null;
		List<Float> MA5Values = new ArrayList<Float>();
		if(type == 1){
			for (int j = 0; j < lineList.size(); j++) {
				MA5Values.add((float)lineList.get(j).noeprice);
			}
		}else if(type == 0){
			for (int j = 0; j < lineList.size(); j++) {
				MA5Values.add((float)lineList.get(j).navprice);
			}
		}
		return MA5Values;
	}
	
	public static double getMAValue(int days, List<KlineData> ohlc){
    	double sum = 0;
    	double avg = 0;
        for(int i = 0 ; i < ohlc.size(); i++){
        	float close =(float)ohlc.get(i).getClose();
        	if(i< days){
        		sum = sum + close;
        		avg = sum / (i + 1f);
        	}else{
        		sum = sum + close - (float)ohlc.get(i-days).getClose();
        		avg = sum / days;
        	}
        }
        
        return avg;
	}
	
	public static double getDouble1(double num){
		return new BigDecimal(num).setScale(1, BigDecimal.ROUND_HALF_UP).doubleValue();
	}

	public static String getDoubleString(double num){
		return new DecimalFormat("0.00").format(num);
	}
	
	public static double getDouble(double num, int length){
		return new BigDecimal(num).setScale(length, BigDecimal.ROUND_HALF_UP).doubleValue();
	}
//	vol.add(new StickEntity(36.4, 0, "201306141912"));
//	vol.add(new StickEntity(36.4, 0, "201306141912"));
//	vol.add(new StickEntity(39.5, 0, "201305281625"));
//	vol.add(new StickEntity(41.0, 0, "201305151203"));
//	vol.add(new StickEntity(26.4, 0, "201305021836"));
//	vol.add(new StickEntity(40.4, 0, "201306051235"));
//	vol.add(new StickEntity(36.4, 0, "201306051235"));
//	vol.add(new StickEntity(26.4, 0, "201306051235"));
//	vol.add(new StickEntity(28.4, 0, "201306051235"));
//	vol.add(new StickEntity(38.0, 0, "201305151203"));
//	vol.add(new StickEntity(26.4, 0, "201305021836"));
//	vol.add(new StickEntity(46.4, 0, "201306051235"));
//	vol.add(new StickEntity(36.4, 0, "201306051235"));
//	vol.add(new StickEntity(26.4, 0, "201306051235"));
//	vol.add(new StickEntity(38.4, 0, "201306051235"));
//	vol.add(new StickEntity(39.4, 0, "201306051235"));
//	vol.add(new StickEntity(35.4, 0, "201306051235"));
//	vol.add(new StickEntity(45.0, 0, "201305151203"));
//	vol.add(new StickEntity(26.4, 0, "201305021836"));
//	vol.add(new StickEntity(46.4, 0, "201306051235"));
//	vol.add(new StickEntity(36.4, 0, "201306051235"));
	
//	ohlc.add(new OHLCEntity(25.2, 36.4, 20.4, 32.5, "201306141912"));
//	ohlc.add(new OHLCEntity(32.3, 39.5, 27.2, 28.2, "201305281625"));
//	ohlc.add(new OHLCEntity(35.6, 41.0, 20.4, 28.6, "201305151203"));
//	ohlc.add(new OHLCEntity(15.2, 26.4, 21.4, 23.8, "201305021836"));
//	ohlc.add(new OHLCEntity(30.2, 40.4, 26.4, 29.2, "201306051235"));
//	ohlc.add(new OHLCEntity(26.2, 36.4, 20.4, 30.2, "201306051235"));
//	ohlc.add(new OHLCEntity(25.2, 26.4, 12.4, 24.2, "201306051235"));
//	ohlc.add(new OHLCEntity(20.2, 28.4, 15.4, 22.2, "201306051235"));
//	ohlc.add(new OHLCEntity(35.6, 38.0, 20.4, 28.6, "201305151203"));
//	ohlc.add(new OHLCEntity(15.2, 26.4, 21.4, 23.8, "201305021836"));
//	ohlc.add(new OHLCEntity(30.2, 46.4, 26.4, 29.2, "201306051235"));
//	ohlc.add(new OHLCEntity(26.2, 36.4, 20.4, 30.2, "201306051235"));
//	ohlc.add(new OHLCEntity(25.2, 26.4, 12.4, 24.2, "201306051235"));
//	ohlc.add(new OHLCEntity(20.2, 38.4, 15.4, 22.2, "201306051235"));
//	ohlc.add(new OHLCEntity(28.2, 39.4, 24.4, 32.2, "201306051235"));
//	ohlc.add(new OHLCEntity(35.2, 35.4, 19.4, 25.2, "201306051235"));
//	ohlc.add(new OHLCEntity(35.6, 45.0, 20.4, 28.6, "201305151203"));
//	ohlc.add(new OHLCEntity(15.2, 26.4, 21.4, 23.8, "201305021836"));
//	ohlc.add(new OHLCEntity(30.2, 46.4, 26.4, 29.2, "201306051235"));
//	ohlc.add(new OHLCEntity(26.2, 36.4, 20.4, 30.2, "201306051235"));
}
