package logic;

/**
 * CancelOrderResultMessage is a message from the server to the client contains
 * an instance of Boolean.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class CancelOrderResultMessage extends Message<Boolean> {

	/**
	 * Save the data as the data of the message
	 * 
	 * @param data flag of the data
	 */
	public CancelOrderResultMessage(Boolean data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

}
