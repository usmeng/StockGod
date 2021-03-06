package cn.chinat2t.stockgod.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import cn.chinat2t.stockgod.MainActivity;
import cn.chinat2t.stockgod.R;

public class ZhuYeActivity extends Activity {

	private ListView mListView;
	private ZhuyeAdapter adapter;
	private List<Object> list;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.zhuye_layout);
		MainActivity.setTitleName("个人主页");

		mListView = (ListView) findViewById(R.id.zhuye_listView);
		adapter = new ZhuyeAdapter();
		adapter.setData(list);
		mListView.setAdapter(adapter);
	}

	@Override
	public void onBackPressed() {
		CompetitiveGroup.getInstance().back();
	}

	class ZhuyeAdapter extends BaseAdapter {

		private List<Object> mList = new ArrayList<Object>();

		public void setData(List<Object> mList) {
			this.mList = mList;
		}

		@Override
		public int getCount() {
			return 5;
		}

		@Override
		public Object getItem(int arg0) {
			return arg0;
		}

		@Override
		public long getItemId(int arg0) {
			return arg0;
		}

		@Override
		public View getView(final int arg0, View arg1, ViewGroup arg2) {
			DongTaiHolder holder = null;
			if (arg1 == null) {
				arg1 = View.inflate(ZhuYeActivity.this,
						R.layout.friend_zhuye_list_item, null);
				holder = new DongTaiHolder();
				holder.linearLayout = (LinearLayout) arg1
						.findViewById(R.id.friend_zhuye_list_item);
				// holder.tv1 = (TextView)
				// arg1.findViewById(R.id.selflistitem_text1);
				// holder.tv2 = (TextView)
				// arg1.findViewById(R.id.selflistitem_text2);
				// holder.iv = (ImageView)
				// arg1.findViewById(R.id.selflistitem_opt);
				arg1.setTag(holder);
			} else {
				holder = (DongTaiHolder) arg1.getTag();
			}
			switch (arg0 % 2) {
			case 0:
				holder.linearLayout.setBackgroundColor(Color
						.parseColor("#fff4eddd"));
				break;
			case 1:
				holder.linearLayout.setBackgroundColor(Color
						.parseColor("#ffeee2d1"));
				break;
			}
			return arg1;
		}
	}

	class DongTaiHolder {
		public LinearLayout linearLayout;
		public TextView tv1;
		public TextView tv2;
		public TextView tv3;
		public ImageView iv;
		public Button tv7;
	}
	
	@Override
	public void onContentChanged() {
		MainActivity.setTitleName("个人主页");
	}

}
