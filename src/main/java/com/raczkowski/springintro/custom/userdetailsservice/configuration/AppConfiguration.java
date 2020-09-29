package com.raczkowski.springintro.custom.userdetailsservice.configuration;

import com.raczkowski.springintro.custom.userdetailsservice.CustomUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collection;

import static java.util.Collections.singletonList;

@Configuration
public class AppConfiguration {

    @Autowired
    public PasswordEncoder passwordEncoder;

    @Bean
    public CustomUserDetailsService userDetailsService() {
        Collection<UserDetails> users = singletonList(
                new CustomUserDetails("przemek", passwordEncoder.encode("braczkowski"), true, "USER")
        );

        return new CustomUserDetailsService(users);
    }


}
