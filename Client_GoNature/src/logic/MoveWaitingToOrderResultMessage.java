package logic;

/**
 * MoveWaitingToOrderResultMessage is a message from the server to the client
 * contains an instance of string
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class MoveWaitingToOrderResultMessage extends Message<String> {

	/**
	 * Save the string as the data of the message
	 * 
	 * @param data data
	 */
	public MoveWaitingToOrderResultMessage(String data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

}
