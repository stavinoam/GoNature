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
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.LogOutMessage;
import logic.OnResponseListener;
import logic.ParkManagerReportMessage;
import logic.Report;
import logic.Worker;

/**
 * A page to show all the reports that sent from all park managers.
 * @author Yarden Shahar
 *
 */
public class ReportsTableController implements Initializable
{
	/**
	 * Label lblReportsTable
	 */
	@FXML
	private Label lblReportsTable;

	/**
	 * TableView<Report> reportsTable
	 */
	@FXML
	private TableView<Report> reportsTable;

	/**
	 * TableColumn<Report, String> clmnParkName
	 */
	@FXML
	private TableColumn<Report, String> clmnParkName;

	/**
	 * TableColumn<Report, String> clmnReportName
	 */
	@FXML
	private TableColumn<Report, String> clmnReportName;

	/**
	 * TableColumn<Report, String> clmnMonth
	 */
	@FXML
	private TableColumn<Report, String> clmnMonth;

	/**
	 * TableColumn<Report, String> clmnYear
	 */
	@FXML
	private TableColumn<Report, String> clmnYear;

	/**
	 * TableColumn<Report, JFXButton> clmnView
	 */
	@FXML
	private TableColumn<Report, JFXButton> clmnView;

	/**
	 * JFXButton btnBack
	 */
	@FXML
	private JFXButton btnBack;

	/**
	 * JFXHamburger menu
	 */
	@FXML
	private JFXHamburger menu;

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
	 * Stage homeStage
	 */
	public Stage homeStage;
	/**
	 * ObservableList reportsList
	 */
	public static ObservableList<Report> reportsList = FXCollections.observableArrayList();

	/**
	 * worker object
	 */
	private Worker worker;

	/**
	 * @param event buttonaction
	 * @throws IOException throws a new input output exception
	 */
	@FXML
	void back(ActionEvent event) throws IOException
	{
		for (Report r : reportsTable.getItems())
			Platform.runLater(() ->
			{
				reportsTable.getItems().remove(r);
			});
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage stage = new Stage();
		DepartmentReportProducingController departmentReportProducingController = new DepartmentReportProducingController();
		try
		{
			departmentReportProducingController.start(stage, worker);
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FAILED TO OPEN PARKMANAGER PAGE");
		}
	}

	/**
	 * @param s string that contains the reports list
	 */
	public void loadReports(String s)
	{
		String[] str;
		str = s.split("/");
		int size = str.length / 4;
		Report[] r = new Report[size];
		for (int i = 0, j = 0; i < size; i++, j += 4)
		{
			JFXButton b = new JFXButton("View");
			b.setStyle("-fx-background-color: #e7e7e7; -fx-border-color: grey; -fx-border-radius: 5px;");
			r[i] = new Report(str[j], str[j + 1], str[j + 2], str[j + 3], b);
			reportsList.add(r[i]);
		}
	}

	/**
	 * @param title the title of the popup
	 * @param s the text of the popup
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
	 *initialize the table and the hamburger menu
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		clmnParkName.setCellValueFactory(new PropertyValueFactory<>("ParkName"));
		clmnReportName.setCellValueFactory(new PropertyValueFactory<>("ReportName"));
		clmnMonth.setCellValueFactory(new PropertyValueFactory<>("Month"));
		clmnYear.setCellValueFactory(new PropertyValueFactory<>("Year"));
		clmnView.setCellValueFactory(new PropertyValueFactory<>("View"));
		reportsTable.setItems(reportsList);

		for (Report report : ReportsTableController.reportsList)
		{
			report.getView().setOnAction(e ->
			{
				String[] r = new String[4];
				r[0] = report.getReportName();
				r[1] = report.getMonth();
				r[2] = report.getYear();
				r[3] = report.getParkName();
				ClientUI.chat.accept(new ParkManagerReportMessage(r), new OnResponseListener()
				{
					@Override
					public void onResponse(Object message)
					{
						Platform.runLater(() ->
						{
							try
							{
								for (Report r : reportsTable.getItems())
									Platform.runLater(() ->
									{
										reportsTable.getItems().remove(r);
									});
								((Node) e.getSource()).getScene().getWindow().hide(); // hiding primary
								Stage primaryStage = new Stage();
								if (ChatClient.s[0].contains("Revenue"))
								{
									RevenueReportController revenueReportController = new RevenueReportController();
									revenueReportController.start(primaryStage, worker, ChatClient.s, "table");
								} else if (ChatClient.s[0].contains("Visits"))
								{
									VisitsReportController visitsReportController = new VisitsReportController();
									visitsReportController.start(primaryStage, worker, ChatClient.s, "table");
								} else
								{
									UseReportController useReportController = new UseReportController();
									useReportController.start(primaryStage, worker, ChatClient.s, "table");
								}
							} catch (Exception e)
							{
								e.printStackTrace();
							}
						});

					}
				});
			});
		}

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

	/**
	 * @param stage starts a new stage
	 * @param worker worker object
	 * @throws Exception throws a new exception
	 */
	public void start(Stage stage, Worker worker) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ReportsTable.fxml"));
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
		this.worker = worker;
		stage.setOnCloseRequest(event ->
		{
			System.out.println("Stage is closing");
			ClientUI.chat.accept(new LogOutMessage(this.worker.getUserName()), null);
		});
	}

}
