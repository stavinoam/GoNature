package logic;

/**
 * OnResponseListener is an interface Every class that implements
 * OnResponseListener must implement onResponse method
 * 
 * @author Stav Anna
 *
 */
public interface OnResponseListener {
	/**
	 * The method waits for a response from the server.
	 * 
	 * @param message the message that was sent from the server.
	 */
	void onResponse(Object message);
}
