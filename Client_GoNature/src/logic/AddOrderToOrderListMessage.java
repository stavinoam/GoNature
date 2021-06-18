package logic;

/**
 * AddOrderToOrderListMessage is a message from the client to the server it
 * contains an instance of order.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class AddOrderToOrderListMessage extends Message<Order> {

	/**
	 * Save the order as the data of the message
	 * 
	 * @param order order that is the data
	 */
	public AddOrderToOrderListMessage(Order order) {
		super(order);
	}

}
