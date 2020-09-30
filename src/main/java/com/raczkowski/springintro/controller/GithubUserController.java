package com.raczkowski.springintro.controller;

import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

//@RestController
public class GithubUserController {

    @GetMapping("/github-user")
    @ResponseBody
    public Map<String, Object> getGithubUser(OAuth2AuthenticationToken auth2AuthenticationToken) {
        return auth2AuthenticationToken.getPrincipal().getAttributes();
    }

}
