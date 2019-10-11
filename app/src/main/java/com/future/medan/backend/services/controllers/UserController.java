package com.future.medan.backend.services.controllers;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.constants.ApiPath;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.payload.responses.ResponseHelper;
import com.future.medan.backend.payload.responses.Response;
import com.future.medan.backend.payload.responses.UserWebResponse;
import com.future.medan.backend.payload.responses.WebResponseConstructor;
import com.future.medan.backend.services.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;
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

    @GetMapping("/users")
    public Response<List<UserWebResponse>> getAll(){
        return ResponseHelper.ok(userService.getAll()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/users/{id}")
    public Response<UserWebResponse> getById(@PathVariable String id) {
        Optional<User> user = userService.getById(id);

        if (!user.isPresent())
            throw new ResourceNotFoundException("User", "id", id);

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(user.get()));
    }

    @PostMapping(value = "/users", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<UserWebResponse> save(@RequestBody User user) {
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(userService.save(user)));
    }

    @PutMapping(value = "/users/{id}", produces =  MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<UserWebResponse> editById(@RequestBody User user, @PathVariable String id){
        Optional<User> findUser = userService.getById(id);

        if (!findUser.isPresent())
            throw new ResourceNotFoundException("User", "id", id);

        user.setId(id);
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(userService.save(user)));
    }

    @DeleteMapping(value = "/users/{id}")
    public void deleteById(@PathVariable String id){
        Optional<User> user = userService.getById(id);

        if (!user.isPresent())
            throw new ResourceNotFoundException("User", "id", id);

        userService.deleteById(id);
    }
}
