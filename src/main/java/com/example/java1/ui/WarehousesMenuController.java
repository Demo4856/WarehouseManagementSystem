package com.example.java1.ui;

import com.example.java1.MainApp;
import com.example.java1.backend.PurchaseManager;
import com.example.java1.backend.Warehouse;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.type.TypeReference;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.KeyEvent;

import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

public class WarehousesMenuController {

    @FXML
    private TableView<Map<String, Object>> warehouseTable;

    @FXML
    private TableView<Map<String, Object>> partTable;

    @FXML
    private TableView<Map<String, Object>> productsTable;

    @FXML
    private TextField partNameField;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField newWarehouseField;

    @FXML
    private TextField warehouseSearchField;

    @FXML
    private TextField partSearchField;

    @FXML
    private TextField productSearchField;

    @FXML
    private TextField productionQuantityField;

    @FXML
    private ComboBox<String> fromWarehouseComboBox;
    // Gönderen depo
    @FXML
    private ComboBox<String> toWarehouseComboBox;    // Alıcı depo

    @FXML
    private ComboBox<String> productComboBox;  // Ürün seçimi için ComboBox

    @FXML
    private ComboBox<String> itemComboBox;

    @FXML
    private TextField transferQuantityField;
    // Transfer edilecek miktar
    @FXML
    private Button transferButton;

    private static final String FILE_PATH = "src/main/resources/com/example/java1/warehouses.json";
    private static final String RECIPES_FILE_PATH = "src/main/resources/com/example/java1/recipes.json";
    private static final String LOGS_FILE_PATH = "src/main/resources/com/example/java1/logs.json";

    private List<Map<String, Object>> recipes;
    private List<Map<String, Object>> warehouses;

    // Master listeler (asıl veri kaynağı)
    private final ObservableList<Map<String, Object>> masterWarehouseData = FXCollections.observableArrayList();
    private final ObservableList<Map<String, Object>> masterPartData = FXCollections.observableArrayList();
    private final ObservableList<Map<String, Object>> masterProductData = FXCollections.observableArrayList();


    @FXML
    public void initialize() {
        loadWarehouses();
        setupTables();
        loadRecipes();
        populateComboBoxes();
        populateProductComboBox();
        setupListeners();
        warehouseSearchField.setOnKeyReleased(this::filterWarehouses);
        partSearchField.setOnKeyReleased(this::filterParts);
        productSearchField.setOnKeyReleased(this::filterProducts);
    }

    // Depo ve parça tablolarını yapılandırma
    private void setupTables() {
        TableColumn<Map<String, Object>, String> warehouseColumn = new TableColumn<>("Depolar");
        warehouseColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty((String) param.getValue().get("name")));
        warehouseTable.getColumns().add(warehouseColumn);

        TableColumn<Map<String, Object>, String> partNameColumn = new TableColumn<>("Parça Adı");
        partNameColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty((String) param.getValue().get("partName")));

        TableColumn<Map<String, Object>, Integer> quantityColumn = new TableColumn<>("Stok");
        quantityColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleIntegerProperty((Integer) param.getValue().get("quantity")).asObject());

        partTable.getColumns().addAll(partNameColumn, quantityColumn);

        TableColumn<Map<String, Object>, String> productNameColumn = new TableColumn<>("Ürün Adı");
        productNameColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleStringProperty((String) param.getValue().get("productName")));

        TableColumn<Map<String, Object>, Integer> stockColumn = new TableColumn<>("Stok");
        stockColumn.setCellValueFactory(param -> new javafx.beans.property.SimpleIntegerProperty((Integer) param.getValue().get("stock")).asObject());

        productsTable.getColumns().addAll(productNameColumn, stockColumn);


        warehouseTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getClickCount() == 1 && warehouseTable.getSelectionModel().getSelectedItem() != null) {
                loadPartsForSelectedWarehouse();
                loadProductsForSelectedWarehouse();
            }
        });
    }

    // Depoları JSON dosyasından yükleme
    private void loadWarehouses() {
        warehouses = readWarehousesFromFile();
        if (warehouses != null) {
            warehouseTable.getItems().clear();
            warehouseTable.getItems().addAll(warehouses);
        }
    }
    // Ürün tariflerini JSON dosyasından yükleme
    private void loadRecipes() {
        File file = new File(RECIPES_FILE_PATH);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            recipes = objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Ürün tarifleri yüklenemedi.");
            recipes = new ArrayList<>();
        }
    }

    // Seçilen depodaki parçaları yükleme
    private void loadPartsForSelectedWarehouse() {
        Map<String, Object> selectedWarehouse = warehouseTable.getSelectionModel().getSelectedItem();
        if (selectedWarehouse != null) {
            List<Map<String, Object>> parts = (List<Map<String, Object>>) selectedWarehouse.get("parts");
            partTable.getItems().clear();
            partTable.getItems().addAll(parts);
        }
    }

    // Seçilen depodaki ürünleri yükleme
    private void loadProductsForSelectedWarehouse() {
        Map<String, Object> selectedWarehouse = warehouseTable.getSelectionModel().getSelectedItem();
        if (selectedWarehouse != null) {
            List<Map<String, Object>> products = (List<Map<String, Object>>) selectedWarehouse.get("products");
            if (products != null) {
                productsTable.getItems().clear();
                productsTable.getItems().addAll(products);
            } else {
                productsTable.getItems().clear();  // Ürün listesi yoksa tabloyu boşalt
            }
        }
    }

    // Yeni depo ekleme butonu işlemi
    @FXML
    public void addWarehouse() {
        String newWarehouseName = newWarehouseField.getText().trim();
        if (newWarehouseName.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen depo adını girin.");
            return;
        }

        for (Map<String, Object> warehouse : warehouses) {
            if (warehouse.get("name").equals(newWarehouseName)) {
                showAlert(Alert.AlertType.ERROR, "Hata", "Bu depo zaten mevcut.");
                return;
            }
        }

        Map<String, Object> newWarehouse = new HashMap<>();
        newWarehouse.put("name", newWarehouseName);
        newWarehouse.put("parts", new ArrayList<>());
        warehouses.add(newWarehouse);
        writeWarehousesToFile(warehouses);

        showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Yeni depo başarıyla eklendi!");
        loadWarehouses();
        logAddWarehouse(newWarehouseName);
    }

    @FXML
    public void addPartToWarehouse() {
        Map<String, Object> selectedWarehouse = warehouseTable.getSelectionModel().getSelectedItem();
        String partName = partNameField.getText().trim();
        int quantity;
        String warehouseName = (String) selectedWarehouse.get("name");

        try {
            quantity = Integer.parseInt(quantityField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen geçerli bir sayı girin.");
            return;
        }

        if (selectedWarehouse == null || partName.isEmpty() || quantity <= 0) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen tüm alanları doldurun.");
            return;
        }

        // Parçalar listesini al
        List<Map<String, Object>> parts = (List<Map<String, Object>>) selectedWarehouse.get("parts");

        // Parçayı liste içinde arıyoruz
        Optional<Map<String, Object>> existingPartOpt = parts.stream()
                .filter(part -> part.get("partName").equals(partName))
                .findFirst();

        if (existingPartOpt.isPresent()) {
            // Eğer parça varsa mevcut miktara ekle
            Map<String, Object> existingPart = existingPartOpt.get();
            int currentQuantity = (int) existingPart.get("quantity");
            existingPart.put("quantity", currentQuantity + quantity);
        } else {
            // Parça yoksa yeni parça ekle
            parts.add(Map.of("partName", partName, "quantity", quantity));
        }

        // Güncellenmiş listeyi JSON'a yaz
        writeWarehousesToFile(warehouses);

        showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Parça başarıyla eklendi!");
        loadPartsForSelectedWarehouse(); // Güncellenmiş tabloyu göster
        logAddPart(warehouseName,partName,quantity);
    }
    // Ürün ekleme işlemi
    @FXML
    public void addProductToWarehouse() {
        Map<String, Object> selectedWarehouse = warehouseTable.getSelectionModel().getSelectedItem();
        if (selectedWarehouse == null) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen bir depo seçin.");
            return;
        }

        String productName = partNameField.getText().trim();
        int productQuantity;
        String warehouseName = (String) selectedWarehouse.get("name");

        try {
            productQuantity = Integer.parseInt(quantityField.getText().trim());
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen geçerli bir sayı girin.");
            return;
        }

        if (productName.isEmpty() || productQuantity <= 0) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen tüm alanları doldurun.");
            return;
        }

        List<Map<String, Object>> products = (List<Map<String, Object>>) selectedWarehouse.get("products");
        if (products == null) {
            products = FXCollections.observableArrayList();
            selectedWarehouse.put("products", products);
        }

        Optional<Map<String, Object>> existingProductOpt = products.stream()
                .filter(p -> p.get("productName").equals(productName))
                .findFirst();

        if (existingProductOpt.isPresent()) {
            int currentStock = (int) existingProductOpt.get().get("stock");
            existingProductOpt.get().put("stock", currentStock + productQuantity);
        } else {
            products.add(Map.of("productName", productName, "stock", productQuantity));
        }

        showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Ürün başarıyla eklendi!");
        loadProductsForSelectedWarehouse();
        logAddProduct(warehouseName,productName,productQuantity);
    }

    // Parça düzenleme
    @FXML
    public void editPartInWarehouse() {
        Map<String, Object> selectedWarehouse = warehouseTable.getSelectionModel().getSelectedItem();
        Map<String, Object> selectedPart = partTable.getSelectionModel().getSelectedItem();

        if (selectedWarehouse == null || selectedPart == null) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen bir depo ve düzenlenecek parçayı seçin.");
            return;
        }
        String warehouseName = (String) selectedWarehouse.get("name");
        String oldPartName = (String) selectedPart.get("partName");
        int oldStock = (int) selectedPart.get("quantity");  // Eski stok miktarı

        String newPartName = showInputDialog("Parça Düzenle", "Yeni parça adını girin:");
        int newQuantity;

        try {
            newQuantity = Integer.parseInt(showInputDialog("Parça Düzenle", "Yeni miktarı girin:"));
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen geçerli bir sayı girin.");
            return;
        }

        if (newPartName == null || newPartName.trim().isEmpty() || newQuantity < 0) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen geçerli bir bilgi girin.");
            return;
        }

        selectedPart.put("partName", newPartName);
        selectedPart.put("quantity", newQuantity);
        writeWarehousesToFile(warehouses);
        loadPartsForSelectedWarehouse();

        showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Parça başarıyla güncellendi!");
        logEditPart(warehouseName,oldPartName,newPartName,oldStock,newQuantity);

    }

    // Parça silme
    @FXML
    public void deletePartFromWarehouse() {
        Map<String, Object> selectedWarehouse = warehouseTable.getSelectionModel().getSelectedItem();
        Map<String, Object> selectedPart = partTable.getSelectionModel().getSelectedItem();

        if (selectedWarehouse == null || selectedPart == null) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen bir depo ve silinecek parçayı seçin.");
            return;
        }

        List<Map<String, Object>> parts = (List<Map<String, Object>>) selectedWarehouse.get("parts");
        parts.remove(selectedPart);
        writeWarehousesToFile(warehouses);
        loadPartsForSelectedWarehouse();

        showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Parça başarıyla silindi!");
    }

    // Ürün düzenleme
    @FXML
    public void editProductInWarehouse() {
        Map<String, Object> selectedWarehouse = warehouseTable.getSelectionModel().getSelectedItem();
        Map<String, Object> selectedProduct = productsTable.getSelectionModel().getSelectedItem();

        if (selectedWarehouse == null || selectedProduct == null) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen bir depo ve düzenlenecek ürünü seçin.");
            return;
        }

        String warehouseName = (String) selectedWarehouse.get("name");
        // Eski ürün bilgilerini al
        String oldProductName = (String) selectedProduct.get("productName");
        int oldStock = (int) selectedProduct.get("stock");  // Eski stok miktarı

        String newProductName = showInputDialog("Ürün Düzenle", "Yeni ürün adını girin:");
        int newStock;

        try {
            newStock = Integer.parseInt(showInputDialog("Ürün Düzenle", "Yeni stok miktarını girin:"));
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen geçerli bir sayı girin.");
            return;
        }

        if (newProductName == null || newProductName.trim().isEmpty() || newStock < 0) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen geçerli bir bilgi girin.");
            return;
        }

        selectedProduct.put("productName", newProductName);
        selectedProduct.put("stock", newStock);
        writeWarehousesToFile(warehouses);
        loadProductsForSelectedWarehouse();

        showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Ürün başarıyla güncellendi!");
        logEditProduct(warehouseName,oldProductName,newProductName,oldStock,newStock);
    }

    // Ürün silme
    @FXML
    public void deleteProductFromWarehouse() {
        Map<String, Object> selectedWarehouse = warehouseTable.getSelectionModel().getSelectedItem();
        Map<String, Object> selectedProduct = productsTable.getSelectionModel().getSelectedItem();

        if (selectedWarehouse == null || selectedProduct == null) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen bir depo ve silinecek ürünü seçin.");
            return;
        }

        List<Map<String, Object>> products = (List<Map<String, Object>>) selectedWarehouse.get("products");
        products.remove(selectedProduct);
        writeWarehousesToFile(warehouses);
        loadProductsForSelectedWarehouse();

        showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Ürün başarıyla silindi!");
    }

    @FXML
    public void produceProduct() {
        Map<String, Object> selectedWarehouse = warehouseTable.getSelectionModel().getSelectedItem();
        if (selectedWarehouse == null) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen bir depo seçin.");
            return;
        }

        String productName = productComboBox.getValue();  // Ürün adı ComboBox'tan alınır
        if (productName == null || productName.trim().isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen bir ürün seçin.");
            return;
        }

        String warehouseName = (String) selectedWarehouse.get("name");
        int productionQuantity;
        try {
            productionQuantity = Integer.parseInt(productionQuantityField.getText().trim());
            if (productionQuantity <= 0) {
                showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen geçerli bir miktar girin.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Geçerli bir üretim miktarı girin.");
            return;
        }

        // Ürün tarifini bul
        Map<String, Object> recipe = recipes.stream()
                .filter(r -> r.get("productName").equals(productName))
                .findFirst()
                .orElse(null);

        if (recipe == null) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Bu ürün için tarif bulunamadı.");
            return;
        }

        List<Map<String, Object>> requiredParts = (List<Map<String, Object>>) recipe.get("recipe");
        List<Map<String, Object>> availableParts = (List<Map<String, Object>>) selectedWarehouse.get("parts");

        // Parçaların yeterliliğini kontrol et
        for (Map<String, Object> part : requiredParts) {
            String partName = (String) part.get("partName");
            int requiredQuantity = (int) part.get("quantity") * productionQuantity;

            Optional<Map<String, Object>> availablePartOpt = availableParts.stream()
                    .filter(p -> p.get("partName").equals(partName))
                    .findFirst();

            if (availablePartOpt.isEmpty() || (int) availablePartOpt.get().get("quantity") < requiredQuantity) {
                showAlert(Alert.AlertType.ERROR, "Hata", "Yeterli parça bulunmuyor: " + partName);
                return;
            }
        }

        // Stokları güncelle
        for (Map<String, Object> part : requiredParts) {
            String partName = (String) part.get("partName");
            int requiredQuantity = (int) part.get("quantity") * productionQuantity;

            availableParts.stream()
                    .filter(p -> p.get("partName").equals(partName))
                    .findFirst()
                    .ifPresent(p -> p.put("quantity", (int) p.get("quantity") - requiredQuantity));
        }

        // Ürünü depoya ekle
        List<Map<String, Object>> products = (List<Map<String, Object>>) selectedWarehouse.get("products");
        if (products == null) {
            products = new ArrayList<>();
            selectedWarehouse.put("products", products);
        }

        Optional<Map<String, Object>> productOpt = products.stream()
                .filter(p -> p.get("productName").equals(productName))
                .findFirst();

        if (productOpt.isPresent()) {
            int currentStock = (int) productOpt.get().get("stock");
            productOpt.get().put("stock", currentStock + productionQuantity);
        } else {
            products.add(Map.of("productName", productName, "stock", productionQuantity));
        }

        writeWarehousesToFile(warehouses);
        showAlert(Alert.AlertType.INFORMATION, "Başarılı", "Ürün başarıyla üretildi!");
        productionQuantityField.clear();  // Üretim miktarını sıfırla
        logCreateProduct(warehouseName ,productName, productionQuantity);
    }


    // JSON'dan depo listesini okuma
    private List<Map<String, Object>> readWarehousesFromFile() {
        File file = new File(FILE_PATH);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            return objectMapper.readValue(file, new TypeReference<List<Map<String, Object>>>() {});
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Veri yüklenirken hata oluştu.");
            return new ArrayList<>();
        }
    }
    // Depo araması
    private void filterWarehouses(KeyEvent keyEvent) {
        String searchText = warehouseSearchField.getText().toLowerCase();
        if (searchText.isEmpty()) {
            warehouseTable.setItems(masterWarehouseData);  // Tüm listeyi geri yükle
        } else {
            List<Map<String, Object>> filteredWarehouses = masterWarehouseData.stream()
                    .filter(warehouse -> ((String) warehouse.get("name")).toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
            warehouseTable.setItems(FXCollections.observableArrayList(filteredWarehouses));
        }
    }

    // Parça araması
    private void filterParts(KeyEvent keyEvent) {
        String searchText = partSearchField.getText().toLowerCase();
        if (searchText.isEmpty()) {
            partTable.setItems(masterPartData);  // Tüm listeyi geri yükle
        } else {
            List<Map<String, Object>> filteredParts = masterPartData.stream()
                    .filter(part -> ((String) part.get("partName")).toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
            partTable.setItems(FXCollections.observableArrayList(filteredParts));
        }
    }

    // Ürün araması
    private void filterProducts(KeyEvent keyEvent) {
        String searchText = productSearchField.getText().toLowerCase();
        if (searchText.isEmpty()) {
            productsTable.setItems(masterProductData);  // Tüm listeyi geri yükle
        } else {
            List<Map<String, Object>> filteredProducts = masterProductData.stream()
                    .filter(product -> ((String) product.get("productName")).toLowerCase().contains(searchText))
                    .collect(Collectors.toList());
            productsTable.setItems(FXCollections.observableArrayList(filteredProducts));
        }
    }

    // Depoların listesini yükleme
    public void loadWarehouses(List<Map<String, Object>> warehouses) {
        masterWarehouseData.setAll(warehouses);  // Ana listeyi doldur
        warehouseTable.setItems(masterWarehouseData);  // Tabloya ana listeyi yükle
    }

    // Parçaların listesini yükleme
    public void loadParts(List<Map<String, Object>> parts) {
        masterPartData.setAll(parts);  // Ana listeyi doldur
        partTable.setItems(masterPartData);  // Tabloya ana listeyi yükle
    }

    // Ürünlerin listesini yükleme
    public void loadProducts(List<Map<String, Object>> products) {
        masterProductData.setAll(products);  // Ana listeyi doldur
        productsTable.setItems(masterProductData);  // Tabloya ana listeyi yükle
    }

    // ComboBox bileşenlerini doldurma
    private void populateComboBoxes() {
        fromWarehouseComboBox.getItems().clear();
        toWarehouseComboBox.getItems().clear();

        for (Map<String, Object> warehouse : warehouses) {
            String warehouseName = (String) warehouse.get("name");
            fromWarehouseComboBox.getItems().add(warehouseName);
            toWarehouseComboBox.getItems().add(warehouseName);
        }
    }
    // Ürün ComboBox'ını tariflere göre doldurma
    private void populateProductComboBox() {
        productComboBox.getItems().clear();
        for (Map<String, Object> recipe : recipes) {
            String productName = (String) recipe.get("productName");
            productComboBox.getItems().add(productName);
        }
    }
    private void setupListeners() {
        fromWarehouseComboBox.setOnAction(event -> loadItemsForSelectedWarehouse());
    }
    // Seçilen depodaki ürün/parçaları ComboBox'a yükleme
    private void loadItemsForSelectedWarehouse() {
        itemComboBox.getItems().clear();
        String selectedWarehouseName = fromWarehouseComboBox.getValue();
        Map<String, Object> selectedWarehouse = getWarehouseByName(selectedWarehouseName);

        if (selectedWarehouse != null) {
            List<Map<String, Object>> parts = (List<Map<String, Object>>) selectedWarehouse.get("parts");
            List<Map<String, Object>> products = (List<Map<String, Object>>) selectedWarehouse.getOrDefault("products", new ArrayList<>());

            for (Map<String, Object> part : parts) {
                String partName = "Parça: " + part.get("partName");
                itemComboBox.getItems().add(partName);
            }

            for (Map<String, Object> product : products) {
                String productName = "Ürün: " + product.get("productName");
                itemComboBox.getItems().add(productName);
            }
        }
    }
    private Map<String, Object> getWarehouseByName(String warehouseName) {
        return warehouses.stream()
                .filter(warehouse -> warehouse.get("name").equals(warehouseName))
                .findFirst()
                .orElse(null);
    }

    @FXML
    public void transferItem() {
        String fromWarehouseName = fromWarehouseComboBox.getValue();
        String toWarehouseName = toWarehouseComboBox.getValue();
        String selectedItem = itemComboBox.getValue();
        int transferQuantity;

        if (fromWarehouseName == null || toWarehouseName == null || selectedItem == null || fromWarehouseName.equals(toWarehouseName)) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Lütfen tüm seçimleri doğru şekilde yapın.");
            return;
        }

        try {
            transferQuantity = Integer.parseInt(transferQuantityField.getText().trim());
            if (transferQuantity <= 0) {
                showAlert(Alert.AlertType.ERROR, "Hata", "Miktar pozitif bir sayı olmalıdır.");
                return;
            }
        } catch (NumberFormatException e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Geçerli bir miktar girin.");
            return;
        }

        Map<String, Object> fromWarehouse = getWarehouseByName(fromWarehouseName);
        Map<String, Object> toWarehouse = getWarehouseByName(toWarehouseName);

        if (fromWarehouse == null || toWarehouse == null) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Depo bilgileri bulunamadı.");
            return;
        }

        boolean isPart = selectedItem.contains("Parça: ");
        String itemName = isPart ? selectedItem.replace("Parça: ", "") : selectedItem.replace("Ürün: ", "");

        List<Map<String, Object>> fromItems = isPart ? (List<Map<String, Object>>) fromWarehouse.get("parts") : (List<Map<String, Object>>) fromWarehouse.get("products");
        List<Map<String, Object>> toItems = isPart ? (List<Map<String, Object>>) toWarehouse.get("parts") : (List<Map<String, Object>>) toWarehouse.get("products");

        Optional<Map<String, Object>> fromItemOpt = fromItems.stream().filter(i -> i.get(isPart ? "partName" : "productName").equals(itemName)).findFirst();

        if (fromItemOpt.isEmpty() || (int) fromItemOpt.get().get("quantity") < transferQuantity) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Yeterli stok yok: " + itemName);
            return;
        }

        // Değiştirilebilir bir Map kullanmak için HashMap
        Map<String, Object> fromItem = new HashMap<>(fromItemOpt.get());
        int currentQuantity = (int) fromItem.get("quantity");
        fromItem.put("quantity", currentQuantity - transferQuantity);

        if (fromItem.get("quantity").equals(0)) {
            fromItems.remove(fromItemOpt.get());  // Stok sıfır ise listeden kaldır
        } else {
            fromItemOpt.get().put("quantity", currentQuantity - transferQuantity);  // Güncelleme
        }

        Optional<Map<String, Object>> toItemOpt = toItems.stream().filter(i -> i.get(isPart ? "partName" : "productName").equals(itemName)).findFirst();

        if (toItemOpt.isPresent()) {
            Map<String, Object> toItem = new HashMap<>(toItemOpt.get());
            int toCurrentQuantity = (int) toItem.get("quantity");
            toItem.put("quantity", toCurrentQuantity + transferQuantity);
            toItemOpt.get().put("quantity", toCurrentQuantity + transferQuantity);
        } else {
            // HashMap ile yeni öğe ekleme
            Map<String, Object> newItem = new HashMap<>();
            newItem.put(isPart ? "partName" : "productName", itemName);
            newItem.put("quantity", transferQuantity);
            toItems.add(newItem);
        }

        writeWarehousesToFile(warehouses);
        showAlert(Alert.AlertType.INFORMATION, "Başarılı", itemName + " başarıyla transfer edildi!");

        // Transfer işlemi sonrası alanı sıfırla
        transferQuantityField.clear();
        itemComboBox.getSelectionModel().clearSelection();
        fromWarehouseComboBox.getSelectionModel().clearSelection();
        toWarehouseComboBox.getSelectionModel().clearSelection();
        logTransfer(fromWarehouseName,toWarehouseName,itemName,transferQuantity);
    }


    private Map<String, Object> findWarehouseByName(String name) {
        return masterWarehouseData.stream()
                .filter(warehouse -> warehouse.get("name").equals(name))
                .findFirst()
                .orElse(null);
    }

    // JSON dosyasına depo listesini yazma
    private void writeWarehousesToFile(List<Map<String, Object>> warehouses) {
        File file = new File(FILE_PATH);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, warehouses);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Veri kaydedilirken hata oluştu.");
        }
    }

    // Log kaydetme fonksiyonu
    private void addLog(String action, String description) {
        List<Map<String, String>> logs = readLogsFromFile();
        Map<String, String> logEntry = new HashMap<>();
        logEntry.put("action", action);
        logEntry.put("description", description);
        logEntry.put("timestamp", java.time.LocalDateTime.now().toString());
        logs.add(logEntry);
        writeLogsToFile(logs);
    }

    // JSON'dan log listesini okuma
    private List<Map<String, String>> readLogsFromFile() {
        File file = new File(LOGS_FILE_PATH);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            if (file.exists()) {
                return objectMapper.readValue(file, new TypeReference<List<Map<String, String>>>() {});
            } else {
                return new ArrayList<>();
            }
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Loglar yüklenirken hata oluştu.");
            return new ArrayList<>();
        }
    }

    // JSON dosyasına log listesini yazma
    private void writeLogsToFile(List<Map<String, String>> logs) {
        File file = new File(LOGS_FILE_PATH);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, logs);
        } catch (IOException e) {
            showAlert(Alert.AlertType.ERROR, "Hata", "Log kaydedilirken hata oluştu.");
        }
    }

    // Alert mesajı gösterme
    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private String showInputDialog(String title, String message) {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle(title);
        dialog.setHeaderText(null);
        dialog.setContentText(message);
        Optional<String> result = dialog.showAndWait();
        return result.orElse(null);
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

    // Örnek kullanımlar
    public void logAddWarehouse(String warehouseName) {
        addLog("Depo Ekleme", "Depo eklendi: " + warehouseName);
    }

    public void logAddPart(String warehosueName, String partName, int quantity) {
        addLog("Parça Ekleme",  "Depo: " + warehosueName + ", Parça eklendi: " + partName + ", Miktar: " + quantity);
    }

    public void logAddProduct(String warehouseName, String productName, int quantity){
        addLog("Ürün Ekleme", "Depo: " + warehouseName + ", Ürün Eklendi: " + productName + ", Miktar: " + quantity);
    }

    public void logCreateProduct(String warehouseName, String productName, int quantity){
        addLog("Ürün Üretme", "Depo: " + warehouseName + ", Ürün Üretildi: " + productName + ", Miktar: " + quantity);
    }

    public void logEditPart(String warehouseName, String partName, String newPartName, int quantity, int newQuantity){
        addLog("Parça Değiştirme", "Depo: " + warehouseName + ", Parça Değiştirildi: Eski -> " + partName + ", Yeni -> " + newPartName + ", Miktar: Eski -> " + quantity + ", Yeni -> " + newQuantity);
    }
    public void logEditProduct(String warehouseName, String productName, String newProductName, int quantity, int newQuantity){
        addLog("Ürün Değiştirme",    "Depo: " + warehouseName + ", Ürün Değiştirildi: Eski -> " + productName + ", Yeni -> " + newProductName + ", Miktar: Eski -> " + quantity + ", Yeni -> " + newQuantity);
    }
    public void logTransfer(String from, String to, String itemName, int quantity) {
        addLog("Transfer", from + " deposundan " + to + " deposuna " + quantity + " adet " + itemName + " gönderildi.");
    }

    public void setDependencies(Warehouse warehouse, PurchaseManager purchaseManager) {
    }
}
