package com.example.java1.ui;// ProductsController.java

import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import com.example.java1.backend.Part;
import com.example.java1.backend.Product;
import com.example.java1.backend.PurchaseManager;
import com.example.java1.backend.Warehouse;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import com.example.java1.MainApp;


public class PartsProductsController {

    private Warehouse warehouse;
    private PurchaseManager purchaseManager;

    @FXML
    private TextField productNameField;

    @FXML
    private TextField productPriceField;

    @FXML
    private TextField productIdField;

    @FXML
    private TextField partNameField;

    @FXML
    private TextField partPriceField;

    @FXML
    private TextField partQuantityField;

    @FXML
    private Button addButton;

    @FXML
    private Button backButton;

    public void setDependencies(Warehouse warehouse, PurchaseManager purchaseManager) {
        this.warehouse = warehouse;
        this.purchaseManager = purchaseManager;
    }

    public void showAlert() {
        Alert alert = new Alert(AlertType.INFORMATION); // Alert türü (Information)
        alert.setTitle("Ürün - Parça Ekleme");
        alert.setHeaderText("Bu bir bil");
        alert.setContentText("Buraya ek bilgi yazabilirsiniz.");
        alert.showAndWait(); // Mesajı göster ve kullanıcıdan giriş bekle
    }

    @FXML
    public void addProduct() {
        Alert alert = new Alert(AlertType.INFORMATION);
        String name = productNameField.getText();
        double price = Double.parseDouble(productPriceField.getText());
        Product product = new Product(0, name, price);
        warehouse.addProduct(product);
        alert.setTitle("Ürün Ekleme");
        alert.setHeaderText("Ürün Başarıyla Eklendi.");
    }
    @FXML
    public void addPart() {
        Alert alert = new Alert(AlertType.INFORMATION);
        String name = partNameField.getText();
        int quantity = Integer.parseInt(partQuantityField.getText());
        double price = Double.parseDouble(partPriceField.getText());
        Part part = new Part(0, name, quantity, price);
        warehouse.addPart(part);
        alert.setTitle("Parça Ekleme");
        alert.setHeaderText("Parça Başarıyla Eklendi.");
    }

    @FXML
    public void goToMainMenu() {
        MainApp.switchScene("mainmenu.fxml");
    }
    @FXML
    public void goToWarehousesMenu() {
        MainApp.switchScene("warehousesmenu.fxml");
    }
}
