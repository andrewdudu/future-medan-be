package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.PasswordResetToken;
import com.future.medan.backend.models.entity.Role;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.models.enums.RoleEnum;
import com.future.medan.backend.payload.requests.UserWebRequest;
import com.future.medan.backend.repositories.PasswordResetTokenRepository;
import com.future.medan.backend.repositories.RoleRepository;
import com.future.medan.backend.repositories.UserRepository;
import com.future.medan.backend.security.JwtTokenProvider;
import com.future.medan.backend.services.MailService;
import com.future.medan.backend.services.StorageService;
import com.future.medan.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private JwtTokenProvider jwtTokenProvider;

    private PasswordResetTokenRepository passwordResetTokenRepository;

    private MailService mailService;

    private StorageService storageService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            JwtTokenProvider jwtTokenProvider,
            PasswordResetTokenRepository passwordResetTokenRepository,
            MailService mailService,
            StorageService storageService,
            PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.mailService = mailService;
        this.storageService = storageService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAll(){
        return userRepository.findAll();
    }

    @Override
    public Page<User> findPaginatedUser(int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Role role = roleRepository.findByName(RoleEnum.ROLE_USER).orElseThrow(() -> new ResourceNotFoundException("Role", "name", "ROLE_USER"));

        return userRepository.findAllByRoles(role, paging);
    }

    @Override
    public Page<User> findPaginatedMerchant(int page, int size) {
        Pageable paging = PageRequest.of(page, size);
        Role role = roleRepository.findByName(RoleEnum.ROLE_MERCHANT).orElseThrow(() -> new ResourceNotFoundException("Role", "name", "ROLE_USER"));

        return userRepository.findAllByRoles(role, paging);
    }

    @Override
    public User getById(String id){
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Override
    @Transactional
    public User getMerchantById(String id) {
        User merchant = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Merchant", "id", id));

        if (!merchant.getRoles().equals(new HashSet<>(Collections.singletonList(new Role(RoleEnum.ROLE_MERCHANT))))) throw new ResourceNotFoundException("Merchant", "id", id);

        return merchant;
    }

    @Override
    public User save(UserWebRequest userWebRequest, String id) throws IOException {
        if (!userRepository.existsById(id))
            throw new ResourceNotFoundException("User", "id", id);
        else {
            User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));

            if (!userWebRequest.getImage().equals("")) {
                String imagePath = storageService.storeImage(userWebRequest.getImage(), UUID.randomUUID().toString());
                user.setImage(imagePath);
            }

            user.setDescription(userWebRequest.getDescription());
            user.setName(userWebRequest.getName());

            return userRepository.save(user);
        }
    }

    @Override
    public User block(String id) {
        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));;
        user.setStatus(!user.getStatus());

        return userRepository.save(user);
    }

    @Override
    public User save(User user){
        return userRepository.save(user);
    }

    @Override
    public boolean requestPasswordReset(String email) {
        User user = userRepository.findByEmail(email);

        if (user == null) {
            throw new ResourceNotFoundException("User", "email", email);
        }

        String token = jwtTokenProvider.generatePasswordResetToken(user.getId());

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setToken(token);
        passwordResetToken.setUser(user);
        passwordResetTokenRepository.save(passwordResetToken);

        mailService.sendPasswordResetMail(user.getEmail(), token);

        return true;
    }

    @Override
    public boolean resetPassword(String token, String password) {
        boolean returnValue = false;

        if (jwtTokenProvider.hasTokenExpired(token))
            return returnValue;

        PasswordResetToken passwordResetToken = passwordResetTokenRepository.findByToken(token);

        if (passwordResetToken == null)
            return returnValue;

        // Prepare new passwords
        String encodedPassword = passwordEncoder.encode(password);

        // Update user password in database
        User user = passwordResetToken.getUser();
        user.setPassword(encodedPassword);
        User savedUser = userRepository.save(user);

        // Verify if password was saved successfully
        if (savedUser != null && savedUser.getPassword().equalsIgnoreCase(encodedPassword)) {
            returnValue = true;
        }

        // Remove Password Reset Token from database
        passwordResetTokenRepository.delete(passwordResetToken);

        return returnValue;
    }

    @Override
    public void deleteById(String id) { userRepository.deleteById(id); }
}
