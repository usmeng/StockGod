package cn.chinat2t.stockgod.utils;

import android.os.Handler;


public class TimerManager {
	
	private boolean isPause;
	private int timeSum;
	private int delayMillis;
	private boolean isUnlimit;
	
	private TimerListener timerListener;
	
	public void setTimerListener(TimerListener timerListener){
		this.timerListener = timerListener;
	}
	
	public interface TimerListener{
		void start();
		
		void finish();
		
		void pause();
		
		void update(int time);
	}
	
	public TimerManager() {}
	
	public TimerManager(int delayTime, int sumTime) {
		this.delayMillis = delayTime;
		this.timeSum = sumTime;
		relTime = 0;
	}
	
	public boolean isPause() {
		return isPause;
	}

	public void setPause(boolean isPause) {
		this.isPause = isPause;
	}

	public int getTimeSum() {
		return timeSum;
	}

	public void setTimeSum(int timeSum) {
		this.timeSum = timeSum;
	}

	public int getDelayMillis() {
		return delayMillis;
	}

	public void setDelayMillis(int delayMillis) {
		this.delayMillis = delayMillis;
	}

	private static int relTime;
	private Handler handler = new Handler();
	private Runnable runnable = new Runnable() {
		
		@Override
		public void run() {
			
			if (isUnlimit || relTime <= timeSum) {
				if (isPause) {
					timerListener.pause();
					return;
				}else{
					timerListener.update(relTime);
					handler.postDelayed(this, delayMillis);
				}
			} else {
				timerListener.finish();
			}
			relTime++;
		}
		
	};
	
	public void start(){
		if(timerListener != null){
			relTime = 0;
			handler.postDelayed(runnable, delayMillis);
			timerListener.start();
		}
	}
	
	public void pause(){
		this.isPause = true;
	}
	
	public void setUnLimit(boolean isUnlimit){
		this.isUnlimit = isUnlimit;
	}
	
	public void finish(){
		isUnlimit = false;
		relTime = timeSum;
	}
	
	public void reStart(){
		if(timerListener != null)
		handler.postDelayed(runnable, delayMillis);
	}
	
}
