package logic;

/**
 * VisitorsReportResultMessage is a message from the server to the client
 * contains an instance of an array of String.
 * 
 * @author Yarden Shahar
 *
 */
@SuppressWarnings("serial")
public class VisitorsReportResultMessage extends Message<String[]> {
	/**
	 * Save the array of String as the data of the message
	 * 
	 * @param data data
	 */
	public VisitorsReportResultMessage(String[] data) {
		super(data);
	}
}