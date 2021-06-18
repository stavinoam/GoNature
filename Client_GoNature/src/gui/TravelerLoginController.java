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
import logic.Traveler;
import logic.TravelerSearchMessage;

/**
 * TravelerLoginController is a controller of TravelerLogin.fxml FXML page.
 * The controller implements all the functionals of TravelerLogin.fxml 
 * @author Stav Anna
 *
 */
public class TravelerLoginController {

    @FXML
    private JFXTextField fldID;

    @FXML
    private JFXButton btnLogin;
    
	/**
	 * the method respond to user click on "login" button
	 * the method checks if the traveler id appears in the DB
	 * if he does - leads him to the traveler home page
	 * if he doesn't - leads him to the book a visit page as a new traveler (user)
	 * @param event click on "login" button
	 */
	@FXML
	void travelerLogin(ActionEvent event) {
		String id = fldID.getText();
		if (id.contains("[a-zA-Z]+") || id.length() != 9)
			alert("Wrong id number", "Id number must contains 9 digits.");
		else {
			if (id.trim().isEmpty())
				alert("Empty field", "You must enter id number.");
			else { // subscriber / traveler with order / traveler without an order
					// check which traveler is it - with order or without
				ClientUI.chat.accept(new TravelerSearchMessage(id), new OnResponseListener() {
					@Override
					public void onResponse(Object message) {
						if (message instanceof ClientAlreadyConnectedResultMessage)
						{
							Platform.runLater(() ->
							{
								alert("Login error", "You are already connected to the system.\n"
										+ "Please log out from the other instance, then try again.");
							});
						} else if (ChatClient.traveler == null) {// traveler without order
							Platform.runLater(() -> {
								((Node) event.getSource()).getScene().getWindow().hide();
								Stage stage = new Stage();
								Traveler newTraveler = new Traveler();
								newTraveler.setIdNumber(id);
								BookAVisitController book = new BookAVisitController();
								try {
									book.start(stage, newTraveler);// *******
								} catch (Exception e) {
									e.printStackTrace();
									System.out.println("FAILED TO OPEN BOOKING PAGE");
								}
							});
						} else { // traveler WITH order
							Platform.runLater(() -> {
								((Node) event.getSource()).getScene().getWindow().hide();
								Stage stage = new Stage();
								TravelerHomePageController traveler = new TravelerHomePageController();
								// TravelerHomePageController traveler = loader.getController();
								// traveler.loadTraveler(ChatClient.traveler);
								try {
									traveler.start(stage, ChatClient.traveler);// ****
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
	public void alert(String title, String s)
	{
		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(title);
		alert.setHeaderText(null);
		alert.setContentText(s);
		alert.show();
	}
}
