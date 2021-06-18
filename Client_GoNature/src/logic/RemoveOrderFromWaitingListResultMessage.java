package logic;

/**
 * RemoveOrderFromWaitingListResultMessage is a message from the server to the
 * client contains an instance of boolean
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class RemoveOrderFromWaitingListResultMessage extends Message<Boolean> {

	/**
	 * Save the boolean as the data of the message
	 * 
	 * @param data data
	 */
	public RemoveOrderFromWaitingListResultMessage(Boolean data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

}
