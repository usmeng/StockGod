package cn.chinat2t.stockgod.bean;

import java.util.List;

public class FightJuBean {
	
	public List<KlineData> klineList;
	public int id;//": "362",
	public int sid;//": "12",
	public int rid;//": "4",
	public double jlxs;//": "10",
	public double bl;//": "8",
	public double csxs;//": "13",
	public int yasuo;//": "90",
	public int csje;//": "10000",
	public String uids;//": "2,494"
	public int myUid;//": "494",
	public String myPic;//": null
	public String yourNickname;
	public int yourUid;//": "2",
	public String yourPic;//": null
	public int goldf;

	@Override
	public String toString() {
		return "FightJuBean [id=" + id + ", sid="
				+ sid + ", rid=" + rid + ", jlxs=" + jlxs + ", bl=" + bl
				+ ", csxs=" + csxs + ", yasuo=" + yasuo + ", csje=" + csje
				+ ", uids=" + uids + ", myUid=" + myUid + ", myPic=" + myPic
				+ ", yourUid=" + yourUid + ", yourPic=" + yourPic + "]";
	}
}
