package logic;

/**
 * DepartmentReportMessage is a message from the client to the server it
 * contains an instance of order.
 * 
 * @author Shahar Yarden
 *
 */
@SuppressWarnings("serial")
public class DepartmentReportMessage extends Message<String[]> {
	/**
	 * Save the String[] as the data of the message
	 * 
	 * @param data data
	 */
	public DepartmentReportMessage(String[] data) {
		super(data);
	}
}