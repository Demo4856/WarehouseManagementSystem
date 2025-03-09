package com.example.java1.backend;

import java.util.Scanner;

public class DisplayScreen {
    private Warehouse warehouse;
    private Scanner scanner;

    // Constructor
    public DisplayScreen(Warehouse warehouse) {
        this.warehouse = warehouse;
        this.scanner = new Scanner(System.in);
    }

    // Ana ekran
    public void showMainMenu() {
        int choice;
        do {
            System.out.println("\n=== Ürün ve Depo Yönetim Sistemi ===");
            System.out.println("1. Ürün Yönetimi");
            System.out.println("2. Parça Yönetimi");
            System.out.println("3. Depo Yönetimi");
            System.out.println("4. Çıkış");
            System.out.print("Seçiminiz: ");

            // Hatalı giriş için kontrol
            while (!scanner.hasNextInt()) {
                System.out.println("Geçersiz giriş, lütfen bir sayı girin.");
                scanner.next(); // Geçersiz girişi temizle
            }
            choice = scanner.nextInt();  // Kullanıcıdan sayı al
            scanner.nextLine(); // Boşluğu temizlemek için

            switch (choice) {
                case 1 -> manageProducts();
                case 2 -> manageParts();
                case 3 -> manageWarehouse();
                case 4 -> System.out.println("Çıkış yapılıyor...");
                default -> System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
            }
        } while (choice != 4);
    }

    // Ürün yönetimi menüsü
    private void manageProducts() {
        int choice;
        do {
            System.out.println("\n=== Ürün Yönetimi ===");
            System.out.println("1. Yeni Ürün Ekle");
            System.out.println("2. Ürün Sil");
            System.out.println("3. Ürünleri Listele");
            System.out.println("4. Ana Menüye Dön");
            System.out.print("Seçiminiz: ");

            // Hatalı giriş için kontrol
            while (!scanner.hasNextInt()) {
                System.out.println("Geçersiz giriş, lütfen bir sayı girin.");
                scanner.next(); // Geçersiz girişi temizle
            }
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addProduct();
                case 2 -> removeProduct();
                case 3 -> warehouse.listProducts();
                case 4 -> System.out.println("Ana menüye dönülüyor...");
                default -> System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
            }
        } while (choice != 4);
    }

    // Yeni ürün ekleme
    private void addProduct() {
        System.out.print("Ürün adı: ");
        String name = scanner.nextLine();
        System.out.print("Ürün maliyeti: ");

        // Hatalı giriş için kontrol
        while (!scanner.hasNextDouble()) {
            System.out.println("Geçersiz giriş, lütfen bir sayı girin.");
            scanner.next(); // Geçersiz girişi temizle
        }
        double cost = scanner.nextDouble();

        System.out.print("Üretim yılı: ");

        // Hatalı giriş için kontrol
        while (!scanner.hasNextInt()) {
            System.out.println("Geçersiz giriş, lütfen bir sayı girin.");
            scanner.next(); // Geçersiz girişi temizle
        }
        int productionYear = scanner.nextInt();
        scanner.nextLine();

        Product product = new Product(0, name, cost);  // Hatalı dönüşüm burada düzeltiliyor
        warehouse.addProduct(product);
    }

    // Ürün silme
    private void removeProduct() {
        System.out.print("Silmek istediğiniz ürün adı: ");
        String name = scanner.nextLine();
        Product productToRemove = null;

        for (Product product : warehouse.getProducts()) {
            if (product.getName().equalsIgnoreCase(name)) {
                productToRemove = product;
                break;
            }
        }

        if (productToRemove != null) {
            warehouse.removeProduct(productToRemove);
        } else {
            System.out.println("Ürün bulunamadı.");
        }
    }

    // Parça yönetimi menüsü
    private void manageParts() {
        int choice;
        do {
            System.out.println("\n=== Parça Yönetimi ===");
            System.out.println("1. Yeni Parça Ekle");
            System.out.println("2. Parça Sil");
            System.out.println("3. Parçaları Listele");
            System.out.println("4. Ana Menüye Dön");
            System.out.print("Seçiminiz: ");

            // Hatalı giriş için kontrol
            while (!scanner.hasNextInt()) {
                System.out.println("Geçersiz giriş, lütfen bir sayı girin.");
                scanner.next(); // Geçersiz girişi temizle
            }
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> addPart();
                case 2 -> removePart();
                case 3 -> warehouse.listParts();
                case 4 -> System.out.println("Ana menüye dönülüyor...");
                default -> System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
            }
        } while (choice != 4);
    }

    // Yeni parça ekleme
    private void addPart() {
        System.out.print("Parça adı: ");
        String name = scanner.nextLine();
        System.out.print("Parça maliyeti: ");

        // Hatalı giriş için kontrol
        while (!scanner.hasNextDouble()) {
            System.out.println("Geçersiz giriş, lütfen bir sayı girin.");
            scanner.next(); // Geçersiz girişi temizle
        }
        double price = scanner.nextDouble();

        System.out.print("Stok miktarı: ");

        // Hatalı giriş için kontrol
        while (!scanner.hasNextInt()) {
            System.out.println("Geçersiz giriş, lütfen bir sayı girin.");
            scanner.next(); // Geçersiz girişi temizle
        }
        int stock = scanner.nextInt();
        scanner.nextLine();

        Part part = new Part(0, name, stock, price);  // Hatalı dönüşüm burada düzeltiliyor
        warehouse.addPart(part);
    }

    // Parça silme
    private void removePart() {
        System.out.print("Silmek istediğiniz parça adı: ");
        String name = scanner.nextLine();
        Part partToRemove = null;

        for (Part part : warehouse.getParts()) {
            if (part.getName().equalsIgnoreCase(name)) {
                partToRemove = part;
                break;
            }
        }

        if (partToRemove != null) {
            warehouse.removePart(partToRemove);
        } else {
            System.out.println("Parça bulunamadı.");
        }
    }

    // Depo yönetimi menüsü
    private void manageWarehouse() {
        int choice;
        do {
            System.out.println("\n=== Depo Yönetimi ===");
            System.out.println("1. Depo Bilgilerini Kaydet");
            System.out.println("2. Depo Verilerini Yükle");
            System.out.println("3. Ana Menüye Dön");
            System.out.print("Seçiminiz: ");

            // Hatalı giriş için kontrol
            while (!scanner.hasNextInt()) {
                System.out.println("Geçersiz giriş, lütfen bir sayı girin.");
                scanner.next(); // Geçersiz girişi temizle
            }
            choice = scanner.nextInt();
            scanner.nextLine();

            switch (choice) {
                case 1 -> saveWarehouseData();
                case 2 -> loadWarehouseData();
                case 3 -> System.out.println("Ana menüye dönülüyor...");
                default -> System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
            }
        } while (choice != 3);
    }

    // Depo verilerini kaydetme
    private void saveWarehouseData() {
        System.out.print("Kaydedilecek dosya adı: ");
        String fileName = scanner.nextLine();
        warehouse.saveToFile(fileName);
    }

    // Depo verilerini yükleme
    private void loadWarehouseData() {
        System.out.print("Yüklenecek dosya adı: ");
        String fileName = scanner.nextLine();
        warehouse.loadFromFile(fileName);
    }
}
