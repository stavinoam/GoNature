package logic;

/**
 * PaymentResultMessageis a message from the server to the client contains an
 * instance of boolean.
 * 
 * @author Shahar Yarden
 *
 */
@SuppressWarnings("serial")
public class PaymentResultMessage extends Message<Boolean> {
	/**
	 * Save the boolean as the data of the message
	 * 
	 * @param data data
	 */
	public PaymentResultMessage(Boolean data) {
		super(data);
	}
}