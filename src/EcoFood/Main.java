package EcoFood;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Optional;


public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader loader = new  FXMLLoader(getClass().getResource("EcoFoodWindow.fxml"));
        Parent root = loader.load();
        EcoFoodController controller = loader.getController();
        Scene scene = new Scene(root);

        //scene.getStylesheets().add("foodrecipestyle.css");
        primaryStage.setTitle("Eco Food");
        primaryStage.getIcons().add(new Image(Main.class.getResourceAsStream("icon.png")));
        primaryStage.setOnCloseRequest(e -> {
            e.consume();
//            int result = ConformationDialogue.display();
//            if (result == 1)
//                primaryStage.close();
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initStyle(StageStyle.UNDECORATED);
//            alert.setTitle(null);
            alert.setHeaderText(null);
            alert.setContentText("Close the app?");

            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK)
                primaryStage.close();
        });

        primaryStage.setScene(scene);
        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
