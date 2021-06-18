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
import logic.ConfirmationMessage;
import logic.CurrentNumbersMessage;
import logic.LogOutMessage;
import logic.OnResponseListener;
import logic.Worker;

/**
 * Home screen for department manager
 * @author Yarden Shahar
 *
 */
public class DepartmenthpController implements Initializable
{

	/**
	 * label title
	 */
	@FXML
	private Label bookvisittitle;

	/**
	 * button to go to report creation
	 */
	@FXML
	private JFXButton btnCreateReport;

	/**
	 * button to go to confirmations page
	 */
	@FXML
	private JFXButton btnConfirmation;

	/**
	 * anchor pane for hamburger
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
	 * text field, number of visitors in the park
	 */
	@FXML
	private TextField fldNumVisitorsBanias;

	/**
	 * text field, number of spares in the park
	 */
	@FXML
	private TextField fldNumSparesBanias;

	/**
	 * icon to refresh number of visitors
	 */
	@FXML
	private FontAwesomeIconView iconRefresh;
	
	/**
	 * text field, number of visitors in the park
	 */
	@FXML
	private TextField fldNumVisitorsHatzbani;
	/**
	 * text field, number of spares in the park
	 */
	@FXML
	private TextField fldNumSparesHatzbani;
	
	/**
	 * text field, number of visitors in the park
	 */
	@FXML
	private TextField fldNumVisitorsHaiPark;
	/**
	 * text field, number of spares in the park
	 */
	@FXML
	private TextField fldNumSparesHaiPark;

	/**
	 * label
	 */
	@FXML
	private Label home;
	/**
	 * label
	 */
	@FXML
	private Label tree;
	/**
	 * label
	 */
	@FXML
	private Label about;
	/**
	 * label
	 */
	@FXML
	private Label close;

	/**
	 * Object appearance of Worker
	 */
	private Worker worker;
	
	/**
	 * The method respond to user click on  "confirmation" button
	 * @param event clicking on "confirmation" button
	 */
	@FXML
	void gotoConfirmation(ActionEvent event)
	{
		String confirmation = "Confirmation";
		ClientUI.chat.accept(new ConfirmationMessage(confirmation), new OnResponseListener()
		{
			@Override
			public void onResponse(Object message)
			{
				Platform.runLater(() ->
				{	
					((Node) event.getSource()).getScene().getWindow().hide();
					Stage stage = new Stage();
					ConfirmationController confirmationController = new ConfirmationController();
					try
					{
						confirmationController.start(stage, worker, ChatClient.str);
					} catch (Exception e)
					{
						System.out.println("FAILED TO OPEN WORKER HOME PAGE");
					}
				});

			}
		});
	}

	/**
	 * The method respond to user click on  "create a report" button
	 * The method leads the user to the "department reports page"
	 * @param event clicking on "create a report" button
	 */
	@FXML
	void gotoCreateReport(ActionEvent event) throws IOException
	{
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage stage = new Stage();
		DepartmentReportProducingController departmentReportProducingController = new DepartmentReportProducingController();
		try
		{
			departmentReportProducingController.start(stage, worker);
		} catch (Exception e)
		{
			System.out.println("FAILED TO OPEN WORKER HOME PAGE");
		}
	}

	/**
	 * the method to reload number of visitors in parks
	 * @param event click on the refresh icon
	 */
	@FXML
	void refreshNumbers(MouseEvent event)
	{
		ClientUI.chat.accept(new CurrentNumbersMessage("TOTAL"), new OnResponseListener()
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
						String[] str = ChatClient.str.split(" ");
						fldNumVisitorsBanias.setText(str[0]);
						fldNumSparesBanias.setText(str[1]);
						fldNumVisitorsHatzbani.setText(str[2]);
						fldNumSparesHatzbani.setText(str[3]);
						fldNumVisitorsHaiPark.setText(str[4]);
						fldNumSparesHaiPark.setText(str[5]);

					});
				}
			}
		});
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
	 * Start method is the method that starts the stage with the match fxml file 
	 * @param primaryStage the new stage that is created before calling the start method
	 * @param w the worker that uses the page
	 * @throws Exception if the FXMLLoader can't load the fxml file
	 */
	public void start(Stage primaryStage, Worker w) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Departmenthp.fxml"));
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
	 * initialize method is the first method to be run when the controller is starting
	 * the method initializes the parameters of the fxml page
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{

		ClientUI.chat.accept(new CurrentNumbersMessage("TOTAL"), new OnResponseListener()
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
						String[] str = ChatClient.str.split(" ");
						fldNumVisitorsBanias.setText(str[0]);
						fldNumSparesBanias.setText(str[1]);
						fldNumVisitorsHatzbani.setText(str[2]);
						fldNumSparesHatzbani.setText(str[3]);
						fldNumVisitorsHaiPark.setText(str[4]);
						fldNumSparesHaiPark.setText(str[5]);

					});
				}
			}
		});

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
