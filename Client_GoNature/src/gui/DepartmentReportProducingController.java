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
import logic.DepartmentReportMessage;
import logic.GetReportsMessage;
import logic.LogOutMessage;
import logic.OnResponseListener;
import logic.Worker;

/**
 * A page for department manager to produce a report
 * @author Yarden Shahar
 *
 */
public class DepartmentReportProducingController implements Initializable
{

	/**
	 * label to set the title of the page
	 */
	@FXML
	private Label reportProducingTitle;

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
	 * hamburger menu
	 */
	@FXML
	private JFXHamburger menu;

	/**
	 * combobox with report names
	 */
	@FXML
	private ComboBox<String> comboReportName;

	/**
	 * combobox with month names
	 */
	@FXML
	private ComboBox<String> comboMonth;

	/**
	 * combobox with years
	 */
	@FXML
	private ComboBox<String> comboYear;

	/**
	 * button to produce a report
	 */
	@FXML
	private JFXButton btnProduce;

	/**
	 * button to return to the last page
	 */
	@FXML
	private JFXButton brnBack;

	/**
	 * button to go to reports table
	 */
	@FXML
	private JFXButton btnReportsTable;

	/**
	 * list of reports
	 */
	ObservableList<String> reportNames = FXCollections.observableArrayList("Visiting report", "Cancellations report");
	/**
	 * list of months
	 */
	ObservableList<String> monthsList = FXCollections.observableArrayList("January", "February", "March", "April",
			"May", "June", "July", "August", "September", "October", "November", "December");
	/**
	 * list of years
	 */
	ObservableList<String> yearsList = FXCollections.observableArrayList("2019", "2020");
	/**
	 * Object appearance of Worker
	 */
	private Worker worker;
	
	/**
	 * start method to open window
	 * @param primaryStage start new stage
	 * @param w worker object
	 * @throws Exception throws a new exception
	 */
	public void start(Stage primaryStage, Worker w) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/DepartmentReportProducing.fxml"));
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
		worker = w;
		primaryStage.setOnCloseRequest(event ->
		{
			System.out.println("Stage is closing");
			ClientUI.chat.accept(new LogOutMessage(worker.getUserName()), null);
		});
	}
	
	/**
	 * return to last page
	 * @param event
	 * @throws IOException
	 */
	@FXML
	void back(ActionEvent event) throws IOException
	{
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage stage = new Stage();
		DepartmenthpController worker = new DepartmenthpController();
		try
		{
			worker.start(stage, this.worker);
		} catch (Exception e)
		{
			System.out.println("FAILED TO OPEN DEPARTMENT MANAGER HOME PAGE");
		}
	}

	/**
	 * method to go to reports table
	 * @param event
	 */
	@FXML
	void gotoReportsTable(ActionEvent event)
	{
		String report = "report";
		ClientUI.chat.accept(new GetReportsMessage(report), new OnResponseListener()
		{
			@Override
			public void onResponse(Object message)
			{
				Platform.runLater(() ->
				{
					((Node) event.getSource()).getScene().getWindow().hide();
					Stage stage = new Stage();
					ReportsTableController reportsTableController = new ReportsTableController();
					try
					{
						reportsTableController.loadReports(ChatClient.str);
						reportsTableController.start(stage, worker);
					} catch (Exception e)
					{
						e.printStackTrace();
					}
				});

			}
		});
	}

	/**
	 * method to produce a requested report
	 * @param event
	 */
	@FXML
	void produceAreport(ActionEvent event)
	{
		if (comboReportName.getValue() == null || comboMonth.getValue() == null || comboYear.getValue() == null)
			alert("Error", "You must choose report name and date in order to produce a report");
		else
		{
			String[] report = new String[3];
			report[0] = comboReportName.getValue().toString();
			report[1] = comboMonth.getValue().toString();
			report[2] = comboYear.getValue().toString();
			ClientUI.chat.accept(new DepartmentReportMessage(report), new OnResponseListener()
			{
				@Override
				public void onResponse(Object message)
				{
					Platform.runLater(() ->
					{
						((Node) event.getSource()).getScene().getWindow().hide();
						Stage stage = new Stage();
						try
						{
							if (ChatClient.s[0].contains("cancelingReport"))
							{
								CancelingReportController cancelingReportController = new CancelingReportController();
								cancelingReportController.start(stage, worker, ChatClient.s);
							} else
							{
								VisitorsReportController visitorsReportController = new VisitorsReportController();
//								visitorsReportController.loadChart(ChatClient.s, 100);
								visitorsReportController.start(stage, worker, ChatClient.s, 100);
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
	 * initialize comboboxes and hamburger
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
							DepartmenthpController departmenthpController = new DepartmenthpController();
							try
							{
								departmenthpController.start(stage3, worker);
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

}
