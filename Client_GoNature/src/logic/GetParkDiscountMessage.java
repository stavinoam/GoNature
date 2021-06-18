package logic;

/**
 * GetParkDiscountMessage is a message from the client to the server it contains
 * an instance of string.
 * 
 * @author StavAnna
 *
 */
@SuppressWarnings("serial")
public class GetParkDiscountMessage extends Message<String> {

	/**
	 * Save the order as the data of the message
	 * 
	 * @param park park
	 */
	public GetParkDiscountMessage(String park) {
		super(park);
		// TODO Auto-generated constructor stub
	}

}
