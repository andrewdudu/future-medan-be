package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.User;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAll();

    User getById(String id);

    User save(User user);

    User save(User user, String id);

    void deleteById(String id);
}
