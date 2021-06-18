package logic;

/**
 * ConfirmationMessage is a message from the client to the server it contains an
 * instance of string
 * 
 * @author Shahar Yarden
 *
 */
@SuppressWarnings("serial")
public class ConfirmationMessage extends Message<String> {
	/**
	 * Save the string as the data of the message
	 * 
	 * @param data string of the data
	 */
	public ConfirmationMessage(String data) {
		super(data);
	}
}
