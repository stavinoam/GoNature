package logic;

import javafx.beans.property.SimpleStringProperty;
import javafx.scene.control.CheckBox;

/**
 * Confirmation class illustrate the columns of confirm Table
 * 
 * @author Shahar Yarden
 *
 */
public class Confirmation {
	private SimpleStringProperty clmnParkName;
	private SimpleStringProperty clmnSettingName;
	private SimpleStringProperty clmnBefore;
	private SimpleStringProperty clmnAfter;
	private SimpleStringProperty clmnRequest;
	private CheckBox clmnResponse;

	/**
	 * Constructor
	 * 
	 * @param clmnParkName    park name
	 * @param clmnSettingName setting name
	 * @param clmnBefore      setting data (before the change)
	 * @param clmnAfter       setting data (after the change)
	 * @param clmnRequest     Who request the change
	 * @param clmnResponse    CheckBox
	 */
	public Confirmation(String clmnParkName, String clmnSettingName, String clmnBefore, String clmnAfter,
			String clmnRequest, CheckBox clmnResponse) {
		this.clmnParkName = new SimpleStringProperty(clmnParkName);
		this.clmnSettingName = new SimpleStringProperty(clmnSettingName);
		this.clmnBefore = new SimpleStringProperty(clmnBefore);
		this.clmnAfter = new SimpleStringProperty(clmnAfter);
		this.clmnRequest = new SimpleStringProperty(clmnRequest);
		this.clmnResponse = clmnResponse;
	}

	public String getClmnParkName() {
		return clmnParkName.get();
	}

	public void setClmnParkName(String clmnParkName) {
		this.clmnParkName = new SimpleStringProperty(clmnParkName);
	}

	public String getClmnBefore() {
		return clmnBefore.get();
	}

	public void setClmnBefore(String clmnBefore) {
		this.clmnBefore = new SimpleStringProperty(clmnBefore);
	}

	public String getClmnAfter() {
		return clmnAfter.get();
	}

	public void setClmnAfter(String clmnAfter) {
		this.clmnAfter = new SimpleStringProperty(clmnAfter);
	}

	public String getClmnSettingName() {
		return clmnSettingName.get();
	}

	public void setClmnSettingName(String clmnSettingName) {
		this.clmnSettingName = new SimpleStringProperty(clmnSettingName);
	}

	public String getClmnRequest() {
		return clmnRequest.get();
	}

	public void setClmnRequest(String clmnRequest) {
		this.clmnRequest = new SimpleStringProperty(clmnRequest);
	}

	public CheckBox getClmnResponse() {
		return clmnResponse;
	}

	public void setClmnResponse(CheckBox clmnResponse) {
		this.clmnResponse = clmnResponse;
	}

}
