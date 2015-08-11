package cn.chinat2t.stockgod.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.TextView;
import cn.chinat2t.stockgod.MainActivity;
import cn.chinat2t.stockgod.R;
import cn.chinat2t.stockgod.bean.ChooseJuBean;
import cn.chinat2t.stockgod.bean.GuessJuBean;
import cn.chinat2t.stockgod.bean.RoomBean;
import cn.chinat2t.stockgod.bean.SaiZhiBean;
import cn.chinat2t.stockgod.bean.UserBean;
import cn.chinat2t.stockgod.http.CommunicationManager;
import cn.chinat2t.stockgod.http.CommunicationResultListener;
import cn.chinat2t.stockgod.utils.HttpUtil;
import cn.chinat2t.stockgod.utils.ViewUtil;

public class RoomActivity extends Activity implements OnClickListener{
	
	protected static final int GAME_XIANG = 2;
	protected static final int GAME_JU = 1;
	private Intent intent = null;
	private long clickTime = 0;
	private RoomBean roomBean;
	private View game1;
	private View game2;
	private View game3;
	private UserBean userBean;
	private List<SaiZhiBean> saiZhis = new ArrayList<SaiZhiBean>();
	private TextView roomInfor;
	private Dialog showLoading;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.room_layout);
		roomBean = (RoomBean) getIntent().getSerializableExtra("saizhi");
		game1 = findViewById(R.id.room_game_type1);
		game2 = findViewById(R.id.room_game_type2);
		game3 = findViewById(R.id.room_game_type3);
		roomInfor = (TextView) findViewById(R.id.room_info_text);
		initViews();
		userBean = UserBean.getInstance();
		
		showLoading = ViewUtil.showLoading(getParent());
		showLoading.setCancelable(false);
		if(userBean != null && roomBean != null){
			CommunicationManager.getInstance().getSaiZhiList(userBean.uid, roomBean.id, listener);
		}
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		initTitleName();
	}
	
	CommunicationResultListener listener = new CommunicationResultListener() {
		
		@Override
		public void resultListener(byte result, Object resultData) {
			super.resultListener(result, resultData);
			switch (result) {
			case CommunicationManager.FAIL:
				ViewUtil.dismiss(showLoading);
				break;

			case CommunicationManager.SUCCEED:
				Message msg = handler.obtainMessage();
				msg.obj = resultData;
				msg.arg1 = 0;
				handler.sendMessage(msg);
				break;
			}
		}
	};
	
	Handler handler = new Handler(new Callback() {
		
		@Override
		public boolean handleMessage(Message msg) {
			switch (msg.arg1) {
			case 0:
				saiZhis = (List<SaiZhiBean>) msg.obj;

				if (saiZhis.size() >= 1) {
					game1.setVisibility(View.VISIBLE);
				}
				if (saiZhis.size() >= 2) {
					game2.setVisibility(View.VISIBLE);
				}
				if (saiZhis.size() >= 3) {
					game3.setVisibility(View.VISIBLE);
				}
				break;

			case GAME_JU:
				if(msg.obj instanceof GuessJuBean){
					GuessJuBean guessStock = (GuessJuBean) msg.obj;
					if(guessStock.isplay == 1){
						ViewUtil.showDialog(getParent(), "本局你已参赛，请等待下局开始", null);
					} else {
						intent = new Intent(RoomActivity.this, GuessActivity.class);
						intent.putExtra("roomId", roomBean.id);
						intent.putExtra("saizhiID", saiZhis.get(0).id);
						CompetitiveGroup.getInstance().switchActivity("GuessActivity", intent, -1,-1);
					}
				}
				game1.setEnabled(true);
				break;
			case GAME_XIANG:
				if(msg.obj instanceof ChooseJuBean){
					ChooseJuBean gameBean = (ChooseJuBean) msg.obj;
					if(gameBean.isplay == 1){
						ViewUtil.showDialog(getParent(), "　本局你已参赛，请等待下局开始", null);
					}else{
						intent = new Intent(RoomActivity.this, ChooseActivity.class);
						intent.putExtra("roomId", roomBean.id);
						intent.putExtra("saizhiID", saiZhis.get(1).id);
						CompetitiveGroup.getInstance().switchActivity("ChooseActivity", intent, -1,-1);
					}
				}
				game2.setEnabled(true);
				break;
			}
			
			ViewUtil.dismiss(showLoading);
			return false;
		}
	});
	
	private void initTitleName() {
		MainActivity.setTitleName(roomBean.name);
		roomInfor.setText(roomBean.fangjianjieshao);
	}
	
	private void initViews(){
		game1.setOnClickListener(this);
		game2.setOnClickListener(this);
		game3.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		if(roomBean == null || saiZhis == null) return;
		if(!HttpUtil.isConnection(this)){
			ViewUtil.showToast("网络有问题哦~", this);
			return;
		}
		long time = System.currentTimeMillis();
		switch (v.getId()) {
		case R.id.room_game_type1:
			if(time - clickTime > 1000){
				clickTime = time;
				game1.setEnabled(false);
				CommunicationManager.getInstance().getGuessJu(userBean.uid, saiZhis.get(0).id, roomBean.id, gameJulistener);
			}
			break;
		case R.id.room_game_type2:
			if(time - clickTime > 1000){
				clickTime = time;
				game2.setEnabled(false);
				CommunicationManager.getInstance().getChooseJu(userBean.uid, saiZhis.get(1).id, roomBean.id, gameXianglistener);
			}
			break;
		case R.id.room_game_type3:
			if(time - clickTime > 1000){
				clickTime = time;
				if(userBean.usertype == UserBean.USER_LIN_SHI){
					ViewUtil.showLinShiDialog(getParent(), "您是临时用户不能参加比赛,请注册.");
					return;
				}
				intent = new Intent(this, FastFightActivity.class);
				intent.putExtra("roomId", roomBean.id);
				intent.putExtra("saizhiID", saiZhis.get(2).id);
				CompetitiveGroup.getInstance().switchActivity("FastFightActivity", intent, -1,-1);
			}
			break;
		}
	}
	
	private CommunicationResultListener gameXianglistener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			if(CommunicationManager.SUCCEED == result){
				Message message = handler.obtainMessage();
				message.arg1 = GAME_XIANG;
				message.what = result;
				message.obj = resultData;
				handler.sendMessage(message);
			} else {
				new Handler().post(new Runnable() {
					
					@Override
					public void run() {
						game2.setEnabled(true);
					}
				});
				ViewUtil.dismiss(showLoading);
			}
		};
	};
	
    private CommunicationResultListener gameJulistener = new CommunicationResultListener() {
		
		public void resultListener(byte result, Object resultData) {
			if(result == CommunicationManager.SUCCEED){
				Message message = handler.obtainMessage();
				message.arg1 = GAME_JU;
				message.obj = resultData;
				handler.sendMessage(message);
			}else{
				ViewUtil.dismiss(showLoading);
				new Handler().post(new Runnable() {
					
					@Override
					public void run() {
						game1.setEnabled(true);
					}
				});
			}
		};
	};

	@Override
	public void onBackPressed() {
		CompetitiveGroup.getInstance().back();
	}
	
	@Override
	public void onContentChanged() {
		if(roomBean != null)
			MainActivity.setTitleName(roomBean.name);
	}
}
