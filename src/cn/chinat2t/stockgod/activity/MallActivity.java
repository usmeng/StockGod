package cn.chinat2t.stockgod.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.RelativeLayout;
import android.widget.TextView;
import cn.chinat2t.stockgod.MainActivity;
import cn.chinat2t.stockgod.R;
import cn.chinat2t.stockgod.bean.ResponsedBean;
import cn.chinat2t.stockgod.bean.UserBean;
import cn.chinat2t.stockgod.bean.UserDaoJu;
import cn.chinat2t.stockgod.http.CommunicationManager;
import cn.chinat2t.stockgod.http.CommunicationResultListener;
import cn.chinat2t.stockgod.utils.ViewUtil;
/**
 * 商城界面
 * @author Administrator
 *
 */
public class MallActivity extends Activity implements OnCheckedChangeListener{
	
	public final static String UPDATE_MALL_INFOR = "cn.chinat2t.stockgod.update_mall_infor";
	
	private RadioGroup radioGroup;
	private LinearLayout content;
	
	public static final int MALL_DAOJU = 0;
	public static final int MALL_VIP = 1;
	public static final int MALL_DUIHUAN = 2;
	public static final int MALL_CHONGZHI = 3;

	public static final int GET_DAOJU_LIST = 0;
	public static final int GET_VIP_LIST = 1;
	public static final int GET_DUIHUAN_LIST = 2;
	public static final int DUI_HUAN = 3;

	private View daoJuView;
	private View vipView;
	private View duiHuanView;
	private ListView daoJulistView;	
	private ListView vipListView;
	private ListView dHListView;
	private DJListItemAdapter mAdapter;
	private Dialog showLoading;

	private int currentPage;
	private int flag;
	public int type;
	public boolean isSecondView;

	private Map<Integer, Bitmap> mapList = new HashMap<Integer, Bitmap>();
	private UserBean userBean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.mall_layout);
		flag = getIntent().getIntExtra("flag", 0);
		
		userBean = UserBean.getInstance();
		initView();
		if(flag == 1){
			((RadioButton)findViewById(R.id.radiobtn4)).setChecked(true);
			onCheckedChanged(radioGroup, R.id.radiobtn4);
		} else if(flag == 2){
			((RadioButton)findViewById(R.id.radiobtn3)).setChecked(true);
			onCheckedChanged(radioGroup, R.id.radiobtn3);
		}else{
			((RadioButton)findViewById(R.id.radiobtn1)).setChecked(true);
			onCheckedChanged(radioGroup, R.id.radiobtn1);
		}
		
		IntentFilter filter = new IntentFilter(UPDATE_MALL_INFOR);
		registerReceiver(broadcastReceiver, filter);
	}
	
	public BroadcastReceiver broadcastReceiver = new BroadcastReceiver() {
		
		@Override
		public void onReceive(Context context, Intent intent) {
			if(UPDATE_MALL_INFOR.equals(intent.getAction())){
				int type2 = intent.getIntExtra("ACTION_TYPE", 0);
				switch (type2) {
				case MALL_DAOJU:
					((RadioButton)findViewById(R.id.radiobtn1)).setChecked(true);
					createDJView(type2 == type);
					break;

				case MALL_CHONGZHI:
					((RadioButton)findViewById(R.id.radiobtn4)).setChecked(true);
					createChZView();
					break;
					
				case MALL_DUIHUAN:
					((RadioButton)findViewById(R.id.radiobtn3)).setChecked(true);
					createDhView(type2 == type);
					break;
					
				case MALL_VIP:
					((RadioButton)findViewById(R.id.radiobtn2)).setChecked(true);
					createVipView(type2 == type);
					break;
				}
			}
			isSecondView = false;
		}
	};

	@Override
	protected void onResume() {
		super.onResume();
		MainActivity.setTitleName("商城");
	};
	
	public void initView(){
		radioGroup = (RadioGroup)findViewById(R.id.mall_tabs_radiogroup);
		radioGroup.setOnCheckedChangeListener(this);
		content = (LinearLayout)findViewById(R.id.content);
		

		daoJuView = View.inflate(this, R.layout.daoju_list, null);
		daoJulistView = (ListView)daoJuView.findViewById(R.id.daoju_list);

		vipView = View.inflate(this, R.layout.daoju_list, null);
		vipListView = (ListView)vipView.findViewById(R.id.daoju_list);
		
		duiHuanView = View.inflate(this, R.layout.daoju_list, null);
		dHListView = (ListView)duiHuanView.findViewById(R.id.daoju_list);
	}

	@Override
	public void onCheckedChanged(RadioGroup group, int checkedId) {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(radioGroup.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
		switch(checkedId){
		case R.id.radiobtn1:
			createDJView(false);
			break;
		case R.id.radiobtn2:
			createVipView(false);
			break;
		case R.id.radiobtn3:
			createDhView(false);
			break;
		case R.id.radiobtn4:
			createChZView();
			break;
		}
	}
	
	Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.arg1) {
			case GET_DAOJU_LIST:
				//创建道具卡列表
				ArrayList<UserDaoJu> djs = (ArrayList<UserDaoJu>) msg.obj;
				mAdapter = new DJListItemAdapter(MallActivity.this, djs, MALL_DAOJU);
				daoJulistView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				break;
			case GET_VIP_LIST:
				ArrayList<UserDaoJu> vips = (ArrayList<UserDaoJu>) msg.obj;
				mAdapter = new DJListItemAdapter(MallActivity.this, vips, MALL_VIP);
				vipListView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				break;
			case GET_DUIHUAN_LIST:
				ArrayList<UserDaoJu> duis = (ArrayList<UserDaoJu>) msg.obj;
				mAdapter = new DJListItemAdapter(MallActivity.this, duis, MALL_DUIHUAN);
				dHListView.setAdapter(mAdapter);
				mAdapter.notifyDataSetChanged();
				break;
			case DUI_HUAN:
				ResponsedBean bean = (ResponsedBean) msg.obj;
				ViewUtil.showDialog(getParent(), bean.msg, null);
				if(bean.responsecode == 10){
					/*Intent intent = new Intent(MallActivity.UPDATE_MALL_INFOR);
					intent.putExtra("ACTION_TYPE", MallActivity.MALL_DUIHUAN);
					sendBroadcast(intent);*/
					sendBroadcast(new Intent(MainActivity.UPDATE_USER_INFOR));
				}
				break;
			}
			ViewUtil.dismiss(showLoading);
			return false;
		}
	});
	
	@Override
	public void onBackPressed() {
		if(!isSecondView){
			CompetitiveGroup.getInstance().back();
			isSecondView = false;
			unregisterReceiver(broadcastReceiver);
		}else{
			Intent intent = new Intent(UPDATE_MALL_INFOR);
			intent.putExtra("ACTION_TYPE", type);
			sendBroadcast(intent);
		}
	}
	
	/**
	 * 创建道具View
	 */
	private void createDJView(boolean getDataAgain){
		if(content == null) return;
		content.removeAllViews();
		content.addView(daoJuView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		isSecondView = false;
		if(getDataAgain) return;
		showLoading = ViewUtil.showLoading(getParent());
		CommunicationManager.getInstance().getDaojuList(currentPage, 0, new CommunicationResultListener() {
			@Override
			public void resultListener(byte result, Object resultData) {
				if(result == CommunicationManager.SUCCEED){
					Message message = handler.obtainMessage();
					message.arg1 = GET_DAOJU_LIST;
					message.obj = resultData;
					handler.sendMessage(message);
				}else{
					ViewUtil.dismiss(showLoading);
				}
			}
		});
	}
	
	/**
	 * 创建充值View
	 */
	private void createChZView(){
		if(content == null) return;
		content.removeAllViews();
		View view = new ChongZhiView(this, flag, MALL_CHONGZHI);
		content.addView(view, new LinearLayout.LayoutParams(-1, -1));
		isSecondView = false;
	}
	
	private void createVipView(boolean getDataAgain){
		if(content == null) return;
		content.removeAllViews();
		content.addView(vipView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		isSecondView = false;
		if(getDataAgain)return;
		showLoading = ViewUtil.showLoading(getParent());
		CommunicationManager.getInstance().getDaojuList(currentPage, 1, new CommunicationResultListener() {
			@Override
			public void resultListener(byte result, Object resultData) {
				if(result == CommunicationManager.SUCCEED){
					Message message = handler.obtainMessage();
					message.arg1 = GET_VIP_LIST;
					message.obj = resultData;
					handler.sendMessage(message);
				}else{
					ViewUtil.dismiss(showLoading);
				}
			}
		});
		
	}
	
	private void createDhView(boolean getDataAgain){
		if(content == null) return;
		content.removeAllViews();
		content.addView(duiHuanView, new LayoutParams(LayoutParams.FILL_PARENT,LayoutParams.FILL_PARENT));
		isSecondView = false;
		if(getDataAgain) return;
		//获取兑换列表
		showLoading = ViewUtil.showLoading(getParent());
		CommunicationManager.getInstance().getDuiHuanList(userBean.uid, new CommunicationResultListener() {

			@Override
			public void resultListener(byte result, Object resultData) {
				if(result == CommunicationManager.SUCCEED){
					Message message = handler.obtainMessage();
					message.arg1 = GET_DUIHUAN_LIST;
					message.obj = resultData;
					handler.sendMessage(message);
				}else{
					ViewUtil.dismiss(showLoading);
				}
			}
		
		});
	}

	class ImageAsyncTask extends AsyncTask<String, Integer, Bitmap>{
		
		private View image;
		private int position;

		public ImageAsyncTask(int position, View image) {
			this.position = position;
			this.image = image;
		}
		
		@Override
		protected Bitmap doInBackground(String... params) {
			return ViewUtil.downloadBitmap(params[0]);
		}
		
		@Override
		protected void onPostExecute(Bitmap result) {
			super.onPostExecute(result);
			mapList.put(position, result);
			if(image != null){
				image.setBackgroundDrawable(new BitmapDrawable(result));
			}
		}
	}
	
	public class DJListItemAdapter extends BaseAdapter {
		ArrayList<UserDaoJu> djs = new ArrayList<UserDaoJu>();
		Context con;
		
		public DJListItemAdapter(Context con, ArrayList<UserDaoJu> djs, int typeValue){
			this.con = con;
			this.djs = djs;
			type = typeValue;
			mapList.clear();
		}
		
		public void setValues(ArrayList<UserDaoJu> djs){
			this.djs = djs;
		}
		
		@Override
		public int getCount() {
			return djs.size();
		}

		@Override
		public Object getItem(int position) {
			return djs.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = null;
			if(convertView == null){
				convertView = View.inflate(con, R.layout.daoju_list_item, null);
				holder = new ViewHolder();
				holder.image = (RelativeLayout) convertView.findViewById(R.id.image);
				holder.mark = (ImageView) convertView.findViewById(R.id.mark);
				holder.cardName = (TextView) convertView.findViewById(R.id.card_name);
				holder.cardDesc = (TextView) convertView.findViewById(R.id.card_desc);
				holder.price = (TextView) convertView.findViewById(R.id.price_tv);
				holder.jiangQuan = (TextView) convertView.findViewById(R.id.jiang_quan_tv);
				holder.button = (Button) convertView.findViewById(R.id.mall_daoju_action);
				holder.view = convertView.findViewById(R.id.linearLayout2);
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			if(position != 0){
				if(position % 2 == 0) convertView.setBackgroundColor(getResources().getColor(R.color.itemcolor0));
				else convertView.setBackgroundColor(getResources().getColor(R.color.itemcolor1));
			}
			final UserDaoJu djBean = djs.get(position);
			holder.cardName.setText(djBean.name);    // 名称
			holder.cardDesc.setText(djBean.instruction);  //  描述
			if(mapList.containsKey(position)){
//				holder.image.setBackgroundResource(djBean.bitmapRes==0?R.drawable.djk:djBean.bitmapRes);  // 图片
				holder.image.setBackgroundDrawable(new BitmapDrawable(mapList.get(position)));
			}else{
				new ImageAsyncTask(position, holder.image).execute(djBean.icon);
			}
			if(djBean.isHot == 1){                                // 是否热卖
				holder.mark.setVisibility(View.VISIBLE);
			}else{
				holder.mark.setVisibility(View.INVISIBLE);
			}
			switch (type) {
			case MALL_DAOJU:
				holder.button.setText("购买");
				holder.price.setText((int)djBean.price+"");
				holder.price.setTextColor(Color.RED);
				holder.cardDesc.setVisibility(View.VISIBLE);
				holder.jiangQuan.setText("金币/张");
				break;
			case MALL_VIP:
				holder.button.setText("购买");
				holder.price.setText((int)djBean.price+"");
				holder.price.setTextColor(Color.RED);
				holder.cardDesc.setVisibility(View.VISIBLE);
				holder.jiangQuan.setText("金币/张");
				break;
			case MALL_DUIHUAN:
				holder.button.setText("兑换");
				holder.cardDesc.setVisibility(View.GONE);
				holder.price.setTextColor(Color.GRAY);
				holder.price.setText("所需奖券：");
				holder.jiangQuan.setText("" + (int)djBean.lottery);
				break;
			}
			holder.button.setOnClickListener(new OnClickListener() {
				
			@Override
			public void onClick(View v) {
				if(userBean.usertype == UserBean.USER_LIN_SHI){
					ViewUtil.showLinShiDialog(getParent(), "临时用户不能操作，请先注册");
					return;
				}
				switch (type) {
				case MALL_DAOJU:
					if(content == null) return;
					content.removeAllViews();
					View view2 = new BuyDaoJustView(MallActivity.this, djBean, type, mapList.get(position));
					content.addView(view2, new LinearLayout.LayoutParams(-1, -1));
					break;
				case MALL_DUIHUAN:
					if(djBean.type == 2){
						showDialog(djBean);
					}else{
						if(content == null) return;
							content.removeAllViews();
						View view3 = new DuiHuanView(MallActivity.this, djBean);
						content.addView(view3, new LinearLayout.LayoutParams(-1, -1));
					}
					break;
				case MALL_VIP:
					if(content == null) return;
					content.removeAllViews();
					View view4 = new BuyDaoJustView(MallActivity.this, djBean, type, mapList.get(position));
					content.addView(view4, new LinearLayout.LayoutParams(-1, -1));
					break;
				}
				isSecondView = true;
			}
		});
			return convertView;
		}
		
		class ViewHolder{
			RelativeLayout image;
			ImageView mark;
			TextView cardName;
			TextView cardDesc;
			TextView price;
			TextView jiangQuan;
			Button button;
			View view;
		}
		
	}
	
	public void showDialog(final UserDaoJu userDaoju){
		View view = LayoutInflater.from(this).inflate(R.layout.guess_dialog_munu, null);
		TextView tx0 = (TextView) view.findViewById(R.id.textView1);
		TextView tx1 = (TextView) view.findViewById(R.id.textView2);
		TextView tx2 = (TextView) view.findViewById(R.id.textView3);
		Button button = (Button) view.findViewById(R.id.guess_confirm);
		Button chongzhi = (Button) view.findViewById(R.id.guess_chongzhi);
		
		final Dialog showDialog = ViewUtil.showDialog(getParent(), view);
		chongzhi.setVisibility(View.VISIBLE);
		chongzhi.setText("取消");
		button.setText("确定");
		tx0.setText("我们将把" + userDaoju.name + "的电子券以邮件形式发送到您绑定的邮箱中：");
		tx1.setText(userBean.email);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog.cancel();
				showLoading = ViewUtil.showLoading(getParent());
				CommunicationManager.getInstance().mallChange(UserBean.getInstance().uid, 
						userDaoju.id, userDaoju.type, userBean.email, changeListener);
			}
		});
		chongzhi.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				showDialog.cancel();
			}
		});
	}
	
	private CommunicationResultListener changeListener = new CommunicationResultListener() {
		@Override
		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED){
				Message message = handler.obtainMessage();
				message.arg1 = DUI_HUAN;
				message.obj = resultData;
				handler.sendMessage(message);
			}else{
				ViewUtil.dismiss(showLoading);
			}
		};
	};
	
	@Override
	public void onContentChanged() {
		MainActivity.setTitleName("商城");
	}
	
	@Override
	public void onDetachedFromWindow() {
		InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(radioGroup.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
