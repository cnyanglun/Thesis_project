import javafx.application.Application;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.tinylog.Logger;


public class gameApplication extends Application {
    @Override
    public void start(Stage stage) throws Exception {
        Logger.info("Applicant Start......");
        Parent root = FXMLLoader.load(getClass().getResource("/JavaFx/ServerView.fxml"));
        Scene scene = new Scene(root);
        stage.setScene(scene);

        stage.setTitle("Server");
        stage.show();
    }

}
