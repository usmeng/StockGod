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
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import cn.chinat2t.stockgod.MainActivity;
import cn.chinat2t.stockgod.R;
import cn.chinat2t.stockgod.bean.RoomBean;
import cn.chinat2t.stockgod.bean.UserBean;
import cn.chinat2t.stockgod.http.CommunicationManager;
import cn.chinat2t.stockgod.http.CommunicationResultListener;
import cn.chinat2t.stockgod.utils.HttpUtil;
import cn.chinat2t.stockgod.utils.ViewUtil;

public class CompetitiveActivity extends Activity implements OnClickListener{

	private static final int DIALOG_CHONGZHI = 0;
	private Intent intent = null;
	private long clickTime = 0;
	private TextView roomGold1;
	private TextView roomGold2;
	private TextView roomGold3;
	private TextView roomGold4;
	private List<TextView> listRoom = new ArrayList<TextView>();
	List<RoomBean> rooms = new ArrayList<RoomBean>();
	private Dialog showLoading;
	private UserBean userBean;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		setContentView(R.layout.competitive_layout);
		userBean = UserBean.getInstance();
		initViews();
	}

	@Override
	protected void onResume() {
		super.onResume();
		MainActivity.setTitleName("炒股竞技");
	}
	
	private void initViews(){
		
		findViewById(R.id.competitive_mall).setOnClickListener(this);
		findViewById(R.id.competitive_stock).setOnClickListener(this);
		findViewById(R.id.competitive_charge).setOnClickListener(this);
		findViewById(R.id.competitive_exchange).setOnClickListener(this);
		findViewById(R.id.competitive_account).setOnClickListener(this);
		findViewById(R.id.competitive_self).setOnClickListener(this);
		
		findViewById(R.id.competitive_room1).setOnClickListener(this);
		findViewById(R.id.competitive_room2).setOnClickListener(this);
		findViewById(R.id.competitive_room3).setOnClickListener(this);
		findViewById(R.id.competitive_room4).setOnClickListener(this);
		
		roomGold1 = (TextView) findViewById(R.id.room1_gold_amount);
		roomGold2 = (TextView) findViewById(R.id.room2_gold_amount);
		roomGold3 = (TextView) findViewById(R.id.room3_gold_amount);
		roomGold4 = (TextView) findViewById(R.id.room4_gold_amount);
		
		listRoom.add(roomGold1);
		listRoom.add(roomGold2);
		listRoom.add(roomGold3);
		listRoom.add(roomGold4);
		showLoading = ViewUtil.showLoading(this.getParent());
		showLoading.setCancelable(false);
		CommunicationManager.getInstance().getGoldRoom(listener);
	}
	
	CommunicationResultListener listener = new CommunicationResultListener() {
		
		@Override
		public void resultListener(byte result, Object resultData) {
			super.resultListener(result, resultData);
			switch (result) {
			case CommunicationManager.FAIL:
				ViewUtil.dismiss(showLoading);
//				ViewUtil.showToast((String)resultData, CompetitiveActivity.this);
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
			if (msg.arg1 == 0) {
				rooms = (List<RoomBean>) msg.obj;
				for (int i = 0; i < rooms.size(); i++) {
					RoomBean bean = rooms.get(i);
					if(i == 0){
						listRoom.get(i).setText("");
					}else{
						listRoom.get(i).setText(bean.value + "");
					}
				}
			}
			ViewUtil.dismiss(showLoading);
			return false;
		}
	});

	@Override
	public void onClick(View v) {
		userBean = UserBean.getInstance();
		long time = System.currentTimeMillis();
		switch (v.getId()) {
		case R.id.competitive_mall:
			if(time - clickTime > 1000){
				clickTime = time;
				/*if(userBean.usertype == UserBean.USER_LIN_SHI){
					ViewUtil.showLinShiDialog(getParent(), "临时用户不能操作，请先注册");
					return;
				}*/
				intent = new Intent(this, MallActivity.class);
				CompetitiveGroup.group.switchActivity("MallActivity", intent, -1,-1);
			}
			break;
		case R.id.competitive_stock:
			if(time - clickTime > 1000){
				clickTime = time;
				intent = new Intent(this, SimulationActivity.class);
				CompetitiveGroup.group.switchActivity("SimulationActivity", intent, -1,-1);
			}
			break;
		case R.id.competitive_charge:
			if(time - clickTime > 1000){
				clickTime = time;
				/*if(userBean.usertype == UserBean.USER_LIN_SHI){
					ViewUtil.showLinShiDialog(getParent(), "临时用户不能操作，请先注册");
					return;
				}*/
				intent = new Intent(this, MallActivity.class);
				intent.putExtra("flag", 1);
				CompetitiveGroup.group.switchActivity("MallActivity", intent, -1,-1);
			}
			break;
		case R.id.competitive_exchange:
			if(time - clickTime > 1000){
				clickTime = time;
				/*if(userBean.usertype == UserBean.USER_LIN_SHI){
					ViewUtil.showLinShiDialog(getParent(), "临时用户不能操作，请先注册");
					return;
				}*/
				intent = new Intent(this, MallActivity.class);
				intent.putExtra("flag", 2);
				CompetitiveGroup.group.switchActivity("MallActivity", intent, -1,-1);
			}
			break;
		case R.id.competitive_account:
			if(time - clickTime > 1000){
				clickTime = time;
				if(userBean.usertype == UserBean.USER_LIN_SHI){
					ViewUtil.showLinShiDialog(getParent(), "临时用户不能操作，请先注册");
					return;
				}
				intent = new Intent(this, SimulationActivity.class);
				intent.putExtra("flag", 1);
				CompetitiveGroup.group.switchActivity("SimulationActivity", intent, -1,-1);
			}
			break;
		case R.id.competitive_self:
			if(time - clickTime > 1000){
				clickTime = time;
				if(userBean.usertype == UserBean.USER_LIN_SHI){
					ViewUtil.showLinShiDialog(getParent(), "临时用户不能操作，请先注册");
					return;
				}
				intent = new Intent(this, SimulationActivity.class);
				intent.putExtra("flag", 2);
				CompetitiveGroup.group.switchActivity("SimulationActivity", intent, -1,-1);
			}
			break;
		case R.id.competitive_room1:
			if(!HttpUtil.isConnection(this)){
				ViewUtil.showToast("网络有问题", this);
				return;
			}
//			if(userBean.fund < rooms.get(0).value){
//				ViewUtil.showToast("您的资金不足哦~", this);
//				return;
//			}
			if(time - clickTime > 1000){
				clickTime = time;
				intent = new Intent(this, RoomActivity.class);
//				intent.putExtra("flag", 1);
				if(rooms != null && rooms.size() >= 1)
				intent.putExtra("saizhi", rooms.get(0));
				CompetitiveGroup.group.switchActivity("RoomActivity", intent,-1,-1);
			}
			break;
		case R.id.competitive_room2:
			if(!HttpUtil.isConnection(this)){
				ViewUtil.showToast("网络有问题", this);
				return;
			}
			if(rooms != null && rooms.size() >= 2)
			if(userBean.gold < rooms.get(1).value && userBean.usertype != UserBean.USER_LIN_SHI){
				showDialog(DIALOG_CHONGZHI);
				return;
			}
			if(time - clickTime > 1000){
				clickTime = time;
				intent = new Intent(this, RoomActivity.class);
//				intent.putExtra("flag", 2);
				if(rooms != null && rooms.size() >= 2)
				intent.putExtra("saizhi", rooms.get(1));
				CompetitiveGroup.group.switchActivity("RoomActivity", intent, -1,-1);
			}
			break;
		case R.id.competitive_room3:
			if(!HttpUtil.isConnection(this)){
				ViewUtil.showToast("网络有问题", this);
				return;
			}
			if(rooms != null && rooms.size() >= 3)
			if(userBean.gold < rooms.get(2).value  && userBean.usertype != UserBean.USER_LIN_SHI){
				showDialog(DIALOG_CHONGZHI);
				return;
			}
			if(time - clickTime > 1000){
				clickTime = time;
				intent = new Intent(this, RoomActivity.class);
//				intent.putExtra("flag", 3);
				if(rooms != null && rooms.size() >= 3)
				intent.putExtra("saizhi", rooms.get(2));
				CompetitiveGroup.group.switchActivity("RoomActivity", intent, -1,-1);
			}
			break;
		case R.id.competitive_room4:
			if(!HttpUtil.isConnection(this)){
				ViewUtil.showToast("网络有问题", this);
				return;
			}
			if(rooms != null && rooms.size() >= 4)
			if(userBean.gold < rooms.get(3).value  && userBean.usertype != UserBean.USER_LIN_SHI){
				showDialog(DIALOG_CHONGZHI);
				return;
			}
			if(time - clickTime > 1000){
				clickTime = time;
				intent = new Intent(this, RoomActivity.class);
//				intent.putExtra("flag", 4);
				if(rooms != null && rooms.size() >= 4)
				intent.putExtra("saizhi", rooms.get(3));
				CompetitiveGroup.group.switchActivity("RoomActivity", intent, -1,-1);
			}
			break;
		default:
			break;
		}
	}

		
	private Dialog dialog;
	@Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
        case DIALOG_CHONGZHI:
   			View view = LayoutInflater.from(this).inflate(R.layout.guess_dialog_munu, null);
   			TextView tx1 = (TextView) view.findViewById(R.id.textView1);
   			view.findViewById(R.id.textView2).setVisibility(View.GONE);
   			Button button = (Button) view.findViewById(R.id.guess_confirm);
   			Button chongzhi = (Button) view.findViewById(R.id.guess_chongzhi);
   			chongzhi.setVisibility(View.VISIBLE);
   			chongzhi.setText("取消");
   			chongzhi.setOnClickListener(new OnClickListener() {

				@Override
   				public void onClick(View v) {
   					dialog.cancel();
   				}
   			});
   			button.setOnClickListener(new OnClickListener() {
   					
   				@Override
   				public void onClick(View v) {
   					dialog.cancel();
   					Intent intent = new Intent(CompetitiveActivity.this, MallActivity.class);
   					intent.putExtra("flag", 1);
   					CompetitiveGroup.getInstance().switchActivity("MallActivity", intent, -1, -1);
   				}
   			});
   			button.setText("充值");
   			tx1.setText("体验金币房间的刺激赛制，请速到商城充值");
   			
            dialog = new Dialog(getParent(), R.style.selectorDialog);
            dialog.setContentView(view);
            dialog.show();
            break;
        }
        return dialog;
    }
	
	@Override
	public void onBackPressed() {
		CompetitiveGroup.getInstance().back();
	}
	
	@Override
	public void onContentChanged() {
		MainActivity.setTitleName("炒股竞技");
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		Toast.makeText(this, "the result of CompetitiveActivity", 0).show();
	}
}
