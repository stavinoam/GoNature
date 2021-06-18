package logic;

/**
 * TravelerSearchResultMessage is a message from the server to the client
 * contains an instance of a Traveler object.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class TravelerSearchResultMessage extends Message<Traveler> {

	/**
	 * Save the traveler object as the data of the message
	 * 
	 * @param traveler traveler
	 */
	public TravelerSearchResultMessage(Traveler traveler) {
		super(traveler);
	}
}