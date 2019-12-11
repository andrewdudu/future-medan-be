package com.future.medan.backend.repositories;

import com.future.medan.backend.models.entity.PaymentMethod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentMethodRepository extends JpaRepository<PaymentMethod, String> {

    Boolean existsByNameAndType(String name, String type);
}
