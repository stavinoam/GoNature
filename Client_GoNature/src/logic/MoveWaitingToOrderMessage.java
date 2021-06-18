package logic;

/**
 * MoveWaitingToOrderMessage is a message from the client to the server it
 * contains an instance of order.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class MoveWaitingToOrderMessage extends Message<Order> {

	/**
	 * Save the order as the data of the message
	 * 
	 * @param data data
	 */
	public MoveWaitingToOrderMessage(Order data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

}
