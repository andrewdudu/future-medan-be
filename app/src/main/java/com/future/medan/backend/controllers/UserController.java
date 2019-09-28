package com.future.medan.backend.controllers;

import com.future.medan.backend.models.constants.ApiPath;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.services.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Api
@RestController
@RequestMapping(produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {

    private UserService userService;

    @Autowired
    public UserController (UserService userService){
        this.userService = userService;
    }

    @GetMapping(ApiPath.USERS)
    public List<User> getAll(){
        return userService.getAll();
    }

    @GetMapping(ApiPath.USER_BY_USER_ID)
    public User getOne(@PathVariable String id){
        return userService.getOne(id);
    }

    @PostMapping(value = ApiPath.USERS, consumes = MediaType.APPLICATION_JSON_VALUE)
    public User save(@RequestBody User user) throws Exception {
        return userService.save(user);
    }

    @DeleteMapping(ApiPath.USER_BY_USER_ID)
    public void deleteById(@PathVariable String id) { userService.deleteById(id); }
}
