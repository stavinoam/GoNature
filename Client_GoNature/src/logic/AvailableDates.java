package logic;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.Button;

/**
 * AvailableDates class illustrate the columns of the available dates table
 * 
 * @author Stav Anna
 *
 */
public class AvailableDates {
	private SimpleStringProperty clmnDate;
	private SimpleStringProperty clmnTime;
	private Button clmnPick;

	/**
	 * Constructor
	 * 
	 * @param date   available date
	 * @param time   available time
	 * @param button book button
	 */
	public AvailableDates(String date, String time, Button button) {
		clmnDate = new SimpleStringProperty(date);
		clmnTime = new SimpleStringProperty(time);
		clmnPick = button;
	}

	/**
	 * Constructor
	 * 
	 * @param date the available date
	 * @param time the available time
	 */
	public AvailableDates(String date, String time) {
		clmnDate = new SimpleStringProperty(date);
		clmnTime = new SimpleStringProperty(time);
	}

	/**
	 * Getter function
	 * 
	 * @return a sting (date)
	 */
	public String getDate() {
		return clmnDate.get();
	}

	/**
	 * Setter function
	 * 
	 * @param date available date
	 */
	public void setDate(String date) {
		this.clmnDate = new SimpleStringProperty(date);
	}

	/**
	 * Getter function
	 * 
	 * @return a sting (time)
	 */
	public String getTime() {
		return clmnTime.get();
	}

	/**
	 * Setter function
	 * 
	 * @param time time
	 */
	public void setTime(String time) {
		this.clmnTime = new SimpleStringProperty(time);
	}

	/**
	 * Getter function
	 * 
	 * @return button button
	 */
	public Button getPick() {
		return clmnPick;
	}

	/**
	 * Setter function
	 * 
	 * @param button button
	 */
	public void setPick(Button button) {
		this.clmnPick = button;
	}

	/**
	 * To string function
	 */
	@Override
	public String toString() {
		return "AvailableDates [clmnDate=" + clmnDate + ", clmnTime=" + clmnTime + "]";
	}

}
