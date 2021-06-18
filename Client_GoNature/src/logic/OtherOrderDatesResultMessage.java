package logic;

/**
 * OtherOrderDatesResultMessage is a message from the server to the client
 * contains an instance of string.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class OtherOrderDatesResultMessage extends Message<String> {

	/**
	 * Save the string as the data of the message
	 * 
	 * @param str string
	 */
	public OtherOrderDatesResultMessage(String str) {
		super(str);
	}

}
