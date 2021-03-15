package com.sl.umps.dao;


import com.sl.common.vo.SysUser;

public interface UserDao {
    SysUser findByUsername(String username);
}
