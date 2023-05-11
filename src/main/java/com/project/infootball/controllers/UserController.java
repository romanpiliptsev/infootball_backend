package com.project.infootball.controllers;

import com.project.infootball.data_classes.JwtResponse;
import com.project.infootball.data_classes.LogInRequest;
import com.project.infootball.data_classes.RegistrationData;
import com.project.infootball.models.User;
import com.project.infootball.services.AuthService;
import com.project.infootball.services.UserService;
import org.jetbrains.annotations.Nullable;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private AuthService authService;

    @PostMapping("/login")
    @Nullable
    public JwtResponse login(@RequestBody LogInRequest loginRequest) {
        return authService.getJwtDtoByAuthRequest(loginRequest);
    }

    @PostMapping("/registration")
    public boolean register(@RequestBody RegistrationData registrationData) {
        return userService.addUser(registrationData);
    }

    @GetMapping("/check/{username}")
    @Nullable
    public User checkUsername(@PathVariable String username) {
        return userService.checkUsername(username);
    }

    @GetMapping("/get")
    @Nullable
    public User getUserInfo(HttpServletRequest request) {
        String username = authService.getUsernameByRequest(request);
        return userService.getUserInfo(username);
    }

    @PutMapping("/update-username/{newUsername}")
    public boolean updateUsername(HttpServletRequest request, @PathVariable String newUsername) {
        String username = authService.getUsernameByRequest(request);
        return userService.updateUsername(username, newUsername);
    }

    @PutMapping("/update-email/{email}")
    public boolean updateEmail(HttpServletRequest request, @PathVariable String email) {
        String username = authService.getUsernameByRequest(request);
        return userService.updateEmail(username, email);
    }

    @PutMapping("/update-password/{password}")
    public boolean updatePassword(HttpServletRequest request, @PathVariable String password) {
        String username = authService.getUsernameByRequest(request);
        return userService.updatePassword(username, password);
    }

    @PutMapping("/activate/{code}")
    public boolean activateUserAccount(@PathVariable String code) {
        return userService.activateUser(code);
    }

    @DeleteMapping("/remove")
    public boolean removeUser(HttpServletRequest request) {
        String username = authService.getUsernameByRequest(request);
        return userService.removeUser(username);
    }
}