package logic;

/**
 * InstructorSearchMessage is a message from the client to the server it
 * contains an instance of string.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class InstructorSearchMessage extends Message<String> {
	/**
	 * Save the string as the data of the message
	 * 
	 * @param data data
	 */
	public InstructorSearchMessage(String data) {
		super(data);
	}
}
