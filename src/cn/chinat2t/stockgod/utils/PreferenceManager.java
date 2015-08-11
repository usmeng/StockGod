package cn.chinat2t.stockgod.utils;

import java.util.Date;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.preference.Preference;

public class PreferenceManager extends Preference{

	public static final String USER_PARAM = "user_param";
	public static final String name_param = "name_param";
	public static final String uid_param = "uid_param";
	public static final String pass_param = "pass_param";
	public static final String pass_second = "pass_second";
	public static final String type_param = "type_param";
	public static final String msg_param = "msg_param";
	public static final String CHECK_REMEBER = "CHECK_REMEBER";
	public static final String LOGIN_AUTO = "LOGIN_AUTO";
	
	private static PreferenceManager manager;
	private static SharedPreferences preferenceManager;
	private Editor edit;
			
	private PreferenceManager(Context context) {
		super(context);
		preferenceManager = context.getSharedPreferences(USER_PARAM, Context.MODE_PRIVATE);
		edit = preferenceManager.edit();
	}
	
	public static PreferenceManager getInstance(Context context){
		if(manager == null){
			manager = new PreferenceManager(context);
		}
		return manager;
	}
	
	public String getAccount() {
		return preferenceManager.getString(name_param, "");
	}

	public void putAccount(String name) {
		edit.putString(name_param, name).commit();
	}

	/*public int getUid() {
		return preferenceManager.getInt(uid_param, 0);
	}

	public void putUid(int uid) {
		edit.putInt(uid_param, uid).commit();
	}*/

	public String getPass() {
		return preferenceManager.getString(pass_param, "");
	}

	public void putPass(String pass) {
		edit.putString(pass_param, pass).commit();
	}
	
	public String getPassSecond() {
		return preferenceManager.getString(pass_second, "");
	}

	public void putPassMD5(String pass) {
		edit.putString(pass_second, pass).commit();
	}

	/*public int getType() {
		return preferenceManager.getInt(type_param, 0);
	}

	public void putType(int type) {
		edit.putInt(type_param, type).commit();
	}*/

	public String getMsg() {
		return preferenceManager.getString(msg_param, "");
	}

	public void putMsg(String msg) {
		edit.putString(msg_param, msg).commit();
	}
	
	public Boolean isRemember() {
		return preferenceManager.getBoolean(CHECK_REMEBER, true);
	}

	public void putRemember(boolean msg) {
		edit.putBoolean(CHECK_REMEBER, msg).commit();
	}
	
	public boolean isLoginAuto() {
		return preferenceManager.getBoolean(LOGIN_AUTO, false);
	}

	public void putLoginAuto(boolean msg) {
		edit.putBoolean(LOGIN_AUTO, msg).commit();
	}

	public void putRegisteTime(String zhucetime) {
		edit.putString("REGIST_TIME", zhucetime).commit();
	}
	
	public String getRegisteTime() {
		return preferenceManager.getString("REGIST_TIME", "20001010");
	}
	
	public void putUpLoginTime(String zhucetime) {
		edit.putString("UPLOGIN_TIME", zhucetime).commit();
	}
	
	public String getUpLoginTime() {
		return preferenceManager.getString("UPLOGIN_TIME", new Date().toGMTString());
	}
	
	public void setFirstUse(boolean first) {
		edit.putBoolean("FIRST_TIME_USR", first).commit();
	}
	
	public boolean isFirstUse() {
		return preferenceManager.getBoolean("FIRST_TIME_USR", false);
	}
	
	public String getEmail(){
		return preferenceManager.getString("GET_EMAIL", "");
	}
	
	public void putEmail(String email){
		edit.putString("GET_EMAIL", email).commit();
	}
}
