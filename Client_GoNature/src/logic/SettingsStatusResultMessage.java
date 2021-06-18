package logic;

/**
 * SettingsStatusResultMessage is a message from the server to the client
 * contains an instance of string.
 * 
 * @author Shahar Yarden
 *
 */
@SuppressWarnings("serial")
public class SettingsStatusResultMessage extends Message<String> {
	/**
	 * Save the string as the data of the message
	 * 
	 * @param data data
	 */
	public SettingsStatusResultMessage(String data) {
		super(data);
	}
}
