package gui;

import java.io.IOException;

import Server.EchoServer;
import Server.ServerUI;
import client.ClientUI;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import logic.LogOutMessage;

/**
 * A page to connect to the server.
 * @author Yarden Shahar
 *
 */
public class ServerPortFrameController
{

	/**
	 * field to insert a port number
	 */
	@FXML
	private TextField txtPort;

	/**
	 * field to show the connection status
	 */
	@FXML
	private TextField txtConStatus;

	/**
	 * label enter a port
	 */
	@FXML
	private Label lblEnterPort;

	/**
	 * label connection status
	 */
	@FXML
	private Label connectionStatus;

	/**
	 * page title label
	 */
	@FXML
	private Label serverTitle;

	/**
	 * button exit the page
	 */
	@FXML
	private Button btnExit;

	/**
	 * button connect to the server
	 */
	@FXML
	private Button btnConnect;

	/**
	 * button to go to card reader page
	 */
	@FXML
	private Button btnCardReader;


	/**
	 * method to create a connection to specific port
	 * @param event click "connect" button
	 * @throws Exception exception
	 */
	@FXML
	public void connectToPort(ActionEvent event) throws Exception
	{
		String p;
		p = getport();
		if (p.trim().isEmpty())
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Empty Port Number");
			alert.setHeaderText(null);
			alert.setContentText("You must enter a legal port number.");
			alert.show();

		} else
		{
			if (p.matches("[0-9]+") && p.length() >= 2)
			{
				// ((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary
				// window
				Stage primaryStage = new Stage();
				FXMLLoader loader = new FXMLLoader();
				ServerUI.runServer(p);
				setConnectionStatus("Connected to port " + EchoServer.PORT + ". Waiting for a client");
				EchoServer.setServerPortFrameController(this);
			} else
			{
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Illeagel Port Number");
				alert.setHeaderText(null);
				alert.setContentText("Port number must be a number and must contain 2 or more digits.");
				alert.show();
			}

		}

	}

	/**
	 * method to the get the port number from the users
	 * @return port text
	 */
	private String getport()
	{
		return txtPort.getText();
	}

	/**
	 * start method to open a window
	 * @param primaryStage the stage
	 * @throws Exception FXMLLoader
	 */
	public void start(Stage primaryStage) throws Exception
	{
		Parent root = FXMLLoader.load(getClass().getResource("/gui/ServerPort.fxml"));
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/ServerPort.css").toExternalForm());
		CardReaderController.primaryStage = primaryStage;
		primaryStage.setTitle("Server");
		primaryStage.setScene(scene);
		primaryStage.getIcons().add(new Image("gui/css/tree.png"));
		primaryStage.show();
		
		primaryStage.setOnCloseRequest(event ->
		{
			System.exit(0);
		});
	}

	/**
	 * button to exit the application
	 * @param event click "exit" button
	 * @throws Exception exception
	 */
	@FXML
	public void getExitBtn(ActionEvent event) throws Exception
	{
		System.out.println("Exit");
		System.exit(0);
	}

	/**
	 * method to set the connection status in the textfield
	 * @param msg message
	 */
	public void setConnectionStatus(String msg)
	{
		txtConStatus.setText(msg);
	}

	/**
	 * method to set the status of the connected user
	 * @param ip ip
	 * @param hostname host name
	 */
	public void setConnected(String ip, String hostname)
	{ // first change - added set setConnected
		Platform.runLater(() ->
		{
			setConnectionStatus("Connected to IP: " + ip + " Host: " + hostname);

		});
	}

	
	/**
	 * method to indicate that no client is connected
	 */
	public void setDisconnected()
	{ // first change - added set setConnected
		Platform.runLater(() ->
		{
			setConnectionStatus("Connected to port " + EchoServer.PORT + ". Waiting for a client");
		});
	}

	/**
	 * method to go to card reader page
	 * @param event event
	 * @throws IOException exception
	 */
	@FXML
	void goToCardReader(ActionEvent event) throws IOException
	{
			FXMLLoader loader = new FXMLLoader();
			((Node) event.getSource()).getScene().getWindow().hide(); // hiding primary window
			Stage primaryStage = new Stage();
			Pane root = loader.load(getClass().getResource("/gui/CardReader.fxml").openStream());
			Scene scene = new Scene(root);
			scene.getStylesheets().add(getClass().getResource("/gui/Style.css").toExternalForm());
			primaryStage.setTitle("GoNature");
			primaryStage.getIcons().add(new Image("gui/css/tree.png"));
			primaryStage.setScene(scene);
			primaryStage.show();
	}

	/**
	 * make a pop-up alert
	 * @param title title
	 * @param s content
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