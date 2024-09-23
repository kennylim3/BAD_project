package model;

public class Cart {
	private String userid, hoodieid, hoodiename;
	private int qty;
	private double totalprice;
	public Cart(String userid, String hoodieid, String hoodiename, int qty, double totalprice) {
		super();
		this.userid = userid;
		this.hoodieid = hoodieid;
		this.hoodiename = hoodiename;
		this.qty = qty;
		this.totalprice = totalprice;
	}
	public String getUserid() {
		return userid;
	}
	public void setUserid(String userid) {
		this.userid = userid;
	}
	public String getHoodieid() {
		return hoodieid;
	}
	public void setHoodieid(String hoodieid) {
		this.hoodieid = hoodieid;
	}
	public String getHoodiename() {
		return hoodiename;
	}
	public void setHoodiename(String hoodiename) {
		this.hoodiename = hoodiename;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public double getTotalprice() {
		return totalprice;
	}
	public void setTotalprice(double totalprice) {
		this.totalprice = totalprice;
	}
}
