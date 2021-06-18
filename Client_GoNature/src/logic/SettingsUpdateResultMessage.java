package logic;

/**
 * SettingsUpdateResultMessage is a message from the server to the client
 * contains an instance of boolean.
 * 
 * @author Shahar Yarden
 *
 */
@SuppressWarnings("serial")
public class SettingsUpdateResultMessage extends Message<Boolean> {
	/**
	 * Save the boolean as the data of the message
	 * 
	 * @param data data
	 */
	public SettingsUpdateResultMessage(Boolean data) {
		super(data);
	}
}
