package cn.chinat2t.stockgod.http;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.AbstractHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;

import android.content.ContentValues;
import android.util.Log;
import cn.chinat2t.stockgod.utils.CtLog;

/**********************************************
 * 
 */
public class HttpClientGetRequest {
	private static HttpClientGetRequest instance = null;
	public static  CookieStore cookieStore;

	private HttpClientGetRequest() {

	}

	public static HttpClientGetRequest getInstance() {
		if (instance == null)
			instance = new HttpClientGetRequest();

		return instance;
	}

	// ======================================
	public InputStream requestGetInputStream(String url)
			throws IllegalStateException, IOException {
		if (url == null || "".equals(url))
			return null;

		return requestThree(url, null);
	}

	// ======================================
	public InputStream requestGetInputStream(String url, String param)
			throws IllegalStateException, IOException {
		if (url == null || "".equals(url) || param == null)
			return null;

		return requestThree(url, param);
	}

	// ======================================
	public InputStream requestGetInputStream(String url,
			Map<String, String> params) throws IllegalStateException,
			IOException {
		if (url == null || "".equals(url) || params == null
				|| params.size() == 0)
			return null;

		String newParam = CommunicationUtils.getNewParam(params);
		if (newParam == null)
			return null;

		return requestThree(url, newParam);
	}

	// ======================================
	public InputStream requestGetInputStream(String url, ContentValues params)
			throws IllegalStateException, IOException {
		if (url == null || "".equals(url) || params == null
				|| params.size() == 0)
			return null;

		String newParam = CommunicationUtils.getNewParam(params);

		if (newParam == null)
			return null;

		return requestThree(url, newParam);
	}

	// ======================================
	public InputStream requestGetInputStream(String url,
			List<NameValuePair> params) throws ClientProtocolException,
			IOException {
		if (url == null || "".equals(url) || params == null
				|| params.size() == 0)
			return null;

		String newParam = CommunicationUtils.getNewParam(params);
		if (newParam == null)
			return null;

		return requestThree(url, newParam);
	}

	// ========================================
	public byte[] requestGetBytes(String url) throws IllegalStateException,
			IOException {
		if (url == null || "".equals(url))
			return null;

		return requestTwo(url, null);
	}

	// =========================================
	public byte[] requestGetBytes(String url, String param)
			throws IllegalStateException, IOException {
		if (url == null || "".equals(url) || param == null || "".equals(param))
			return null;

		return requestTwo(url, param);
	}

	// =========================================
	public byte[] requestGetBytes(String url, Map<String, String> params)
			throws IllegalStateException, IOException {
		if (url == null || "".equals(url) || params == null
				|| params.size() == 0)
			return null;

		String newParam = CommunicationUtils.getNewParam(params);

		if (newParam == null)
			return null;

		return requestTwo(url, newParam);
	}

	// ========================================
	public byte[] requestGetBytes(String url, ContentValues params)
			throws IllegalStateException, IOException {
		if (url == null || "".equals(url) || params == null
				|| params.size() == 0)
			return null;

		String newParam = CommunicationUtils.getNewParam(params);

		if (newParam == null)
			return null;

		return requestTwo(url, newParam);
	}

	// ========================================
	public byte[] requestGetBytes(String url, List<NameValuePair> params)
			throws ClientProtocolException, IOException {
		if (url == null || "".equals(url) || params == null
				|| params.size() == 0)
			return null;

		String newParam = CommunicationUtils.getNewParam(params);
		if (newParam == null)
			return null;

		return requestTwo(url, newParam);

	}

	// =========================================
	public String requestGetString(String url) throws ClientProtocolException,
			IOException {
		if (url == null || "".equals(url))
			return null;

		String resultStr = requestOne(url);

		return resultStr;
	}

	// =========================================
	public String requestGetString(String url, String param)
			throws ClientProtocolException, IOException {
		if (url == null || "".equals(url) || param == null || "".equals(param))
			return null;
		String resultStr = null;
		try{
			resultStr = requestOne(url, param);
		}catch (Exception e) {
			e.printStackTrace();
		}

		return resultStr;
	}

	// =========================================
	public String requestGetString(String url, Map<String, String> params)
			throws ClientProtocolException, IOException {
		if (url == null || "".equals(url) || params == null
				|| params.size() == 0)
			return null;

		String newParam = CommunicationUtils.getNewParam(params);

		if (newParam == null)
			return null;

		String resultStr = requestOne(url, newParam);

		return resultStr;
	}

	// ==========================================
	public String requestGetString(String url, ContentValues params)
			throws ClientProtocolException, IOException {
		if (url == null || "".equals(url) || params == null
				|| params.size() == 0)
			return null;

		String newParam = CommunicationUtils.getNewParam(params);

		if (newParam == null)
			return null;

		String resultStr = requestOne(url, newParam);

		return resultStr;
	}

	// ==========================================
	public String requestGetString(String url, List<NameValuePair> params)
			throws ClientProtocolException, IOException {
		if (url == null || "".equals(url) || params == null
				|| params.size() == 0)
			return null;

		String newParam = CommunicationUtils.getNewParam(params);
		if (newParam == null)
			return null;

		String resultStr = requestOne(url, newParam);

		return resultStr;
	}

	// ========================================
	public void release() {
		instance = null;
	}

	private String requestOne(String url){
		return requestOne(url, null);
	}
	
	/*****************************************
	 * 
	 * 
	 * 
	 * @param url
	 * @return String
	 * @throws ClientProtocolException
	 * @throws IOException
	 */
	private String requestOne(String url, String param){

		String requestUrl = url;

		if (param != null){
			requestUrl = url + "?" + param;
		}
		CtLog.d("requestUrl = " + requestUrl);
		String resultStr = null;
		HttpGet httpGet = new HttpGet(requestUrl);
		httpGet.setHeader("Content-Type", "application/json;charset=UTF-8");
		HttpClient httpclient = new DefaultHttpClient();
		HttpParams params = httpclient.getParams();
		HttpConnectionParams.setConnectionTimeout(params, 20 * 1000);
		HttpConnectionParams.setSoTimeout(params, 20 * 1000);
		if (cookieStore != null) {
			((AbstractHttpClient) httpclient).setCookieStore(cookieStore);
		}
		try {
			HttpResponse httpresponse = httpclient.execute(httpGet);
			if (httpresponse.getStatusLine().getStatusCode() == 200) {
				resultStr = EntityUtils.toString(httpresponse.getEntity(), "UTF-8");
				cookieStore = ((AbstractHttpClient) httpclient).getCookieStore();
			}
		} catch (ClientProtocolException e) {
			Log.d("chinat2t", "ClientProtocolException:" + e.getMessage());
			e.printStackTrace();
		} catch (IOException e) {
			Log.d("chinat2t", "IOException:" + e.getMessage());
			e.printStackTrace();
		}

		return resultStr;
	}
	
	/****************************************
	 * 
	 * @param url
	 * @return byte[]
	 * @throws IOException
	 * @throws IllegalStateException
	 */
	private byte[] requestTwo(String url, String param)
			throws IllegalStateException, IOException {
		byte[] resultBytes = null;
		InputStream inputStream = requestThree(url, param);
		CommunicationUtils.readFromInputStream(inputStream);

		return resultBytes;
	}

	/**************************************
	 * 
	 * @param url
	 * @return
	 * @throws IllegalStateException
	 * @throws IOException
	 */
	private InputStream requestThree(String url, String param)
			throws IllegalStateException, IOException {
		String newUrl = url;

		if (param != null)
			newUrl = url + "?" + param;

		InputStream inputStream = null;
		HttpGet httpGet = new HttpGet(newUrl);
		HttpResponse httpresponse = new DefaultHttpClient().execute(httpGet);
		if (httpresponse.getStatusLine().getStatusCode() == 200) {
			HttpEntity entity = httpresponse.getEntity();
			inputStream = entity.getContent();
		} else {
			return null;
		}

		return inputStream;
	}


}
