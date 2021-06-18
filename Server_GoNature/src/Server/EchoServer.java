package Server;

import java.io.IOException;
import java.util.HashSet;
import gui.ServerPortFrameController;
import logic.AddOrderToOrderListResultMessage;
import logic.AddOrderToWaitingListMessage;
import logic.AddOrderToWaitingListResultMessage;
import logic.AddTravelerToTravelersMessage;
import logic.CancelOrderMessage;
import logic.CancelOrderResultMessage;
import logic.CardReaderMessage;
import logic.CardReaderResultMessage;
import logic.CheckIfOrderSucceededMessage;
import logic.CheckIfOrderSucceededResultMessage;
import logic.ClientAlreadyConnectedResultMessage;
import logic.ConfirmOrderMessage;
import logic.ConfirmOrderResultMessage;
import logic.ConfirmationMessage;
import logic.ConfirmationResultMessage;
import logic.CurrentNumbersMessage;
import logic.CurrentNumbersResultMessage;
import logic.DenyApprovalMessage;
import logic.DenyApprovalResultMessage;
import logic.DepartmentReportMessage;
import logic.DepartmentReportResultMessage;
import logic.EditOrderMessage;
import logic.EditOrderResultMessage;
import logic.GetParkDiscountMessage;
import logic.GetParkDiscountResultMessage;
import logic.GetReportsMessage;
import logic.GetReportsResultMessage;
import logic.InsertReportsTableMessage;
import logic.InsertReportsTableResultMessage;
import logic.Instructor;
import logic.InstructorRegistrationMessage;
import logic.InstructorRegistrationResultMessage;
import logic.InstructorSearchMessage;
import logic.InstructorSearchResultMessage;
import logic.LogOutMessage;
import logic.MoveWaitingToOrderMessage;
import logic.MoveWaitingToOrderResultMessage;
import logic.OrderListMessage;
import logic.OrderListResultMessage;
import logic.ParkManagerReportMessage;
import logic.ParkManagerReportResultMessage;
import logic.ParkSettingsMessage;
import logic.ParkSettingsResultMessage;
import logic.PaymentMessage;
import logic.PaymentResultMessage;
import logic.RemoveOrderFromWaitingListMessage;
import logic.RemoveOrderFromWaitingListResultMessage;
import logic.SearchOrderMessage;
import logic.SearchOrderResultMessage;
import logic.SettingsStatusMessage;
import logic.SettingsStatusResultMessage;
import logic.SettingsUpdateMessage;
import logic.SettingsUpdateResultMessage;
import logic.Subscriber;
import logic.SubscriberRegistrationMessage;
import logic.SubscriberRegistrationResultMessage;
import logic.SubscriberSearchMessage;
import logic.SubscriberSearchResultMessage;
import logic.Traveler;
import logic.TravelerSearchMessage;
import logic.TravelerSearchResultMessage;
import logic.UpdateSettingsMessage;
import logic.UpdateSettingsResultMessage;
import logic.VisitorsReportMessage;
import logic.VisitorsReportResultMessage;
import logic.WaitingListMessage;
import logic.WaitingListResultMessage;
import logic.Worker;
import logic.WorkerSearchMessage;
import logic.WorkerSearchResultMessage;
import ocsf.server.AbstractServer;
import ocsf.server.ConnectionToClient;

/**
 * This class overrides some of the methods in the abstract superclass in order
 * to give more functionality to the server.
 * 
 * @author Stav Anna
 *
 */
public class EchoServer extends AbstractServer {

	/**
	 * serverPortFrameController instance of ServerPortFrameController
	 */
	private static ServerPortFrameController serverPortFrameController;
	/**
	 * by id or userName
	 */
	private static HashSet<String> connectedUsers = new HashSet<>();

	/**
	 * The default port to listen on.
	 */
	public static int PORT;

	/**
	 * Constructs an instance of the echo server.
	 * 
	 * @param port The port number to connect on.
	 */
	public EchoServer(int port) {
		super(port);
		EchoServer.PORT = port;
	}

	/**
	 * setServerPortFrameController function set the instance
	 * 
	 * @param s type of ServerPortFrameController
	 */
	public static void setServerPortFrameController(ServerPortFrameController s) {
		serverPortFrameController = s;
	}

	/**
	 * The function Belongs to OCSF (AbstractServer) Operated when client is
	 * connected
	 */
	@Override
	protected void clientConnected(ConnectionToClient client) {
		String ip = client.getInetAddress().getHostAddress();
		String host_name = client.getInetAddress().getHostName();
		serverPortFrameController.setConnected(ip, host_name);
		Thread t = new Thread(new Runnable() {
			@Override
			public void run() {
				while (client.isAlive()) {
					try {
						client.join();
					} catch (InterruptedException e) {
					}
				}
				clientDisconnected(client);
			}
		});
		t.start();
	}

	/**
	 * The function set disconnected
	 */
	@Override
	synchronized protected void clientDisconnected(ConnectionToClient client) {
		serverPortFrameController.setDisconnected();
	}

	/**
	 * This method handles any messages received from the client.
	 */
	// msg The message received from the client.
	// client The connection from which the message originated.
	public void handleMessageFromClient(Object msg, ConnectionToClient client) {
		String str;
		String[] s;
		boolean flag;
		Worker worker;
		Traveler traveler;
		Subscriber subscriber;
		Instructor instructor;
		if (msg instanceof LogOutMessage) {
			LogOutMessage logOutMessage = (LogOutMessage) msg;
			String idOrUserName = logOutMessage.getData();
			connectedUsers.remove(idOrUserName);
			System.out.println(idOrUserName + " is now disconnected");
		} else if (msg instanceof ParkSettingsMessage) {
			ParkSettingsMessage parkSettingsMessage = (ParkSettingsMessage) msg;
			str = MysqlConnection.getInstance().getParkSettings(parkSettingsMessage.getData());
			try {
				client.sendToClient(new ParkSettingsResultMessage(str));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msg instanceof SettingsStatusMessage) {
			SettingsStatusMessage settingsStatusMessage = (SettingsStatusMessage) msg;
			str = MysqlConnection.getInstance().getSettingsStatus(settingsStatusMessage.getData());
			try {
				client.sendToClient(new SettingsStatusResultMessage(str));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msg instanceof SettingsUpdateMessage) {
			SettingsUpdateMessage settingsUpdateMessage = (SettingsUpdateMessage) msg;
			flag = MysqlConnection.getInstance().checkIfSettingChanged(settingsUpdateMessage.getData());
			try {
				client.sendToClient(new SettingsUpdateResultMessage(flag));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msg instanceof ConfirmationMessage) {
			str = MysqlConnection.getInstance().getConfirmationStatus();
			try {
				client.sendToClient(new ConfirmationResultMessage(str));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msg instanceof UpdateSettingsMessage) {
			UpdateSettingsMessage updateSettingsMessage = (UpdateSettingsMessage) msg;
			flag = MysqlConnection.getInstance().updateParkSettings(updateSettingsMessage.getData());
			try {
				client.sendToClient(new UpdateSettingsResultMessage(flag));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msg instanceof DenyApprovalMessage) {
			DenyApprovalMessage denyApprovalMessage = (DenyApprovalMessage) msg;
			flag = MysqlConnection.getInstance().denyApproval(denyApprovalMessage.getData());
			try {
				client.sendToClient(new DenyApprovalResultMessage(flag));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msg instanceof SubscriberRegistrationMessage) {
			SubscriberRegistrationMessage subscriberRegistrationMessage = (SubscriberRegistrationMessage) msg;
			str = MysqlConnection.getInstance().SubscriberRegistration(subscriberRegistrationMessage.getData());
			try {
				client.sendToClient(new SubscriberRegistrationResultMessage(str));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msg instanceof WorkerSearchMessage) {
			WorkerSearchMessage workerSearchMessage = (WorkerSearchMessage) msg;
			String userName = workerSearchMessage.getData()[0];
			try {
				if (connectedUsers.contains(userName)) {
					client.sendToClient(new ClientAlreadyConnectedResultMessage(userName));
				} else {
					connectedUsers.add(userName);// ********************************
					worker = MysqlConnection.getInstance().getWorker(workerSearchMessage.getData());
					client.sendToClient(new WorkerSearchResultMessage(worker));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (msg instanceof TravelerSearchMessage) {
			String subNum = null;
			TravelerSearchMessage travelerSearchMessage = (TravelerSearchMessage) msg;
			String travelerId = travelerSearchMessage.getData();
			traveler = MysqlConnection.getInstance().getTravelerById(travelerId);
			if (traveler != null && traveler.getType().equals("subscriber"))
				subNum = ((Subscriber) traveler).getSubscriberNumber();
			try {
				if (subNum != null) {
					if (connectedUsers.contains(travelerId) || connectedUsers.contains(subNum)) 
						client.sendToClient(new ClientAlreadyConnectedResultMessage(travelerId));
					else {
						connectedUsers.add(travelerId);
						connectedUsers.add(subNum);
						client.sendToClient(new TravelerSearchResultMessage(traveler));
					}
				} else {
					if (connectedUsers.contains(travelerId)) 
					client.sendToClient(new ClientAlreadyConnectedResultMessage(travelerId));
					else {
						connectedUsers.add(travelerId);
						client.sendToClient(new TravelerSearchResultMessage(traveler));
					}
				}
					
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (msg instanceof SubscriberSearchMessage) {
			SubscriberSearchMessage subscriberSearchMessage = (SubscriberSearchMessage) msg;
			String subNum = subscriberSearchMessage.getData();
			subscriber = MysqlConnection.getInstance().getTravelerBySubNum(subscriberSearchMessage.getData());
			try {
				if (connectedUsers.contains(subNum) || connectedUsers.contains(subscriber.getIdNumber())) {
					client.sendToClient(new ClientAlreadyConnectedResultMessage(subNum));
				} else {
					connectedUsers.add(subNum);
					connectedUsers.add(subscriber.getIdNumber());
					client.sendToClient(new SubscriberSearchResultMessage(subscriber));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (msg instanceof InstructorRegistrationMessage) {
			InstructorRegistrationMessage instructorRegistrationMessage = (InstructorRegistrationMessage) msg;
			str = MysqlConnection.getInstance().InstructorRegistration(instructorRegistrationMessage.getData());
			try {
				client.sendToClient(new InstructorRegistrationResultMessage(str));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msg instanceof PaymentMessage) {
			PaymentMessage paymentMessage = (PaymentMessage) msg;
			flag = MysqlConnection.getInstance().insertPaymentDetails(paymentMessage.getData());
			try {
				client.sendToClient(new PaymentResultMessage(flag));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msg instanceof CardReaderMessage) {
			CardReaderMessage cardReaderMessage = (CardReaderMessage) msg;
			str = MysqlConnection.getInstance().currentVisitorsUpdate(cardReaderMessage.getData());
			try {
				client.sendToClient(new CardReaderResultMessage(str));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msg instanceof SearchOrderMessage) {
			SearchOrderMessage searchOrderMessage = (SearchOrderMessage) msg;
			str = MysqlConnection.getInstance().getNumOfVisitorsInOrder(searchOrderMessage.getData());
			try {
				client.sendToClient(new SearchOrderResultMessage(str));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msg instanceof CurrentNumbersMessage) {
			CurrentNumbersMessage currentNumbersMessage = (CurrentNumbersMessage) msg;
			str = MysqlConnection.getInstance().getCurrentNumberOfVisitors(currentNumbersMessage.getData());
			try {
				client.sendToClient(new CurrentNumbersResultMessage(str));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msg instanceof CheckIfOrderSucceededMessage) {
			CheckIfOrderSucceededMessage checkIfOrderSucceededMessage = (CheckIfOrderSucceededMessage) msg;
			str = MysqlConnection.getInstance().addOrderIfSlotIsOK(checkIfOrderSucceededMessage.getData());
			try {
				client.sendToClient(new CheckIfOrderSucceededResultMessage(str));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (msg instanceof AddTravelerToTravelersMessage) {
			AddTravelerToTravelersMessage addTravelerToTravelersMessage = (AddTravelerToTravelersMessage) msg;
			str = MysqlConnection.getInstance().addTravelerToTravelersList(addTravelerToTravelersMessage.getData());
			try {
				client.sendToClient(new AddOrderToOrderListResultMessage(str));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (msg instanceof InstructorSearchMessage) {
			InstructorSearchMessage instructorSearchMessage = (InstructorSearchMessage) msg;
			String instructorId = instructorSearchMessage.getData();
			try {
				if (connectedUsers.contains(instructorId)) {
					client.sendToClient(new ClientAlreadyConnectedResultMessage(instructorId));
				} else {
					connectedUsers.add(instructorId);
					instructor = MysqlConnection.getInstance().getInstructor(instructorSearchMessage.getData());
					client.sendToClient(new InstructorSearchResultMessage(instructor));
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (msg instanceof EditOrderMessage) {
			EditOrderMessage editOrderMessage = (EditOrderMessage) msg;
			str = MysqlConnection.getInstance().editVisitorsInOrder(editOrderMessage.getData());
			try {
				client.sendToClient(new EditOrderResultMessage(str));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msg instanceof OrderListMessage) {
			OrderListMessage orderListMessage = (OrderListMessage) msg;
			str = MysqlConnection.getInstance().getOrderList(orderListMessage.getData());
			try {
				client.sendToClient(new OrderListResultMessage(str));
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else if (msg instanceof DepartmentReportMessage) {
			DepartmentReportMessage departmentReportMessage = (DepartmentReportMessage) msg;
			s = MysqlConnection.getInstance().departmentProduceAreport(departmentReportMessage.getData());
			try {
				client.sendToClient(new DepartmentReportResultMessage(s));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msg instanceof ParkManagerReportMessage) {
			ParkManagerReportMessage parkManagerReportMessage = (ParkManagerReportMessage) msg;
			s = MysqlConnection.getInstance().parkManagerProduceAreport(parkManagerReportMessage.getData());
			try {
				client.sendToClient(new ParkManagerReportResultMessage(s));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msg instanceof InsertReportsTableMessage) {
			InsertReportsTableMessage insertReportsTableMessage = (InsertReportsTableMessage) msg;
			flag = MysqlConnection.getInstance().insertReport(insertReportsTableMessage.getData());
			try {
				client.sendToClient(new InsertReportsTableResultMessage(flag));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msg instanceof GetReportsMessage) {
			GetReportsMessage getReportsMessage = (GetReportsMessage) msg;
			str = MysqlConnection.getInstance().getReports(getReportsMessage.getData());
			try {
				client.sendToClient(new GetReportsResultMessage(str));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msg instanceof AddOrderToWaitingListMessage) {
			AddOrderToWaitingListMessage addOrderToWaitingListMessage = (AddOrderToWaitingListMessage) msg;
			str = MysqlConnection.getInstance().addOrderToWaitingList(addOrderToWaitingListMessage.getData());
			try {
				client.sendToClient(new AddOrderToWaitingListResultMessage(str));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msg instanceof WaitingListMessage) {
			WaitingListMessage waitingListMessage = (WaitingListMessage) msg;
			str = MysqlConnection.getInstance().travelerWaitingList(waitingListMessage.getData());
			try {
				client.sendToClient(new WaitingListResultMessage(str));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msg instanceof VisitorsReportMessage) {
			VisitorsReportMessage visitorsReportMessage = (VisitorsReportMessage) msg;
			s = MysqlConnection.getInstance().reportSpecificDay(visitorsReportMessage.getData());
			try {
				client.sendToClient(new VisitorsReportResultMessage(s));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msg instanceof RemoveOrderFromWaitingListMessage) {
			RemoveOrderFromWaitingListMessage removeOrderFromWaitingListMessage = (RemoveOrderFromWaitingListMessage) msg;
			flag = MysqlConnection.getInstance()
					.removeOrderFromWaitingList(removeOrderFromWaitingListMessage.getData());
			try {
				client.sendToClient(new RemoveOrderFromWaitingListResultMessage(flag));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msg instanceof MoveWaitingToOrderMessage) {
			MoveWaitingToOrderMessage moveWaitingToOrderMessage = (MoveWaitingToOrderMessage) msg;
			str = MysqlConnection.getInstance().moveOrderFromWaiting(moveWaitingToOrderMessage.getData());
			try {
				client.sendToClient(new MoveWaitingToOrderResultMessage(str));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msg instanceof ConfirmOrderMessage) {
			ConfirmOrderMessage confirmOrderMessage = (ConfirmOrderMessage) msg;
			flag = MysqlConnection.getInstance().confirmOrder(confirmOrderMessage.getData());
			try {
				client.sendToClient(new ConfirmOrderResultMessage(flag));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msg instanceof CancelOrderMessage) {
			CancelOrderMessage cancelOrderMessage = (CancelOrderMessage) msg;
			flag = MysqlConnection.getInstance().cancelOrder(cancelOrderMessage.getData());
			try {
				client.sendToClient(new CancelOrderResultMessage(flag));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else if (msg instanceof GetParkDiscountMessage) {
			GetParkDiscountMessage getParkDiscountMessage = (GetParkDiscountMessage) msg;
			str = MysqlConnection.getInstance().getParkDiscount(getParkDiscountMessage.getData());
			try {
				client.sendToClient(new GetParkDiscountResultMessage(str));
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method overrides the one in the superclass. Called when the server
	 * starts listening for connections.
	 */
	protected void serverStarted() {
		System.out.println("Server listening for connections on port " + getPort());
	}

	/**
	 * This method overrides the one in the superclass. Called when the server stops
	 * listening for connections.
	 */
	protected void serverStopped() {
		System.out.println("Server has stopped listening for connections.");
	}

}