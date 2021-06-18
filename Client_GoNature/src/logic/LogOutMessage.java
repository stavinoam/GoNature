package logic;

/**
 * LogOutMessage is a message from the client to the server it contains an
 * instance of string.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class LogOutMessage extends Message<String> {

	/**
	 * Save the string as the data of the message
	 * 
	 * @param id id
	 */
	public LogOutMessage(String id) {
		super(id);
		// TODO Auto-generated constructor stub
	}

}
