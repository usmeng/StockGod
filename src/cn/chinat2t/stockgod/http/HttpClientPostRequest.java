package cn.chinat2t.stockgod.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.CoreConnectionPNames;
import org.apache.http.util.EntityUtils;


/**
 * Http Post  
 *
 */
public class HttpClientPostRequest{

	private static HttpClientPostRequest instance = null;
	
	private HttpClientPostRequest(){}
	
	public static HttpClientPostRequest getInstance(){
		if (instance == null)
			instance = new HttpClientPostRequest();
		
		return instance;
	}
	
	
	public InputStream requestPostInputStream(String url,Map<String, Object> params) throws IllegalStateException,IOException {
		if (url == null || "".equals(url) || params == null || params.size() == 0)
			return null;
		String value = CommunicationUtils.buildJsonString(params);
		if (value == null)
			return null;
		
		return requestThree(url, value);
	}
	
	
	
	
	public byte[] requestPostBytes(String url, Map<String, Object> params)throws IllegalStateException, IOException {
		if (url == null || "".equals(url) || params == null || params.size() == 0)
			return null;
		
		String value = CommunicationUtils.buildJsonString(params);
		if (value == null)
			return null;
		
		return requestTwo(url, value);
	}


	
	public String requestPostString(String url, Map<String, Object> params)throws ClientProtocolException, IOException {
		if (url == null || "".equals(url) || params == null || params.size() == 0)
			return null;
		
		
		String value = CommunicationUtils.buildJsonString(params);
		if (value == null)
			return null;
		
		return requestOne(url, value);
	}

	
	
	public void release() {
		instance = null;	
	}
	
	/**
	 * @param url
	 * @param param
	 * @return String
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	private String requestOne(String url, String params ) throws ClientProtocolException, IOException{
		String resultStr = null;
		HttpResponse httpResponse = getRequestResponse(url, params);
		if (httpResponse.getStatusLine().getStatusCode() == 200){
			HttpEntity resultEntity = httpResponse.getEntity();
			resultStr = EntityUtils.toString(resultEntity);
		}
		
		return resultStr;
	}
	
	/*******************************************
	 * @param url
	 * @param params
	 * @return byte[]
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	private byte[] requestTwo(String url, String params) throws ClientProtocolException, IOException{
		byte[] resultBytes = null;
		InputStream inputStream = requestThree(url, params);
		
		if (inputStream != null)
			resultBytes = CommunicationUtils.readFromInputStream(inputStream);
		
		return resultBytes;
	}

	/**
	 * @param url
	 * @param params
	 * @return
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	private InputStream requestThree(String url, String params) throws ClientProtocolException, IOException{
		
		InputStream inputStream = null;
		HttpResponse httpResponse = getRequestResponse(url, params);
		
		if (httpResponse.getStatusLine().getStatusCode() == 200){
			HttpEntity resultEntity = httpResponse.getEntity();
			inputStream = resultEntity.getContent();
		}
		
		return inputStream;
	}
	
	/**
	 * @param url
	 * @param params
	 * @return HttpResponse
	 * @throws IOException 
	 * @throws ClientProtocolException 
	 */
	private HttpResponse getRequestResponse(String url, String params) throws ClientProtocolException, IOException{
		HttpPost httpPost = new HttpPost(url);
		httpPost.setHeader("Content-Type","application/json;charset=UTF-8");
		StringEntity entity = new StringEntity(params,"utf-8");
		httpPost.setEntity(entity);
		DefaultHttpClient defaultHttpClient=new DefaultHttpClient();
		
		if(null!=HttpClientGetRequest.cookieStore){
			((AbstractHttpClient) defaultHttpClient).setCookieStore(HttpClientGetRequest.cookieStore);
		}
		defaultHttpClient.getParams().setParameter(CoreConnectionPNames.CONNECTION_TIMEOUT, 10 * 1000);  
		defaultHttpClient.getParams().setParameter(CoreConnectionPNames.SO_TIMEOUT, 10 * 1000);
		HttpResponse httpResponse =defaultHttpClient.execute(httpPost);
		if (httpResponse.getStatusLine().getStatusCode() == 200){
			HttpClientGetRequest.cookieStore = ((AbstractHttpClient) defaultHttpClient)
					.getCookieStore();
		}
		return httpResponse;
	}
}
