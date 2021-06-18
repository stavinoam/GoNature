package logic;

/**
 * SettingsUpdateMessage is a message from the client to the server it contains
 * an instance of String[].
 * 
 * @author Shahar Yarden
 *
 */
@SuppressWarnings("serial")
public class SettingsUpdateMessage extends Message<String[]> {
	/**
	 * Save the order as the data of the message
	 * 
	 * @param data data
	 */
	public SettingsUpdateMessage(String[] data) {
		super(data);
	}
}