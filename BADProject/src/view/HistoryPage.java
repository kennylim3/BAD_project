package view;

import java.util.List;

import database.Connect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Cart;
import model.History;
import model.LoggedInUser;
import model.TransactionHeader;
import model.User;

public class HistoryPage {
	Stage stage;
	Scene scene;
	BorderPane bp;
	GridPane gp;
	MenuBar menuBar;
	Menu account, user, admin;
	MenuItem log, home, cart, history, editProduct;
	String userID, userRole, userName;
	Label title, selectTrTitle, detailTitle, totalPrice;
	TableView<TransactionHeader> hoodieTableView;
	TableColumn<TransactionHeader, String> trColumn;
	TableColumn<TransactionHeader, String> userColumn;
	String selectedTr;
	TableView<History> detailTableView;
	TableColumn<History, String> trIdColumn;
	TableColumn<History, String> hoodieIdColumn;
	TableColumn<History, String> hoodieNameColumn;
	TableColumn<History, Integer> qtyColumn;
	TableColumn<History, Double> totalPriceColumn;
	String totalSumPrice;
	double sum, totalSum;
	HBox hb;
	
	private void checkLoggedInUser() {
		User currentUser = LoggedInUser.getCurrentUser();
		if (currentUser != null) {
		    userID = currentUser.getUserID();
		    userRole = currentUser.getRole();
		    userName = currentUser.getUsername();
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
		hb = new HBox();
		title = new Label(userName + "'s Transaction(s)");
		selectTrTitle = new Label("(Select a Transaction)");
		detailTitle = new Label("Transaction Detail(s)");
		totalPrice = new Label("");
		hb.getChildren().add(totalPrice);
		title.setFont(Font.font("Verdana",FontWeight.BOLD, FontPosture.ITALIC,20));
		selectTrTitle.setFont(Font.font("Verdana",FontWeight.BOLD, FontPosture.ITALIC,20));
		detailTitle.setFont(Font.font("Verdana",FontWeight.BOLD, FontPosture.ITALIC,20));
		totalPrice.setFont(Font.font("Verdana",FontWeight.BOLD, 15));
		
		hoodieTableView = new TableView<>();
        trColumn = new TableColumn<>("Transaction ID");
        userColumn = new TableColumn<>("Hoodie ID");
        trColumn.setPrefWidth(150);
        userColumn.setPrefWidth(150);
        
        trColumn.setCellValueFactory(new PropertyValueFactory<>("trID"));
        userColumn.setCellValueFactory(new PropertyValueFactory<>("userID"));

        hoodieTableView.getColumns().addAll(trColumn, userColumn);
        
        Connect con = Connect.getInstance();
        List<TransactionHeader> th = con.selectTransactionHeader(userID);

        ObservableList<TransactionHeader> trItems = FXCollections.observableArrayList(th);
        hoodieTableView.setItems(trItems);
        
        detailTableView = new TableView<>();
        trIdColumn = new TableColumn<>("Transaction ID");
        hoodieIdColumn = new TableColumn<>("Hoodie ID");
        hoodieNameColumn = new TableColumn<>("Hoodie Name");
        qtyColumn = new TableColumn<>("Quantity");
        totalPriceColumn = new TableColumn<>("Total Price");
        trIdColumn.setPrefWidth(80);
        hoodieIdColumn.setPrefWidth(80);
        hoodieNameColumn.setPrefWidth(85);
        qtyColumn.setPrefWidth(80);
        totalPriceColumn.setPrefWidth(85);
        
        trIdColumn.setCellValueFactory(new PropertyValueFactory<>("trID"));
        hoodieIdColumn.setCellValueFactory(new PropertyValueFactory<>("hoodieID"));
        hoodieNameColumn.setCellValueFactory(new PropertyValueFactory<>("hoodieName"));
        qtyColumn.setCellValueFactory(new PropertyValueFactory<>("qty"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        detailTableView.getColumns().addAll(trIdColumn, hoodieIdColumn, hoodieNameColumn, qtyColumn, totalPriceColumn);
        
        selectTrTitle.setVisible(true);
        detailTitle.setVisible(false);
        detailTableView.setVisible(false);
        totalPrice.setVisible(false);
        hoodieTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                selectedTr = newSelection.getTrID();
                
                selectTrTitle.setVisible(false);
                detailTitle.setVisible(true);
                detailTableView.setVisible(true);
                totalPrice.setVisible(true);
                
                List<History> history = con.selectTransactionDetail(selectedTr);
                ObservableList<History> detailItems = FXCollections.observableArrayList(history);
                detailTableView.setItems(detailItems);
                
                totalSum = calculateTotalSum();
                totalSumPrice = String.format("%." + 4 + "f", totalSum);
                totalPrice.setText("Total Price: " + totalSumPrice);
            }else {
            	selectTrTitle.setVisible(true);
                detailTitle.setVisible(false);
                detailTableView.setVisible(false);
                totalPrice.setVisible(false);
			}
        });

        gp.add(title, 0, 0);
        gp.add(hoodieTableView, 0, 1);
        gp.add(selectTrTitle, 1, 0);
        gp.add(detailTitle, 1, 0);
        gp.add(detailTableView, 1, 1);
        gp.add(hb, 1, 2);
        hb.setAlignment(Pos.CENTER_RIGHT);
		
		gp.setVgap(20);
        gp.setHgap(20);
	    gp.setPadding(new Insets(30));
		bp.setTop(menuBar);
		bp.setCenter(gp);
		scene = new Scene(bp, 800, 700);
	}
	
	private Double calculateTotalSum() {
	    sum = detailTableView.getItems().stream().mapToDouble(History::getTotalPrice).sum();
	    return sum;
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
	}
	
	public HistoryPage(Stage stage) {
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
