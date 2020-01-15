package com.future.medan.backend.services;

import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.payload.requests.UserWebRequest;
import org.springframework.data.domain.Page;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> getAll();

    Page<User> findPaginatedUser(int page, int size);

    Page<User> findPaginatedMerchant(int page, int size);

    User getById(String id);

    User getMerchantById(String id);

    User save(User user);

    User save(UserWebRequest userWebRequest, String id) throws IOException;

    User block(String id);

    boolean requestPasswordReset(String email);

    boolean resetPassword(String token, String password);

    void deleteById(String id);
}
