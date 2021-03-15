package com.sl.umps.controller;

import com.sl.common.vo.Result;
import com.sl.common.vo.SysResources;
import com.sl.umps.service.ResourceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("permission")
@Slf4j
public class ResourceController {
    @Autowired
    ResourceService resourceService;

    @RequestMapping("getRolePermission")
    public Result<List<SysResources>> getRolePermission(@RequestBody String roleCode){
        Result<List<SysResources>> resourceResult;
        try{
            List<SysResources> resourcesList =  resourceService.getRolePermission(roleCode);
            resourceResult = Result.ok();
            resourceResult.setData(resourcesList);
        }catch (Exception e){
            log.error(e.getMessage(),e);
            resourceResult = Result.failure(e.getMessage());
        }
        return resourceResult;
    }
}
