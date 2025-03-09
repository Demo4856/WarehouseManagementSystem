package com.example.java1.backend;

import java.io.*;

public class Part extends CommonEntity {
    private int quantity;

    public Part() {
        super();
        this.quantity = 0;
    }

    public Part(int id, String name, int quantity, double price) {
        super(id, name, price);
        this.quantity = quantity;
    }

    public Part(int id, String name) {
        this(id, name, 0, 0.0); // Default values for stock and price
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void updateStock(int amount) {
        if (this.quantity + amount >= 0) {
            this.quantity += amount;
        } else {
            System.out.println("Hata: Stok yetersiz!");
        }
    }

    @Override
    public void writeToFile(String fileName) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(fileName, true))) {
            bw.write(this.id + "," + this.name + "," + this.quantity + "," + this.price + "\n");
        } catch (IOException e) {
            System.out.println("Dosya yazma hatası: " + e.getMessage());
        }
    }

    public static Part readFromFile(String fileName, int id) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length == 4 && Integer.parseInt(tokens[0]) == id) {
                    return new Part(Integer.parseInt(tokens[0]), tokens[1], Integer.parseInt(tokens[2]), Double.parseDouble(tokens[3]));
                }
            }
        } catch (IOException e) {
            System.out.println("Dosya okuma hatası: " + e.getMessage());
        }
        return null;
    }

    @Override
    public String toString() {
        return super.toString() + ", Stok: " + quantity;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }


}
