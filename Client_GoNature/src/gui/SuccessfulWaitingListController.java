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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.AddTravelerToTravelersMessage;
import logic.LogOutMessage;
import logic.OnResponseListener;
import logic.Order;
import logic.RemoveOrderFromWaitingListMessage;
import logic.Subscriber;
import logic.Traveler;
import logic.WaitingListMessage;

/**
 * SuccessfulWaitingListController is a controller of SuccessfulWaitingList.fxml FXML page.
 * The controller implements all the functionals of SuccessfulWaitingList.fxml 
 * The controller implements the Initializable interface - implements initialize method
 * @author Stav Anna
 *
 */
public class SuccessfulWaitingListController implements Initializable
{
	@FXML
	private TextField fieldParkName;

	@FXML
	private TextField fieldOrderDate;

	@FXML
	private TextField fieldOrderTime;

	@FXML
	private TextField fieldVisitorsNumber;

	@FXML
	private JFXButton btnMyWaitingLists;

	@FXML
	private JFXButton btnCancelWaitingList;

	@FXML
	private JFXHamburger menu;

	@FXML
	private AnchorPane drawerpane;

	@FXML
	private JFXDrawer drawer;

	private Order order;

	/**
	 * Start method is the method that starts the stage with the match fxml file 
	 * @param stage the new stage that is created before calling the start method
	 * @param order the order that has just added to the waiting list
	 * @throws Exception if the FXMLLoader can't load the fxml file
	 */
	void start(Stage stage, Order order) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SuccessfulWaitingList.fxml"));
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
		this.order = order;
		loadParameters();
		if (order.getTraveler() != null)
			addTravelerToTravelerList(order.getTraveler());
		
		stage.setOnCloseRequest(event -> {
		    System.out.println("Stage is closing");
		    if(order.getTraveler() != null & order.getType().equals("subscriber"))
				ClientUI.chat.accept(new LogOutMessage(((Subscriber)order.getTraveler()).getSubscriberNumber()), null);
		    ClientUI.chat.accept(new LogOutMessage(order.getIdentification()), null);
		});
	}

	/**
	 * the method starts the fxml parameters according to the waiting list order
	 */
	private void loadParameters()
	{
		fieldParkName.setText(order.getParkName());
		fieldOrderDate.setText(order.getDate().toString());
		fieldOrderTime.setText(order.getTimeSlot());
		fieldVisitorsNumber.setText(order.getNumberOfVisitors() + "");
	}

	/**
	 * the method adds the traveler that has just joined to waiting list to the DB
	 * @param traveler the traveler that has just joined to waiting list
	 */
	private void addTravelerToTravelerList(Traveler traveler)
	{
		ClientUI.chat.accept(new AddTravelerToTravelersMessage(order.getTraveler()), new OnResponseListener()
		{
			@Override
			public void onResponse(Object message)
			{
				if (ChatClient.str.equals("Added"))
				{
					// added = new order for new traveler
					Platform.runLater(() ->
					{
						System.out.println("Traveler was added to the DB");
					});
				} else if (ChatClient.str.equals("Updated"))
				{
					// already = traveler already has an order
					Platform.runLater(() ->
					{
						System.out.println("Traveler was updated");
					});
				} else
				{ // something went wrong: str == null
					Platform.runLater(() ->
					{
						System.out.println("str == null: adding traveler falied");
					});
				}
			}
		});
	}

	/**
	 * cancelWitingList method respond to user click on "cancel waiting list" button
	 * the method leads the user to the "book a visit" page
	 * @param event click on "my order list" button
	 */
	@FXML
	void cancelWitingList(ActionEvent event)
	{
		ClientUI.chat.accept(new RemoveOrderFromWaitingListMessage(order), new OnResponseListener()
		{
			@Override
			public void onResponse(Object message)
			{
				if (ChatClient.flag)
					Platform.runLater(() ->
					{
						((Node) event.getSource()).getScene().getWindow().hide();
						Stage stage = new Stage();
						BookAVisitController book = new BookAVisitController();
						try
						{
							book.start(stage, order.getTraveler());
						} catch (Exception e)
						{
							e.printStackTrace();
							System.out.println("FAILED TO OPEN BOOK A VISIT PAGE");
						}
					});
			}
		});
	}

	/**
	 * waitingListsPage method respond to user click on "my waiting list" button
	 * the method leads the user to the "my waiting list" page
	 * @param event click on "my order list" button
	 */
	@FXML
	void waitingListsPage(ActionEvent event)
	{
		ClientUI.chat.accept(new WaitingListMessage(order.getTraveler()), new OnResponseListener()
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
						waitingList.start(stage, order.getTraveler());
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
								travelerHomePageController.start(stage4, order.getTraveler());
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
								login.logout(stage2, order.getTraveler());
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
