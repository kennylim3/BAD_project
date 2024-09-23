package view;

import java.util.List;

import database.Connect;
import javafx.application.Platform;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Stage;
import jfxtras.labs.scene.control.window.Window;
import model.Cart;
import model.LoggedInUser;
import model.TransactionDetail;
import model.TransactionHeader;
import model.User;

public class CartPage {
	Stage stage, popupStage;
	Scene scene, popupScene;
	BorderPane bp;
	GridPane gp, gp2, gp3, gp4, gp5;
	Label title, detailTitle, contactTitle, priceTitle, caption, detailID, detailName, 
	detailPrice, detailQty, detailTotal, detailEmail, detailPhone, detailAddress;
	Button removeBtn, checkoutBtn;
	MenuBar menuBar;
	Menu account, user, admin;
	MenuItem log, home, cart, history, editProduct;
	TableView<Cart> hoodieTableView;
	TableColumn<Cart, String> idColumn;
	TableColumn<Cart, String> nameColumn;
	TableColumn<Cart, Integer> qtyColumn;
	TableColumn<Cart, Double> totalPriceColumn;
	String userID, userRole, userName, userEmail, userPhone, userAddress, totalSumPrice;
	double sum, totalSum;
	VBox vb;
	HBox hb;
	Label confirmationLabel;
	Button makePaymentBtn, cancelBtn;
	Window window;
	StackPane root;
	
	private void checkLoggedInUser() {
		User currentUser = LoggedInUser.getCurrentUser();
		if (currentUser != null) {
		    userID = currentUser.getUserID();
		    userRole = currentUser.getRole();
		    userName = currentUser.getUsername();
		    userEmail = currentUser.getEmail();
		    userPhone = currentUser.getPhone();
		    userAddress = currentUser.getAddress();
		}
	}
	
	private void initializeMenu() {
		menuBar = new MenuBar();
		account = new Menu("Account");
		user = new Menu("User");
		admin = new Menu("Admin");
		log = new MenuItem("Logout");
		home = new MenuItem("Home");
		cart = new MenuItem("Cart");
		history = new MenuItem("History");
		editProduct = new MenuItem("Edit Product");
		menuBar.getMenus().addAll(account, user, admin);
		account.getItems().add(log);
		user.getItems().addAll(home, cart, history);
		admin.getItems().add(editProduct);
		
		if (userRole.equals("Admin")) {
			user.setVisible(false);
			admin.setVisible(true);
		}else if (userRole.equals("User")) {
			user.setVisible(true);
			admin.setVisible(false);
		}
	}
	
	private void initialize() {
		bp = new BorderPane();
		gp = new GridPane();
		gp2 = new GridPane();
		gp3 = new GridPane();
		gp4 = new GridPane();
		gp5 = new GridPane();
		vb = new VBox();
		hb = new HBox();
		title = new Label(userName + "'s Cart");
		title.setFont(Font.font("Verdana",FontWeight.SEMI_BOLD, FontPosture.ITALIC,30));
		detailTitle = new Label("Hoodie's Detail");
		detailTitle.setFont(Font.font("Verdana",FontWeight.BOLD, FontPosture.ITALIC,18));
		contactTitle = new Label("Contact Information");
		contactTitle.setFont(Font.font("Verdana",FontWeight.BOLD, FontPosture.ITALIC,18));
		caption = new Label("Select an item from the table...");
		detailID = new Label();
		detailName = new Label();
		detailPrice = new Label();
		detailQty = new Label();
		detailTotal = new Label();
		detailEmail = new Label("Email     : " + userEmail);
		detailPhone = new Label("Phone     : " + userPhone);
		detailAddress = new Label("Address   : " + userAddress);
		removeBtn = new Button("Remove from cart");
		checkoutBtn = new Button("Checkout");
		confirmationLabel = new Label("Are you sure, you want to make the payment?");
		makePaymentBtn = new Button("Make Payment");
        cancelBtn = new Button("Cancel");
        confirmationLabel.setFont(Font.font(confirmationLabel.getFont().getFamily(), FontWeight.BOLD, FontPosture.REGULAR, confirmationLabel.getFont().getSize()));
        makePaymentBtn.setFont(Font.font(makePaymentBtn.getFont().getFamily(), FontWeight.BOLD, FontPosture.REGULAR, makePaymentBtn.getFont().getSize()));
        cancelBtn.setFont(Font.font(cancelBtn.getFont().getFamily(), FontWeight.BOLD, FontPosture.REGULAR, cancelBtn.getFont().getSize()));
		
		hoodieTableView = new TableView<>();
        idColumn = new TableColumn<>("Hoodie ID");
        nameColumn = new TableColumn<>("Hoodie Name");
        qtyColumn = new TableColumn<>("Quantity");
        totalPriceColumn = new TableColumn<>("Total Price");
        idColumn.setPrefWidth(90);
        nameColumn.setPrefWidth(90);
        qtyColumn.setPrefWidth(90);
        totalPriceColumn.setPrefWidth(90);
        
        idColumn.setCellValueFactory(new PropertyValueFactory<>("hoodieid"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("hoodiename"));
        qtyColumn.setCellValueFactory(new PropertyValueFactory<>("qty"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalprice"));
        
        hoodieTableView.getColumns().addAll(idColumn, nameColumn, qtyColumn, totalPriceColumn);
        
        Connect con = Connect.getInstance();
        List<Cart> carts = con.selectCart(userID);
        
        ObservableList<Cart> cartItems = FXCollections.observableArrayList(carts);
        hoodieTableView.setItems(cartItems);
        
        totalSum = calculateTotalSum();
        totalSumPrice = String.format("%." + 4 + "f", totalSum);
        priceTitle = new Label("Cart's Total price: " + totalSumPrice);
        priceTitle.setFont(Font.font("Verdana",FontWeight.BOLD, FontPosture.ITALIC,18));
        
        detailID.setVisible(false);
		detailName.setVisible(false);
		detailPrice.setVisible(false);
		detailQty.setVisible(false);
		detailTotal.setVisible(false);
		removeBtn.setVisible(false);
		caption.setVisible(true);
        hoodieTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                detailID.setText("Hoodie ID: " + newSelection.getHoodieid());
                detailName.setText("Name: " + newSelection.getHoodiename());
                detailPrice.setText("Price: " + String.valueOf(newSelection.getTotalprice()/newSelection.getQty()));
                detailQty.setText("Quantity: " + String.valueOf(newSelection.getQty()));
                detailTotal.setText("Total Price: " + String.valueOf(newSelection.getTotalprice()));
                detailTotal.setFont(Font.font(detailTotal.getFont().getFamily(), FontWeight.BOLD, FontPosture.REGULAR, detailTotal.getFont().getSize()));
                
                detailID.setVisible(true);
        		detailName.setVisible(true);
        		detailPrice.setVisible(true);
        		detailQty.setVisible(true);
        		detailTotal.setVisible(true);
        		removeBtn.setVisible(true);
        		caption.setVisible(false);
            }else {
            	detailID.setVisible(false);
        		detailName.setVisible(false);
        		detailPrice.setVisible(false);
        		detailQty.setVisible(false);
        		detailTotal.setVisible(false);
        		removeBtn.setVisible(false);
        		caption.setVisible(true);
			}
        });
        
        gp3.add(detailID, 0, 0);
        gp3.add(caption, 0, 0);
        gp3.add(detailName, 0, 1);
        gp3.add(detailPrice, 0, 2);
        gp3.add(detailQty, 0, 3);
        gp3.add(detailTotal, 0, 4);
        gp3.add(removeBtn, 0, 5);
        gp3.setVgap(15);
        
        gp4.add(detailEmail, 0, 0);
        gp4.add(detailPhone, 0, 1);
        gp4.add(detailAddress, 0, 2);
        gp4.setVgap(15);
        
        gp5.add(checkoutBtn, 0, 0);
        gp5.setAlignment(Pos.CENTER_RIGHT);
        
        gp2.add(detailTitle, 0, 0);
        gp2.add(gp3, 0, 1);
        gp2.add(contactTitle, 0, 2);
        gp2.add(gp4, 0, 3);
        gp2.add(priceTitle, 0, 4);
        gp2.add(gp5, 0, 5);
        gp2.setVgap(20);
        gp2.setPadding(new Insets(15));
		
		gp.add(title, 0, 0);
		gp.add(hoodieTableView, 0, 1);
		gp.add(gp2, 1, 1);
		
		gp.setVgap(20);
        gp.setHgap(20);
	    gp.setPadding(new Insets(30));
		bp.setTop(menuBar);
	    bp.setCenter(gp);
		scene = new Scene(bp, 800, 700);
	}
	
	private Double calculateTotalSum() {
	    sum = hoodieTableView.getItems().stream().mapToDouble(Cart::getTotalprice).sum();
	    return sum;
	}
	
	private void deleteHoodieFromCart() {
		Cart selectedHoodie = hoodieTableView.getSelectionModel().getSelectedItem();
		
		Connect con = Connect.getInstance();
		con.deleteFromCart(selectedHoodie.getUserid(), selectedHoodie.getHoodieid());
		new CartPage(stage);
	}
	
	private void showAlert(AlertType alertType, String title, String header, String content) {
	    Alert alert = new Alert(alertType);
	    alert.setTitle(title);
	    alert.setHeaderText(header);
	    alert.setContentText(content);
	    alert.showAndWait();
	}
	
	private void handling() {
		log.setOnAction(e->{
			try {
				new LoginPage().start(stage);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		home.setOnAction(e->{
			new HomePage(stage);
		});
		
		cart.setOnAction(e->{
			new CartPage(stage);
		});
		
		history.setOnAction(e->{
			new HistoryPage(stage);
		});
		
		removeBtn.setOnMouseClicked(e->{
			deleteHoodieFromCart();
		});
		
		checkoutBtn.setOnMouseClicked(e->{
			if (hoodieTableView.getItems().isEmpty()) {
				showAlert(AlertType.ERROR, "Error", "Error", "Cart is empty");
			}else {
				paymentConfirmationPopup();
			}
		});
		
		cancelBtn.setOnMouseClicked(e -> {
            popupStage.close();
            new CartPage(stage);
        });
		
		makePaymentBtn.setOnMouseClicked(e->{
			Connect con = Connect.getInstance();
			int rowCount = con.getRowCount("transactionheader");
			String trID = null;
			
			if (rowCount < 9) {
				trID = "TR00" + (rowCount+1);
			}else if (rowCount < 99) {
				trID = "TR0" + (rowCount+1);
			}else if (rowCount < 999) {
				trID = "TR" + (rowCount+1);
			}
			
			con.insertTransactionHeader("INSERT INTO transactionheader VALUES (?,?)", new TransactionHeader(trID, userID));
			
			for (Cart cart : hoodieTableView.getItems()) {
	            con.insertTransactionDetail("INSERT INTO transactiondetail VALUES (?, ?, ?)", 
	            		new TransactionDetail(trID, cart.getHoodieid(), cart.getQty()));
	        }
			
			con.deleteAfterCheckout(userID);
			
			popupStage.close();
			showAlert(AlertType.INFORMATION, "Success", "Message", userID + " successfully made a transaction");
			new CartPage(stage);
		});
	}
	
	private void paymentConfirmationPopup() {
        vb.setSpacing(30);

        vb.getChildren().add(confirmationLabel);

        hb.getChildren().addAll(makePaymentBtn, cancelBtn);
        vb.getChildren().add(hb);
        hb.setAlignment(Pos.CENTER);
        hb.setSpacing(20);
        
        window = new Window("Payment Confirmation");
        window.getContentPane().getChildren().add(vb);
        vb.setAlignment(Pos.CENTER);
        window.getContentPane().setPadding(new Insets(20));

        root = new StackPane(window);

        popupScene = new Scene(root, 400, 250);

        popupStage = new Stage();
        popupStage.setScene(popupScene);
        popupStage.initOwner(stage);
        popupStage.initModality(Modality.WINDOW_MODAL);
        popupStage.showAndWait();
    }

	
	public CartPage(Stage stage) {
		this.stage = stage;
		checkLoggedInUser();
		initializeMenu();
		initialize();
		handling();
		this.stage.setTitle("hO-Ohdie");
		this.stage.setResizable(false);
		this.stage.setScene(scene);
		this.stage.show();
	}
}
