package com.example.java1.backend;

import java.io.*;

public abstract class CommonEntity {
    protected int id;
    protected String name;
    protected double price;

    public CommonEntity() {
        this.id = 0;
        this.name = "Bilinmiyor";
        this.price = 0.0;
    }

    public CommonEntity(int id, String name, double price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    // Getter ve Setter Metotları
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    // Dosya yazma metodu (abstract olarak tanımlıyoruz ki her alt sınıf bunu kendine özgü şekilde uygulasın)
    public abstract void writeToFile(String fileName);

    // Dosya okuma metodu (static olarak tanımlıyoruz ki sınıf seviyesinde kullanılsın)
    public static CommonEntity readFromFile(String fileName, int id) {
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                if (tokens.length >= 3 && Integer.parseInt(tokens[0]) == id) {
                    return new Part(Integer.parseInt(tokens[0]), tokens[1], Integer.parseInt(tokens[2]), Double.parseDouble(tokens[3])); // Örnek olarak Part döndürülüyor
                }
            }
        } catch (IOException e) {
            System.out.println("Dosya okuma hatası: " + e.getMessage());
        }
        return null;
    }

    @Override
    public String toString() {
        return "ID: " + id + ", Adı: " + name + ", Fiyat: " + price;
    }
}
