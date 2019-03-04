package Application;

import java.net.URL;
import java.util.ResourceBundle;

import Backend.Campaign;
import Backend.BounceType;
import GUI.Launcher;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.*;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Paint;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import javafx.scene.text.TextAlignment;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import java.awt.Dimension;
import java.awt.Font;
import javafx.collections.ObservableList;
import java.util.Optional;

import javax.swing.*;
import java.io.File;

public class Controller implements Initializable {

    private Handler handler;

    @FXML
    private Pane input, metrics, charts, compare, settings, filters;

    @FXML
    private JFXButton btn_plus, btn_input, btn_metrics, btn_charts, btn_compare, btn_filter, btn_settings, btn_filechooser_impression, btn_filechooser_click, btn_filechooser_server, btn_filechooser_load, btn_filechooser_reset, specify_bounce_1, specify_bounce_2;

    @FXML
    private FontAwesomeIcon icon_filter, icon_input, icon_metrics, icon_settings, icon_charts, icon_compare, logo_filechooser_impression, logo_filechooser_click, logo_filechooser_server;

    @FXML
    private Label text, text_filechooser_impression_top, text_filechooser_server_top, text_filechooser_click_top, text_filechooser_impression_bot, text_filechooser_server_bot, text_filechooser_click_bot;

    @FXML
    private Label metric_impression, metric_clicks, metric_uniques, metric_conversions, metric_ctr, metric_cpa, metric_cpc, metric_cpm, metric_bounce_rate, metric_bounces, metric_cost;

    @FXML
    private JFXComboBox combo_campaigns;

    @FXML
    private PieChart pie1, pie2;

    @FXML
    private BarChart chart1;

    @FXML
    private LineChart chart2;

    @FXML
    private void handleComboSelect(ActionEvent event) {
        setImpressionLoaded(false, null);
        setClickLoaded(false, null);
        setServerLoaded(false, null);
        input.toFront();
        if(event.getSource() == combo_campaigns) {
            String selectedName = combo_campaigns.getSelectionModel().getSelectedItem().toString();
            for(Campaign c : handler.getAllCampaigns()) {
                if(c.getName().equals(selectedName)) {
                    handler.setCurrentCampaign(c);
                    loadCampaign();
                }
            }
        }
    }

    private void loadCampaign() {
        Campaign c = handler.getCurrentCampaign();
        if(c.isImpressionLoaded()) {
            setImpressionLoaded(true, c.getImpressionFile());
        }  else {
            setImpressionLoaded(false, null);
        }
        if(c.isClickLoaded()) {
            setClickLoaded(true, c.getClickFile());
        } else {
            setClickLoaded(false, null);
        }
        if(c.isServerLoaded()) {
            setServerLoaded(true, c.getServerFile());
        } else {
            setServerLoaded(false, null);
        }
        setKeyMetrics();
        if(c.isImpressionLoaded() || c.isClickLoaded() || c.isServerLoaded()) {
            metrics.toFront();
        } else {
            input.toFront();
        }
        if(c.isBounceSet()) {
            specify_bounce_1.setText(null);
            specify_bounce_2.setText(null);
        } else {
            specify_bounce_1.setText("Specify Bounce");
            specify_bounce_2.setText("Specify Bounce");
        }
    }

    @FXML
    private void handleButtonAction(ActionEvent event) {
        if(event.getSource() == btn_input) {
            input.toFront();
        } else if(event.getSource() == btn_metrics) {
            metrics.toFront();
        } else if(event.getSource() == btn_charts) {
            charts.toFront();
        } else if(event.getSource() == btn_compare) {
            compare.toFront();
        } else if(event.getSource() == btn_settings) {
            settings.toFront();
        } else if(event.getSource() == btn_filter) {
            filters.toFront();
        } else if(event.getSource() == btn_plus) {
            String newName = askCampaignName(null);
            if(newName != null) {
                handler.addCampaign(newName);
                combo_campaigns.setPromptText(handler.getCurrentCampaign().getName());
                combo_campaigns.getItems().add(handler.getCurrentCampaign().getName());
                loadCampaign();
            }
        } else if(event.getSource() == specify_bounce_1 || event.getSource() == specify_bounce_2) {
            Dialog<Results> dialog = new Dialog<>();
            dialog.setTitle("Specify Bounce");
            dialog.setHeaderText("Please specify…");
            DialogPane dialogPane = dialog.getDialogPane();
            Label label1 = new Label("Bounce Type");
            Label label2 = new Label("Interval in minutes (Integers only)");
            Label label3 = new Label("Interval in seconds (Integers only)");
            TextField textField = new TextField("1");
            TextField textField2 = new TextField("0");
            ObservableList<BounceType> options =
                    FXCollections.observableArrayList(BounceType.values());
            ComboBox<BounceType> comboBox = new ComboBox<>(options);
            comboBox.getSelectionModel().selectFirst();
            dialogPane.setContent(new VBox(8, label1, comboBox, label2, textField, label3, textField2));
            Platform.runLater(textField::requestFocus);
            comboBox.getSelectionModel().selectedItemProperty().addListener( (theOptions, oldValue, newValue) -> {
                label2.setStyle("-fx-text-fill: black");
                label3.setStyle("-fx-text-fill: black");
                if(newValue == BounceType.Time) {
                    label2.setText("Interval in minutes (Integers only)");
                    dialogPane.setContent(new VBox(8, label1, comboBox, label2, textField, label3, textField2));
                    Platform.runLater(textField::requestFocus);
                } else {
                    label2.setText("Number of pages (Integers only)");
                    dialogPane.setContent(new VBox(8, label1, comboBox, label2, textField));
                    Platform.runLater(textField::requestFocus);
                }
            });

            dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);
            final Button btOk = (Button) dialog.getDialogPane().lookupButton(ButtonType.OK);
            btOk.addEventFilter(
                    ActionEvent.ACTION,
                    e -> {
                        label2.setStyle("-fx-text-fill: black");
                        label3.setStyle("-fx-text-fill: black");
                        if(comboBox.getSelectionModel().getSelectedItem().equals(BounceType.Time)) {
                            if (!(isInteger(textField.getText()) && isInteger(textField2.getText()))) {
                                if(!isInteger(textField.getText())) {
                                    label2.setStyle("-fx-text-fill: red");
                                }
                                if(!isInteger(textField2.getText())) {
                                    label3.setStyle("-fx-text-fill: red");
                                }
                                e.consume();
                            }
                        } else {
                            if(!isInteger(textField.getText())) {
                                label2.setStyle("-fx-text-fill: red");
                                e.consume();
                            }
                        }
                    }
            );

            dialog.setResultConverter((ButtonType button) -> {
                if (button == ButtonType.OK) {
                    if(comboBox.getSelectionModel().getSelectedItem().equals(BounceType.Time)) {
                        return new Results((Integer.parseInt(textField.getText()) * 60) + Integer.parseInt(textField2.getText()), BounceType.Time);
                    } else {
                        return new Results(Integer.parseInt(textField.getText()), BounceType.Pages);
                    }
                }
                return null;
            });
            Optional<Results> optionalResult = dialog.showAndWait();
            optionalResult.ifPresent((Results results) -> {
                specify_bounce_1.setText(null);
                specify_bounce_2.setText(null);
                handler.getCurrentCampaign().setBounceType(results.type);
                handler.getCurrentCampaign().setBounceInterval(results.integer);
                handler.getCurrentCampaign().loadBounceData();
                setKeyMetrics();
            });
        }
    }

    private static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch(Exception e) {
            return false;
        }
        return true;
    }

    @FXML
    private void handleFileChooserAction(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.getExtensionFilters().addAll(
                new ExtensionFilter("Log Files", "*.csv"));
        if(event.getSource() == btn_filechooser_impression) {
            fileChooser.setTitle("Choose Impression Log File");
            File selectedFile = fileChooser.showOpenDialog(Launcher.theStage);
            if(selectedFile != null) {
                setImpressionLoaded(true, selectedFile);
            }
        }
        if(event.getSource() == btn_filechooser_click) {
            fileChooser.setTitle("Choose Click Log File");
            File selectedFile = fileChooser.showOpenDialog(Launcher.theStage);
            if(selectedFile != null) {
                setClickLoaded(true, selectedFile);
            }
        }
        if(event.getSource() == btn_filechooser_server) {
            fileChooser.setTitle("Choose Server Log File");
            File selectedFile = fileChooser.showOpenDialog(Launcher.theStage);
            if(selectedFile != null) {
                setServerLoaded(true, selectedFile);
            }
        }
        if(event.getSource() == btn_filechooser_load) {
            if(handler.getCurrentCampaign().getImpressionFile() != null && handler.getCurrentCampaign().getClickFile() != null && handler.getCurrentCampaign().getServerFile() != null) {
                handler.calculateMetrics();
                handler.getCurrentCampaign().loadBounceData();
                setKeyMetrics();
            } else {
                UIManager.put("OptionPane.minimumSize",new Dimension(650,200));
                UIManager.put("OptionPane.messageFont", new Font("Arial", Font.PLAIN, 26));
                UIManager.put("OptionPane.buttonFont", new Font("Arial", Font.PLAIN, 28));
                if(handler.getCurrentCampaign().getImpressionFile() != null | handler.getCurrentCampaign().getClickFile() != null | handler.getCurrentCampaign().getServerFile() != null) {
                    int n = JOptionPane.showConfirmDialog(null,"Not selecting all required files results in omitted data. \nDo you wish to continue loading the files?","Warning: Not all files selected", JOptionPane.YES_NO_OPTION);
                    if(n == JOptionPane.YES_OPTION) {
                        handler.calculateMetrics();
                        handler.getCurrentCampaign().loadBounceData();
                        setKeyMetrics();
                    }
                } else {
                    JOptionPane.showMessageDialog(null, "Please select at least one file to load!", "Warning", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        if(event.getSource() == btn_filechooser_reset) {
            setImpressionLoaded(false, null);
            setClickLoaded(false, null);
            setServerLoaded(false, null);
        }
    }

    private void setImpressionLoaded(Boolean b, File f) {
        if(b == true) {
            btn_filechooser_impression.setStyle("-fx-background-color: #73e600;" + "-fx-border-color: #008000;" + "-fx-border-width: 0.5em");
            logo_filechooser_impression.setFill(Paint.valueOf("#008000"));
            text_filechooser_impression_top.setText("Impression Log Selected");
            text_filechooser_impression_top.setTextFill(Paint.valueOf("#008000"));
            text_filechooser_impression_bot.setText(f.getName());
            text_filechooser_impression_bot.setTextFill(Paint.valueOf("#008000"));
            handler.getCurrentCampaign().setImpressionFile(f);
        } else {
            btn_filechooser_impression.setStyle("-fx-background-color: #cccccc;" + "-fx-border-color: #7f7f7f;" + "-fx-border-width: 0.5em");
            logo_filechooser_impression.setFill(Paint.valueOf("#000000"));
            text_filechooser_impression_top.setText("Choose Impression Log");
            text_filechooser_impression_top.setTextFill(Paint.valueOf("#000000"));
            text_filechooser_impression_bot.setText(null);
        }
    }

    private void setClickLoaded(Boolean b, File f) {
        if(b == true) {
            btn_filechooser_click.setStyle("-fx-background-color: #73e600;" + "-fx-border-color: #008000;" + "-fx-border-width: 0.5em");
            logo_filechooser_click.setFill(Paint.valueOf("#008000"));
            text_filechooser_click_top.setText("Click Log Selected");
            text_filechooser_click_top.setTextFill(Paint.valueOf("#008000"));
            text_filechooser_click_bot.setText(f.getName());
            text_filechooser_click_bot.setTextFill(Paint.valueOf("#008000"));
            handler.getCurrentCampaign().setClickFile(f);
        } else {
            btn_filechooser_click.setStyle("-fx-background-color: #cccccc;" + "-fx-border-color: #7f7f7f;" + "-fx-border-width: 0.5em");
            logo_filechooser_click.setFill(Paint.valueOf("#000000"));
            text_filechooser_click_top.setText("Choose Click Log");
            text_filechooser_click_top.setTextFill(Paint.valueOf("#000000"));
            text_filechooser_click_bot.setText(null);
        }
    }

    private void setServerLoaded(Boolean b, File f) {
        if(b == true) {
            btn_filechooser_server.setStyle("-fx-background-color: #73e600;" + "-fx-border-color: #008000;" + "-fx-border-width: 0.5em");
            logo_filechooser_server.setFill(Paint.valueOf("#008000"));
            text_filechooser_server_top.setText("Server Log Selected");
            text_filechooser_server_top.setTextFill(Paint.valueOf("#008000"));
            text_filechooser_server_bot.setText(f.getName());
            text_filechooser_server_bot.setTextFill(Paint.valueOf("#008000"));
            handler.getCurrentCampaign().setServerFile(f);
        } else {
            btn_filechooser_server.setStyle("-fx-background-color: #cccccc;" + "-fx-border-color: #7f7f7f;" + "-fx-border-width: 0.5em");
            logo_filechooser_server.setFill(Paint.valueOf("#000000"));
            text_filechooser_server_top.setText("Choose Server Log");
            text_filechooser_server_top.setTextFill(Paint.valueOf("#000000"));
            text_filechooser_server_bot.setText(null);
        }
    }

    //NEED TO CHANGE FOR WHEN SEPERATE FILES CAN BE LOADED
    private void setKeyMetrics() {
        if(handler.getCurrentCampaign().isImpressionLoaded()) {
            metric_impression.setText("" + handler.getCurrentCampaign().getMetrics().getNoImpression());
            metric_impression.setStyle("-fx-font: 24 dengxian; -fx-font-weight: bold; -fx-background-color: #F0F0F0; -fx-border-color: #404040; -fx-border-width: 0.1em");
            metric_impression.setTextFill(Paint.valueOf("#000000"));
        } else {
            metric_impression.setText("Load Impression File");
            metric_impression.setStyle("-fx-font: 18 dengxian; -fx-font-weight: bold; -fx-background-color: #F0F0F0; -fx-border-color: #404040; -fx-border-width: 0.1em; -fx-opacity: 0.4");
            metric_impression.setTextFill(Paint.valueOf("#ff3232"));
        }
        if(handler.getCurrentCampaign().isClickLoaded()) {
            metric_clicks.setText("" + handler.getCurrentCampaign().getMetrics().getNoClicks());
            metric_clicks.setStyle("-fx-font: 24 dengxian; -fx-font-weight: bold; -fx-background-color: #F0F0F0; -fx-border-color: #404040; -fx-border-width: 0.1em");
            metric_clicks.setTextFill(Paint.valueOf("#000000"));
            metric_uniques.setText("" + handler.getCurrentCampaign().getMetrics().getNoUniques());
            metric_uniques.setStyle("-fx-font: 24 dengxian; -fx-font-weight: bold; -fx-background-color: #F0F0F0; -fx-border-color: #404040; -fx-border-width: 0.1em");
            metric_uniques.setTextFill(Paint.valueOf("#000000"));
        } else {
            metric_clicks.setText("Load Click File");
            metric_clicks.setStyle("-fx-font: 18 dengxian; -fx-font-weight: bold; -fx-background-color: #F0F0F0; -fx-border-color: #404040; -fx-border-width: 0.1em; -fx-opacity: 0.4");
            metric_clicks.setTextFill(Paint.valueOf("#ff3232"));
            metric_uniques.setText("Load Click File");
            metric_uniques.setStyle("-fx-font: 18 dengxian; -fx-font-weight: bold; -fx-background-color: #F0F0F0; -fx-border-color: #404040; -fx-border-width: 0.1em; -fx-opacity: 0.4");
            metric_uniques.setTextFill(Paint.valueOf("#ff3232"));
        }
        if (handler.getCurrentCampaign().isServerLoaded()) {
            metric_conversions.setText("" + handler.getCurrentCampaign().getMetrics().getNoConversions());
            metric_conversions.setStyle("-fx-font: 24 dengxian; -fx-font-weight: bold; -fx-background-color: #F0F0F0; -fx-border-color: #404040; -fx-border-width: 0.1em");
            metric_conversions.setTextFill(Paint.valueOf("#000000"));
            if(handler.getCurrentCampaign().isBounceSet()) {
                metric_bounces.setText("" + handler.getCurrentCampaign().getMetrics().getNoBounces());
                metric_bounces.setStyle("-fx-font: 24 dengxian; -fx-font-weight: bold; -fx-background-color: #F0F0F0; -fx-border-color: #404040; -fx-border-width: 0.1em");
                metric_bounces.setTextFill(Paint.valueOf("#000000"));
            } else {
                metric_bounces.setText(null);
            }
        } else {
            metric_conversions.setText("Load Server File");
            metric_conversions.setStyle("-fx-font: 18 dengxian; -fx-font-weight: bold; -fx-background-color: #F0F0F0; -fx-border-color: #404040; -fx-border-width: 0.1em; -fx-opacity: 0.4");
            metric_conversions.setTextFill(Paint.valueOf("#ff3232"));
            if(handler.getCurrentCampaign().isBounceSet()) {
                metric_bounces.setText("Load Server File");
                metric_bounces.setStyle("-fx-font: 18 dengxian; -fx-font-weight: bold; -fx-background-color: #F0F0F0; -fx-border-color: #404040; -fx-border-width: 0.1em; -fx-opacity: 0.4");
                metric_bounces.setTextFill(Paint.valueOf("#ff3232"));
            } else {
                metric_bounces.setText(null);
            }
        }
        if(handler.getCurrentCampaign().isImpressionLoaded() && handler.getCurrentCampaign().isClickLoaded()) {
            metric_ctr.setText("" + handler.getCurrentCampaign().getMetrics().getCTR());
            metric_ctr.setStyle("-fx-font: 24 dengxian; -fx-font-weight: bold; -fx-background-color: #F0F0F0; -fx-border-color: #404040; -fx-border-width: 0.1em");
            metric_ctr.setTextFill(Paint.valueOf("#000000"));
            metric_cpc.setText("" + handler.getCurrentCampaign().getMetrics().getCPC());
            metric_cpc.setStyle("-fx-font: 24 dengxian; -fx-font-weight: bold; -fx-background-color: #F0F0F0; -fx-border-color: #404040; -fx-border-width: 0.1em");
            metric_cpc.setTextFill(Paint.valueOf("#000000"));
            metric_cpm.setText("" + handler.getCurrentCampaign().getMetrics().getCPM());
            metric_cpm.setStyle("-fx-font: 24 dengxian; -fx-font-weight: bold; -fx-background-color: #F0F0F0; -fx-border-color: #404040; -fx-border-width: 0.1em");
            metric_cpm.setTextFill(Paint.valueOf("#000000"));
            metric_cost.setText("" + handler.getCurrentCampaign().getMetrics().getTotalCost());
            metric_cost.setStyle("-fx-font: 24 dengxian; -fx-font-weight: bold; -fx-background-color: #F0F0F0; -fx-border-color: #404040; -fx-border-width: 0.1em");
            metric_cost.setTextFill(Paint.valueOf("#000000"));
        } else {
            StringBuilder sb = new StringBuilder();
            if(!handler.getCurrentCampaign().isImpressionLoaded()) {
                sb.append("Load Impression File");
            }
            if(!handler.getCurrentCampaign().isClickLoaded()) {
                if(sb.toString().isEmpty()) {
                    sb.append("Load Click File");
                } else {
                    sb.append("\nLoad Click File");
                }
            }
            metric_ctr.setText(sb.toString());
            metric_ctr.setTextAlignment(TextAlignment.CENTER);
            metric_cpc.setText(sb.toString());
            metric_cpc.setTextAlignment(TextAlignment.CENTER);
            metric_cpm.setText(sb.toString());
            metric_cpm.setTextAlignment(TextAlignment.CENTER);
            metric_cost.setText(sb.toString());
            metric_cost.setTextAlignment(TextAlignment.CENTER);
            metric_ctr.setStyle("-fx-font: 18 dengxian; -fx-font-weight: bold; -fx-background-color: #F0F0F0; -fx-border-color: #404040; -fx-border-width: 0.1em; -fx-opacity: 0.4");
            metric_ctr.setTextFill(Paint.valueOf("#ff3232"));
            metric_cpc.setStyle("-fx-font: 18 dengxian; -fx-font-weight: bold; -fx-background-color: #F0F0F0; -fx-border-color: #404040; -fx-border-width: 0.1em; -fx-opacity: 0.4");
            metric_cpc.setTextFill(Paint.valueOf("#ff3232"));
            metric_cpm.setStyle("-fx-font: 18 dengxian; -fx-font-weight: bold; -fx-background-color: #F0F0F0; -fx-border-color: #404040; -fx-border-width: 0.1em; -fx-opacity: 0.4");
            metric_cpm.setTextFill(Paint.valueOf("#ff3232"));
            metric_cost.setStyle("-fx-font: 18 dengxian; -fx-font-weight: bold; -fx-background-color: #F0F0F0; -fx-border-color: #404040; -fx-border-width: 0.1em; -fx-opacity: 0.4");
            metric_cost.setTextFill(Paint.valueOf("#ff3232"));
        }
        if(handler.getCurrentCampaign().isImpressionLoaded() && handler.getCurrentCampaign().isClickLoaded() && handler.getCurrentCampaign().isServerLoaded()) {
            metric_cpa.setText("" + handler.getCurrentCampaign().getMetrics().getCPA());
            metric_cpa.setStyle("-fx-font: 24 dengxian; -fx-font-weight: bold; -fx-background-color: #F0F0F0; -fx-border-color: #404040; -fx-border-width: 0.1em");
            metric_cpa.setTextFill(Paint.valueOf("#000000"));
        } else {
            StringBuilder sb = new StringBuilder();
            if(!handler.getCurrentCampaign().isImpressionLoaded()) {
                sb.append("Load Impression File");
            }
            if(!handler.getCurrentCampaign().isClickLoaded()) {
                if(sb.toString().isEmpty()) {
                    sb.append("Load Click File");
                } else {
                    sb.append("\nLoad Click File");
                }
            }
            if(!handler.getCurrentCampaign().isServerLoaded()) {
                if(sb.toString().isEmpty()) {
                    sb.append("Load Server File");
                } else {
                    sb.append("\nLoad Server File");
                }
            }
            metric_cpa.setText(sb.toString());
            metric_cpa.setTextAlignment(TextAlignment.CENTER);
            metric_cpa.setStyle("-fx-font: 18 dengxian; -fx-font-weight: bold; -fx-background-color: #F0F0F0; -fx-border-color: #404040; -fx-border-width: 0.1em; -fx-opacity: 0.4");
            metric_cpa.setTextFill(Paint.valueOf("#ff3232"));
        }
        if(handler.getCurrentCampaign().isBounceSet()) {
            if (handler.getCurrentCampaign().isClickLoaded() && handler.getCurrentCampaign().isServerLoaded()) {
                metric_bounce_rate.setStyle("-fx-font: 24 dengxian; -fx-font-weight: bold; -fx-background-color: #F0F0F0; -fx-border-color: #404040; -fx-border-width: 0.1em");
                metric_bounce_rate.setTextFill(Paint.valueOf("#000000"));
                metric_bounce_rate.setText("" + handler.getCurrentCampaign().getMetrics().getBounceRate());
            } else {
                StringBuilder sb = new StringBuilder();
                if (!handler.getCurrentCampaign().isClickLoaded()) {
                    sb.append("Load Click File");
                }
                if (!handler.getCurrentCampaign().isServerLoaded()) {
                    if (sb.toString().isEmpty()) {
                        sb.append("Load Server File");
                    } else {
                        sb.append("\nLoad Server File");
                    }
                }
                metric_bounce_rate.setText(sb.toString());
                metric_bounce_rate.setTextAlignment(TextAlignment.CENTER);
                metric_bounce_rate.setStyle("-fx-font: 18 dengxian; -fx-font-weight: bold; -fx-background-color: #F0F0F0; -fx-border-color: #404040; -fx-border-width: 0.1em; -fx-opacity: 0.4");
                metric_bounce_rate.setTextFill(Paint.valueOf("#ff3232"));
            }
        } else {
            metric_bounce_rate.setText(null);
        }
        metrics.toFront();
    }

    @Override
    public void initialize(URL url, ResourceBundle bnd) {
        String name = askCampaignName(null);
        if(name == null)
            name = "Ad Campaign";
        handler = new Handler(name);
        setKeyMetrics();
        input.toFront();
        btn_filter.toFront();
        icon_filter.toFront();
        combo_campaigns.setPromptText(handler.getCurrentCampaign().getName());
        combo_campaigns.getItems().add(handler.getCurrentCampaign().getName());

        //NOT in the final thing, just for looks for first increment
        setRandomData();
    }

    private String askCampaignName(String s) {
        TextInputDialog dialog = new TextInputDialog("Ad Campaign");
        dialog.setTitle("Name your advertising campaign");
        if(s == null) {
            dialog.setHeaderText("Please name your advertising campaign");
        } else {
            dialog.setHeaderText(s);
        }
        dialog.showAndWait();
        String name = dialog.getResult();
        if(handler != null && name != null) {
            for (Campaign c : handler.getAllCampaigns()) {
                if (c.getName().replaceAll("\\s+","").equals(name.replaceAll("\\s+",""))) {
                    askCampaignName("That campaign name already exists!");
                }
            }
        }
        return name;
    }

    private static class Results {

        Integer integer;
        Backend.BounceType type;

        public Results(Integer integer, BounceType type) {
            this.integer = integer;
            this.type = type;
        }
    }

    //SETS RANDOM SAMPLE DATA TO MAKE IT LOOK PRETTY!
    private void setRandomData() {
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("No. Impressions", 45),
                        new PieChart.Data("No. Clicks", 25),
                        new PieChart.Data("No. Uniques", 20),
                        new PieChart.Data("No. Conversions", 22),
                        new PieChart.Data("No. Bounces", 30));
        pie1.setData(pieChartData);

        ObservableList<PieChart.Data> pieChartData2 =
                FXCollections.observableArrayList(
                        new PieChart.Data("CTR", 8),
                        new PieChart.Data("CPM", 4),
                        new PieChart.Data("CPC", 3),
                        new PieChart.Data("CPA", 5));
        pie2.setData(pieChartData2);

        XYChart.Series dataSeries1 = new XYChart.Series();
        dataSeries1.setName("Value of");

        dataSeries1.getData().add(new XYChart.Data("Impressions", 178));
        dataSeries1.getData().add(new XYChart.Data("Clicks"  , 65));
        dataSeries1.getData().add(new XYChart.Data("Uniques"  , 23));
        dataSeries1.getData().add(new XYChart.Data("Coversions"  , 103));


        chart1.getData().add(dataSeries1);

        final CategoryAxis xAxis = new CategoryAxis();
        final NumberAxis yAxis = new NumberAxis();
        xAxis.setLabel("Month");

        final LineChart<String,Number> lineChart =
                new LineChart<String,Number>(xAxis,yAxis);

        lineChart.setTitle("Stock Monitoring, 2010");

        XYChart.Series series = new XYChart.Series();
        series.setName("Number of bounce over time");

        series.getData().add(new XYChart.Data("Jan", 23));
        series.getData().add(new XYChart.Data("Feb", 14));
        series.getData().add(new XYChart.Data("Mar", 15));
        series.getData().add(new XYChart.Data("Apr", 24));
        series.getData().add(new XYChart.Data("May", 34));
        series.getData().add(new XYChart.Data("Jun", 36));
        series.getData().add(new XYChart.Data("Jul", 22));
        series.getData().add(new XYChart.Data("Aug", 45));
        series.getData().add(new XYChart.Data("Sep", 43));
        series.getData().add(new XYChart.Data("Oct", 17));
        series.getData().add(new XYChart.Data("Nov", 29));
        series.getData().add(new XYChart.Data("Dec", 25));

        chart2.getData().add(series);
    }

}
