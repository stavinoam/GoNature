package logic;

import java.io.Serializable;

/**
 * The Worker class contains the data of the worker
 * 
 * @author Stav Anna
 */
@SuppressWarnings("serial")
public class Worker implements Serializable {
	private String fName;
	private String lName;
	private String email;
	private String workerNumber;
	private String organization;
	private String userName;
	private String password;
	private String role;

	/**
	 * getter
	 * 
	 * @return worker first name
	 */
	public String getfName() {
		return fName;
	}

	/**
	 * getter
	 * 
	 * @return worker last name
	 */
	public String getlName() {
		return lName;
	}

	/**
	 * getter
	 * 
	 * @return worker email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * getter
	 * 
	 * @return worker number
	 */
	public String getWorkerNumber() {
		return workerNumber;
	}

	/**
	 * getter
	 * 
	 * @return worker organization
	 */
	public String getOrganization() {
		return organization;
	}

	/**
	 * getter
	 * 
	 * @return worker user name
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * getter
	 * 
	 * @return worker password
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * getter
	 * 
	 * @return worker role
	 */
	public String getRole() {
		return role;
	}

	/**
	 * setter - worker first name
	 * 
	 * @param fName First Name
	 */
	public void setfName(String fName) {
		this.fName = fName;
	}

	/**
	 * setter - worker last name
	 * 
	 * @param lName last Name
	 */
	public void setlName(String lName) {
		this.lName = lName;
	}

	/**
	 * setter - worker email
	 * 
	 * @param email worker email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * setter - worker number
	 * 
	 * @param workerNumber worker number
	 */
	public void setWorkerNumber(String workerNumber) {
		this.workerNumber = workerNumber;
	}

	/**
	 * setter - worker organization
	 * 
	 * @param organization worker organization
	 */
	public void setOrganization(String organization) {
		this.organization = organization;
	}

	/**
	 * setter - worker user name
	 * 
	 * @param userName worker user name
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * setter - worker password
	 * 
	 * @param password worker password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * setter - worker role
	 * 
	 * @param role role
	 */
	public void setRole(String role) {
		this.role = role;
	}

	/**
	 * toString method
	 */
	@Override
	public String toString() {
		return "fisrt name: =" + fName + ", last name: " + lName + ", email: " + email + ", worker number: "
				+ workerNumber + ", organization: " + organization + ", user name:" + userName + ", password: "
				+ password;
	}

}
