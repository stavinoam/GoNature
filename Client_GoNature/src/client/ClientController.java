
package client;
import java.io.IOException;
import common.ChatIF;
import logic.Message;
import logic.OnResponseListener;

/**
 * This class constructs the UI for a chat client. It implements the chat
 * interface in order to activate the display() method. Warning: Some of the
 * code here is cloned in ServerConsole
 * @author Stav Anna
 *
 */
public class ClientController implements ChatIF
{
	/**
	 * The default port to connect on.
	 */
	public static int DEFAULT_PORT;
	/**
	 * The instance of the client that created this ConsoleChat.
	 */
	ChatClient client;

	/**
	 * Constructs an instance of the ClientConsole UI.
	 * @param host The host to connect to.
	 * @param port The port to connect on.
	 */
	public ClientController(String host, int port)
	{
		try
		{
			client = new ChatClient(host, port, this);
		} catch (IOException exception)
		{
			System.out.println("Error: Can't setup connection!" + " Terminating client.");
			System.exit(1);
		}
	}

	/**
	 * This method waits for input from the console. Once it is received, it sends
	 * it to the client's message handler.
	 * @param message The message from the UI.
	 * @param listener an object that waits for a response from the server
	 */
	public void accept(Message message, OnResponseListener listener)
	{
		client.handleMessageFromClientUI(message, listener);
		display(message.getData().toString());
	}
	/**
	 * This method overrides the method in the ChatIF interface. It displays a
	 * message onto the screen.
	 */
	//message The string to be displayed.
	public void display(String message)
	{
		System.out.println("> " + message);
	}
}
