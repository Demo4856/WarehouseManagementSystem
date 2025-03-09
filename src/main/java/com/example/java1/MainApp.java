package com.example.java1;

// MainApp.java
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import com.example.java1.backend.*;
import com.example.java1.ui.*;

public class MainApp extends Application {

    private static Stage primaryStage;
    private static Warehouse warehouse;
    private static PurchaseManager purchaseManager;

    @Override
    public void start(Stage primaryStage) throws Exception {
        MainApp.primaryStage = primaryStage;
        warehouse = new Warehouse("Main Warehouse");
        purchaseManager = new PurchaseManager();

        primaryStage.setTitle("Warehouse Management System");
        switchScene("adminscene.fxml");
        primaryStage.show();
    }

    public static void switchScene(String fxmlFile) {
        try {
            FXMLLoader loader = new FXMLLoader(MainApp.class.getResource("/com/example/java1/" + fxmlFile));
            Parent root = loader.load();

            // Set dependencies in the controllers
            Object controller = loader.getController();
            if (controller instanceof MainMenuController) {
                ((MainMenuController) controller).setDependencies(warehouse, purchaseManager);
            } else if (controller instanceof AdminSceneController) {
                ((AdminSceneController) controller).setDependencies(warehouse, purchaseManager);
            } else if (controller instanceof PartsProductsController) {
                ((PartsProductsController) controller).setDependencies(warehouse, purchaseManager);
            } else if (controller instanceof WarehousesMenuController) {
                ((WarehousesMenuController) controller).setDependencies(warehouse, purchaseManager);
            }

            Scene scene = new Scene(root);
            primaryStage.setScene(scene);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load the FXML file: " + fxmlFile);
        }
    }

    public static void main(String[] args) {
        launch(args);
    }
}
