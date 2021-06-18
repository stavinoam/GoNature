package logic;

/**
 * SettingsStatusMessage is a message from the client to the server it contains
 * an instance of string.
 * 
 * @author Shahr Yraden
 *
 */
@SuppressWarnings("serial")
public class SettingsStatusMessage extends Message<String> {
	/**
	 * Save the string as the data of the message
	 * 
	 * @param data data
	 */
	public SettingsStatusMessage(String data) {
		super(data);
	}
}