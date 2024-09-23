package model;

public class TransactionDetail {
	private String trID, hoodieID;
	private int quantity;
	public TransactionDetail(String trID, String hoodieID, int quantity) {
		super();
		this.trID = trID;
		this.hoodieID = hoodieID;
		this.quantity = quantity;
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
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
}
