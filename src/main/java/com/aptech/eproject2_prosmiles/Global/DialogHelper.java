package com.aptech.eproject2_prosmiles.Global;

import com.aptech.eproject2_prosmiles.Controller.ConfirmNotificationController;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class DialogHelper {
    public static boolean showConfirmationDialog(String title, String message) {
        boolean isConfirmed = false;
        FXMLLoader loader = new FXMLLoader(DialogHelper.class
                .getResource("/com/aptech/eproject2_prosmiles/View/Notification/ConfirmNotification.fxml"));
        try {
            Parent root = loader.load();

            Stage dialogStage = new Stage();
            dialogStage.setTitle(title);
            Scene scene = new Scene(root);

            scene.getStylesheets().add(Objects.requireNonNull(DialogHelper.class
                    .getResource("/com/aptech/eproject2_prosmiles/Style/Style.css")).toExternalForm());
            dialogStage.setScene(scene);

            ConfirmNotificationController controller = loader.getController();
            controller.setDialogStage(dialogStage);
            controller.setMessage(message);

            dialogStage.initModality(Modality.WINDOW_MODAL);
            dialogStage.showAndWait();

            isConfirmed = controller.getIsConfirmed();

        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return isConfirmed;
    }
}
