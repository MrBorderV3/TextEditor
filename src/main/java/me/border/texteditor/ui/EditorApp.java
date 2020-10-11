package me.border.texteditor.ui;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import me.border.texteditor.FileLoader;
import me.border.texteditor.ui.controller.EditorController;
import me.border.utilities.ui.ConfirmBox;
import me.border.utilities.utils.URLUtils;

import java.io.IOException;

public class EditorApp extends Application {

    public void start(String[] args){
        launch(args);
    }

    @Override
    public void start(Stage stage) throws IOException {
        stage.setTitle("Untitled");
        stage.setResizable(false);
        FXMLLoader loader = new FXMLLoader(URLUtils.getURL("/ui/ui.fxml"));
        Parent root = loader.load();
        EditorController controller = loader.getController();
        stage.getIcons().add(new Image("/assets/icon.png"));
        stage.setScene(new Scene(root, 1280, 800));
        stage.show();


        if (FileLoader.cacheExists()){
            controller.openSpecific(FileLoader.openCache());
        }

        stage.setOnCloseRequest(e -> {
            if (!controller.saved.get()) {
                ConfirmBox confirmBox = new ConfirmBox("TextEditor", "Do you want to save changes to " + stage.getTitle().replace("*", ""));
                boolean save = confirmBox.show();
                if (save) {
                    controller.save();
                }
            }
        });
    }
}
