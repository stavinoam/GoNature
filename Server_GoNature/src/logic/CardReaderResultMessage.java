package logic;

/**
 * CardReaderResultMessage is a message from the server to the client contains
 * an instance of string.
 * 
 * @author Shahar Yarden
 *
 */
@SuppressWarnings("serial")
public class CardReaderResultMessage extends Message<String> {
	/**
	 * Save the string as the data of the message
	 * 
	 * @param data data
	 */
	public CardReaderResultMessage(String data) {
		super(data);
	}
}
