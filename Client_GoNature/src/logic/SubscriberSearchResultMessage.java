package logic;

/**
 * SubscriberSearchResultMessage is a message from the server to the client
 * contains an instance of subscriber.
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class SubscriberSearchResultMessage extends Message<Subscriber> {

	/**
	 * Save the subscriber as the data of the message
	 * 
	 * @param subscriber subscriber
	 */
	public SubscriberSearchResultMessage(Subscriber subscriber) {
		super(subscriber);
	}
}