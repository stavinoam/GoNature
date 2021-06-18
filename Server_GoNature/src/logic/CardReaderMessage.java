package logic;

/**
 * CardReaderMessage is a message from the client to the server it contains an
 * instance of String.
 * 
 * @author Shahar Yarden
 *
 */
@SuppressWarnings("serial")
public class CardReaderMessage extends Message<String> {
	/**
	 * Save the String as the data of the message
	 * 
	 * @param data data
	 */
	public CardReaderMessage(String data) {
		super(data);
	}
}
