package com.db.pratice.security;

import com.db.pratice.controller.Controller;
import com.db.pratice.model.Role;
import com.db.pratice.model.User;
import com.db.pratice.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.ObjectPostProcessor;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

//@Configuration
//interface provided by the spring-security.
//It is used to retrieve user-related data such as username, password, and authorities (roles) during the authentication process.
public class CustomerUserDetailsService implements UserDetailsService {
    private final Logger logger = LoggerFactory.getLogger(CustomerUserDetailsService.class);
    @Autowired
    private UserRepository userRepo;

    //loadUserByName -- is used to load user details.
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUsername(username) //Entity User
                .orElseThrow(()->new UsernameNotFoundException("Username not Found"));
        //below user is different.
        //to get the -- Role
        logger.info("Role ->"+mapRolesToAuthority(user.getRole()));
        logger.info("Username of the user is "+username);
        logger.info("Password for the user is "+user.getPassword());
        return new org.springframework.security.core.userdetails.User(username,user.getPassword(),mapRolesToAuthority(user.getRole()));
    }

    @Bean
    //interface
    //GrantedAuthority -- represents the roles or permissions granted to authenticated users. It is used for authorization purposes
    public Collection<GrantedAuthority> mapRolesToAuthority(List<Role> roles) {

        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());// O/P is in list format
    }

}


