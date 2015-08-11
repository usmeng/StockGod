package cn.chinat2t.stockgod.http;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;

import android.content.ContentValues;
import android.view.View;

public class NetworkManager {
	
//	private final String TAG = "NetworkManager";

	private static NetworkManager instance = null;
	private RequestPackage requestPackage;
	private RequestThread requestThread;

	private NetworkManager() {
		requestThread = new RequestThread();
		requestThread.start();
	}

	public static NetworkManager getInstance() {
		if (instance == null)
			instance = new NetworkManager();

		return instance;
	}

	// ==========================================
	public void requestGetBytes(String url, NetworkRequestListener listener) {
		if (url == null || "".equals(url)) {
			urlError();
			return;
		}

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_1;
		requestPackage.listener = listener;
		requestPackage.params.add(url);

		requestThread.addRequestPackage(requestPackage);
	}

	// ===========================================
	public void requestGetString(String url, NetworkRequestListener listener) {
		if (url == null || "".equals(url)) {
			urlError();
			return;
		}
		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_2;
		requestPackage.listener = listener;
		requestPackage.params.add(url);

		requestThread.addRequestPackage(requestPackage);
	}

	
	// ===========================================
	public void requestGetInputStream(String url,
			NetworkRequestListener listener) {
		if (url == null || "".equals(url)) {
			urlError();
			return;
		}

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_3;
		requestPackage.listener = listener;
		requestPackage.params.add(url);

		requestThread.addRequestPackage(requestPackage);
	}

	// ==========================================
	public void requestGetBytes(String url, String param,
			NetworkRequestListener listener) {
		if (url == null || "".equals(url)) {
			urlError();
			return;
		}

		if (param == null || "".equals(param)) {
			paramsError();
			return;
		}

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_4;
		requestPackage.listener = listener;
		requestPackage.params.add(url);
		requestPackage.params.add(param);

		requestThread.addRequestPackage(requestPackage);
	}

	// ===========================================
	public void requestGetInputStream(String url, String param,
			NetworkRequestListener listener) {
		if (url == null || "".equals(url)) {
			urlError();
			return;
		}

		if (param == null || "".equals(param)) {
			paramsError();
			return;
		}

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_5;
		requestPackage.listener = listener;
		requestPackage.params.add(url);
		requestPackage.params.add(param);

		requestThread.addRequestPackage(requestPackage);
	}

	public void requestGetInputStream(String url, String param,
			NetworkRequestListener listener, View view) {

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_26;
		requestPackage.listener = listener;
		requestPackage.params.add(url);
		requestPackage.params.add(param);
		requestPackage.view = view;

		requestThread.addRequestPackage(requestPackage);
	}

	public void uploadtPostInputStream(String url, Map<String, String> param,
			Map<String, File> file, NetworkRequestListener listener) {

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_27;
		requestPackage.listener = listener;
		requestPackage.params.add(url);
		requestPackage.params.add(param);
		requestPackage.params.add(file);

		requestThread.addRequestPackage(requestPackage);
	}

	// ===========================================
	public void requestGetString(String url, String param,
			NetworkRequestListener listener) {
		if (url == null || "".equals(url)) {
			urlError();
			return;
		}

		if (param == null || "".equals(param)) {
			paramsError();
			return;
		}

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_6;
		requestPackage.listener = listener;
		requestPackage.params.add(url);
		requestPackage.params.add(param);

		requestThread.addRequestPackage(requestPackage);
	}

	// ==========================================
	public void requestGetBytes(String url, Map<String, String> params,
			NetworkRequestListener listener) {
		if (url == null || "".equals(url)) {
			urlError();
			return;
		}

		if (params == null || params.size() == 0) {
			paramsError();
			return;
		}

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_7;
		requestPackage.listener = listener;
		requestPackage.params.add(url);
		requestPackage.params.add(params);

		requestThread.addRequestPackage(requestPackage);
	}

	// ===========================================
	public void requestGetInputStream(String url, Map<String, String> params,
			NetworkRequestListener listener) {
		if (url == null || "".equals(url)) {
			urlError();
			return;
		}

		if (params == null || params.size() == 0) {
			paramsError();
			return;
		}

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_8;
		requestPackage.listener = listener;
		requestPackage.params.add(url);
		requestPackage.params.add(params);

		requestThread.addRequestPackage(requestPackage);
	}

	// ===========================================
	public void requestGetString(String url, Map<String, String> params,
			NetworkRequestListener listener) {
		if (url == null || "".equals(url)) {
			urlError();
			return;
		}

		if (params == null || params.size() == 0) {
			paramsError();
			return;
		}

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_9;
		requestPackage.listener = listener;
		requestPackage.params.add(url);
		requestPackage.params.add(params);

		requestThread.addRequestPackage(requestPackage);
	}

	// ===========================================
	public void requestPostBytes(String url, Map<String, String> params,
			NetworkRequestListener listener) {
		if (url == null || "".equals(url)) {
			urlError();
			return;
		}

		if (params == null || params.size() == 0) {
			paramsError();
			return;
		}

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_10;
		requestPackage.listener = listener;
		requestPackage.params.add(url);
		requestPackage.params.add(params);

		requestThread.addRequestPackage(requestPackage);
	}

	// ===========================================
	public void requestPostInputStream(String url, Map<String, String> params,
			NetworkRequestListener listener) {
		if (url == null || "".equals(url)) {
			urlError();
			return;
		}

		if (params == null || params.size() == 0) {
			paramsError();
			return;
		}

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_11;
		requestPackage.listener = listener;
		requestPackage.params.add(url);
		requestPackage.params.add(params);

		requestThread.addRequestPackage(requestPackage);
	}

	// ===========================================
	public void requestPostString(String url, Map<String, Object> params,
			NetworkRequestListener listener) {
		if (url == null || "".equals(url)) {
			urlError();
			return;
		}

		if (params == null || params.size() == 0) {
			paramsError();
			return;
		}

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_12;
		requestPackage.listener = listener;
		requestPackage.params.add(url);
		requestPackage.params.add(params);

		requestThread.addRequestPackage(requestPackage);
	}

	// ===========================================
	public void requestGetBytes(String url, List<NameValuePair> params,
			NetworkRequestListener listener) {
		if (url == null || "".equals(url)) {
			urlError();
			return;
		}

		if (params == null || params.size() == 0) {
			paramsError();
			return;
		}

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_13;
		requestPackage.listener = listener;
		requestPackage.params.add(url);
		requestPackage.params.add(params);

		requestThread.addRequestPackage(requestPackage);
	}

	// ===========================================
	public void requestGetInputStream(String url, List<NameValuePair> params,
			NetworkRequestListener listener) {
		if (url == null || "".equals(url)) {
			urlError();
			return;
		}

		if (params == null || params.size() == 0) {
			paramsError();
			return;
		}

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_14;
		requestPackage.listener = listener;
		requestPackage.params.add(url);
		requestPackage.params.add(params);

		requestThread.addRequestPackage(requestPackage);
	}

	// ===========================================
	public void requestGetString(String url, List<NameValuePair> params,
			NetworkRequestListener listener) {
		if (url == null || "".equals(url)) {
			urlError();
			return;
		}

		if (params == null || params.size() == 0) {
			paramsError();
			return;
		}

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_15;
		requestPackage.listener = listener;
		requestPackage.params.add(url);
		requestPackage.params.add(params);

		requestThread.addRequestPackage(requestPackage);
	}

	// ===========================================
	public void requestPostBytes(String url, List<NameValuePair> params,
			NetworkRequestListener listener) {
		if (url == null || "".equals(url)) {
			urlError();
			return;
		}

		if (params == null || params.size() == 0) {
			paramsError();
			return;
		}

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_16;
		requestPackage.listener = listener;
		requestPackage.params.add(url);
		requestPackage.params.add(params);

		requestThread.addRequestPackage(requestPackage);
	}

	// ===========================================
	public void requestPostInputStream(String url, List<NameValuePair> params,
			NetworkRequestListener listener) {
		if (url == null || "".equals(url)) {
			urlError();
			return;
		}

		if (params == null || params.size() == 0) {
			paramsError();
			return;
		}

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_17;
		requestPackage.listener = listener;
		requestPackage.params.add(url);
		requestPackage.params.add(params);

		requestThread.addRequestPackage(requestPackage);
	}

	// ===========================================
	public void requestPostString(String url, List<NameValuePair> params,
			NetworkRequestListener listener) {
		if (url == null || "".equals(url)) {
			urlError();
			return;
		}

		if (params == null || params.size() == 0) {
			paramsError();
			return;
		}

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_18;
		requestPackage.listener = listener;
		requestPackage.params.add(url);
		requestPackage.params.add(params);

		requestThread.addRequestPackage(requestPackage);
	}

	// ==========================================
	public void requestGetBytes(String url, ContentValues params,
			NetworkRequestListener listener) {
		if (url == null || "".equals(url)) {
			urlError();
			return;
		}

		if (params == null || params.size() == 0) {
			paramsError();
			return;
		}

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_19;
		requestPackage.listener = listener;
		requestPackage.params.add(url);
		requestPackage.params.add(params);

		requestThread.addRequestPackage(requestPackage);
	}

	// ===========================================
	public void requestGetInputStream(String url, ContentValues params,
			NetworkRequestListener listener) {
		if (url == null || "".equals(url)) {
			urlError();
			return;
		}

		if (params == null || params.size() == 0) {
			paramsError();
			return;
		}

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_20;
		requestPackage.listener = listener;
		requestPackage.params.add(url);
		requestPackage.params.add(params);

		requestThread.addRequestPackage(requestPackage);
	}

	// ===========================================
	public void requestGetString(String url, ContentValues params,
			NetworkRequestListener listener) {
		if (url == null || "".equals(url)) {
			urlError();
			return;
		}

		if (params == null || params.size() == 0) {
			paramsError();
			return;
		}

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_21;
		requestPackage.listener = listener;
		requestPackage.params.add(url);
		requestPackage.params.add(params);

		requestThread.addRequestPackage(requestPackage);
	}

	// ===========================================
	public void requestPostBytes(String url, ContentValues params,
			NetworkRequestListener listener) {
		if (url == null || "".equals(url)) {
			urlError();
			return;
		}

		if (params == null || params.size() == 0) {
			paramsError();
			return;
		}

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_22;
		requestPackage.listener = listener;
		requestPackage.params.add(url);
		requestPackage.params.add(params);

		requestThread.addRequestPackage(requestPackage);
	}

	// ===========================================
	public void requestPostInputStream(String url, ContentValues params,
			NetworkRequestListener listener) {
		if (url == null || "".equals(url)) {
			urlError();
			return;
		}

		if (params == null || params.size() == 0) {
			paramsError();
			return;
		}

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_23;
		requestPackage.listener = listener;
		requestPackage.params.add(url);
		requestPackage.params.add(params);

		requestThread.addRequestPackage(requestPackage);
	}

	// ===========================================
	public void requestPostString(String url, ContentValues params,
			NetworkRequestListener listener) {
		if (url == null || "".equals(url)) {
			urlError();
			return;
		}

		if (params == null || params.size() == 0) {
			paramsError();
			return;
		}

		requestPackage = new RequestPackage();
		requestPackage.choose_method_id = RequestPackage.METHOD_ID_24;
		requestPackage.listener = listener;
		requestPackage.params.add(url);
		requestPackage.params.add(params);

		requestThread.addRequestPackage(requestPackage);
	}



	// ===========================================
	private void urlError() {
	}

	// ===========================================
	private void paramsError() {
	}

	/********************************************
	 */
	public void release() {
		instance = null;
		requestThread.close();
	}

	class RequestThread extends Thread {
		List<RequestPackage> requestPackages;
		boolean isRun = false;
		int sleepTime = 1000;
		HttpClientGetRequest httpClientGetRequest;
		HttpClientPostRequest httpClientPostRequest;


		// =======================================
		RequestThread() {

			requestPackages = new LinkedList<RequestPackage>();
			isRun = true;
			httpClientGetRequest = HttpClientGetRequest.getInstance();
			httpClientPostRequest = HttpClientPostRequest.getInstance();
		}

		// =======================================
		void addRequestPackage(RequestPackage pkg) {
			requestPackages.add(pkg);
		}

		// =======================================
		void close() {
			isRun = false;
			requestPackages = null;
			httpClientGetRequest.release();
			httpClientPostRequest.release();
		}

		// =======================================
		public void run() {
			RequestPackage pkg = null;

			while (isRun) {
				if (requestPackages.size() != 0) {
					synchronized (this) {
						pkg = requestPackages.remove(0);
					}

					try {
						request(pkg);
					} catch (IllegalStateException e) {
						if (pkg.listener != null) {
							pkg.listener.error(INetworkStatusCode.REQUEST_URL_ERROR);
							pkg.listener.resultString(null);
						}

						e.printStackTrace();
					} catch (IOException e) {
						if (pkg.listener != null) {
							pkg.listener.resultString(null);
							pkg.listener.error(INetworkStatusCode.REQUEST_IO_ERROR);
						}

						e.printStackTrace();
					}
				}

				try {
					Thread.sleep(sleepTime);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}

		// ========================================
		@SuppressWarnings("unchecked")
		void request(RequestPackage pkg) throws IllegalStateException, IOException {
			String url = null;
			Object param = null;
			byte[] resultBytes = null;
			InputStream ins = null;
			String resultString = null;

			switch (pkg.choose_method_id) {

			case RequestPackage.METHOD_ID_1:
				url = (String) pkg.params.get(0);
				resultBytes = httpClientGetRequest.requestGetBytes(url);

				if (pkg.listener != null)
					pkg.listener.resultBytes(resultBytes);
				break;

			case RequestPackage.METHOD_ID_2:
				// TODO pkg.listener = netWorkListener
				url = (String) pkg.params.get(0);
				resultString = httpClientGetRequest.requestGetString(url);
				if (pkg.listener != null)
					pkg.listener.resultString(resultString);
				// 调用Communication getDayLine()方法中匿名内部类中的resultString()方法
				break;

			case RequestPackage.METHOD_ID_3:
				url = (String) pkg.params.get(0);
				ins = httpClientGetRequest.requestGetInputStream(url);

				if (pkg.listener != null)
					pkg.listener.resultInputStream(ins);
				break;

			case RequestPackage.METHOD_ID_4:
				url = (String) pkg.params.get(0);
				param = pkg.params.get(1);
				resultBytes = httpClientGetRequest.requestGetBytes(url,
						(String) param);

				if (pkg.listener != null)
					pkg.listener.resultBytes(resultBytes);
				break;

			case RequestPackage.METHOD_ID_5:
				url = (String) pkg.params.get(0);
				param = pkg.params.get(1);
				ins = httpClientGetRequest.requestGetInputStream(url,
						(String) param);

				if (pkg.listener != null)
					pkg.listener.resultInputStream(ins);
				break;

			case RequestPackage.METHOD_ID_6:
				url = (String) pkg.params.get(0);
				param = pkg.params.get(1);
				resultString = httpClientGetRequest.requestGetString(url,
						(String) param);

				if (pkg.listener != null)
					pkg.listener.resultString(resultString);
				break;

			case RequestPackage.METHOD_ID_7:
				url = (String) pkg.params.get(0);
				param = pkg.params.get(1);
				resultBytes = httpClientGetRequest.requestGetBytes(url,
						(Map<String, String>) param);

				if (pkg.listener != null)
					pkg.listener.resultBytes(resultBytes);
				break;

			case RequestPackage.METHOD_ID_8:
				url = (String) pkg.params.get(0);
				param = pkg.params.get(1);
				ins = httpClientGetRequest.requestGetInputStream(url,
						(Map<String, String>) param);

				if (pkg.listener != null)
					pkg.listener.resultInputStream(ins);
				break;

			case RequestPackage.METHOD_ID_9:
				url = (String) pkg.params.get(0);
				param = pkg.params.get(1);
				resultString = httpClientGetRequest.requestGetString(url,
						(Map<String, String>) param);

				if (pkg.listener != null)
					pkg.listener.resultString(resultString);
				break;

			case RequestPackage.METHOD_ID_10:
				url = (String) pkg.params.get(0);
				param = pkg.params.get(1);
				resultBytes = httpClientPostRequest.requestPostBytes(url,
						(Map<String, Object>) param);

				if (pkg.listener != null)
					pkg.listener.resultBytes(resultBytes);
				break;

			case RequestPackage.METHOD_ID_11:
				url = (String) pkg.params.get(0);
				param = pkg.params.get(1);
				ins = httpClientPostRequest.requestPostInputStream(url,
						(Map<String, Object>) param);

				if (pkg.listener != null)
					pkg.listener.resultInputStream(ins);
				break;

			case RequestPackage.METHOD_ID_12:
				url = (String) pkg.params.get(0);
				param = pkg.params.get(1);
				resultString = httpClientPostRequest.requestPostString(url,
						(Map<String, Object>) param);

				if (pkg.listener != null){
					pkg.listener.resultString(resultString);
				}
					
				break;

			case RequestPackage.METHOD_ID_13:
				url = (String) pkg.params.get(0);
				param = pkg.params.get(1);
				resultBytes = httpClientGetRequest.requestGetBytes(url,
						(List<NameValuePair>) param);

				if (pkg.listener != null)
					pkg.listener.resultBytes(resultBytes);
				break;

			case RequestPackage.METHOD_ID_14:
				url = (String) pkg.params.get(0);
				param = pkg.params.get(1);
				ins = httpClientGetRequest.requestGetInputStream(url,
						(List<NameValuePair>) param);

				if (pkg.listener != null)
					pkg.listener.resultInputStream(ins);
				break;

			case RequestPackage.METHOD_ID_15:
				url = (String) pkg.params.get(0);
				param = pkg.params.get(1);
				resultString = httpClientGetRequest.requestGetString(url,
						(List<NameValuePair>) param);

				if (pkg.listener != null)
					pkg.listener.resultString(resultString);
				break;

			case RequestPackage.METHOD_ID_16:
				break;

			case RequestPackage.METHOD_ID_17:
				break;

			case RequestPackage.METHOD_ID_18:
				break;

			case RequestPackage.METHOD_ID_19:
				url = (String) pkg.params.get(0);
				param = pkg.params.get(1);
				resultBytes = httpClientGetRequest.requestGetBytes(url,
						(ContentValues) param);

				if (pkg.listener != null)
					pkg.listener.resultBytes(resultBytes);
				break;

			case RequestPackage.METHOD_ID_20:
				url = (String) pkg.params.get(0);
				param = pkg.params.get(1);
				ins = httpClientGetRequest.requestGetInputStream(url,
						(ContentValues) param);

				if (pkg.listener != null)
					pkg.listener.resultInputStream(ins);
				break;

			case RequestPackage.METHOD_ID_21:
				url = (String) pkg.params.get(0);
				param = pkg.params.get(1);
				resultString = httpClientGetRequest.requestGetString(url,
						(ContentValues) param);

				if (pkg.listener != null)
					pkg.listener.resultString(resultString);
				break;

			case RequestPackage.METHOD_ID_22:
				break;

			case RequestPackage.METHOD_ID_23:
				break;

			case RequestPackage.METHOD_ID_24:
				break;
			case RequestPackage.METHOD_ID_25:
				break;

			case RequestPackage.METHOD_ID_26:
				url = (String) pkg.params.get(0);
				param = pkg.params.get(1);
				ins = httpClientGetRequest.requestGetInputStream(url,
						(String) param);

				if (pkg.listener != null)
					pkg.listener.resultInputStream(ins, pkg.view);
				break;

			case RequestPackage.METHOD_ID_27:
				break;
			}
		}
	}
}
