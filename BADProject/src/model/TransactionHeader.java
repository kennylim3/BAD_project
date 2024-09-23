package model;

public class TransactionHeader {
	private String trID, userID;

	public TransactionHeader(String trID, String userID) {
		super();
		this.trID = trID;
		this.userID = userID;
	}

	public String getTrID() {
		return trID;
	}

	public void setTrID(String trID) {
		this.trID = trID;
	}

	public String getUserID() {
		return userID;
	}

	public void setUserID(String userID) {
		this.userID = userID;
	}
}
