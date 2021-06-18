package logic;

/**
 * OtherOrderDatesMessage is a message from the client to the server it contains
 * an instance of order.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class OtherOrderDatesMessage extends Message<Order> {

	/**
	 * Save the order as the data of the message
	 * 
	 * @param order order
	 */
	public OtherOrderDatesMessage(Order order) {
		super(order);
	}

}
