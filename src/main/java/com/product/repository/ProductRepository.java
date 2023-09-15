package com.product.repository;

import com.product.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    @Query("Select (count(p.id) > 0) from Product p where p.productName= :name")
    boolean checkIfProductExistByName(@Param("name") String name);

    Product findByProductName(String name);
}
