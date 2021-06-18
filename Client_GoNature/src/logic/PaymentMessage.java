package logic;

/**
 * PaymentMessage is a message from the client to the server it contains an
 * instance of String[].
 * 
 * @author Shahr Yarden
 *
 */
@SuppressWarnings("serial")
public class PaymentMessage extends Message<String[]> {
	/**
	 * Save the String[] as the data of the message
	 * 
	 * @param data data
	 */
	public PaymentMessage(String[] data) {
		super(data);
	}
}