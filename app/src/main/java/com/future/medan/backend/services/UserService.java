package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.User;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAll();

    Page<User> findPaginatedUser(int page, int size);

    Page<User> findPaginatedMerchant(int page, int size);

    User getById(String id);

    User save(User user);

    User save(User user, String id);

    User block(String id);

    boolean requestPasswordReset(String email);

    boolean resetPassword(String token, String password);

    void deleteById(String id);
}
