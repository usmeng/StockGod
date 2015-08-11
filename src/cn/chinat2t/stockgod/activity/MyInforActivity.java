package cn.chinat2t.stockgod.activity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.TextView;
import cn.chinat2t.stockgod.MainActivity;
import cn.chinat2t.stockgod.R;
import cn.chinat2t.stockgod.bean.AccountStockBean;
import cn.chinat2t.stockgod.bean.ChengJiuBean;
import cn.chinat2t.stockgod.bean.ImageBean;
import cn.chinat2t.stockgod.bean.ImageListBean;
import cn.chinat2t.stockgod.bean.ResponsedBean;
import cn.chinat2t.stockgod.bean.SaiGuoBean;
import cn.chinat2t.stockgod.bean.SaiguoXiangBean;
import cn.chinat2t.stockgod.bean.UserBean;
import cn.chinat2t.stockgod.bean.UserDaoJu;
import cn.chinat2t.stockgod.http.CommunicationManager;
import cn.chinat2t.stockgod.http.CommunicationResultListener;
import cn.chinat2t.stockgod.utils.Constant;
import cn.chinat2t.stockgod.utils.DataUtil;
import cn.chinat2t.stockgod.utils.PreferenceManager;
import cn.chinat2t.stockgod.utils.StringUtil;
import cn.chinat2t.stockgod.utils.ViewUtil;

public class MyInforActivity extends Activity implements OnClickListener{
	
	public static final int LIST_INFOR = 0;
	public static final int LIST_CAIFU = 1;
	public static final int LIST_SAIGUO = 2;
	public static final int LIST_SETTING = 3;
	public static final int LIST_DAOJU = 4;
	public static final int EDIT_NAME = 5;
	public static final int EDIT_PASS = 6;
	public static final int FIND_PASS = 7;
	public static final int EDIT_EMAIL = 8;
	public static final int BINL_PHONE = 9;
	public static final int BINL_XINLANG = 10;
	public static final int BINL_WEIXIN = 11;

	public static final int USER_INFO = 12;
	public static final int GET_IAMGE_LIST = 13;
	public static final int SAI_GUO_LIST = 14;
	public static final int DELE_SAI_GUO = 15;
	public static final int ACCOUNT_OF_STOCKS = 16;
	public static final int CHANGE_IAMGE = 17;
	
	private RadioButton button1;
	private RadioButton button2;
	private RadioButton button3;
	private RadioButton button4;
	private RadioButton button5;
	private LinearLayout contentLayout;
	private View view;
	private LayoutInflater inflater;
	private ListView caiFuList;
	private ListView saiGuoList;
	private StockAdapter adapter;
	private SaiGuoAdapter sAdapter;
	private DaoJuAdapter dAdapter;
	private ChengJiuAdapter cAdapter;
	private ListView daojuList;
	private ListView imageList;
	private ImageAdapter iAdapter;
	private List<UserDaoJu> djList;
	private List<ChengJiuBean> cList;
	private UserBean userBean;
	private Dialog showLoading;
	private boolean isSecondView;
	private InfoBeanHolder infoBeanHolder;
	private SaiguoXiangBean bean;
	private String titleName = "我的信息";
	private TextView winTime;
	private TextView loserTime;
	private TextView winLV;
	private Dialog showLoading2;
	private int secondViewType;
	private Button confirmBtn2;
	
	public class InfoBeanHolder{
		private ListView chengJiuList;
		private TextView account;
		private TextView nickname;
		private TextView level;
		private TextView experience;
		private TextView neddEx;
		private TextView vip;
		private TextView jiasu;
		private TextView youxiaoqi;
		private TextView vipyouxiaoqi;
		private ImageView image;

		private TextView zijin;
		private TextView jinbing;
		private TextView jiangquan;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.myinfo_layout);
		
		inflater = LayoutInflater.from(this);
		userBean = UserBean.getInstance();
		
		button1 = (RadioButton) findViewById(R.id.myinfo_radio_button0);
		button2 = (RadioButton) findViewById(R.id.myinfo_radio_button1);
		button3 = (RadioButton) findViewById(R.id.myinfo_radio_button2);
		button4 = (RadioButton) findViewById(R.id.myinfo_radio_button3);
		button5 = (RadioButton) findViewById(R.id.myinfo_radio_button4);
		
		button1.setOnClickListener(this);
		button2.setOnClickListener(this);
		button3.setOnClickListener(this);
		button4.setOnClickListener(this);
		button5.setOnClickListener(this);
		
		contentLayout = (LinearLayout) findViewById(R.id.myinfo_content_layout);
		infoBeanHolder = new InfoBeanHolder();
		int flag = getIntent().getIntExtra("flag", 0);
		if(flag == 2){
			onClick(button3);
			button3.setChecked(true);
		}else{
			onClick(button1);
		}
	}
	
	private void refreshAccountStock() {
		showLoading2 = ViewUtil.showNewLoading(getParent());
		CommunicationManager.getInstance().getAccountStock(userBean, new CommunicationResultListener() {
			
			@Override
			public void resultListener(byte result, Object resultData) {
				super.resultListener(result, resultData);
				switch (result) {
				case CommunicationManager.FAIL:
					ViewUtil.dismiss(showLoading2);
					break;

				case CommunicationManager.SUCCEED:
					Message message = handler.obtainMessage();
					message.arg1 = ACCOUNT_OF_STOCKS;
					message.obj = resultData;
					handler.sendMessage(message);
					break;
				}
			}
		
		});
	}
	
	@Override
	public void onClick(View v) {
		
		Intent intent = new Intent();
		switch (v.getId()) {
		case R.id.myinfo_confirm_button:
			int id = 0;
			if(bean2 != null && bean2.imageList != null && onSelectedView != null && onSelectedView.getId() < bean2.imageList.size()){
				id = bean2.imageList.get(onSelectedView.getId()).id;
			}
			confirmBtn2.setClickable(false);
			showLoading = ViewUtil.showLoading(getParent());
			CommunicationManager.getInstance().mallImageChange(userBean.uid, id, new CommunicationResultListener() {
				@Override
				public void resultListener(byte result, Object resultData) {
					if(result == CommunicationManager.SUCCEED){
						Message message = handler.obtainMessage();
						message.arg1 = CHANGE_IAMGE;
						message.obj = resultData;
						handler.sendMessage(message);
					}else{
						ViewUtil.dismiss(showLoading);
						new Handler().post(new Runnable() {
							
							@Override
							public void run() {
								confirmBtn2.setClickable(true);
							}
						});
					}
				
				}
			});
//			showInforView();
			break;
		case R.id.infor_change_button1:
			showImageView();
			break;
		case R.id.myinfo_radio_button0:
			secondViewType = LIST_INFOR;
			if(!isSecondView){
				showInforView();
			}else{
				showImageView();
			}
			break;
		case R.id.button2:
			intent.setClass(MyInforActivity.this, SimulationActivity.class);
			CompetitiveGroup.getInstance().switchActivity("SimulationActivity", intent , -1, -2);
			break;
		case R.id.button3:
			intent.setClass(MyInforActivity.this, MallActivity.class);
			CompetitiveGroup.getInstance().switchActivity("MallActivity", intent, -1, -2);
			break;
		case R.id.button5:
			intent.setClass(MyInforActivity.this, MallActivity.class);
			intent.putExtra("flag", 2);
			CompetitiveGroup.getInstance().switchActivity("MallActivity", intent, -1, -2);
			break;
		case R.id.myinfo_radio_button1:
			contentLayout.removeAllViews();
			view = inflater.inflate(R.layout.myinfo_caifu_item, null);
			contentLayout.addView(view, new LinearLayout.LayoutParams(-1, -1));
			caiFuList = (ListView) view.findViewById(R.id.myinfo_caifu_listview);
			view.findViewById(R.id.button2).setOnClickListener(this);
			view.findViewById(R.id.button5).setOnClickListener(this);
			view.findViewById(R.id.button3).setOnClickListener(this);
			infoBeanHolder.zijin = (TextView) view.findViewById(R.id.textview_value1);
			infoBeanHolder.jinbing = (TextView) view.findViewById(R.id.textview_value2);
			infoBeanHolder.jiangquan = (TextView) view.findViewById(R.id.textview_value3);
			titleName = "我的财富";
			MainActivity.setTitleName(titleName);
			refreshAccountStock();
			break;
		case R.id.myinfo_radio_button2:
			contentLayout.removeAllViews();
			view = inflater.inflate(R.layout.myinfo_saiguo_item, null);
			saiGuoList = (ListView) view.findViewById(R.id.myinfo_saiguo_listview);
			winTime = (TextView) view.findViewById(R.id.myinfo_saiguo_win);
			loserTime = (TextView) view.findViewById(R.id.myinfo_saiguo_false);
			winLV = (TextView) view.findViewById(R.id.myinfo_saiguo_winLV);
			showLoading = ViewUtil.showLoading(getParent());
			CommunicationManager.getInstance().getGamestatisticAction(userBean.uid, new CommunicationResultListener() {
				
				public void resultListener(byte result, Object resultData) {
					if(result == CommunicationManager.SUCCEED){
						Message message = handler.obtainMessage();
						message.arg1 = SAI_GUO_LIST;
						message.obj = resultData;
						handler.sendMessage(message);
					}else{
						ViewUtil.dismiss(showLoading);
					}
				};
			});
			contentLayout.addView(view, new LinearLayout.LayoutParams(-1, -1));
			titleName = "我的赛果";
			MainActivity.setTitleName(titleName);
			break;
		case R.id.myinfo_radio_button3:
			contentLayout.removeAllViews();
			view = inflater.inflate(R.layout.myinfo_setting_item, null);
			contentLayout.addView(view, new LinearLayout.LayoutParams(-1, -1));
			nickName = (TextView) view.findViewById(R.id.textView2);  
			Button editName = (Button) view.findViewById(R.id.button1);
			Button editPass = (Button) view.findViewById(R.id.button2);
			findPass = (Button) view.findViewById(R.id.button3);
			Button blindXinLang = (Button) view.findViewById(R.id.button4);
			Button blindWeiXin = (Button) view.findViewById(R.id.button5);
			Button editEmail = (Button) view.findViewById(R.id.button6);
			Button bindPhone = (Button) view.findViewById(R.id.button7);
			Button loginOut = (Button) view.findViewById(R.id.button8);
			editName.setOnClickListener(settingOnClickListener);
			editPass.setOnClickListener(settingOnClickListener);
			findPass.setOnClickListener(settingOnClickListener);
			blindXinLang.setOnClickListener(settingOnClickListener);
			blindWeiXin.setOnClickListener(settingOnClickListener);
			editEmail.setOnClickListener(settingOnClickListener);
			bindPhone.setOnClickListener(settingOnClickListener);
			loginOut.setOnClickListener(settingOnClickListener);
			nickName.setText(userBean.nickname);
			titleName = "我的设置";
			MainActivity.setTitleName(titleName);
			isSecondView = false;
			secondViewType = LIST_SETTING;
			break;
		case R.id.myinfo_radio_button4:
			contentLayout.removeAllViews();
			view = inflater.inflate(R.layout.myinfo_daoju_item, null);
			daojuList = (ListView) view.findViewById(R.id.myinfo_daoju_listview);
			contentLayout.addView(view, new LinearLayout.LayoutParams(-1, -1));
			titleName = "我的道具";
			MainActivity.setTitleName(titleName);
			showLoading = ViewUtil.showLoading(getParent());
			CommunicationManager.getInstance().getUserDaoJuList(userBean.uid, getDJListListener);
			break;
		}
	}
	
	private OnClickListener settingOnClickListener = new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.button1:
				editNickName();
				break;
			case R.id.button2:
				editPass();
				break;
			case R.id.button3:
				findPass();
				break;
			case R.id.button4:
				bindXinLang();
				break;
			case R.id.button5:
				bindWeiXin();
				break;
			case R.id.button6:
				editEmail();
				break;
			case R.id.button7:
				bindPhone();
				break;
			case R.id.button8:
				View view = LayoutInflater.from(MyInforActivity.this).inflate(R.layout.guess_dialog_munu, null);
				TextView tx1 = (TextView) view.findViewById(R.id.textView2);
				TextView tx2 = (TextView) view.findViewById(R.id.textView3);
				Button button = (Button) view.findViewById(R.id.guess_confirm);
				Button chongzhi = (Button) view.findViewById(R.id.guess_chongzhi);
				
				final Dialog showDialog = ViewUtil.showDialog(getParent(), view);
				chongzhi.setVisibility(View.VISIBLE);
				button.setText("确定");
				chongzhi.setText("取消");
				tx1.setText("确定要退出么?");
				chongzhi.setOnClickListener(new OnClickListener() {
					
					@Override
					public void onClick(View v) {
						showDialog.cancel();
					}
				});
				button.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						showDialog.cancel();
						
						sendBroadcast(new Intent(MainActivity.USER_QUIT));
						finish();
					}
				});
				
				break;
			}
		}
		
	};
	
	private void bindPhone() {
		// TODO Auto-generated method stub
	}

	private void editEmail() {
		// TODO Auto-generated method stub
		
	}

	private void bindWeiXin() {
		// TODO Auto-generated method stub
		
	}

	private void bindXinLang() {
		// TODO Auto-generated method stub
		
	}

	private void findPass() {
		findPass.setClickable(false);
		CommunicationManager.getInstance().foundPass(UserBean.getInstance().uid, new CommunicationResultListener() {
			@Override
			public void resultListener(byte result, Object resultData) {
				if(CommunicationManager.SUCCEED == result){
					Message message = handler.obtainMessage();
					message.arg1 = FIND_PASS;
					message.obj = resultData;
					handler.sendMessage(message);
				}else{
					new Handler().post(new Runnable() {
						@Override
						public void run() {
							findPass.setClickable(true);
						}
					});
				}
			}
		});
	}

	private void editPass() {
		contentLayout.removeAllViews();
		view = inflater.inflate(R.layout.myinfo_edit_layout, null);
		final EditText yPass = (EditText) view.findViewById(R.id.editText1);
		final EditText nPass = (EditText) view.findViewById(R.id.editText2);
		final EditText cPass = (EditText) view.findViewById(R.id.editText3);
		confirmBtn = (Button) view.findViewById(R.id.button1);
		confirmBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				String yPassValue = yPass.getText().toString();
				String nPassValue = nPass.getText().toString();
				String cPassValue = cPass.getText().toString();
				PreferenceManager manager = PreferenceManager.getInstance(MyInforActivity.this);
				
				if(!manager.getPass().equals(yPassValue)){
					ViewUtil.showToast("原始密码错误", MyInforActivity.this);
					return;
				}
				if(!StringUtil.isNegPass(nPassValue)){
					ViewUtil.showToast("新密码不符合规定", MyInforActivity.this);
					return;
				}
				if(!nPassValue.equals(cPassValue)){
					ViewUtil.showToast("两次密码输入不一致", getParent());
					return;
				}
				CommunicationManager.getInstance().restPass(UserBean.getInstance().uid, yPassValue, nPassValue,
						new CommunicationResultListener() {
					@Override
					public void resultListener(byte result, Object resultData) {
						if(CommunicationManager.SUCCEED == result){
							Message message = handler.obtainMessage();
							message.arg1 = EDIT_PASS;
							message.obj = resultData;
							handler.sendMessage(message);
						}else{
							new Handler().post(new Runnable() {
								@Override
								public void run() {
									confirmBtn.setClickable(true);
								}
							});
						}
					}
				});
			}
		});
		contentLayout.addView(view);
		isSecondView = true;
	}

	private String newName;
	private View confirmBtn;
	private ImageView imageIcon;
	private void editNickName() {
		contentLayout.removeAllViews();
		view = inflater.inflate(R.layout.myinfo_edit_nickname, null);
		final EditText nickName = (EditText) view.findViewById(R.id.editText1);
		confirmBtn = (Button) view.findViewById(R.id.button1);
		confirmBtn.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				confirmBtn.setClickable(false);
				newName = nickName.getText().toString();
				if(nickName.length() == 0 || nickName.length() > 16){
					ViewUtil.showToast("昵称应在0~16个字符之间", getParent());
					return;
				}
				CommunicationManager.getInstance().reSetNameAction(UserBean.getInstance().uid, newName, 
						new CommunicationResultListener() {
					@Override
					public void resultListener(byte result, Object resultData) {
						if(CommunicationManager.SUCCEED == result){
							Message message = handler.obtainMessage();
							message.arg1 = EDIT_NAME;
							message.obj = resultData;
							handler.sendMessage(message);
						}else{
							new Handler().post(new Runnable() {
								@Override
								public void run() {
									confirmBtn.setClickable(true);
								}
							});
						}
					}
				});
			}
		});
		contentLayout.addView(view);
		isSecondView = true;
	}

	private void initSaiGuoViewData(Message msg) {
		// TODO Auto-generated method stub
		
	}

	private void initCaiFuViewData(Message msg) {
		// TODO Auto-generated method stub
		
	}

	private void initInforViewData(Message msg) {
		// TODO Auto-generated method stub
		
	}
	
	private void initDaoJuViewData(Message msg) {
		djList = (List<UserDaoJu>) msg.obj;
		dAdapter = new DaoJuAdapter(djList);
		daojuList.setAdapter(dAdapter);
	}
	
	private void showImageView() {
		contentLayout.removeAllViews();
		view = inflater.inflate(R.layout.myinfo_image_item, null);
		contentLayout.addView(view, new LinearLayout.LayoutParams(-1, -1));
		imageList = (ListView) view.findViewById(R.id.myinfo_image_listView1);
		imageIcon = (ImageView) view.findViewById(R.id.myinfo_image_imageView1);
		imageName = (TextView) view.findViewById(R.id.myinfo_image_textView3);
		imageDesc = (TextView) view.findViewById(R.id.myinfo_image_textView4);
		confirmBtn2 = (Button) view.findViewById(R.id.myinfo_confirm_button);
		confirmBtn2.setOnClickListener(this);
		MainActivity.setTitleName("我的信息");
		isSecondView = true;
		showLoading = ViewUtil.showLoading(getParent());
		mapList.clear();
		CommunicationManager.getInstance().getImageList(new CommunicationResultListener() {
			@Override
			public void resultListener(byte result, Object resultData) {
				if(result == CommunicationManager.SUCCEED && token > 0){
					Message message = handler.obtainMessage();
					message.arg1 = GET_IAMGE_LIST;
					message.obj = resultData;
					handler.sendMessage(message);
				}else{
					ViewUtil.dismiss(showLoading);
				}
			}
		});
	}

	private void showInforView() {
		contentLayout.removeAllViews();
		view = inflater.inflate(R.layout.myinfo_xiang_item, null);
		contentLayout.addView(view, new LinearLayout.LayoutParams(-1, -1));
		
		infoBeanHolder.chengJiuList = (ListView) view.findViewById(R.id.myinfo_item_listView1);
		infoBeanHolder.image = (ImageView) view.findViewById(R.id.myinfo_image_imageView1);
		infoBeanHolder.account = (TextView)view.findViewById(R.id.myinfo_image_textView3);
		infoBeanHolder.nickname = (TextView)view.findViewById(R.id.myinfo_image_textView4);
		infoBeanHolder.level = (TextView)view.findViewById(R.id.textView2);
		infoBeanHolder.experience = (TextView)view.findViewById(R.id.textView4);
		infoBeanHolder.neddEx = (TextView)view.findViewById(R.id.textView6);
		infoBeanHolder.vip = (TextView)view.findViewById(R.id.textView8);
		infoBeanHolder.jiasu = (TextView)view.findViewById(R.id.textView10);
		infoBeanHolder.youxiaoqi = (TextView)view.findViewById(R.id.textView16);
		infoBeanHolder.vipyouxiaoqi = (TextView)view.findViewById(R.id.textView17);
		view.findViewById(R.id.infor_change_button1).setOnClickListener(this);
		showLoading = ViewUtil.showLoading(getParent());
		CommunicationManager.getInstance().getUserInfo(userBean.uid, userInforListener);
		MainActivity.setTitleName("我的信息");
		isSecondView = false;
	}
	
	private CommunicationResultListener editNameListListener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED && token > 0){
				Message message = handler.obtainMessage();
				message.arg1 = EDIT_NAME;
				message.obj = resultData;
				handler.sendMessage(message);
			}else{
				ViewUtil.dismiss(showLoading);
//				ViewUtil.showToast((String)resultData, MyInforActivity.this);
			}
		};
	};
	
	private CommunicationResultListener userInforListener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED && token > 0){
				Message message = handler.obtainMessage();
				message.arg1 = USER_INFO;
				message.obj = resultData;
				handler.sendMessage(message);
			}else{
				ViewUtil.dismiss(showLoading);
//				ViewUtil.showToast((String)resultData, MyInforActivity.this);
			}
		};
	};
	
	/*private CommunicationResultListener caiFuListListener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED && token > 0){
				Message message = handler.obtainMessage();
				message.arg1 = CAI_FU_LIST;
				message.obj = resultData;
				handler.sendMessage(message);
			}else{
				ViewUtil.dismiss(showLoading);
//				ViewUtil.showToast((String)resultData, MyInforActivity.this);
			}
		};
	};*/
	
	private CommunicationResultListener getDJListListener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED && token > 0){
				Message message = handler.obtainMessage();
				message.arg1 = LIST_DAOJU;
				message.obj = resultData;
				handler.sendMessage(message);
			}else{
				ViewUtil.dismiss(showLoading);
//				ViewUtil.showToast((String)resultData, MyInforActivity.this);/
			}
		};
	};
	
	private ImageListBean bean2;
	Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.arg1) {
			case CHANGE_IAMGE:
				confirmBtn2.setClickable(true);
				final ResponsedBean responseBean = (ResponsedBean) msg.obj;
				View view = LayoutInflater.from(MyInforActivity.this).inflate(R.layout.guess_dialog_munu, null);
				TextView tx1 = (TextView) view.findViewById(R.id.textView2);
				TextView tx2 = (TextView) view.findViewById(R.id.textView3);
				Button button = (Button) view.findViewById(R.id.guess_confirm);
				Button chongzhi = (Button) view.findViewById(R.id.guess_chongzhi);
				
				final Dialog showDialog = ViewUtil.showDialog(getParent(), view);
				if(responseBean.responsecode == 29){
					tx1.setText("不能更换形象，请到商城购买形象卡");
					chongzhi.setVisibility(View.VISIBLE);
					chongzhi.setText("取消");
					chongzhi.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							showDialog.cancel();
						}
					});
					button.setText("商城");
					button.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							Intent intent = new Intent(MyInforActivity.this, MallActivity.class);
		   					CompetitiveGroup.getInstance().switchActivity("MallActivity", intent, -1, -1);
							showDialog.cancel();
						}
					});
				}else{
					tx1.setText(responseBean.msg);
					chongzhi.setVisibility(View.GONE);
					button.setText("确定");
					button.setOnClickListener(new OnClickListener() {
						
						@Override
						public void onClick(View v) {
							if(responseBean.responsecode == 10){
								showInforView();
							}
							showDialog.cancel();
						}
					});
				}
				break;
			case GET_IAMGE_LIST:
				bean2 = (ImageListBean) msg.obj;
				if(bean2.responsecode == 10){
					iAdapter = new ImageAdapter(bean2.imageList);
					imageList.setAdapter(iAdapter);
					if(bean2.imageList != null && bean2.imageList.size() > 0){
						ImageBean imageBean = bean2.imageList.get(0);
						imageName.setText(imageBean.name);
						imageDesc.setText(imageBean.descp);
					}
					iAdapter.getView(0, null, null).setBackgroundColor(getResources().getColor(R.color.yello));
				}else{
					ViewUtil.showToast(bean.msg, getParent());
				}
				break;
			case FIND_PASS:
				ResponsedBean response3 = (ResponsedBean) msg.obj;
				if(response3.responsecode == 10){
					ViewUtil.showToast("密码已发送至邮箱，请注意查收", getParent());
				}else{
					ViewUtil.showToast(response3.msg, getParent());
				}
				findPass.setClickable(true);
				break;
			case EDIT_NAME:
				if(confirmBtn != null) confirmBtn.setClickable(true);
				ResponsedBean response = (ResponsedBean) msg.obj;
				ViewUtil.showBackDialog(getParent(), response.responsecode == 10, response.msg);
				if(response.responsecode == 10){
					sendBroadcast(new Intent(MainActivity.UPDATE_USER_INFOR));
					nickName.setText(newName);
				}
				break;
			case EDIT_PASS:
				if(confirmBtn != null) confirmBtn.setClickable(true);
				ResponsedBean response2 = (ResponsedBean) msg.obj;
				ViewUtil.showBackDialog(getParent(), response2.responsecode == 10, response2.msg);
				break;
			case LIST_INFOR:
				initInforViewData(msg);
				break;

			case LIST_CAIFU:
				initCaiFuViewData(msg);
				break;

			case LIST_SAIGUO:
				initSaiGuoViewData(msg);
				break;
			case LIST_DAOJU:
				initDaoJuViewData(msg);
				break;
			case USER_INFO:
				UserBean userBean = (UserBean) msg.obj;
				UserBean.setUser(userBean);
				infoBeanHolder.account.setText(userBean.account + "");
				infoBeanHolder.nickname.setText(userBean.nickname);
				infoBeanHolder.level.setText(userBean.level + "");
				infoBeanHolder.experience.setText(userBean.experience + "");
				infoBeanHolder.neddEx.setText(userBean.experiencevalue + "");
				infoBeanHolder.vip.setText(userBean.vip + "");
				infoBeanHolder.jiasu.setText(userBean.acceleration + "天");
				infoBeanHolder.youxiaoqi.setText(/*userBean.youxiaoqi + */"");
				infoBeanHolder.vipyouxiaoqi.setText(/*userBean.vipyouxiaoqi + */"");
				if(userBean.pic != null && userBean.pic.length() > 4){
					new ImageAsyncTask(-1, infoBeanHolder.image).execute(userBean.pic);
				}
				cList = new ArrayList<ChengJiuBean>();
				cList.add(new ChengJiuBean());
				cList.add(new ChengJiuBean());
				cList.add(new ChengJiuBean());
				cList.add(new ChengJiuBean());
				cList.add(new ChengJiuBean());
				cAdapter = new ChengJiuAdapter(cList);
				infoBeanHolder.chengJiuList.setAdapter(cAdapter);
				sendBroadcast(new Intent(MainActivity.UPDATE_USER_INFOR));
				break;
			case SAI_GUO_LIST:
				bean = (SaiguoXiangBean) msg.obj;
				if(bean.responsecode == 10){
					winLV.setText(DataUtil.getDoubleString(bean.lv) + "%");
					winTime.setText("" + bean.y);
					loserTime.setText("" + bean.s);
					sAdapter = new SaiGuoAdapter(bean.saiguoList);
					saiGuoList.setAdapter(sAdapter);
				}
				break;
			case DELE_SAI_GUO:
				// TODO 删除赛果项目
				if(msg.what == 10){
					bean.saiguoList.remove((SaiGuoBean)msg.obj);
					sAdapter.setData(bean.saiguoList);
					sAdapter.notifyDataSetChanged();
				}else{
					ViewUtil.showToast("删除失败", MyInforActivity.this);
				}
				break;
			case ACCOUNT_OF_STOCKS:
				UserBean user2 = (UserBean) msg.obj;
				infoBeanHolder.jinbing.setText((int)user2.gold + "");
				infoBeanHolder.zijin.setText(user2.fund + "");
				infoBeanHolder.jiangquan.setText((int)user2.lottery + "");
				adapter = new StockAdapter(user2.listStock);
				caiFuList.setAdapter(adapter);
				adapter.notifyDataSetChanged();
				ViewUtil.dismiss(showLoading2);
			}
			ViewUtil.dismiss(showLoading);
			return false;
		}
	});
	private Button findPass;
	private TextView nickName;
	private TextView imageName;
	private TextView imageDesc;
	
	@Override
	public void onBackPressed() {
		if(isSecondView){
			if(secondViewType == LIST_INFOR){
				showInforView();
			}else if(secondViewType == LIST_SETTING){
				onClick(button4);
			}
			isSecondView = false;
		}else{
			CompetitiveGroup.getInstance().back();
		}
	}
	
	class DaoJuAdapter extends BaseAdapter{
		
        List<UserDaoJu> mList;
        
        public DaoJuAdapter(List<UserDaoJu> mList) {
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
			CaiFuHolder holder = null;
			if(convertView == null){
				convertView = inflater.inflate(R.layout.myinfo_caifu_list_item, null);
				holder = new CaiFuHolder();
				holder.tv1 = (TextView) convertView.findViewById(R.id.myinfo_caifu_textView3);
				holder.tv2 = (TextView) convertView.findViewById(R.id.myinfo_caifu_textView4);
				holder.tv3 = (TextView) convertView.findViewById(R.id.myinfo_caifu_textView5);
				holder.tv4 = (TextView) convertView.findViewById(R.id.myinfo_caifu_textView6);
				convertView.findViewById(R.id.myinfo_caifu_textView1).setVisibility(View.GONE);
				convertView.findViewById(R.id.myinfo_caifu_textView2).setVisibility(View.GONE);
				holder.btn = (Button) convertView.findViewById(R.id.myinfo_caifu_button1);
				holder.btn.setBackgroundResource(R.drawable.btn_gold_selector);
				convertView.setTag(holder);
			} else {
				holder = (CaiFuHolder) convertView.getTag();
			}
			UserDaoJu bean = mList.get(position);
			if(position % 2 == 0) convertView.setBackgroundColor(getResources().getColor(R.color.itemcolor0));
			else convertView.setBackgroundColor(getResources().getColor(R.color.itemcolor1));
			holder.btn.setText("使用");
			holder.tv1.setText(bean.name);
			holder.tv1.setTextColor(Color.RED);
			holder.tv2.setText(bean.shuliang + "");
			holder.tv2.setTextColor(Color.RED);
			if(0 == bean.endtime){
				holder.tv3.setText("一次性");
			}else{
				holder.tv3.setText(bean.endtime + "天");
			}
			holder.tv4.setText(bean.instruction);
			holder.btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO 使用道具
				}
			});
			return convertView;
		}
		
	}
	
	class ChengJiuAdapter extends BaseAdapter{
		
        List<ChengJiuBean> mList;
        
        public ChengJiuAdapter(List<ChengJiuBean> mList) {
        	this.mList = mList;
		}
        
		@Override
		public int getCount() {
			return 5;
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
			CaiFuHolder holder = null;
			if(convertView == null){
				convertView = inflater.inflate(R.layout.myinfo_caifu_list_item, null);
				holder = new CaiFuHolder();
				holder.tv1 = (TextView) convertView.findViewById(R.id.myinfo_caifu_textView4);
				holder.tv2 = (TextView) convertView.findViewById(R.id.myinfo_caifu_textView1);
				holder.tv3 = (TextView) convertView.findViewById(R.id.myinfo_caifu_textView5);
				holder.tv4 = (TextView) convertView.findViewById(R.id.myinfo_caifu_textView6);
				convertView.findViewById(R.id.myinfo_caifu_textView3).setVisibility(View.GONE);
				convertView.findViewById(R.id.myinfo_caifu_textView2).setVisibility(View.GONE);
				convertView.findViewById(R.id.myinfo_caifu_button1).setVisibility(View.GONE);
				convertView.setTag(holder);
			} else {
				holder = (CaiFuHolder) convertView.getTag();
			}
			ChengJiuBean jiuBean = cList.get(position);
			if(position % 2 == 0) convertView.setBackgroundColor(getResources().getColor(R.color.itemcolor0));
			else convertView.setBackgroundColor(getResources().getColor(R.color.itemcolor1));
			holder.tv1.setText("".equals(jiuBean.name)?"略有小成":jiuBean.name);
			holder.tv2.setText("".equals(jiuBean.tianJian)?"玩家到10级":jiuBean.tianJian);
			holder.tv3.setText("".equals(jiuBean.jiangLi)?"100资金":jiuBean.jiangLi);
			holder.tv4.setText("获得");
			holder.tv4.setPadding(5, 10, 5, 10);
			return convertView;
		}
		
	}
	
	class SaiGuoHolder{
		TextView  tv;
		ImageView img;
		Button    btn;
	}
	
	private View onSelectedView;
	Map<Integer, Bitmap> mapList = new HashMap<Integer, Bitmap>();
	class ImageAdapter extends BaseAdapter{
        List<ImageBean> mList;
        
        public ImageAdapter(List<ImageBean> mList) {
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
		public View getView(final int position, View convertView, ViewGroup parent) {
			ImageHolder holder = null;
			if(convertView == null){
				convertView = inflater.inflate(R.layout.myinfo_image_list_item, null);
				holder = new ImageHolder();
				holder.tv1 = (TextView) convertView.findViewById(R.id.myinfo_image_textView1);
				holder.btn = (ImageView) convertView.findViewById(R.id.myinfo_image_imageView1);
				convertView.setTag(holder);
			} else {
				holder = (ImageHolder) convertView.getTag();
			}
			if(position % 2 == 0) convertView.setBackgroundColor(getResources().getColor(R.color.itemcolor1));
			else convertView.setBackgroundColor(getResources().getColor(R.color.itemcolor0));
			final ImageBean imageBean = mList.get(position);
			if(mapList.containsKey(position)){
				holder.btn.setBackgroundDrawable(new BitmapDrawable(mapList.get(position)));
			}else{
				new ImageAsyncTask(position, holder.btn).execute(imageBean.path);
			}
			holder.tv1.setText(imageBean.name);
			if(onSelectedView != null && onSelectedView.getId() == position){
				convertView.setBackgroundColor(getResources().getColor(R.color.yello));
			}
			convertView.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					if(onSelectedView != null){
						if((Integer)onSelectedView.getId() % 2 == 0) 
							onSelectedView.setBackgroundColor(getResources().getColor(R.color.itemcolor1));
						else onSelectedView.setBackgroundColor(getResources().getColor(R.color.itemcolor0));
					}
					onSelectedView = v;
					onSelectedView.setId(position);
					onSelectedView.setBackgroundColor(getResources().getColor(R.color.yello));
					imageIcon.setBackgroundDrawable(new BitmapDrawable(mapList.get(position)));
					imageName.setText(imageBean.name);
					imageDesc.setText(imageBean.descp);
				}
			});
			return convertView;
		}
		
	}
	
	class ImageAsyncTask extends AsyncTask<String, Integer, Bitmap>{
		
		private ImageView image;
		private int position;

		public ImageAsyncTask(int position, ImageView image) {
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
	
	class ImageHolder{
		TextView tv1;
		ImageView   btn;
	}
	
	class SaiGuoAdapter extends BaseAdapter{
		
        List<SaiGuoBean> mList;
        
        public SaiGuoAdapter(List<SaiGuoBean> mList) {
        	this.mList = mList;
		}
        
        public void setData( List<SaiGuoBean> mList){
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
			SaiGuoHolder holder = null;
			if(convertView == null){
				convertView = inflater.inflate(R.layout.myinfo_saiguo_list_item, null);
				holder = new SaiGuoHolder();
				holder.tv = (TextView) convertView.findViewById(R.id.myinfo_saiguo_textView1);
				holder.img = (ImageView) convertView.findViewById(R.id.myinfo_saiguo_imageView1);
				holder.btn = (Button) convertView.findViewById(R.id.myinfo_saiguo_button1);
				convertView.setTag(holder);
			} else {
				holder = (SaiGuoHolder) convertView.getTag();
			}
			final SaiGuoBean saiGuoBean = mList.get(position);
			if(saiGuoBean.detail == 1){
				holder.tv.setText(Html.fromHtml("恭喜您在" + saiGuoBean.time + "的" + saiGuoBean.name + "中" + "<font color=\"#D03738\">获胜，得到：" 
			                       + saiGuoBean.result + "金币, " + saiGuoBean.dianjuan + "奖券</font>"));
				holder.img.setImageResource(R.drawable.saiguo_win);
			}else if(saiGuoBean.detail == 3){
				holder.img.setImageResource(R.drawable.saiguo_false);
				holder.tv.setText(Html.fromHtml("很遗憾,您在" + saiGuoBean.time + "的" + saiGuoBean.name + "中" + "<font color=\"#26C100\">失败，失去：" + Math.abs(saiGuoBean.result) + "金币, 得到：" + saiGuoBean.dianjuan + "奖券</font>"));
			}else{
				holder.tv.setText(Html.fromHtml("您在" + saiGuoBean.time + "的" + saiGuoBean.name + "中获得"+ "<font color=\"#ffffff\">平局，失去：" + saiGuoBean.result + "金币, 得到：" + saiGuoBean.dianjuan + "奖券</font>"));
				holder.img.setImageResource(R.drawable.saiguo_ping);
			}
			holder.tv.setMovementMethod(LinkMovementMethod.getInstance());
			holder.btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {

					View view = LayoutInflater.from(MyInforActivity.this).inflate(R.layout.guess_dialog_munu, null);
//					TextView tx0 = (TextView) view.findViewById(R.id.textView1);
					TextView tx1 = (TextView) view.findViewById(R.id.textView2);
					TextView tx2 = (TextView) view.findViewById(R.id.textView3);
					Button button = (Button) view.findViewById(R.id.guess_confirm);
					Button chongzhi = (Button) view.findViewById(R.id.guess_chongzhi);
					
					final Dialog showDialog = ViewUtil.showDialog(getParent(), view);
					chongzhi.setVisibility(View.VISIBLE);
					chongzhi.setText("取消");
					
					tx1.setText("确定要删除吗？");
					button.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							CommunicationManager.getInstance().delGamestatisticAction(userBean.uid, saiGuoBean.id, new CommunicationResultListener() {
								
								public void resultListener(byte result, Object resultData) {
									if(result == CommunicationManager.SUCCEED && token > 0){
										Message message = handler.obtainMessage();
										message.arg1 = DELE_SAI_GUO;
										message.what = (Integer) resultData;
										message.obj = saiGuoBean;
										handler.sendMessage(message);
									}else{
										ViewUtil.dismiss(showLoading);
									}
								};
							} );
							showDialog.cancel();
						}
					});
					chongzhi.setOnClickListener(new OnClickListener() {

						@Override
						public void onClick(View v) {
							showDialog.cancel();
						}
					});
				}
			});
			return convertView;
		}
		
	}
	
	class StockAdapter extends BaseAdapter{
		
        List<AccountStockBean> mList = new ArrayList<AccountStockBean>();
        
        public StockAdapter(List<AccountStockBean> mList) {
        	if(mList != null){
        		this.mList = mList;
        	}
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
			CaiFuHolder holder = null;
			if(convertView == null){
				convertView = inflater.inflate(R.layout.myinfo_caifu_list_item, null);
				holder = new CaiFuHolder();
				holder.tv1 = (TextView) convertView.findViewById(R.id.myinfo_caifu_textView1);
				holder.tv2 = (TextView) convertView.findViewById(R.id.myinfo_caifu_textView2);
				holder.tv3 = (TextView) convertView.findViewById(R.id.myinfo_caifu_textView3);
				holder.tv4 = (TextView) convertView.findViewById(R.id.myinfo_caifu_textView4);
				holder.tv5 = (TextView) convertView.findViewById(R.id.myinfo_caifu_textView5);
				holder.tv6 = (TextView) convertView.findViewById(R.id.myinfo_caifu_textView6);
				holder.btn = (Button) convertView.findViewById(R.id.myinfo_caifu_button1);
				holder.tv2.setVisibility(View.GONE);
				convertView.setTag(holder);
			} else {
				holder = (CaiFuHolder) convertView.getTag();
			}
			final AccountStockBean stockBean = mList.get(position);
			if(position % 2 == 0) convertView.setBackgroundColor(getResources().getColor(R.color.itemcolor0));
			else convertView.setBackgroundColor(getResources().getColor(R.color.itemcolor1));
			holder.tv1.setText(stockBean.sname);
			holder.tv3.setText(stockBean.member + "");
			holder.tv4.setText(String.valueOf(stockBean.closes));
			holder.tv5.setText(DataUtil.getDoubleString(stockBean.zhangfu) + "%");
			holder.tv6.setText(stockBean.shizhi + "");
			if(stockBean.zhangfu > 0){
				holder.tv5.setTextColor(getResources().getColor(R.color.red));
				holder.tv4.setTextColor(getResources().getColor(R.color.red));
			} else if(stockBean.zhangfu == 0){
				holder.tv5.setTextColor(getResources().getColor(R.color.white));
				holder.tv4.setTextColor(getResources().getColor(R.color.white));
			} else {
				holder.tv5.setTextColor(getResources().getColor(R.color.green));
				holder.tv4.setTextColor(getResources().getColor(R.color.green));
			}
			holder.btn.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent(MyInforActivity.this, SimulationSubActivity.class);
					intent.putExtra(Constant.string.INTENT_SIMULATION_ACTION, Constant.id.INTENT_SIMULATION_SELL);
					intent.putExtra(Constant.string.INTENT_SIMULATION_VALUE, stockBean);
					CompetitiveGroup.getInstance().switchActivity("SimulationSubActivity", intent , -1, -1);
				}
			});
			return convertView;
		}
		
	}
	
	class CaiFuHolder{
		TextView tv1;
		TextView tv2;
		TextView tv3;
		TextView tv4;
		TextView tv5;
		TextView tv6;
		Button   btn;
	}
	
	@Override
	public void onContentChanged() {
		MainActivity.setTitleName(titleName);
	}
	
}
