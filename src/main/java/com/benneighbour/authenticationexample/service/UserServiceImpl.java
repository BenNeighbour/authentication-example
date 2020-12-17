package com.benneighbour.authenticationexample.service;

import com.benneighbour.authenticationexample.dao.UserDao;
import com.benneighbour.authenticationexample.model.LoginResponse;
import com.benneighbour.authenticationexample.model.User;
import com.benneighbour.authenticationexample.security.JwtTokenProvider;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;

@Service
public class UserServiceImpl implements UserService {

    private final UserDao userDao;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationManager authenticationManager;
    private final PasswordEncoder encoder;

    public UserServiceImpl(UserDao userDao, JwtTokenProvider jwtTokenProvider, AuthenticationManager authenticationManager, PasswordEncoder encoder) {
        this.userDao = userDao;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationManager = authenticationManager;
        this.encoder = encoder;
    }

    @Override
    public LoginResponse login(String username, String password) {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
            String token = jwtTokenProvider.createToken(username, userDao.findByUsername(username).getRoles());

            return new LoginResponse(token);
        } catch (AuthenticationException e) {
            throw new RuntimeException("Invalid username/password supplied");
        }
    }

    @Override
    public LoginResponse signup(User user) {
        if (!userDao.existsByUsername(user.getUsername())) {
            user.setPassword(encoder.encode(user.getPassword()));
            userDao.save(user);
            String token = jwtTokenProvider.createToken(user.getUsername(), user.getRoles());

            return new LoginResponse(token);
        } else {
            throw new RuntimeException("Username is already in use");
        }
    }

    @Override
    public User whoAmI(HttpServletRequest request) {
        return userDao.findByUsername(jwtTokenProvider.getUsername(jwtTokenProvider.resolveToken(request)));
    }

    @Override
    public LoginResponse refreshToken(String username) {
        String token = jwtTokenProvider.createToken(username, userDao.findByUsername(username).getRoles());

        return new LoginResponse(token);
    }
}
