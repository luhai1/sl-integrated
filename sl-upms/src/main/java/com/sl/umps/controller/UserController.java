package com.sl.umps.controller;

import com.sl.common.vo.Result;
import com.sl.common.vo.SysUser;
import com.sl.umps.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@Slf4j
public class UserController {
    @Autowired
    UserService userService;

    @RequestMapping("findByUsername")
    public Result<SysUser> findByUsername(@RequestBody String username){
        Result<SysUser> result ;
        try{
           SysUser sysUser = userService.findByUsername(username);
            result = Result.ok();
            result.setData(sysUser);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            result = Result.failure(e.getMessage());
        }
       return result;
    }
}
