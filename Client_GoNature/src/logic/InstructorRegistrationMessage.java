package logic;

/**
 * AddOrderToOrderListMessage is a message from the client to the server it
 * contains an instance of String[].
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class InstructorRegistrationMessage extends Message<String[]> {
	/**
	 * Save the String[] as the data of the message
	 * 
	 * @param data data
	 */
	public InstructorRegistrationMessage(String[] data) {
		super(data);
	}
}
