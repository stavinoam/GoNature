package logic;

/**
 * ConfirmOrderMessage is a message from the client to the server it contains an
 * instance of string.
 * 
 * @author Shahe Yarden
 *
 */
@SuppressWarnings("serial")
public class ConfirmOrderMessage extends Message<String> {

	/**
	 * Save the string as the data of the message
	 * 
	 * @param data string of the data
	 */
	public ConfirmOrderMessage(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

}
