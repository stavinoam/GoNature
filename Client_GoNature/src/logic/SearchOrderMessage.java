package logic;

/**
 * SearchOrderMessage is a message from the client to the server it contains an
 * instance of string
 * 
 * @author Satv Anna
 *
 */
@SuppressWarnings("serial")
public class SearchOrderMessage extends Message<String> {
	/**
	 * Save the string as the data of the message
	 * 
	 * @param data data
	 */
	public SearchOrderMessage(String data) {
		super(data);
	}
}
