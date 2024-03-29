package com.future.medan.backend.controllers;

import com.future.medan.backend.models.entity.Product;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.payload.requests.ForgotPasswordWebRequest;
import com.future.medan.backend.payload.requests.ResetPasswordWebRequest;
import com.future.medan.backend.payload.requests.WebRequestConstructor;
import com.future.medan.backend.payload.responses.*;
import com.future.medan.backend.services.ProductService;
import com.future.medan.backend.services.UserService;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.RolesAllowed;
import java.util.List;
import java.util.stream.Collectors;

@Api
@RestController
public class UserController {

    private UserService userService;

    private ProductService productService;

    @Autowired
    public UserController (
            UserService userService,
            ProductService productService){
        this.userService = userService;
        this.productService = productService;
    }

    @GetMapping("/api/users")
    @RolesAllowed("ROLE_ADMIN")
    public Response<List<UserWebResponse>> getAll(){
        return ResponseHelper.ok(userService.getAll()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()));
    }

    @GetMapping("/api/users/paginate")
    @RolesAllowed("ROLE_ADMIN")
    public PaginationResponse<List<UserWebResponse>> findPaginatedUser(@RequestParam("page") final int page, @RequestParam("size") final int size) {
        Page<User> resultPage = userService.findPaginatedUser(page, size);

        return ResponseHelper.ok(resultPage.getContent()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()), resultPage.getTotalElements(), resultPage.getTotalPages());
    }

    @GetMapping("/api/merchants/paginate")
    @RolesAllowed("ROLE_ADMIN")
    public PaginationResponse<List<UserWebResponse>> findPaginatedMerchant(@RequestParam("page") final int page, @RequestParam("size") final int size) {
        Page<User> resultPage = userService.findPaginatedMerchant(page, size);

        return ResponseHelper.ok(resultPage.getContent()
                .stream()
                .map(WebResponseConstructor::toWebResponse)
                .collect(Collectors.toList()), resultPage.getTotalElements(), resultPage.getTotalPages());
    }

    @GetMapping("/api/users/{id}")
    public Response<UserWebResponse> getById(@PathVariable String id) {
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(userService.getById(id)));
    }

    @GetMapping("/api/merchant/{id}")
    public Response<MerchantWebResponse> getByMerchantId(@PathVariable String id) {
        User merchant = userService.getMerchantById(id);
        List<Product> products = productService.getByMerchantId(id);

        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(merchant, products));
    }

    @PostMapping(value = "/api/users", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<UserWebResponse> save(@RequestBody User user) {
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(userService.save(user)));
    }

    @PostMapping(value = "/api/users/{id}", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<UserWebResponse> blockUser(@PathVariable String id) {
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(userService.block(id)));
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

    @PutMapping(value = "/api/users/{id}", produces =  MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
    public Response<UserWebResponse> editById(@RequestBody User user, @PathVariable String id){
        user.setId(id);
        return ResponseHelper.ok(WebResponseConstructor.toWebResponse(userService.save(user, id)));
    }

    @DeleteMapping(value = "/api/users/{id}")
    public void deleteById(@PathVariable String id){
        userService.deleteById(id);
    }
}
