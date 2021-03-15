package com.sl.umps.dao;

import com.sl.common.vo.SysRole;

import java.util.List;

public interface RoleDao {
    List<SysRole> getRoleByUserId(String userId);
}
