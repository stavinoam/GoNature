package gui;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;
import com.jfoenix.transitions.hamburger.HamburgerBasicCloseTransition;

import client.ClientUI;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.LogOutMessage;
import logic.Worker;

/**
 * Cancellation report class
 * Show details about cancellation of the requested month.
 * @author Yarden Shahar
 *
 */
public class CancelingReportController implements Initializable
{

	/**
	 * label title
	 */
	@FXML
	private Label reportTitle;

	/**
	 * hamburger menu
	 */
	@FXML
	private JFXHamburger menu;

	/**
	 * label
	 */
	@FXML
	private Label aboutlabel;

	/**
	 * drawer for hamburger
	 */
	@FXML
	private AnchorPane drawerpane;

	/**
	 * drawer
	 */
	@FXML
	private JFXDrawer drawer;

	/**
	 * label title
	 */
	@FXML
	private Label reportTitle1;

	/**
	 * label date
	 */
	@FXML
	private Label reportDate;

	/**
	 * label canceled
	 */
	@FXML
	private Label baniasCanceled;

	/**
	 * label unused
	 */
	@FXML
	private Label baniasUnused;

	/**
	 * label total
	 */
	@FXML
	private Label totalBanias;

	/**
	 * label canceled
	 */
	@FXML
	private Label hatzbaniCanceled;

	/**
	 * label unused
	 */
	@FXML
	private Label hatzbaniUnused;
	
	/**
	 * label total
	 */
	@FXML
	private Label totalHatzbani;

	/**
	 * label canceled
	 */
	@FXML
	private Label haiParkCanceled;
	
	/**
	 * label unused
	 */
	@FXML
	private Label haiParkUnused;

	/**
	 * label total
	 */
	@FXML
	private Label totalHaiPark;

	/**
	 * label canceled
	 */
	@FXML
	private Label totalCanceled;

	/**
	 * label unused
	 */
	@FXML
	private Label totalUnused;

	/**
	 * button
	 */
	@FXML
	private JFXButton btnBack;

	/**
	 * label total
	 */
	@FXML
	private Label totalTotal;

	/**
	 * label percent
	 */
	@FXML
	private Label baniasPercent;
	/**
	 * label percent
	 */
	@FXML
	private Label hatzbaniPercent;
	/**
	 * label percent
	 */
	@FXML
	private Label haiParkPercent;
	/**
	 * label percent
	 */
	@FXML
	private Label totalPercent;

	/**
	 * worker is an object appearance of Worker
	 */
	private Worker worker;

	/**
	 * Start method is the method that starts the stage with the match fxml file 
	 * @param primaryStage the new stage that is created before calling the start method
	 * @param w the worker that uses the page
	 * @param s array of strings from the DB of the table content
	 * @throws Exception if the FXMLLoader can't load the fxml file
	 */
	public void start(Stage primaryStage, Worker w, String[] s) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/cancelingReport.fxml"));
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
		loadCancelingReport(s);
		primaryStage.setOnCloseRequest(event ->
		{
			System.out.println("Stage is closing");
			ClientUI.chat.accept(new LogOutMessage(worker.getUserName()), null);
		});
	}

	/**
	 * load report data
	 * @param s the data from the DB to be loaded at the report
	 */
	public void loadCancelingReport(String[] s)
	{
		reportDate.setText(s[4]);
		int total = 0;
		int baniasCancelSummary = 0, haiParkCancelSummary = 0, hatzbaniCancelSummary = 0;
		int baniasUnuseSummary = 0, haiParkUnuseSummary = 0, hatzbaniUnuseSummary = 0;
		int totalOrdersBanias = 0, totalOrdersHaiPark = 0, totalOrdersHatzbani = 0;
		double percent1 = 0, percent2 = 0, percent3 = 0, totalPercent = 0;
		// Unused
		if (s[1].length() > 1)
		{
			int totalUnused = 0;
			String[] unused = s[1].split("/");
			for (int i = 0; i < unused.length; i += 2)
			{
				System.out.println(unused[i]);
				if (unused[i].equals("Banias"))
				{
					baniasUnused.setText(unused[i + 1]);
					baniasUnuseSummary = Integer.parseInt(unused[i + 1]);
				} else if (unused[i].equals("Hai Park"))
				{
					haiParkUnused.setText(unused[i + 1]);
					haiParkUnuseSummary = Integer.parseInt(unused[i + 1]);
				} else
				{
					hatzbaniUnused.setText(unused[i + 1]);
					hatzbaniUnuseSummary = Integer.parseInt(unused[i + 1]);
					if (hatzbaniUnused.getText().equals("0"))
						hatzbaniUnused.setText("-");
				}
				totalUnused += Integer.parseInt(unused[i + 1]);
			}
			this.totalUnused.setText(String.valueOf(totalUnused));
			if (this.totalUnused.getText().equals("0"))
				this.totalUnused.setText("-");
		}
		// Canceled
		if (s[2].length() > 1)
		{
			int totalCanceled = 0;
			String[] canceled = s[2].split("/");
			for (int i = 0; i < canceled.length; i += 2)
			{
				if (canceled[i].equals("Banias"))
				{
					baniasCanceled.setText(canceled[i + 1]);
					baniasCancelSummary = Integer.parseInt(canceled[i + 1]);
				} else if (canceled[i].equals("Hai Park"))
				{
					haiParkCanceled.setText(canceled[i + 1]);
					haiParkCancelSummary = Integer.parseInt(canceled[i + 1]);
				} else if (canceled[i].equals("Hatzbani"))
				{
					hatzbaniCanceled.setText(canceled[i + 1]);
					hatzbaniCancelSummary = Integer.parseInt(canceled[i + 1]);
				}
				totalCanceled += Integer.parseInt(canceled[i + 1]);
			}
			this.totalCanceled.setText(String.valueOf(totalCanceled));
		}
		// Total
		if (s[3].length() > 1)
		{
			String[] totalForEachPark = s[3].split("/");
			for (int i = 0; i < totalForEachPark.length; i += 2)
			{
				if (totalForEachPark[i].equals("Banias"))
				{
					totalBanias.setText(String.format("%d", baniasCancelSummary + baniasUnuseSummary));
					totalOrdersBanias = Integer.parseInt(totalForEachPark[i + 1]);
				} else if (totalForEachPark[i].equals("Hai Park"))
				{
					totalHaiPark.setText(String.format("%d", haiParkCancelSummary + haiParkUnuseSummary));
					totalOrdersHaiPark = Integer.parseInt(totalForEachPark[i + 1]);
				} else
				{
					totalHatzbani.setText(String.format("%d", hatzbaniCancelSummary + hatzbaniUnuseSummary));
					totalOrdersHatzbani = Integer.parseInt(totalForEachPark[i + 1]);

				}
				total += Integer.parseInt(totalForEachPark[i + 1]);
			}
			totalTotal.setText(String.format("%d", baniasCancelSummary + baniasUnuseSummary + haiParkCancelSummary
					+ haiParkUnuseSummary + hatzbaniCancelSummary + hatzbaniUnuseSummary));
			if ((baniasCancelSummary + baniasUnuseSummary) > 0)
				percent1 = ((double) (baniasCancelSummary + baniasUnuseSummary) / totalOrdersBanias) * 100;
			if ((haiParkCancelSummary + haiParkUnuseSummary) > 0)
				percent2 = ((double) (haiParkCancelSummary + haiParkUnuseSummary) / totalOrdersHaiPark) * 100;
			if ((hatzbaniCancelSummary + hatzbaniUnuseSummary) > 0)
				percent3 = ((double) (hatzbaniCancelSummary + hatzbaniUnuseSummary) / totalOrdersHatzbani) * 100;
			if (percent1 > 0)
				baniasPercent.setText(String.format("%.2f", percent1));
			if (percent2 > 0)
				haiParkPercent.setText(String.format("%.2f", percent2));
			if (percent3 > 0)
				hatzbaniPercent.setText(String.format("%.2f", percent3));

			totalPercent = ((double) (baniasCancelSummary + baniasUnuseSummary + haiParkCancelSummary
					+ haiParkUnuseSummary + hatzbaniCancelSummary + hatzbaniUnuseSummary) / total) * 100;
			if (totalPercent > 0)
				this.totalPercent.setText(String.format("%.2f", totalPercent));
		}
	}

	/**
	 * The method respond to user click on  "back" button
	 * The method leads the user to the last page that he used before entering "book a visit page"
	 * @param event clicking on "back" button
	 */
	@FXML
	void back(ActionEvent event)
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
	 * initialize method is the first method to be run when the controller is starting
	 * the method initializes the parameters of the fxml page
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
