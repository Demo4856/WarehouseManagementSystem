package com.example.java1.ui;

import com.example.java1.MainApp;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

public class LogsController {

    @FXML
    private TableView<LogEntry> logsTable;

    @FXML
    private TableColumn<LogEntry, String> actionColumn;

    @FXML
    private TableColumn<LogEntry, String> descriptionColumn;

    @FXML
    private TableColumn<LogEntry, String> timestampColumn;

    private static final String LOGS_FILE_PATH = "src/main/resources/com/example/java1/logs.json";

    @FXML
    public void initialize() {
        // Tablo sütunlarının yapılandırılması
        TableColumn<LogEntry, String> actionColumn = new TableColumn<>("İşlem");
        actionColumn.setCellValueFactory(new PropertyValueFactory<>("action"));

        TableColumn<LogEntry, String> descriptionColumn = new TableColumn<>("Açıklama");
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));

        TableColumn<LogEntry, String> timestampColumn = new TableColumn<>("Zaman");
        timestampColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));

        logsTable.getColumns().addAll(actionColumn, descriptionColumn, timestampColumn);

        loadLogs();
    }

    private void loadLogs() {
        File file = new File(LOGS_FILE_PATH);
        ObjectMapper objectMapper = new ObjectMapper();
        List<LogEntry> logs;

        try {
            if (file.exists() && file.length() > 0) {
                logs = objectMapper.readValue(file, new TypeReference<List<LogEntry>>() {});
            } else {
                logs = List.of();  // Boş liste
                System.out.println("Log dosyası boş.");
            }
        } catch (IOException e) {
            logs = List.of();
            System.out.println("Log dosyası okunurken hata oluştu: " + e.getMessage());
        }

        logsTable.getItems().clear();
        logsTable.getItems().addAll(logs);
    }

    // LogEntry veri sınıfı (nested class)
    public static class LogEntry {
        public String action;
        public String description;
        public String timestamp;

        public LogEntry(){

        }
        public LogEntry(String action, String description, String timestamp) {
            this.action = action;
            this.description = description;
            this.timestamp = timestamp;
        }

        public String getAction() {
            return action;
        }

        public String getDescription() {
            return description;
        }

        public String getTimestamp() {
            return timestamp;
        }
    }


    @FXML
    public void goToMainMenu() {
        MainApp.switchScene("mainmenu.fxml");
    }
}
