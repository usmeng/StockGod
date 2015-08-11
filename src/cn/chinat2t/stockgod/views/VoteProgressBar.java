package cn.chinat2t.stockgod.views;

import java.util.Timer;
import java.util.TimerTask;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ProgressBar;

public class VoteProgressBar extends ProgressBar {
	
	private boolean isStart;
	private boolean isStop;
	private int targetPre;
	private int startPre;
	private VoteProgressBar vpb;
	private final int time_slice=1200;
	private int count;
	private int gap;
	private int useTime=0;
	private Timer timer;

	public VoteProgressBar(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
	}

	public VoteProgressBar(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	public VoteProgressBar(Context context) {
		super(context);
	}
	/**
	 * å¼?§‹æ»šåŠ¨
	 * @param startPre	å¼?§‹çš„ç™¾åˆ†æ¯”å€?	 * @param targetPre ç›®æ ‡çš„ç™¾åˆ†æ¯”å€?	 */
	public void startRoll(int startPre,int targetPre){
		
		TimerTask timerTask=new TimerTask() {
			@Override
			public void run() {
				useTime+=gap;
				System.out.println("useTime: "+useTime);
				if (useTime>time_slice) {
					timer.cancel();
				}
				incrementProgressBy(1);
			}
		};
		
		gap=time_slice/targetPre;
		
		timer = new Timer(true);
		timer.schedule(timerTask,0, gap);
		
	}

}
