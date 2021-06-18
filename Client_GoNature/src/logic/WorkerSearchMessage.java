package logic;

/**
 * WorkerSearchMessage is a message from the client to the server it contains an
 * instance of an array of Strings.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class WorkerSearchMessage extends Message<String[]> {

	/**
	 * Save the array of Strings "workerDetatils" as the data of the message
	 * 
	 * @param workerDetatils workerDetatils
	 */
	public WorkerSearchMessage(String[] workerDetatils) {
		super(workerDetatils);
	}
}
