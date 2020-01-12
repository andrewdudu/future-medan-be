package com.future.medan.backend.controllers;

import com.future.medan.backend.constants.ApiPath;
import com.future.medan.backend.exceptions.AppException;
import com.future.medan.backend.exceptions.AuthenticationFailException;
import com.future.medan.backend.models.entity.Role;
import com.future.medan.backend.models.entity.User;
import com.future.medan.backend.models.enums.RoleEnum;
import com.future.medan.backend.payload.responses.*;
import com.future.medan.backend.payload.requests.LoginWebRequest;
import com.future.medan.backend.payload.requests.SignUpWebRequest;
import com.future.medan.backend.repositories.RoleRepository;
import com.future.medan.backend.repositories.UserRepository;
import com.future.medan.backend.security.JwtTokenProvider;
import com.future.medan.backend.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import javax.annotation.security.RolesAllowed;
import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;

@RestController
public class AuthenticationController {

    private AuthenticationManager authenticationManager;

    private UserRepository userRepository;

    private RoleRepository roleRepository;

    private PasswordEncoder passwordEncoder;

    private JwtTokenProvider tokenProvider;

    @Autowired
    public AuthenticationController(AuthenticationManager authenticationManager,
                                    UserRepository userRepository,
                                    RoleRepository roleRepository,
                                    PasswordEncoder passwordEncoder,
                                    JwtTokenProvider jwtTokenProvider) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
        this.tokenProvider = jwtTokenProvider;
    }

    @PostMapping(ApiPath.LOGIN)
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginWebRequest loginWebRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginWebRequest.getUsernameOrEmail(),
                        loginWebRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();

        if (!userPrincipal.getStatus()) throw new AuthenticationFailException("User has been blocked");

        String jwt = tokenProvider.generateToken(authentication);
        return ResponseEntity.ok(new JwtAuthenticationResponse(
                userPrincipal.getName(),
                userPrincipal.getUsername(),
                userPrincipal.getEmail(),
                jwt,
                userPrincipal.getAuthorities())
        );
    }

    @PostMapping(ApiPath.VALIDATE_ADMIN_TOKEN)
    @RolesAllowed("ROLE_ADMIN")
    public Response<AuthenticationApiResponse> validateAdminToken(@RequestBody String token) {
        return ResponseHelper.ok(WebResponseConstructor.toValidateToken(true));
    }

    @PostMapping(ApiPath.VALIDATE_USER_TOKEN)
    @RolesAllowed("ROLE_USER")
    public Response<AuthenticationApiResponse> validateUserToken(@RequestBody String token) {
        return ResponseHelper.ok(WebResponseConstructor.toValidateToken(true));
    }

    @PostMapping(ApiPath.VALIDATE_MERCHANT_TOKEN)
    @RolesAllowed("ROLE_MERCHANT")
    public Response<AuthenticationApiResponse> validateMerchantToken(@RequestBody String token) {
        return ResponseHelper.ok(WebResponseConstructor.toValidateToken(true));
    }

    @PostMapping(ApiPath.MERCHANT_REGISTER)
    public ResponseEntity<?> registerMerchant(@Valid @RequestBody SignUpWebRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new AuthenticationApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new AuthenticationApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        return createUser(signUpRequest, RoleEnum.ROLE_MERCHANT);
    }

    @PostMapping(ApiPath.USER_REGISTER)
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignUpWebRequest signUpRequest) {
        if(userRepository.existsByUsername(signUpRequest.getUsername())) {
            return new ResponseEntity<>(new AuthenticationApiResponse(false, "Username is already taken!"),
                    HttpStatus.BAD_REQUEST);
        }

        if(userRepository.existsByEmail(signUpRequest.getEmail())) {
            return new ResponseEntity<>(new AuthenticationApiResponse(false, "Email Address already in use!"),
                    HttpStatus.BAD_REQUEST);
        }

        return createUser(signUpRequest, RoleEnum.ROLE_USER);
    }

    private ResponseEntity<?> createUser(@RequestBody @Valid SignUpWebRequest signUpRequest, RoleEnum role) {
        User user = new User(signUpRequest.getName(), signUpRequest.getUsername(),
                signUpRequest.getEmail(), signUpRequest.getPassword());

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        Role userRole = roleRepository.findByName(role)
                .orElseThrow(() -> new AppException("User Role not set."));

        user.setRoles(Collections.singleton(userRole));
        user.setStatus(true);

        User result = userRepository.save(user);

        URI location = ServletUriComponentsBuilder
                .fromCurrentContextPath().path("/api/users/{username}")
                .buildAndExpand(result.getUsername()).toUri();

        return ResponseEntity.created(location).body(new AuthenticationApiResponse(true, "User registered successfully"));
    }
}
