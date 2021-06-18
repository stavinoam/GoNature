package common;

/**
 * This interface implements the abstract method used to display objects onto
 * the client or server UIs.
 * 
 * @author Stav Anna
 *
 */
public interface ChatIF {

	/**
	 * Method that when overriden is used to display objects onto a UI.
	 * 
	 * @param message message that we want to display
	 */
	public abstract void display(String message);
}
