package logic;

/**
 * AddTravelerToTravelersMessage is a message from the client to the server it
 * contains an instance of traveler.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class AddTravelerToTravelersMessage extends Message<Traveler> {

	/**
	 * Save the traveler as the data of the message
	 * 
	 * @param data traveler that is the data
	 */
	public AddTravelerToTravelersMessage(Traveler data) {
		super(data);
		// TODO Auto-generated constructor stub
	}

}
