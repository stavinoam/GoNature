package logic;

/**
 * AddOrderToOrderListResultMessage is a message from the server to the client
 * contains an instance of string.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class SubscriberRegistrationResultMessage extends Message<String> {
	/**
	 * Save the string as the data of the message
	 * 
	 * @param data data
	 */
	public SubscriberRegistrationResultMessage(String data) {
		super(data);
	}
}
