package cn.chinat2t.stockgod.bean;

public class KlineData {
	
	public int id;
    public double noeprice;  //�ּ�
    public double navprice;  //����
    public double shou;      //�ɽ������֣�
    public String yearm;     //ʱ�䣨���£�
    public String dayhour;   //ʱ�䣨ʱ�֣�
    public double open;      //����
    public double high;      //���
    public double low;       //���
    public double close;     //����
    
    public int    xianflag;  //�ߵı�־
    public double jine;      //���
    public int    sid;
    public int    kid;
    public int    status;
    
    /**  �ǵ���*/
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
