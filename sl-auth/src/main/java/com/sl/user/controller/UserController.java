package com.sl.user.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

/**
 * 检查用户
 * @author luhai
 */
@RestController
public class UserController {
    @RequestMapping("/checkUser")
    public Principal user(Principal user) {
        return user;
    }
}