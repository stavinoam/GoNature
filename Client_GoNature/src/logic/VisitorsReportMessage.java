package logic;

/**
 * VisitorsReportMessage is a message from the client to the server it contains
 * an instance of an array of Strings.
 * 
 * @author Yarden Shahar
 *
 */
@SuppressWarnings("serial")
public class VisitorsReportMessage extends Message<String[]> {
	/**
	 * Save the array of Strings as the data of the message
	 * 
	 * @param data data
	 */
	public VisitorsReportMessage(String[] data) {
		super(data);
	}
}