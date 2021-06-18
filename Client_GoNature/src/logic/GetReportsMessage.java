package logic;

/**
 * GetReportsMessage is a message from the client to the server it contains an
 * instance of string.
 * 
 * @author Shahar Yarden
 *
 */
@SuppressWarnings("serial")
public class GetReportsMessage extends Message<String> {
	/**
	 * Save the order as the data of the message
	 * 
	 * @param data data
	 */
	public GetReportsMessage(String data) {
		super(data);
	}
}