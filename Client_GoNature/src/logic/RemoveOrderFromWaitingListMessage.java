package logic;

/**
 * RemoveOrderFromWaitingListMessage is a message from the client to the server
 * it contains an instance of order.
 * 
 * @author Stva Anna
 *
 */
@SuppressWarnings("serial")
public class RemoveOrderFromWaitingListMessage extends Message<Order> {

	/**
	 * Save the order as the data of the message
	 * 
	 * @param data data
	 */
	public RemoveOrderFromWaitingListMessage(Order data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

}
