package logic;

/**
 * InstructorSearchResultMessage is a message from the server to the client
 * contains an instance of instructor.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class InstructorSearchResultMessage extends Message<Instructor> {
	/**
	 * Save the instructor as the data of the message
	 * 
	 * @param data data
	 */
	public InstructorSearchResultMessage(Instructor data) {
		super(data);
	}
}
