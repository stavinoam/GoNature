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
import logic.ParkSettingsMessage;
import logic.SettingsStatus;
import logic.Worker;

/**
 * SettingsStatusController class
 * The class shows the setting status after sent for approve inside a table
 * @author Shahar Yarden
 *
 */
public class SettingsStatusController implements Initializable
{
	/**
	 * organization is object appearance of Worker 
	 */
	private Worker organization;

	/**
	 * Label lblSettings
	 */
	@FXML
	private Label lblSettings;

	/**
	 * TableView<SettingsStatus> statusTable
	 */
	@FXML
	private TableView<SettingsStatus> statusTable;

	/**
	 * TableColumn<SettingsStatus, String> clmnSettingChanged
	 */
	@FXML
	private TableColumn<SettingsStatus, String> clmnSettingChanged;

	/**
	 * TableColumn<SettingsStatus, String> clmnBefore
	 */
	@FXML
	private TableColumn<SettingsStatus, String> clmnBefore;

	/**
	 * TableColumn<SettingsStatus, String> clmnAfter
	 */
	@FXML
	private TableColumn<SettingsStatus, String> clmnAfter;

	/**
	 * TableColumn<SettingsStatus, String> clmnChangeTime
	 */
	@FXML
	private TableColumn<SettingsStatus, String> clmnChangeTime;

	/**
	 * TableColumn<SettingsStatus, String> clmnStatus
	 */
	@FXML
	private TableColumn<SettingsStatus, String> clmnStatus;

	/**
	 * TableColumn<SettingsStatus, String> clmnResponse
	 */
	@FXML
	private TableColumn<SettingsStatus, String> clmnResponse;

	/**
	 * JFXButton btnParkSettings
	 */
	@FXML
	private JFXButton btnParkSettings;

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
	 * ObservableList settingList - list of SettingsStatus type that contain all of the settings status 
	 */
	ObservableList<SettingsStatus> settingList = FXCollections.observableArrayList();
	
	/**
	 * s string that contain all of the setting status that wait for response
	 */
	private String s;
	
	
	/**
	 * starting a new stage 
	 * @param primaryStage object of a stage
	 * @param w object of Worker type
	 * @param s string that contain the settings status that wait for response
	 * @throws Exception throw any exception
	 */
	public void start(Stage primaryStage, Worker w, String s) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SettingsStatus.fxml"));
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
		this.s = s;
		loadSettingsStatus(this.s);
		primaryStage.setOnCloseRequest(event ->
		{
			System.out.println("Stage is closing");
			ClientUI.chat.accept(new LogOutMessage(organization.getUserName()), null);
		});
	}
	
	/**
	 * a function with an ActionEvent, when click on the correct button it will return to the last page(stage) ParkSettingsController
	 * @param event the event that invoked is button click
	 * @throws IOException throw IOException
	 */
	@FXML
	void backToSettings(ActionEvent event) throws IOException
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
						System.out.println("FAILED TO OPEN PARK WORKER HOME PAGE");
					}
				});

			}
		});
	}

	/**
	 * A function that load all of the data from the data base into the text table in the stage
	 * @param s String that contain all of the settings status that sent for approve
	 */
	public void loadSettingsStatus(String s)
	{
		String[] str;
		str = s.split("/");
		SettingsStatus capacity = new SettingsStatus(str[0], str[1], str[2], str[3], str[4], str[5]);
		SettingsStatus spares = new SettingsStatus(str[6], str[7], str[8], str[9], str[10], str[11]);
		SettingsStatus traveltime = new SettingsStatus(str[12], str[13], str[14], str[15], str[16], str[17]);
		SettingsStatus discount = new SettingsStatus(str[18], str[19], str[20], str[21], str[22], str[23]);
		settingList.add(capacity);
		settingList.add(spares);
		settingList.add(traveltime);
		settingList.add(discount);
	}

	/**
	 * initialize the hamburger settings 
	 * initialize the table
	 */
	@Override
	public void initialize(URL location, ResourceBundle resources)
	{
		clmnSettingChanged.setCellValueFactory(new PropertyValueFactory<>("ClmnSettingChanged"));
		clmnBefore.setCellValueFactory(new PropertyValueFactory<>("ClmnBefore"));
		clmnAfter.setCellValueFactory(new PropertyValueFactory<>("ClmnAfter"));
		clmnChangeTime.setCellValueFactory(new PropertyValueFactory<>("ClmnChangeTime"));
		clmnStatus.setCellValueFactory(new PropertyValueFactory<>("ClmnStatus"));
		clmnResponse.setCellValueFactory(new PropertyValueFactory<>("ClmnResponse"));
		statusTable.setItems(settingList);

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
