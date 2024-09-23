package view;

import java.util.List;
import database.Connect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.*;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.*;
import model.AddtoCart;
import model.Hoodie;
import model.LoggedInUser;
import model.User;

public class HomePage {
	Stage stage;
	Scene scene;
	MenuBar menuBar;
	Menu account, user, admin;
	MenuItem log, home, cart, history, editProduct;
	GridPane gp, gp2;
	BorderPane bp;
	Label title, detailTitle, caption, id, name, price, qty, totalPrice;
	HBox hb;
	Spinner<Integer> qtySp;
	Button addBtn;
	String userID, userRole;
	double sumTotal;
	Hoodie selectedHoodie = null;
	
	private void checkLoggedInUser() {
		User currentUser = LoggedInUser.getCurrentUser();
		if (currentUser != null) {
		    userID = currentUser.getUserID();
		    userRole = currentUser.getRole();
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
		hb = new HBox();
		title = new Label("hO-Ohdie");
		detailTitle = new Label("Hoodie's Detail");
		caption = new Label("Select an item from the list...");
		title.setFont(Font.font("Verdana",FontWeight.SEMI_BOLD, FontPosture.ITALIC,40));
		detailTitle.setFont(Font.font("Verdana",FontWeight.BOLD, FontPosture.ITALIC,35));
		id = new Label("");
		name = new Label("");
		price = new Label("");
		qty = new Label("Quantity: ");
		totalPrice = new Label("");
		addBtn = new Button("Add to Cart");
        addBtn.setFont(Font.font(addBtn.getFont().getFamily(), FontWeight.BOLD, addBtn.getFont().getSize()));
        qtySp = new Spinner<>(1, 1000, 1);
        hb.getChildren().addAll(qtySp, totalPrice);
		
		ListView<String> hoodieListView = new ListView<>();
	    ObservableList<String> hoodieItems = FXCollections.observableArrayList();

	    Connect con = Connect.getInstance();
	    List<Hoodie> hoodies = con.selectHoodies();

	    for (Hoodie hoodie : hoodies) {
	        hoodieItems.add(hoodie.getId() + "        " + hoodie.getName());
	    }

	    hoodieListView.setItems(hoodieItems);
	    
	    hoodieListView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
	    	if (newValue != null) {
	            String[] parts = newValue.split("\\s+");
	            String selectedHoodieId = parts[0];

	            for (Hoodie hoodie : hoodies) {
	                if (hoodie.getId().equals(selectedHoodieId)) {
	                    selectedHoodie = hoodie;
	                    break;
	                }
	            }

	            if (selectedHoodie != null) {
	                caption.setVisible(false);
	                id.setVisible(true);
	                name.setVisible(true);
	                price.setVisible(true);
	                qty.setVisible(true);
	                hb.setVisible(true);
	                addBtn.setVisible(true);

	                id.setText("Hoodie ID: " + selectedHoodie.getId());
	                name.setText("Name: " + selectedHoodie.getName());
	                price.setText("Price: " + selectedHoodie.getPrice());
	                totalPrice.setText("Total Price: " + selectedHoodie.getPrice());
	                qtySp.valueProperty().addListener((observables, oldQty, newQty) -> {
	                	double total = newQty * selectedHoodie.getPrice();
	                	
	                	totalPrice.setText("Total Price: " + total);
	                });
	            }
	        }
	    });

	    
	    caption.setVisible(true);
	    id.setVisible(false);
	    name.setVisible(false);
	    price.setVisible(false);
	    qty.setVisible(false);
	    hb.setVisible(false);
	    addBtn.setVisible(false);
	    
	    hb.setSpacing(10);
	    gp2.add(detailTitle, 0, 2);
		gp2.add(caption, 0, 3);
		gp2.add(id, 0, 3);
		gp2.add(name, 0, 4);
		gp2.add(price, 0, 5);
		gp2.add(qty, 0, 6);
		gp2.add(hb, 0, 7);
		gp2.add(addBtn, 0, 8);
		gp2.setVgap(15);
		gp2.setPadding(new Insets(15));
	    gp.add(title, 0, 0);
	    gp.add(hoodieListView, 0, 1);
	    gp.add(gp2, 1, 1);
	    
        gp.setVgap(20);
        gp.setHgap(20);
	    gp.setPadding(new Insets(30));
        bp.setTop(menuBar);
        bp.setCenter(gp);
        
		scene = new Scene(bp, 800, 700);
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
		
		addBtn.setOnMouseClicked(e->{
			Connect con = Connect.getInstance();
			
			con.insertCart("INSERT INTO cart VALUES(?,?,?)", new AddtoCart(userID, selectedHoodie.getId(), qtySp.getValue()));
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Success");
		    alert.setHeaderText("Message");
		    alert.setContentText(selectedHoodie.getId()+" - "+selectedHoodie.getName()+" - "+qtySp.getValue()+"x added");
		    alert.showAndWait();
		});
	}
	
	
	public HomePage(Stage stage) {
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
