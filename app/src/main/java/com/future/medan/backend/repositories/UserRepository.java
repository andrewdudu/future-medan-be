package com.future.medan.backend.repositories;

import com.future.medan.backend.models.entity.Role;
import com.future.medan.backend.models.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

    User findByEmail(String email);

    Optional<User> findByUsernameOrEmail(String username, String email);

    Page<User> findAllByRoles(Role role, Pageable paging);

    List<User> findByIdIn(List<Long> userIds);

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);
}
