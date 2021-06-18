package logic;

/**
 * SubscriberRegistrationMessage is a message from the client to the server it
 * contains an instance of String[]
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class SubscriberRegistrationMessage extends Message<String[]> {
	/**
	 * Save the string as the data of the message
	 * 
	 * @param data data
	 */
	public SubscriberRegistrationMessage(String[] data) {
		super(data);
	}
}
