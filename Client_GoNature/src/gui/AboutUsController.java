package gui;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXDrawer;
import com.jfoenix.controls.JFXHamburger;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;


/**
 * AboutUsController is a controller of AboutUs.fxml FXML.
 * The controller implements all the functionals of AboutUs.fxml 
 * @author Stav  Anna
 *
 */
public class AboutUsController {
	/**
	 * primaryStage is the stage of the last page that led to this one
	 */
	static Stage primaryStage;

    @FXML
    private JFXButton btnBackToLast;
    
    @FXML
    private JFXHamburger menu;

    @FXML
    private AnchorPane drawerpane;

    @FXML
    private JFXDrawer drawer;
	
	/**
	 * Start method is the method that starts the stage with the match fxml file 
	 * @param stage the new stage that is created before calling the start method
	 * @throws Exception if the FXMLLoader can't load the fxml file
	 */
	public void start(Stage stage) throws Exception {
		Parent root = FXMLLoader.load(getClass().getResource("/gui/AboutUs.fxml"));
		root.getStylesheets().add(getClass().getResource("/gui/Style.css").toExternalForm());
		Scene scene = new Scene(root);
		stage.setTitle("GoNature");
		stage.setScene(scene);
		stage.getIcons().add(new Image("gui/css/tree.png"));
		stage.show();
	}

	/**
	 * The method respond to user click on  "back" button
	 * The method leads the user to the last page that he used before entering "about us page"
	 * @param event clicking on "back" button
	 */
	@FXML
	void backToTheLastPage(ActionEvent event) {
		((Node) event.getSource()).getScene().getWindow().hide();
		primaryStage.show();
	}

}
