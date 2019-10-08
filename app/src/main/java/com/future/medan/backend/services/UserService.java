package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAll();

    Optional<User> getById(String id);

    User save(User user);

    void deleteById(String id);
}
