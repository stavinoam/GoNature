package logic;

/**
 * CheckIfOrderSucceededMessage is a message from the client to the server it
 * contains an instance of order.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class CheckIfOrderSucceededMessage extends Message<Order> {

	/**
	 * Save the order as the data of the message
	 * 
	 * @param order order of the data
	 */
	public CheckIfOrderSucceededMessage(Order order) {
		super(order);
	}

}
