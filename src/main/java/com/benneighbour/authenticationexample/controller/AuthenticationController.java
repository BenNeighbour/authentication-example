package com.benneighbour.authenticationexample.controller;

import com.benneighbour.authenticationexample.dao.UserDao;
import com.benneighbour.authenticationexample.model.LoginResponse;
import com.benneighbour.authenticationexample.model.User;
import com.benneighbour.authenticationexample.service.UserService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/users")
public class AuthenticationController {

    private final UserService userService;
    private final UserDao userDao;

    public AuthenticationController(UserService userService, UserDao userDao) {
        this.userService = userService;
        this.userDao = userDao;
    }

    @PostMapping("/login")
    public LoginResponse login(
                        @RequestParam String username,
                        @RequestParam String password) {
        return userService.login(username, password);
    }

    @PostMapping("/signup")
    public LoginResponse signup(@RequestBody User user) {
        return userService.signup(user);
    }

    @DeleteMapping(value = "/{username}")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String delete(@PathVariable String username) {
        userDao.delete(userDao.findByUsername(username));
        return username;
    }

    @GetMapping(value = "/me")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public User whoAmI(HttpServletRequest request) {
        return userService.whoAmI(request);
    }

    @GetMapping("/refresh")
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_CLIENT')")
    public LoginResponse refresh(HttpServletRequest req) {
        return userService.refreshToken(req.getRemoteUser());
    }

}
