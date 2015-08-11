package cn.chinat2t.stockgod.activity;

import cn.chinat2t.stockgod.R;
import android.app.Activity;
import android.os.Bundle;

public class PresentActivity extends Activity{
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.present_layout);
	}
	
	@Override
	public void onBackPressed() {
		CompetitiveGroup.getInstance().back();
	}
}
