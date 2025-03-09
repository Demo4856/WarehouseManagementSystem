package com.example.java1.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;

import java.io.File;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.example.java1.MainApp;



public class LoginController {

    private static final String FILE_PATH = "users.json";
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;

    // Giriş Yap Butonu
    @FXML
    public void handleLogin() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen tüm alanları doldurun.");
            return;
        }

        Map<String, String> users = loadUsersFromFile();
        if (users.containsKey(email) && users.get(email).equals(password)) {
            showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Giriş başarılı!");
            MainApp.switchScene("mainmenu.fxml");
            // Ana menüye yönlendirme yapılabilir
        } else {
            showAlert(Alert.AlertType.ERROR, "Hatalı Giriş", "Email veya şifre yanlış.");
        }
    }

    // Kayıt Ol Butonu
    @FXML
    public void handleRegister() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();

        if (email.isEmpty() || password.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen tüm alanları doldurun.");
            return;
        }

        Map<String, String> users = loadUsersFromFile();

        if (users.containsKey(email)) {
            showAlert(Alert.AlertType.WARNING, "Uyarı", "Bu email zaten kayıtlı.");
        } else {
            users.put(email, password);
            saveUsersToFile(users);
            showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Kayıt işlemi başarılı!");
        }
    }

    // Kullanıcıları JSON'dan okuma
    private Map<String, String> loadUsersFromFile() {
        File file = new File("src/main/resources/com/example/java1/users.json");
        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> users = new HashMap<>();

        if (file.exists()) {
            try {
                users = objectMapper.readValue(file, new TypeReference<Map<String, String>>() {});
            } catch (IOException e) {
                showAlert(Alert.AlertType.ERROR, "Hata", "Veri yüklenirken hata oluştu.");
            }
        }
        return users;
    }

    // Kullanıcıları JSON'a yazma
    private void saveUsersToFile(Map<String, String> users) {
        File file = new File("src/main/resources/com/example/java1/users.json");
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writeValue(file, users);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Veri kaydedilirken hata oluştu.");
        }
    }

    // Alert Mesajı Gösterme
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void Login() {
    }
}
