package com.sl.user.service.impl;

import com.sl.common.util.StatusCode;
import com.sl.common.vo.Result;
import com.sl.common.vo.SysResources;
import com.sl.common.vo.SysRole;
import com.sl.common.vo.SysUser;
import com.sl.user.entity.LoginUser;
import com.sl.user.service.UpmsService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.List;
import java.util.Set;


@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UpmsService upmsService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Result<SysUser> userResult = upmsService.findByUsername(username);
        if (userResult.getCode() != StatusCode.SUCCESS_CODE) {
            throw new UsernameNotFoundException("用户:" + username + ",不存在!");
        }
        Set<SimpleGrantedAuthority> grantedAuthorities = new HashSet<>();
        SysUser userVo = new SysUser();
        BeanUtils.copyProperties(userResult.getData(),userVo);
        Result<List<SysRole>> roleResult = upmsService.getRoleByUserId(userVo.getId());
        if (roleResult.getCode() == StatusCode.SUCCESS_CODE){
            List<SysRole> roleVoList = roleResult.getData();
            for (SysRole role :roleVoList){
                //角色必须是ROLE_开头，可以在数据库中设置
                SimpleGrantedAuthority grantedAuthority = new SimpleGrantedAuthority(role.getRoleCode());
                grantedAuthorities.add(grantedAuthority);
                //获取权限
                Result<List<SysResources>> perResult  = upmsService.getRolePermission(role.getRoleCode());
                if (perResult.getCode() == StatusCode.SUCCESS_CODE){
                    List<SysResources> permissionList = perResult.getData();
                    for (SysResources resources : permissionList) {
                        if ( !StringUtils.isEmpty(resources.getUrl()) ){
                            SimpleGrantedAuthority authority = new SimpleGrantedAuthority(resources.getUrl());
                            grantedAuthorities.add(authority);
                        }
                    }
                }
            }
        }
        LoginUser user = new LoginUser(userVo.getUserName(),userVo.getPassword(),grantedAuthorities);
        user.setId(userVo.getId());
        return user;
    }
}
