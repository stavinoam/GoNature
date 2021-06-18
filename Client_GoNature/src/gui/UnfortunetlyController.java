package gui;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;

import client.ChatClient;
import client.ClientUI;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.AddOrderToWaitingListMessage;
import logic.AvailableDates;
import logic.CheckIfOrderSucceededMessage;
import logic.LogOutMessage;
import logic.OnResponseListener;
import logic.Order;
import logic.Subscriber;

/**
 * UnfortunetlyController is a controller of Unfortunetly.fxml FXML page.
 * The controller implements all the functionals of Unfortunetly.fxml 
 * The controller implements the Initializable interface - implements initialize method
 * @author Stav Anna
 *
 */
public class UnfortunetlyController implements Initializable {
	
    @FXML
    private Label sad;

    @FXML
    private JFXHamburger menu;

    @FXML
    private AnchorPane drawerpane;

    @FXML
    private JFXDrawer drawer;
    
	private Order order;

	@FXML
	private TableView<AvailableDates> tblAvailableDates;

	@FXML
	private TableColumn<AvailableDates, String> clmnDate;

	@FXML
	private TableColumn<AvailableDates, String> clmnTime;

	@FXML
	private TableColumn<AvailableDates, Button> clmnPick;

	/**
	 * ObservableList that contains the tblAvailableDates rows (instances of AvailableDates object)
	 */
	ObservableList<AvailableDates> dates = FXCollections.observableArrayList();

	@FXML
	private Button btnWaitingList;

	/**
	 * Start method is the method that starts the stage with the match fxml file 
	 * @param stage the new stage that is created before calling the start method
	 * @param order the order that the traveler has just failed to book
	 * @throws Exception if the FXMLLoader can't load the fxml file
	 */
	void start(Stage stage, Order order) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Unfortunately.fxml"));
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
		
		stage.setOnCloseRequest(event -> {
		    System.out.println("Stage is closing");
		    if(order.getTraveler() != null & order.getType().equals("subscriber"))
				ClientUI.chat.accept(new LogOutMessage(((Subscriber)order.getTraveler()).getSubscriberNumber()), null);
		    ClientUI.chat.accept(new LogOutMessage(order.getIdentification()), null);
		});
	}

	/**
	 * addToWaitingList method respond to user click on "join the waiting list" button
	 * the method leads the user to the "successfuly added to waiting list" page
	 * or else, pops-up a match message
	 * @param event click on "join the waiting list" button
	 */
	@FXML
	void addToWaitingList(ActionEvent event) {
		ClientUI.chat.accept(new AddOrderToWaitingListMessage(order), new OnResponseListener() {
			@Override
			public void onResponse(Object message) {
				if (ChatClient.str.equals("Added")) {
					Platform.runLater(() -> {
						((Node) event.getSource()).getScene().getWindow().hide();
						Stage stage = new Stage();
						SuccessfulWaitingListController waitingList = new SuccessfulWaitingListController();
						try {
							waitingList.start(stage, order);
						} catch (Exception e) {
							e.printStackTrace();
							System.out.println("FAILED TO OPEN Successful waiting list PAGE");
						}
					});
				} else if (ChatClient.str.equals("Already")) {
					Platform.runLater(() -> {
						alert("Already Exists", "You already have a waiting list request for the slot.");
					});
				} else { // false
					Platform.runLater(() -> {
						alert("Something went wrong", "Please try again later.");
					});
				}
			}
		});
	}

	/**
	 * the method loads alternative available dates for the order to tblAvailableDates
	 * @param s the available dates for the order to tblAvailableDates
	 */
	public void loadDates(String s) {
		String[] str;
		str = s.split("/");
		int size = str.length / 2;// rows number
		AvailableDates[] ad = new AvailableDates[size];
		for (int i = 0, j = 0; i < str.length; j++, i += 2) {
			LocalDate date = LocalDate.parse(str[i]);
			String time = str[i + 1];
			Button book = new Button("Book");
			book.setPrefSize(50, 25);
			book.setStyle("-fx-background-color: #B7EB9E;");
			book.setOnMouseClicked(new EventHandler<Event>() {

				@Override
				public void handle(Event event) {
					order.setDate(date);
					order.setTimeSlot(time);
					ClientUI.chat.accept(new CheckIfOrderSucceededMessage(order), new OnResponseListener() {

						@Override
						public void onResponse(Object message) {
							if (ChatClient.str.equals("AlreadyExist")) {
								Platform.runLater(() -> {
									alert("Multiply orders", "You already have and order to the same park and date");
								});
							} else { // the date is OK for amount
								// str = orderID
								Platform.runLater(() -> {
									String[] s = ChatClient.str.split(" ");
									((Node) event.getSource()).getScene().getWindow().hide();
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
							}

						}

					});
				}
			});
			
			ad[j] = new AvailableDates(str[i], str[i + 1], book);
			//System.out.println(ad[j]);
			dates.add(ad[j]);
		}

	}

	/**
	 * initialize method is the first method to be run when the controller is starting
	 * the method initializes the parameters of the fxml page
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		clmnDate.setCellValueFactory(new PropertyValueFactory<>("Date"));
		clmnTime.setCellValueFactory(new PropertyValueFactory<>("Time"));
		clmnPick.setCellValueFactory(new PropertyValueFactory<>("Pick"));

		tblAvailableDates.setItems(dates);

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
	 * alert method shows a information dialog to the screen
	 * @param title the title of the information dialog
	 * @param s the content of the information dialog
	 */
	public void alert(String title, String s) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(s);
		alert.show();
	}
}
