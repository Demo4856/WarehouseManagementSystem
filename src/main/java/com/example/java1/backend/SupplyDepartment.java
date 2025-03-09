package com.example.java1.backend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SupplyDepartment {
    private List<Part> availableParts;   // Mevcut parçalar
    private List<Product> availableProducts; // Mevcut ürünler

    // Constructor
    public SupplyDepartment() {
        this.availableParts = new ArrayList<>();
        this.availableProducts = new ArrayList<>();
    }

    // Parça Ekleme (Stok Arttırma)
    public void addPartToStock(Part part) {
        availableParts.add(part);
        System.out.println(part.getName() + " stoğa eklendi.");
    }

    // Ürün Ekleme (Stok Arttırma)
    public void addProductToStock(Product product) {
        availableProducts.add(product);
        System.out.println(product.getName() + " stoğa eklendi.");
    }

    // Parça Çıkarma (Stok Azaltma)
    public void removePartFromStock(Part part) {
        if (availableParts.contains(part)) {
            availableParts.remove(part);
            System.out.println(part.getName() + " stoktan çıkarıldı.");
        } else {
            System.out.println(part.getName() + " stokta bulunamadı.");
        }
    }

    // Ürün Çıkarma (Stok Azaltma)
    public void removeProductFromStock(Product product) {
        if (availableProducts.contains(product)) {
            availableProducts.remove(product);
            System.out.println(product.getName() + " stoktan çıkarıldı.");
        } else {
            System.out.println(product.getName() + " stokta bulunamadı.");
        }
    }

    // Mevcut Parçaları Listeleme
    public void listAvailableParts() {
        if (availableParts.isEmpty()) {
            System.out.println("Stokta mevcut parça bulunmamaktadır.");
        } else {
            System.out.println("Mevcut Parçalar:");
            for (Part part : availableParts) {
                System.out.println(part);
            }
        }
    }

    // Mevcut Ürünleri Listeleme
    public void listAvailableProducts() {
        if (availableProducts.isEmpty()) {
            System.out.println("Stokta mevcut ürün bulunmamaktadır.");
        } else {
            System.out.println("Mevcut Ürünler:");
            for (Product product : availableProducts) {
                System.out.println(product);
            }
        }
    }

    // Parçaları Dosyaya Yazma
    public void writePartsToFile(String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Part part : availableParts) {
                bw.write(part.getId() + "," + part.getName() + "," + part.getQuantity() + "," + part.getPrice() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Dosya yazma hatası: " + e.getMessage());
        }
    }

    // Ürünleri Dosyaya Yazma
    public void writeProductsToFile(String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Product product : availableProducts) {
                bw.write(product.getId() + "," + product.getName() + "," + product.getPrice() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Dosya yazma hatası: " + e.getMessage());
        }
    }

    // Parçaları Dosyadan Okuma
    public void readPartsFromFile(String fileName) {
        availableParts.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 4) {
                    Part part = new Part(Integer.parseInt(tokens[0]), tokens[1], Integer.parseInt(tokens[2]), Double.parseDouble(tokens[3]));
                    availableParts.add(part);
                }
            }
        } catch (IOException e) {
            System.out.println("Dosya okuma hatası: " + e.getMessage());
        }
    }

    // Ürünleri Dosyadan Okuma
    public void readProductsFromFile(String fileName) {
        availableProducts.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 3) {
                    Product product = new Product(Integer.parseInt(tokens[0]), tokens[1], Double.parseDouble(tokens[2]));
                    availableProducts.add(product);
                }
            }
        } catch (IOException e) {
            System.out.println("Dosya okuma hatası: " + e.getMessage());
        }
    }

    // Overriding (toString Metodu)
    @Override
    public String toString() {
        return "Mevcut Ürün Sayısı: " + availableProducts.size() +
                ", Mevcut Parça Sayısı: " + availableParts.size();
    }
}
