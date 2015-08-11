/*
 * Copyright (C) 2010 The MobileSecurePay Project
 * All right reserved.
 * author: shiqun.shi@alipay.com
 */

package cn.chinat2t.stockgod.alipay;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.RemoteException;

import com.alipay.android.app.IAlixPay;
import com.alipay.android.app.IRemoteServiceCallback;

/**
 * 和安全支付服务�?信，发�?订单信息进行支付，接收支付宝返回信息
 * 
 */
public class MobileSecurePayer {
	static String TAG = "MobileSecurePayer";

	Integer lock = 0;
	IAlixPay mAlixPay = null;
	boolean mbPaying = false;

	Activity mActivity = null;

	// 和安全支付服务建立连�?
	private ServiceConnection mAlixPayConnection = new ServiceConnection() {

		public void onServiceConnected(ComponentName className, IBinder service) {
			//
			// wake up the binder to continue.
			// 获得通信通道
			synchronized (lock) {
				mAlixPay = IAlixPay.Stub.asInterface(service);
				lock.notify();
			}
		}

		public void onServiceDisconnected(ComponentName className) {
			mAlixPay = null;
		}
	};

	/**
	 * 向支付宝发�?支付请求
	 * 
	 * @param strOrderInfo
	 *            订单信息
	 * @param callback
	 *            回调handler
	 * @param myWhat
	 *            回调信息
	 * @param activity
	 *            目标activity
	 * @return
	 */
	public boolean pay(final String strOrderInfo, final Handler callback,
			final int myWhat, final Activity activity) {
		if (mbPaying)
			return false;
		mbPaying = true;

		//
		mActivity = activity;

		// bind the service.
		// 绑定服务
		if (mAlixPay == null) {
			// 绑定安全支付服务�?��获取上下文环境，
			// 如果绑定不成功使用mActivity.getApplicationContext().bindService
			// 解绑时同�?
			mActivity.getApplicationContext().bindService(
					new Intent(IAlixPay.class.getName()), mAlixPayConnection,
					Context.BIND_AUTO_CREATE);
		}
		// else ok.

		// 实例�?��线程来进行支�?
		new Thread(new Runnable() {
			public void run() {
				try {
					// wait for the service bind operation to completely
					// finished.
					// Note: this is important,otherwise the next mAlixPay.Pay()
					// will fail.
					// 等待安全支付服务绑定操作结束
					// 注意：这里很重要，否则mAlixPay.Pay()方法会失�?
					synchronized (lock) {
						if (mAlixPay == null)
							lock.wait();
					}

					// register a Callback for the service.
					// 为安全支付服务注册一个回�?
					mAlixPay.registerCallback(mCallback);

					// call the MobileSecurePay service.
					// 调用安全支付服务的pay方法
					String strRet = mAlixPay.Pay(strOrderInfo);
					BaseHelper.log(TAG, "After Pay: " + strRet);

					// set the flag to indicate that we have finished.
					// unregister the Callback, and unbind the service.
					// 将mbPaying置为false，表示支付结�?
					// 移除回调的注册，解绑安全支付服务
					mbPaying = false;
					mAlixPay.unregisterCallback(mCallback);
					mActivity.getApplicationContext().unbindService(
							mAlixPayConnection);

					// send the result back to caller.
					// 发�?交易结果
					Message msg = new Message();
					msg.what = myWhat;
					msg.obj = strRet;
					callback.sendMessage(msg);
				} catch (Exception e) {
					e.printStackTrace();

					// send the result back to caller.
					// 发�?交易结果
					Message msg = new Message();
					msg.what = myWhat;
					msg.obj = e.toString();
					callback.sendMessage(msg);
				}
			}
		}).start();

		return true;
	}

	/**
	 * This implementation is used to receive callbacks from the remote service.
	 * 实现安全支付的回�?
	 */
	private IRemoteServiceCallback mCallback = new IRemoteServiceCallback.Stub() {
		/**
		 * This is called by the remote service regularly to tell us about new
		 * values. Note that IPC calls are dispatched through a thread pool
		 * running in each process, so the code executing here will NOT be
		 * running in our main thread like most other things -- so, to update
		 * the UI, we need to use a Handler to hop over there. 通过IPC机制启动安全支付服务
		 */
		public void startActivity(String packageName, String className,
				int iCallingPid, Bundle bundle) throws RemoteException {
			Intent intent = new Intent(Intent.ACTION_MAIN, null);

			if (bundle == null)
				bundle = new Bundle();
			// else ok.

			try {
				bundle.putInt("CallingPid", iCallingPid);
				intent.putExtras(bundle);
			} catch (Exception e) {
				e.printStackTrace();
			}

			intent.setClassName(packageName, className);
			mActivity.startActivity(intent);
		}

		/**
		 * when the msp loading dialog gone, call back this method.
		 */
		@Override
		public boolean isHideLoadingScreen() throws RemoteException {
			return false;
		}

		/**
		 * when the current trade is finished or cancelled, call back this method.
		 */
		@Override
		public void payEnd(boolean arg0, String arg1) throws RemoteException {

		}

	};
}