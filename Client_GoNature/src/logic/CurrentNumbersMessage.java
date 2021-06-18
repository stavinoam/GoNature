package logic;

/**
 * CurrentNumbersMessage is a message from the client to the server it contains
 * an instance of string.
 * 
 * @author Shahar Yarden
 *
 */
@SuppressWarnings("serial")
public class CurrentNumbersMessage extends Message<String> {
	/**
	 * Save the string as the data of the message
	 * 
	 * @param data string of the data
	 */
	public CurrentNumbersMessage(String data) {
		super(data);
	}
}
