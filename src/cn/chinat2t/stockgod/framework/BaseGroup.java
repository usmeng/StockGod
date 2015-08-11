package cn.chinat2t.stockgod.framework;

import android.app.Activity;
import android.app.ActivityGroup;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Window;
import android.widget.LinearLayout.LayoutParams;
import android.widget.Toast;
import android.widget.ViewFlipper;
import cn.chinat2t.stockgod.MainActivity;
import cn.chinat2t.stockgod.StockApplication;
import cn.chinat2t.stockgod.activity.FastFightActivity;

public class BaseGroup extends ActivityGroup {

	protected TabStack stack = new TabStack();
	protected ViewFlipper containerFlipper;
	public static String tag = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	public void switchActivity(String id, Intent intent, int inAnimation, int outAnimation) {
		if(id.equals(getCurrentTag())) return;
		Activity activityByTag = getActivityByTag(getCurrentTag());
		if(activityByTag != null) activityByTag.onDetachedFromWindow();
		if(FastFightActivity.gameStatus == FastFightActivity.GAME_ONGOING) {
			tag = id;
			return;
		}else{
			tag = "";
		}
		if("CompetitiveActivity".equals(id)){
			popSome(id);
		}else if(stack.isExist(id)){
			pop(id);
		}
		intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
		Window window = getLocalActivityManager().startActivity(id, intent);
		View v = window.getDecorView();
		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		LayoutParams param = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT);
		v.setLayoutParams(param);
		StockApplication.cancelToast();
		containerFlipper.addView(v);
		containerFlipper.showNext();
		stack.push(id);
		Activity activity = getActivityByTag(getCurrentTag());
		if(activity != null) activity.onContentChanged();
		
		//  切换Activity时，前一个调用onDetachedFromWindow()；
		//  后一个调用onContentChanged();
	}

	public void back() {
		if (!stack.isEmpty() && stack.size() > 1) {
			//  切换Activity时，当前这个调用onDetachedFromWindow()；
			//  后一个调用onContentChanged();
			Activity activityByTag = getActivityByTag(getCurrentTag());
			if(activityByTag != null) activityByTag.onDetachedFromWindow();
			StockApplication.cancelToast();
			containerFlipper.showPrevious();
			containerFlipper.removeViewAt(stack.size() - 1);
			stack.pop();
			Activity activity = getActivityByTag(getCurrentTag());
			if(activity != null) activity.onContentChanged();
		} else {
			((MainActivity) getParent()).exitApp();
		}
	}

	public Activity getCurrentActivity(){
		return getLocalActivityManager().getActivity(stack.top());
	}
	
	public String getCurrentTag(){
		return stack.top();
	}
	
	public Activity getActivityByTag(String tag){
		return getLocalActivityManager().getActivity(tag);
	}
	
	@Override
	public void onBackPressed() {
		getActivityByTag(getCurrentTag()).onBackPressed();
	}
	
	@Override
	protected void onStop() {
		super.onStop();
		getActivityByTag(getCurrentTag()).onDetachedFromWindow();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		Toast.makeText(this, "the result of activity", 0).show();
	}
	
	public void pop(String id){
		int index = stack.getCurrentViewIndex(id);
		if(index >= 0){
			stack.pop(id);
			containerFlipper.removeViewAt(index + 1);
		}
	}
	
	public void popSome(String id) {
		int sum = stack.getTheSumToPop(id);
		if(sum > 0){
			containerFlipper.removeViews(0, sum);
			stack.popSome(id);
		}
	}
}
