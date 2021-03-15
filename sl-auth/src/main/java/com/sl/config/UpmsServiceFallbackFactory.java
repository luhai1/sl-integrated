package com.sl.config;

import com.sl.common.vo.SysResources;
import com.sl.common.vo.SysRole;
import com.sl.common.vo.SysUser;
import com.sl.common.vo.Result;
import com.sl.user.service.UpmsService;
import feign.hystrix.FallbackFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author luhai
 */
@Slf4j
@Component
public class UpmsServiceFallbackFactory implements FallbackFactory<UpmsService> {
    @Override
    public UpmsService create(Throwable throwable) {
        log.error("调用UPMS失败", throwable);
        return new UpmsService() {
            @Override
            public Result<SysUser> findByUsername(String username) {
                return Result.failure(201,"调用失败");
            }

            @Override
            public Result<List<SysRole>> getRoleByUserId(String userId) {
                return Result.failure(201,"调用失败");
            }

            @Override
            public Result<List<SysResources>> getRolePermission(String roleId) {
                return Result.failure(201,"调用失败");
            }
        };
    }
}
