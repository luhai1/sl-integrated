package com.sl.user.service;

import com.sl.common.vo.SysUser;
import com.sl.common.vo.Result;
import com.sl.common.vo.SysResources;
import com.sl.common.vo.SysRole;
import com.sl.config.UpmsServiceFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

@FeignClient(name = "sl-upms",fallbackFactory = UpmsServiceFallbackFactory.class)
public interface UpmsService {
    @GetMapping("user/findByUsername")
    Result<SysUser> findByUsername(@RequestBody String username);

    @GetMapping("role/getRoleByUserId")
    Result<List<SysRole>> getRoleByUserId(@RequestBody String userId);

    @GetMapping("permission/getRolePermission")
    Result<List<SysResources>> getRolePermission(@RequestBody String roleCode);
}