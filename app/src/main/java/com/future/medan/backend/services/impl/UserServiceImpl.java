package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.PasswordResetToken;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.repositories.PasswordResetTokenRepository;
import com.future.medan.backend.repositories.UserRepository;
import com.future.medan.backend.security.JwtTokenProvider;
import com.future.medan.backend.services.MailService;
import com.future.medan.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import sun.plugin.com.Utils;

import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {

    private UserRepository userRepository;

    private JwtTokenProvider jwtTokenProvider;

    private PasswordResetTokenRepository passwordResetTokenRepository;

    private MailService mailService;

    private PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(
            UserRepository userRepository,
            JwtTokenProvider jwtTokenProvider,
            PasswordResetTokenRepository passwordResetTokenRepository,
            MailService mailService,
            PasswordEncoder passwordEncoder){
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordResetTokenRepository = passwordResetTokenRepository;
        this.mailService = mailService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public List<User> getAll(){
        return userRepository.findAll();
    }

    @Override
    public User getById(String id){
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User", "id", id));
    }

    @Override
    public User save(User user, String id){
        if (!userRepository.existsById(id))
            throw new ResourceNotFoundException("User", "id", id);
        else
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
            return false;
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
