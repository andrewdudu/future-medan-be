package com.future.medan.backend.controllers;

import com.future.medan.backend.exceptions.ResourceNotFoundException;
import com.future.medan.backend.constants.ApiPath;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.payload.requests.ForgotPasswordWebRequest;
import com.future.medan.backend.payload.requests.ResetPasswordWebRequest;
import com.future.medan.backend.payload.responses.*;
import com.future.medan.backend.services.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Api
@RestController
public class UserController {

    private UserService userService;

    @Autowired
    public UserController (
            UserService userService,
            PasswordEncoder passwordEncoder){
        this.userService = userService;
    }

    @GetMapping(ApiPath.USERS)
    public Response<List<UserWebResponse>> getAll(){
        return ResponseHelper.ok(userService.getAll()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping(ApiPath.USER_BY_USER_ID)
    public Response<UserWebResponse> getById(@PathVariable String id) {
        Optional<User> user = userService.getById(id);

        if (!user.isPresent())
            throw new ResourceNotFoundException("User", "id", id);

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(user.get()));
    }

    @PostMapping(value = ApiPath.USERS, produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<UserWebResponse> save(@RequestBody User user) {
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(userService.save(user)));
    }

    @PostMapping(
            path="/api/forgot-password",
            produces = MediaType.APPLICATION_JSON_VALUE,
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<ForgotPasswordWebResponse> forgotPassword(@RequestBody ForgotPasswordWebRequest passwordReqResetRequestModel) {
        return ResponseHelper.ok(WebResponseConstructor.toForgotPasswordWebResponse(userService.requestPasswordReset(passwordReqResetRequestModel.getEmail())));
    }

    @PostMapping(
            path = "/api/reset-password",
            consumes = MediaType.APPLICATION_JSON_VALUE
    )
    public Response<ResetPasswordWebResponse> resetPassword(@RequestBody ResetPasswordWebRequest passwordResetModel) {
        return ResponseHelper.ok(WebResponseConstructor.toResetPasswordWebResponse(userService.resetPassword(passwordResetModel.getToken(), passwordResetModel.getPassword())));
    }

    @PutMapping(value = ApiPath.USER_BY_USER_ID, produces =  MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<UserWebResponse> editById(@RequestBody User user, @PathVariable String id){
        Optional<User> findUser = userService.getById(id);

        if (!findUser.isPresent())
            throw new ResourceNotFoundException("User", "id", id);

        user.setId(id);
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(userService.save(user)));
    }

    @DeleteMapping(value = ApiPath.USER_BY_USER_ID)
    public void deleteById(@PathVariable String id){
        Optional<User> user = userService.getById(id);

        if (!user.isPresent())
            throw new ResourceNotFoundException("User", "id", id);

        userService.deleteById(id);
    }
}
