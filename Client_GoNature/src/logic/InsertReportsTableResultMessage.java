package logic;

/**
 * InsertReportsTableResultMessage is a message from the server to the client
 * contains an instance of boolean.
 * 
 * @author Shahar Yarden
 *
 */
@SuppressWarnings("serial")
public class InsertReportsTableResultMessage extends Message<Boolean> {
	/**
	 * Save the string as the data of the message
	 * 
	 * @param data data
	 */
	public InsertReportsTableResultMessage(Boolean data) {
		super(data);
	}
}