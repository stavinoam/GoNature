package gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;

import client.ChatClient;
import client.ClientUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;
import logic.ClientAlreadyConnectedResultMessage;
import logic.OnResponseListener;
import logic.SubscriberSearchMessage;

/**
 * SubscriberLoginController is a controller of SubscriberLogin.fxml FXML page.
 * The controller implements all the functionals of SubscriberLogin.fxml 
 * @author Stav Anna
 *
 */
public class SubscriberLoginController
{

	@FXML
	private JFXTextField fldSubscriberNum;

	@FXML
	private JFXButton btnLogin;

	/**
	 * the method respond to user click on "login" button
	 * the method checks if the subscriber number appears in the DB
	 * if he does - leads him to the traveler home page, else show a pop-up message
	 * @param event click on "login" button
	 */
	@FXML
	void subscriberLogin(ActionEvent event)
	{
		// FXMLLoader loader = new FXMLLoader();
		String sub_num = fldSubscriberNum.getText();
		if (sub_num.trim().isEmpty())
			alert("Empty field.", "You must enter subscriber number.");
		else
		{ // subscriber / traveler with order / traveler without an order
			// if there's such subscriber on db
			ClientUI.chat.accept(new SubscriberSearchMessage(sub_num), new OnResponseListener()
			{
				@Override
				public void onResponse(Object message)
				{
					if (message instanceof ClientAlreadyConnectedResultMessage)
					{
						Platform.runLater(() ->
						{
							alert("Login error", "You are already connected to the system.\n"
									+ "Please log out from the other instance, then try again.");
						});
					} else if (ChatClient.subscriber == null)
					{// subscriber does not exist
						Platform.runLater(() ->
						{
							alert("Wrong subscriber number", "Subscriber number is incorrect.");
						});
					} else
					{ // subscriber exists
						Platform.runLater(() ->
						{
							((Node) event.getSource()).getScene().getWindow().hide();
							Stage stage = new Stage();
							TravelerHomePageController traveler = new TravelerHomePageController();
							// TravelerHomePageController traveler = loader.getController();
							//traveler.loadTraveler(ChatClient.traveler);
							try
							{
								traveler.start(stage, ChatClient.subscriber);// ***********
							} catch (Exception e)
							{
								System.out.println("FAILED TO OPEN TRAVELER HOME PAGE");
							}
						});
					}
				}
			});

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