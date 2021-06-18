package logic;

/**
 * UpdateSettingsMessage is a message from the client to the server it contains
 * an instance of an array of Strings.
 * 
 * @author Yarden Shahar
 *
 */
@SuppressWarnings("serial")
public class UpdateSettingsMessage extends Message<String[]> {
	/**
	 * Save the array of Strings s as the data of the message
	 * 
	 * @param s string
	 */
	public UpdateSettingsMessage(String[] s) {
		super(s);
	}
}
