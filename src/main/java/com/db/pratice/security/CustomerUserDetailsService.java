package com.db.pratice.security;

import com.db.pratice.model.Role;
import com.db.pratice.model.User;
import com.db.pratice.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class CustomerUserDetailsService implements UserDetailsService {
    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findByUserName(username) //Entity User
                .orElseThrow(()->new UsernameNotFoundException("Username not Found"));
        //below user is different.
        return new org.springframework.security.core.userdetails.User(user.ge);
    }
    private Collection<GrantedAuthority> mapRolesToAuthority(List<>) {
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getRoleName())).collect(Collectors.toList());
    }
}
