package logic;

/**
 * AddOrderToWaitingListMessage is a message from the client to the server it
 * contains an instance of order.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class AddOrderToWaitingListMessage extends Message<Order> {

	/**
	 * Save the order as the data of the message
	 * 
	 * @param data order that is the data
	 */
	public AddOrderToWaitingListMessage(Order data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

}
