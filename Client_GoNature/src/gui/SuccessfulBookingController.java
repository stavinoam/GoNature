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
import logic.OrderListMessage;
import logic.Subscriber;
import logic.Traveler;

/**
 * SuccessfulBookingController is a controller of SuccessfulBooking.fxml FXML page.
 * The controller implements all the functionals of SuccessfulBooking.fxml 
 * The controller implements the Initializable interface - implements initialize method
 * @author Stav Anna
 *
 */
public class SuccessfulBookingController implements Initializable
{
	private Order order;
	private String orderNumber;
	private String price;

	@FXML
	private TextField fldOrderNum;

	@FXML
	private TextField fieldParkName;

	@FXML
	private TextField fieldOrderDate;

	@FXML
	private TextField fieldOrderTime;

	@FXML
	private TextField fieldVisitorsNumber;

	@FXML
	private TextField fieldPrice;

	@FXML
	private JFXButton btnMyOrderList;

	@FXML
	private JFXButton btnBookAnotherVisit;

	@FXML
	private JFXHamburger menu;

	@FXML
	private AnchorPane drawerpane;

	@FXML
	private JFXDrawer drawer;

	/**
	 * Start method is the method that starts the stage with the match fxml file 
	 * @param stage the new stage that is created before calling the start method
	 * @param order the order that has just succeeded
	 * @throws Exception if the FXMLLoader can't load the fxml file
	 */
	void start(Stage stage, Order order) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SuccessfulBook.fxml"));
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

		stage.setOnCloseRequest(event ->
		{
			System.out.println("Stage is closing");
			if(order.getTraveler() != null & order.getType().equals("subscriber"))
				ClientUI.chat.accept(new LogOutMessage(((Subscriber)order.getTraveler()).getSubscriberNumber()), null);
			ClientUI.chat.accept(new LogOutMessage(order.getIdentification()), null);
		});
	}

	/**
	 * the method starts the fxml parameters according to the successful order
	 */
	private void loadParameters()
	{
		fldOrderNum.setText(orderNumber); // loading order number
		fieldParkName.setText(order.getParkName());
		fieldOrderDate.setText(order.getDate().toString());
		fieldOrderTime.setText(order.getTimeSlot());
		fieldVisitorsNumber.setText(order.getNumberOfVisitors() + "");
		// fieldPrice.setText(order.getPrice() + "");
	}

	/**
	 * the method adds the traveler that has just succeeded to book a visit to the DB
	 * @param traveler the traveler that has just succeeded to book a visit
	 */
	private void addTravelerToTravelerList(Traveler traveler)
	{
		ClientUI.chat.accept(new AddTravelerToTravelersMessage(order.getTraveler()), new OnResponseListener()
		{
			@Override
			public void onResponse(Object message)
			{
				if (ChatClient.str.equals("Added"))
				{// added = new order for new traveler
					Platform.runLater(() ->
					{
						System.out.println("Traveler was added to the DB");
					});
				} else if (ChatClient.str.equals("Updated"))
				{// already = traveler already has an order
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
	 * BookANewVisit method respond to user click on "book a new visit" button
	 * the method leads the user to the "book a visit" page
	 * @param event click on "my order list" button
	 */
	@FXML
	void BookANewVisit(ActionEvent event)
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
			System.out.println("FAILED TO OPEN BOOKING PAGE");
		}
	}

	/**
	 * orderList method respond to user click on "my order list" button
	 * the method leads the user to the "my order list" page
	 * @param event click on "my order list" button
	 */
	@FXML
	void orderList(ActionEvent event)
	{
		ClientUI.chat.accept(new OrderListMessage(order.getTraveler()), new OnResponseListener()
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
						orderList.start(stage, order.getTraveler());
					} catch (Exception e)
					{
						e.printStackTrace();
						System.out.println("FAILED TO OPEN BOOKING PAGE");
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
		fldOrderNum.setText(orderNumber); // loading order number
		fieldPrice.setText(price);

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

	/**
	 * the method sets the successful order number and its price to class parameters
	 * @param orderNum the successful order number
	 * @param price the successful order price
	 */
	public void setOrderNumberAndPrice(String orderNum, String price)
	{
		//System.out.println(price);
		this.orderNumber = orderNum;
		this.price = price;
	}
}
