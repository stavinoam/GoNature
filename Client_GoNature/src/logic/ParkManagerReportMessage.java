package logic;

/**
 * ParkManagerReportMessage is a message from the client to the server it
 * contains an instance of String[].
 * 
 * @author Shahar Yarden
 *
 */
@SuppressWarnings("serial")
public class ParkManagerReportMessage extends Message<String[]> {
	/**
	 * Save the String[] as the data of the message
	 * 
	 * @param data data
	 */
	public ParkManagerReportMessage(String[] data) {
		super(data);
	}
}
