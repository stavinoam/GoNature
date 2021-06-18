package logic;

/**
 * InsertReportsTableMessage is a message from the client to the server it
 * contains an instance of String[].
 * 
 * @author Shahar Yarden
 *
 */
@SuppressWarnings("serial")
public class InsertReportsTableMessage extends Message<String[]> {
	/**
	 * Save the order as the data of the message
	 * 
	 * @param data data
	 */
	public InsertReportsTableMessage(String[] data) {
		super(data);
	}
}