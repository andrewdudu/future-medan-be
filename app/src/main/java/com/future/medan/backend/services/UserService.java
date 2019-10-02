package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.User;

import java.util.List;

public interface UserService {

    List<User> getAll();

    User save(User user);
}
