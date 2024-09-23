package view;

import database.Connect;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.PasswordField;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
import model.User;

public class RegisterPage {
	Stage stage;
	Scene scene;
	GridPane gp;
	BorderPane bp;
	TextField emailTF, usernameTF, phoneTF;
	PasswordField passPF, conpassPF;
	RadioButton maleRadioButton;
	RadioButton femaleRadioButton;
	TextArea addressTA;
	CheckBox agreeCheckBox;
	MenuBar menuBar;
	Menu menu;
	MenuItem menuItemLogin;
	Label title, email, username, password, confirmPassword, phoneNumber, gender, address, check;
	Button submitBtn;
	HBox hb;
	ToggleGroup group;
	
	private void initializeMenu() {
		menuBar = new MenuBar();
		menu = new Menu("Register");
		menuItemLogin = new MenuItem("Login");
		menuBar.getMenus().add(menu);
		menu.getItems().add(menuItemLogin);
	}
	
	private void initialize() {
		bp = new BorderPane();
		gp = new GridPane();
		group = new ToggleGroup();
		title = new Label("Register");
		email= new Label("Email:");
		email.setFont(Font.font(email.getFont().getFamily(), FontWeight.BOLD, FontPosture.REGULAR, email.getFont().getSize()));
		emailTF = new TextField();
		emailTF.setPromptText("Input email");
		username = new Label("Username:");
		username.setFont(Font.font(username.getFont().getFamily(), FontWeight.BOLD, FontPosture.REGULAR, username.getFont().getSize()));
		usernameTF = new TextField();
		usernameTF.setPromptText("Input an unique username");
		password = new Label("Password:");
		password.setFont(Font.font(password.getFont().getFamily(), FontWeight.BOLD, FontPosture.REGULAR, password.getFont().getSize()));
		passPF = new PasswordField();
		passPF.setPromptText("Input password");
		confirmPassword = new Label("Confirm Password:");
		confirmPassword.setFont(Font.font(confirmPassword.getFont().getFamily(), FontWeight.BOLD, FontPosture.REGULAR, confirmPassword.getFont().getSize()));
		conpassPF = new PasswordField();
		conpassPF.setPromptText("Confirm password");
		phoneNumber = new Label("Phone Number:");
		phoneTF = new TextField();
		phoneTF.setPromptText("Example: +6212345678901");
		gender = new Label("Gender:");
		gender.setFont(Font.font(gender.getFont().getFamily(), FontWeight.BOLD, FontPosture.REGULAR, gender.getFont().getSize()));
		maleRadioButton = new RadioButton("Male");
		femaleRadioButton = new RadioButton("Female");
		maleRadioButton.setToggleGroup(group);
		femaleRadioButton.setToggleGroup(group);
		address = new Label("Address:");
		address.setFont(Font.font(address.getFont().getFamily(), FontWeight.BOLD, FontPosture.REGULAR, address.getFont().getSize()));
		addressTA = new TextArea();
		addressTA.setPromptText("Input address");
		agreeCheckBox = new CheckBox("I Agree to Term & Conditions");
		submitBtn = new Button("Register");
		hb = new HBox();
		hb.getChildren().addAll(maleRadioButton, femaleRadioButton);
		initializeMenu();
		gp.add(title, 0, 1);
		gp.add(email, 0, 2);
		gp.add(emailTF, 0, 3);
		gp.add(username, 0, 4);
		gp.add(usernameTF, 0, 5);
		gp.add(password, 0, 6);
		gp.add(passPF, 0, 7);
		gp.add(confirmPassword, 0, 8);
		gp.add(conpassPF, 0, 9);
		gp.add(phoneNumber, 0, 10);
		gp.add(phoneTF, 0, 11);
		gp.add(gender, 0, 12);
		gp.add(hb, 0, 13);
		gp.add(address, 0, 14);
		gp.add(addressTA, 0, 15);
		gp.add(agreeCheckBox, 0, 16);
		bp.setTop(menuBar);
		bp.setCenter(gp);
		bp.setBottom(submitBtn);
		emailTF.setPrefWidth(800);
		scene = new Scene(bp, 800, 700);
	}
	
	public void styling() {
		title.setFont(Font.font("Verdana",FontWeight.BOLD, FontPosture.ITALIC,40));
		gp.setVgap(5);
		gp.setPadding(new Insets(10, 40, 10, 40));
		gp.setAlignment(Pos.CENTER_LEFT);
		BorderPane.setMargin(submitBtn, new Insets(10, 30, 10, 0));
		GridPane.setMargin(email, new Insets(0,0,3,0));
		GridPane.setMargin(username, new Insets(0,0,3,0));
		GridPane.setMargin(password, new Insets(0,0,3,0));
		GridPane.setMargin(confirmPassword, new Insets(0,0,3,0));
		GridPane.setMargin(gender, new Insets(0,0,3,0));
		GridPane.setMargin(address, new Insets(0,0,3,0));
		GridPane.setMargin(phoneNumber, new Insets(0,0,3,0));
		BorderPane.setAlignment(submitBtn, Pos.CENTER_RIGHT);
		hb.setSpacing(20);
		submitBtn.setPrefWidth(150);
		email.setFont(Font.font(null, FontWeight.BOLD, 12));
		username.setFont(Font.font(null, FontWeight.BOLD, 12));
		password.setFont(Font.font(null, FontWeight.BOLD, 12));
		confirmPassword.setFont(Font.font(null, FontWeight.BOLD, 12));
		gender.setFont(Font.font(null, FontWeight.BOLD, 12));
		address.setFont(Font.font(null, FontWeight.BOLD, 12));
		phoneNumber.setFont(Font.font(null, FontWeight.BOLD, 12));
	}
	
	private boolean validateInputs() {
	    if (!emailTF.getText().endsWith("@hoohdie.com")) {
	        showAlert("Email must end with '@hoohdie.com'");
	        return false;
	    }

	    if (!isUsernameUnique(usernameTF.getText())) {
	        showAlert("Username must be unique");
	        return false;
	    }

	    if (passPF.getText().length() < 5) {
	        showAlert("Password must be at least 5 characters");
	        return false;
	    }

	    if (!passPF.getText().equals(conpassPF.getText())) {
	        showAlert("Password and Confirm Password must match");
	        return false;
	    }

	    String phoneNumber = phoneTF.getText();
	    if (phoneNumber.length() != 14 || !phoneNumber.startsWith("+62")) {
	        showAlert("Phone Number must be 14 characters and start with '+62'");
	        return false;
	    }

	    if (group.getSelectedToggle() == null) {
	        showAlert("Please select your gender");
	        return false;
	    }

	    if (addressTA.getText().isEmpty()) {
	        showAlert("Address must be filled");
	        return false;
	    }

	    if (!agreeCheckBox.isSelected()) {
	        showAlert("Please agree to the Terms & Conditions");
	        return false;
	    }

	    return true;
	}

	private void showAlert(String content) {
	    Alert alert = new Alert(AlertType.ERROR);
	    alert.setTitle("Error");
	    alert.setHeaderText("Error");
	    alert.setContentText(content);
	    alert.showAndWait();
	}
	
	private boolean isUsernameUnique(String username) {
	    return Connect.getInstance().isUsernameUnique(username);
	}
	
	public void handling() {
		menuItemLogin.setOnAction(e->{
			try {
				new LoginPage().start(stage);
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			};
		});
		
		submitBtn.setOnMouseClicked(e->{
			Connect con = Connect.getInstance();
			int rowCount = con.getRowCount("user");
			String userID = null;
			String role = "User";
			
			if (validateInputs()) {
				if (rowCount < 9) {
					userID = "US00" + (rowCount+1);
				}else if (rowCount < 99) {
					userID = "US0" + (rowCount+1);
				}else if (rowCount < 999) {
					userID = "US" + (rowCount+1);
				}
				
				con.insertUser("INSERT INTO user VALUES(?,?,?,?,?,?,?,?)", new User(userID, emailTF.getText(), usernameTF.getText(),
						passPF.getText(), phoneTF.getText(), addressTA.getText(), ((RadioButton) group.getSelectedToggle()).getText(), 
						role));
				try {
					new LoginPage().start(stage);
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
			}
			
			
		});
	}
	
	public RegisterPage(Stage stage) {
		this.stage = stage;
		initialize();
		styling();
		handling();
		this.stage.setTitle("hO-Ohdie");
		this.stage.setResizable(false);
		this.stage.setScene(scene);
		this.stage.show();
	}
}
