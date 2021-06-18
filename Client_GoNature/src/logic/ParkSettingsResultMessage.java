package logic;

/**
 * ParkSettingsResultMessage is a message from the server to the client contains
 * an instance of string.
 * 
 * @author Shahr Yarden
 *
 */
@SuppressWarnings("serial")
public class ParkSettingsResultMessage extends Message<String> {
	/**
	 * Save the string as the data of the message
	 * 
	 * @param data data
	 */
	public ParkSettingsResultMessage(String data) {
		super(data);
	}
}
