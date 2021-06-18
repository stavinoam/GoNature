package logic;

/**
 * UpdateSettingsResultMessage is a message from the server to the client
 * contains an instance of a Boolean.
 * 
 * @author Yarden Shahar
 *
 */
@SuppressWarnings("serial")
public class UpdateSettingsResultMessage extends Message<Boolean> {
	/**
	 * Save the flag as the data of the message
	 * 
	 * @param flag data
	 */
	public UpdateSettingsResultMessage(Boolean flag) {
		super(flag);
	}
}
