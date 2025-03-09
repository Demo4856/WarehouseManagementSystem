package com.example.java1.ui;// MainMenuController.java

import com.example.java1.backend.PurchaseManager;
import com.example.java1.backend.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import com.example.java1.MainApp;
import javafx.scene.control.TextField;


public class MainMenuController {

    private Warehouse warehouse;
    private PurchaseManager purchaseManager;

    @FXML
    private Button adminButton;

    @FXML
    private TextField usernameField;

    @FXML
    private TextField passwordField;

    private LoginController loginController;

    @FXML
    private Button loginButton;

    @FXML
    private Button editProductsButton;

    @FXML
    private Button warehouseMenuButton;

    public void setDependencies(Warehouse warehouse, PurchaseManager purchaseManager) {
        this.warehouse = warehouse;
        this.purchaseManager = purchaseManager;
    }

    @FXML
    public void initialize() {
        loginController = new LoginController();

    }

    @FXML
    public void handleLogin() {
        loginController.Login();
    }


    @FXML
    public void goToAdminScene() {
        MainApp.switchScene("adminscene.fxml");
    }

    @FXML
    public void goToAddProducts() {
        MainApp.switchScene("addproducts.fxml");
    }

    @FXML
    public void goToEditProducts() {
        MainApp.switchScene("editproducts.fxml");
    }

    @FXML
    public void goToMainMenu() {
        MainApp.switchScene("mainmenu.fxml");
    }

    @FXML
    public void goToWarehousesMenu() {
        MainApp.switchScene("warehousesmenu.fxml");
    }

    @FXML
    public void goToLogs() {
        MainApp.switchScene("logs.fxml");
    }
}