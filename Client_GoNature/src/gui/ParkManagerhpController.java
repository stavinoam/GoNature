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
import logic.CurrentNumbersMessage;
import logic.LogOutMessage;
import logic.OnResponseListener;
import logic.ParkSettingsMessage;
import logic.Worker;

/**
 * ParkManagerhpController class
 * The class is made to be a home page, which controlling the park manager functions
 * @author Shahar Yarden
 *
 */
public class ParkManagerhpController implements Initializable
{

	/**
	 * organization is object appearance of Worker 
	 */
	private Worker organization;
	
	/**
	 * homeStage is a static appearance of a stage
	 */
	public static Stage homeStage;

	/**
	 * label parkManagerhpTitle
	 */
	@FXML
	private Label parkManagerhpTitle;

	/**
	 * JFXButton btnCreateReport
	 */
	@FXML
	private JFXButton btnCreateReport;

	/**
	 * JFXButton btnParkSettings
	 */
	@FXML
	private JFXButton btnParkSettings;

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
	 *  Label logoutlabel
	 */
	@FXML
	private Label logoutlabel;

	/**
	 * Label aboutlabel
	 */
	@FXML
	private Label aboutlabel;

	/**
	 * TextField fldCurrentNumOfVisitors
	 */
	@FXML
	private TextField fldCurrentNumOfVisitors;

	/**
	 * TextField fldCurrentParkSpares
	 */
	@FXML
	private TextField fldCurrentParkSpares;

	/**
	 * FontAwesomeIconView iconRefresh
	 */
	@FXML
	private FontAwesomeIconView iconRefresh;

	/**
	 *  Label home
	 */
	@FXML
	private Label home;

	/**
	 * Label tree
	 */
	@FXML
	private Label tree;

	/**
	 * Label about
	 */
	@FXML
	private Label about;

	/**
	 * Label close
	 */
	@FXML
	private Label close;

	/**
	 * starting a new stage 
	 * @param primaryStage object of a stage
	 * @param w object of Worker type
	 * @throws Exception throw any exception
	 */
	public void start(Stage primaryStage, Worker w) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/ParkManagerhp.fxml"));
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
		loadOrganization();
		primaryStage.setOnCloseRequest(event ->
		{
			System.out.println("Stage is closing");
			ClientUI.chat.accept(new LogOutMessage(organization.getUserName()), null);
		});
	}

	/**
	 * a function with an ActionEvent, when click on the correct button will appear a page transform to the ParkReportProducingController
	 * @param event the event that invoked is button click
	 * @throws IOException throw IOException
	 */
	@FXML
	void gotoCreateReport(ActionEvent event) throws IOException
	{
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage stage = new Stage();
		ParkReportProducingController parkReportProducingController = new ParkReportProducingController();
		try
		{
			parkReportProducingController.start(stage, organization);
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FAILED TO OPEN PARKMANAGER PAGE");
		}
	}

	/**
	 * a function with an ActionEvent, when click on the correct button will appear a page transform
	 * @param event the event that invoked is button click
	 */
	@FXML
	void gotoParkSettings(ActionEvent event)
	{
		ClientUI.chat.accept(new ParkSettingsMessage(organization.getOrganization()), new OnResponseListener()
		{
			@Override
			public void onResponse(Object message)
			{
				Platform.runLater(() ->
				{
					((Node) event.getSource()).getScene().getWindow().hide();
					Stage stage = new Stage();
					ParkSettingsController parkSettingsController = new ParkSettingsController();
					try
					{
						parkSettingsController.start(stage, organization, ChatClient.str);
					} catch (Exception e)
					{
						System.out.println("FAILED TO OPEN PARK !WORKER HOME PAGE");
					}
				});

			}
		});

	}

	/**
	 * a function with an MouseEvent, when clicked the numbers of current visitors will be update
	 * @param event the event sent a message to update the current number of visitors
	 */
	@FXML
	void refreshNumbers(MouseEvent event)
	{
		ClientUI.chat.accept(new CurrentNumbersMessage(organization.getOrganization()), new OnResponseListener()
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
						loadCurrentNumbers(ChatClient.str);
					});
				}
			}
		});
	}

	/**
	 * a function that load the current number of visitors that sent from the database
	 * @param s a String that contain the current number of visitors
	 */
	public void loadCurrentNumbers(String s)
	{
		String[] str = s.split(" ");
		fldCurrentNumOfVisitors.setText(str[0]);
		fldCurrentParkSpares.setText(str[1]);
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
	 * load the correct park name
	 */
	public void loadOrganization()
	{
		parkManagerhpTitle.setText(organization.getOrganization() + " Home Page");
		refreshNumbers(null);
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
