package gui;

import java.io.IOException;
import java.net.URL;
import java.time.Month;
import java.time.YearMonth;
import java.util.Arrays;
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
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.chart.XYChart.Data;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.LogOutMessage;
import logic.OnResponseListener;
import logic.VisitorsReportMessage;
import logic.Worker;

/**
 * page to show detail of visitors
 * the report show details of the requested month
 * @author Yarden Shahar
 *
 */
public class VisitorsReportController implements Initializable
{
	/**
	 * anchorpane of graphs
	 */
	@FXML
	private AnchorPane rootPane;

	/**
	 * label title of report
	 */
	@FXML
	private Label reportTitle;

	/**
	 * hamburger menu
	 */
	@FXML
	private JFXHamburger menu;

	/**
	 * label report date
	 */
	@FXML
	private Label reportDate;

	/**
	 * label report name
	 */
	@FXML
	private Label reportTitle1;

	/**
	 * label park name
	 */
	@FXML
	private Label park_name;

	/**
	 * button back to last page
	 */
	@FXML
	private JFXButton btnBack;

	/**
	 * anchorpane for hamburger menu
	 */
	@FXML
	private AnchorPane drawerpane;

	/**
	 * drawer for hamburger
	 */
	@FXML
	private JFXDrawer drawer;

	/**
	 * combobox with days of a specific month
	 */
	@FXML
	private ComboBox<String> comboDays;

	/**
	 * button to go to requested day in the month
	 */
	@FXML
	private JFXButton go;

	/**
	 * anchorpane
	 */
	@FXML
	private AnchorPane root;

	/**
	 * bar chart of visitors enter time
	 */
	@FXML
	private BarChart<String, Number> visitorsChart;

	/**
	 * x axis in graph
	 */
	@FXML
	private CategoryAxis x;

	/**
	 * y axis in graph
	 */
	@FXML
	private NumberAxis y;

	/**
	 * graph to show average time of visiting the park
	 */
	@FXML
	private BarChart<String, Number> avgTime;

	/**
	 * string of specific month and year
	 */
	private String month = "", year = "";
	/**
	 * list of options
	 */
	ObservableList<String> options = FXCollections.observableArrayList();

	/**
	 * object appearance of Worker
	 */
	private Worker worker;

	/**
	 * start method to open window and operate methods
	 * @param primaryStage starts a new stage
	 * @param w worker object
	 * @param s report data
	 * @param num number of days in the month
	 * @throws Exception throws a new exception
	 */
	public void start(Stage primaryStage, Worker w, String[] s, int num) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/VisitorsReport.fxml"));
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
		worker = w;
		loadChart(s, num);
		primaryStage.setOnCloseRequest(event ->
		{
			System.out.println("Stage is closing");
			ClientUI.chat.accept(new LogOutMessage(worker.getUserName()), null);
		});
	}

	/**
	 * method to load the graph with requested data
	 * @param s report data
	 * @param num number of days in the month
	 */
	public void loadChart(String[] s, int num)
	{
		month = s[1];
		year = s[2];

		visitorsChart.setAnimated(false);

		int startIdx = Arrays.asList(s).indexOf("AVG");
		double[] avg = calculateAvg(s, startIdx, s.length);
		if (num < 100)
			reportDate.setText(num + "/" + Month.valueOf(s[1].toUpperCase()).getValue() + "/" + s[2]);
		else
			reportDate.setText("for month " + s[1] + ", " + s[2]);
		XYChart.Series<String, Number> regSeries = new XYChart.Series<>();
		XYChart.Series<String, Number> subSeries = new XYChart.Series<>();
		XYChart.Series<String, Number> grpSeries = new XYChart.Series<>();

		for (int i = 8; i < 16; i++)
		{
			regSeries.getData().add(new Data<String, Number>(i + ":00", 0));
			subSeries.getData().add(new Data<String, Number>(i + ":00", 0));
			grpSeries.getData().add(new Data<String, Number>(i + ":00", 0));
		}

		if (s.length > 4)
		{
			for (int i = 3; i < startIdx; i += 3)
			{
				if (s[i + 1].equals("instructor"))

					grpSeries.getData().add(new Data<String, Number>(s[i] + ":00", Integer.parseInt(s[i + 2])));
				else if (s[i + 1].equals("subscriber"))
					subSeries.getData().add(new Data<String, Number>(s[i] + ":00", Integer.parseInt(s[i + 2])));
				else
					regSeries.getData().add(new Data<String, Number>(s[i] + ":00", Integer.parseInt(s[i + 2])));
			}
			regSeries.setName("Regular");
			subSeries.setName("Subscriber");
			grpSeries.setName("Group");

			visitorsChart.getData().add(regSeries);
			visitorsChart.getData().add(subSeries);
			visitorsChart.getData().add(grpSeries);

			XYChart.Series<String, Number> regSeriesAvg = new XYChart.Series<>();
			XYChart.Series<String, Number> subSeriesAvg = new XYChart.Series<>();
			XYChart.Series<String, Number> grpSeriesAvg = new XYChart.Series<>();
			regSeriesAvg.setName("Regular");
			subSeriesAvg.setName("Subscriber");
			grpSeriesAvg.setName("Group");
			regSeriesAvg.getData().add(new Data<String, Number>("Regular", avg[0]));
			subSeriesAvg.getData().add(new Data<String, Number>("Subscriber", avg[1]));
			grpSeriesAvg.getData().add(new Data<String, Number>("Group", avg[2]));

			avgTime.setAnimated(false);
			avgTime.getData().addAll(regSeriesAvg, subSeriesAvg, grpSeriesAvg);

		}
		YearMonth ym = YearMonth.of(Integer.parseInt(s[2]), Month.valueOf(s[1].toUpperCase()).getValue());

		for (int i = 1; i <= ym.lengthOfMonth(); i++)
			options.add(i + "");
		comboDays.setItems(options);
	}

	/**
	 * method to load the average visiting time chart
	 * @param s average visiting time data
	 * @param idx number of rows
	 * @param end the final value of the chart
	 * @return array of doubles
	 */
	private double[] calculateAvg(String[] s, int idx, int end)
	{
		int regTime = 0, subTime = 0, grpTime = 0;
		int regSum = 0, subSum = 0, grpSum = 0;
		int minutes = 0;
		double avgRegHour = 0, avgSubHour = 0, avgGrpHour = 0;
		String[] time;
		for (int i = idx + 1; i < end; i += 3)
		{
			time = s[i + 2].split(":");
			minutes = Integer.parseInt(time[0]) * 60 + Integer.parseInt(time[1]);
			int visitorsNum = Integer.parseInt(s[i]);
			if (s[i + 1].equals("subscriber"))
			{
				subTime += minutes * visitorsNum;
				subSum += visitorsNum;
			} else if (s[i + 1].equals("instructor"))
			{
				grpTime += minutes * visitorsNum;
				grpSum += visitorsNum;
			} else
			{
				regTime += minutes * visitorsNum;
				regSum += visitorsNum;
			}
		}
		if (regSum > 0)
			avgRegHour = (double) ((regTime / regSum)) / 60;
		if (subSum > 0)
			avgSubHour = (double) ((subTime / subSum)) / 60;
		if (grpSum > 0)
			avgGrpHour = (double) ((grpTime / grpSum)) / 60;

		double[] avg = new double[3];
		avg[0] = avgRegHour;
		avg[1] = avgSubHour;
		avg[2] = avgGrpHour;

		return avg;
	}

	/**
	 * method to go to a specific day in the month
	 * @param event buttonaction
	 * @throws IOException throws a new input output exception
	 */
	@FXML
	void goToSpecificDay(ActionEvent event) throws IOException
	{
		if (comboDays.getValue() == null)
			alert("Error", "You must choose a day.");
		else
		{
			String[] report = new String[4];
			report[0] = "Visiting report";
			report[1] = comboDays.getValue().toString(); // day
			report[2] = month; // month
			report[3] = year; // year
			ClientUI.chat.accept(new VisitorsReportMessage(report), new OnResponseListener()
			{
				@Override
				public void onResponse(Object message)
				{
					Platform.runLater(() ->
					{
						((Node) event.getSource()).getScene().getWindow().hide();
						Stage stage = new Stage();
						VisitorsReportController visitorsReportController = new VisitorsReportController();
						try
						{
							visitorsReportController.start(stage, worker, ChatClient.s,
									Integer.parseInt(comboDays.getValue().toString()));
						} catch (Exception e)
						{
							e.printStackTrace();
							System.out.println("FAILED TO OPEN PARKMANAGER PAGE");
						}
					});

				}
			});
		}
	}

	/**
	 * method to go to the last page
	 * @param event buttonaction
	 * @throws IOException throws a new input output exception
	 */
	@FXML
	void back(ActionEvent event) throws IOException
	{
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
	 * make a popup alert
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
	 * method to intialize the hamburger menu
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