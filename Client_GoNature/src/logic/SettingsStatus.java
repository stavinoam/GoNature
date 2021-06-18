package logic;

import javafx.beans.property.SimpleStringProperty;

/**
 * SettingsStatus class illustrate the columns of settings status table
 * 
 * @author Shahr Yarden
 *
 */
public class SettingsStatus {
	private SimpleStringProperty clmnSettingChanged;
	private SimpleStringProperty clmnBefore;
	private SimpleStringProperty clmnAfter;
	private SimpleStringProperty clmnChangeTime;
	private SimpleStringProperty clmnStatus;
	private SimpleStringProperty clmnResponse;

	/**
	 * Constructoe
	 * 
	 * @param clmnSettingChanged The setting that was changed
	 * @param clmnBefore         The data before the change
	 * @param clmnAfter          The data after the change
	 * @param clmnChangeTime     Change time
	 * @param clmnStatus         Status
	 * @param clmnResponse       Button
	 */
	public SettingsStatus(String clmnSettingChanged, String clmnBefore, String clmnAfter, String clmnChangeTime,
			String clmnStatus, String clmnResponse) {
		this.clmnSettingChanged = new SimpleStringProperty(clmnSettingChanged);
		this.clmnBefore = new SimpleStringProperty(clmnBefore);
		this.clmnAfter = new SimpleStringProperty(clmnAfter);
		this.clmnChangeTime = new SimpleStringProperty(clmnChangeTime);
		this.clmnStatus = new SimpleStringProperty(clmnStatus);
		this.clmnResponse = new SimpleStringProperty(clmnResponse);
	}

	/**
	 * getter clmnSettingChanged
	 * 
	 * @return The setting that was changed
	 */
	public String getClmnSettingChanged() {
		return clmnSettingChanged.get();
	}

	/**
	 * setter clmnSettingChanged
	 * 
	 * @param clmnSettingChanged The setting that was changed
	 */
	public void setClmnSettingChanged(String clmnSettingChanged) {
		this.clmnSettingChanged = new SimpleStringProperty(clmnSettingChanged);
	}

	/**
	 * getter getClmnBefore
	 * 
	 * @return The data before the change
	 */
	public String getClmnBefore() {
		return clmnBefore.get();
	}

	/**
	 * setter getClmnBefore
	 * 
	 * @param clmnBefore The data before the change
	 */
	public void setClmnBefore(String clmnBefore) {
		this.clmnBefore = new SimpleStringProperty(clmnBefore);
	}

	/**
	 * getter getClmnAfter
	 * 
	 * @return The data after the change
	 */
	public String getClmnAfter() {
		return clmnAfter.get();
	}

	/**
	 * setter getClmnAfter
	 * 
	 * @param clmnAfter The data after the change
	 */
	public void setClmnAfter(String clmnAfter) {
		this.clmnAfter = new SimpleStringProperty(clmnAfter);
	}

	/**
	 * getter getClmnChangeTime
	 * 
	 * @return Change time
	 */
	public String getClmnChangeTime() {
		return clmnChangeTime.get();
	}

	/**
	 * setter getClmnChangeTime
	 * 
	 * @param clmnChangeTime Change time
	 */
	public void setClmnChangeTime(String clmnChangeTime) {
		this.clmnChangeTime = new SimpleStringProperty(clmnChangeTime);
	}

	/**
	 * getter setting status
	 * 
	 * @return setting status
	 */
	public String getClmnStatus() {
		return clmnStatus.get();
	}

	/**
	 * setter setting status
	 * 
	 * @param clmnStatus setting status
	 */
	public void setClmnStatus(String clmnStatus) {
		this.clmnStatus = new SimpleStringProperty(clmnStatus);
	}

	/**
	 * getter Button Response
	 * 
	 * @return Button Response
	 */
	public String getClmnResponse() {
		return clmnResponse.get();
	}

	/**
	 * setter Button Response
	 * 
	 * @param clmnResponse Button Response
	 */
	public void setClmnResponse(String clmnResponse) {
		this.clmnResponse = new SimpleStringProperty(clmnResponse);
	}

}
