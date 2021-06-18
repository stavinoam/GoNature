package gui;

import Server.MysqlConnection;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

/** A page to demonstrante an outer system of card reader machine
 * user enters his identification number
 * @author Yarden Shahar
 *
 */
public class CardReaderController
{
	/**
	 * stage to save the last page
	 */
	static Stage primaryStage;

	/**
	 * anchor pane for hamburger menu
	 */
	@FXML
	private AnchorPane drawerpane;

	/**
	 * label card reader title
	 */
	@FXML
	private Label lblCardReaderTitle;

	/**
	 * label to ask the user to insert number
	 */
	@FXML
	private Label lblEnterOrderNum;

	/**
	 * field of id number
	 */
	@FXML
	private TextField fldOrderNumber;

    /**
     * button to check if the user can enter the park
     */
    @FXML
    private Button btnCheck;

    /**
     * button to go to the last page
     */
    @FXML
    private Button btnBack;

	
	/**
	 * check if the order exists in the database
	 * @param event click on "check" button
	 */
	@FXML
	void check(ActionEvent event)
	{
		if (!fldOrderNumber.getText().matches("[0-9]+"))
			alert("Input Error", "Order number must contain only numbers.");
		else
		{
			String s = MysqlConnection.getInstance().currentVisitorsUpdate(fldOrderNumber.getText());
					if (s.equals("ENTERED"))
					{
						Platform.runLater(() ->
						{
							alert("Success", "You can enter the park! have fun!!!");
						});
					} else if (s.equals("EXIT"))
					{
						Platform.runLater(() ->
						{
							alert("Goodbye", "Thank for the visit, see you next time.");
						});
					} else if (s.equals("IDNOTFOUND"))
					{
						Platform.runLater(() ->
						{
							alert("ID not found", "There's no order for the specified id/subscriber number\nor the order is not for today.");
						});
					} else if (s.equals("USED"))
					{
						Platform.runLater(() ->
						{
							alert("Already used", "The order is already used.");
						});
					} else if (s.equals("CANCELED"))
					{
						Platform.runLater(() ->
						{
							alert("Order canceled", "The order has been canceled.");
						});
					} else if (s.contains("WRONGTIME"))
					{
						Platform.runLater(() ->
						{
							String[] str = s.split(" ");
							alert("Wrong time", "The order exists but for other date.\nOrder date: " + str[1]);
						});
					} else if (s.contains("WRONGHOUR")) {
						Platform.runLater(() ->
						{
							String[] str = s.split(" ");
							alert("Wrong time", "The order exists but for other time.\nOrder time: " + str[1] + " - " + str[3]);
						});
					}
					else {
						Platform.runLater(() ->
						{
							alert("Failed", "Something went wrong.");
						});
					}
				}
	}

	/**
	 * The method respond to user click on  "back" button
	 * The method leads the user to the last page
	 * @param event clicking on "back" button
	 */
	@FXML
	void goToLastPage(ActionEvent event)
	{
		((Node) event.getSource()).getScene().getWindow().hide();
		primaryStage.show();
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
