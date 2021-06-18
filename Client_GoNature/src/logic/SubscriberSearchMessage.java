package logic;

/**
 * SubscriberSearchMessage is a message from the client to the server it
 * contains an instance of string.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class SubscriberSearchMessage extends Message<String> {

	/**
	 * Save the order as the data of the string
	 * 
	 * @param subNum subNum
	 */
	public SubscriberSearchMessage(String subNum) {
		super(subNum);
	}
}
