package gui;

import java.io.IOException;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXPasswordField;
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
import logic.WorkerSearchMessage;

/**
 * SubscriberLoginController is a controller of SubscriberLogin.fxml FXML page.
 * The controller implements all the functionals of SubscriberLogin.fxml 
 * @author Stav Anna
 *
 */
public class WorkerLoginController
{

	@FXML
	private JFXTextField fldUsername;

	@FXML
	private JFXButton btnLogin;

	@FXML
	private JFXPasswordField fldPassword;

	/**
	 * the method respond to user click on "login" button
	 * the method checks if the user name appears in the DB with the match password
	 * if it does - leads the worker to the right page for him
	 * else shows a pop-up message
	 * @param event click on "login" button
	 */
	@FXML
	void workerLogin(ActionEvent event) throws IOException
	{
		String userName, password;
		String str[] = new String[2];
		// FXMLLoader loader = new FXMLLoader();
		userName = fldUsername.getText();
		password = fldPassword.getText();
		str[0] = userName;
		str[1] = password;
		if (userName.trim().isEmpty() || password.trim().isEmpty())
			alert("All fields are required", "You must enter user name AND password.");
		else
		{
			ClientUI.chat.accept(new WorkerSearchMessage(str), new OnResponseListener()
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
					} else if (ChatClient.worker.getUserName().equals("Error"))
					{
						Platform.runLater(() ->
						{
							alert("Worker not found", "User number or password incorrect.");
						});
					} else if (ChatClient.worker.getRole().equals("Department Manager"))
					{
						Platform.runLater(() ->
						{
							((Node) event.getSource()).getScene().getWindow().hide();
							Stage stage = new Stage();
							DepartmenthpController worker = new DepartmenthpController();
							try
							{
								worker.start(stage, ChatClient.worker);
							} catch (Exception e)
							{
								System.out.println("FAILED TO OPEN WORKER HOME PAGE");
							}
						});
					} else if (ChatClient.worker.getRole().equals("Park Manager"))
					{
						Platform.runLater(() ->
						{
							((Node) event.getSource()).getScene().getWindow().hide();
							Stage stage = new Stage();
							ParkManagerhpController worker = new ParkManagerhpController();
							try
							{
								worker.start(stage, ChatClient.worker);
							} catch (Exception e)
							{
								System.out.println("FAILED TO OPEN PARK !WORKER HOME PAGE");
							}
						});
					} else if (ChatClient.worker.getRole().equals("Service Representative"))
					{
						Platform.runLater(() ->
						{
							try
							{
								((Node) event.getSource()).getScene().getWindow().hide();
								Stage stage = new Stage();
								RegistrationController registrationController = new RegistrationController();
								registrationController.start(stage, ChatClient.worker);
							} catch (Exception e)
							{
								e.printStackTrace();
								System.out.println("FAILED TO OPEN PARK !WORKER HOME PAGE");
							}
						});
					} else if (ChatClient.worker.getRole().equals("Entrance Worker"))
					{
						Platform.runLater(() ->
						{
							try
							{
								((Node) event.getSource()).getScene().getWindow().hide();
								Stage stage = new Stage();
								ParkEntranceController parkEntranceController = new ParkEntranceController();
								try
								{
									parkEntranceController.start(stage, ChatClient.worker);
								} catch (Exception e)
								{
									System.out.println("FAILED TO OPEN PARK !WORKER HOME PAGE");
								}
							} catch (Exception e)
							{
								e.printStackTrace();
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
