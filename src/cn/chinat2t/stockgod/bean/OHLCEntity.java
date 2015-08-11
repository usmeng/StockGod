package cn.chinat2t.stockgod.bean;

public class OHLCEntity {
	
	/** Open Session Value */
	private double open;
	
	/** Highest Value */
	private double high;
	
	/** Lowest Value */
	private double low;
	
	/** Close Session Value */
	private double close;
	
	/** Date  20110212*/
	private String date;
	
	private double shouHigh;
	
	
	public double getShouHigh() {
		return shouHigh;
	}

	public void setShouHigh(double shouHigh) {
		this.shouHigh = shouHigh;
	}

	public double getShouLow() {
		return shouLow;
	}

	public void setShouLow(double shouLow) {
		this.shouLow = shouLow;
	}

	private double shouLow;
	
	/** ֵΪ0��������ֵΪ1���½�*/
	private int state;

	public double getOpen() {
		return open;
	}

	public void setOpen(double open) {
		this.open = open;
	}

	public double getHigh() {
		return high;
	}

	public void setHigh(double high) {
		this.high = high;
	}

	public double getLow() {
		return low;
	}

	public void setLow(double low) {
		this.low = low;
	}

	public double getClose() {
		return close;
	}

	public void setClose(double close) {
		this.close = close;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public OHLCEntity(double open, double high, double low, double close, double shouHigh, double shuoLow,
			String date) {
		super();
		this.open = open;
		this.high = high;
		this.low = low;
		this.close = close;
		this.date = date;
		this.shouHigh = shouHigh;
		this.shouLow = shuoLow;
		state = (close - open) > 0? 0:1;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public OHLCEntity() {
		super();
	}
}