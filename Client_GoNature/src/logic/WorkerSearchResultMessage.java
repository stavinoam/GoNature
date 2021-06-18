package logic;

/**
 * WorkerSearchResultMessage is a message from the server to the client contains
 * an instance of a Worker object.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class WorkerSearchResultMessage extends Message<Worker> {

	/**
	 * Save the worker object as the data of the message
	 * 
	 * @param worker worker
	 */
	public WorkerSearchResultMessage(Worker worker) {
		super(worker);
	}
}