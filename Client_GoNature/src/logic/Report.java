package logic;

import com.jfoenix.controls.JFXButton;

import javafx.beans.property.SimpleStringProperty;

/**
 * Report class illustrate the columns of report table
 * 
 * @author Shahar Yarden
 *
 */
public class Report {
	private SimpleStringProperty parkName;
	private SimpleStringProperty reportName;
	private SimpleStringProperty month;
	private SimpleStringProperty year;
	private JFXButton view;

	/**
	 * Constructor
	 * 
	 * @param parkName   Park name
	 * @param reportName Report table
	 * @param month      Report month
	 * @param year       Report year
	 * @param view       button
	 */
	public Report(String parkName, String reportName, String month, String year, JFXButton view) {
		this.parkName = new SimpleStringProperty(parkName);
		this.reportName = new SimpleStringProperty(reportName);
		this.month = new SimpleStringProperty(month);
		this.year = new SimpleStringProperty(year);
		this.view = view;
	}

	/**
	 * Getter Park Name
	 * 
	 * @return Park Name
	 */
	public String getParkName() {
		return parkName.get();
	}

	/**
	 * Setter Park Name
	 * 
	 * @param parkName Park Name
	 */
	public void setparkName(String parkName) {
		this.parkName = new SimpleStringProperty(parkName);
	}

	/**
	 * Getter Report Name
	 * 
	 * @return Report Name
	 */
	public String getReportName() {
		return reportName.get();
	}

	/**
	 * Setter Report Name
	 * 
	 * @param reportName Report Name
	 */
	public void setReportName(String reportName) {
		this.reportName = new SimpleStringProperty(reportName);
	}

	/**
	 * Getter Month
	 * 
	 * @return Report Month
	 */
	public String getMonth() {
		return month.get();
	}

	/**
	 * Setter Month
	 * 
	 * @param month Report Month
	 */
	public void setMonth(String month) {
		this.month = new SimpleStringProperty(month);
	}

	/**
	 * Getter Year
	 * 
	 * @return Report Year
	 */
	public String getYear() {
		return year.get();
	}

	/**
	 * Setter Report Year
	 * 
	 * @param year Report Year
	 */
	public void setYear(String year) {
		this.year = new SimpleStringProperty(year);
	}

	/**
	 * Getter Button
	 * 
	 * @return JFX Button
	 */
	public JFXButton getView() {
		return view;
	}

	/**
	 * Setter Button
	 * 
	 * @param view Button
	 */
	public void setView(JFXButton view) {
		this.view = view;
	}

}
