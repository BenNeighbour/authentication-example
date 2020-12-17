package com.benneighbour.authenticationexample.service;

import com.benneighbour.authenticationexample.model.LoginResponse;
import com.benneighbour.authenticationexample.model.User;

import javax.servlet.http.HttpServletRequest;

public interface UserService {

    LoginResponse login(String username, String password);

    LoginResponse signup(User user);

    User whoAmI(HttpServletRequest request);

    LoginResponse refreshToken(String username);

}
