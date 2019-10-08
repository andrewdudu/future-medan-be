package com.future.medan.backend.repositories;

import com.future.medan.backend.models.entity.Role;
import com.future.medan.backend.models.enums.RoleEnum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, String> {

    Optional<Role> findByName(RoleEnum roleName);
}
