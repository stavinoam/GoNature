package logic;

import java.io.Serializable;
import java.time.LocalDate;

/**
 * Order class is an object that contains the data of an order
 * 
 * @author Stav Anna
 *
 */
@SuppressWarnings("serial")
public class Order implements Serializable {

	private String parkName;
	private LocalDate date;
	private String timeSlot;
	private String email;
	private boolean isInstructor;
	private int numberOfVisitors;
	private String type;
	private double price;
	private String identification;
	private Traveler traveler;

	public Order() {
	}

	/**
	 * Constructor
	 * 
	 * @param other    is a Park Name
	 * @param newDate  order date
	 * @param timeSlot order time
	 */
	public Order(Order other, LocalDate newDate, String timeSlot) {
		this.parkName = other.getParkName();
		this.date = newDate;
		this.timeSlot = timeSlot;
		this.numberOfVisitors = other.getNumberOfVisitors();
	}

	public String getParkName() {
		return parkName;
	}

	public LocalDate getDate() {
		return date;
	}

	public void setDate(LocalDate date) {
		this.date = date;
	}

	public String getTimeSlot() {
		return timeSlot;
	}

	public void setTimeSlot(String timeSlot) {
		this.timeSlot = timeSlot;
	}

	public void setParkName(String parkName) {
		this.parkName = parkName;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public boolean isInstructor() {
		return isInstructor;
	}

	public void setInstructor(boolean isInstructor) {
		this.isInstructor = isInstructor;
	}

	public int getNumberOfVisitors() {
		return numberOfVisitors;
	}

	public void setNumberOfVisitors(int numberOfVisitors) {
		this.numberOfVisitors = numberOfVisitors;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public String getIdentification() {
		return identification;
	}

	public void setIdentification(String identification) {
		this.identification = identification;
	}

	public Traveler getTraveler() {
		return traveler;
	}

	public void setTraveler(Traveler traveler) {
		this.traveler = traveler;
	}

	@Override
	public String toString() {
		return "Order [parkName=" + parkName + ", date=" + date + ", timeSlot=" + timeSlot + ", email=" + email
				+ ", isInstructor=" + isInstructor + ", numberOfVisitors=" + numberOfVisitors + ", type=" + type
				+ ", price=" + price + ", identification=" + identification + "]";
	}
}
