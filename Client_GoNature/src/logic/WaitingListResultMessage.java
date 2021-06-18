package logic;

/**
 * WaitingListResultMessage is a message from the client to the server it
 * contains an instance of a String.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class WaitingListResultMessage extends Message<String> {

	/**
	 * Save the String as the data of the message
	 * 
	 * @param string the string data
	 */
	public WaitingListResultMessage(String string) {
		super(string);
	}

}
