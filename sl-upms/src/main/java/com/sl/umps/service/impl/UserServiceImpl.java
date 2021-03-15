package com.sl.umps.service.impl;


import com.sl.common.vo.SysUser;
import com.sl.umps.dao.UserDao;
import com.sl.umps.service.UserService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


@Service
@Transactional
public class UserServiceImpl implements UserService {
    @Resource
    UserDao userDao;


    @Override
    public SysUser findByUsername(String username) {
        return userDao.findByUsername(username);
    }
}
