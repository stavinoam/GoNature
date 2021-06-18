package logic;

/**
 * CancelOrderMessage is a message from the client to the server it contains an
 * instance of order.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class CancelOrderMessage extends Message<String> {

	/**
	 * Save the string as the data of the message
	 * 
	 * @param data string of the data
	 */
	public CancelOrderMessage(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

}
