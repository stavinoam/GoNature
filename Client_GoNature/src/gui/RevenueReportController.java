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
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.GetReportsMessage;
import logic.InsertReportsTableMessage;
import logic.LogOutMessage;
import logic.OnResponseListener;
import logic.Worker;

/**
 * RevenueReportController class
 * The class is made to be a page of Revenue Report context
 * @author Shahar Yarden
 *
 */
public class RevenueReportController implements Initializable
{
	/**
	 * organization is a string that contain the worker organization
	 */
	private String organization;
	
	/**
	 * lastPage is a string that contain the lastPage address
	 */
	public String lastPage;

	/**
	 * Label reportTitle
	 */
	@FXML
	private Label reportTitle;

	/**
	 * JFXHamburger menu
	 */
	@FXML
	private JFXHamburger menu;

	/**
	 * Label aboutlabel
	 */
	@FXML
	private Label aboutlabel;

	/**
	 * Label reportTitle1
	 */
	@FXML
	private Label reportTitle1;

	/**
	 * Label reportDate
	 */
	@FXML
	private Label reportDate;

	/**
	 * Label park_name
	 */
	@FXML
	private Label park_name;

	/**
	 * Label totalVisitors
	 */
	@FXML
	private Label totalVisitors;

	/**
	 * Label totalIncome
	 */
	@FXML
	private Label totalIncome;

	/**
	 * JFXButton btnBack
	 */
	@FXML
	private JFXButton btnBack;

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
	 * worker is object appearance of Worker 
	 */
	private Worker worker;
	
	/**
	 * s is an array of strings that contains the information of report that return from the database
	 */
	private String[] s;

	/**
	 * starting a new stage
	 * @param primaryStage object of a stage
	 * @param w object of Worker type
	 * @param s contain the data of the report 
	 * @param lp contain the last page address
	 * @throws Exception throw any exception
	 */
	public void start(Stage primaryStage, Worker w, String[] s, String lp) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/RevenueReport.fxml"));
		loader.setController(this);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		// scene.getStylesheets().add(getClass().getResource("/gui/Style.css").toExternalForm());
		AboutUsController.primaryStage = primaryStage;
		OurParksController.primaryStage = primaryStage;
		PricesListController.primaryStage = primaryStage;
		primaryStage.setTitle("GoNature");
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image("gui/css/tree.png"));
		primaryStage.show();
		this.worker = w;
		this.s = s;
		lastPage = lp;
		loadRevenueReport(this.s, lastPage);
		primaryStage.setOnCloseRequest(event ->
		{
			System.out.println("Stage is closing");
			ClientUI.chat.accept(new LogOutMessage(worker.getUserName()), null);
		});
	}

	/**
	 * a function that load the data of the report to the page
	 * @param s contain the data of the report
	 * @param lastPage contain the last page address
	 */
	public void loadRevenueReport(String[] s, String lastPage)
	{
		this.lastPage = lastPage;
		reportDate.setText("for month " + s[4] + ", " + s[5]);
		park_name.setText(s[1]);
		organization = s[1];
		if (s[2] == null)
			totalVisitors.setText("-");
		else
			totalVisitors.setText(s[2]);
		if (s[3] == null)
			totalIncome.setText("-");
		else
			totalIncome.setText(s[3]);

		String[] details = new String[4];
		details[0] = organization;
		details[1] = "Revenue report";
		details[2] = s[4];
		details[3] = s[5];

		ClientUI.chat.accept(new InsertReportsTableMessage(details), new OnResponseListener()
		{
			@Override
			public void onResponse(Object message)
			{
				if (!ChatClient.flag)
				{
					Platform.runLater(() ->
					{
						alert("Failed", "Something went wrong.\nThe report was not added to reports table.");
					});
				}
			}
		});

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
	 * a function with an ActionEvent, when click on the correct button it will return to the last page(stage)
	 * @param event the event that invoked is button click
	 * @throws IOException throw IOException
	 */
	@FXML
	void back(ActionEvent event) throws IOException
	{
		if (lastPage.equals("table"))
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
		} else
		{
			((Node) event.getSource()).getScene().getWindow().hide();
			Stage stage = new Stage();
			ParkReportProducingController parkReportProducingController = new ParkReportProducingController();
			try
			{
				parkReportProducingController.start(stage, worker);
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}
	}

	/**
	 * initialize the hamburger settings 
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
							if (lastPage.equals("report"))
							{
								ParkManagerhpController parkManagerhpController = new ParkManagerhpController();
								try
								{
									parkManagerhpController.start(stage3, worker);
								} catch (Exception e1)
								{
									e1.printStackTrace();
									System.out.println("FAILED TO OPEN PARKMANAGER PAGE");
								}
							} else
							{
								DepartmenthpController departmenthpController = new DepartmenthpController();
								try
								{
									departmenthpController.start(stage3, worker);
								} catch (Exception e1)
								{
									e1.printStackTrace();
									System.out.println("FAILED TO OPEN PARKMANAGER PAGE");
								}
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