package cn.chinat2t.stockgod.bean;

public class KlineData {
	
	public int id;
    public double noeprice;  //现价
    public double navprice;  //均价
    public double shou;      //成交量（手）
    public String yearm;     //时间（年月）
    public String dayhour;   //时间（时分）
    public double open;      //开盘
    public double high;      //最高
    public double low;       //最低
    public double close;     //收盘
    
    public int    xianflag;  //线的标志
    public double jine;      //金额
    public int    sid;
    public int    kid;
    public int    status;
    
    /**  涨跌率*/
    public double getZhang(){
    	return 12.3;
    }
    
    public double getJine(){
    	return jine;
    }
    
    public double getHigh(){
    	return high;
    }
    
    public double getShouLow(){
    	return 0;
    }
    
    public double getShouHigh(){
    	return shou;
    }
    
    public double getOpen(){
    	return open;
    }
    
    public double getClose(){
    	return close;
    }
    
    public double getLow(){
    	return low;
    }
    
    public String getDate(){
    	return yearm;
    }
    

    public String getTime(){
    	return dayhour;
    }
}
