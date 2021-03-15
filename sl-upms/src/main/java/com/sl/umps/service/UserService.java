package com.sl.umps.service;


import com.sl.common.vo.SysUser;

public interface UserService {
    SysUser findByUsername(String username);

}
