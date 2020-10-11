package me.border.texteditor.ui.controller;

import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.input.Clipboard;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import me.border.texteditor.FileLoader;
import me.border.utilities.ui.ConfirmBox;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

public class EditorController {

    @FXML
    public TextArea textArea;

    private AtomicReference<String> oldText = new AtomicReference<>();
    private AtomicReference<File> file = new AtomicReference<>();
    private AtomicBoolean exists = new AtomicBoolean(false);
    public AtomicBoolean saved = new AtomicBoolean(true);

    @FXML
    public void close(){
        Stage window = (Stage) textArea.getScene().getWindow();
        if (!saved.get()){
            ConfirmBox confirmBox = new ConfirmBox("TextEditor", "Do you want to save changes to " + window.getTitle().replace("*", ""));
            boolean save =  confirmBox.show();
            if (save){
                save();
            }
        }

        window.close();
    }

    @FXML
    public void save(){
        Stage window = (Stage) textArea.getScene().getWindow();
        if (!exists.get()) {
            FileChooser fileChooser = new FileChooser();
            File chosenFile = fileChooser.showOpenDialog(window);
            if (chosenFile == null || !chosenFile.exists())
                return;
            file.set(chosenFile);
            exists.set(true);
        }

        try {
            FileLoader.save(file.get(), textArea.getText());
        } catch (IOException ex){
            ex.printStackTrace();
        }

        String oldTitle = window.getTitle();
        if (oldTitle.startsWith("*")){
            window.setTitle(oldTitle.substring(1));
        }
        saved.set(true);
    }

    @FXML
    public void ctrlcheck(KeyEvent e){
        if (e.getCode() == KeyCode.S && e.isControlDown()){
            save();
        } else if (e.getCode() == KeyCode.O && e.isControlDown()){
            open();
        }
    }

    @FXML
    public void paste(){
        textArea.paste();
    }

    @FXML
    public void copy(){
        textArea.copy();
    }

    @FXML
    public void cut(){
        textArea.cut();
    }

    @FXML
    public void delete(){
        textArea.replaceSelection("");
    }

    @FXML
    public void undo(){
        textArea.undo();
    }

    @FXML
    public void open(){
        Stage window = (Stage) textArea.getScene().getWindow();
        FileChooser fileChooser = new FileChooser();
        File chosenFile = fileChooser.showOpenDialog(window);
        if (chosenFile == null || !chosenFile.exists())
            return;
        file.set(chosenFile);
        exists.set(true);
        window.setTitle(chosenFile.getName());
        try {
            textArea.setText(FileLoader.loadFile(chosenFile));
        } catch (IOException ex){
            ex.printStackTrace();
        }

        FileLoader.cache(file.get());
    }

    @FXML
    public void openSpecific(File file){
        Stage window = (Stage) textArea.getScene().getWindow();
        this.file.set(file);
        exists.set(true);
        window.setTitle(file.getName());
        try {
            textArea.setText(FileLoader.loadFile(file));
        } catch (IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    public void update(){
        Stage window = (Stage) textArea.getScene().getWindow();
        if (textArea.getText().equals(oldText.get()))
            return;
        saved.set(false);
        oldText.set(textArea.getText());
        String oldTitle = window.getTitle();
        if (!oldTitle.startsWith("*")){
            window.setTitle("*" + oldTitle);
        }
    }
}
