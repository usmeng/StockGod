package cn.chinat2t.stockgod.http;
public interface INetworkStatusCode {
	public static final short REQUEST_URL_ERROR = -1;
	public static final short REQUEST_IO_ERROR = -2;
	public static final short REQUEST_IO_FAIL = -3;
	public static final short REQUEST_FAIL = -4;
	
	public static final short REQUEST_200 = 200;
	public static final short REQUEST_206 = 206;
}
