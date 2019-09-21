package com.future.medan.backend.repositories;

import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, String> {
}
