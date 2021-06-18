package logic;

/**
 * CheckIfOrderSucceededResultMessage is a message from the server to the client
 * contains an instance of String.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class CheckIfOrderSucceededResultMessage extends Message<String> {

	/**
	 * Save the string as the data of the message
	 * 
	 * @param str string of the data
	 */
	public CheckIfOrderSucceededResultMessage(String str) {
		super(str);
	}

}
