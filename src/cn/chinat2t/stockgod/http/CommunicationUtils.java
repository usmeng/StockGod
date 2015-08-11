package cn.chinat2t.stockgod.http;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;


import android.content.ContentValues;

/*******************************************
 *
 */
public class CommunicationUtils {
	
	public static String getNewParam(Map<String, String> params) throws UnsupportedEncodingException{
		
		String paramStr = buildString(params);
		
		if (paramStr == null)
			return null;
		
		
		return paramStr;
	}
	
	public static String getNewParam(ContentValues params) throws UnsupportedEncodingException{
		
		String paramStr = buildString(params);
		
		if (paramStr == null)
			return null;
		
		String newParam = getUrlEncode(paramStr, "UTF-8");
		
		return newParam;
	}
	
	public static String getNewParam(List<NameValuePair> params) throws UnsupportedEncodingException{
		
		String paramStr = buildString(params);
		
		if (paramStr == null)
			return null;
		
		String newParam = getUrlEncode(paramStr, "UTF-8");
		
		return newParam;
	}
	
	public static List<NameValuePair> convertParams(ContentValues params){
		if (params == null || params.size() == 0)
			return null;
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();	
    	Set<Entry<String,Object>> setEntry = params.valueSet();
    	BasicNameValuePair bnvp = null;
    	
    	for (Entry<String,Object> entry : setEntry){
    		 bnvp = new BasicNameValuePair(entry.getKey(),entry.getValue().toString());
    		 nameValuePairs.add(bnvp);
    	}
    	
    	return nameValuePairs;
	}
	
	public static List<NameValuePair> convertParams(Map<String, String> params){
		if (params == null || params.size() == 0)
			return null;
		
		List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
		Set<Entry<String,String>> setEntry = params.entrySet();
    	BasicNameValuePair bnvp = null;
    	
    	for (Entry<String,String> entry : setEntry){
   		 	bnvp = new BasicNameValuePair(entry.getKey(),entry.getValue());
   		 	nameValuePairs.add(bnvp);
    	}
    	
    	return nameValuePairs;    	
	}
	
	public static String getUrlEncode(String str, String codeType) throws UnsupportedEncodingException{
		if (str == null || "".equals(str))
			return null;
		
		String resultString = null;
		if (codeType == null || "".equals(codeType))			
			resultString = URLEncoder.encode(str,"UTF-8");
		else
			resultString = URLEncoder.encode(str, codeType);
		
		return resultString.toLowerCase() ;
	}
	
    public static String buildString(Map<String,String> params){
    	if (params == null || params.size() == 0)
    		return null;
    	
    	StringBuilder sb = new StringBuilder();  	
    	Set<Entry<String,String>> setEntry = params.entrySet();
    	
    	for (Entry<String,String> entry : setEntry){
    		sb.append(entry.getKey());
    		sb.append("=");
    		try {
				sb.append(getUrlEncode(entry.getValue(), "utf-8") + "&");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
    	}
    	
    	sb.deleteCharAt(sb.length() - 1);
    	return sb.toString();
    }
    
    public static String buildString(ContentValues params){
    	if (params == null || params.size() == 0)
    		return null;
    	
    	Set<Entry<String,Object>> setEntry = params.valueSet();
    	StringBuilder sb = new StringBuilder();
    	
    	for (Entry<String,Object> entry : setEntry){
    		sb.append(entry.getKey());
    		sb.append("=");
    		sb.append(entry.getValue());
    	}
    	
    	return sb.toString();
    }
    
    public static String buildString(List<NameValuePair> params){
    	if (params == null || params.size() == 0)
    		return null;
    	
    	StringBuilder sb = new StringBuilder();
    	
    	for (NameValuePair pair : params){
    		sb.append(pair.getName());
    		sb.append("=");
    		sb.append(pair.getValue());
    	}
    	
    	return sb.toString();
    }
    
    public static byte[] readFromInputStream(InputStream inputStream) throws IOException{
    	
    	if (inputStream == null)
    		return null;
    	
    	BufferedInputStream bin = new BufferedInputStream(inputStream);
    	ByteArrayOutputStream bos = new ByteArrayOutputStream();
    	
    	byte[] buffer = new byte[1024 * 8];
    	int len = 0;
    	
    	while ((len = bin.read(buffer)) != -1){
    		bos.write(buffer, 0, len);
    	}
    	
    	bin.close();
    	
    	return bos.toByteArray();
    }
    
    
    /**
     * json
     * @param params
     * @return
     */
    public static String buildJsonString(Map<String, Object> params){
    	if (params == null || params.size() == 0)
			return null;
		
		Set<Entry<String,Object>> setEntry = params.entrySet();
    	StringBuilder str = new StringBuilder();
    	str.append("{");
    	for (Entry<String, Object> entry : setEntry){
    		str.append("\"");
    		str.append(entry.getKey());
    		str.append("\"");
    		str.append(":");
    		if(entry.getValue() instanceof String){
    			str.append("\"");
    		}
    		str.append(entry.getValue());
    		if(entry.getValue() instanceof String){
    			str.append("\"");
    		}
    		str.append(",");
    	}
    	str.setCharAt(str.length()-1, '}');
    	return str.toString();
    }
}
