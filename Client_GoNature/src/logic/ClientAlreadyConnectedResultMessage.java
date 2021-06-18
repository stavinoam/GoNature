package logic;

/**
 * ClientAlreadyConnectedResultMessage is a message from the server to the
 * client contains an instance of string.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class ClientAlreadyConnectedResultMessage extends Message<String> {

	/**
	 * Save the string as the data of the message
	 * 
	 * @param id string of the data
	 */
	public ClientAlreadyConnectedResultMessage(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

}
