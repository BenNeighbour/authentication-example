package com.benneighbour.authenticationexample.dao;

import com.benneighbour.authenticationexample.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface UserDao extends JpaRepository<User, UUID> {

    User findByUsername(String username);

    boolean existsByUsername(String username);

}
