package logic;

/**
 * CurrentNumbersResultMessage is a message from the server to the client
 * contains an instance of string.
 * 
 * @author Shahar Yarden
 *
 */
@SuppressWarnings("serial")
public class CurrentNumbersResultMessage extends Message<String> {
	/**
	 * Save the string as the data of the message
	 * 
	 * @param data string of the data
	 */
	public CurrentNumbersResultMessage(String data) {
		super(data);
	}
}
