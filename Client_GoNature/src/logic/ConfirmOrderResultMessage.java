package logic;

/**
 * ConfirmOrderResultMessage is a message from the server to the client contains
 * an instance of boolean.
 * 
 * @author Shahar  Yarden
 *
 */
@SuppressWarnings("serial")
public class ConfirmOrderResultMessage extends Message<Boolean> {

	/**
	 * Save the boolean as the data of the message
	 * 
	 * @param data flag of the data
	 */
	public ConfirmOrderResultMessage(Boolean data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

}
