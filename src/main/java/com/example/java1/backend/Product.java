package com.example.java1.backend;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class Product extends CommonEntity {
    private List<Part> parts;

    public Product() {
        super();
        this.parts = new ArrayList<>();
    }

    public Product(int id, String name, double price) {
        super(id, name, price);
        this.parts = new ArrayList<>();
    }

    public List<Part> getParts() {
        return parts;
    }

    public void addPart(Part part) {
        this.parts.add(part);
    }

    public void removePart(Part part) {
        if (this.parts.contains(part)) {
            this.parts.remove(part);
        } else {
            System.out.println("Hata: Parça bulunamadı!");
        }
    }

    public void listParts() {
        if (this.parts.isEmpty()) {
            System.out.println("Bu ürüne bağlı parça bulunmamaktadır.");
        } else {
            System.out.println("Ürün: " + name + " için parçalar:");
            for (Part part : parts) {
                System.out.println(part);
            }
        }
    }

    @Override
    public void writeToFile(String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(this.id + "," + this.name + "," + this.price + "\n");
        } catch (IOException e) {
            System.out.println("Dosya yazma hatası: " + e.getMessage());
        }
    }

    public static Product readFromFile(String fileName, int id) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 3 && Integer.parseInt(tokens[0]) == id) {
                    return new Product(Integer.parseInt(tokens[0]), tokens[1], Double.parseDouble(tokens[2]));
                }
            }
        } catch (IOException e) {
            System.out.println("Dosya okuma hatası: " + e.getMessage());
        }
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + ", Parçalar: " + parts.size();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public double getPrice() {
        return price;
    }

}
