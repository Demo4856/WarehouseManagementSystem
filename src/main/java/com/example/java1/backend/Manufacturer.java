package com.example.java1.backend;

import java.util.List;

public interface Manufacturer {
    // Yeni bir ürün üretmek için
    void produceProduct(Product product);

    // Yeni bir parça üretmek için
    void producePart(Part part);

    // Üretilen ürünlerin listesi
    List<Product> getProducedProducts();

    // Üretilen parçaların listesi
    List<Part> getProducedParts();

    // Ürün veya parça ile ilgili bir işlem gerçekleştirme
    void processItem(String itemName);

    // Üretim sürecine ilişkin rapor oluşturma
    String generateProductionReport();
}
