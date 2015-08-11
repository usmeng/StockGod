package cn.chinat2t.stockgod.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.chinat2t.stockgod.MainActivity;
import cn.chinat2t.stockgod.R;
import cn.chinat2t.stockgod.bean.NewsBean;

public class NewsActivity extends Activity implements OnCheckedChangeListener{

	private RadioGroup radioGroup;
	private Button deleteBtn;
	private Button flagBtn;
	private CheckBox selectAll;
	private ListView listView;
	private View view1;
	private View view2;
	private TextView nameTv;
	private List<NewsBean> mList;
	private NewsAdapter adapter;
	private TextView timeTv;
	private TextView contentTv;
	private int flagIndex;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.news_layout);
		
		initViews();
		initDatas();
		onCheckedChanged(radioGroup, R.id.news_private);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MainActivity.setTitleName("��Ϣ����");
	}
	
	private void initViews() {
		radioGroup = (RadioGroup)findViewById(R.id.news_radiogroup);
		radioGroup.setOnCheckedChangeListener(this);
		deleteBtn = (Button)findViewById(R.id.news_button1);
		flagBtn = (Button)findViewById(R.id.news_button2);
		selectAll = (CheckBox)findViewById(R.id.news_check_all_select);
		listView = (ListView)findViewById(R.id.news_listView1);
		view1 = findViewById(R.id.news_list_layout);
		view2 = findViewById(R.id.news_text_layout);
		nameTv = (TextView)findViewById(R.id.news_textView3);
		timeTv = (TextView)findViewById(R.id.news_textView4);
		contentTv = (TextView)findViewById(R.id.news_textView6);
	}

	public void initDatas(){
		adapter = new NewsAdapter();
		mList = new ArrayList<NewsBean>();
		loadingData();
		adapter.setData(mList);
		listView.setAdapter(adapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				view1.setVisibility(View.GONE);
				view2.setVisibility(View.VISIBLE);
				NewsBean bean = mList.get(position);
				nameTv.setText(bean.name);
				timeTv.setText(bean.time);
				contentTv.setText(bean.content);
			}
		});
		
	}

	private void loadingData() {
		mList.clear();
		if(flagIndex == 0){
			mList.add(new NewsBean("���ǵ��ɼ�", "2013-07-02", "��ý���1000���"));
			mList.add(new NewsBean("ѡ�ɾ����ɼ�", "2013-07-03", "��ý���2000���"));
			mList.add(new NewsBean("����Ծ��ɼ�", "2013-07-04", "��ý���1000�ʽ�"));
			mList.add(new NewsBean("���ǵ��ɼ�", "2013-07-05", "��ý���1000���"));
			mList.add(new NewsBean("���ǵ��ɼ�", "2013-07-06", "��ý���1000�ʽ�"));
		}else{
			mList.add(new NewsBean("�������", "2013-10-01", "����������~~"));
			mList.add(new NewsBean("�������", "2013-08-15", "����������~~"));
			mList.add(new NewsBean("���ڴ���", "2013-01-02", "����������~~"));
			mList.add(new NewsBean("��һ����", "2013-05-01", "����������~~"));
			mList.add(new NewsBean("���˽ڴ���", "2013-07-07", "����������~~"));
		}
	}
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.news_private:
			flagIndex = 0;
			break;

		case R.id.news_public:
			flagIndex = 1;
			break;
		}
		loadingData();
		view1.setVisibility(View.VISIBLE);
		view2.setVisibility(View.GONE);
		adapter.setData(mList);
		adapter.notifyDataSetInvalidated();
	}
	
	class NewsAdapter extends BaseAdapter {

		private List<NewsBean> mList = new ArrayList<NewsBean>();

		public void setData(List<NewsBean> mList) {
			this.mList = mList;
		}

		@Override
		public int getCount() {
			return mList.size();
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
				arg1 = View.inflate(NewsActivity.this, R.layout.news_list_item, null);
				holder = new DongTaiHolder();
				holder.linearLayout = arg1.findViewById(R.id.news_item_layout);
				holder.che = (CheckBox) arg1.findViewById(R.id.news_item_checkBox1);
				holder.tv1 = (TextView) arg1.findViewById(R.id.news_item_textView1);
				holder.tv2 = (TextView) arg1.findViewById(R.id.news_item_textView2);
				arg1.setTag(holder);
			} else {
				holder = (DongTaiHolder) arg1.getTag();
			}
			if (arg0 % 2 == 1) {
				holder.linearLayout.setBackgroundColor(Color.parseColor("#fff4eddd"));
			} else {
				holder.linearLayout.setBackgroundColor(Color.parseColor("#ffeee2d1"));
			}
			final NewsBean news = mList.get(arg0);
			holder.che.setChecked(news.isSelect);
			holder.tv1.setText(news.name);
			holder.tv2.setText(news.time);
			return arg1;
		}
	}

	class DongTaiHolder {
		public View linearLayout;
		public CheckBox che;
		public TextView tv1;
		public TextView tv2;
	}
	
	@Override
	public void onBackPressed() {
		CompetitiveGroup.getInstance().back();
	}

	@Override
	public void onContentChanged() {
		MainActivity.setTitleName("��Ϣ����");
	}
}
