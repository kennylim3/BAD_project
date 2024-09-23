package view;

import java.sql.ResultSet;
import java.sql.SQLException;

import database.Connect;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.LoggedInUser;
import model.User;

public class LoginPage extends Application{
	
	Stage stage;
	Scene scene; 
	MenuBar  menuBar;
	Menu  menu;
	MenuItem  menuItemRegister;
	Label loginl, username,pass;
	BorderPane bp; 
	GridPane gp;
	TextField user;
	PasswordField password;
	Button loginbtn;
	VBox v;
	HBox h,h2;
	
	public void initialized() {
		bp = new BorderPane();
		gp = new GridPane();
		setMenu();
		loginl = new Label("Login");
		username = new Label("Username: ");
		pass = new Label("Password: ");
		user = new TextField();
		user.setPromptText("Input username");
		password = new PasswordField();
		password.setPromptText("Input password");
		h = new HBox();
		h2= new HBox();
		v = new VBox();
		
		scene = new Scene(bp, 800, 700);
		loginbtn = new Button("Submit");
	}
	
	public void style() {
		loginl.setFont(Font.font("Verdana",FontWeight.BOLD, FontPosture.ITALIC,50));
		loginbtn.setPrefWidth(227);
		gp.setAlignment(Pos.TOP_LEFT);
		bp.setCenter(gp);
		v.setAlignment(Pos.CENTER);
		h.setAlignment(Pos.CENTER);
		h2.setAlignment(Pos.CENTER);
		v.getChildren().addAll(loginl,h,h2,loginbtn);
		h.getChildren().addAll(username,user);
		h2.getChildren().addAll(pass,password);
		VBox.setMargin(loginl, new Insets(0, 0, 30, 0));
		h.setSpacing(20);
		h2.setSpacing(22);
		bp.setTop(menuBar);
		bp.setCenter(v);
		v.setSpacing(10);
	}
	
    private void setMenu() {
		menuBar = new MenuBar();
		menu = new Menu("Login");
		menuItemRegister = new MenuItem("Register");
		
		menuBar.getMenus().add(menu);
		menu.getItems().add(menuItemRegister);
		
		bp.setTop(menuBar);
    }
    private void handling() {
    	Connect con = Connect.getInstance();
    	
		menuItemRegister.setOnAction(e->{
		    new RegisterPage(stage);
		});
		
		loginbtn.setOnAction(e -> {
		    String enteredUsername = user.getText();
		    String enteredPassword = password.getText();

		    ResultSet rs = con.selectData("SELECT * FROM user");

		    try {
		        boolean validCredentials = false;

		        while (rs.next()) {
		            String u = rs.getString("Username");
		            String p = rs.getString("Password");

		            if (enteredUsername.equals(u) && enteredPassword.equals(p)) {
		                validCredentials = true;
		                break;
		            }
		        }

		        if (validCredentials) {
		        	String userID = rs.getString("UserID");
		            String email = rs.getString("Email");
		            String username = rs.getString("Username");
		            String password = rs.getString("Password");
		            String phone = rs.getString("PhoneNumber");
		            String address = rs.getString("Address");
		            String gender = rs.getString("Gender");
		            String role = rs.getString("Role");
		            
		        	User loggedInUser = new User(userID, email, username, password, phone, address, gender, role);
		            LoggedInUser.setCurrentUser(loggedInUser);
		            
		            if (role.equals("Admin")) {
						new EditProductPage(stage);
					}else {
						new HomePage(stage);
					}
		        } else {
		            showAlert("Error", "Wrong Credential");
		        }
		    } catch (SQLException e1) {
		        e1.printStackTrace();
		    }
		});
	}
    
    private void showAlert(String title, String content) {
	    Alert alert = new Alert(AlertType.ERROR);
	    alert.setTitle(title);
	    alert.setHeaderText("Error");
	    alert.setContentText(content);
	    alert.showAndWait();
	}


	@Override
	public void start(Stage stage) throws Exception {
		initialized();
		style();
		handling();
		this.stage = stage;
		this.stage.setTitle("hO-Ohdie");
		this.stage.setScene(scene);
		this.stage.show();
	}

	public static void main(String[] args) {
		launch(args);
	}
}
