package cn.chinat2t.stockgod.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
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
import cn.chinat2t.stockgod.bean.ResponsedBean;
import cn.chinat2t.stockgod.bean.ShouYiBean;
import cn.chinat2t.stockgod.bean.ShouYiRank;
import cn.chinat2t.stockgod.bean.UserBean;
import cn.chinat2t.stockgod.bean.UserDaoJu;
import cn.chinat2t.stockgod.http.CommunicationManager;
import cn.chinat2t.stockgod.http.CommunicationResultListener;
import cn.chinat2t.stockgod.utils.ViewUtil;

public class RankActivity extends Activity implements OnCheckedChangeListener {

	private static final int RANK_GAME_JINGJI = 1;
	private static final int RANK_GAME_MONI = 2;
	private static final int ADD_ATTENTION = 3;
	private static final int ADD_TRACE = 4;
	private static final int LING_QU = 5;
	
	private ListView listView;
	private RankAdapter adapter;
	private List<ShouYiBean> mCurrentList;
	private RadioGroup gameGroup;
	private RadioGroup shouyiGroup;
	private RadioButton gameJingji;
	private RadioButton gameMoni;
	private RadioButton shouyiAll;
	private RadioButton shouyiMonth;
	private RadioButton shouyiWeek;
	private RadioButton shouyiDay;
	private RadioButton onSelected;

	private UserBean userBean;
	private ShouYiRank rank;
	private int moniIndex = R.id.rank_shouyi_all;
	private int jingJiIndex = R.id.rank_shouyi_all;
	private int sortIndex = RANK_GAME_JINGJI;
	private int currentPage = 1;
	private Dialog showLoading;
	private TextView textShouYi;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.rank_layout);
		
		userBean = UserBean.getInstance();
		textShouYi = (TextView) findViewById(R.id.textView3);
		gameGroup = ((RadioGroup) this.findViewById(R.id.rank_game_radiogroup));
		gameGroup.setOnCheckedChangeListener(this);
		gameJingji = (RadioButton) findViewById(R.id.rank_jingji_game);
		gameMoni = (RadioButton) findViewById(R.id.rank_moni_game);

		shouyiGroup = ((RadioGroup) this.findViewById(R.id.rank_shouyi_radiogroup));
		shouyiGroup.setOnCheckedChangeListener(this);
		shouyiAll = (RadioButton) findViewById(R.id.rank_shouyi_all);
		shouyiMonth = (RadioButton) findViewById(R.id.rank_shouyi_month);
		shouyiWeek = (RadioButton) findViewById(R.id.rank_shouyi_week);
		shouyiDay = (RadioButton) findViewById(R.id.rank_shouyi_day);
		onSelected = shouyiAll;
		listView = (ListView) findViewById(R.id.rank_listView);
		adapter = new RankAdapter();

		onCheckedChanged(gameGroup, R.id.rank_jingji_game);
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		MainActivity.setTitleName("排行榜");
	};
	
	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		switch (checkedId) {
		case R.id.rank_jingji_game:
			sortIndex = RANK_GAME_JINGJI;
			shouyiAll.setText("总收益");
			shouyiMonth.setText("月收益");
			shouyiWeek.setText("周收益");
			shouyiDay.setText("日收益");
			onCheckedChanged(shouyiGroup, jingJiIndex);
			break;

		case R.id.rank_moni_game:
			sortIndex = RANK_GAME_MONI;
			shouyiAll.setText("总收益率");
			shouyiMonth.setText("月收益率");
			shouyiWeek.setText("周收益率");
			shouyiDay.setText("日收益率");
			onCheckedChanged(shouyiGroup, moniIndex);
			break;
		case R.id.rank_shouyi_all:
			if(onSelected != null)
			onSelected.setTextColor(getResources().getColor(R.color.white));
			onSelected.setBackgroundResource(R.drawable.main_tag_default);
			shouyiAll.setTextColor(getResources().getColor(R.color.black));
			shouyiAll.setBackgroundResource(R.drawable.main_tag_active);
			onSelected = shouyiAll;
			if(sortIndex == RANK_GAME_JINGJI){
				jingJiIndex = checkedId;
				textShouYi.setText("总收益");
			}else{
				textShouYi.setText("总收益率");
				moniIndex = checkedId;
			}
			getRankData(sortIndex, "total", currentPage);
			break;
		case R.id.rank_shouyi_month:
			if(onSelected != null)
			onSelected.setTextColor(getResources().getColor(R.color.white));
			onSelected.setBackgroundResource(R.drawable.main_tag_default);
			shouyiMonth.setTextColor(getResources().getColor(R.color.black));
			shouyiMonth.setBackgroundResource(R.drawable.main_tag_active);
			onSelected = shouyiMonth;
			if(sortIndex == RANK_GAME_JINGJI){
				jingJiIndex = checkedId;
				textShouYi.setText("月收益");
			}else{
				textShouYi.setText("月收益率");
				moniIndex = checkedId;
			}
			getRankData(sortIndex, "month", currentPage);
			break;
		case R.id.rank_shouyi_week:
			if(onSelected != null)
			onSelected.setTextColor(getResources().getColor(R.color.white));
			onSelected.setBackgroundResource(R.drawable.main_tag_default);
			shouyiWeek.setTextColor(getResources().getColor(R.color.black));
			shouyiWeek.setBackgroundResource(R.drawable.main_tag_active);
			onSelected = shouyiWeek;
			if(sortIndex == RANK_GAME_JINGJI){
				jingJiIndex = checkedId;
				textShouYi.setText("周收益");
			}else{
				textShouYi.setText("周收益率");
				moniIndex = checkedId;
			}
			getRankData(sortIndex, "week", currentPage);
			break;
		case R.id.rank_shouyi_day:
			if(onSelected != null)
			onSelected.setTextColor(getResources().getColor(R.color.white));
			onSelected.setBackgroundResource(R.drawable.main_tag_default);
			shouyiDay.setTextColor(getResources().getColor(R.color.black));
			shouyiDay.setBackgroundResource(R.drawable.main_tag_active);
			onSelected = shouyiDay;
			if(sortIndex == RANK_GAME_JINGJI){
				jingJiIndex = checkedId;
				textShouYi.setText("日收益");
			}else{
				textShouYi.setText("日收益率");
				moniIndex = checkedId;
			}
			getRankData(sortIndex, "day", currentPage);
			break;
		}
	}
	
	private void getRankData(int sort, String type, int page){
		showLoading = ViewUtil.showLoading(getParent());
		if(sort == RANK_GAME_JINGJI){
			CommunicationManager.getInstance().getJingJiStockRank(userBean.uid, type, page, shouyiJingJiListener);
		}else{
			CommunicationManager.getInstance().getMoniStockRank(userBean.uid, type, page, shouyiMoniListener);
		}
	}
	
	@Override
	public void onBackPressed() {
		CompetitiveGroup.getInstance().back();
	};
	
	Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {

			switch (msg.arg1) {
			case RANK_GAME_JINGJI:
				rank = (ShouYiRank) msg.obj;
				adapter.setData(rank.allSYList);
				listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				break;
			case RANK_GAME_MONI:
				rank = (ShouYiRank) msg.obj;
				adapter.setData(rank.allSYList);
				listView.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				break;
			case ADD_TRACE:
				ResponsedBean use = (ResponsedBean) msg.obj;
				if (use.responsecode == 10) {
					ShouYiBean shouYiBean = mCurrentList.get(msg.what);
					shouYiBean.trace = 1;
					adapter.setData(mCurrentList);
					listView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				} else {
					ViewUtil.showToast("追踪失败", RankActivity.this);
				}
				break;
			case ADD_ATTENTION:
				ResponsedBean user = (ResponsedBean) msg.obj;
				if (user.responsecode == 10) {
					ShouYiBean bean = mCurrentList.get(msg.what);
					bean.attention = 1;
					adapter.setData(mCurrentList);
					listView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				} else {
					ViewUtil.showToast("关注失败", RankActivity.this);
				}
				break;
			case LING_QU:
				ResponsedBean respon = (ResponsedBean) msg.obj;
				if(respon.responsecode == 10){
					ShouYiBean bean = mCurrentList.get(msg.what);
					bean.reward = 1;
					adapter.setData(mCurrentList);
					listView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
				} else {
					ViewUtil.showToast("领取奖励失败", RankActivity.this);
				}
				/*UserBean users = (UserBean) msg.obj;
				if (users.responsecode == 10) {
					ShouYiBean bean = mCurrentList.get(msg.what);
					bean.reward = 1;
					adapter.setData(mCurrentList);
					listView.setAdapter(adapter);
					adapter.notifyDataSetChanged();
					showRewardDialog();
				} else {
					ViewUtil.showToast("领取奖励失败", RankActivity.this);
				}*/
				break;
			}
			ViewUtil.dismiss(showLoading);
			return false;
		}
	});
	Dialog dialog;
	private void showRewardDialog(){
		if(rank == null) return;
		View view = LayoutInflater.from(this).inflate(R.layout.ling_reward_view, null);
		TextView tx1 = (TextView) view.findViewById(R.id.textView1);
		TextView tx2 = (TextView) view.findViewById(R.id.textView2);
		TextView tx3 = (TextView) view.findViewById(R.id.textView3);
		TextView tx4 = (TextView) view.findViewById(R.id.textView4);
		TextView tx5 = (TextView) view.findViewById(R.id.textView5);
		Button button = (Button) view.findViewById(R.id.guess_confirm);
		tx1.setText("经验：" + rank.expvalue);
		tx2.setText("金币：" + rank.gold);
		tx3.setText("资金：" + rank.fund);
		UserDaoJu d1 = rank.daoJu.get(0);
		UserDaoJu d2 = rank.daoJu.get(0);
		tx4.setText(d1.name + "X" + d1.num);
		tx5.setText(d2.name + "X" + d2.num);
		button.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				dialog.cancel();
				showLoading = ViewUtil.showLoading(getParent());
				String type = "";
				if(sortIndex == RANK_GAME_JINGJI){
					if(jingJiIndex == R.id.rank_shouyi_all){
						type = "total";
					}else if(jingJiIndex == R.id.rank_shouyi_month){
						type = "month";
					}else if(jingJiIndex == R.id.rank_shouyi_week){
						type = "week";
					}else if(jingJiIndex == R.id.rank_shouyi_day){
						type = "day";
					}
				}else if(sortIndex == RANK_GAME_MONI){
					if(moniIndex == R.id.rank_shouyi_all){
						type = "total";
					}else if(moniIndex == R.id.rank_shouyi_month){
						type = "month";
					}else if(moniIndex == R.id.rank_shouyi_week){
						type = "week";
					}else if(moniIndex == R.id.rank_shouyi_day){
						type = "day";
					}
				}
				CommunicationManager.getInstance().lingJiang(userBean.uid, sortIndex, type, new CommunicationResultListener() {
					public void resultListener(byte result, Object resultData) {
						if(result == CommunicationManager.SUCCEED && token > 0){
							Message message = handler.obtainMessage();
							message.arg1 = LING_QU;
							message.obj = resultData;
							message.what = Integer.parseInt(this.tag.toString());
							handler.sendMessage(message);
						}else{
							ViewUtil.dismiss(showLoading);
						}
					};
				});
			}
		});
		dialog = new Dialog(getParent(), R.style.selectorDialog);
		dialog.setContentView(view);
		dialog.show();
	}
	
	private CommunicationResultListener shouyiJingJiListener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED && token > 0){
				Message message = handler.obtainMessage();
				message.arg1 = RANK_GAME_JINGJI;
				message.obj = resultData;
				handler.sendMessage(message);
			}else{
				ViewUtil.dismiss(showLoading);
			}
		};
	};
	
	private CommunicationResultListener shouyiMoniListener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED && token > 0){
				Message message = handler.obtainMessage();
				message.arg1 = RANK_GAME_MONI;
				message.obj = resultData;
				handler.sendMessage(message);
			}else{
				ViewUtil.dismiss(showLoading);
//				ViewUtil.showToast((String)resultData, RankActivity.this);
			}
		};
	};
	
	class RankAdapter extends BaseAdapter {

		private List<ShouYiBean> mList = new ArrayList<ShouYiBean>();

		public void setData(List<ShouYiBean> mList) {
			if(mList != null){
				this.mList = mList;
				mCurrentList = mList;
			}
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
				arg1 = View.inflate(RankActivity.this, R.layout.rank_shouyi_list_item, null);
				holder = new DongTaiHolder();
				holder.linearLayout = (LinearLayout) arg1.findViewById(R.id.rank_shouyi_list_item);
				holder.tv1 = (TextView) arg1.findViewById(R.id.rank_shouyi_textView1);
				holder.tv2 = (TextView) arg1.findViewById(R.id.rank_shouyi_textView2);
				holder.tv3 = (TextView) arg1.findViewById(R.id.rank_shouyi_textView3);
				holder.btn1 = (Button) arg1.findViewById(R.id.rank_shouyi_zhui);
				holder.btn2 = (Button) arg1.findViewById(R.id.rank_shouyi_guanzhu);
				holder.btn3 = (Button) arg1.findViewById(R.id.rank_shouyi_jiang);
				arg1.setTag(holder);
			} else {
				holder = (DongTaiHolder) arg1.getTag();
			}
			final ShouYiBean shouYiBean = mList.get(arg0);
			if (arg0 % 2 == 1) {
				holder.linearLayout.setBackgroundColor(Color.parseColor("#fff4eddd"));
			} else {
				holder.linearLayout.setBackgroundColor(Color.parseColor("#ffeee2d1"));
			}
			holder.tv1.setText(shouYiBean.paiming + "");
			int lefeDrawable = R.drawable.rank_flat;
			if(shouYiBean.ranking == 1){
				lefeDrawable = R.drawable.rank_down;
			}else if(shouYiBean.ranking == 2){
				lefeDrawable = R.drawable.rank_flat;
			}else if(shouYiBean.ranking == 3){
				lefeDrawable = R.drawable.rank_up;
			}
			Drawable drawable = getResources().getDrawable(lefeDrawable);
			drawable.setBounds(0, 0, drawable.getMinimumWidth(), drawable.getMinimumHeight()); 
			holder.tv1.setCompoundDrawables(drawable , null, null, null);
			holder.tv2.setText(shouYiBean.nickname);
			if(sortIndex == RANK_GAME_JINGJI){
				holder.tv3.setText((int)shouYiBean.shouyi + "");
			}else{
				holder.tv3.setText(shouYiBean.shouyi + "%");
				holder.tv3.setCompoundDrawables(null, null, null, null);
			}
			if(shouYiBean.my == 1){ // 如果是自己
				holder.btn1.setVisibility(View.GONE);
				holder.btn2.setVisibility(View.GONE);
				holder.btn3.setVisibility(View.VISIBLE);
				if(shouYiBean.reward == 0){
					holder.btn3.setText("已领取奖励");
					holder.btn3.setTextColor(getResources().getColor(R.color.white));
					holder.btn3.setBackgroundResource(R.drawable.grey_btn);
					holder.btn3.setOnClickListener(null);
				}else{
					holder.btn3.setText("领取奖励");
					holder.btn3.setBackgroundResource(R.drawable.btn_yellow_selector);
					holder.btn3.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							showRewardDialog();
						}
					});
				}
			}else{
				holder.btn1.setVisibility(View.VISIBLE);
				holder.btn2.setVisibility(View.VISIBLE);
				holder.btn3.setVisibility(View.GONE);
			}
			if(shouYiBean.trace == 1) {
				holder.btn1.setText("已追踪");
				holder.btn1.setBackgroundResource(R.drawable.yellow_btn);
				holder.btn1.setOnClickListener(null);
			} else {
				holder.btn1.setText("追踪");
				holder.btn1.setBackgroundResource(R.drawable.btn_gold_selector);
				holder.btn1.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(userBean.usertype == UserBean.USER_LIN_SHI){
							ViewUtil.showLinShiDialog(getParent(), "临时用户不能操作，请先注册");
							return;
						}
						showLoading = ViewUtil.showLoading(getParent());
						CommunicationManager.getInstance().addFriend(userBean.uid, shouYiBean.uid, "trace", new CommunicationResultListener() {
							
							public void resultListener(byte result, Object resultData) {
								if(result == CommunicationManager.SUCCEED && token > 0){
									Message message = handler.obtainMessage();
									message.arg1 = ADD_TRACE;
									message.obj = resultData;
									message.what = Integer.parseInt(this.tag.toString());
									handler.sendMessage(message);
								}else{
									ViewUtil.dismiss(showLoading);
								}
							};
						});
					}
				});
			}
			if(shouYiBean.attention == 1)	{
				holder.btn2.setText("已关注");
				holder.btn2.setBackgroundResource(R.drawable.yellow_btn);
				holder.btn2.setOnClickListener(null);
			} else {
				holder.btn2.setText("关注");
				holder.btn2.setBackgroundResource(R.drawable.btn_gold_selector);
				holder.btn2.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						if(userBean.usertype == UserBean.USER_LIN_SHI){
							ViewUtil.showLinShiDialog(getParent(), "临时用户不能操作，请先注册");
							return;
						}
						showLoading = ViewUtil.showLoading(getParent());
						CommunicationManager.getInstance().addFriend(userBean.uid, shouYiBean.uid, "attention", new CommunicationResultListener() {
							
							public void resultListener(byte result, Object resultData) {
								if(result == CommunicationManager.SUCCEED && token > 0){
									Message message = handler.obtainMessage();
									message.arg1 = ADD_ATTENTION;
									message.obj = resultData;
									handler.sendMessage(message);
								}else{
									ViewUtil.dismiss(showLoading);
								}
							};
						});
					}
				});
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
		public Button btn1;
		public Button btn2;
		public Button btn3;
	}

	@Override
	public void onContentChanged() {
		MainActivity.setTitleName("排行榜");
	}
}
