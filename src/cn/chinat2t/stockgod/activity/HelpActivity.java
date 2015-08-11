package cn.chinat2t.stockgod.activity;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.chinat2t.stockgod.MainActivity;
import cn.chinat2t.stockgod.R;

public class HelpActivity extends Activity implements OnCheckedChangeListener, OnClickListener{
	
	private ListView hepListView;
	private ListBaseAdapter adapter;
	private String[] mStrings = new String[]{"应用常见问题", "炒股基础知识", 
			"用户等级经验对照表", "用户等级权限对照表", "VIP等级成长值对照表", "VIP等级权限对照表"};
	private RadioGroup radioGroup;
	private View kefuLayout;
	private RadioGroup kefuRadioGroup;
	private RadioButton radioBtn0;
	private RadioButton radioBtn1;
	private RadioButton radioBtn2;
	private RadioButton radioBtn3;
	private RadioButton radioBtn4;
	private RadioButton radioBtn5;
	private RadioButton radioBtn;
	private TextView textName;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.help_layout);
		
		radioGroup = (RadioGroup) findViewById(R.id.help_radiogroup);
		radioGroup.setOnCheckedChangeListener(this);
		kefuRadioGroup = (RadioGroup) findViewById(R.id.kefu_radiogroup);
		kefuRadioGroup.setOnCheckedChangeListener(this);
		textName = (TextView) findViewById(R.id.help_name_textView1);
		radioBtn0 = (RadioButton) findViewById(R.id.radio0);
		radioBtn1 = (RadioButton) findViewById(R.id.radio1);
		radioBtn2 = (RadioButton) findViewById(R.id.radio2);
		radioBtn3 = (RadioButton) findViewById(R.id.radio3);
		radioBtn4 = (RadioButton) findViewById(R.id.radio4);
		radioBtn5 = (RadioButton) findViewById(R.id.radio5);
		radioBtn0.setOnClickListener(this);
		radioBtn1.setOnClickListener(this);
		radioBtn2.setOnClickListener(this);
		radioBtn3.setOnClickListener(this);
		radioBtn4.setOnClickListener(this);
		radioBtn5.setOnClickListener(this);
		radioBtn = radioBtn0;
		hepListView = (ListView) findViewById(R.id.help_listView);
		kefuLayout = findViewById(R.id.help_kefu_layout);
		adapter = new ListBaseAdapter();
		hepListView.setAdapter(adapter);
		
		onCheckedChanged(radioGroup, R.id.help_bangzhu);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MainActivity.setTitleName("帮助");
	}
	
	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.radio0:
			radioBtn.setChecked(false);
			radioBtn0.setChecked(true);
			radioBtn = radioBtn0;
			findViewById(R.id.help_value_layout).setVisibility(View.GONE);
			break;
			
		case R.id.radio1:
			radioBtn.setChecked(false);
			radioBtn1.setChecked(true);
			radioBtn = radioBtn1;
			findViewById(R.id.help_value_layout).setVisibility(View.GONE);
			break;
			
		case R.id.radio2:
			radioBtn.setChecked(false);
			radioBtn2.setChecked(true);
			radioBtn = radioBtn2;
			textName.setText("举报用户名称：");
			findViewById(R.id.help_value_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.help_value_layout1).setVisibility(View.VISIBLE);
			findViewById(R.id.help_value_layout2).setVisibility(View.GONE);
			break;
			
		case R.id.radio3:
			radioBtn.setChecked(false);
			radioBtn3.setChecked(true);
			radioBtn = radioBtn3;
			textName.setText("充值订单号：");
			findViewById(R.id.help_value_layout).setVisibility(View.VISIBLE);
			findViewById(R.id.help_value_layout1).setVisibility(View.VISIBLE);
			findViewById(R.id.help_value_layout2).setVisibility(View.VISIBLE);
			break;
			
		case R.id.radio4:
			radioBtn.setChecked(false);
			radioBtn4.setChecked(true);
			radioBtn = radioBtn4;
			findViewById(R.id.help_value_layout).setVisibility(View.GONE);
			break;
			
		case R.id.radio5:
			radioBtn.setChecked(false);
			radioBtn5.setChecked(true);
			radioBtn = radioBtn5;
			findViewById(R.id.help_value_layout).setVisibility(View.GONE);
			break;
		}
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.help_bangzhu:
			hepListView.setVisibility(View.VISIBLE);
			kefuLayout.setVisibility(View.GONE);
			break;

		case R.id.help_kefu:
			hepListView.setVisibility(View.GONE);
			kefuLayout.setVisibility(View.VISIBLE);
			onCheckedChanged(kefuRadioGroup, R.id.kefu_normal);
			break;
			
		case R.id.kefu_normal:
			break;
			
		case R.id.kefu_vip:
			break;
		}
	}
	
	class ListBaseAdapter extends BaseAdapter{
		private LayoutInflater inflater;

		public ListBaseAdapter() {
			inflater = LayoutInflater.from(getApplicationContext());
		}
		@Override
		public int getCount() {
			return mStrings.length;
		}

		@Override
		public Object getItem(int position) {
			return mStrings[position];
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			if(convertView == null){
				convertView = inflater.inflate(android.R.layout.simple_list_item_1, null);
				((TextView)convertView).setTextColor(getResources().getColor(R.color.black));
			}
			((TextView)convertView).setText(mStrings[position]);
			if (position % 2 == 1) {
				convertView.setBackgroundColor(Color.parseColor("#fff4eddd"));
			} else {
				convertView.setBackgroundColor(Color.parseColor("#ffeee2d1"));
			}
			return convertView;
		}
		
	}
	
	@Override
	public void onBackPressed() {
		CompetitiveGroup.getInstance().back();
	}
}
