package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;

import client.ChatClient;
import client.ClientUI;
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
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.InstructorRegistrationMessage;
import logic.LogOutMessage;
import logic.OnResponseListener;
import logic.SubscriberRegistrationMessage;
import logic.Worker;

/**
 * A page for service representative to register users.
 * @author Yarden Shahar
 *
 */
public class RegistrationController implements Initializable
{

	/**
	 * title of registration page
	 */
	@FXML
	private Label registrationTitle;

	/**
	 * label fill all the fields
	 */
	@FXML
	private Label fillfieldslabel;

	/**
	 * label of park
	 */
	@FXML
	private Label parklabel;

	/**
	 * field of first name
	 */
	@FXML
	private TextField fldFname;

	/**
	 * date label
	 */
	@FXML
	private Label datelabel;

	/**
	 * field of last name
	 */
	@FXML
	private TextField fldLname;

	/**
	 * time label
	 */
	@FXML
	private Label timelabel;

	/**
	 * field of id number
	 */
	@FXML
	private TextField fldID;

	/**
	 * email label to register a user
	 */
	@FXML
	private Label emaillabel1;

	/**
	 * combobox with phone prefixes
	 */
	@FXML
	private ComboBox<String> comboPhone;

	/**
	 * field to type 7 left digits of phone
	 */
	@FXML
	private TextField fldPhone;

	/**
	 * email label
	 */
	@FXML
	private Label emaillabel;

	/**
	 * field to type email of user
	 */
	@FXML
	private TextField fldEmail;

	/**
	 * label to type an email
	 */
	@FXML
	private Label emaillabel2;

	/**
	 * spinner to choose number of visitors
	 */
	@FXML
	private Spinner<Integer> spnVisitorsNum;

	/**
	 * button to sign up a subscriber
	 */
	@FXML
	private JFXButton btnSignupSub;
	
	/**
	 * button to sign up an instructor
	 */
	@FXML
	private JFXButton btnSignupInst;

	/**
	 * button to clear all fields
	 */
	@FXML
	private JFXButton btnCancel;

	/**
	 * hamburger menu
	 */
	@FXML
	private JFXHamburger menu;

	/**
	 * drawer pane for hamburger
	 */
	@FXML
	private AnchorPane drawerpane;

	/**
	 * drawer for hamburger
	 */
	@FXML
	private JFXDrawer drawer;

	/**
	 * list of phone prefixes
	 */
	ObservableList<String> list = FXCollections.observableArrayList("050", "052", "053", "054", "055", "058");
	/**
	 * initialize the spinner
	 */
	SpinnerValueFactory<Integer> valueFactoryVistorsNum = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 15, 1);
	/**
	 * object appearance of Worker
	 */
	private Worker worker;

	/**
	 * start method to open window and set worker
	 * @param primaryStage start a new stage
	 * @param w worker object
	 * @throws Exception throws a new exception
	 */
	public void start(Stage primaryStage, Worker w) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Registration.fxml"));
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
		primaryStage.setOnCloseRequest(event ->
		{
			System.out.println("Stage is closing");
			ClientUI.chat.accept(new LogOutMessage(worker.getUserName()), null);
		});
	}

	/**
	 * method to clear all the fields
	 * @param event buttonaction
	 * @throws IOException throws a new input output exception
	 */
	@FXML
	void cancel(ActionEvent event) throws IOException
	{
		SpinnerValueFactory<Integer> number = new SpinnerValueFactory.IntegerSpinnerValueFactory(1, 15, 1);
		fldFname.setText("");
		fldLname.setText("");
		fldID.setText("");
		fldEmail.setText("");
		fldPhone.setText("");
		comboPhone.getSelectionModel().clearSelection();
		spnVisitorsNum.setValueFactory(number);
	}

	/**
	 * message to server to sign up an instructor
	 * @param event buttonaction
	 */
	@FXML
	void signUpInstructor(ActionEvent event)
	{
		if (!validation())
			;
		else if (spnVisitorsNum.getValue() > 1)
			alert("Input Error", "Number of visitors can be bigger than 1 only for subscribers.");
		else
		{
			String[] s = getFields();
			ClientUI.chat.accept(new InstructorRegistrationMessage(s), new OnResponseListener()
			{
				@Override
				public void onResponse(Object message)
				{
					if (ChatClient.str.equals("true"))
					{
						Platform.runLater(() ->
						{
							try
							{
								((Node) event.getSource()).getScene().getWindow().hide();
								Stage stage = new Stage();
								InstructorSuccessController instructorSuccessController = new InstructorSuccessController();
								instructorSuccessController.start(stage, ChatClient.worker);
							} catch (Exception e)
							{
								e.printStackTrace();
								System.out.println("FAILED TO OPEN PARK !WORKER HOME PAGE");
							}
						});
					} else if (ChatClient.str.equals("false"))
					{
						Platform.runLater(() ->
						{
							alert("Error", "Something went wrong");
						});
					} else if (ChatClient.str.equals("ID"))
					{
						Platform.runLater(() ->
						{
							alert("Input Error", "ID already exists");
						});
					} else if (ChatClient.str.equals("Email"))
					{
						Platform.runLater(() ->
						{
							alert("Input Error", "Email already exists");
						});
					} else if (ChatClient.str.equals("Phone"))
					{
						Platform.runLater(() ->
						{
							alert("Input Error", "Phone already exists");
						});
					}
				}
			});
		}

	}

	/**
	 * message to server to sign up a subscriber
	 * @param event buttonaction
	 */
	@FXML
	void signUpSubscriber(ActionEvent event) throws IOException
	{

		if (!validation())
			;
		else
		{
			String[] s = getFields();
			ClientUI.chat.accept(new SubscriberRegistrationMessage(s), new OnResponseListener()
			{
				@Override
				public void onResponse(Object message)
				{
					if (ChatClient.str.contains("true"))
					{
						Platform.runLater(() ->
						{		
							try
							{
								((Node) event.getSource()).getScene().getWindow().hide();
								Stage stage = new Stage();
								PaymentController paymentController = new PaymentController();
								paymentController.start(stage, worker, ChatClient.str);
							} catch (Exception e)
							{
								e.printStackTrace();
								System.out.println("FAILED TO OPEN PARK !WORKER HOME PAGE");
							}
						});
					} else if (ChatClient.str.equals("false"))
					{
						Platform.runLater(() ->
						{
							alert("Error", "Something went wrong");
						});
					} else if (ChatClient.str.equals("ID"))
					{
						Platform.runLater(() ->
						{
							alert("Input Error", "ID already exists");
						});
					} else if (ChatClient.str.equals("Email"))
					{
						Platform.runLater(() ->
						{
							alert("Input Error", "Email already exists");
						});
					} else if (ChatClient.str.equals("Phone"))
					{
						Platform.runLater(() ->
						{
							alert("Input Error", "Phone already exists");
						});
					}
				}
			});
		}

	}

	/**
	 * initialize comboboxs, spinner and hamburger menu
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		comboPhone.setItems(list);
		spnVisitorsNum.setValueFactory(valueFactoryVistorsNum);

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
	 * make a popup alert
	 * @param title the title of the popup
	 * @param s the text of the popup
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
	 * method to check if the email is valid
	 * @param email string that contains an email
	 * @return boolean variable
	 */
	public boolean isValidEmailAddress(String email)
	{
		String ePattern = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$";
		Pattern p = Pattern.compile(ePattern);
		Matcher m = p.matcher(email);
		return m.matches();
	}

	/**
	 * method to check if the values of the fields are correct
	 * @return boolean variable that indicates if validation was correct
	 */
	public boolean validation()
	{
		if (fldFname.getText().trim().isEmpty() || fldLname.getText().trim().isEmpty()
				|| fldID.getText().trim().isEmpty() || fldPhone.getText().trim().isEmpty()
				|| fldEmail.getText().trim().isEmpty())
		{
			alert("Input Error", "All fields required.");
			return false;
		} else if (!fldFname.getText().matches("[a-zA-Z]+"))
		{
			alert("Input Error", "First name is invalid");
			return false;
		} else if (!fldLname.getText().matches("[a-zA-Z]+"))
		{
			alert("Input Error", "Last name is invalid");
			return false;
		} else if (!fldID.getText().matches("[0-9]+") || fldID.getText().length() != 9)
		{
			alert("Input Error", "ID number must contain 9 digits.");
			return false;
		} else if (!fldPhone.getText().matches("[0-9]+") || fldPhone.getText().length() != 7)
		{
			alert("Input Error", "Phone number is not valid.");
			return false;
		} else if (!isValidEmailAddress(fldEmail.getText()))
		{
			alert("Input Error", "Email is not valid.");
			return false;
		}
		return true;

	}

	/**
	 * method to save all the fields
	 * @return string array with the field of the form
	 */
	public String[] getFields()
	{
		String[] s = new String[7];
		s[0] = fldFname.getText();
		s[1] = fldLname.getText();
		s[2] = fldID.getText();
		if (comboPhone.getValue() == (null))
			s[3] = "050" + fldPhone.getText();
		else
			s[3] = comboPhone.getValue() + fldPhone.getText();
		s[4] = fldEmail.getText();
		s[5] = String.valueOf(spnVisitorsNum.getValue());
		return s;
	}

}
