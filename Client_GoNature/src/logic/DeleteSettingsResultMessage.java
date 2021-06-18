package logic;

/**
 * DeleteSettingsResultMessage is a message from the server to the client
 * contains an instance of boolean.
 * 
 * @author Shahar Yarden
 *
 */
@SuppressWarnings("serial")
public class DeleteSettingsResultMessage extends Message<Boolean> {
	/**
	 * Save the string as the data of the message
	 * 
	 * @param data flag of the data
	 */
	public DeleteSettingsResultMessage(Boolean data) {
		super(data);
	}
}