package photos;
/**
 * @author ktt43
 */
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import view.loginController;

public class Photos extends Application {

	@Override
	public void start(Stage primaryStage) throws Exception {
		FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/login.fxml"));
		Parent root = (Parent)loader.load();		
	
		Scene scene = new Scene(root);
		primaryStage.setScene(scene);
		primaryStage.setTitle("Photo Library");
		primaryStage.setResizable(false);  
		primaryStage.show();
		
	loginController loginController = loader.getController();
	loginController.start(primaryStage);
		
	}
	
	public static void main(String[] args) {
		launch(args);
	}

}
