package com.future.medan.backend.services.impl;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.repositories.UserRepository;
import com.future.medan.backend.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserImpl implements UserService {

    private UserRepository userRepository;

    @Autowired
    public UserImpl(UserRepository userRepository){
        this.userRepository = userRepository;
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
    public User save(User user){
        return userRepository.save(user);
    }

    @Override
    public User save(User user, String id){
        if (!userRepository.existsById(id))
            throw new ResourceNotFoundException("User", "id", id);
        else
            return userRepository.save(user);
    }

    @Override
    public void deleteById(String id) { userRepository.deleteById(id); }
}
