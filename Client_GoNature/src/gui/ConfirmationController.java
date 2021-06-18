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
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.Confirmation;
import logic.DenyApprovalMessage;
import logic.LogOutMessage;
import logic.OnResponseListener;
import logic.UpdateSettingsMessage;
import logic.Worker;

/**
 * Class for department manager to approve of deny settings.
 * @author Yarden Shahar
 *
 */
public class ConfirmationController implements Initializable
{

	/**
	 * button to save settings
	 */
	@FXML
	private JFXButton btnSave;
	/**
	 * button to delete settings
	 */
	@FXML
	private JFXButton btnDelete;
	/**
	 * back button
	 */
	@FXML
	private JFXButton btnBack;

	/**
	 * hamburger menu
	 */
	@FXML
	private JFXHamburger menu;

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
	 * label confirmation
	 */
	@FXML
	private Label labelConfirmation;

	/**
	 * table of requests to confirm
	 */
	@FXML
	private TableView<Confirmation> confirmTable;

	/**
	 * column park name
	 */
	@FXML
	private TableColumn<Confirmation, String> clmnParkName;

	/**
	 * column setting name
	 */
	@FXML
	private TableColumn<Confirmation, String> clmnSettingName;

	/**
	 * column before value
	 */
	@FXML
	private TableColumn<Confirmation, String> clmnBefore;

	/**
	 * column after value
	 */
	@FXML
	private TableColumn<Confirmation, String> clmnAfter;

	/**
	 * column request time
	 */
	@FXML
	private TableColumn<Confirmation, String> clmnRequestTime;

	/**
	 * column response
	 */
	@FXML
	private TableColumn<Confirmation, CheckBox> clmnResponse;

	/**
	 * list of requests
	 */
	ObservableList<Confirmation> settingList = FXCollections.observableArrayList();
	/**
	 * object appearance of worker
	 */
	private Worker worker;
	
	/**
	 * Start method is the method that starts the stage with the match fxml file 
	 * @param primaryStage the new stage that is created before calling the start method
	 * @param w the worker that uses the page
	 * @param s array of strings from the DB of the table content
	 * @throws Exception if the FXMLLoader can't load the fxml file
	 */
	public void start(Stage primaryStage, Worker w, String s) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/Confirmation.fxml"));
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
		loadConfirmationSettings(s);
		primaryStage.setOnCloseRequest(event ->
		{
			System.out.println("Stage is closing");
			ClientUI.chat.accept(new LogOutMessage(worker.getUserName()), null);
		});
	}
	
	/**
	 * The method respond to user click on  "back" button
	 * The method leads the user to the last page that he used before entering "confirmation page"
	 * @param event clicking on "back" button
	 */
	@FXML
	void back(ActionEvent event)
	{
		((Node) event.getSource()).getScene().getWindow().hide();
		Stage stage = new Stage();
		DepartmenthpController worker = new DepartmenthpController();
		try
		{
			worker.start(stage, this.worker);
		} catch (Exception e)
		{
			System.out.println("FAILED TO OPEN WORKER HOME PAGE");
		}
	}

	/**
	 * The method respond to user click on  "delete" button
	 * The method deletes the request row
	 * @param event clicking on "delete" button
	 */
	@FXML
	void deleteSelected(ActionEvent event)
	{
		int i = 0;
		for (Confirmation c : confirmTable.getItems())
		{
			if (c.getClmnResponse().isSelected())
				i++;
		}
		if (i == 0)
			alert("Error", "No setting selected.");
		else
		{
			String[] s = new String[i * 3];
			i = 0;

			for (Confirmation c : confirmTable.getItems())
			{
				if (c.getClmnResponse().isSelected())
				{
					s[i++] = c.getClmnParkName();
					s[i++] = c.getClmnSettingName();
					s[i++] = c.getClmnAfter();
					Platform.runLater(() ->
					{
						confirmTable.getItems().remove(c);
					});
				}
			}
			denyApproval(s);
		}
	}

	/**
	 * the method denies the request s
	 * @param s the request data that we want to deny
	 */
	private void denyApproval(String[] s)
	{
		ClientUI.chat.accept(new DenyApprovalMessage(s), new OnResponseListener()
		{
			@Override
			public void onResponse(Object message)
			{
				if (ChatClient.flag)
				{
					Platform.runLater(() ->
					{
						alert("Success", "Approval denied.");
					});
				} else
				{
					System.out.println("not updated");
					Platform.runLater(() ->
					{
						alert("Failure", "Operation failed!");
					});
				}
			}
		});
	}

	/**
	 * The method respond to user click on "save" button
	 * The method saves requested settings
	 * @param event clicking on "save" button
	 */
	@FXML
	void saveSettings(ActionEvent event)
	{
		int i = 0;
		for (Confirmation c : confirmTable.getItems())
		{
			if (c.getClmnResponse().isSelected())
				i++;
		}

		if (i == 0)
			alert("Error", "No setting selected.");
		else if (checkSparesBiggerThanCapacity())
			alert("Error", "Spares cannot be bigger than capacity.");
		else
		{
			String[] s = new String[i * 3];
			i = 0;
			for (Confirmation c : confirmTable.getItems())
			{
				if (c.getClmnResponse().isSelected())
				{
					s[i++] = c.getClmnParkName();
					s[i++] = c.getClmnSettingName();
					s[i++] = c.getClmnAfter();
					Platform.runLater(() ->
					{
						confirmTable.getItems().remove(c);
					});
				}
			}
			updateSettings(s);
		}
	}

	/**
	 * function checks if spares is bigger than capacity
	 * @return if spare>capacity returns true, else returns false
	 */
	private boolean checkSparesBiggerThanCapacity()
	{
		String park_nameCapacity = "";
		int capacity_before = -1;
		int spares_after = -1;
		for (Confirmation c : confirmTable.getItems())
		{
			if (!c.getClmnResponse().isSelected())
			{
				park_nameCapacity = c.getClmnParkName();

				if (c.getClmnSettingName().equals("Park capacity"))
					capacity_before = Integer.parseInt(c.getClmnBefore());
			} else // Means row is selected.
			{
				if (c.getClmnParkName().equals(park_nameCapacity) && c.getClmnSettingName().equals("Park spares"))
				{
					spares_after = Integer.parseInt(c.getClmnAfter());
				}
			}

		}
		return spares_after > capacity_before;
	}

	/**
	 * the method updates a requested setting by message to the server
	 * @param s the data that we want to update
	 */
	private void updateSettings(String[] s)
	{
		ClientUI.chat.accept(new UpdateSettingsMessage(s), new OnResponseListener()
		{
			@Override
			public void onResponse(Object message)
			{
				if (ChatClient.flag)
				{
					Platform.runLater(() ->
					{
						alert("Success", "Items were updated successfully.");
					});
				} else
				{
					System.out.println("not updated");
					Platform.runLater(() ->
					{
						alert("Failure", "Updating failed!");
					});
				}
			}
		});
	}

	/**
	 * the method loads the data to the table
	 * @param s the data from the DB to be loaded at the table
	 */
	public void loadConfirmationSettings(String s)
	{
		String[] str;
		str = s.split("/");
		int size = str.length / 6;
		Confirmation[] c = new Confirmation[size];
		for (int i = 0, j = 0; i < size; i++, j += 6)
		{
			CheckBox ch = new CheckBox();
			c[i] = new Confirmation(str[j], str[j + 1], str[j + 2], str[j + 3], str[j + 4], ch);
			settingList.add(c[i]);

		}
	}

	/**
	 * initialize method is the first method to be run when the controller is starting
	 * the method initializes the parameters of the fxml page
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1)
	{
		clmnParkName.setCellValueFactory(new PropertyValueFactory<>("ClmnParkName"));
		clmnSettingName.setCellValueFactory(new PropertyValueFactory<>("ClmnSettingName"));
		clmnBefore.setCellValueFactory(new PropertyValueFactory<>("ClmnBefore"));
		clmnAfter.setCellValueFactory(new PropertyValueFactory<>("ClmnAfter"));
		clmnRequestTime.setCellValueFactory(new PropertyValueFactory<>("ClmnRequest"));
		clmnResponse.setCellValueFactory(new PropertyValueFactory<>("ClmnResponse"));
		confirmTable.setItems(settingList);

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

}
