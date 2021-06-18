package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;

import client.ChatClient;
import client.ClientUI;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleGroup;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.LogOutMessage;
import logic.OnResponseListener;
import logic.PaymentMessage;
import logic.Worker;

/**
 * A page to insert payment details of a subscriber.
 * @author Yarden Shahar
 *
 */
public class PaymentController extends Application implements Initializable
{

	/**
	 * page title
	 */
	@FXML
	private Label registrationTitle;

	/**
	 * label of fields
	 */
	@FXML
	private Label fillfieldslabel;

	/**
	 * label of fields
	 */
	@FXML
	private Label fillfieldslabel1;

	/**
	 * radio button of visa card type
	 */
	@FXML
	private RadioButton radioVisa;

	/**
	 * label to show a picture of visa card
	 */
	@FXML
	private Label lblVisa;

	/**
	 * radio button of mastercard type
	 */
	@FXML
	private RadioButton radioMastercard;

	/**
	 * label to show a picture of mastercard
	 */
	@FXML
	private Label lblMastercard;

	/**
	 * radio button of express card type
	 */
	@FXML
	private RadioButton radioExpress;

	/**
	 * label to show an american express card
	 */
	@FXML
	private Label lblExpress;

	/**
	 * radio button of dinners card type
	 */
	@FXML
	private RadioButton radioDinners;

	/**
	 * label to show dinners card
	 */
	@FXML
	private Label lblDinners;

	/**
	 * time label
	 */
	@FXML
	private Label timelabel;

	/**
	 * first field of card
	 */
	@FXML
	private TextField fldCard1;
	/**
	 * second field of card
	 */
	@FXML
	private TextField fldCard2;
	/**
	 * third field of card
	 */
	@FXML
	private TextField fldCard3;
	/**
	 * fourth field of card
	 */
	@FXML
	private TextField fldCard4;

	/**
	 * email label
	 */
	@FXML
	private Label emaillabel1;

	/**
	 * combobox with month expiration date
	 */
	@FXML
	private ComboBox<String> comboMonth;
	/**
	 * combobox with year expiration date
	 */
	@FXML
	private ComboBox<String> comboYear;

	/**
	 * label of email
	 */
	@FXML
	private Label emaillabel;

	/**
	 * question mark to explain about cvv
	 */
	@FXML
	private Hyperlink question_mark;

	/**
	 * textfield cvv of card
	 */
	@FXML
	private TextField fldCVV;

	/**
	 * email label
	 */
	@FXML
	private Label emaillabel2;

	/**
	 * identification of card owner
	 */
	@FXML
	private TextField fldID;

	/**
	 * full name of card owner
	 */
	@FXML
	private TextField fldName;

    /**
     * button to move to the next page
     */
    @FXML
    private JFXButton btnFinish;

    /**
     * button to pass on inserting a payment method
     */
    @FXML
    private JFXButton btnContinue;

    /**
     * hamburger menu
     */
    @FXML
    private JFXHamburger menu;

    /**
     * anchorpane for hamburger
     */
    @FXML
    private AnchorPane drawerpane;

    /**
     * drawer for hamburger
     */
    @FXML
    private JFXDrawer drawer;

	/**
	 * subscriber number
	 */
	private int subNum;
	/**
	 * group of radiobuttons
	 */
	private ToggleGroup group;
	/**
	 * list of months for expiration date
	 */
	ObservableList<String> monthList = FXCollections.observableArrayList("01", "02", "03", "04", "05", "06", "07", "08",
			"09", "10", "11", "12");
	/**
	 * list of years for expiration date
	 */
	ObservableList<String> yearList = FXCollections.observableArrayList("2021", "2022", "2023", "2024", "2025", "2026",
			"2027", "2028", "2029", "2030");

	/**
	 * object appearance of Worker
	 */
	private Worker worker;
	
	/**
	 * start method to open a window and set worker
	 * @param primaryStage start new stage
	 * @param w worker object
	 * @param s the subscriber number
	 * @throws Exception throws a new exception
	 */
	public void start(Stage primaryStage, Worker w, String s) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Payment.fxml"));
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
		this.worker = w;
		loadSubscriberNum(s);
		primaryStage.setOnCloseRequest(event ->
		{
			System.out.println("Stage is closing");
			ClientUI.chat.accept(new LogOutMessage(worker.getUserName()), null);
		});
	}
	
	/**
	 * method to go to website that explains about cvv
	 * @param event buttonaction
	 */
	@FXML
	void cvvExplain(ActionEvent event)
	{
        getHostServices().showDocument("https://www.cvvnumber.com/");
	}

	/**
	 * finish method to go to the next page
	 * @param event buttonaction
	 */
	@FXML
	void finish(ActionEvent event)
	{
		if (!validation())
			;
		else
		{
			String[] s = getFields();
			ClientUI.chat.accept(new PaymentMessage(s), new OnResponseListener()
			{
				@Override
				public void onResponse(Object message)
				{
					if (ChatClient.flag)
					{
						Platform.runLater(() ->
						{				
							try
							{
								((Node) event.getSource()).getScene().getWindow().hide();
								Stage stage = new Stage();
								SubscriberSuccessController subscriberSuccessController = new SubscriberSuccessController();
								subscriberSuccessController.start(stage, worker, String.valueOf(subNum));
							} catch (Exception e)
							{
								e.printStackTrace();
								System.out.println("FAILED TO OPEN PARK !WORKER HOME PAGE");
							}
						});
					} else
					{
						Platform.runLater(() ->
						{

							Alert alert = new Alert(AlertType.INFORMATION);
							alert.setTitle("Failed");
							alert.setHeaderText(null);
							alert.setContentText("failed adding payment method.");
							alert.show();

						});
					}
				}
			});
		}
	}

	
	/**
	 * method to go to the next page without inserting a payment method
	 * @param event button action
	 * @throws IOException throws a new input output exception
	 */
	@FXML
	void gotoRegistrationSuccess(ActionEvent event) throws IOException
	{
		try
		{
			((Node) event.getSource()).getScene().getWindow().hide();
			Stage stage = new Stage();
			SubscriberSuccessController subscriberSuccessController = new SubscriberSuccessController();
			subscriberSuccessController.start(stage, worker, String.valueOf(subNum));
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FAILED TO OPEN PARK !WORKER HOME PAGE");
		}
	}

	/**
	 * method to save the number of the subscriber
	 * @param str string to load subscriber number
	 */
	public void loadSubscriberNum(String str)
	{
		String s[] = str.split(" ");
		subNum = Integer.parseInt(s[1]); // subscriber number
	}

	/**
	 *initialize the toggle group, comboboxes and hamburger menu
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		initiateToggleGroup();
		comboMonth.setItems(monthList);
		comboYear.setItems(yearList);
		
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
							RegistrationController registrationController = new RegistrationController();
							try
							{
								registrationController.start(stage3, worker);
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
								login.logoutWorker(stage2, worker);
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
	 * method to initialize the togglegroup with radio buttons
	 */
	private void initiateToggleGroup()
	{
		group = new ToggleGroup();
		radioVisa.setUserData("Visa");
		radioVisa.setToggleGroup(group);
		radioMastercard.setUserData("Mastercard");
		radioMastercard.setToggleGroup(group);
		radioExpress.setUserData("American Express");
		radioExpress.setToggleGroup(group);
		radioDinners.setUserData("Dinners");
		radioDinners.setToggleGroup(group);
	}

	/**
	 * method to check if the fields are correct
	 * @return boolean variable
	 */
	public boolean validation()
	{
		if (group.getSelectedToggle() == null || fldCard1.getText().trim().isEmpty()
				|| fldCard2.getText().trim().isEmpty() || fldCard3.getText().trim().isEmpty()
				|| fldCard4.getText().trim().isEmpty() || comboMonth.getValue() == null || comboYear.getValue() == null
				|| fldCVV.getText().trim().isEmpty() || fldID.getText().trim().isEmpty()
				|| fldName.getText().trim().isEmpty())
		{
			alert("Input Error", "All fields required.");
			return false;
		} else if (!fldCard1.getText().matches("[0-9]+") || fldCard1.getText().length() != 4
				|| !fldCard2.getText().matches("[0-9]+") || fldCard2.getText().length() != 4
				|| !fldCard3.getText().matches("[0-9]+") || fldCard3.getText().length() != 4
				|| !fldCard4.getText().matches("[0-9]+") || fldCard4.getText().length() != 4)
		{
			alert("Input Error", "Card number must contain 16 digits, 4 in each field.");
			return false;
		} else if (!fldCVV.getText().matches("[0-9]+") || fldCVV.getText().length() != 3)
		{
			alert("Input Error", "CVV must contain 3 digits.");
			return false;
		} else if (!fldID.getText().matches("[0-9]+") || fldID.getText().length() != 9)
		{
			alert("Input Error", "ID number must contain 9 digits.");
			return false;
		} else if (!fldName.getText().matches("^[ A-Za-z]+$") || fldName.getText().length() > 45)
		{
			alert("Input Error", "Full name must contain only letters. 45 letters or less.");
			return false;
		}
		return true;
	}

	/**
	 * make a popup alert
	 * @param title the title of the popup
	 * @param s text of the popup
	 */
	public void alert(String title, String s)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(s);
		alert.show();
	}

	/**
	 * method to get all the data from the fields
	 * @return string array
	 */
	public String[] getFields()
	{
		String[] s = new String[7];
		s[0] = group.getSelectedToggle().getUserData().toString();
		s[1] = fldCard1.getText() + fldCard2.getText() + fldCard3.getText() + fldCard4.getText();
		s[2] = comboMonth.getValue() + "/" + comboYear.getValue();
		s[3] = fldCVV.getText();
		s[4] = fldID.getText();
		s[5] = fldName.getText();
		s[6] = String.valueOf(subNum);
		return s;
	}

	@Override
	public void start(Stage arg0) throws Exception
	{
	}

}
