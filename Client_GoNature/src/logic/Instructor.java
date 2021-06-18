package logic;

/**
 * Instructor class is an object
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class Instructor extends Traveler {
	private String phone;

	/**
	 * Getter instructor phone
	 *
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * Setter instructor phone
	 *
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * To string function
	 */
	@Override
	public String toString() {
		return super.toString() + "Instructor [phone=" + phone + "]";
	}
}
