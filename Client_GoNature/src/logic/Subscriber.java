package logic;

/**
 * Subscriber class is an object that contains the data of the subscriber
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class Subscriber extends Traveler {
	private String phone;
	private int visitorsNumber;
	private String subscriberNumber;

	/**
	 * Getter Subscriber Number
	 * 
	 * @return Subscriber Number
	 */
	public String getSubscriberNumber() {
		return subscriberNumber;
	}

	/**
	 * Setter subscriber number
	 * 
	 * @param subscriberNumber subscriber number
	 */
	public void setSubscriberNumber(String subscriberNumber) {
		this.subscriberNumber = subscriberNumber;
	}

	/**
	 * Getter Subscriber phone
	 *
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Setter Subscriber phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * Getter Visitors Number
	 * 
	 * @return Visitors Number
	 */
	public int getVisitorsNumber() {
		return visitorsNumber;
	}

	/**
	 * Setter Visitors Number
	 * 
	 * @param visitorsNumber Visitors Number
	 */
	public void setVisitorsNumber(int visitorsNumber) {
		this.visitorsNumber = visitorsNumber;
	}

	/**
	 * toString function
	 */
	@Override
	public String toString() {
		return super.toString() + "Subscriber [phone=" + phone + ", visitorsNumber=" + visitorsNumber + "]";
	}

}
