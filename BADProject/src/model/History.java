package model;

public class History {
	private String trID, hoodieID, hoodieName;
	private int qty;
	private double totalPrice;
	public History(String trID, String hoodieID, String hoodieName, int qty, double totalPrice) {
		super();
		this.trID = trID;
		this.hoodieID = hoodieID;
		this.hoodieName = hoodieName;
		this.qty = qty;
		this.totalPrice = totalPrice;
	}
	public String getTrID() {
		return trID;
	}
	public void setTrID(String trID) {
		this.trID = trID;
	}
	public String getHoodieID() {
		return hoodieID;
	}
	public void setHoodieID(String hoodieID) {
		this.hoodieID = hoodieID;
	}
	public String getHoodieName() {
		return hoodieName;
	}
	public void setHoodieName(String hoodieName) {
		this.hoodieName = hoodieName;
	}
	public int getQty() {
		return qty;
	}
	public void setQty(int qty) {
		this.qty = qty;
	}
	public double getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(double totalPrice) {
		this.totalPrice = totalPrice;
	}
}
