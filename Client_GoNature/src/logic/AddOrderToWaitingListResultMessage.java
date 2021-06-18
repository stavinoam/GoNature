package logic;

/**
 * AddOrderToWaitingListResultMessage is a message from the server to the client
 * contains an instance of String.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class AddOrderToWaitingListResultMessage extends Message<String> {

	/**
	 * Save the string as the data of the message
	 * 
	 * @param data string of the data
	 */
	public AddOrderToWaitingListResultMessage(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

}
