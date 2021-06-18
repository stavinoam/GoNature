package logic;

/**
 * GetParkDiscountResultMessage is a message from the server to the client
 * contains an instance of string.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class GetParkDiscountResultMessage extends Message<String> {

	/**
	 * Save the string as the data of the message
	 * 
	 * @param discount discount
	 */
	public GetParkDiscountResultMessage(String discount) {
		super(discount);
		// TODO Auto-generated constructor stub
	}

}
