package logic;

/**
 * 
 * DenyApprovalMessage is a message from the client to the server it contains an
 * instance of String[].
 * 
 * @author Shahar Yrarden
 *
 */
@SuppressWarnings("serial")
public class DenyApprovalMessage extends Message<String[]> {
	/**
	 * Save the String[] as the data of the message
	 * 
	 * @param data data
	 */
	public DenyApprovalMessage(String[] data) {
		super(data);
	}
}