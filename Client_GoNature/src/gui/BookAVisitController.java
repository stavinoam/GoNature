package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;

import animation.Bounce;
import client.ChatClient;
import client.ClientUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import javafx.util.Duration;
import logic.CheckIfOrderSucceededMessage;
import logic.LogOutMessage;
import logic.OnResponseListener;
import logic.Order;
import logic.Subscriber;
import logic.Traveler;

/**
 * BookAVisitController is a controller of BookAVisit.fxml FXML page. The
 * controller implements all the functionals of BookAVisit.fxml The controller
 * implements the Initializable interface - implements initialize method
 * 
 * @author Stav Anna
 *
 */
public class BookAVisitController implements Initializable {
	private Stage home;
	@FXML
	private DatePicker datefield;

	@FXML
	private ComboBox<String> timeSlotCombo;

	@FXML
	private TextField emailfield;

	@FXML
	private Hyperlink linkOurParks;

	@FXML
	private RadioButton noRadioButton;

	@FXML
	private RadioButton yesRadioButton;

	@FXML
	private ComboBox<String> parkNameCombo;

	@FXML
	private Spinner<Integer> spnNumberOfVisitors;

	@FXML
	private ComboBox<String> phoneCombo;

	@FXML
	private TextField fldPhone;

	@FXML
	private Circle circle1;

	@FXML
	private Circle circle2;

	@FXML
	private Circle circle3;

	@FXML
	private HBox circles;

	@FXML
	private JFXButton btnContinue;

	@FXML
	private JFXButton btnBack;

	@FXML
	private JFXHamburger menu;

	@FXML
	private AnchorPane drawerpane;

	@FXML
	private JFXDrawer drawer;

	private Traveler traveler;
	private ToggleGroup group;
	private static String EMAIL_PATTERN = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";
	SpinnerValueFactory<Integer> valueFactoryVistorsNum = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 15, 1);

	/**
	 * Start method is the method that starts the stage with the match fxml file
	 * 
	 * @param stage    the new stage that is created before calling the start method
	 * @param traveler the traveler that uses the BookAvisit fxml page
	 * @throws Exception if the FXMLLoader can't load the fxml file
	 */
	public void start(Stage stage, Traveler traveler) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/BookAVisit.fxml"));
		loader.setController(this);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Style.css").toExternalForm());
		home = stage;
		stage.setTitle("GoNature");
		stage.setScene(scene);
		stage.getIcons().add(new Image("gui/css/tree.png"));
		stage.show();
		setTraveler(traveler);

		stage.setOnCloseRequest(event -> {
			System.out.println("Stage is closing");
			if(traveler != null & traveler.getType().equals("subscriber"))
				ClientUI.chat.accept(new LogOutMessage(((Subscriber)traveler).getSubscriberNumber()), null);
			ClientUI.chat.accept(new LogOutMessage(traveler.getIdNumber()), null);
		});
	}

	/**
	 * The method initialize all the parameters by the traveler's data that the
	 * start method gets
	 * 
	 * @param traveler the traveler that uses the BookAvisit fxml page
	 */
	private void setTraveler(Traveler traveler) {
		this.traveler = traveler;
		if (traveler != null && traveler.getEmail() != null)
			emailfield.setText(traveler.getEmail());

		if (traveler != null && traveler.getPhone() != null) {
			String prefix = traveler.getPhone().substring(0, 3);
			String phone = traveler.getPhone().substring(3, 10);
			fldPhone.setText(phone);
			switch (prefix) {
			case "052":
				phoneCombo.getSelectionModel().select(1);
				break;
			case "053":
				phoneCombo.getSelectionModel().select(2);
				break;
			case "054":
				phoneCombo.getSelectionModel().select(3);
				break;
			case "055":
				phoneCombo.getSelectionModel().select(4);
				break;
			case "058":
				phoneCombo.getSelectionModel().select(5);
				break;
			default:
				phoneCombo.getSelectionModel().select(0);
				break;
			}
		}

		if (traveler != null && !"instructor".equals(traveler.getType()))
			yesRadioButton.setDisable(true);
		else
			yesRadioButton.setDisable(false);

		if (traveler != null && "subscriber".equals(traveler.getType())) {
			if (traveler instanceof Subscriber) {
				int number = ((Subscriber) traveler).getVisitorsNumber();
				spnNumberOfVisitors.getValueFactory().setValue(number);
			}
		}
	}

	/**
	 * initialize method is the first method to be run when the controller is
	 * starting the method initializes the parameters of the fxml page
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		loadParameters();
		spnNumberOfVisitors.setValueFactory(valueFactoryVistorsNum);

		try {
			VBox box = FXMLLoader.load(getClass().getResource("Drawer.fxml"));
			drawer.setSidePane(box);

			for (Node node : box.getChildren())
				if (node.getAccessibleText() != null) {
					node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) -> {
						switch (node.getAccessibleText()) {
						case "home":
							((Node) e.getSource()).getScene().getWindow().hide();
							Stage stage4 = new Stage();
							try {
								if (traveler.isNew()) {
									LoginPageController loginPageController = new LoginPageController();
									loginPageController.logout(stage4, traveler);
								} else {
									TravelerHomePageController travelerHomePageController = new TravelerHomePageController();
									travelerHomePageController.start(stage4, traveler);
								}
							} catch (Exception e1) {
								System.out.println("FAILED TO OPEN OUR PARKS PAGE");
							}
							break;
						case "price":
							((Node) e.getSource()).getScene().getWindow().hide();
							Stage stage5 = new Stage();
							PricesListController pricesListController = new PricesListController();
							try {
								PricesListController.primaryStage = home;
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
								OurParksController.primaryStage = home;
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
								AboutUsController.primaryStage = home;
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
								login.logout(stage2, traveler);
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

//		final Tooltip tooltip1 = new Tooltip();
//		tooltip1.setText("If you are an instructor, please sign up as an instructor");
//		this.instructorTooltip = tooltip1;

		new Bounce(circle1).setCycleCount(5000).setDelay(Duration.valueOf("300ms")).play();
		new Bounce(circle2).setCycleCount(5000).setDelay(Duration.valueOf("400ms")).play();
		new Bounce(circle3).setCycleCount(5000).setDelay(Duration.valueOf("500ms")).play();
	}

	/**
	 * the method sets the style of the fxml objects to be the same
	 */
	private void makeBorderBlack() {
		timeSlotCombo.setStyle("-fx-border-color: black; -fx-border-width: 2 2 2 2;");
		parkNameCombo.setStyle("-fx-border-color: black; -fx-border-width: 2 2 2 2;");
		datefield.setStyle("-fx-border-color: black; -fx-border-width: 2 2 2 2;");
		fldPhone.setStyle("-fx-border-color: black; -fx-border-width: 2 2 2 2;");
		emailfield.setStyle("-fx-border-color: black; -fx-border-width: 2 2 2 2;");
	}

	/**
	 * sendOrderRequest method respond to user click on "book" button the method
	 * makes a request to the server to add the order to the orders list if the
	 * request denies, a custom pop-up would appear, or the user would move to
	 * another page
	 * 
	 * @param event clicking on "book" button
	 */
	@FXML
	void sendOrderRequest(ActionEvent event) {
		makeBorderBlack();
		Order order = new Order();
		if ((timeSlotCombo.getValue() == null) || (parkNameCombo.getValue() == null) || (datefield.getValue() == null)
				|| (fldPhone.getText().trim().isEmpty()) || (emailfield.getText().trim().isEmpty())) {
			if (timeSlotCombo.getValue() == null)
				timeSlotCombo.setStyle("-fx-border-color: red; -fx-border-width: 2 2 2 2;");
			if (parkNameCombo.getValue() == null)
				parkNameCombo.setStyle("-fx-border-color: red; -fx-border-width: 2 2 2 2;");
			if (datefield.getValue() == null)
				datefield.setStyle("-fx-border-color: red; -fx-border-width: 2 2 2 2;");
			if (fldPhone.getText().trim().isEmpty())
				fldPhone.setStyle("-fx-border-color: red; -fx-border-width: 2 2 2 2;");
			if (emailfield.getText().trim().isEmpty())
				emailfield.setStyle("-fx-border-color: red; -fx-border-width: 2 2 2 2;");
			alert("Empty field", "You must fill all the fields.");
		} else if (datefield.getValue().isBefore(LocalDate.now())) {
			alert("Invalid date", "The date can't be empty or a past date.");
			datefield.setStyle("-fx-border-color: red; -fx-border-width: 2 2 2 2;");
		} else if (!fldPhone.getText().matches("[0-9]+") || fldPhone.getText().length() != 7) {
			alert("Invalid phone number", "phone number has to be 10 digits.\n"
					+ "Please choose your phone number prefix from the list and complete the 7 left digit at the the next field.");
			fldPhone.setStyle("-fx-border-color: red; -fx-border-width: 2 2 2 2;");
		} else if (!isValidEmail(emailfield.getText())) {
			alert("Wrong email format", "The email must match the format ____@____.___");
			emailfield.setStyle("-fx-border-color: red; -fx-border-width: 2 2 2 2;");
		} else {
			String phone = "";
			String identification = "";

			if (phoneCombo.getValue() == (null))
				phone = "050" + fldPhone.getText();
			else
				phone = phoneCombo.getValue() + fldPhone.getText();

			traveler.setPhone(phone);
			traveler.setEmail(emailfield.getText());
			identification = traveler.getIdNumber();
			// order.setInstructor(yesRadioButton.isSelected());
			if (traveler.getType().equals("instructor") && noRadioButton.isSelected())
				order.setType("regular");
			else
				order.setType(traveler.getType());

			order.setParkName(parkNameCombo.getValue());
			order.setDate(datefield.getValue());
			order.setTimeSlot(timeSlotCombo.getValue());
			order.setEmail(emailfield.getText());
			order.setNumberOfVisitors(spnNumberOfVisitors.getValue());

			order.setTraveler(traveler);
			order.setIdentification(identification);

			// check if there's slot for number of visitors at the picked date and time
			ClientUI.chat.accept(new CheckIfOrderSucceededMessage(order), new OnResponseListener() {
				@Override
				public void onResponse(Object message) {
					String[] s = ChatClient.str.split(" ");
					if (ChatClient.str.equals("AlreadyExist")) {
						Platform.runLater(() -> {
							circles.setVisible(false);
							alert("Multiply orders", "You already have and order to the same park and date");
						});
					} else if (ChatClient.str.equals("DatePassed")) {
						Platform.runLater(() -> {
							circles.setVisible(false);
							alert("The time has passed", "The time that you picked has already passed.");
						});
					} else if (s[0].length() == 5) { // the date is OK for amount
						Platform.runLater(() -> {
							((Node) event.getSource()).getScene().getWindow().hide();
							if (traveler != null)
								traveler.setNew(false);
							Stage stage = new Stage();
							SuccessfulBookingController sucBook = new SuccessfulBookingController();
							try {
								sucBook.setOrderNumberAndPrice(s[0], s[1]);
								sucBook.start(stage, order);
							} catch (Exception e) {
								e.printStackTrace();
								System.out.println("FAILED TO OPEN Successful Booking PAGE");
							}
						});
					} else
					/* if (ChatClient.str.equals("NoSlot")) */ {
						// the date is already full for amount - str = otherDates
						Platform.runLater(() -> {
							((Node) event.getSource()).getScene().getWindow().hide();
							try {
								Stage stage = new Stage();
								UnfortunetlyController failedBook = new UnfortunetlyController();
								// failedBook.loadTable(ChatClient.str);
								failedBook.loadDates(ChatClient.str);
								failedBook.start(stage, order);
							} catch (Exception e) {
								e.printStackTrace();
								System.out.println("FAILED TO OPEN Unfortunetly BOOKING PAGE");
							}
						});
					}
				}
			});
			circles.setVisible(true);
		}
	}

	/**
	 * The method respond to user click on "back" button The method leads the user
	 * to the last page that he used before entering "book a visit page"
	 * 
	 * @param event clicking on "back" button
	 */
	@FXML
	void back(ActionEvent event) {
		if ("subscriber".equals(traveler.getType()) || "instructor".equals(traveler.getType())) {
			((Node) event.getSource()).getScene().getWindow().hide();
			Stage stage = new Stage();
			TravelerHomePageController tHomePage = new TravelerHomePageController();
			try {
				tHomePage.start(stage, traveler);
			} catch (Exception e) {
				e.printStackTrace();
				System.out.println("FAILED TO OPEN Traveler Home Page PAGE");
			}
		} else {// regular traveler - with or without an order
			if (traveler.isNew()) {// true - new traveler without an order
				((Node) event.getSource()).getScene().getWindow().hide();
				Stage stage = new Stage();
				LoginPageController login = new LoginPageController();
				try {
					login.start(stage);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("FAILED TO OPEN Traveler Home Page PAGE");
				}
			} else {// false - traveler with an order
				((Node) event.getSource()).getScene().getWindow().hide();
				Stage stage = new Stage();
				TravelerHomePageController tHomePage = new TravelerHomePageController();
				try {
					tHomePage.start(stage, traveler);
				} catch (Exception e) {
					e.printStackTrace();
					System.out.println("FAILED TO OPEN Traveler Home Page PAGE");
				}
			}
		}
	}

	/**
	 * loadParameters method loads the comboboxes, the date picker and the toggle
	 * group
	 */
	private void loadParameters() {
		List<String> parkName = Arrays.asList("Hatzbani", "Banias", "Hai Park");
		List<String> timeSlots = Arrays.asList("08:00 - 12:00", "12:00 - 16:00");
		List<String> phonePrefix = Arrays.asList("050", "052", "053", "054", "055", "058");

		parkNameCombo.setItems(FXCollections.observableArrayList(parkName));
		timeSlotCombo.setItems(FXCollections.observableArrayList(timeSlots));
		phoneCombo.setItems(FXCollections.observableArrayList(phonePrefix));

		// initiate Time Comboboxes:
		LocalDate minDate = LocalDate.now();
		this.datefield.setDayCellFactory(param -> new DateCell() {
			@Override
			public void updateItem(LocalDate item, boolean empty) {
				super.updateItem(item, empty);
				setDisable(item.isBefore(minDate));
			}
		});

		// initiate Toggle Group
		group = new ToggleGroup();
		noRadioButton.setToggleGroup(group);
		yesRadioButton.setToggleGroup(group);
	}

	/**
	 * the method checks if the email is valid to the format ___@___.___
	 * 
	 * @param email the email that we want to check
	 * @return true if the email is valid to the format, else false
	 */
	private boolean isValidEmail(String email) {
		return email.matches(EMAIL_PATTERN);
	}

	/**
	 * the method respond to user click on "our parks" button
	 * 
	 * @param event clicking on "our parks" button
	 */
	@FXML
	void goToOurParksPage(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage stage = new Stage();
		OurParksController ourPark = new OurParksController();
		try {
			OurParksController.primaryStage = home;
			ourPark.start(stage);
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("FAILED TO OPEN OUR PARKS PAGE");
		}
	}

	/**
	 * alert method shows a information dialog to the screen
	 * 
	 * @param title the title of the information dialog
	 * @param s     the content of the information dialog
	 */
	private void alert(String title, String s) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(s);
		alert.show();
	}

}
