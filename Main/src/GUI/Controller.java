package GUI;

import java.net.URL;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXButton;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Paint;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import java.io.File;

public class Controller implements Initializable {

    @FXML
    private Pane input, general, impressions, click, server;

    @FXML
    private JFXButton btn_input, btn_general, btn_impressions, btn_click, btn_server;

    @FXML
    private Label text;

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if(event.getSource() == btn_input) {
            input.toFront();
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Open Resource File");
            fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("Log Files", "*.svg"));
            File selectedFile = fileChooser.showOpenDialog(Main.theStage);
            if (selectedFile != null) {
                //sendFileToBackend
            }
        } else if(event.getSource() == btn_general) {
            general.toFront();
        } else if(event.getSource() == btn_impressions) {
            impressions.toFront();
            text.setText("Entry 1: getVal\nEntry 2: getVal2\nEntry 3: getVal3");
            text.setTextFill(Paint.valueOf("#fff"));
        } else if(event.getSource() == btn_click) {
            click.toFront();
        } else if(event.getSource() == btn_server) {
            server.toFront();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle bnd) {
    }

}
