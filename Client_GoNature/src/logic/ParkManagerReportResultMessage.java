package logic;

/**
 * ParkManagerReportResultMessage is a message from the server to the client
 * contains an instance of string.
 * 
 * @author Shahar Yarden
 *
 */
@SuppressWarnings("serial")
public class ParkManagerReportResultMessage extends Message<String[]> {
	/**
	 * Save the String[] as the data of the message
	 * 
	 * @param data data
	 */
	public ParkManagerReportResultMessage(String[] data) {
		super(data);
	}
}
