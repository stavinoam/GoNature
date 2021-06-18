package logic;

/**
 * ParkSettingsMessage is a message from the client to the server it contains an
 * instance of string.
 * 
 * @author Shahar Yarden
 *
 */
@SuppressWarnings("serial")
public class ParkSettingsMessage extends Message<String> {
	/**
	 * Save the string as the data of the message
	 * 
	 * @param data data
	 */
	public ParkSettingsMessage(String data) {
		super(data);
	}
}
