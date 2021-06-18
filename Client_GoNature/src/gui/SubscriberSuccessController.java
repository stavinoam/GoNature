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
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import logic.LogOutMessage;
import logic.Worker;

/**
 * A page indicates that the registration was successful
 * @author Yarden Shahar
 *
 */
public class SubscriberSuccessController implements Initializable
{

	/**
	 * registration title label
	 */
	@FXML
	private Label registrationTitle;

	/**
	 * label to show a smiley emoji
	 */
	@FXML
	private Label smiley;
	
	/**
	 * label to show the number of the subscriber
	 */
	@FXML
	private Label lblSubscriberNumber;

	/**
	 * textfield to show the number of the subscriber
	 */
	@FXML
	private TextField fldSubNum;

    /**
     * button to register another user
     */
    @FXML
    private JFXButton btnRegisterSubscriber;

    /**
     * hamburger menu
     */
    @FXML
    private JFXHamburger menu;

    /**
     * anchorpane of hamburger
     */
    @FXML
    private AnchorPane drawerpane;

    /**
     * drawer for hamburger
     */
    @FXML
    private JFXDrawer drawer;

    /**
     * object appearance of Worker
     */
    private Worker worker;
    
	/**
	 * start method to open a window and set worker
	 * @param primaryStage starts a new stage
	 * @param w worker object
	 * @param s subscriber number
	 * @throws Exception throws a new exception
	 */
	public void start(Stage primaryStage, Worker w, String s) throws Exception
	{
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/SubscriberSuccess.fxml"));
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
		this.worker = w;
		loadSubscriberNum(s);
		primaryStage.setOnCloseRequest(event ->
		{
			System.out.println("Stage is closing");
			ClientUI.chat.accept(new LogOutMessage(worker.getUserName()), null);
		});
	}
    
	/**
	 * method to register another user
	 * @param event buttonaction
	 * @throws IOException throws a new input output exception
	 */
	@FXML
	void registerSubcriber(ActionEvent event) throws IOException
	{
		try
		{
			((Node) event.getSource()).getScene().getWindow().hide();
			Stage stage = new Stage();
			RegistrationController registrationController = new RegistrationController();
			registrationController.start(stage, worker);
		} catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("FAILED TO OPEN PARK !WORKER HOME PAGE");
		}
	}

	/**
	 * method to load the subscriber number to the textfield
	 * @param subNum loads the subscriber number
	 */
	public void loadSubscriberNum(String subNum)
	{
		fldSubNum.setText(subNum);
	}

	/**
	 *initialize the hamburger
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
							RegistrationController registrationController = new RegistrationController();
							try
							{
								registrationController.start(stage3, worker);
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
