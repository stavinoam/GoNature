package logic;

/**
 * AddOrderToOrderListResultMessage is a message from the server to the client
 * contains an instance of string.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class AddOrderToOrderListResultMessage extends Message<String> {

	/**
	 * Save the string as the data of the message
	 * 
	 * @param data string of the data
	 */
	public AddOrderToOrderListResultMessage(String data) {
		super(data);
	}

}
