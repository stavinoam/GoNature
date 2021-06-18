package client;

import java.io.IOException;

import common.ChatIF;
import logic.AddOrderToWaitingListResultMessage;
import logic.AddTravelerToTravelersResultMessage;
import logic.CancelOrderResultMessage;
import logic.CheckIfOrderSucceededResultMessage;
import logic.ClientAlreadyConnectedResultMessage;
import logic.ConfirmOrderResultMessage;
import logic.ConfirmationResultMessage;
import logic.CurrentNumbersResultMessage;
import logic.DenyApprovalResultMessage;
import logic.DepartmentReportResultMessage;
import logic.EditOrderResultMessage;
import logic.GetParkDiscountResultMessage;
import logic.GetReportsResultMessage;
import logic.InsertReportsTableResultMessage;
import logic.Instructor;
import logic.InstructorRegistrationResultMessage;
import logic.InstructorSearchResultMessage;
import logic.Message;
import logic.MoveWaitingToOrderResultMessage;
import logic.OnResponseListener;
import logic.OrderListResultMessage;
import logic.ParkManagerReportResultMessage;
import logic.ParkSettingsResultMessage;
import logic.PaymentResultMessage;
import logic.RemoveOrderFromWaitingListResultMessage;
import logic.SearchOrderResultMessage;
import logic.SettingsStatusResultMessage;
import logic.SettingsUpdateResultMessage;
import logic.Subscriber;
import logic.SubscriberRegistrationResultMessage;
import logic.SubscriberSearchResultMessage;
import logic.Traveler;
import logic.TravelerSearchResultMessage;
import logic.UpdateSettingsResultMessage;
import logic.VisitorsReportResultMessage;
import logic.WaitingListResultMessage;
import logic.Worker;
import logic.WorkerSearchResultMessage;
import ocsf.client.AbstractClient;

/**
 * This class overrides some of the methods defined in the abstract superclass
 * in order to give more functionality to the client.
 * 
 * @author Stav Anna
 */
public class ChatClient extends AbstractClient {
	/**
	 * The interface type variable. It allows the implementation of the display
	 * method in the client.
	 */
	ChatIF clientUI;
	/**
	 * flag is a data of the message that returned from the server
	 */
	public static Boolean flag;
	/**
	 * str is a data of the message that returned from the server
	 */
	public static String str;
	/**
	 * worker is a data of the message that returned from the server
	 */
	public static Worker worker;
	/**
	 * traveler is a data of the message that returned from the server
	 */
	public static Traveler traveler;
	/**
	 * subscriber is a data of the message that returned from the server
	 */
	public static Subscriber subscriber;
	/**
	 * instructor is a data of the message that returned from the server
	 */
	public static Instructor instructor;
	/**
	 * s is a data of the message that returned from the server
	 */
	public static String[] s;
	private OnResponseListener listener;

	/**
	 * Constructs an instance of the chat client.
	 * 
	 * @param host     The server to connect to.
	 * @param port     The port number to connect on.
	 * @param clientUI The interface type variable.
	 * @throws IOException any Exception `
	 */
	public ChatClient(String host, int port, ChatIF clientUI) throws IOException {
		super(host, port); // Call the superclass constructor
		this.clientUI = clientUI;
		openConnection();
	}

	/**
	 * This method handles all data that comes in from the server
	 */
	// msg The message from the server.
	public void handleMessageFromServer(Object msg) {
		if (msg instanceof ClientAlreadyConnectedResultMessage) {
			this.listener.onResponse(msg);
		} else if (msg instanceof ParkSettingsResultMessage) {
			str = ((ParkSettingsResultMessage) msg).getData();
			this.listener.onResponse(str);
		} else if (msg instanceof SettingsStatusResultMessage) {
			str = ((SettingsStatusResultMessage) msg).getData();
			this.listener.onResponse(str);
		} else if (msg instanceof SettingsUpdateResultMessage) {
			flag = ((SettingsUpdateResultMessage) msg).getData();
			this.listener.onResponse(flag);
		} else if (msg instanceof ConfirmationResultMessage) {
			str = ((ConfirmationResultMessage) msg).getData();
			this.listener.onResponse(str);
		} else if (msg instanceof UpdateSettingsResultMessage) {
			flag = ((UpdateSettingsResultMessage) msg).getData();
			this.listener.onResponse(flag);
		} else if (msg instanceof DenyApprovalResultMessage) {
			flag = ((DenyApprovalResultMessage) msg).getData();
			this.listener.onResponse(flag);
		} else if (msg instanceof SubscriberRegistrationResultMessage) {
			str = ((SubscriberRegistrationResultMessage) msg).getData();
			this.listener.onResponse(str);
		} else if (msg instanceof WorkerSearchResultMessage) {
			worker = ((WorkerSearchResultMessage) msg).getData();
			this.listener.onResponse(worker);
		} else if (msg instanceof TravelerSearchResultMessage) {
			traveler = ((TravelerSearchResultMessage) msg).getData();
			this.listener.onResponse(traveler);
		} else if (msg instanceof SubscriberSearchResultMessage) {
			subscriber = ((SubscriberSearchResultMessage) msg).getData();
			this.listener.onResponse(subscriber);
		} else if (msg instanceof InstructorRegistrationResultMessage) {
			str = ((InstructorRegistrationResultMessage) msg).getData();
			this.listener.onResponse(str);
		} else if (msg instanceof PaymentResultMessage) {
			flag = ((PaymentResultMessage) msg).getData();
			this.listener.onResponse(flag);
		} else if (msg instanceof SearchOrderResultMessage) {
			str = ((SearchOrderResultMessage) msg).getData();
			this.listener.onResponse(str);
		} else if (msg instanceof CurrentNumbersResultMessage) {
			str = ((CurrentNumbersResultMessage) msg).getData();
			this.listener.onResponse(str);
		} else if (msg instanceof CheckIfOrderSucceededResultMessage) {
			str = ((CheckIfOrderSucceededResultMessage) msg).getData();
			this.listener.onResponse(str);
		} else if (msg instanceof AddTravelerToTravelersResultMessage) {
			str = ((AddTravelerToTravelersResultMessage) msg).getData();
			this.listener.onResponse(str);
		} else if (msg instanceof InstructorSearchResultMessage) {
			instructor = ((InstructorSearchResultMessage) msg).getData();
			this.listener.onResponse(instructor);
		} else if (msg instanceof EditOrderResultMessage) {
			str = ((EditOrderResultMessage) msg).getData();
			this.listener.onResponse(str);
		} else if (msg instanceof OrderListResultMessage) {
			str = ((OrderListResultMessage) msg).getData();
			this.listener.onResponse(str);
		} else if (msg instanceof DepartmentReportResultMessage) {
			s = ((DepartmentReportResultMessage) msg).getData();
			this.listener.onResponse(s);
		} else if (msg instanceof ParkManagerReportResultMessage) {
			s = ((ParkManagerReportResultMessage) msg).getData();
			this.listener.onResponse(s);
		} else if (msg instanceof InsertReportsTableResultMessage) {
			flag = ((InsertReportsTableResultMessage) msg).getData();
			this.listener.onResponse(flag);
		} else if (msg instanceof GetReportsResultMessage) {
			str = ((GetReportsResultMessage) msg).getData();
			this.listener.onResponse(str);
		} else if (msg instanceof AddOrderToWaitingListResultMessage) {
			str = ((AddOrderToWaitingListResultMessage) msg).getData();
			this.listener.onResponse(str);
		} else if (msg instanceof WaitingListResultMessage) {
			str = ((WaitingListResultMessage) msg).getData();
			this.listener.onResponse(str);
		} else if (msg instanceof VisitorsReportResultMessage) {
			s = ((VisitorsReportResultMessage) msg).getData();
			this.listener.onResponse(s);
		} else if (msg instanceof RemoveOrderFromWaitingListResultMessage) {
			flag = ((RemoveOrderFromWaitingListResultMessage) msg).getData();
			this.listener.onResponse(flag);
		} else if (msg instanceof MoveWaitingToOrderResultMessage) {
			str = ((MoveWaitingToOrderResultMessage) msg).getData();
			this.listener.onResponse(str);
		} else if (msg instanceof ConfirmOrderResultMessage) {
			flag = ((ConfirmOrderResultMessage) msg).getData();
			this.listener.onResponse(flag);
		} else if (msg instanceof CancelOrderResultMessage) {
			flag = ((CancelOrderResultMessage) msg).getData();
			this.listener.onResponse(flag);
		} else if (msg instanceof GetParkDiscountResultMessage) {
			str = ((GetParkDiscountResultMessage) msg).getData();
			this.listener.onResponse(str);
		}
	}

	/**
	 * This method handles all data coming from the UI
	 * 
	 * @param message  The message from the UI.
	 * @param listener an object that waits for a response from the server
	 */
	public void handleMessageFromClientUI(Message message, OnResponseListener listener) {
		this.listener = listener;
		try {
			sendToServer(message);
		} catch (IOException e) {
			e.printStackTrace();
			clientUI.display("Could not send message to server.  Terminating client.");
			quit();
		}
	}

	/**
	 * This method terminates the client.
	 */
	public void quit() {
		try {
			closeConnection();
		} catch (IOException e) {
		}
		System.exit(0);
	}
}
