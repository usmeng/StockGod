package cn.chinat2t.stockgod.views;

import android.app.ProgressDialog;
import android.content.Context;

public class ProgressDialogLoading extends ProgressDialog{
	
	private LoadingListener loadingListener;

	public abstract class LoadingListener{
		public void dismiss(){
			this.dismiss();
		}
	}
	
	public ProgressDialogLoading(Context context) {
		super(context);
	}
	
	public void setLoadingListener(LoadingListener loadingListener){
		this.loadingListener = loadingListener;
	}
	
	
}