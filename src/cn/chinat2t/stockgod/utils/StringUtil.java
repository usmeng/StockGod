package cn.chinat2t.stockgod.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtil {
	

	public static final String format(String str, Object...objects){
		for (int i = 0; i < objects.length; i++) {
			str.replace("?", (String)objects[i]);
		}
		return str;
	}

    public static String goldView(double num){
    	String str = "";
    	if(num < 100000) return (int)num + "";
    	int yushu = (int) (num % 10000);
    	int result = (int) (num / 10000);
    	if(result < 100){
    		str = result + "万";
    	}else if(result < 1000){
    		str = result / 100 + "百万";
    	}else if(result < 10000){
    		str = result / 1000 + "千万";
    	}else{
    		str = result / 10000 + "亿";
    	}
    	if(yushu != 0) str += "+";
    	return str;
    }
    
	public static int paraseString(String str){
		if(str != null && str.length() > 0 && str.matches("^[0-9.]+$")){
			return Integer.parseInt(str);
		}
		return 0;
	}
	
	public static String formatTime(int time){
		int fen = time / 60;
		int miao = time % 60;
		return time(fen)+ ":" + time(miao);
	}
	
	public static String time(int time){
		if(time >= 0 && time < 10){
			return "0"+time;
		}
		return ""+time;
	}

	public static boolean isNegUserName(String name){
		if(name.matches("^[0-9a-zA-Z_]{4,10}$")){
			return true;
		}
		return false;
	}
	
	public static boolean isNegPass(String name){
		if(name != null && name.matches("^[0-9a-zA-Z_]{6,10}$")){
			return true;
		}
		return false;
	}
	
	public static boolean isNegEmail(String name){
		if(name.matches("^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+((\\.[a-zA-Z0-9_-]{2,3}){1,2})$")){
			return true;
		}
		return false;
	}
	
	public static String md5(String plainText) {
        String re_md5 = new String();  
        try {  
            MessageDigest md = MessageDigest.getInstance("MD5");  
            md.update(plainText.getBytes());  
            byte b[] = md.digest();  
            int i;  
            StringBuffer buf = new StringBuffer("");  
            for (int offset = 0; offset < b.length; offset++) {  
                i = b[offset];  
                if (i < 0)  
                    i += 256;  
                if (i < 16)  
                    buf.append("0");  
                buf.append(Integer.toHexString(i));  
            }  
            re_md5 = buf.toString();  
        } catch (NoSuchAlgorithmException e) {  
            e.printStackTrace();  
        }  
        return re_md5;  
    }
	
	public static String getCode(String code){
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < code.length(); i++) {
            char c = code.charAt(i);
			if(c > 47 && c < 58){
				buffer.append(c);
			}
		}
		return buffer.toString();
	}

	public static String getTime(String time) {
		if(time.length() < 5) return "00:00:00";
		StringBuffer buffer = new StringBuffer(time);
		buffer.insert(2, ":");
		buffer.insert(5, ":");
		return buffer.toString();
	}
	
	public static String getFenShiTime(String time) {
		if(time == null) return "00:00";
		if(time.length() < 5) return "00:00";
		time = time.substring(0, 4);
		StringBuffer buffer = new StringBuffer(time);
		buffer.insert(2, ":");
		return buffer.toString();
	}
	
	public static String getKlineTime(String time) {
		if(time.length() < 3) return "00:00";
		StringBuffer buffer = new StringBuffer(time);
		buffer.insert(2, ":");
		return buffer.toString();
	}
	

	public static String getKlineDate(String time) {
		if(time.length() < 7) return "0000-00-00";
		StringBuffer buffer = new StringBuffer(time);
		buffer.insert(4, "-");
		buffer.insert(7, "-");
		return buffer.toString();
	}

	public static int getInt(String str) {
		StringBuffer buffer = new StringBuffer();
		for (int i = 0; i < str.length(); i++) {
			char c = str.charAt(i);
			if(c >= 48 && c <= 57){
				buffer.append(c);
			}
		}
		return Integer.parseInt(buffer.toString());
	}
}
