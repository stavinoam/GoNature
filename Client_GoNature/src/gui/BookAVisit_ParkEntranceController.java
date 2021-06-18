package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;

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
import javafx.scene.image.Image;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.CheckIfOrderSucceededMessage;
import logic.LogOutMessage;
import logic.OnResponseListener;
import logic.Order;
import logic.Worker;

/**
 * BookAVisit_ParkEntranceController is a controller of BookAVisit_ParkEntrance.fxml FXML page.
 * The controller implements all the functionals of BookAVisit_ParkEntrance.fxml 
 * The controller implements the Initializable interface - implements initialize method
 * @author Stav & Anna
 *
 */
/**
 * @author Stav Anna
 *
 */
public class BookAVisit_ParkEntranceController implements Initializable
{
	/**
	 * primaryStage is the stage of the current page
	 */
	static Stage primaryStage;
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

	@FXML
	private ComboBox<String> timeSlotCombo;

	@FXML
	private TextField fldTravelerID;

	@FXML
	private RadioButton noRadioButton;

	@FXML
	private RadioButton yesRadioButton;

	@FXML
	private Spinner<Integer> spnNumberOfVisitors;

	private ToggleGroup group;
	private Worker organization;
	SpinnerValueFactory<Integer> valueFactoryVistorsNum = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 15, 1);

	
	/** 
	 * Start method is the method that starts the stage with the match fxml file 
	 * @param stage the new stage that is created before calling the start method
	 * @param w the entrance worker that uses the fxml page
	 * @throws Exception if the FXMLLoader can't load the fxml file
	 */
	public void start(Stage stage, Worker w) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/BookAVisit_ParkEntrance.fxml"));
		loader.setController(this);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Style.css").toExternalForm());
		AboutUsController.primaryStage = primaryStage;
		OurParksController.primaryStage = primaryStage;
		primaryStage = stage;
		organization = w;
		stage.setTitle("GoNature");
		stage.setScene(scene);
		stage.getIcons().add(new Image("gui/css/tree.png"));
		stage.show();
		primaryStage.setOnCloseRequest(event ->
		{
			System.out.println("Stage is closing");
			ClientUI.chat.accept(new LogOutMessage(organization.getUserName()), null);
		});
	}

	/**
	 * initialize method is the first method to be run when the controller is starting
	 * the method initializes the parameters of the fxml page
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		loadParameters();
		spnNumberOfVisitors.setValueFactory(valueFactoryVistorsNum);

		try
		{
			VBox box = FXMLLoader.load(getClass().getResource("Drawer.fxml"));
			drawer.setSidePane(box);

			for (Node node : box.getChildren())
				if (node.getAccessibleText() != null)
				{
					node.addEventHandler(MouseEvent.MOUSE_CLICKED, (e) ->
					{
						switch (node.getAccessibleText())
						{
						case "home":
							((Node) e.getSource()).getScene().getWindow().hide();
							Stage stage3 = new Stage();
							ParkEntranceController parkEntranceController = new ParkEntranceController();
							try
							{
								parkEntranceController.start(stage3, organization);
							} catch (Exception e1)
							{
								e1.printStackTrace();
								System.out.println("FAILED TO OPEN PARKMANAGER PAGE");
							}
							break;
						case "price":
							((Node) e.getSource()).getScene().getWindow().hide();
							Stage stage5 = new Stage();
							PricesListController pricesListController = new PricesListController();
							try
							{
								PricesListController.primaryStage = primaryStage;
								pricesListController.start(stage5);
							} catch (Exception e1)
							{
								System.out.println("FAILED TO OPEN OUR PARKS PAGE");
							}
							break;
						case "ourparks":
							((Node) e.getSource()).getScene().getWindow().hide();
							Stage stage = new Stage();
							OurParksController ourParks = new OurParksController();
							try
							{
								OurParksController.primaryStage = primaryStage;
								ourParks.start(stage);
							} catch (Exception e1)
							{
								System.out.println("FAILED TO OPEN OUR PARKS PAGE");
							}
							break;
						case "aboutus":
							((Node) e.getSource()).getScene().getWindow().hide();
							Stage stage1 = new Stage();
							AboutUsController aboutUs = new AboutUsController();
							try
							{
								AboutUsController.primaryStage = primaryStage;
								aboutUs.start(stage1);
							} catch (Exception e1)
							{
								System.out.println("FAILED TO OPEN ABOUT US PAGE");
							}
							break;
						case "signout":
							((Node) e.getSource()).getScene().getWindow().hide();
							Stage stage2 = new Stage();
							LoginPageController login = new LoginPageController();
							try
							{
								login.logoutWorker(stage2, organization);
							} catch (Exception e1)
							{
								e1.printStackTrace();
							}
							break;
						}
					});
				}

			HamburgerBasicCloseTransition basicClose = new HamburgerBasicCloseTransition(menu);
			basicClose.setRate(-1);

			menu.addEventHandler(MouseEvent.MOUSE_PRESSED, (e) ->
			{
				basicClose.setRate(basicClose.getRate() * -1);
				basicClose.play();

				if (drawer.isOpened())
				{
					drawer.close();
					drawerpane.setMouseTransparent(true);
				} else
				{
					drawer.open();
					drawerpane.setMouseTransparent(false);
				}

			});
		} catch (IOException e)
		{
			e.printStackTrace();
		}
	}

	/**
	 * sendOrderRequest method respond to user click on "book" button
	 * the method makes a request to the server to add the order to the orders list
	 * @param event clicking on "book" button
	 */
	@FXML
	void sendOrderRequest(ActionEvent event)
	{
		timeSlotCombo.setStyle("-fx-border-color: black; -fx-border-width: 2 2 2 2;");
		fldTravelerID.setStyle("-fx-border-color: black; -fx-border-width: 2 2 2 2;");
		Order order = new Order();
		if (fldTravelerID.getText() == null || timeSlotCombo.getValue() == null)
		{
			if (fldTravelerID.getText() == null)
				fldTravelerID.setStyle("-fx-border-color: red; -fx-border-width: 2 2 2 2;");
			if (timeSlotCombo.getValue() == null)
				timeSlotCombo.setStyle("-fx-border-color: red; -fx-border-width: 2 2 2 2;");
			alert("Empty field", "You must fill all the fields.");
		} else if ((timeSlotCombo.getValue().equals("08:00 - 12:00")
				&& LocalTime.now().isAfter(LocalTime.parse("12:00:00")))
				|| (timeSlotCombo.getValue().equals("12:00 - 16:00")
						&& LocalTime.now().isAfter(LocalTime.parse("16:00:00"))))
		{
			timeSlotCombo.setStyle("-fx-border-color: red; -fx-border-width: 2 2 2 2;");
			alert("Wrong time slot", "The time slot is already passed.");
		} else if (fldTravelerID.getText().contains("[a-zA-Z]+") || fldTravelerID.getText().length() != 9)
		{
			fldTravelerID.setStyle("-fx-border-color: red; -fx-border-width: 2 2 2 2;");
			alert("Wrong id number", "Id number must contains 9 digits.");
		}
		else
		{
			order.setIdentification(fldTravelerID.getText());// identification = traveler id number
			order.setParkName(organization.getOrganization());
			order.setDate(LocalDate.now());
			order.setTimeSlot(timeSlotCombo.getValue());
			// order.setInstructor(yesRadioButton.isSelected());
			if (yesRadioButton.isSelected())
				order.setType("instructor");
			order.setNumberOfVisitors(spnNumberOfVisitors.getValue());

			order.setTraveler(null);

			// check if the order added
			ClientUI.chat.accept(new CheckIfOrderSucceededMessage(order), new OnResponseListener()
			{
				@Override
				public void onResponse(Object message)
				{// suppose that we get orderNumber - the order succeded
					if (ChatClient.str.equals("false"))
					{
						// something when wrong
						Platform.runLater(() ->
						{
							alert("Something went wrong", "Please try again later.");
						});
					} else
					{ // Succeeded to make an order
						Platform.runLater(() ->
						{
							// alert("Order added successfuly","Order id: " + ChatClient.str);
							String[] s = ChatClient.str.split(" ");
							((Node) event.getSource()).getScene().getWindow().hide();
							Stage stage = new Stage();
							ParkEntranceController parkEntrance = new ParkEntranceController();
							try
							{
								parkEntrance.start2(stage, organization, s, order.getNumberOfVisitors());
							} catch (Exception e)
							{
								e.printStackTrace();
								System.out.println("FAILED TO OPEN PARKMANAGER PAGE");
							}

							alert("Order added successfully", "Order id: " + s[0]);
						});
					}
				}
			});

		}

	}


	/**
	 * The method respond to user click on  "back" button
	 * @param event clicking on "back" button
	 */
	@FXML
	void back(ActionEvent event)
	{
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage stage = new Stage();
		ParkEntranceController parkEntrance = new ParkEntranceController();
		try
		{
			parkEntrance.start(stage, organization);
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FAILED TO OPEN PARKMANAGER PAGE");
		}
	}

	/**
	 * loadParameters method loads the comboboxes and the toggle group
	 */
	private void loadParameters()
	{
		List<String> timeSlots = Arrays.asList("08:00 - 12:00", "12:00 - 16:00");
		timeSlotCombo.setItems(FXCollections.observableArrayList(timeSlots));

		// initiate Toggle Group
		group = new ToggleGroup();
		noRadioButton.setToggleGroup(group);
		yesRadioButton.setToggleGroup(group);
	}

	/**
	 * alert method shows a information dialog to the screen
	 * @param title the title of the information dialog
	 * @param s the content of the information dialog
	 */
	private void alert(String title, String s)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(s);
		alert.show();
	}
}
