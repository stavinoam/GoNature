package logic;

/**
 * TravelerSearchMessage is a message from the client to the server it contains
 * an instance of a Strings.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class TravelerSearchMessage extends Message<String> {

	/**
	 * Save the travelerId as the data of the message
	 * 
	 * @param travelerId travelerId
	 */
	public TravelerSearchMessage(String travelerId) {
		super(travelerId);
	}
}
