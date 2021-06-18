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
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.LogOutMessage;
import logic.OnResponseListener;
import logic.ParkManagerReportMessage;
import logic.Worker;

/**
 * ParkReportProducingController class
 * The class is made to be report home page, that page contain list of report that the park manager can produce
 * @author Shahar Yarden
 *
 */
public class ParkReportProducingController implements Initializable
{
	/**
	 * organization is object appearance of Worker 
	 */
	private Worker organization;

	/**
	 *  Label reportProducingtitle
	 */
	@FXML
	private Label reportProducingtitle;

	/**
	 * AnchorPane drawerpane
	 */
	@FXML
	private AnchorPane drawerpane;
	
	/**
	 * JFXDrawer drawer
	 */
	@FXML
	private JFXDrawer drawer;

	/**
	 * JFXHamburger menu
	 */
	@FXML
	private JFXHamburger menu;

	/**
	 * ComboBox<String> comboReportName
	 */
	@FXML
	private ComboBox<String> comboReportName;

	/**
	 * ComboBox<String> comboMonth
	 */
	@FXML
	private ComboBox<String> comboMonth;

	/**
	 * ComboBox<String> comboYear
	 */
	@FXML
	private ComboBox<String> comboYear;

	/**
	 * JFXButton btnProduce
	 */
	@FXML
	private JFXButton btnProduce;

	/**
	 * JFXButton btnBack
	 */
	@FXML
	private JFXButton btnBack;

	/**
	 * ObservableList reportNames - list of String type that contain all of the report names
	 */
	ObservableList<String> reportNames = FXCollections.observableArrayList("Total visitors", "Use report",
			"Income report");
	
	/**
	 * ObservableList monthsList - list of String type that contain all of the month names
	 */
	ObservableList<String> monthsList = FXCollections.observableArrayList("January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October", "November", "December");
	
	/**
	 * ObservableList yearsList - list of String type that contain all of the year numbers
	 */
	ObservableList<String> yearsList = FXCollections.observableArrayList("2019", "2020");

	/**
	 * starting a new stage 
	 * @param primaryStage object of a stage
	 * @param w object of Worker type
	 * @throws Exception throw any exception
	 */
	public void start(Stage primaryStage, Worker w) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ParkReportProducing.fxml"));
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
		this.organization = w;
		primaryStage.setOnCloseRequest(event ->
		{
			System.out.println("Stage is closing");
			ClientUI.chat.accept(new LogOutMessage(organization.getUserName()), null);
		});
	}

	
	/**
	 * a function with an ActionEvent, when click on the correct button it will return to the last page(stage)
	 * @param event the event that invoked is button click
	 * @throws IOException throw IOException
	 */
	@FXML
	void back(ActionEvent event) throws IOException
	{
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage stage = new Stage();
		ParkManagerhpController parkManagerhpController = new ParkManagerhpController();
		try
		{
			parkManagerhpController.start(stage, organization);
		} catch (Exception e)
		{
			System.out.println("FAILED TO OPEN WORKER HOME PAGE");
		}
	}
	

	/**
	 * a function with an ActionEvent, when click on the correct button it will transform into a new page(stage)
	 * The function contains fields check if the values are empty.
	 * If the values are not empty the movement to the report produce will be based on the report chosen inside the combobox
	 * @param event the event that invoked is button click
	 */
	@FXML
	void produceAreport(ActionEvent event)
	{
		if (comboReportName.getValue() == null || comboMonth.getValue() == null || comboYear.getValue() == null)
			alert("Error", "You must choose report name and date in order to produce a report");
		else
		{
			String[] report = new String[4];
			report[0] = comboReportName.getValue().toString();
			report[1] = comboMonth.getValue().toString();
			report[2] = comboYear.getValue().toString();
			report[3] = organization.getOrganization();
			ClientUI.chat.accept(new ParkManagerReportMessage(report), new OnResponseListener()
			{
				@Override
				public void onResponse(Object message)
				{
					Platform.runLater(() ->
					{
						try
						{
							((Node) event.getSource()).getScene().getWindow().hide();
							Stage stage = new Stage();
							if (ChatClient.s[0].contains("Revenue"))
							{
								RevenueReportController revenueReportController = new RevenueReportController();
								revenueReportController.start(stage, organization, ChatClient.s, "report");
							} else if (ChatClient.s[0].contains("Visits"))
							{
								VisitsReportController visitsReportController = new VisitsReportController();
								visitsReportController.start(stage, organization, ChatClient.s, "report");
							} else
							{
								UseReportController useReportController = new UseReportController();
								useReportController.start(stage, organization, ChatClient.s, "report");
							}
						} catch (Exception e)
						{
							e.printStackTrace();
						}
					});

				}
			});
		}
	}

	/**
	 * function that produce a pop-up alert
	 * @param title the title of the alert
	 * @param s the message that will written in the alert
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
	 * initialize the hamburger settings 
	 * initialize the combo box
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		comboReportName.setItems(reportNames);
		comboMonth.setItems(monthsList);
		comboYear.setItems(yearsList);

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
							ParkManagerhpController parkManagerhpController = new ParkManagerhpController();
							try
							{
								parkManagerhpController.start(stage3, organization);
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
