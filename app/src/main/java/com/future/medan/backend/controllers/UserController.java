package com.future.medan.backend.controllers;

import com.future.medan.backend.exception.ResourceNotFoundException;
import com.future.medan.backend.models.constants.ApiPath;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.responses.ResponseHelper;
import com.future.medan.backend.responses.Response;
import com.future.medan.backend.responses.UserWebResponse;
import com.future.medan.backend.responses.WebResponseConstructor;
import com.future.medan.backend.services.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;
import java.util.stream.Collectors;

@Api
@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController (UserService userService){
        this.userService = userService;
    }

    @GetMapping(ApiPath.USERS)
    public Response getAll(){
        return ResponseHelper.ok(userService.getAll()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping(ApiPath.USER_BY_USER_ID)
    public com.future.medan.backend.responses.Response getById(@PathVariable String id) {
        Optional<User> user = userService.getById(id);

        if (!user.isPresent())
            throw new ResourceNotFoundException("User", "id", id);

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(user.get()));
    }

    @PostMapping(value = ApiPath.USERS, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response save(@RequestBody User user) {
        return ResponseHelper.ok(userService.save(user));
    }

    @PutMapping(value = ApiPath.USER_BY_USER_ID, produces =  MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response editById(@RequestBody User user, @PathVariable String id){
        Optional<User> findUser = userService.getById(id);

        if (!findUser.isPresent())
            throw new ResourceNotFoundException("User", "id", id);

        user.setId(id);
        return ResponseHelper.ok(userService.save(user));
    }

    @DeleteMapping(value = ApiPath.USER_BY_USER_ID)
    public void deleteById(@PathVariable String id){
        Optional<User> user = userService.getById(id);

        if (!user.isPresent())
            throw new ResourceNotFoundException("User", "id", id);

        userService.deleteById(id);
    }
}
