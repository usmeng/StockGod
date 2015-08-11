package cn.chinat2t.stockgod.activity;

import android.app.Activity;
import android.os.Bundle;
import cn.chinat2t.stockgod.views.KLineView;

public class TestKlineActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(new KLineView(this));
	}
	
	@Override
	public void onBackPressed() {
		CompetitiveGroup.getInstance().back();
	}
	
}
