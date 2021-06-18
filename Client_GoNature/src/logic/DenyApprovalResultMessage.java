package logic;

/**
 * AddOrderToOrderListResultMessage is a message from the server to the client
 * contains an instance of boolean.
 * 
 * @author Shahar Yarden
 *
 */
@SuppressWarnings("serial")
public class DenyApprovalResultMessage extends Message<Boolean> {
	/**
	 * Save the boolean as the data of the message
	 * 
	 * @param data data
	 */
	public DenyApprovalResultMessage(Boolean data) {
		super(data);
	}
}