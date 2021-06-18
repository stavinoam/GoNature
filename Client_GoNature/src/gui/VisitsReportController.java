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
import javafx.scene.input.MouseEvent;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.GetReportsMessage;
import logic.InsertReportsTableMessage;
import logic.LogOutMessage;
import logic.OnResponseListener;
import logic.Worker;

/**
 * VisitsReportController class
 * The class is made to be a page of Visits Report context
 * @author Shahar Yarden
 *
 */
public class VisitsReportController implements Initializable
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
	 *  Label reportDate
	 */
	@FXML
	private Label reportDate;

	/**
	 * Label hour1
	 */
	@FXML
	private Label hour1;

	/**
	 * Label regular1hr
	 */
	@FXML
	private Label regular1hr;

	/**
	 *  Label subs1hr
	 */
	@FXML
	private Label subs1hr;

	/**
	 * Label grp1hr
	 */
	@FXML
	private Label grp1hr;

	/**
	 * Label hour2
	 */
	@FXML
	private Label hour2;

	/**
	 * Label regular2hr
	 */
	@FXML
	private Label regular2hr;

	/**
	 * Label subs2hr
	 */
	@FXML
	private Label subs2hr;

	/**
	 * Label grp2hr
	 */
	@FXML
	private Label grp2hr;

	/**
	 * Label hour3
	 */
	@FXML
	private Label hour3;

	/**
	 * Label regular3hr
	 */
	@FXML
	private Label regular3hr;

	/**
	 * Label subs3hr
	 */
	@FXML
	private Label subs3hr;

	/**
	 * Label grp3hr
	 */
	@FXML
	private Label grp3hr;

	/**
	 * Label regularAvg
	 */
	@FXML
	private Label regularAvg;

	/**
	 * Label subsAvg
	 */
	@FXML
	private Label subsAvg;

	/**
	 * Label grpAvg
	 */
	@FXML
	private Label grpAvg;

	/**
	 * JFXButton btnBack
	 */
	@FXML
	private JFXButton btnBack;

	/**
	 * Label tot1hr
	 */
	@FXML
	private Label tot1hr;

	/**
	 * Label tot2hr
	 */
	@FXML
	private Label tot2hr;

	/**
	 * Label tot3hr
	 */
	@FXML
	private Label tot3hr;

	/**
	 * Label totAvg
	 */
	@FXML
	private Label totAvg;

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
	 * Label hour4
	 */
	@FXML
	private Label hour4;

	/**
	 * Label regular4hr
	 */
	@FXML
	private Label regular4hr;

	/**
	 * Label subs4hr
	 */
	@FXML
	private Label subs4hr;

	/**
	 * Label grp4hr
	 */
	@FXML
	private Label grp4hr;

	/**
	 * Label tot4hr
	 */
	@FXML
	private Label tot4hr;

	/**
	 * regTime is array of int to calculate the regular visitors time
	 */
	private int[] regTime = new int[4];
	
	/**
	 * subTime is array of int to calculate the subscriber visitors time
	 */
	private int[] subTime = new int[4];

	/**
	 * grpTime is array of int to calculate the group visitors time
	 */
	private int[] grpTime = new int[4];
	
	/**
	 * minutes is calculate the total of minutes spent in the park
	 */
	private int minutes = 0;
	
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
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/VisitsReport.fxml"));
		loader.setController(this);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		//scene.getStylesheets().add(getClass().getResource("/gui/Style.css").toExternalForm());
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
		loadVisitsReport(this.s, lastPage);
		primaryStage.setOnCloseRequest(event ->
		{
			System.out.println("Stage is closing");
			ClientUI.chat.accept(new LogOutMessage(worker.getUserName()), null);
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
	 * a function that load the data of the report and calculate informations to the page
	 * @param s contain the data of the report
	 * @param lastPage contain the last page address
	 */
	public void loadVisitsReport(String[] s, String lastPage)
	{
		this.lastPage = lastPage;
		reportDate.setText("for month " + s[2] + ", " + s[3]);
		reportTitle1.setText(s[1] + " - Visits report");
		int regSum = 0, subSum = 0, grpSum = 0; // count visitors number
		int totTimeReg = 0, totTimeSub = 0, totTimeGrp = 0;
		String[] time;
		if (s.length > 5)
		{
			for (int i = 4; i < s.length; i += 3)
			{
				time = s[i + 2].split(":");
				minutes = Integer.parseInt(time[0]) * 60 + Integer.parseInt(time[1]);
				int num = Integer.parseInt(s[i]);
				if (s[i + 1].equals("instructor"))
				{
					totTimeGrp += minutes * num;
					grpSum += num;
					calculateHours("instructor", num);
				} else if (s[i + 1].equals("subscriber"))
				{
					totTimeSub += minutes * num;
					subSum += num;
					calculateHours("subscriber", num);
				} else
				{
					totTimeReg += minutes * num;
					regSum += num;
					calculateHours("regular", num);
				}
			}
		}
		if (regSum > 0)
		{
			if (regTime[0] > 0)
				regular1hr.setText(String.format("%.2f", ((double) regTime[0] / regSum) * 100));
			if (regTime[1] > 0)
				regular2hr.setText(String.format("%.2f", ((double) regTime[1] / regSum) * 100));
			if (regTime[2] > 0)
				regular3hr.setText(String.format("%.2f", ((double) regTime[2] / regSum) * 100));
			if (regTime[3] > 0)
				regular4hr.setText(String.format("%.2f", ((double) regTime[3] / regSum) * 100));
			if (totTimeReg > 0)
				regularAvg.setText(String.format("%d:%02d", ((int) ((totTimeReg) / regSum) / 60),
						((int) ((totTimeReg) / regSum) % 60)));
		}
		if (subSum > 0)
		{
			if (subTime[0] > 0)
				subs1hr.setText(String.format("%.2f", ((double) subTime[0] / subSum) * 100));
			if (subTime[1] > 0)
				subs2hr.setText(String.format("%.2f", ((double) subTime[1] / subSum) * 100));
			if (subTime[2] > 0)
				subs3hr.setText(String.format("%.2f", ((double) subTime[2] / subSum) * 100));
			if (subTime[3] > 0)
				subs4hr.setText(String.format("%.2f", ((double) subTime[3] / subSum) * 100));
			if (totTimeSub > 0)
				subsAvg.setText(String.format("%d:%02d", ((int) ((totTimeSub) / subSum) / 60),
						((int) ((totTimeSub) / subSum) % 60)));
		}
		if (grpSum > 0)
		{
			if (grpTime[0] > 0)
				grp1hr.setText(String.format("%.2f", ((double) grpTime[0] / grpSum) * 100));
			if (grpTime[1] > 0)
				grp2hr.setText(String.format("%.2f", ((double) grpTime[1] / grpSum) * 100));
			if (grpTime[2] > 0)
				grp3hr.setText(String.format("%.2f", ((double) grpTime[2] / grpSum) * 100));
			if (grpTime[3] > 0)
				grp4hr.setText(String.format("%.2f", ((double) grpTime[3] / grpSum) * 100));
			if (totTimeGrp > 0)
				grpAvg.setText(String.format("%d:%02d", ((int) ((totTimeGrp) / grpSum) / 60),
						((int) ((totTimeGrp) / grpSum) % 60)));
		}

		if ((regSum + subSum + grpSum) > 0)
		{
			if ((regTime[0] + subTime[0] + grpTime[0]) > 0)
				tot1hr.setText(String.format("%.2f",
						(double) (regTime[0] + subTime[0] + grpTime[0]) / (regSum + subSum + grpSum) * 100));
			if ((regTime[1] + subTime[1] + grpTime[1]) > 0)
				tot2hr.setText(String.format("%.2f",
						(double) (regTime[1] + subTime[1] + grpTime[1]) / (regSum + subSum + grpSum) * 100));
			if ((regTime[2] + subTime[2] + grpTime[2]) > 0)
				tot3hr.setText(String.format("%.2f",
						(double) (regTime[2] + subTime[2] + grpTime[2]) / (regSum + subSum + grpSum) * 100));
			if ((regTime[3] + subTime[3] + grpTime[3]) > 0)
				tot4hr.setText(String.format("%.2f",
						(double) (regTime[3] + subTime[3] + grpTime[3]) / (regSum + subSum + grpSum) * 100));
			if ((totTimeReg + totTimeSub + totTimeGrp) > 0)
				totAvg.setText(String.format("%d:%02d",
						(int) (((totTimeReg + totTimeSub + totTimeGrp) / (regSum + subSum + grpSum)) / 60),
						(int) (((totTimeReg + totTimeSub + totTimeGrp) / (regSum + subSum + grpSum)) % 60)));
		}
		organization = s[1];
		String[] details = new String[4];
		details[0] = organization;
		details[1] = "Total visitors";
		details[2] = s[2];
		details[3] = s[3];

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
	 * private function that calculate values to the visits report
	 * @param type string that contain a visitor type
	 * @param visitorNumber number of visitors in the used order
	 */
	private void calculateHours(String type, int visitorNumber)
	{
		if (minutes <= 60)
		{
			if (type.equals("instructor"))
				grpTime[0] += visitorNumber;
			else if (type.equals("subscriber"))
				subTime[0] += visitorNumber;
			else
				regTime[0] += visitorNumber;
		} else if (minutes > 60 && minutes <= 120)
		{
			if (type.equals("instructor"))
				grpTime[1] += visitorNumber;
			else if (type.equals("subscriber"))
				subTime[1] += visitorNumber;
			else
				regTime[1] += visitorNumber;
		} else if (minutes > 120 && minutes <= 180)
		{
			if (type.equals("instructor"))
				grpTime[2] += visitorNumber;
			else if (type.equals("subscriber"))
				subTime[2] += visitorNumber;
			else
				regTime[2] += visitorNumber;
		} else
		{
			if (type.equals("instructor"))
				grpTime[3] += visitorNumber;
			else if (type.equals("subscriber"))
				subTime[3] += visitorNumber;
			else
				regTime[3] += visitorNumber;
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
