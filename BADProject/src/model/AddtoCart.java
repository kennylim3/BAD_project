package model;

public class AddtoCart {
	private String userID, hoodieID;
	private int qty;
	public AddtoCart(String userID, String hoodieID, int qty) {
		super();
		this.userID = userID;
		this.hoodieID = hoodieID;
		this.qty = qty;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getHoodieID() {
		return hoodieID;
	}
	public void setHoodieID(String hoodieID) {
		this.hoodieID = hoodieID;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
}
