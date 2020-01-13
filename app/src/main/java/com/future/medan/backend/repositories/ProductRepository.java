package com.future.medan.backend.repositories;

import com.future.medan.backend.models.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;
import java.util.Set;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {

    Set<Product> findByIdIn(Collection<String> ids);

    List<Product> getAllByHiddenIs(Boolean hidden);

    List<Product> getByCategoryIdAndHiddenIs(String categoryId, Boolean hidden);

    List<Product> getByMerchantId(String merchantId);

    @Query(value = "SELECT * FROM products p " +
            "WHERE LOWER(p.name) LIKE %:term%", nativeQuery = true)
    List<Product> searchProduct(String term);
}
