package cn.chinat2t.stockgod.activity;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RadioGroup.OnCheckedChangeListener;
import android.widget.TextView;
import cn.chinat2t.stockgod.MainActivity;
import cn.chinat2t.stockgod.MainActivity.OnActivityResultListener;
import cn.chinat2t.stockgod.R;
import cn.chinat2t.stockgod.alipay.AlixId;
import cn.chinat2t.stockgod.alipay.MobileSecurePayHelper;
import cn.chinat2t.stockgod.alipay.MobileSecurePayer;
import cn.chinat2t.stockgod.alipay.PartnerConfig;
import cn.chinat2t.stockgod.alipay.ResultChecker;
import cn.chinat2t.stockgod.alipay.Rsa;
import cn.chinat2t.stockgod.bean.ChargeBean;
import cn.chinat2t.stockgod.bean.UserBean;
import cn.chinat2t.stockgod.http.CommunicationManager;
import cn.chinat2t.stockgod.http.CommunicationResultListener;
import cn.chinat2t.stockgod.utils.StringUtil;
import cn.chinat2t.stockgod.utils.ViewUtil;

import com.unionpay.UPPayAssistEx;

public class ChongZhiView extends LinearLayout implements OnClickListener{

	private final String LOG_TAG ="sysm.out";
	public final static int CHARGE_PHONE = 0;
	public final static int CHARGE_YINLIAN = 1;
	public final static int CHARGE_YINLIAN_SUCCESS = 2;
	public final static int CHARGE_ALIPAY = 3;
	
	public static final int PLUGIN_VALID = 0;
	public static final int PLUGIN_NOT_INSTALLED = -1;
	public static final int PLUGIN_NEED_UPGRADE = 2;

    /*****************************************************************
     * mMode�������ͣ�
     *      "00" - ����������ʽ����
     *      "01" - �����������Ի���
     *****************************************************************/
    private String mMode = "01";
    public static final String TN_URL_01 = "http://222.66.233.198:8080/sim/gettn";
    
	private LinearLayout layout;
	private LinearLayout rechargeLayout;
	private LayoutInflater inflater;
	private RadioButton zhifubaoBtn;
	private RadioButton yinlianBtn;
	private RadioButton phoneBtn;
	private Button onClickBtn;
	private Button onSelectBtn;
	private int chargeFlag;
//	private int type;
	private View baoView;
	private View phoneView;
	private View lianView;
	private UserBean userBean;
	private Button zhifuBtn;
	private ProgressDialog showLoading;
	private ProgressDialog mLoadingDialog;
	private EditText customValue;
	private Activity act;
	private String tn = "";
	private int chargeValue = 0;
	private EditText customValue2;
	private int chargeValue2;
    
	public ChongZhiView(Context context) {
		super(context);
		act = (Activity) context;
	}
	
	public ChongZhiView(Context context, int flag, int type) {
		super(context);
		act = (MallActivity) context;
		chargeFlag = flag;
		inflater = LayoutInflater.from(context);
		layout = (LinearLayout) inflater.inflate(R.layout.recharge_layout, null);
		addView(layout, new LinearLayout.LayoutParams(-1, -1));

		userBean = UserBean.getInstance();
		initView();
		MainActivity.intance.setOnActivityResultListener(onActivityResultListener);
	}

	protected void initView() {
		rechargeLayout = (LinearLayout) layout.findViewById(R.id.recharge_type_layout);
		zhifubaoBtn = (RadioButton) layout.findViewById(R.id.recharge_type_button1);
		phoneBtn = (RadioButton) layout.findViewById(R.id.recharge_type_button2);
		yinlianBtn = (RadioButton) layout.findViewById(R.id.recharge_type_button3);
		zhifuBtn = (Button) findViewById(R.id.button12);
		
		zhifubaoBtn.setOnClickListener(this);
		phoneBtn.setOnClickListener(this);
		yinlianBtn.setOnClickListener(this);
		
		baoView = inflater.inflate(R.layout.recharge_zhifubao_layou, null);
		phoneView = inflater.inflate(R.layout.recharge_shouji_layou, null);
		lianView = inflater.inflate(R.layout.recharge_yinlian_layou, null);
		
		onClickBtn = phoneBtn;
		onClick(phoneBtn);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.recharge_type_button1:
			onClickBtn.setTextColor(Color.WHITE);
			onClickBtn = (Button)v;
			onClickBtn.setTextColor(Color.BLACK);
			rechargeLayout.removeAllViews();
			rechargeLayout.addView(baoView, new LinearLayout.LayoutParams(-1, -2));
			chargeForZhiFuBao();
			break;

		case R.id.recharge_type_button2:
			onClickBtn.setTextColor(Color.WHITE);
			onClickBtn = (Button)v;
			onClickBtn.setTextColor(Color.BLACK);
			rechargeLayout.removeAllViews();
			rechargeLayout.addView(phoneView, new LinearLayout.LayoutParams(-1, -2));
			chargeForPhone();
			break;
			
		case R.id.recharge_type_button3:
			onClickBtn.setTextColor(Color.WHITE);
			onClickBtn = (Button)v;
			onClickBtn.setTextColor(Color.BLACK);
			rechargeLayout.removeAllViews();
			rechargeLayout.addView(lianView, new LinearLayout.LayoutParams(-1, -2));
			chargeForYinLian();
			break;
		case R.id.button7:
			onClickButton(v, 0);
			break;
		case R.id.button8:
			onClickButton(v, 0);
			break;
		case R.id.button9:
			onClickButton(v, 0);
			break;
		case R.id.button1:
			onClickButton(v, 1);
			break;
		case R.id.button2:
			onClickButton(v, 1);
			break;
		case R.id.button3:
			onClickButton(v, 1);
			break;
		}
	}

	private void onClickButton(View v, int type) {
		/*if(userBean.usertype == UserBean.USER_LIN_SHI){
			ViewUtil.showLinShiDialog(act.getParent(), "��ʱ�û����ܲ���������ע��");
			return;
		}*/
		if(onSelectBtn == (Button)v){
			onSelectBtn.setBackgroundResource(R.drawable.btn_white_active);
			if(type == 0){
				chargeValue = 0;
			}else{
				chargeValue2 = 0;
			}
			onSelectBtn = null;
			return;
		}
		if(onSelectBtn != null){
			onSelectBtn.setBackgroundResource(R.drawable.btn_white_active);
		}
		if(type == 0 && customValue != null){
			customValue.setText("");
		}else if(type == 1 && customValue2 != null){
			customValue2.setText("");
		}
		onSelectBtn = (Button)v;
		onSelectBtn.setBackgroundResource(R.drawable.btn_gold_yellow);
		if(type == 0){
			chargeValue = Integer.parseInt(onSelectBtn.getText().toString());
		}else{
			chargeValue2 = Integer.parseInt(onSelectBtn.getText().toString());
		}
	}
    
	private void chargeForZhiFuBao() {
		customValue2 = (EditText) baoView.findViewById(R.id.editText12);
		Button btn1 = (Button) baoView.findViewById(R.id.button1);
		Button btn2 = (Button) baoView.findViewById(R.id.button2);
		Button btn3 = (Button) baoView.findViewById(R.id.button3);
		if(onSelectBtn != null){
			onSelectBtn.setBackgroundResource(R.drawable.btn_white_active);
		}
		onSelectBtn = btn1;
		chargeValue2 = Integer.parseInt(onSelectBtn.getText().toString());
		onSelectBtn.setBackgroundResource(R.drawable.btn_gold_yellow);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		customValue2.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(s.toString().length() > 0 && onSelectBtn != null){
					onSelectBtn.setBackgroundResource(R.drawable.btn_white_active);
				}else if(s.toString().length() == 0){
					chargeValue2 = 0;
				}
			}
		});
		zhifuBtn.setText("ȷ��");
		zhifuBtn.setOnClickListener(new View.OnClickListener() {

			@Override
	        public void onClick(View v) {
				if(userBean.usertype == UserBean.USER_LIN_SHI){
					ViewUtil.showLinShiDialog(act.getParent(), "��ʱ�û����ܲ���������ע��");
					return;
				}
				String length = customValue2.getText().toString();
				if(length.length() != 0 && length.length() > 8){
					ViewUtil.showToast("��ֵ���̫��", act.getParent());
					return;
				}
				if(chargeValue2 <= 0){
					ViewUtil.showToast("��ֵ����С��0", act.getParent());
					return;
				}
				if(length.length() != 0){
					chargeValue2 = Integer.parseInt(length);
				}
	            zhifuBtn.setClickable(false);
	            mLoadingDialog = (ProgressDialog) ViewUtil.showLoading(act.getParent());
	            mLoadingDialog.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss(DialogInterface dialog) {
						zhifuBtn.setClickable(true);
					}
				});
	            mLoadingDialog.setMessage("����Ŭ���Ļ�ȡ��ֵ��ˮ��,���Ժ�...");
	            mLoadingDialog.setCancelable(false);
	            
	            // ����֧����
	            performPay();
	        }
	    });
	
	}

	private void performPay(){
		MobileSecurePayHelper mspHelper = new MobileSecurePayHelper(act);
		boolean isMobile_spExist = mspHelper.detectMobile_sp();
		if(!isMobile_spExist) return;
		if (!checkInfo()) {
			ViewUtil.showToast("ȱ��partner����seller������PartnerConfig.java�����ӡ�", act);
			return;
		}
		// start pay for this order.
				// ���ݶ�����Ϣ��ʼ����֧��
		try {
			// prepare the order info.
			// ׼��������Ϣ
			String orderInfo = getOrderInfo();
			// �������ǩ����ʽ�Զ�����Ϣ����ǩ��
			String signType = getSignType();
			String strsign = sign(signType, orderInfo);
			Log.v("sign:", strsign);
			// ��ǩ�����б���
			strsign = URLEncoder.encode(strsign, "UTF-8");
			String alipayInfo = orderInfo + "&sign=" + "\"" + strsign + "\"" + "&" + getSignType();
			Log.v("orderInfo:", alipayInfo);
			// start the pay.
			// ����pay��������֧��
			MobileSecurePayer msp = new MobileSecurePayer();
			boolean bRet = msp.pay(alipayInfo, mHandler, AlixId.RQF_PAY, act);

			if (bRet) {
				// show the progress bar to indicate that we have started paying.
				// ��ʾ������֧����������
				ViewUtil.dismiss(mLoadingDialog);
				showLoading = (ProgressDialog) ViewUtil.showLoading(act.getParent());
				showLoading.setMessage("����֧��...");
			}
		} catch (Exception ex) {
			ViewUtil.showToast(act.getString(R.string.remote_call_failed), act);
		}		
	}
	
	String getOrderInfo() {
		String strOrderInfo = "partner=\"" + PartnerConfig.PARTNER + "\"&";
		strOrderInfo += "seller=\"" + PartnerConfig.SELLER + "\"&";
		strOrderInfo += "out_trade_no=\"" + getOutTradeNo() + "\"&";
		strOrderInfo += "subject=\"" + "֧������ֵ" + "\"&";
		strOrderInfo += "body=\"" + "��ֵ��" + chargeValue2 + "\"&";
		strOrderInfo += "total_fee=\"" + chargeValue2 + "\"&";
		strOrderInfo += "notify_url=\"" + "http://notify.java.jpxx.org/index.jsp" + "\"";

		return strOrderInfo;
	}
	
	/**
	 * get the out_trade_no for an order. ��ȡ�ⲿ������
	 * @return
	 */
	String getOutTradeNo() {
		SimpleDateFormat format = new SimpleDateFormat("MMddHHmmss", Locale.getDefault());
		Date date = new Date();
		String strKey = format.format(date);

		java.util.Random r = new java.util.Random();
		strKey = strKey + r.nextInt();
		strKey = strKey.substring(0, 15);
		return strKey;
	}
	
	private boolean checkInfo() {
		String partner = PartnerConfig.PARTNER;
		String seller = PartnerConfig.SELLER;
		if (partner == null || partner.length() <= 0 || seller == null
				|| seller.length() <= 0)
			return false;

		return true;
	}
	
	/**
	 * sign the order info. �Զ�����Ϣ����ǩ��
	 * @param signType
	 *            ǩ����ʽ
	 * @param content
	 *            ��ǩ��������Ϣ
	 * @return
	 */
	String sign(String signType, String content) {
		return Rsa.sign(content, PartnerConfig.RSA_PRIVATE);
	}
	/**
	 * get the sign type we use. ��ȡǩ����ʽ
	 * 
	 * @return
	 */
	String getSignType() {
		String getSignType = "sign_type=" + "\"" + "RSA" + "\"";
		return getSignType;
	}
	
	private void chargeForYinLian() {
		customValue = (EditText) lianView.findViewById(R.id.editText1);
		Button btn1 = (Button) lianView.findViewById(R.id.button7);
		Button btn2 = (Button) lianView.findViewById(R.id.button8);
		Button btn3 = (Button) lianView.findViewById(R.id.button9);
		if(onSelectBtn != null){
			onSelectBtn.setBackgroundResource(R.drawable.btn_white_active);
		}
		onSelectBtn = btn1;
		chargeValue = Integer.parseInt(onSelectBtn.getText().toString());
		onSelectBtn.setBackgroundResource(R.drawable.btn_gold_yellow);
		btn1.setOnClickListener(this);
		btn2.setOnClickListener(this);
		btn3.setOnClickListener(this);
		customValue.addTextChangedListener(new TextWatcher() {
			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
			
			@Override
			public void afterTextChanged(Editable s) {
				if(s.toString().length() > 0 && onSelectBtn != null){
					onSelectBtn.setBackgroundResource(R.drawable.btn_white_active);
				}else if(s.toString().length() == 0){
					chargeValue = 0;
				}
			}
		});
		zhifuBtn.setText("ȷ��");
		zhifuBtn.setOnClickListener(new View.OnClickListener() {

			@Override
	        public void onClick(View v) {
				if(userBean.usertype == UserBean.USER_LIN_SHI){
					ViewUtil.showLinShiDialog(act.getParent(), "��ʱ�û����ܲ���������ע��");
					return;
				}
				String length = customValue.getText().toString();
				if(length.length() != 0 && length.length() > 8){
					ViewUtil.showToast("��ֵ���̫��", act.getParent());
					return;
				}
				if(chargeValue <= 0){
					ViewUtil.showToast("��ֵ����С��0", act.getParent());
					return;
				}
				if(length.length() != 0){
					chargeValue = Integer.parseInt(length);
				}
	            zhifuBtn.setClickable(false);
	            mLoadingDialog = (ProgressDialog) ViewUtil.showLoading(act.getParent());
	            mLoadingDialog.setOnDismissListener(new OnDismissListener() {
					
					@Override
					public void onDismiss(DialogInterface dialog) {
						zhifuBtn.setClickable(true);
					}
				});
	            mLoadingDialog.setMessage("����Ŭ���Ļ�ȡ��ֵ��ˮ��,���Ժ�...");
	            mLoadingDialog.setCancelable(false);

	            /************************************************* 
	             * 
	             *  ����1�������翪ʼ,��ȡ������ˮ�ż�TN 
	             *  
	             ************************************************/
//	            new Thread(runnable).start();
	            CommunicationManager.getInstance().getYinLianTN(userBean.uid, chargeValue, listener);
	        }
	    });
	}
	
	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			int resultType = 1;
			try {
				String ret = (String) msg.obj;

				switch (msg.what) {
				case AlixId.RQF_PAY: {
					ViewUtil.dismiss(showLoading);
					// �����׽��
					try {
						// ��ȡ����״̬�룬����״̬������ο��ĵ�
						String tradeStatus = "resultStatus={";
						int imemoStart = ret.indexOf("resultStatus=");
						imemoStart += tradeStatus.length();
						int imemoEnd = ret.indexOf("};memo=");
						tradeStatus = ret.substring(imemoStart, imemoEnd);

						// ����ǩ֪ͨ
						ResultChecker resultChecker = new ResultChecker(ret);
						int retVal = resultChecker.checkSign();
						// ��ǩʧ��
						if (retVal == ResultChecker.RESULT_CHECK_SIGN_FAILED) {
							resultType = 2;
//							ViewUtil.showToast(getResources().getString(R.string.check_sign_failed), act.getParent());
						} else {// ��ǩ�ɹ�����ǩ�ɹ������жϽ���״̬��
							if (tradeStatus.equals("9000")){
								resultType = 1;
								// �жϽ���״̬�룬ֻ��9000��ʾ���׳ɹ�
//								ViewUtil.showToast("֧���ɹ�������״̬�룺" + tradeStatus, act.getParent());
							}else{
								resultType = 2;
//								ViewUtil.showToast("֧��ʧ�ܡ�����״̬��:" + tradeStatus, act.getParent());
							}
						}

					} catch (Exception e) {
						resultType = 3;
						e.printStackTrace();
//						ViewUtil.showToast(ret.substring(ret.indexOf("{") + 1, ret.indexOf("}")), act.getParent());
					}
				}
					break;
				}

				super.handleMessage(msg);
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				CommunicationManager.getInstance().chargeForAlipay(userBean.uid, chargeValue2, resultType, getOutTradeNo(), 
						new CommunicationResultListener() {
							@Override
							public void resultListener(byte result, Object resultData) {
								if(result == CommunicationManager.SUCCEED){
									Message message = handler.obtainMessage();
									message.arg1 = CHARGE_ALIPAY;
									message.obj = resultData;
									handler.sendMessage(message);
								}else{
									/*new Handler().post(new Runnable() {
										
										@Override
										public void run() {
											zhifuBtn.setClickable(true);
										}
									});*/
									ViewUtil.dismiss(showLoading);
								}
							};
						});
			}
		}
	};
	
	private void chargeForPhone() {
		TextView accountName = (TextView) phoneView.findViewById(R.id.textView1);
		accountName.setText(userBean.account);
		final EditText hao = (EditText) phoneView.findViewById(R.id.editText1);
		final EditText pass = (EditText) phoneView.findViewById(R.id.editText2);
		RadioButton btn7 = ((RadioButton)phoneView. findViewById(R.id.button7));
		btn7.setChecked(true);
		chargeValue = StringUtil.getInt(btn7.getText().toString());
		RadioGroup valueGroup = (RadioGroup) phoneView.findViewById(R.id.charge_value_group);
		valueGroup.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			
			@Override
			public void onCheckedChanged(RadioGroup group, int checkedId) {
//				if(userBean.usertype == UserBean.USER_LIN_SHI){
//					ViewUtil.showLinShiDialog(act.getParent(), "��ʱ�û����ܲ���������ע��");
//					return;
//				}
				RadioButton btn = (RadioButton) findViewById(checkedId);
				String value = btn.getText().toString();
				chargeValue = StringUtil.getInt(value);
			}
		});
		zhifuBtn.setClickable(true);
		zhifuBtn.setText("����֧��");
		zhifuBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				if(userBean.usertype == UserBean.USER_LIN_SHI){
					ViewUtil.showLinShiDialog(act.getParent(), "��ʱ�û����ܲ���������ע��");
					return;
				}
				zhifuBtn.setClickable(false);
				String haoValue = hao.getText().toString();
				String passValue = pass.getText().toString();
				if(haoValue.length() == 0 || passValue.length() == 0){
					ViewUtil.showToast("���п��ź����벻��Ϊ��", getContext());
					zhifuBtn.setClickable(true);
					return;
				}
				showLoading = (ProgressDialog) ViewUtil.showLoading(((Activity)getContext()).getParent());
				showLoading.setMessage("���ڳ�ֵ�����Ժ�...");
				CommunicationManager.getInstance().chargeForPhone(userBean.uid, haoValue, passValue, chargeValue,
						new CommunicationResultListener() {

					@Override
					public void resultListener(byte result, Object resultData) {
						if(result == CommunicationManager.SUCCEED){
							Message message = handler.obtainMessage();
							message.arg1 = CHARGE_PHONE;
							message.obj = resultData;
							handler.sendMessage(message);
						}else{
							new Handler().post(new Runnable() {
								
								@Override
								public void run() {
									zhifuBtn.setClickable(true);
								}
							});
							ViewUtil.dismiss(showLoading);
						}
					}
				});
			}
		});
	}
	
	public void updateUserInfor(){
		act.sendBroadcast(new Intent(MainActivity.UPDATE_USER_INFOR));
	}
	
	Handler handler = new Handler(new Callback() {

		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.arg1) {
			case CHARGE_ALIPAY:
				ChargeBean bean3 = (ChargeBean) msg.obj;
				if(bean3.responsecode == 10){
					ViewUtil.showChargeDialog(((Activity)getContext()), bean3, "֧����");
				}else{
					ViewUtil.showBackDialog(((Activity)getContext()).getParent(), false, bean3.msg);
				}
				ViewUtil.dismiss(showLoading);
				zhifuBtn.setClickable(true);
				updateUserInfor();
				break;
			case CHARGE_PHONE:
				ChargeBean bean = (ChargeBean) msg.obj;
				if(bean.responsecode == 10){
					ViewUtil.showChargeDialog(((Activity)getContext()), bean, "�ֻ���");
				}else{
					ViewUtil.showBackDialog(((Activity)getContext()).getParent(), false, bean.msg);
				}
				ViewUtil.dismiss(showLoading);
				zhifuBtn.setClickable(true);
				updateUserInfor();
				break;
			case CHARGE_YINLIAN_SUCCESS:
				ChargeBean bean2 = (ChargeBean) msg.obj;
				if(bean2.responsecode == 10){
					ViewUtil.showChargeDialog(((Activity)getContext()), bean2, "����");
				}else{
					ViewUtil.showBackDialog(((Activity)getContext()).getParent(), false, bean2.msg);
				}
				ViewUtil.dismiss(showLoading);
				zhifuBtn.setClickable(true);
				updateUserInfor();
				break;
			case CHARGE_YINLIAN:
				ViewUtil.dismiss(mLoadingDialog);
		        zhifuBtn.setClickable(true);
		        ChargeBean responseBean = (ChargeBean) msg.obj;
		        tn = responseBean.tn;
		        if (tn.length() == 0) {
		        	ViewUtil.showDialog(act.getParent(), "������ʾ", "��������ʧ��,������!");
		        } else {
		            /************************************************* 
		             * 
		             *  ����2��ͨ����������������֧����� 
		             *  
		             ************************************************/
		            // mMode�������ͣ�
		            // 0 - ����������ʽ����
		            // 1 - �����������Ի���
		            int ret = UPPayAssistEx.startPay(MainActivity.intance, null, null, tn, mMode);
		            if (ret == PLUGIN_NEED_UPGRADE || ret == PLUGIN_NOT_INSTALLED) {
		                // ��Ҫ���°�װ�ؼ�
		                Log.e(LOG_TAG, " plugin not found or need upgrade!!!");
		                AlertDialog.Builder builder = new AlertDialog.Builder(act.getParent());
		                builder.setTitle("��ʾ");
		                builder.setMessage("��ɹ�����Ҫ��װ����֧���ؼ����Ƿ�װ��");

		                builder.setNegativeButton("ȷ��", new DialogInterface.OnClickListener() {
		                            @Override
		                            public void onClick(DialogInterface dialog, int which) {
		                                dialog.dismiss();
		                                UPPayAssistEx.installUPPayPlugin(act);
		                            }
		                        });
		                builder.setPositiveButton("ȡ��", new DialogInterface.OnClickListener() {

		                            @Override
		                            public void onClick(DialogInterface dialog, int which) {
		                                dialog.dismiss();
		                            }
		                        });
		                builder.create().show();
		            }
		        }
				break;
			}
			return false;
		}
	
	});
	
	private OnActivityResultListener onActivityResultListener = new OnActivityResultListener() {
		
		@Override
		public void onActivityResult(int requestCode, int resultCode, Intent data) {
			if (data == null) {
	            return;
	        }
			zhifuBtn.setClickable(false);
			String str = data.getExtras().getString("pay_result");
			int code = 0;
		    if (str.equalsIgnoreCase("success")) {
		        code = 1;
		    } else if (str.equalsIgnoreCase("fail")) {
		    	code = 2;
		    } else if (str.equalsIgnoreCase("cancel")) {
		    	code = 3;
		    }
		    showLoading = (ProgressDialog) ViewUtil.showLoading(((Activity)getContext()).getParent());
			showLoading.setMessage("���ڳ�ֵ�����Ժ�...");
			showLoading.setCancelable(false);
			CommunicationManager.getInstance().updateGold(userBean.uid, code, tn, new CommunicationResultListener() {
		            @Override
		            public void resultListener(byte result, Object resultData) {
						if(result == CommunicationManager.SUCCEED){
							Message message = handler.obtainMessage();
							message.arg1 = CHARGE_YINLIAN_SUCCESS;
							message.obj = resultData;
							handler.sendMessage(message);
						}else{
							new Handler().post(new Runnable() {
									
								@Override
								public void run() {
									zhifuBtn.setClickable(true);
								}
							});
						    ViewUtil.dismiss(mLoadingDialog);
						}
		            }
				});
		}
	};
	
	public int getChargeFlag() {
		return chargeFlag;
	}

	public void setChargeFlag(int chargeFlag) {
		this.chargeFlag = chargeFlag;
	}
	
	private CommunicationResultListener listener = new CommunicationResultListener() {
		@Override
		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED){
				Message msg = handler.obtainMessage();
		        msg.obj = resultData;
		        msg.arg1 = CHARGE_YINLIAN;
		        handler.sendMessage(msg);
			}else{
				ViewUtil.dismiss(mLoadingDialog);
				new Handler().post(new Runnable() {
					
					@Override
					public void run() {
						zhifuBtn.setClickable(true);						
					}
				});
			}
		}
	};

	public static class AlixOnCancelListener implements DialogInterface.OnCancelListener {
		Activity mcontext;

		public AlixOnCancelListener(Activity context) {
			mcontext = context;
		}

		public void onCancel(DialogInterface dialog) {
			mcontext.onKeyDown(KeyEvent.KEYCODE_BACK, null);
		}
	}

}
