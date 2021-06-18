package logic;

/**
 * GetReportsResultMessage is a message from the server to the client contains
 * an instance of string.
 * 
 * @author Shahar Yarden
 *
 */
@SuppressWarnings("serial")
public class GetReportsResultMessage extends Message<String> {
	/**
	 * Save the string as the data of the message
	 * 
	 * @param data data
	 */
	public GetReportsResultMessage(String data) {
		super(data);
	}
}