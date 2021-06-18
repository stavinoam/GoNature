package logic;

/**
 * DeleteSettingsMessage is a message from the client to the server it contains
 * an instance of Sting [].
 * 
 * @author Shahar Yarden
 *
 */
@SuppressWarnings("serial")
public class DeleteSettingsMessage extends Message<String[]> {
	/**
	 * Save the String [] as the data of the message
	 * 
	 * @param data array of strings of the data
	 */
	public DeleteSettingsMessage(String[] data) {
		super(data);
	}
}