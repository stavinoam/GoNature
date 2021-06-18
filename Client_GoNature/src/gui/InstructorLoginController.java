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
import logic.InstructorSearchMessage;
import logic.OnResponseListener;

/**
 * InstructorLoginController is a controller of InstructorLogin.fxml FXML page.
 * The controller implements all the functionals of InstructorLogin.fxml 
 * @author Stav  Anna
 *
 */
public class InstructorLoginController {

	@FXML
	private JFXTextField fldID;

	@FXML
	private JFXButton btnLogin;

	/**
	 * the method respond to user click on "login" button
	 * the method checks if the instructor id appears in the DB
	 * if he does - leads him to the traveler home page, else show a pop-up message
	 * @param event click on "login" button
	 */
	@FXML
	void instructorLogin(ActionEvent event) {
		String id = fldID.getText();
		if (id.contains("[a-zA-Z]+") || id.length() != 9)
			alert("Wrong id number", "Id number must contains 9 digits.");
		else {
			if (id.trim().isEmpty())
				alert("Empty field.", "You must enter ID number.");
			else {
				ClientUI.chat.accept(new InstructorSearchMessage(id), new OnResponseListener() {
					@Override
					public void onResponse(Object message) {
						if (message instanceof ClientAlreadyConnectedResultMessage)
						{
							Platform.runLater(() ->
							{
								alert("Login error", "You are already connected to the system.\n"
										+ "Please log out from the other instance, then try again.");
							});
						} else if (ChatClient.instructor == null) {// instructor does not exist
							Platform.runLater(() -> {
								alert("Wrong ID",
										"ID number is incorrect.\nIf you are not an instructor please try to login as a traveler.");
							});
						} else { // instructor exists
							Platform.runLater(() -> {
								((Node) event.getSource()).getScene().getWindow().hide();
								Stage stage = new Stage();
								TravelerHomePageController traveler = new TravelerHomePageController();
								// TravelerHomePageController traveler = loader.getController();
								// traveler.loadTraveler(ChatClient.traveler);
								try {
									traveler.start(stage, ChatClient.instructor);// ***********
								} catch (Exception e) {
									System.out.println("FAILED TO OPEN TRAVELER HOME PAGE");
								}
							});
						}
					}
				});

			}
		}
	}

	/**
	 * alert method shows a information dialog to the screen
	 * @param title the title of the information dialog
	 * @param s the content of the information dialog
	 */
	public void alert(String title, String s) {
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(s);
		alert.show();
	}

}
