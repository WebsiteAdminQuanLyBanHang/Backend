package edu.ctu.SpringShopBE.repository;

import edu.ctu.SpringShopBE.entity.ProductImage;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductImageRepository extends JpaRepository<ProductImage, Long> {
}