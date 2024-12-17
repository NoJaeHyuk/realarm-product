package com.realarmproduct.productNotification.repository;

import com.realarmproduct.productNotification.repository.entity.ProductEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<ProductEntity, Long> {
}
