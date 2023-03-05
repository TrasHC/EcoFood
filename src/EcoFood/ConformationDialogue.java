package EcoFood;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class ConformationDialogue {

    public static int result = -1;

    public static int display(){
        Stage window = new Stage();
        window.initModality(Modality.APPLICATION_MODAL);
        window.initStyle(StageStyle.UNDECORATED);

        Label label = new Label("Really close?");
        Button buttonYes = new Button("Yes");
        Button buttonNo = new Button("No");
        VBox vbox = new VBox(10);
        vbox.setAlignment(Pos.CENTER);
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(buttonYes, buttonNo);
        vbox.getChildren().addAll(label, hbox);

        buttonNo.setOnAction(e -> {
            result = 0;
            window.close();
        });
        buttonYes.setOnAction(e -> {
            result = 1;
            window.close();
        });

        Scene scene = new Scene(vbox);
        window.setScene(scene);
        window.showAndWait();

        return result;
    }
}
