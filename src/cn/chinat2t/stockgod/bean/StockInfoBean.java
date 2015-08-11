package cn.chinat2t.stockgod.bean;

import java.io.Serializable;

public class StockInfoBean implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	public int id;
    public String ver;       //�汾��
    /** stkLabel������Ҫ����Ĺ�Ʊ�б�(�г�����+��Ʊ���룬SH��ʾ�Ϻ���SZ��ʾ���ڣ�
                    �ο����ʷ��Ʊ���������صļ۸񾫶������һ������ľ���Ϊ׼��Ĭ��2λС��)��*/
    
    public String sid;       //����
    public double sprice;
    public int    member;
    
    public String stklabel;  //����
    public String code;      //Ҫ��ʾ�Ĵ���
    public String sname ;    //����
    public String date;      //����(������)
    public String time;      //ʱ���� 
    public String block;     //����ϵͳ���(��0��ʾSZZS���ο���¼��)
    public int    precisions;//С��λ��
    public int    shou;      //���׵�λ(ÿ�ֶ��ٹ�)
    public int    isbuy;     //������ǣ�С��0: �µ�������0:���ǣ� ����2:ƽ������һ�����ǣ�����-2:ƽ������һ���µ�
    public double opens;     //����
    public double high;      //���
    public double low;       //���
    public double closes;    //����(�ּ�)
    public int    vol;       //�ɽ������֣�
    public double amount;    //�ɽ����Ԫ��
    public int    nowv;      //���֣���ʱ�ɽ���
    public double preclose;  //����
    public double avprice;   //����
    public double sellvol;   //����
    public double buyvol;    //����
    public double changehand;//������(�ٷ���)
    public double weicha;    //ί��֣�
    public double weibi;     //ί��(�ٷ�����
    public double liangbi;   //����(�ٷ���)
    public double zhangdie;  //�ǵ�
    public double zhangfu;   //�Ƿ�(�ٷ���)
    public double zhenfu;    //���(�ٷ���)
    public double shiyinglv; //��ӯ��(�ٷ���)
    public double zongguben; //�ܹɱ�(���)
    public double liutonggu; //��ͨ��(���)
//    public double[] pricesell;//���̼�1��2��3��4��5
//    public double[] volsell;  //������1��2��3��4��5
//    public double[] pricebuy; //���̼�1��2��3��4��5
//    public double[] volbuy;   //������1��2��3��4��5
    public double nowprice;   //�ּ�
    public int    stocktype;  //��Ʊ����
    public double shizhi;
}