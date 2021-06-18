package logic;

import java.io.Serializable;

/**
 * The Worker class contains the data of the worker
 * 
 * @author Stav Anna
 * 
 */
@SuppressWarnings("serial")
public class Traveler implements Serializable {
	private String fName;
	private String lName;
	private String idNumber;
	private String email;
	private String type;
	private boolean isNew;
	private String phone;

	/**
	 * Getter last Name
	 * 
	 * @return traveler last name
	 */
	public String getlName() {
		return lName;
	}

	/**
	 * Getter id number
	 * 
	 * @return traveler id number
	 */
	public String getIdNumber() {
		return idNumber;
	}

	/**
	 * Getter traveler email
	 * 
	 * @return traveler email
	 */
	public String getEmail() {
		return email;
	}

	/**
	 * setter - traveler first name
	 * 
	 * @param fName first name
	 */
	public void setfName(String fName) {
		this.fName = fName;
	}

	/**
	 * setter - traveler last name
	 * 
	 * @param lName traveler last name
	 */
	public void setlName(String lName) {
		this.lName = lName;
	}

	/**
	 * setter - traveler id number
	 * 
	 * @param idNumber traveler id number
	 */
	public void setIdNumber(String idNumber) {
		this.idNumber = idNumber;
	}

	/**
	 * setter - traveler email
	 * 
	 * @param email traveler email
	 */
	public void setEmail(String email) {
		this.email = email;
	}

	/**
	 * Getter traveler first name
	 * 
	 * @return traveler first name
	 */
	public String getfName() {
		return fName;
	}

	/**
	 * toString method
	 */
	@Override
	public String toString() {
		return "Traveler [fName=" + fName + ", lName=" + lName + ", idNumber=" + idNumber + ", email=" + email
				+ ", type=" + type + ", phone=" + phone + "]";
	}

	/**
	 * Getter traveler type
	 * 
	 * @return traveler type - "regular" / "subscriber" / "instructor"
	 */
	public String getType() {
		return type;
	}

	/**
	 * setter - traveler type
	 * 
	 * @param type traveler type
	 */
	public void setType(String type) {
		this.type = type;
	}

	/**
	 * @return if the traveler is new
	 */
	public boolean isNew() {
		return isNew;
	}

	/**
	 * setter - if the traveler is new
	 * 
	 * @param isNew boolean is New
	 */
	public void setNew(boolean isNew) {
		this.isNew = isNew;
	}

	/**
	 * Getter traveler phone
	 * 
	 * @return traveler phone
	 */
	public String getPhone() {
		return phone;
	}

	/**
	 * setter - traveler phone
	 * 
	 * @param phone traveler phone
	 */
	public void setPhone(String phone) {
		this.phone = phone;
	}

	/**
	 * hashCode method - return the hashCode of the object
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((fName == null) ? 0 : fName.hashCode());
		result = prime * result + ((idNumber == null) ? 0 : idNumber.hashCode());
		result = prime * result + (isNew ? 1231 : 1237);
		result = prime * result + ((lName == null) ? 0 : lName.hashCode());
		result = prime * result + ((phone == null) ? 0 : phone.hashCode());
		result = prime * result + ((type == null) ? 0 : type.hashCode());
		return result;
	}

	/**
	 * equals method - returns true if the objects are equals
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Traveler other = (Traveler) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (fName == null) {
			if (other.fName != null)
				return false;
		} else if (!fName.equals(other.fName))
			return false;
		if (idNumber == null) {
			if (other.idNumber != null)
				return false;
		} else if (!idNumber.equals(other.idNumber))
			return false;
		if (isNew != other.isNew)
			return false;
		if (lName == null) {
			if (other.lName != null)
				return false;
		} else if (!lName.equals(other.lName))
			return false;
		if (phone == null) {
			if (other.phone != null)
				return false;
		} else if (!phone.equals(other.phone))
			return false;
		if (type == null) {
			if (other.type != null)
				return false;
		} else if (!type.equals(other.type))
			return false;
		return true;
	}

}
