package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import model.AddtoCart;
import model.Cart;
import model.History;
import model.Hoodie;
import model.TransactionDetail;
import model.TransactionHeader;
import model.User;

public class Connect {
	private final String username = "root";
	private final String password = "";
	private final String database = "ho-ohdie";
	private final String host = "localhost:3306";
	private final String connection = String.format("jdbc:mysql://%s/%s", host, database);
	
	private Connection con;
	private static PreparedStatement ps;
	
	public static Connect connect;
	public ResultSet rs;
	
	public static Connect getInstance() {
		if(connect == null) {
			connect = new Connect();
		}
		
		return connect;
	}
	
	private Connect() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(connection, username, password);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	public ResultSet selectData(String query) {
		try {
			ps = con.prepareStatement(query);
			rs = ps.executeQuery(query);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return rs;
	}
	
	public boolean isUsernameUnique(String username) {
	    String query = "SELECT COUNT(*) FROM user WHERE username = ?";
	    
	    try {
	        PreparedStatement checkUsername = con.prepareStatement(query);
	        checkUsername.setString(1, username);
	        ResultSet result = checkUsername.executeQuery();

	        if (result.next()) {
	            int count = result.getInt(1);
	            return count == 0;
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return false;
	}
	
	public int getRowCount(String tableName) {
	    String query = "SELECT COUNT(*) FROM " +tableName;

	    try {
	        Statement statement = con.createStatement();
	        ResultSet result = statement.executeQuery(query);

	        if (result.next()) {
	            return result.getInt(1);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return -1;
	}
	
	public int getLastID() {
		String query = "SELECT MAX(CAST(SUBSTRING(HoodieID, 3) AS SIGNED)) FROM hoodie";;
		
		try {
			PreparedStatement ps = con.prepareStatement(query);
			ResultSet result = ps.executeQuery(query);
		
			if (result.next()) {
				return result.getInt(1);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return -1;
	}
	
	public void insertUser(String query, User user) {
		try {
			ps = con.prepareStatement(query);
			
			ps.setString(1, user.getUserID());
			ps.setString(2, user.getEmail());
			ps.setString(3, user.getUsername());
			ps.setString(4, user.getPassword());
			ps.setString(5, user.getPhone());
			ps.setString(6, user.getAddress());
			ps.setString(7, user.getGender());
			ps.setString(8, user.getRole());
			
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Hoodie> selectHoodies() {
        List<Hoodie> hoodies = new ArrayList<>();

        try {
            String query = "SELECT hoodieID, hoodieName, hoodiePrice FROM hoodie";
            ps = con.prepareStatement(query);
            rs = ps.executeQuery();

            while (rs.next()) {
                String hoodieID = rs.getString("hoodieID");
                String hoodieName = rs.getString("hoodieName");
                double hoodiePrice = rs.getDouble("hoodiePrice");
                

                Hoodie hoodie = new Hoodie(hoodieID, hoodieName, hoodiePrice);
                hoodies.add(hoodie);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return hoodies;
    }
	
	public void updateHoodie(String hoodieId, double newPrice) {
		String query = "UPDATE hoodie SET hoodiePrice = ? WHERE hoodieID = ?";

	    try {
	        PreparedStatement updateStatement = con.prepareStatement(query);
	        updateStatement.setDouble(1, newPrice);
	        updateStatement.setString(2, hoodieId);
	        updateStatement.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void deleteHoodie(String hoodieId) {
		String query = "DELETE FROM hoodie WHERE HoodieID = ?";

	    try {
	        PreparedStatement updateStatement = con.prepareStatement(query);
	        updateStatement.setString(1, hoodieId);
	        updateStatement.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void insertHoodie(String query, Hoodie hoodie) {
		try {
			ps = con.prepareStatement(query);
			
			ps.setString(1, hoodie.getId());
			ps.setString(2, hoodie.getName());
			ps.setDouble(3, hoodie.getPrice());
			
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insertCart(String query, AddtoCart ac) {
		try {
			ps = con.prepareStatement(query);
			
			ps.setString(1, ac.getUserID());
			ps.setString(2, ac.getHoodieID());
			ps.setInt(3, ac.getQty());
			
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public List<Cart> selectCart(String userID) {
        List<Cart> cart = new ArrayList<>();

        try {
            String query = "SELECT c.HoodieID, HoodieName, Quantity, SUM(Quantity*HoodiePrice) AS 'TotalPrice'"
            		+ "FROM cart c JOIN hoodie h ON c.HoodieID = h.HoodieID "
            		+ "WHERE UserID = ? GROUP BY c.HoodieID, HoodieName, Quantity;";
            ps = con.prepareStatement(query);
            ps.setString(1, userID);
            rs = ps.executeQuery();

            while (rs.next()) {
            	String hoodieID = rs.getString("HoodieID");
                String hoodieName = rs.getString("HoodieName");
                int quantity = rs.getInt("Quantity");
                double totalPrice = rs.getDouble("TotalPrice");
                

                Cart insertCart = new Cart(userID, hoodieID, hoodieName, quantity, totalPrice);
                cart.add(insertCart);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return cart;
    }
	
	public void deleteFromCart(String userid, String hoodieid) {
		String query = "DELETE FROM cart WHERE UserID = ? AND HoodieID = ?";

	    try {
	        PreparedStatement updateStatement = con.prepareStatement(query);
	        updateStatement.setString(1, userid);
	        updateStatement.setString(2, hoodieid);
	        updateStatement.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public void insertTransactionHeader(String query, TransactionHeader th) {
		try {
			ps = con.prepareStatement(query);
			
			ps.setString(1, th.getTrID());
			ps.setString(2, th.getUserID());
			
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void insertTransactionDetail(String query, TransactionDetail td) {
		try {
			ps = con.prepareStatement(query);
			
			ps.setString(1, td.getTrID());
			ps.setString(2, td.getHoodieID());
			ps.setInt(3, td.getQuantity());
			
			ps.executeUpdate();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void deleteAfterCheckout(String userid) {
		String query = "DELETE FROM cart WHERE UserID = ?";

	    try {
	        PreparedStatement updateStatement = con.prepareStatement(query);
	        updateStatement.setString(1, userid);
	        updateStatement.executeUpdate();
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }
	}
	
	public List<TransactionHeader> selectTransactionHeader(String userID) {
	    List<TransactionHeader> th = new ArrayList<>();

	    try {
	        String query = "SELECT * FROM transactionheader WHERE userID = ?";
	        ps = con.prepareStatement(query);
	        ps.setString(1, userID);
	        rs = ps.executeQuery();

	        while (rs.next()) {
	            String trID = rs.getString("TransactionID");
	            String headerUserID = rs.getString("UserID");

	            TransactionHeader transactionHeader = new TransactionHeader(trID, headerUserID);
	            th.add(transactionHeader);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return th;
	}
	
	public List<History> selectTransactionDetail(String trID) {
	    List<History> history = new ArrayList<>();

	    try {
	        String query = "SELECT TransactionID, td.HoodieID, HoodieName, Quantity, SUM(Quantity*HoodiePrice) AS 'totalprice' "
	        		+ "FROM transactiondetail td JOIN hoodie h ON td.HoodieID = h.HoodieID "
	        		+ "WHERE TransactionID = ? "
	        		+ "GROUP BY TransactionID, HoodieID, HoodieName, Quantity";
	        ps = con.prepareStatement(query);
	        ps.setString(1, trID);
	        rs = ps.executeQuery();

	        while (rs.next()) {
	            String tranID = rs.getString("TransactionID");
	            String hoodieID = rs.getString("HoodieID");
	            String hoodieName = rs.getString("HoodieName");
	            int qty = rs.getInt("Quantity");
	            double totalPrice = rs.getDouble("totalprice");

	            History td = new History(tranID, hoodieID, hoodieName, qty, totalPrice);
	            history.add(td);
	        }
	    } catch (SQLException e) {
	        e.printStackTrace();
	    }

	    return history;
	}
}
