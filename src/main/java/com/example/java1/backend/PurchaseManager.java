package com.example.java1.backend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class PurchaseManager {
    private List<Product> products;  // Satın alınan ürünlerin listesi
    private List<Part> parts;        // Satın alınan parçaların listesi

    // Default Constructor
    public PurchaseManager() {
        this.products = new ArrayList<>();
        this.parts = new ArrayList<>();
    }

    // Ürün Satın Alma (Overloading - Ürün Adıyla)
    public void purchaseProduct(String productName, double price) {
        Product product = new Product(products.size() + 1, productName, price);
        products.add(product);
        System.out.println(productName + " satın alındı ve listeye eklendi.");
    }

    // Ürün Satın Alma (Overloading - Ürün Nesnesiyle)
    public void purchaseProduct(Product product) {
        products.add(product);
        System.out.println(product.getName() + " satın alındı ve listeye eklendi.");
    }

    // Parça Satın Alma
    public void purchasePart(Part part) {
        parts.add(part);
        System.out.println(part.getName() + " parçası satın alındı ve listeye eklendi.");
    }

    // Toplam Satın Alınan Ürün Sayısını Döndürme
    public int getTotalProductsPurchased() {
        return products.size();
    }

    // Toplam Satın Alınan Parça Sayısını Döndürme
    public int getTotalPartsPurchased() {
        return parts.size();
    }

    // Satın Alınan Ürünleri Listeleme
    public void listPurchasedProducts() {
        if (products.isEmpty()) {
            System.out.println("Satın alınmış ürün bulunmamaktadır.");
        } else {
            System.out.println("Satın Alınan Ürünler:");
            for (Product product : products) {
                System.out.println(product);
            }
        }
    }

    // Satın Alınan Parçaları Listeleme
    public void listPurchasedParts() {
        if (parts.isEmpty()) {
            System.out.println("Satın alınmış parça bulunmamaktadır.");
        } else {
            System.out.println("Satın Alınan Parçalar:");
            for (Part part : parts) {
                System.out.println(part);
            }
        }
    }

    // Ürünleri Dosyaya Yazma
    public void writeProductsToFile(String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Product product : products) {
                bw.write(product.getId() + "," + product.getName() + "," + product.getPrice() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Dosya yazma hatası: " + e.getMessage());
        }
    }

    // Parçaları Dosyaya Yazma
    public void writePartsToFile(String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName))) {
            for (Part part : parts) {
                bw.write(part.getId() + "," + part.getName() + "," + part.getQuantity() + "," + part.getPrice() + "\n");
            }
        } catch (IOException e) {
            System.out.println("Dosya yazma hatası: " + e.getMessage());
        }
    }

    // Ürünleri Dosyadan Okuma
    public void readProductsFromFile(String fileName) {
        products.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 3) {
                    Product product = new Product(Integer.parseInt(tokens[0]), tokens[1], Double.parseDouble(tokens[2]));
                    products.add(product);
                }
            }
        } catch (IOException e) {
            System.out.println("Dosya okuma hatası: " + e.getMessage());
        }
    }

    // Parçaları Dosyadan Okuma
    public void readPartsFromFile(String fileName) {
        parts.clear();
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 4) {
                    Part part = new Part(Integer.parseInt(tokens[0]), tokens[1], Integer.parseInt(tokens[2]), Double.parseDouble(tokens[3]));
                    parts.add(part);
                }
            }
        } catch (IOException e) {
            System.out.println("Dosya okuma hatası: " + e.getMessage());
        }
    }

    // Ürün Adı ile Ürün Bulma
    public Product findProductByName(String productName) {
        for (Product product : products) {
            if (product.getName().equalsIgnoreCase(productName)) {
                return product;
            }
        }
        return null;
    }

    // Parça Adı ile Parça Bulma
    public Part findPartByName(String partName) {
        for (Part part : parts) {
            if (part.getName().equalsIgnoreCase(partName)) {
                return part;
            }
        }
        return null;
    }

    // Overriding (toString Metodu)
    @Override
    public String toString() {
        return "Toplam Satın Alınan Ürün Sayısı: " + getTotalProductsPurchased() +
                ", Toplam Satın Alınan Parça Sayısı: " + getTotalPartsPurchased();
    }
}
