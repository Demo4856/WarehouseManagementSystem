package com.example.java1.backend;

import java.util.Scanner;

public class ConsoleApp {
    public static void startConsoleApp() {
        // Depo, ürün yöneticisi ve üretici sınıflarının örnekleri oluşturulur
        Warehouse warehouse = new Warehouse("Ana Depo");
        PurchaseManager purchaseManager = new PurchaseManager();
        SupplyDepartment supplyDepartment = new SupplyDepartment();

        Scanner scanner = new Scanner(System.in);

        System.out.println("Ürün Parça Takip Uygulamasına Hoş Geldiniz!");
        boolean exit = false;

        while (!exit) {
            System.out.println("\nLütfen bir işlem seçin:");
            System.out.println("1. Yeni Ürün Ekle");
            System.out.println("2. Yeni Parça Ekle");
            System.out.println("3. Depoya Ürün Ekle");
            System.out.println("4. Depodan Ürün Sil");
            System.out.println("5. Ürüne Parça Ekle");
            System.out.println("6. Üründen Parça Sil");
            System.out.println("7. Depo Durumunu Görüntüle");
            System.out.println("8. Depo Verilerini Kaydet");
            System.out.println("9. Depo Verilerini Yükle");
            System.out.println("10. Çıkış");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Yeni satırı temizler

            switch (choice) {
                case 1:
                    // Yeni ürün ekleme
                    System.out.print("Ürün adı: ");
                    String productName = scanner.nextLine();
                    System.out.print("Ürün fiyatı: ");
                    double productPrice = scanner.nextDouble();
                    Product newProduct = new Product(0, productName, productPrice);
                    purchaseManager.purchaseProduct(newProduct);
                    System.out.println("Ürün başarıyla eklendi: " + newProduct.getName());
                    break;

                case 2:
                    // Yeni parça ekleme
                    System.out.print("Parça adı: ");
                    String partName = scanner.nextLine();
                    System.out.print("Parça fiyatı: ");
                    double partPrice = scanner.nextDouble();
                    System.out.print("Parça stok miktarı: ");
                    int stock = scanner.nextInt();
                    Part newPart = new Part(0, partName, stock, partPrice);
                    purchaseManager.purchasePart(newPart);
                    System.out.println("Parça başarıyla eklendi: " + newPart.getName());
                    break;

                case 3:
                    // Depoya ürün ekleme
                    System.out.print("Depoya eklenecek ürün adı: ");
                    String warehouseProductName = scanner.nextLine();
                    Product productToAdd = purchaseManager.findProductByName(warehouseProductName);
                    if (productToAdd != null) {
                        warehouse.addProduct(productToAdd);
                        System.out.println("Ürün depoya başarıyla eklendi: " + productToAdd.getName());
                    } else {
                        System.out.println("Ürün bulunamadı.");
                    }
                    break;

                case 4:
                    // Depodan ürün silme
                    System.out.print("Depodan silinecek ürün adı: ");
                    String warehouseProductToDelete = scanner.nextLine();
                    Product productToDelete = purchaseManager.findProductByName(warehouseProductToDelete);
                    if (productToDelete != null) {
                        warehouse.removeProduct(productToDelete);
                        System.out.println("Ürün depodan silindi.");
                    } else {
                        System.out.println("Ürün bulunamadı.");
                    }
                    break;

                case 5:
                    // Ürüne parça ekleme
                    System.out.print("Parça eklenecek ürün adı: ");
                    String productForPartName = scanner.nextLine();
                    Product productForPart = purchaseManager.findProductByName(productForPartName);

                    if (productForPart != null) {
                        System.out.print("Eklenecek parça adı: ");
                        String partToAddName = scanner.nextLine();
                        Part partToAdd = purchaseManager.findPartByName(partToAddName);

                        if (partToAdd != null) {
                            warehouse.addPartToProduct(productForPart, partToAdd);
                            System.out.println("Parça başarıyla eklendi: " + partToAdd.getName());
                        } else {
                            System.out.println("Parça bulunamadı.");
                        }
                    } else {
                        System.out.println("Ürün bulunamadı.");
                    }
                    break;

                case 6:
                    // Üründen parça silme
                    System.out.print("Parça silinecek ürün adı: ");
                    String productForPartRemoveName = scanner.nextLine();
                    Product productForPartRemove = purchaseManager.findProductByName(productForPartRemoveName);

                    if (productForPartRemove != null) {
                        System.out.print("Silinecek parça adı: ");
                        String partToRemoveName = scanner.nextLine();
                        Part partToRemove = purchaseManager.findPartByName(partToRemoveName);
                        if (partToRemove != null) {
                            warehouse.removePartFromProduct(productForPartRemove, partToRemove);
                            System.out.println("Parça başarıyla silindi.");
                        } else {
                            System.out.println("Parça bulunamadı.");
                        }
                    } else {
                        System.out.println("Ürün bulunamadı.");
                    }
                    break;

                case 7:
                    // Depo durumunu görüntüleme
                    warehouse.listProducts();
                    warehouse.listParts();
                    break;

                case 8:
                    // Depo verilerini kaydetme
                    System.out.print("Verilerin kaydedileceği dosya adı: ");
                    String saveFileName = scanner.nextLine();
                    warehouse.saveToFile(saveFileName);
                    break;

                case 9:
                    // Depo verilerini yükleme
                    System.out.print("Yüklenecek dosya adı: ");
                    String loadFileName = scanner.nextLine();
                    warehouse.loadFromFile(loadFileName);
                    break;

                case 10:
                    // Çıkış
                    exit = true;
                    System.out.println("Uygulama kapatılıyor. Hoşça kalın!");
                    break;

                default:
                    System.out.println("Geçersiz seçim. Lütfen tekrar deneyin.");
            }
        }

        scanner.close();
    }
}
