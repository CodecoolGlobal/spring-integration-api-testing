package com.raczkowski.springintro.custom.userdetailsservice;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class CustomUserDetailsService implements UserDetailsService {

    private final Map<String, UserDetails> users = new ConcurrentHashMap<>();

    public CustomUserDetailsService(Collection<UserDetails> userDetails) {
        userDetails.forEach(userDetail -> users.put(userDetail.getUsername(), userDetail));
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (users.containsKey(username)) {
            return users.get(username);
        }

        throw new UsernameNotFoundException("Couldn't find user details for username: " + username);
    }
}
