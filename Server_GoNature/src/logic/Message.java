package logic;

import java.io.Serializable;

@SuppressWarnings("serial")
public abstract class Message<T extends Object> implements Serializable {
	private T data;

	/**
	 * Constructor
	 * 
	 * @param data data of the message
	 */
	public Message(T data) {
		this.data = data;
	}

	public T getData() {
		return data;
	}

	public void setData(T data) {
		this.data = data;
	}
}
