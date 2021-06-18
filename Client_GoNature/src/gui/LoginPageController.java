package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;

import client.ChatClient;
import client.ClientUI;
import javafx.application.Application;
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
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.ClientAlreadyConnectedResultMessage;
import logic.LogOutMessage;
import logic.OnResponseListener;
import logic.Subscriber;
import logic.Traveler;
import logic.TravelerSearchMessage;
import logic.Worker;

/**
 * LoginPageController is a controller of LoginPage.fxml FXML page.
 * The controller implements all the functionals of LoginPage.fxml 
 * The controller implements the Initializable interface - implements initialize method
 * The controller uses some other controllers for each anchorPane (tab)
 * @author Stav Anna
 *
 */
public class LoginPageController extends Application implements Initializable
{
	/**
	 * homeStage is the stage of the current page
	 */
	public static Stage homeStage;

	@FXML
	private JFXButton btnWorkerLogin;

	@FXML
	private JFXButton btnInstructorLogin;

	@FXML
	private JFXButton btnIdLogin;

	@FXML
	private JFXHamburger menu;

	@FXML
	private AnchorPane drawerpane;

	@FXML
	private JFXDrawer drawer;

	@FXML
	private AnchorPane anchor;

	@FXML
	private JFXTextField fldID;

	@FXML
	private JFXButton btnLogin;

	@FXML
	private JFXButton btnTravelerLogin;

	@FXML
	private Label lblAboutUs;

	@FXML
	private Label lblCardReader;

	/**
	 * the method respond to user click on "login" button
	 * the method checks if the traveler id appears in the DB
	 * if he does - leads him to the traveler home page
	 * if he doesn't - leads him to the book a visit page as a new traveler (user)
	 * @param event click on "login" button
	 */
	@FXML
	void login(ActionEvent event)
	{
		String id = fldID.getText();
		if (id.contains("[a-zA-Z]+") || id.length() != 9)
			alert("Wrong id number", "Id number must contains 9 digits.");
		else
		{
			if (id.trim().isEmpty())
				alert("Empty field", "You must enter id number.");
			else
			{ // subscriber / traveler with order / traveler without an order
				// check which traveler it is - with order or without
				ClientUI.chat.accept(new TravelerSearchMessage(id), new OnResponseListener()
				{
					@Override
					public void onResponse(Object message)
					{
						if (message instanceof ClientAlreadyConnectedResultMessage)
						{
							Platform.runLater(() ->
							{
								alert("Login error", "You are already connected to the system.\n"
										+ "Please log out from the other instance, then try again.");
							});
						} else if (ChatClient.traveler == null)
						{// traveler without order
							Platform.runLater(() ->
							{
								((Node) event.getSource()).getScene().getWindow().hide();
								Stage stage = new Stage();
								Traveler newTraveler = new Traveler();
								newTraveler.setIdNumber(id);
								newTraveler.setType("regular");
								newTraveler.setNew(true);
								BookAVisitController book = new BookAVisitController();
								try
								{
									book.start(stage, newTraveler);
								} catch (Exception e)
								{
									e.printStackTrace();
									System.out.println("FAILED TO OPEN BOOKING PAGE");
								}
							});
						} else
						{ // traveler WITH order
							Platform.runLater(() ->
							{
								((Node) event.getSource()).getScene().getWindow().hide();
								Stage stage = new Stage();
								TravelerHomePageController traveler = new TravelerHomePageController();
								// TravelerHomePageController traveler = loader.getController();
								// traveler.loadTraveler(ChatClient.traveler);
								try
								{
									traveler.start(stage, ChatClient.traveler);
								} catch (Exception e)
								{
									System.out.println("FAILED TO OPEN TRAVELER HOME PAGE");
								}
							});
						}
					}
				});
			}
		}
	}

	/**
	 * the method loads the travelerLogin anchorPane to the first tab
	 * @param event click on the first tab "Traveler"
	 * @throws IOException if the FXMLLoader can't load the fxml file
	 */
	@FXML
	void travelerLogin(ActionEvent event) throws IOException
	{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/gui/travelerLogin.fxml"));
		anchor.getChildren().setAll(pane);
	}

	/**
	 * the method loads the subscriberLogin anchorPane to the second tab
	 * @param event click on the second tab "Subscriber"
	 * @throws IOException if the FXMLLoader can't load the fxml file
	 */
	@FXML
	void idLogin(ActionEvent event) throws IOException
	{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/gui/subscriberLogin.fxml"));
		anchor.getChildren().setAll(pane);
	}

	/**
	 * the method loads the instructorLogin anchorPane to the third tab
	 * @param event click on the third tab "Instructor"
	 * @throws IOException if the FXMLLoader can't load the fxml file
	 */
	@FXML
	void instructorLogin(ActionEvent event) throws IOException
	{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/gui/instructorLogin.fxml"));
		anchor.getChildren().setAll(pane);
	}

	/**
	 * the method loads the workerLogin anchorPane to the fourth tab
	 * @param event click on the fourth tab "Worker"
	 * @throws IOException if the FXMLLoader can't load the fxml file
	 */
	@FXML
	void workerLogin(ActionEvent event) throws IOException
	{
		AnchorPane pane = FXMLLoader.load(getClass().getResource("/gui/workerLogin.fxml"));
		anchor.getChildren().setAll(pane);
	}

	/**
	 * alert method shows a information dialog to the screen
	 * @param title the title of the information dialog
	 * @param s the content of the information dialog
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
	 * Start method is the method that starts the stage with the match fxml file 
	 * the method implements Application method
	 */
	@Override
	public void start(Stage primaryStage) throws Exception
	{
		homeStage = primaryStage;
		Parent root = FXMLLoader.load(getClass().getResource("/gui/LoginPage.fxml"));
		root.getStylesheets().add(getClass().getResource("/gui/css/main.css").toExternalForm());
		AboutUsController.primaryStage = primaryStage;
		OurParksController.primaryStage = primaryStage;
		PricesListController.primaryStage = primaryStage;
		homeStage.setTitle("GoNature");
		homeStage.setScene(new Scene(root, 700, 470));
		homeStage.getIcons().add(new Image("gui/css/tree.png"));
		homeStage.setResizable(false);
		homeStage.show();
	}

	/**
	 * initialize method is the first method to be run when the controller is starting
	 * the method initializes the parameters of the fxml page
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
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
							Stage stage4 = new Stage();
							try
							{
								LoginPageController loginPageController = new LoginPageController();
								loginPageController.start(stage4);
							} catch (Exception e1)
							{
								System.out.println("FAILED TO OPEN OUR PARKS PAGE");
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
	 * logout method starts the stage with the match fxml file 
	 * @param primaryStage the new stage that is created before calling the start method
	 * @param traveler the traveler that we wants to logout the system
	 * @throws Exception if the FXMLLoader can't load the fxml file
	 */
	public void logout(Stage primaryStage, Traveler traveler) throws Exception
	{
		System.out.println("Stage is closing");
		if(traveler != null & traveler.getType().equals("subscriber"))
			ClientUI.chat.accept(new LogOutMessage(((Subscriber)traveler).getSubscriberNumber()), null);
		ClientUI.chat.accept(new LogOutMessage(traveler.getIdNumber()), null);
		homeStage = primaryStage;
		Parent root = FXMLLoader.load(getClass().getResource("/gui/LoginPage.fxml"));
		root.getStylesheets().add(getClass().getResource("/gui/css/main.css").toExternalForm());
		AboutUsController.primaryStage = primaryStage;
		OurParksController.primaryStage = primaryStage;
		PricesListController.primaryStage = primaryStage;
		homeStage.setTitle("GoNature");
		homeStage.getIcons().add(new Image("gui/css/tree.png"));
		homeStage.setScene(new Scene(root, 700, 470));
		homeStage.setResizable(false);
		homeStage.show();
	}

	/**
	 * logout method starts the stage with the match fxml file 
	 * @param primaryStage the new stage that is created before calling the start method
	 * @param worker the worker that we wants to logout the system
	 * @throws Exception if the FXMLLoader can't load the fxml file
	 */
	public void logoutWorker(Stage primaryStage, Worker worker) throws Exception
	{
		System.out.println("Stage is closing");
		ClientUI.chat.accept(new LogOutMessage(worker.getUserName()), null);
		homeStage = primaryStage;
		Parent root = FXMLLoader.load(getClass().getResource("/gui/LoginPage.fxml"));
		root.getStylesheets().add(getClass().getResource("/gui/css/main.css").toExternalForm());
		AboutUsController.primaryStage = primaryStage;
		OurParksController.primaryStage = primaryStage;
		PricesListController.primaryStage = primaryStage;
		homeStage.setTitle("GoNature");
		homeStage.setScene(new Scene(root, 700, 470));
		homeStage.getIcons().add(new Image("gui/css/tree.png"));
		homeStage.setResizable(false);
		homeStage.show();
	}

}
