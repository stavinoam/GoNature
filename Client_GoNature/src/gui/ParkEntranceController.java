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
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
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
import logic.CurrentNumbersMessage;
import logic.EditOrderMessage;
import logic.LogOutMessage;
import logic.OnResponseListener;
import logic.SearchOrderMessage;
import logic.Worker;

/**
 * A page for the entrance worker
 * show the number of visitors in a specific park
 * @author Yarden Shahar
 *
 */
public class ParkEntranceController implements Initializable
{

	/**
	 * icon to refresh the numbers
	 */
	@FXML
	private FontAwesomeIconView iconRefresh;

	/**
	 * drawer for hamburger menu
	 */
	@FXML
	private AnchorPane drawerpane;

	/**
	 * drawer for hamburger
	 */
	@FXML
	private JFXDrawer drawer;

	/**
	 * hamburger menu
	 */
	@FXML
	private JFXHamburger menu;

	/**
	 * park information title
	 */
	@FXML
	private Label lblParkInfoTitle;

	/**
	 * text field with the number of visitors in the park
	 */
	@FXML
	private TextField fldCurrentNumOfVisitors;

	/**
	 * text field with the number of spares in the park
	 */
	@FXML
	private TextField fldCurrentParkSpares;

	/**
	 * text field to insert the order number
	 */
	@FXML
	private TextField fldOrderNumber;

	/**
	 * button to search the order in the database
	 */
	@FXML
	private JFXButton btnSearch;

	/**
	 * text field with number of visitors
	 */
	@FXML
	private TextField fldNumOfVisitors;

	/**
	 * button to edit the number of visitors in the order
	 */
	@FXML
	private JFXButton btnEdit;

	/**
	 * text field to show the price of the order
	 */
	@FXML
	private TextField fldPrice;

	/**
	 * about label
	 */
	@FXML
	private Label aboutlabel;

	/**
	 * button to book a visit of for occasional visitors
	 */
	@FXML
	private JFXButton btnBookAVisit;

	/**
	 * object appearance of Worker
	 */
	private Worker organization;

	/**
	 * start method to open a window and set a worker
	 * @param stage start new stage
	 * @param w worker object
	 * @throws Exception throws new exception
	 */
	public void start(Stage stage, Worker w) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ParkEntrance.fxml"));
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
		organization = w;
		loadOrganization();
		stage.setOnCloseRequest(event ->
		{
			System.out.println("Stage is closing");
			ClientUI.chat.accept(new LogOutMessage(organization.getUserName()), null);
		});
	}

	/**
	 * start method to open a window, set a worker and load the order data
	 * @param stage start a new stage
	 * @param w worker object
	 * @param s data of order
	 * @param numOfVisitors the number of the visitors in the park
	 * @throws Exception throws a new exception
	 */
	public void start2(Stage stage, Worker w, String[] s, int numOfVisitors) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ParkEntrance.fxml"));
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
		organization = w;
		loadOrganization();
		loadOrderData(s[0], s[1], numOfVisitors);
		stage.setOnCloseRequest(event ->
		{
			System.out.println("Stage is closing");
			ClientUI.chat.accept(new LogOutMessage(organization.getUserName()), null);
		});
	}

	/**
	 * method to book a new visit to the park
	 * @param event buttonaction
	 */
	@FXML
	void gotoBookAVisit(ActionEvent event)
	{
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage stage = new Stage();
		BookAVisit_ParkEntranceController book = new BookAVisit_ParkEntranceController();
		try
		{
			book.start(stage, organization);
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FAILED TO OPEN BOOKING PAGE");
		}
	}

	/**
	 * method to edit the number of visitors of a requested order
	 * @param event buttonaction
	 */
	@FXML
	void editOrder(ActionEvent event)
	{
		if (!fldNumOfVisitors.getText().matches("[0-9]+"))
			alert("Input Error", "Visitors number must contain only numbers.");
		else if (Integer.parseInt(fldNumOfVisitors.getText()) > 15 || Integer.parseInt(fldNumOfVisitors.getText()) == 0)
			alert("Input Error", "Visitors number be a number between 1 to 15.");
		else if (fldOrderNumber.getText().trim().isEmpty())
			alert("Input Error", "You must search an order first.");
		else
		{
			String s = fldOrderNumber.getText() + " " + fldNumOfVisitors.getText();
			ClientUI.chat.accept(new EditOrderMessage(s), new OnResponseListener()
			{
				@Override
				public void onResponse(Object message)
				{
					if (ChatClient.str.equals("false"))
					{
						Platform.runLater(() ->
						{
							alert("Failed", "Something went wrong.");
						});
					} else
					{
						Platform.runLater(() ->
						{
							fldPrice.setText(ChatClient.str);
							alert("Success", "The change was made successfully.");
						});
					}
				}
			});
		}

	}

	/**
	 * method to search an order in the database
	 * @param event actionevent
	 */
	@FXML
	void searchOrder(ActionEvent event)
	{
		if (!fldOrderNumber.getText().matches("[0-9]+"))
			alert("Input Error", "Order number must contain only numbers.");
		else
		{
			ClientUI.chat.accept(new SearchOrderMessage(fldOrderNumber.getText() + " " + organization.getOrganization()), new OnResponseListener()
			{
				@Override
				public void onResponse(Object message)
				{
					if (ChatClient.str.equals("IDNOTFOUND"))
					{
						Platform.runLater(() ->
						{
							alert("Wrong number", "The order is not found.");
						});
					} else if (ChatClient.str.equals("USED"))
					{
						Platform.runLater(() ->
						{
							alert("Used order", "The order number you entered is already used.");
						});
					} else if(ChatClient.str.equals("CANCELED")) {
						Platform.runLater(() ->
						{
							alert("Canceled order", "The order is canceled.");
						});
					}
					else if (ChatClient.str.equals("false"))
					{
						Platform.runLater(() ->
						{
							alert("Failed", "Something went wrong.");
						});
					} else
					{
						Platform.runLater(() ->
						{
							String[] str = new String[2];
							str = ChatClient.str.split(" ");
							fldNumOfVisitors.setText(str[0]);
							fldPrice.setText(str[1]);
						});
					}
				}
			});
		}
	}

	/**
	 * method to refresh the number of visitors in the park
	 * @param event mouseevent to refresh the number of visitors
	 */
	@FXML
	void refreshNumbers(MouseEvent event)
	{
		ClientUI.chat.accept(new CurrentNumbersMessage(organization.getOrganization()), new OnResponseListener()
		{
			@Override
			public void onResponse(Object message)
			{
				if (ChatClient.str.equals("false"))
				{
					Platform.runLater(() ->
					{
						alert("Error", "Something went wrong.");
					});
				} else
				{
					Platform.runLater(() ->
					{
						loadCurrentNumbers(ChatClient.str);
					});
				}
			}
		});
	}

	/**
	 * method to load the current number of visitors in the park
	 * @param s string to load numbers
	 */
	public void loadCurrentNumbers(String s)
	{
		String[] str = s.split(" ");
		fldCurrentNumOfVisitors.setText(str[0]);
		fldCurrentParkSpares.setText(str[1]);
	}

	/**
	 * method to start the page with the number of current visitors
	 */
	public void loadOrganization()
	{
		refreshNumbers(null);
	}

	/**
	 * method to load the data of the order
	 * @param orderId id number of an order
	 * @param orderPrice order price
	 * @param visitorNum number of visitors in the order
	 */
	public void loadOrderData(String orderId, String orderPrice, int visitorNum)// *******************
	{
		fldOrderNumber.setText(orderId);
		fldNumOfVisitors.setText(visitorNum + "");
		fldPrice.setText(orderPrice);
	}

	/**
	 * make a popup alert
	 * @param title title of the popup
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
	 *initialize the hamburger menu
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

}
