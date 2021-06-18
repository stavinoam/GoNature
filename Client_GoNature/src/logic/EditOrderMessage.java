package logic;

/**
 * EditOrderMessage is a message from the client to the server it contains an
 * instance of string
 * 
 * @author Shahar Yarden
 *
 */
@SuppressWarnings("serial")
public class EditOrderMessage extends Message<String> {
	/**
	 * Save the order as the data of the message
	 * 
	 * @param data data
	 */
	public EditOrderMessage(String data) {
		super(data);
	}
}
