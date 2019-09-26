package com.future.medan.backend.repositories;

import com.future.medan.backend.models.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User getById(String id);

    User editById(String id);
}
