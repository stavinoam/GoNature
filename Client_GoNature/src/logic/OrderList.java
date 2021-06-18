package logic;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.ToggleGroup;

/**
 * OrderList class illustrate the columns of the all orders that the traveler
 * booked
 * 
 * @author Stav Anna
 *
 */
public class OrderList {
	private SimpleStringProperty clmnOrderNumber;
	private SimpleStringProperty clmnParkName;
	private SimpleStringProperty clmnVisitorsNumber;
	private SimpleStringProperty clmnDate;
	private SimpleStringProperty clmnStatus;
	private ToggleGroup clmnAction;

	private ToggleGroup clmnAction2;
	private SimpleStringProperty clmnTime;

	/**
	 * Constructor
	 * 
	 * @param clmnOrderNumber    Order number
	 * @param clmnParkName       Park name
	 * @param clmnDate           Order date
	 * @param clmnTime           Order time
	 * @param clmnVisitorsNumber Visitors number
	 * @param clmnStatus         Order status
	 * @param clmnAction         button
	 */
	public OrderList(String clmnOrderNumber, String clmnParkName, String clmnDate, String clmnTime,
			String clmnVisitorsNumber, String clmnStatus, ToggleGroup clmnAction) {
		this.clmnOrderNumber = new SimpleStringProperty(clmnOrderNumber);
		this.clmnParkName = new SimpleStringProperty(clmnParkName);
		this.clmnVisitorsNumber = new SimpleStringProperty(clmnVisitorsNumber);
		this.clmnDate = new SimpleStringProperty(clmnDate);
		this.clmnTime = new SimpleStringProperty(clmnTime);
		this.clmnStatus = new SimpleStringProperty(clmnStatus);
		this.clmnAction = clmnAction;
	}

	/**
	 * Constructor
	 * 
	 * @param clmnParkName       Park name
	 * @param clmnDate           Order date
	 * @param clmnTime           Order time
	 * @param clmnVisitorsNumber Visitors number
	 * @param clmnAction         button
	 */
	public OrderList(String clmnParkName, String clmnDate, String clmnTime, String clmnVisitorsNumber,
			ToggleGroup clmnAction) {
		this.clmnParkName = new SimpleStringProperty(clmnParkName);
		this.clmnTime = new SimpleStringProperty(clmnTime);
		this.clmnVisitorsNumber = new SimpleStringProperty(clmnVisitorsNumber);
		this.clmnDate = new SimpleStringProperty(clmnDate);
		this.clmnAction2 = clmnAction;
	}

	/**
	 * getter Order number
	 * 
	 * @return Order number
	 */
	public String getOrderNumber() {
		return clmnOrderNumber.get();
	}

	/**
	 * setter Order number
	 * 
	 * @param clmnOrderNumber Order number
	 */
	public void setOrderNumber(String clmnOrderNumber) {
		this.clmnOrderNumber = new SimpleStringProperty(clmnOrderNumber);
	}

	/**
	 * getter Park Name
	 * 
	 * @return Park Name
	 */
	public String getParkName() {
		return clmnParkName.get();
	}

	/**
	 * setter Park Name
	 * 
	 * @param clmnParkName Park Name
	 */
	public void setParkName(String clmnParkName) {
		this.clmnParkName = new SimpleStringProperty(clmnParkName);
	}

	/**
	 * getter Visitors Number
	 * 
	 * @return Visitors Number
	 */
	public String getVisitorsNumber() {
		return clmnVisitorsNumber.get();
	}

	/**
	 * setter Visitors Number
	 * 
	 * @param clmnVisitorsNumber Visitors Number
	 */
	public void setVisitorsNumber(String clmnVisitorsNumber) {
		this.clmnVisitorsNumber = new SimpleStringProperty(clmnVisitorsNumber);
	}

	/**
	 * getter Order date
	 * 
	 * @return Order date
	 */
	public String getDate() {
		return clmnDate.get();
	}

	/**
	 * setter Order date
	 * 
	 * @param clmnDate Order date
	 */
	public void setDate(String clmnDate) {
		this.clmnDate = new SimpleStringProperty(clmnDate);
	}

	/**
	 * getter order status
	 * 
	 * @return order status
	 */
	public String getStatus() {
		return clmnStatus.get();
	}

	/**
	 * setter order status
	 * 
	 * @param clmnStatus order status
	 */
	public void setStatus(String clmnStatus) {
		this.clmnStatus = new SimpleStringProperty(clmnStatus);
	}

	/**
	 * getter Button action1
	 * 
	 * @return Button action1
	 */
	public ToggleGroup getAction() {
		return clmnAction;
	}

	public void setAction(ToggleGroup clmnAction) {
		this.clmnAction = clmnAction;
	}


	public ToggleGroup getAction2() {
		return clmnAction2;
	}


	public void setAction2(ToggleGroup clmnAction) {
		this.clmnAction2 = clmnAction;
	}

	/**
	 * getter order time
	 * 
	 * @return order time
	 */
	public String getTime() {
		return clmnTime.get();
	}

	/**
	 * setter order time
	 * 
	 * @param clmnTime order time
	 */
	public void setTime(String clmnTime) {
		this.clmnTime = new SimpleStringProperty(clmnTime);
	}

}
