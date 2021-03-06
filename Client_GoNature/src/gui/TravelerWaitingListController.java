package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;

import client.ChatClient;
import client.ClientUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.ToggleButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.LogOutMessage;
import logic.MoveWaitingToOrderMessage;
import logic.OnResponseListener;
import logic.Order;
import logic.OrderList;
import logic.RemoveOrderFromWaitingListMessage;
import logic.Subscriber;
import logic.Traveler;

/**
 * TravelerWaitingListController is a controller of TravelerWaitingList.fxml FXML page.
 * The controller implements all the functionals of TravelerWaitingList.fxml 
 * The controller implements the Initializable interface - implements initialize method
 * @author Stav Anna
 *
 */
public class TravelerWaitingListController implements Initializable
{
	@FXML
	private TextField txtSearch;

	@FXML
	private TableView<OrderList> tblWaitingList;

	@FXML
	private TableColumn<OrderList, String> clmnParkName;

	@FXML
	private TableColumn<OrderList, String> clmnVisitorsNumber;

	@FXML
	private TableColumn<OrderList, String> clmnDate;

	@FXML
	private TableColumn<OrderList, String> clmnTime;

	@FXML
	private TableColumn<OrderList, ToggleGroup> clmnAction;

	@FXML
	private JFXButton btnBookAVisit;

	@FXML
	private JFXButton btnBack;

	@FXML
	private JFXHamburger menu;

	@FXML
	private AnchorPane drawerpane;

	@FXML
	private JFXDrawer drawer;

	/**
	 * ObservableList that contains the tblWaitingList rows (instances of OrderList object)
	 */
	ObservableList<OrderList> waitingList = FXCollections.observableArrayList();
	private Traveler traveler;

	/**
	 * Start method is the method that starts the stage with the match fxml file 
	 * @param stage the new stage that is created before calling the start method
	 * @param traveler the traveler that want to see his waiting list
	 * @throws Exception if the FXMLLoader can't load the fxml file
	 */
	public void start(Stage stage, Traveler traveler) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/??TravelerWaitingList.fxml"));
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
		
		stage.setOnCloseRequest(event -> {
		    System.out.println("Stage is closing");
		    if(traveler != null & traveler.getType().equals("subscriber"))
				ClientUI.chat.accept(new LogOutMessage(((Subscriber)traveler).getSubscriberNumber()), null);
		    ClientUI.chat.accept(new LogOutMessage(traveler.getIdNumber()), null);
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
			book.start(stage, traveler);
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FAILED TO OPEN BOOKING PAGE");
		}
	}

	/**
	 * back method respond to user click on "back" button
	 * the method leads the user to the "traveler home page" page
	 * @param event click on "back" button
	 */
	@FXML
	void back(ActionEvent event)
	{
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage stage = new Stage();
		TravelerHomePageController homePage = new TravelerHomePageController();
		try
		{
			homePage.start(stage, traveler);
		} catch (Exception e)
		{
			System.out.println("FAILED TO OPEN TRAVELER HOME PAGE");
		}
	}

	/**
	 * the method loads the traveler's waiting lists to tblWaitingList
	 * @param queryRes the result of the query from the DB
	 */
	public void loadOrders(String queryRes)
	{
		String[] str;
		str = queryRes.split("/");
		int size = str.length / 5;
		OrderList[] ol = new OrderList[size];
		for (int i = 0, j = 0; i < size; i++, j += 5)
		{
			LocalDate date = LocalDate.parse(str[j + 1]);
			String park = str[j], time = str[j + 2], number = str[j + 3];
			ToggleButton book = new ToggleButton("Book");
			ToggleButton cancel = new ToggleButton("Cancel");
			book.setUserData("Book");
			cancel.setUserData("Cancel");
			book.setStyle("-fx-background-color: #B7EB9E;");
			cancel.setStyle("-fx-background-color: #f05b59;");
			final ToggleGroup group = new ToggleGroup();
			book.setToggleGroup(group);
			cancel.setToggleGroup(group);
			if (str[j + 4].equals("0"))
				book.setVisible(false);
			OrderList orderList = new OrderList(park, str[j + 1], time, number, group);
			ol[i] = orderList;
			waitingList.add(ol[i]);

			book.setOnMouseClicked(new EventHandler<Event>()
			{

				@Override
				public void handle(Event arg0)
				{
					Order order = setOrder(park, date, time, Integer.parseInt(number));
					ClientUI.chat.accept(new MoveWaitingToOrderMessage(order), new OnResponseListener()
					{
						@Override
						public void onResponse(Object message)
						{
							String[] s = ChatClient.str.split(" ");
							if (!ChatClient.str.equals("false"))
								Platform.runLater(() ->
								{
									waitingList.remove(orderList);
									tblWaitingList.setItems(waitingList);
									alert("Successfully booked", "Order " + s[0] + " was successfully booked!");
								});
							else
							{
								Platform.runLater(() ->
								{
									alert("Something went wrong", "Please try again later.");
								});
							}
						}
					});
				}
			});

			cancel.setOnMouseClicked(new EventHandler<Event>()
			{

				@Override
				public void handle(Event event)
				{
					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.setTitle("Cancel waiting order");
					alert.setHeaderText(null);
					alert.setContentText("Are you sure you want to cancel this order from waiting list?");
					ButtonType yes = new ButtonType("Yes");
					ButtonType no = new ButtonType("No");

					alert.getButtonTypes().setAll(yes, no);

					Optional<ButtonType> result = alert.showAndWait();
					if (result.get() == yes)
					{
						Order order = setOrder(park, date, time, Integer.parseInt(number));
						ClientUI.chat.accept(new RemoveOrderFromWaitingListMessage(order), new OnResponseListener()
						{
							@Override
							public void onResponse(Object message)
							{
								if (ChatClient.flag)
								{
									Platform.runLater(() ->
									{
										waitingList.remove(orderList);
										tblWaitingList.setItems(waitingList);
										alert("Successfully canceled", "Order was deleted from the waiting list");
									});
								} else
								{
									Platform.runLater(() ->
									{
										alert("Something went wrong", "Please try again later.");
									});
								}
							}
						});
					} else if (result.get() == no)
					{
						// ... user chose "No"
					}

				}
			});
		}
	}

	
	/**
	 * the method creates an instance of Order object and returns it
	 * @param park the park name of the order
	 * @param date the order date
	 * @param time the order time
	 * @param number order number of visitors
	 * @return a new instance of Order object with the data
	 */
	private Order setOrder(String park, LocalDate date, String time, int number)
	{
		Order order = new Order();
		order.setParkName(park);
		order.setDate(date);
		order.setTimeSlot(time);
		order.setNumberOfVisitors(number);
		order.setTraveler(traveler);
		order.setIdentification(traveler.getIdNumber());
		order.setEmail(traveler.getEmail());
		order.setType(traveler.getType());
		return order;
	}

	/**
	 * initialize method is the first method to be run when the controller is starting
	 * the method initializes the parameters of the fxml page
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		clmnParkName.setCellValueFactory(new PropertyValueFactory<>("ParkName"));
		clmnDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
		clmnTime.setCellValueFactory(new PropertyValueFactory<>("Time"));
		clmnVisitorsNumber.setCellValueFactory(new PropertyValueFactory<>("VisitorsNumber"));

		clmnAction.setCellFactory(column -> new TableCell<OrderList, ToggleGroup>()
		{
			@Override
			protected void updateItem(ToggleGroup item, boolean empty)
			{
				super.updateItem(item, empty);
				if (!empty)
				{
					HBox hb = new HBox(7);
					hb.setAlignment(Pos.CENTER);
					item.getToggles().forEach(toggle -> hb.getChildren().add(((ToggleButton) toggle)));
					setGraphic(hb);
				} else
				{
					setGraphic(null); // This will clear the text for empty rows
				}
			}
		});
		clmnAction.setCellValueFactory(new PropertyValueFactory<>("Action2"));

//		tblWaitingList.setItems(waitingList);

		FilteredList<OrderList> filteredData = new FilteredList<>(waitingList, b -> true);

		txtSearch.textProperty().addListener((observable, oldValue, newValue) ->
		{
			filteredData.setPredicate(order ->
			{
				if (newValue == null || newValue.isEmpty())
				{
					return true;
				}
				String lowerCaseFilter = newValue.toLowerCase();
				if (order.getParkName().toLowerCase().contains(lowerCaseFilter))
				{
					return true;
				}
				return false; // does not match
			});
		});

		SortedList<OrderList> sortedData = new SortedList<>(filteredData);

		sortedData.comparatorProperty().bind(tblWaitingList.comparatorProperty());

		tblWaitingList.setItems(sortedData);
		
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
