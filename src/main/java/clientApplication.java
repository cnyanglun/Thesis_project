import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tinylog.Logger;

public class clientApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Logger.info("Starting......");
        Parent root = FXMLLoader.load(getClass().getResource("/JavaFx/loginView.fxml"));
        stage.setTitle("Yang lun");
        stage.setScene(new Scene(root));
        stage.show();
    }
}
