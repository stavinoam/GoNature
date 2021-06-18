package logic;

/**
 * WaitingListMessage is a message from the server to the client contains an
 * instance of a Traveler object.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class WaitingListMessage extends Message<Traveler> {

	/**
	 * Save the traveler object as the data of the message
	 * 
	 * @param traveler the traveler data
	 */
	public WaitingListMessage(Traveler traveler) {
		super(traveler);
	}

}
