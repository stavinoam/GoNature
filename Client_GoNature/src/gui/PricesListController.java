package gui;

import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;

import client.ChatClient;
import client.ClientUI;
import javafx.application.Platform;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import logic.GetParkDiscountMessage;
import logic.OnResponseListener;

/**
 * PricesListController is a controller of PricesList.fxml FXML. The controller
 * implements all the functionals of PricesList.fxml
 * 
 * @author Stav Anna
 *
 */
public class PricesListController implements Initializable {
	/**
	 * primaryStage is the stage of the last page that led to this one
	 */
	static Stage primaryStage;
	@FXML
	private Label pricesListTitle;

	@FXML
	private Label base50Nis;

	@FXML
	private GridPane gridPane;

	@FXML
	private JFXComboBox<String> parkCombo;

	@FXML
	private TextField parkDiscountField;

	@FXML
	private JFXButton btnBack;

	@FXML
	private JFXHamburger menu;

	@FXML
	private AnchorPane drawerpane;

	@FXML
	private JFXDrawer drawer;

	/**
	 * Start method is the method that starts the stage with the match fxml file
	 * 
	 * @param stage the new stage that is created before calling the start method
	 * @throws Exception if the FXMLLoader can't load the fxml file
	 */
	public void start(Stage stage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/gui/PriceList.fxml"));
		loader.setController(this);
		Parent root = loader.load();
		Scene scene = new Scene(root);
		scene.getStylesheets().add(getClass().getResource("/gui/Style.css").toExternalForm());
		stage.setTitle("GoNature");
		stage.setScene(scene);
		stage.getIcons().add(new Image("gui/css/tree.png"));
		stage.show();
		parkDiscount();
	}

	/**
	 * The method respond to user click on "back" button The method leads the user
	 * to the last page that he used before entering "about us page"
	 * 
	 * @param event clicking on "back" button
	 */
	@FXML
	void back(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
		primaryStage.show();
	}

	/**
	 * the method changes the value of the parkDiscountField according to the park
	 * that the uses chooses
	 */
	private void parkDiscount() {
		parkCombo.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
			@Override
			public void changed(ObservableValue<? extends String> arg0, String arg1, String arg2) {
				if (arg2 != null) {
					ClientUI.chat.accept(new GetParkDiscountMessage(arg2), new OnResponseListener() {
						@Override
						public void onResponse(Object message) {
							if (ChatClient.str != null) {
								Platform.runLater(() -> {
									String discount = ChatClient.str;
									parkDiscountField.setText(discount + "%");
								});
							}
						}
					});
				}

			}
		});
	}

	/**
	 * initialize method is the first method to be run when the controller is starting
	 * the method initializes the parameters of the fxml page
	 */
	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {
		List<String> parkName = Arrays.asList("Hatzbani", "Banias", "Hai Park");
		parkCombo.setItems(FXCollections.observableArrayList(parkName));

		final Tooltip parkDiscount = new Tooltip();
		parkDiscount.setText("Please choose a park to see the park's discont");
		parkDiscountField.setTooltip(parkDiscount);
		parkCombo.setTooltip(parkDiscount);
	}

}
