package com.example.java1.backend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

public class Warehouse implements Manufacturer {
    private static final Logger LOGGER = Logger.getLogger(Warehouse.class.getName());
    private String name; // Depo adı
    private int id; // Depo ID
    private List<Product> products; // Depodaki ürünler
    private List<Part> parts; // Depodaki parçalar
    private List<Product> producedProducts; // Üretilen ürünler
    private List<Part> producedParts; // Üretilen parçalar

    // Constructor
    public Warehouse(String name) {
        this.id = id;
        this.name = name;
        this.products = new ArrayList<>();
        this.parts = new ArrayList<>();
        this.producedProducts = new ArrayList<>();
        this.producedParts = new ArrayList<>();
    }

    // Depoya ürün ekleme
    public void addProduct(Product product) {
        products.add(product);
        System.out.println(product.getName() + " depoya eklendi.");
    }

    // Depodan ürün silme
    public void removeProduct(Product product) {
        if (products.contains(product)) {
            products.remove(product);
            System.out.println(product.getName() + " depodan silindi.");
        } else {
            System.out.println(product.getName() + " depoda bulunamadı.");
        }
    }

    // Depoya parça ekleme
    public void addPart(Part part) {
        parts.add(part);
        System.out.println(part.getName() + " depoya eklendi.");
    }

    // Depodan parça silme
    public void removePart(Part part) {
        if (parts.contains(part)) {
            parts.remove(part);
            System.out.println(part.getName() + " depodan silindi.");
        } else {
            System.out.println(part.getName() + " depoda bulunamadı.");
        }
    }

    // Ürüne parça ekleme
    public void addPartToProduct(Product product, Part part) {
        if (products.contains(product)) {
            product.addPart(part);
            System.out.println(part.getName() + " " + product.getName() + " ürününe eklendi.");
        } else {
            System.out.println(product.getName() + " depoda bulunamadı.");
        }
    }

    // Üründen parça silme
    public void removePartFromProduct(Product product, Part part) {
        if (products.contains(product)) {
            product.removePart(part);
            System.out.println(part.getName() + " " + product.getName() + " ürününden silindi.");
        } else {
            System.out.println(product.getName() + " depoda bulunamadı.");
        }
    }

    // Depodaki ürünleri listeleme
    public void listProducts() {
        if (products.isEmpty()) {
            System.out.println("Depoda ürün bulunmamaktadır.");
        } else {
            System.out.println("Depodaki Ürünler:");
            for (Product product : products) {
                System.out.println(product);
            }
        }
    }

    // Depodaki parçaları listeleme
    public void listParts() {
        if (parts.isEmpty()) {
            System.out.println("Depoda parça bulunmamaktadır.");
        } else {
            System.out.println("Depodaki Parçalar:");
            for (Part part : parts) {
                System.out.println(part);
            }
        }
    }

    // Ürünleri Getirme
    public List<Product> getProducts() {
        return products;
    }

    // Parçaları Getirme
    public List<Part> getParts() {
        return parts;
    }

    // Depodaki verileri dosyaya kaydetme
    public void saveToFile(String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write("Depo Adı: " + name + "\n");
            writer.write("Ürünler:\n");
            for (Product product : products) {
                writer.write(product.toString() + "\n");
            }
            writer.write("Parçalar:\n");
            for (Part part : parts) {
                writer.write(part.toString() + "\n");
            }
            System.out.println("Depo verileri " + fileName + " dosyasına kaydedildi.");
        } catch (IOException e) {
            System.out.println("Dosyaya yazarken hata oluştu: " + e.getMessage());
        }
    }

    // Dosyadan veri yükleme
    public void loadFromFile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            products.clear();
            parts.clear();
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Ürünler:")) {
                    while ((line = reader.readLine()) != null && !line.startsWith("Parçalar:")) {
                        // Ürünleri listeye ekle
                        String[] tokens = line.split(",");
                        if (tokens.length == 3) {
                            Product product = new Product(Integer.parseInt(tokens[0]), tokens[1], Double.parseDouble(tokens[2]));
                            products.add(product);
                        }
                    }
                }
                if (line != null && line.startsWith("Parçalar:")) {
                    while ((line = reader.readLine()) != null) {
                        // Parçaları listeye ekle
                        String[] tokens = line.split(",");
                        if (tokens.length == 4) {
                            Part part = new Part(Integer.parseInt(tokens[0]), tokens[1], Integer.parseInt(tokens[2]), Double.parseDouble(tokens[3]));
                            parts.add(part);
                        }
                    }
                }
            }
            System.out.println("Depo verileri " + fileName + " dosyasından yüklendi.");
        } catch (IOException e) {
            System.out.println("Dosyadan okurken hata oluştu: " + e.getMessage());
        }
    }

    // Ürün üretme
    @Override
    public void produceProduct(Product product) {
        producedProducts.add(product);
        System.out.println("Ürün üretildi: " + product.getName());
    }

    // Parça üretme
    @Override
    public void producePart(Part part) {
        producedParts.add(part);
        System.out.println("Parça üretildi: " + part.getName());
    }

    // Üretilen ürünlerin listesi
    @Override
    public List<Product> getProducedProducts() {
        return producedProducts;
    }

    // Üretilen parçaların listesi
    @Override
    public List<Part> getProducedParts() {
        return producedParts;
    }

    // Ürün veya parça ile ilgili işlem gerçekleştirme
    @Override
    public void processItem(String itemName) {
        System.out.println("İşlem yapılıyor: " + itemName);
    }

    // Üretim raporu oluşturma
    @Override
    public String generateProductionReport() {
        StringBuilder report = new StringBuilder();
        report.append("Üretim Raporu:\n");
        report.append("Üretilen Ürünler:\n");
        for (Product product : producedProducts) {
            report.append(product.toString()).append("\n");
        }
        report.append("Üretilen Parçalar:\n");
        for (Part part : producedParts) {
            report.append(part.toString()).append("\n");
        }
        return report.toString();
    }

    // Overriding (toString)
    @Override
    public String toString() {
        return "Depo Adı: " + name + ", Ürün Sayısı: " + products.size() + ", Parça Sayısı: " + parts.size();
    }
    public String getName() {
        return name;
    }

}
