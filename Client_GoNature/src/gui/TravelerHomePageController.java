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
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.LogOutMessage;
import logic.OnResponseListener;
import logic.OrderListMessage;
import logic.Subscriber;
import logic.Traveler;
import logic.WaitingListMessage;

/**
 * TravelerHomePageController is a controller of TravelerHomePage.fxml FXML page.
 * The controller implements all the functionals of TravelerHomePage.fxml 
 * The controller implements the Initializable interface - implements initialize method
 * @author Stav Anna
 *
 */
public class TravelerHomePageController implements Initializable
{
	private Traveler traveler;

	@FXML
	private Label lblHomePage;

	@FXML
	private JFXButton btnWaitingLists;

	@FXML
	private JFXButton btnMyOrders;

	@FXML
	private JFXButton btnBookAVisit;

	@FXML
	private JFXHamburger menu;

	@FXML
	private AnchorPane drawerpane;

	@FXML
	private JFXDrawer drawer;
	
    @FXML
    private Label lblWelcome;

	/**
	 * Start method is the method that starts the stage with the match fxml file 
	 * @param stage the new stage that is created before calling the start method
	 * @param traveler the traveler that has just logged into the system
	 * @throws Exception if the FXMLLoader can't load the fxml file
	 */
	public void start(Stage stage, Traveler traveler) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/TravelerHomePage.fxml"));
		loader.setController(this);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Style.css").toExternalForm());
		AboutUsController.primaryStage = stage;
		OurParksController.primaryStage = stage;
		PricesListController.primaryStage = stage;
		stage.setTitle("GoNature");
		stage.setScene(scene);
		stage.getIcons().add(new Image("gui/css/tree.png"));
		stage.show();
		this.traveler = traveler;
		loadTraveler();
		
		stage.setOnCloseRequest(event -> {
		    System.out.println("Stage is closing");
		    if(traveler != null & traveler.getType().equals("subscriber"))
				ClientUI.chat.accept(new LogOutMessage(((Subscriber)traveler).getSubscriberNumber()), null);
		    ClientUI.chat.accept(new LogOutMessage(traveler.getIdNumber()), null);
		});
	}

	/**
	 * the method loads the welcome title according to the traveler data
	 */
	public void loadTraveler()
	{
		if(traveler.getfName() != null) //subscriber or instructor
			lblWelcome.setText("Welcome " + traveler.getfName());
		else {//traveler with no name
			lblWelcome.setText("Welcome " + traveler.getIdNumber());
		}
	}

	/**
	 * bookAVisit method respond to user click on "book a visit" button
	 * the method leads the user to the "book a visit" page
	 * @param event click on "my order list" button
	 */
	@FXML
	void bookAVisit(ActionEvent event)
	{
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage stage = new Stage();
		BookAVisitController book = new BookAVisitController();
		try
		{
			book.start(stage, traveler);
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FAILED TO OPEN BOOKING PAGE");
		}
	}

	/**
	 * myOrdersList method respond to user click on "my order list" button
	 * the method leads the user to the "my order list" page
	 * @param event click on "my order list" button
	 */
	@FXML
	void myOrdersList(ActionEvent event)
	{
		ClientUI.chat.accept(new OrderListMessage(traveler), new OnResponseListener()
		{
			@Override
			public void onResponse(Object message)
			{
				Platform.runLater(() ->
				{
					((Node) event.getSource()).getScene().getWindow().hide();
					Stage stage = new Stage();
					TravelerOrderListController orderList = new TravelerOrderListController();
					try
					{
						orderList.setOrders(ChatClient.str);
						orderList.start(stage, traveler);
					} catch (Exception e)
					{
						e.printStackTrace();
						System.out.println("FAILED TO OPEN ORDER LIST PAGE");
					}
				});
			}
		});
	}

	/**
	 * waitingLists method respond to user click on "my waiting lists" button
	 * the method leads the user to the "my waiting list" page
	 * @param event click on "my order list" button
	 */
	@FXML
	void waitingLists(ActionEvent event)
	{
		ClientUI.chat.accept(new WaitingListMessage(traveler), new OnResponseListener()
		{
			@Override
			public void onResponse(Object message)
			{
				Platform.runLater(() ->
				{
					((Node) event.getSource()).getScene().getWindow().hide();
					Stage stage = new Stage();
					TravelerWaitingListController waitingList = new TravelerWaitingListController();
					try
					{
						waitingList.loadOrders(ChatClient.str);
						waitingList.start(stage, traveler);
					} catch (Exception e)
					{
						e.printStackTrace();
						System.out.println("FAILED TO OPEN WAITING LIST PAGE");
					}
				});
			}
		});
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
								TravelerHomePageController travelerHomePageController = new TravelerHomePageController();
								travelerHomePageController.start(stage4, traveler);
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
							((Node) e.getSource()).getScene().getWindow().hide();
							Stage stage2 = new Stage();
							LoginPageController login = new LoginPageController();
							try
							{
								login.logout(stage2, traveler);
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

}
