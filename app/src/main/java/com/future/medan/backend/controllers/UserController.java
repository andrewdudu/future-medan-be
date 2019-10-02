package com.future.medan.backend.controllers;

import com.future.medan.backend.models.constants.ApiPath;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.responses.ResponseHelper;
import com.future.medan.backend.responses.WebResponseConstructor;
import com.future.medan.backend.services.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.xml.ws.Response;
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
        return (Response) ResponseHelper.ok(userService.getAll()
                .stream()
                .map(user -> WebResponseConstructor.toWebResponse(user))
                .collect(Collectors.toList()));
    }

    @PostMapping(value = ApiPath.USERS, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public User save(@RequestBody User user) throws Exception {
        return userService.save(user);
    }
}
