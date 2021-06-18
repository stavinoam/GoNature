package Server;

import javafx.application.Application;
import javafx.stage.Stage;

import gui.ServerPortFrameController;

/**
 * ServerUI class is the user interface of the system server
 * @author Stav Anna
 *
 */
public class ServerUI extends Application {
	public static void main(String args[]) throws Exception {
		launch(args);
	}

	/**
	 * Start method starts the ServerPortFrameController page
	 * the method implements Application method
	 */
	@Override
	public void start(Stage primaryStage) throws Exception {
		ServerPortFrameController aFrame = new ServerPortFrameController();
		aFrame.start(primaryStage);
	}

	/**
	 * The function run the sever - start listening for connections
	 * @param p The port to connect on.
	 */
	public static void runServer(String p) {
		int port = 0;
		try {
			port = Integer.parseInt(p);
		} catch (Throwable t) {
			System.out.println("ERROR - Could not connect!");
		}
		EchoServer sv = new EchoServer(port);
		try {
			sv.listen();
		} catch (Exception ex) {
			System.out.println("ERROR - Could not listen for clients!");
		}
	}

}
