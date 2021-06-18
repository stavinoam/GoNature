package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;

import client.ChatClient;
import client.ClientUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.LogOutMessage;
import logic.OnResponseListener;
import logic.SettingsStatusMessage;
import logic.SettingsUpdateMessage;
import logic.Worker;

/**
 * ParkSettingsController class The class is provides the current park settings
 * with a possibility to change them and sent the change to approve by the
 * department manager
 * 
 * @author Shahar Yarden
 *
 */
public class ParkSettingsController implements Initializable {
	
	/**
	 * currentCapacity represents the current number of capacity 
	 * currentSpares represents the current number of spares
	 * currentTravelingTime represents the current traveling time
	 * currentDiscount represents the current discount amount
	 */
	private String currentCapacity, currentSpares, currentTravelingTime, currentDiscount;

	
	/**
	 * JFXHamburger menu
	 */
	@FXML
	private JFXHamburger menu;

	/**
	 * JFXDrawer drawer
	 */
	@FXML
	private JFXDrawer drawer;

	/**
	 * AnchorPane drawerpane
	 */
	@FXML
	private AnchorPane drawerpane;

	/**
	 * Label lblSettings
	 */
	@FXML
	private Label lblSettings;

	/**
	 * Label lblCapacity
	 */
	@FXML
	private Label lblCapacity;

	/**
	 * TextField txtCapacity
	 */
	@FXML
	private TextField txtCapacity;

	/**
	 * Label lblSpares
	 */
	@FXML
	private Label lblSpares;

	/**
	 * TextField txtSpares
	 */
	@FXML
	private TextField txtSpares;

	/**
	 * Label lblTravelTime
	 */
	@FXML
	private Label lblTravelTime;

	/**
	 * TextField txtTravelingTime
	 */
	@FXML
	private TextField txtTravelingTime;

	/**
	 * Label lblDiscount
	 */
	@FXML
	private Label lblDiscount;

	/**
	 * TextField txtDiscount
	 */
	@FXML
	private TextField txtDiscount;

	/**
	 * JFXButton btnSend
	 */
	@FXML
	private JFXButton btnSend;

	/**
	 * JFXButton btnCancel
	 */
	@FXML
	private JFXButton btnCancel;

	/**
	 * JFXButton btnSettingsStatus
	 */
	@FXML
	private JFXButton btnSettingsStatus;

	/**
	 * Label aboutlabel
	 */
	@FXML
	private Label aboutlabel;

	/**
	 * organization is object appearance of Worker 
	 */
	private Worker organization;
	
	/**
	 * s is a string that contain the current setting values
	 */
	private String s;

	/**
	 * starting a new stage 
	 * @param primaryStage object of a stage
	 * @param w object of Worker type
	 * @param s string that contain the updated park settings
	 * @throws Exception throw any exception
	 */
	public void start(Stage primaryStage, Worker w, String s) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ParkSettings.fxml"));
		loader.setController(this);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Style.css").toExternalForm());
		AboutUsController.primaryStage = primaryStage;
		OurParksController.primaryStage = primaryStage;
		PricesListController.primaryStage = primaryStage;
		primaryStage.setTitle("GoNature");
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image("gui/css/tree.png"));
		primaryStage.show();
		this.organization = w;
		this.s = s;
		loadParkSettings(this.s);
		primaryStage.setOnCloseRequest(event -> {
			System.out.println("Stage is closing");
			ClientUI.chat.accept(new LogOutMessage(organization.getUserName()), null);
		});
	}

	
	/**
	 * a function with an ActionEvent, when click on the correct button it will return to the last page(stage)
	 * @param event the event that invoked is button click
	 * @throws IOException throw IOException
	 */
	@FXML
	void backToWorkerHomePage(ActionEvent event) throws IOException {
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage stage = new Stage();
		ParkManagerhpController parkManagerhpController = new ParkManagerhpController();
		try {
			parkManagerhpController.start(stage, organization);
		} catch (Exception e) {
			System.out.println("FAILED TO OPEN PARK WORKER HOME PAGE");
		}
	}

	/**
	 * a function with an ActionEvent, when click on the correct button will appear a page transform to the SettingsStatusController
	 * @param event the event that invoked is button click
	 * @throws IOException throw IOException
	 */
	@FXML
	void goToSettingsStatus(ActionEvent event) throws IOException {
		ClientUI.chat.accept(new SettingsStatusMessage(organization.getOrganization()), new OnResponseListener() {
			@Override
			public void onResponse(Object message) {
				Platform.runLater(() -> {
					((Node) event.getSource()).getScene().getWindow().hide();
					Stage stage = new Stage();
					SettingsStatusController settingsStatusController = new SettingsStatusController();
					try {
						settingsStatusController.start(stage, organization, ChatClient.str);
					} catch (Exception e) {
						System.out.println("FAILED TO OPEN PARK WORKER HOME PAGE");
					}
				});

			}
		});
	}

	/**
	 * a function with an ActionEvent
	 * The function check if the setting values that desire to be changes are legal
	 * If the values are legal they are sent to the department manager for approve and also to the setting status
	 * If the values are illegal a matched alert will pop up
	 * @param event the event that invoked is button click
	 * @throws IOException throw IOException
	 */
	@FXML
	void sendSettingsToDepartmentManager(ActionEvent event) {
		if (currentCapacity.equals(txtCapacity.getText()) && currentSpares.equals(txtSpares.getText())
				&& currentTravelingTime.equals(txtTravelingTime.getText())
				&& currentDiscount.equals(txtDiscount.getText()))
			alert("No change was made.");
		else if (!txtCapacity.getText().matches("[0-9]+") || !txtSpares.getText().matches("[0-9]+")
				|| !txtTravelingTime.getText().matches("[0-9]+") || !txtDiscount.getText().matches("[0-9]+"))
			alert("Input must contain positive numbers only.");
		else if (Integer.parseInt(txtSpares.getText()) > Integer.parseInt(txtCapacity.getText()))
			alert("Spares must be less than capacity.");
		else if (Integer.parseInt(txtTravelingTime.getText()) > 24 || Integer.parseInt(txtTravelingTime.getText()) < 1)
			alert("Traveling time must be between 1 to 24 hours");
		else if (Integer.parseInt(txtDiscount.getText()) > 100)
			alert("Discount percentage mustn't be more than 100.");
		else if (Integer.parseInt(txtCapacity.getText()) == 0)
			alert("Capacity must be bigger than 0.");
		else {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yy, HH:mm:ss");
			LocalDateTime now = LocalDateTime.now();

			String[] s = new String[25];
			s[0] = "Park capacity";
			s[1] = currentCapacity;
			s[2] = txtCapacity.getText();
			s[3] = dtf.format(now);
			s[4] = "Waiting";
			s[5] = "No response";

			s[6] = "Park spares";
			s[7] = currentSpares;
			s[8] = txtSpares.getText();
			s[9] = dtf.format(now);
			s[10] = "Waiting";
			s[11] = "No response";

			s[12] = "Traveling time";
			s[13] = currentTravelingTime;
			s[14] = txtTravelingTime.getText();
			s[15] = dtf.format(now);
			s[16] = "Waiting";
			s[17] = "No response";

			s[18] = "Discount";
			s[19] = currentDiscount;
			s[20] = txtDiscount.getText();
			s[21] = dtf.format(now);
			s[22] = "Waiting";
			s[23] = "No response";
			s[24] = organization.getOrganization();

			ClientUI.chat.accept(new SettingsUpdateMessage(s), new OnResponseListener() {
				@Override
				public void onResponse(Object message) {
					if (ChatClient.flag) {
						Platform.runLater(() -> {
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Success");
							alert.setHeaderText(null);
							alert.setContentText("Settings has been sent successfully.");
							alert.show();
						});
					} else {
						System.out.println("not updated");
						Platform.runLater(() -> {
							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Failure");
							alert.setHeaderText(null);
							alert.setContentText("Settings hasn't been changed!");
							alert.show();
						});
					}
				}
			});
		}
	}

	/**
	 * function that produce a pop-up alert
	 * @param s the message that will written in the alert
	 */
	public void alert(String s) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle("Wrong input");
		alert.setHeaderText(null);
		alert.setContentText(s);
		alert.show();
	}

	
	/**
	 * A function that load the park setting into the right text fields
	 * @param s string that contains the current setting values
	 */
	public void loadParkSettings(String s) {
		String[] str = s.split(" ");
		txtCapacity.setText(str[0]);
		txtSpares.setText(str[1]);
		txtTravelingTime.setText(str[2]);
		txtDiscount.setText(str[3]);
		currentCapacity = str[0];
		currentSpares = str[1];
		currentTravelingTime = str[2];
		currentDiscount = str[3];
	}

	/**
	 * initialize the hamburger settings 
	 * initialize the combo box
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		try {
			VBox box = FXMLLoader.load(getClass().getResource("Drawer.fxml"));
			drawer.setSidePane(box);

			for (Node node : box.getChildren())
				if (node.getAccessibleText() != null) {
					node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
						switch (node.getAccessibleText()) {
						case "home":
							((Node) e.getSource()).getScene().getWindow().hide();
							Stage stage3 = new Stage();
							ParkManagerhpController parkManagerhpController = new ParkManagerhpController();
							try {
								parkManagerhpController.start(stage3, organization);
							} catch (Exception e1) {
								e1.printStackTrace();
								System.out.println("FAILED TO OPEN PARKMANAGER PAGE");
							}
							break;
						case "price":
							((Node) e.getSource()).getScene().getWindow().hide();
							Stage stage5 = new Stage();
							PricesListController pricesListController = new PricesListController();
							try {
								pricesListController.start(stage5);
							} catch (Exception e1) {
								System.out.println("FAILED TO OPEN OUR PARKS PAGE");
							}
							break;
						case "ourparks":
							((Node) e.getSource()).getScene().getWindow().hide();
							Stage stage = new Stage();
							OurParksController ourParks = new OurParksController();
							try {
								ourParks.start(stage);
							} catch (Exception e1) {
								System.out.println("FAILED TO OPEN OUR PARKS PAGE");
							}
							break;
						case "aboutus":
							((Node) e.getSource()).getScene().getWindow().hide();
							Stage stage1 = new Stage();
							AboutUsController aboutUs = new AboutUsController();
							try {
								aboutUs.start(stage1);
							} catch (Exception e1) {
								System.out.println("FAILED TO OPEN ABOUT US PAGE");
							}
							break;
						case "signout":
							((Node) e.getSource()).getScene().getWindow().hide();
							Stage stage2 = new Stage();
							LoginPageController login = new LoginPageController();
							try {
								login.logoutWorker(stage2, organization);
							} catch (Exception e1) {
								e1.printStackTrace();
							}
							break;
						}
					});
				}

			HamburgerBasicCloseTransition basicClose = new HamburgerBasicCloseTransition(menu);
			basicClose.setRate(-1);

			menu.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) -> {
				basicClose.setRate(basicClose.getRate() * -1);
				basicClose.play();

				if (drawer.isOpened()) {
					drawer.close();
					drawerpane.setMouseTransparent(true);
				} else {
					drawer.open();
					drawerpane.setMouseTransparent(false);
				}

			});
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
