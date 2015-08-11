package cn.chinat2t.stockgod.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.chinat2t.stockgod.MainActivity;
import cn.chinat2t.stockgod.R;
import cn.chinat2t.stockgod.bean.FriendBean;
import cn.chinat2t.stockgod.bean.UserBean;
import cn.chinat2t.stockgod.bean.UserFenSi;
import cn.chinat2t.stockgod.bean.UserGuanZhu;
import cn.chinat2t.stockgod.bean.UserZhuiZhong;
import cn.chinat2t.stockgod.http.CommunicationManager;
import cn.chinat2t.stockgod.http.CommunicationResultListener;
import cn.chinat2t.stockgod.utils.ViewUtil;

public class FriendActivity extends Activity implements OnCheckedChangeListener {

	protected static final int FRIEND_OF_DONGTAI = 0;
	protected static final int FRIEND_OF_ZHUIZONG = 1;
	protected static final int FRIEND_OF_GUANZHU = 2;
	protected static final int FRIEND_OF_FENSI = 3;
	protected static final int FRIEND_OF_CANCEL_GUANZHU = 4;
	protected static final int FRIEND_OF_START_ZHUIZHONG = 5;
	protected static final int FRIEND_OF_FENSI_START_ZHUIZHONG = 6;
	protected static final int FRIEND_OF_FENSI_CANCEL_GUANZHU = 7;
	private ListView mList;
	private RadioGroup radioGroup;
	private DongTaiAdapter dAdapter;
	private ZhuiZongAdapter zAdapter;
	private GuanZhuAdapter gAdapter;
	private FenSiAdapter fAdapter;

	private List<FriendBean> dongtaiList;
	private List<UserZhuiZhong> zhuizongList;
	private List<UserGuanZhu> guanzhuList;
	private List<UserFenSi> fensiList;

	private RadioButton dRadio;
	private UserBean user;
	private RadioButton fRadio;
	private RadioButton gRadio;
	private RadioButton zRadio;
	private View titleBar;
	private Dialog showLoading;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.friend_layout);
		user = UserBean.getInstance();

		mList = (ListView) findViewById(R.id.friend_listView);
		titleBar = findViewById(R.id.friend_title_bar);
		
		dAdapter = new DongTaiAdapter();
		zAdapter = new ZhuiZongAdapter();
		gAdapter = new GuanZhuAdapter();
		fAdapter = new FenSiAdapter();

		radioGroup = ((RadioGroup) this.findViewById(R.id.friend_radio_group));
		radioGroup.setOnCheckedChangeListener(this);

		dRadio = ((RadioButton) findViewById(R.id.friend_radio_dongtai));
		fRadio = ((RadioButton) findViewById(R.id.friend_radio_fensi));
		gRadio = ((RadioButton) findViewById(R.id.friend_radio_guanzhu));
		zRadio = ((RadioButton) findViewById(R.id.friend_radio_zhuizong));
		
		onCheckedChanged(radioGroup, R.id.friend_radio_dongtai);
	}

	@Override
	protected void onResume() {
		super.onResume();
		MainActivity.setTitleName("好友");
	};

	Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.arg1) {
			case FRIEND_OF_DONGTAI:
				dongtaiList = (List<FriendBean>) msg.obj;
				dRadio.setText("动态" + (dongtaiList.size()==0?"":dongtaiList.size()));
				dAdapter.setData(dongtaiList);
				mList.setAdapter(dAdapter);
				dAdapter.notifyDataSetInvalidated();
				break;

			case FRIEND_OF_ZHUIZONG:
				zhuizongList = (List<UserZhuiZhong>) msg.obj;
				zRadio.setText("追踪" + (zhuizongList.size()==0?"":zhuizongList.size()));
				zAdapter.setData(zhuizongList);
				mList.setAdapter(zAdapter);
				zAdapter.notifyDataSetInvalidated();
				break;

			case FRIEND_OF_GUANZHU:
				guanzhuList = (List<UserGuanZhu>) msg.obj;
				gRadio.setText("关注" + (guanzhuList.size()==0?"":guanzhuList.size()));
				gAdapter.setData(guanzhuList);
				mList.setAdapter(gAdapter);
				gAdapter.notifyDataSetInvalidated();
				break;

			case FRIEND_OF_FENSI:
				fensiList = (List<UserFenSi>) msg.obj;
				fRadio.setText("粉丝" + (fensiList.size()==0?"":fensiList.size()));
				fAdapter.setData(fensiList);
				mList.setAdapter(fAdapter);
				fAdapter.notifyDataSetInvalidated();
				break;
				
			case FRIEND_OF_CANCEL_GUANZHU:
				guanzhuList.remove(msg.what);
				gRadio.setText("关注" + (guanzhuList.size()==0?"":guanzhuList.size()));
				gAdapter.setData(guanzhuList);
				mList.setAdapter(gAdapter);
				gAdapter.notifyDataSetInvalidated();
				break;
				
			case FRIEND_OF_START_ZHUIZHONG:
				UserGuanZhu zhu = guanzhuList.get(msg.what);
				zhu.guanzhuflag = 1;
				gRadio.setText("关注" + (guanzhuList.size()==0?"":guanzhuList.size()));
				gAdapter.setData(guanzhuList);
				mList.setAdapter(gAdapter);
				gAdapter.notifyDataSetInvalidated();
				break;
				
			case FRIEND_OF_FENSI_START_ZHUIZHONG:
				UserFenSi fenSi = fensiList.get(msg.what);
				fenSi.guanzhuflag = 1;
				fRadio.setText("粉丝" + (fensiList.size()==0?"":fensiList.size()));
				fAdapter.setData(fensiList);
				mList.setAdapter(fAdapter);
				fAdapter.notifyDataSetInvalidated();
				break;

			case FRIEND_OF_FENSI_CANCEL_GUANZHU:
				fensiList.remove(msg.what);
				fRadio.setText("粉丝" + (fensiList.size()==0?"":fensiList.size()));
				fAdapter.setData(fensiList);
				mList.setAdapter(fAdapter);
				fAdapter.notifyDataSetInvalidated();
				break;
			}
			ViewUtil.dismiss(showLoading);
			return false;
		}
	});
	
	private CommunicationResultListener dongTaiListener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED && token > 0){
				Message message = handler.obtainMessage();
				message.arg1 = FRIEND_OF_DONGTAI;
				message.obj = resultData;
				handler.sendMessage(message);
			}else{
				ViewUtil.dismiss(showLoading);
//				ViewUtil.showToast((String)resultData, FriendActivity.this);
			}
		};
	};
	
	private CommunicationResultListener zhuiZhongListener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED && token > 0){
				Message message = handler.obtainMessage();
				message.arg1 = FRIEND_OF_ZHUIZONG;
				message.obj = resultData;
				handler.sendMessage(message);
			}else{
				ViewUtil.dismiss(showLoading);
//				ViewUtil.showToast((String)resultData, FriendActivity.this);
			}
		};
	};
	
	private CommunicationResultListener guanZhuListener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED && token > 0){
				Message message = handler.obtainMessage();
				message.arg1 = FRIEND_OF_GUANZHU;
				message.obj = resultData;
				handler.sendMessage(message);
			}else{
				ViewUtil.dismiss(showLoading);
//				ViewUtil.showToast((String)resultData, FriendActivity.this);
			}
		};
	};
	
	private CommunicationResultListener fenSiListener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED && token > 0){
				Message message = handler.obtainMessage();
				message.arg1 = FRIEND_OF_FENSI;
				message.obj = resultData;
				handler.sendMessage(message);
			}else{
				ViewUtil.dismiss(showLoading);
//				ViewUtil.showToast((String)resultData, FriendActivity.this);
			}
		};
	};
	
	private CommunicationResultListener startZhuiListener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED && token > 0){
				Message message = handler.obtainMessage();
				message.arg1 = FRIEND_OF_START_ZHUIZHONG;
				message.obj = resultData;
				handler.sendMessage(message);
			}else{
				ViewUtil.dismiss(showLoading);
//				ViewUtil.showToast((String)resultData, FriendActivity.this);
			}
		};
	};
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.friend_radio_dongtai:
			titleBar.setVisibility(View.GONE);
			showLoading = ViewUtil.showLoading(this.getParent());
			CommunicationManager.getInstance().getDongTaiList(user.uid, dongTaiListener);
			break;

		case R.id.friend_radio_zhuizong:
			titleBar.setVisibility(View.VISIBLE);
			showLoading = ViewUtil.showLoading(this.getParent());
			CommunicationManager.getInstance().getZhuiZongList(user.uid, zhuiZhongListener);
			break;
		case R.id.friend_radio_guanzhu:
			titleBar.setVisibility(View.VISIBLE);
			showLoading = ViewUtil.showLoading(this.getParent());
			CommunicationManager.getInstance().getGuanZhuList(user.uid, guanZhuListener);
			break;
		case R.id.friend_radio_fensi:
			titleBar.setVisibility(View.VISIBLE);
			showLoading = ViewUtil.showLoading(this.getParent());
			CommunicationManager.getInstance().getFansList(user.uid, fenSiListener);
			break;
		}
	}
	
	class DongTaiAdapter extends BaseAdapter {

		private List<FriendBean> mList = new ArrayList<FriendBean>();

		public void setData(List<FriendBean> mList) {
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
				arg1 = View.inflate(FriendActivity.this, R.layout.friend_dongtai_list_item, null);
				holder = new DongTaiHolder();
				holder.linearLayout = (LinearLayout) arg1.findViewById(R.id.friend_dongtai_list_item);
				holder.tv1 = (TextView) arg1.findViewById(R.id.friend_dongtai_textView1);
				holder.tv2 = (TextView) arg1.findViewById(R.id.friend_dongtai_textView2);
				arg1.setTag(holder);
			} else {
				holder = (DongTaiHolder) arg1.getTag();
			}
			final FriendBean friendBean = mList.get(arg0);
			String text = holder.tv1.getText().toString();
			if (arg0 % 2 == 1) {
				holder.linearLayout.setBackgroundColor(Color.parseColor("#fff4eddd"));
			} else {
				holder.linearLayout.setBackgroundColor(Color.parseColor("#ffeee2d1"));
			}

			holder.tv1.setText(Html.fromHtml(String.format(text,
					"<font color=\"#00FF00\">"+ friendBean.nickname + "</font>",
					"<font color=\"#FF0000\">"+ 1000 + "</font>",
					"<font color=\"#0000FF\">"+ 8600 + "</font>",
					"<font color=\"#00FF00\">某股票</font>")));
			holder.tv1.setMovementMethod(LinkMovementMethod.getInstance());

			arg1.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent intent = new Intent(FriendActivity.this, ZhuYeActivity.class);
					intent.putExtra("userID", friendBean.uid);
					CompetitiveGroup.group.switchActivity("ZhuYeActivity", intent, -1, -1);
				}
			});
			return arg1;
		}
	}
	
	class FenSiAdapter extends BaseAdapter {

		private List<UserFenSi> mList = new ArrayList<UserFenSi>();

		public void setData(List<UserFenSi> mList) {
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
				arg1 = View.inflate(FriendActivity.this, R.layout.friend_guanzhu_list_item, null);
				holder = new DongTaiHolder();
				holder.linearLayout = (LinearLayout) arg1.findViewById(R.id.friend_guanzhu_list_item);
				holder.tv1 = (TextView) arg1.findViewById(R.id.friend_guanzhu_textView1);
				holder.tv2 = (TextView) arg1.findViewById(R.id.friend_guanzhu_textView2);
				holder.tv3 = (TextView) arg1.findViewById(R.id.friend_guanzhu_textView3);
				holder.btn6 = (Button) arg1.findViewById(R.id.friend_guanzhu_button1);
				holder.btn7 = (Button) arg1.findViewById(R.id.friend_guanzhu_button2);
				arg1.setTag(holder);
			} else {
				holder = (DongTaiHolder) arg1.getTag();
			}
			
			final UserFenSi guanZhu = mList.get(arg0);
			if (arg0 % 2 == 1) {
				holder.linearLayout.setBackgroundColor(Color.parseColor("#fff4eddd"));
			} else {
				holder.linearLayout.setBackgroundColor(Color.parseColor("#ffeee2d1"));
			}
			holder.tv1.setText(guanZhu.nickname);
			holder.tv2.setText(guanZhu.level + "");
			holder.tv3.setText(guanZhu.vip>=3?"VIP":"普通用户");
			holder.btn6.setText(guanZhu.guanzhuflag==1?"已追踪":"追踪");
			if(guanZhu.guanzhuflag != 1){
				holder.btn6.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						CommunicationManager.getInstance().zhuiZongFriend(1, guanZhu.uid, new CommunicationResultListener(arg0) {
							public void resultListener(byte result, Object resultData) {
								if(result == CommunicationManager.SUCCEED && token > 0){
									Message message = handler.obtainMessage();
									message.arg1 = FRIEND_OF_FENSI_START_ZHUIZHONG;
									message.obj = resultData;
									message.what = Integer.parseInt(this.tag.toString());
									handler.sendMessage(message);
								}else{
									ViewUtil.dismiss(showLoading);
//									ViewUtil.showToast((String)resultData, FriendActivity.this);
								}
							};
						});
					}
				});
			}
			holder.btn6.setBackgroundResource(guanZhu.guanzhuflag==1?R.drawable.btn_yellow_selector:R.drawable.btn_red_selector);
			holder.btn7.setText("取消关注");
			holder.btn7.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CommunicationManager.getInstance().cancelGuanZhu(1, guanZhu.fensiid, new CommunicationResultListener(arg0) {
						
						public void resultListener(byte result, Object resultData) {
							if(result == CommunicationManager.SUCCEED && token > 0){
								Message message = handler.obtainMessage();
								message.arg1 = FRIEND_OF_FENSI_CANCEL_GUANZHU;
								message.obj = resultData;
								message.what = Integer.parseInt(this.tag.toString());
								handler.sendMessage(message);
							}else{
								ViewUtil.dismiss(showLoading);
//								ViewUtil.showToast((String)resultData, FriendActivity.this);
							}
						};
					});
				}
			});
			return arg1;
		}
	}

	class ZhuiZongAdapter extends BaseAdapter {

		private List<UserZhuiZhong> mList = new ArrayList<UserZhuiZhong>();

		public void setData(List<UserZhuiZhong> mList) {
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
				arg1 = View.inflate(FriendActivity.this, R.layout.friend_guanzhu_list_item, null);
				holder = new DongTaiHolder();
				holder.linearLayout = (LinearLayout) arg1.findViewById(R.id.friend_guanzhu_list_item);
				holder.tv1 = (TextView) arg1.findViewById(R.id.friend_guanzhu_textView1);
				holder.tv2 = (TextView) arg1.findViewById(R.id.friend_guanzhu_textView2);
				holder.tv3 = (TextView) arg1.findViewById(R.id.friend_guanzhu_textView3);
				holder.btn6 = (Button) arg1.findViewById(R.id.friend_guanzhu_button1);
				holder.btn7 = (Button) arg1.findViewById(R.id.friend_guanzhu_button2);
				arg1.setTag(holder);
			} else {
				holder = (DongTaiHolder) arg1.getTag();
			}
			if (arg0 % 2 == 1) {
				holder.linearLayout.setBackgroundColor(Color.parseColor("#fff4eddd"));
			} else {
				holder.linearLayout.setBackgroundColor(Color.parseColor("#ffeee2d1"));
			}
			return arg1;
		}
	}

	class GuanZhuAdapter extends BaseAdapter {

		private List<UserGuanZhu> mList = new ArrayList<UserGuanZhu>();

		public void setData(List<UserGuanZhu> mList) {
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
				arg1 = View.inflate(FriendActivity.this, R.layout.friend_guanzhu_list_item, null);
				holder = new DongTaiHolder();
				holder.linearLayout = (LinearLayout) arg1.findViewById(R.id.friend_guanzhu_list_item);
				holder.tv1 = (TextView) arg1.findViewById(R.id.friend_guanzhu_textView1);
				holder.tv2 = (TextView) arg1.findViewById(R.id.friend_guanzhu_textView2);
				holder.tv3 = (TextView) arg1.findViewById(R.id.friend_guanzhu_textView3);
				holder.btn6 = (Button) arg1.findViewById(R.id.friend_guanzhu_button1);
				holder.btn7 = (Button) arg1.findViewById(R.id.friend_guanzhu_button2);
				arg1.setTag(holder);
			} else {
				holder = (DongTaiHolder) arg1.getTag();
			}
			final UserGuanZhu guanZhu = mList.get(arg0);
			if (arg0 % 2 == 1) {
				holder.linearLayout.setBackgroundColor(Color.parseColor("#fff4eddd"));
			} else {
				holder.linearLayout.setBackgroundColor(Color.parseColor("#ffeee2d1"));
			}
			holder.tv1.setText(guanZhu.nickname);
			holder.tv2.setText(guanZhu.level + "");
			holder.tv3.setText(guanZhu.usertype>=3?"VIP":"普通用户");
			holder.btn6.setText(guanZhu.guanzhuflag==1?"已追踪":"追踪");
			if(guanZhu.guanzhuflag != 1){
				holder.btn6.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						CommunicationManager.getInstance().zhuiZongFriend(1, guanZhu.uid, new CommunicationResultListener(arg0) {
							public void resultListener(byte result, Object resultData) {
								if(result == CommunicationManager.SUCCEED && token > 0){
									Message message = handler.obtainMessage();
									message.arg1 = FRIEND_OF_START_ZHUIZHONG;
									message.obj = resultData;
									message.what = Integer.parseInt(this.tag.toString());
									handler.sendMessage(message);
								}else{
									ViewUtil.dismiss(showLoading);
//									ViewUtil.showToast((String)resultData, FriendActivity.this);
								}
							};
						});
					}
				});
			}
			holder.btn6.setBackgroundResource(guanZhu.guanzhuflag==1?R.drawable.btn_yellow_selector:R.drawable.btn_red_selector);
			holder.btn7.setText("取消关注");
			holder.btn7.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					CommunicationManager.getInstance().cancelGuanZhu(1, guanZhu.beiuid, new CommunicationResultListener(arg0) {
						
						public void resultListener(byte result, Object resultData) {
							if(result == CommunicationManager.SUCCEED && token > 0){
								Message message = handler.obtainMessage();
								message.arg1 = FRIEND_OF_CANCEL_GUANZHU;
								message.obj = resultData;
								message.what = Integer.parseInt(this.tag.toString());
								handler.sendMessage(message);
							}else{
								ViewUtil.dismiss(showLoading);
//								ViewUtil.showToast((String)resultData, FriendActivity.this);
							}
						};
					});
				}
			});
			return arg1;
		}
	}

	class DongTaiHolder {
		public LinearLayout linearLayout;
		public TextView tv1;
		public TextView tv2;
		public TextView tv3;
		public ImageView iv;
		public Button btn7;
		public Button btn6;
	}
	
	@Override
	public void onBackPressed() {
		CompetitiveGroup.getInstance().back();
	}

	@Override
	public void onContentChanged() {
		MainActivity.setTitleName("好友");
	}
}
