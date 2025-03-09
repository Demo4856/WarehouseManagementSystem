package com.example.java1.backend;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WarehouseDataLoader {

    public static List<Warehouse> loadWarehouses(String warehouseFile) throws IOException {
        List<Warehouse> warehouses = new ArrayList<>();

        Map<Integer, Part> parts = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(warehouseFile))) {
            // Başlık satırını atla
            reader.readLine();

            String line;
            while ((line = reader.readLine()) != null) {
                String name = line.trim(); // Sadece name sütunu
                if (!name.isEmpty()) {
                    warehouses.add(new Warehouse(name)); // Listeye ekle
                }
            }
            return warehouses;
        }
    }


}
