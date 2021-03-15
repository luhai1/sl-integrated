package com.sl.umps.service.impl;

import com.sl.common.vo.SysRole;
import com.sl.umps.dao.RoleDao;
import com.sl.umps.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {
    @Autowired
    RoleDao roleDao;

    @Override
    public List<SysRole> getRoleByUserId(String userId) {
        return roleDao.getRoleByUserId(userId);
    }
}
