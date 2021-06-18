package client;

import gui.LoginPageController;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 * ServerUI class is the user interface of the system client
 * 
 * @author Stav Anna
 *
 */
public class ClientUI extends Application {
	/**
	 * chat is of instance ClientController
	 */
	public static ClientController chat; // only one instance
	/**
	 * clientMessage is a string message
	 */
	@SuppressWarnings("unused")
	private static String clientMessage;

	/**
	 * Main function
	 * 
	 * @param args main String args[]
	 * @throws Exception any Exception
	 */
	public static void main(String args[]) throws Exception {
		launch(args);
	} // end main

	/**
	 * Start method starts the LoginPageController page the method implements
	 * Application method
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		chat = new ClientController("localhost", 5555);
		LoginPageController aFrame = new LoginPageController();
		aFrame.start(primaryStage);
	}

}
