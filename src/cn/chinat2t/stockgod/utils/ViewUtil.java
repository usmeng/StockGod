package cn.chinat2t.stockgod.utils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import cn.chinat2t.stockgod.MainActivity;
import cn.chinat2t.stockgod.R;
import cn.chinat2t.stockgod.activity.MallActivity;
import cn.chinat2t.stockgod.activity.RegActivity;
import cn.chinat2t.stockgod.bean.ChargeBean;

public class ViewUtil {
	private static ProgressDialog dialogLoading;
	public static Dialog showLoading(final Activity act){
		if(dialogLoading != null && dialogLoading.isShowing()){
			
		}else{
			dialogLoading = new ProgressDialog(act);
			dialogLoading.setMessage("正在加载中，请稍后...");
			dialogLoading.setIndeterminate(true);
			dialogLoading.setCancelable(true);
			dialogLoading.show();
		}
        return dialogLoading;
	}
	
	public static Dialog showNewLoading(final Activity act) {
		ProgressDialog dialogLoading = new ProgressDialog(act);
		dialogLoading.setMessage("请稍后...");
		dialogLoading.setIndeterminate(true);
		dialogLoading.setCancelable(true);
		dialogLoading.show();
		return dialogLoading;
	}
	
	public static void showDialog(Activity act, String title, String message){
		View view = LayoutInflater.from(act).inflate(R.layout.guess_dialog_munu, null);
//		TextView tx0 = (TextView) view.findViewById(R.id.textView1);
		TextView tx1 = (TextView) view.findViewById(R.id.textView2);
		TextView tx2 = (TextView) view.findViewById(R.id.textView3);
		Button button = (Button) view.findViewById(R.id.guess_confirm);
		Button chongzhi = (Button) view.findViewById(R.id.guess_chongzhi);
		
		final Dialog showDialog = ViewUtil.showDialog(act, view);
		chongzhi.setVisibility(View.GONE);
		button.setText("确定");
		tx1.setText(title);
		tx2.setText(message);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog.cancel();
			}
		});
	}
	
	public static void showChargeDialog(final Activity context, ChargeBean bean, String name) {
		View view = LayoutInflater.from(context).inflate(R.layout.guess_dialog_munu2, null);
		TextView tx1 = (TextView) view.findViewById(R.id.textView1);
		TextView tx2 = (TextView) view.findViewById(R.id.textView2);
		TextView tx3 = (TextView) view.findViewById(R.id.textView3);
		TextView tx4 = (TextView) view.findViewById(R.id.textView4);
		Button button = (Button) view.findViewById(R.id.guess_confirm);
		
		final Dialog showDialog = ViewUtil.showDialog(context.getParent(), view);
		tx1.setText(String.format(tx1.getText().toString(), name));
		tx2.setText("￥" + bean.payMoney);
		tx3.setText(String.format(tx3.getText().toString(), bean.gold));
		tx4.setText(String.format(tx4.getText().toString(), bean.zeng));
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog.cancel();
				if(context instanceof MallActivity){
					MallActivity mall = (MallActivity) context;
					mall.type = MallActivity.MALL_DAOJU;
					mall.isSecondView = true;
					mall.onBackPressed();
				}
			}
		});
	}
	
	public static void showLinShiDialog(final Activity act, String msg){
		View view = LayoutInflater.from(act).inflate(R.layout.guess_dialog_munu, null);
//		TextView tx0 = (TextView) view.findViewById(R.id.textView1);
		TextView tx1 = (TextView) view.findViewById(R.id.textView2);
//		TextView tx2 = (TextView) view.findViewById(R.id.textView3);
		Button button = (Button) view.findViewById(R.id.guess_confirm);
		Button chongzhi = (Button) view.findViewById(R.id.guess_chongzhi);
		
		final Dialog showDialog = ViewUtil.showDialog(act, view);
		chongzhi.setVisibility(View.VISIBLE);
		button.setText("取消");
		tx1.setText(msg);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog.cancel();
			}
		});
		chongzhi.setText("注册");
		chongzhi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				act.sendBroadcast(new Intent(MainActivity.APP_QUIT));
				Intent intent = new Intent(act, RegActivity.class);
				act.startActivity(intent);
			}
		});
	}
	
	public static void showBackDialog(final Activity act, final boolean code, String msg){
		View view = LayoutInflater.from(act).inflate(R.layout.guess_dialog_munu, null);
		TextView tx1 = (TextView) view.findViewById(R.id.textView2);
		Button button = (Button) view.findViewById(R.id.guess_confirm);
		Button chongzhi = (Button) view.findViewById(R.id.guess_chongzhi);
		
		final Dialog showDialog = ViewUtil.showDialog(act, view);
		chongzhi.setVisibility(View.GONE);
		tx1.setText(msg);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if(code){
					act.onBackPressed();
					showDialog.cancel();
				}else{
					showDialog.cancel();
				}
			}
		});
	}
	
	public static Dialog showDialog(Activity act, String title, String message, OnClickListener clickListener){
		View view = LayoutInflater.from(act).inflate(R.layout.guess_dialog_munu, null);
//		TextView tx0 = (TextView) view.findViewById(R.id.textView1);
		TextView tx1 = (TextView) view.findViewById(R.id.textView2);
		TextView tx2 = (TextView) view.findViewById(R.id.textView3);
		Button button = (Button) view.findViewById(R.id.guess_confirm);
		Button chongzhi = (Button) view.findViewById(R.id.guess_chongzhi);
		tx2.setText(message);
		Dialog showDialog = ViewUtil.showDialog(act, view);
		chongzhi.setVisibility(View.GONE);
		button.setText("确定");
		tx1.setText(title);
		button.setOnClickListener(clickListener);
		return showDialog;
	}
	
	public static Dialog showDialog(final Activity act, String title, String message, 
			OnClickListener onPositiveClickListener, 
			OnClickListener onNegetiveClickListener, boolean canVisable){
		View view = LayoutInflater.from(act).inflate(R.layout.guess_dialog_munu, null);
		TextView tx0 = (TextView) view.findViewById(R.id.textView1);
		TextView tx1 = (TextView) view.findViewById(R.id.textView2);
		TextView tx2 = (TextView) view.findViewById(R.id.textView3);
		Button button = (Button) view.findViewById(R.id.guess_confirm);
		Button chongzhi = (Button) view.findViewById(R.id.guess_chongzhi);
		
		final Dialog showDialog = ViewUtil.showDialog(act, view);
		chongzhi.setVisibility(canVisable?View.VISIBLE:View.GONE);
		button.setText("确定");
		tx1.setText(title);
		tx0.setVisibility(View.GONE);
		tx2.setText(message);
		showDialog.setOnDismissListener(new OnDismissListener() {
			
			@Override
			public void onDismiss(DialogInterface dialog) {
				act.finish();
			}
		});
		chongzhi.setOnClickListener(onNegetiveClickListener);
		button.setOnClickListener(onPositiveClickListener);
		Dialog dialog = new Dialog(act, R.style.selectorDialog);
		dialog.setContentView(view);
		dialog.show();
		return dialog;
	}
	
	public static Dialog showDialog(final Activity act, View view){
		Dialog dialog = new Dialog(act, R.style.selectorDialog);
        dialog.setContentView(view);
        dialog.show();
        return dialog;
	}
	
	public static void dismiss(Dialog dialog){
		if(dialog != null && dialog.isShowing())
			dialog.dismiss();
	}
	
	public static void dismiss(){
		if(dialogLoading != null && dialogLoading.isShowing())
			dialogLoading.dismiss();
	}
	
	public static void showToast(final String str, final Activity ctx){
		ctx.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
//				StockApplication.showToast(ctx, str);
				Activity parent = ((Activity)ctx).getParent();
				if(parent != null){
					showDialog(parent, str, null);
				}else{
					showDialog((Activity)ctx, str, null);
				}
			}
		});
	}
	
	public static void showToast(final String str, final Context ctx){
//		StockApplication.showToast(ctx, str);
		Activity parent = ((Activity)ctx).getParent();
		if(parent != null){
			showDialog(parent, str, null);
		}else{
			showDialog((Activity)ctx, str, null);
		}
	}
	
	public static Bitmap downloadBitmap(String urlStr){
		Bitmap bitmap=null;
		try {
            URL url = new URL(urlStr);
            HttpURLConnection con = (HttpURLConnection) url.openConnection();
            con.setDoInput(true);
            con.connect();
            InputStream inputStream = con.getInputStream();
//			if (inputStream == null){
//				throw new RuntimeException("stream is null");
//			}else{
//				try {
//					byte[] data=readStream(inputStream);
//					if(data!=null){
//						bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
//					}
//				} catch (Exception e) {
//					e.printStackTrace();
//				}
//				
//				inputStream.close();
//			}
            bitmap = BitmapFactory.decodeStream(inputStream); 
            inputStream.close();
		} 
		catch (MalformedURLException e) {
            e.printStackTrace();
        }catch (IOException e) {
        	e.printStackTrace();
        } 
		return bitmap;
	}
	
	/*
	 * 得到图片字节流 数组大小
	 * */
	public static byte[] readStream(InputStream inStream) throws Exception{      
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();      
        byte[] buffer = new byte[1024];      
        int len = 0;      
        while( (len=inStream.read(buffer)) != -1){      
            outStream.write(buffer, 0, len);      
        }      
        outStream.close();      
        inStream.close();      
        return outStream.toByteArray();      
    }
}
