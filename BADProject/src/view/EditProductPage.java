package view;

import java.util.List;

import database.Connect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.Hoodie;
import model.LoggedInUser;
import model.User;

public class EditProductPage {
	Stage stage;
	Scene scene;
	BorderPane bp;
	GridPane gp, gp2, gp3, gp4;
	HBox hb, hb2;
	Label title, updateTitle, insertTitle, hoodieId, hoodieName, hoodiePrice, hoodieName2, hoodiePrice2, caption;
	TextField priceTF, nameTF, priceTF2;
	Button updateBtn, deleteBtn, insertBtn;
	MenuBar menuBar;
	Menu account, user, admin;
	MenuItem log, home, cart, history, editProduct;
	TableView<Hoodie> hoodieTableView;
	TableColumn<Hoodie, String> idColumn;
	TableColumn<Hoodie, String> nameColumn;
	TableColumn<Hoodie, Double> priceColumn;
	String userID, userRole;
	
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
		gp3 = new GridPane();
		gp4 = new GridPane();
		hb = new HBox();
		hb2 = new HBox();
		title = new Label("Edit Product");
		title.setFont(Font.font("Verdana",FontWeight.SEMI_BOLD, FontPosture.ITALIC,30));
		updateTitle = new Label("Update & Delete Hoodie(s)");
		insertTitle = new Label("Insert Hoodie");
		updateTitle.setFont(Font.font("Verdana",FontWeight.BOLD, FontPosture.ITALIC,20));
		insertTitle.setFont(Font.font("Verdana",FontWeight.BOLD, FontPosture.ITALIC,20));
		
		hoodieId = new Label();
		hoodieName = new Label();
		hoodiePrice = new Label("Price: ");
		hoodieName2 = new Label("Name: ");
		hoodiePrice2 = new Label("Price: ");
		caption = new Label("Select an item from the list...");
		
		priceTF = new TextField("");
		nameTF = new TextField();
		nameTF.setPromptText("Input name");
		priceTF2 = new TextField();
		priceTF2.setPromptText("Input price");
		
		updateBtn = new Button("Update Price");
		deleteBtn = new Button("Delete Hoodie");
		insertBtn = new Button("Insert");
        updateBtn.setFont(Font.font(updateBtn.getFont().getFamily(), FontWeight.BOLD, updateBtn.getFont().getSize()));
        deleteBtn.setFont(Font.font(deleteBtn.getFont().getFamily(), FontWeight.BOLD, deleteBtn.getFont().getSize()));
        insertBtn.setFont(Font.font(insertBtn.getFont().getFamily(), FontWeight.BOLD, insertBtn.getFont().getSize()));

		
		hoodieTableView = new TableView<>();
        idColumn = new TableColumn<>("Hoodie ID");
        nameColumn = new TableColumn<>("Hoodie Name");
        priceColumn = new TableColumn<>("Hoodie Price");
        idColumn.setPrefWidth(125);
        nameColumn.setPrefWidth(125);
        priceColumn.setPrefWidth(125);

        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        hoodieTableView.getColumns().addAll(idColumn, nameColumn, priceColumn);

        Connect con = Connect.getInstance();
        List<Hoodie> hoodies = con.selectHoodies();

        ObservableList<Hoodie> hoodieItems = FXCollections.observableArrayList(hoodies);
        hoodieTableView.setItems(hoodieItems);
        
        hoodieId.setVisible(false);
		hoodieName.setVisible(false);
		hb.setVisible(false);
		hb2.setVisible(false);
		caption.setVisible(true);
        hoodieTableView.getSelectionModel().selectedItemProperty().addListener((obs, oldSelection, newSelection) -> {
            if (newSelection != null) {
                hoodieId.setText("Hoodie ID: " + newSelection.getId());
                hoodieName.setText("Name: " + newSelection.getName());
                priceTF.setText(String.valueOf(newSelection.getPrice()));
                
                hoodieId.setVisible(true);
				hoodieName.setVisible(true);
				hb.setVisible(true);
				hb2.setVisible(true);
                caption.setVisible(false);
            }else {
				hoodieId.setVisible(false);
				hoodieName.setVisible(false);
				hb.setVisible(false);
				hb2.setVisible(false);
				caption.setVisible(true);
			}
        });
        
        
        gp2.add(updateTitle, 0, 1);
        
        gp3.add(caption, 0, 0);
        gp3.add(hoodieId, 0, 0);
        gp3.add(hoodieName, 0, 1);
        hb.getChildren().addAll(hoodiePrice, priceTF);
        hb.setSpacing(5);
        gp3.add(hb, 0, 2);
        hb2.getChildren().addAll(updateBtn, deleteBtn);
        hb2.setSpacing(5);
        gp3.add(hb2, 0, 3);
        gp3.setVgap(15);
        
        gp4.add(hoodieName2, 0, 0);
        gp4.add(nameTF, 1, 0);
        gp4.add(hoodiePrice2, 0, 1);
        gp4.add(priceTF2, 1, 1);
        gp4.add(insertBtn, 1, 2);
        insertBtn.setPrefWidth(150);
        gp4.setHgap(5);
        gp4.setVgap(15);
        
		gp2.add(gp3, 0, 2);
		gp2.add(insertTitle, 0, 3);
		gp2.add(gp4, 0, 4);
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
	
	private void updateHoodiePrice() {
		Hoodie selectedHoodie = hoodieTableView.getSelectionModel().getSelectedItem();

	    if (selectedHoodie != null) {
	        double newPrice = Double.parseDouble(priceTF.getText());

	        selectedHoodie.setPrice(newPrice);
	        hoodieTableView.refresh();

	        Connect con = Connect.getInstance();
	        con.updateHoodie(selectedHoodie.getId(), newPrice);
	    }
	}
	
	private void deleteHoodie() {
		Hoodie selectedHoodie = hoodieTableView.getSelectionModel().getSelectedItem();
		
		Connect con = Connect.getInstance();
		con.deleteHoodie(selectedHoodie.getId());
		new EditProductPage(stage);
	}
	
	private void handling() {
		log.setOnAction(e->{
			try {
				new LoginPage().start(stage);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		});
		
		editProduct.setOnAction(e->{
			new EditProductPage(stage);
		});
		
		updateBtn.setOnMouseClicked(e->{
			updateHoodiePrice();
		});
		
		deleteBtn.setOnMouseClicked(e->{
			deleteHoodie();
		});
		
		insertBtn.setOnMouseClicked(e->{
			Connect con = Connect.getInstance();
			int lastID = con.getLastID();
			String hoodieID = null;
			
			if (lastID < 9) {
				hoodieID = "HO00" + (lastID+1);
			}else if (lastID < 99) {
				hoodieID = "HO0" + (lastID+1);
			}else if (lastID < 999) {
				hoodieID = "HO" + (lastID+1);
			}
			
			con.insertHoodie("INSERT INTO hoodie VALUES(?,?,?)", new Hoodie(hoodieID, nameTF.getText(), Double.parseDouble(priceTF2.getText())));
			new EditProductPage(stage);
		});
	}
	
	public EditProductPage(Stage stage) {
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
