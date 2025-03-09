package com.example.java1.ui;// AdminSceneController.java

import com.example.java1.backend.PurchaseManager;
import com.example.java1.backend.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import com.example.java1.MainApp;


public class AdminSceneController {

    private Warehouse warehouse;
    private PurchaseManager purchaseManager;

    @FXML
    private Button backButton;

    public void setDependencies(Warehouse warehouse, PurchaseManager purchaseManager) {
        this.warehouse = warehouse;
        this.purchaseManager = purchaseManager;
    }

    @FXML
    public void goBackToMainMenu() {
        MainApp.switchScene("mainmenu.fxml");
    }
}