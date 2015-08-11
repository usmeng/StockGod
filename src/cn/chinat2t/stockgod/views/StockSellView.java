package cn.chinat2t.stockgod.views;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import cn.chinat2t.stockgod.MainActivity;
import cn.chinat2t.stockgod.R;
import cn.chinat2t.stockgod.activity.CompetitiveGroup;
import cn.chinat2t.stockgod.activity.SimulationActivity;
import cn.chinat2t.stockgod.activity.SimulationSubActivity;
import cn.chinat2t.stockgod.activity.SimulationSubActivity.ViewBackPressedListener;
import cn.chinat2t.stockgod.bean.SellBuyBean;
import cn.chinat2t.stockgod.bean.StockKlineBean;
import cn.chinat2t.stockgod.bean.UserBean;
import cn.chinat2t.stockgod.http.CommunicationManager;
import cn.chinat2t.stockgod.http.CommunicationResultListener;
import cn.chinat2t.stockgod.utils.ViewUtil;

public class StockSellView extends LinearLayout implements OnClickListener, TextWatcher {

	public static final int STOCK_LIST = 2;
	private LayoutInflater mInflater;
	private ListView sellList;
	private ListView buyList;
	private Button button;
	private EditText priceEdit;
	private EditText amountEdit;
	private View stockSellView;
	private PopupWindow menu;
	private SimulationSubActivity activity;
	private StockAdapter sellAdapter;
	private StockAdapter buyAdapter;
	private StockKlineBean stock;
	private int type;
	private TextView zuidagu;
	private TextView yichigu;
	private UserBean userBean;
	private boolean isNotEdit;
	private boolean isEdit;
	public  static boolean isSelling = false;
	private ProgressDialog showLoading;
	
	public StockSellView(Context context) {
		super(context);
	}
	
	public StockSellView(Context context, StockKlineBean stock, int type) {
		super(context);
		this.stock = stock;
		this.type = type;
		activity = (SimulationSubActivity) context;
		userBean = UserBean.getInstance();
		
		mInflater = LayoutInflater.from(activity);
		stockSellView = mInflater.inflate(R.layout.stock_buy_layout, null);
		addView(stockSellView);
		
		button = (Button) stockSellView.findViewById(R.id.btn_stock_buy);
		button.setOnClickListener(this);
		
		if(type == SellBuyBean.STOCK_BUY){
			button.setText("买入");
		}else{
			button.setText("卖出");
		}
		
		priceEdit = (EditText) stockSellView.findViewById(R.id.editText_price);
		amountEdit = (EditText) stockSellView.findViewById(R.id.editText_amount);
		amountEdit.setOnFocusChangeListener(new OnFocusChangeListener() {
			
			@Override
			public void onFocusChange(View v, boolean hasFocus) {
				if(!hasFocus){
					isEdit = false;
					amountEdit.setFocusable(false);
					amountEdit.setFocusableInTouchMode(false);
				}else{
					new Handler().postDelayed(new Runnable() {
						
						@Override
						public void run() {
							InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
							imm.showSoftInput(amountEdit, 0);
						}
					}, 500);
				}
			}
		});
		yichigu = (TextView) stockSellView.findViewById(R.id.yi_chi_gu);
		zuidagu = (TextView) stockSellView.findViewById(R.id.max_buy);
		priceEdit.setOnClickListener(this);
		amountEdit.setOnClickListener(this);
		amountEdit.setFocusable(false);

		sellList = (ListView)stockSellView.findViewById(R.id.listView_buy);
		buyList = (ListView)stockSellView.findViewById(R.id.listView_sell);
		
		updateSellBuy(stock);
		
		activity.setOnViewBackPressedListener(pressedListener);
		isSelling = false;
	}

	public void updateSellBuy(StockKlineBean stock) {
		if(stock == null) return;
		this.stock = stock;
		if(stock.sellList.size() >= 10){
			sellAdapter = new StockAdapter(stock.sellList.subList(0, 5));
			buyAdapter = new StockAdapter(revserList(stock.sellList.subList(5, 10)));
			sellList.setAdapter(sellAdapter);
			buyList.setAdapter(buyAdapter);

			yichigu.setText("已持该股：" + stock.member);
			if(type == SellBuyBean.STOCK_BUY){
				if(!isNotEdit){
					priceEdit.setText("" + stock.nowprice);
					amountEdit.setText("" + stock.maxstock);
				}
				zuidagu.setText("最大可买：" + stock.maxstock);
			}else{
				if(!isNotEdit){
					priceEdit.setText("" + stock.nowprice);
					amountEdit.setText("" + stock.member);
				}
				zuidagu.setText("最大可卖：" + stock.member);
				if(stock.member == 0){
					button.setEnabled(false);
				}else{
					if(!isSelling){
						button.setEnabled(true);
					}
				}
			}
		}
		ViewUtil.dismiss();
	}
	
	private List<SellBuyBean> revserList(List<SellBuyBean> list) {
		List<SellBuyBean> newList = new ArrayList<SellBuyBean>();
		if(list == null) return newList;
		for (int i = list.size() - 1; i >= 0; i--) {
			newList.add(list.get(i));
		}
		return newList;
	}

	private CommunicationResultListener sellListener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			switch (result) {
			case CommunicationManager.FAIL:
				ViewUtil.dismiss(showLoading);
				button.setEnabled(true);
				button.setClickable(true);
				isSelling = false;
				break;
				
			case CommunicationManager.SUCCEED:
				if(token > 0){
					Message msg = mHandler.obtainMessage();
					msg.what = result;
					msg.arg1 = SellBuyBean.STOCK_SELL;
					msg.obj = resultData;
					mHandler.sendMessage(msg);
				}
				break;
			}
		};
	};
	
	private CommunicationResultListener buyListener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			switch (result) {
			case CommunicationManager.FAIL:
				ViewUtil.dismiss(showLoading);
				button.setEnabled(true);
				button.setClickable(true);
				break;
				
			case CommunicationManager.SUCCEED:
				if(token > 0){
					Message msg = mHandler.obtainMessage();
					msg.what = result;
					msg.arg1 = SellBuyBean.STOCK_BUY;
					msg.obj = resultData;
					mHandler.sendMessage(msg);
				}
				break;
			}
		};
	};

	ViewBackPressedListener pressedListener = new ViewBackPressedListener() {
		
		@Override
		public void dismiss() {
			if(menu != null)
			menu.dismiss();
		}

		@Override
		public boolean isShow() {
			if(menu != null)
				return menu.isShowing();
			return false;
		}
	};
	
	Handler mHandler = new Handler(){

		public void handleMessage(Message msg) {
			showDialog(activity.getParent(), (String) msg.obj, null);
			isNotEdit = false;
			isEdit = false;
			isSelling = true;
			button.setEnabled(true);
			button.setClickable(true);
			amountEdit.setFocusable(false);
			amountEdit.setFocusableInTouchMode(false);
			getContext().sendBroadcast(new Intent(MainActivity.UPDATE_USER_INFOR));
			ViewUtil.dismiss(showLoading);
		};
	};
	
	void showDialog(Activity act, String title, String message){
		View view = LayoutInflater.from(act).inflate(R.layout.guess_dialog_munu, null);
		TextView tx0 = (TextView) view.findViewById(R.id.textView1);
		TextView tx1 = (TextView) view.findViewById(R.id.textView2);
		TextView tx2 = (TextView) view.findViewById(R.id.textView3);
		Button button = (Button) view.findViewById(R.id.guess_confirm);
		Button chongzhi = (Button) view.findViewById(R.id.guess_chongzhi);
		
		final Dialog showDialog = ViewUtil.showDialog(act, view);
		chongzhi.setVisibility(View.GONE);
		button.setText("确定");
		tx0.setText(title);
		if(message != null){
			tx2.setText(message);
		}else{
			tx2.setVisibility(View.GONE);
		}
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				showDialog.cancel();
				activity.onBackPressed();
				CompetitiveGroup.getInstance().back();
				Intent intent = new Intent(activity, SimulationActivity.class);
				intent.putExtra("flag", 1);
				CompetitiveGroup.getInstance().switchActivity("SimulationActivity", intent , -1, -1);
			}
		});
	}
	
//	private double costMoney;
	@Override
	public void onClick(View v) {
		if(stock == null  || userBean == null) return;
		switch (v.getId()) {
		case R.id.btn_stock_buy:
			if(userBean.usertype == UserBean.USER_LIN_SHI){
				ViewUtil.showLinShiDialog(activity.getParent(), "您是临时用户,请注册");
				return;
			}
			String string = amountEdit.getText().toString();
			String priceStr = priceEdit.getText().toString();
			if(priceStr.length() == 0){
				ViewUtil.showToast("价格不能为空！", activity);
				return;
			}
			if(string.length() == 0 ){
				ViewUtil.showToast("数量不能为空！", activity);
				return;
			}
			if(string.length() > 9){
				ViewUtil.showToast("买入数量不能超过最大可买数", activity);
				return;
			}
			if(priceStr.length() > 9){
				ViewUtil.showToast("价格太高啦~", activity);
				return;
			}
			int amount = Integer.parseInt(string);
			double price = Double.parseDouble(priceStr);
			if(amount <= 0 ){
				ViewUtil.showToast("数量至少为1股", activity);
				return;
			}
			if(type == SellBuyBean.STOCK_BUY){
				if(amount > stock.maxstock){
					ViewUtil.showToast("买入数量不能超过最大可买数" + stock.maxstock, activity);
					return;
				}
				showLoading = (ProgressDialog) ViewUtil.showNewLoading(activity.getParent());
				showLoading.setCancelable(false);
				showLoading.setMessage("正在交易，请稍后...");
				button.setEnabled(false);
				button.setClickable(false);
				CommunicationManager.getInstance().buyAccountStock(userBean.uid, 
						stock.stklabel, amount, price, stock.nowprice, buyListener);
			}else{
				if(amount > stock.member){
					ViewUtil.showToast("卖出数量不能超过已持数量" + stock.member, activity);
					return;
				}
				button.setEnabled(false);
				button.setClickable(false);
				isSelling = true;
				showLoading = (ProgressDialog) ViewUtil.showNewLoading(activity.getParent());
				showLoading.setCancelable(false);
				showLoading.setMessage("正在交易，请稍后...");
				CommunicationManager.getInstance().sellAccountStock(userBean.uid, stock.stklabel, 
						amount, Double.parseDouble(priceStr), stock.nowprice, sellListener);
			}
			InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(button.getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
			break;
		case R.id.editText_price:
			isNotEdit = true;
			priceEdit.setFocusable(true);
			priceEdit.requestFocus();
			break;
		case R.id.editText_amount:
			if(isEdit) return;
			isNotEdit = true;
			menu = new PopupWindow(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
			LinearLayout contentView = (LinearLayout) mInflater.inflate(R.layout.window_menu_layout, null);
			for (int i = 0; i < 6; i++) {
				TextView text = new TextView(getContext());
				String textValue = "";
				if(i == 0){
					textValue = "自主输入";
				}else{
					if(type == SellBuyBean.STOCK_BUY){
						textValue = "买入" + "1/" + (i * 2);
					} else {
						textValue = "卖出" + "1/" + (i * 2);
					}
				}
				text.setTag(i);
				text.setText(textValue);
				text.setTextSize(18);
				text.setGravity(Gravity.CENTER);
				text.setTextColor(getResources().getColor(R.color.white));
				LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
				params.bottomMargin = 5;
				params.topMargin = 5;
				params.rightMargin = 50;
				params.leftMargin = 50;
				amountEdit.setSelected(true);
				text.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						TextView text = (TextView) v;
						int position = (Integer) text.getTag();
						if(position == 0){
							isEdit = true;
							priceEdit.clearFocus();
							amountEdit.setFocusable(true);
							amountEdit.setFocusableInTouchMode(true);
							amountEdit.requestFocus();
							amountEdit.setSelected(true);
						}else{
							amountEdit.setFocusable(false);
							amountEdit.setFocusableInTouchMode(false);
							if(type == SellBuyBean.STOCK_BUY){
								amountEdit.setText(stock.maxstock / (position * 2) + "");
							}else{
								amountEdit.setText(stock.member / (position * 2) + "");
							}
						}
						menu.dismiss();
					}
				});
				text.setLayoutParams(params);
				text.setPadding(20, 2, 20, 2);
				text.setMinWidth(200);
				text.setBackgroundResource(R.drawable.menu_item);
				contentView.addView(text);
			}
			menu.setContentView(contentView);
			menu.setBackgroundDrawable(new BitmapDrawable());
			menu.setOutsideTouchable(true);
			int[] loaction = new int[2];
			amountEdit.getLocationInWindow(loaction);
			DisplayMetrics dm = new DisplayMetrics();   
	        ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(dm); 
			menu.showAtLocation(amountEdit, Gravity.CENTER | Gravity.BOTTOM, 0, dm.heightPixels - loaction[1]);
			break;
		}
	}

	class StockAdapter extends BaseAdapter{
		
        List<SellBuyBean> mList;
        
        public void setmList(List<SellBuyBean> mList) {
			this.mList = mList;
		}

		public StockAdapter(List<SellBuyBean> mList) {
        	this.mList = mList;
		}
        
		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			StockHolder holder = null;
			if(convertView == null){
				convertView = mInflater.inflate(R.layout.sellbuy_listitem_layout, null);
				holder = new StockHolder();
				holder.tv1 = (TextView) convertView.findViewById(R.id.listitem_text1);
				holder.tv2 = (TextView) convertView.findViewById(R.id.listitem_text2);
				holder.tv3 = (TextView) convertView.findViewById(R.id.listitem_text3);
				holder.tv4 = (TextView) convertView.findViewById(R.id.listitem_text4);
				convertView.setTag(holder);
			} else {
				holder = (StockHolder) convertView.getTag();
			}

			SellBuyBean zijinBean = mList.get(position);
			holder.tv1.setText((zijinBean.flag == SellBuyBean.STOCK_BUY)?"买":"卖");
			holder.tv1.setTextColor(getResources().getColor(R.color.red));
			if(zijinBean.flag == SellBuyBean.STOCK_BUY){
				holder.tv2.setText((6 - zijinBean.num) + "");
			}else{
				holder.tv2.setText(zijinBean.num + "");
			}
			holder.tv3.setText(String.valueOf(zijinBean.pricesell));
			if(zijinBean.pricesell > stock.closes){
				holder.tv3.setTextColor(Color.RED);
			}else if(zijinBean.pricesell < stock.closes){
				holder.tv3.setTextColor(Color.GREEN);
			}else{
				holder.tv3.setTextColor(Color.WHITE);
			}
			holder.tv4.setText(String.valueOf(zijinBean.volsell));
			return convertView;
		}
		
	}
	
	class StockHolder{
		TextView tv1;
		TextView tv2;
		TextView tv3;
		TextView tv4;
		TextView tv5;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public void afterTextChanged(Editable s) {
		
	}

	@Override
	public void beforeTextChanged(CharSequence s, int start, int count,
			int after) {
		
	}

	@Override
	public void onTextChanged(CharSequence s, int start, int before, int count) {
		
	}
}
