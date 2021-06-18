package logic;

/**
 * OrderListMessage is a message from the client to the server it contains an
 * instance of traveler.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class OrderListMessage extends Message<Traveler> {

	/**
	 * Save the traveler as the data of the message
	 * 
	 * @param data data
	 */
	public OrderListMessage(Traveler data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

}
