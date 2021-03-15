package com.sl.umps.controller;

import com.sl.common.vo.Result;
import com.sl.common.vo.SysRole;
import com.sl.umps.service.RoleService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("role")
@Slf4j
public class RoleController {
    @Autowired
    RoleService roleService;

    @RequestMapping("getRoleByUserId")
    public Result<List<SysRole>> getRoleByUserId(@RequestBody String userId){
        Result<List<SysRole>> roleResult;
        try {
            List<SysRole> roleList = roleService.getRoleByUserId(userId);
            roleResult = Result.ok();
            roleResult.setData(roleList);

        }catch (Exception e){
            log.error(e.getMessage(),e);
            roleResult = Result.failure(e.getMessage());
        }
        return roleResult;
    }
}
