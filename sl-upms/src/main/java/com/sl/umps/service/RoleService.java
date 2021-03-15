package com.sl.umps.service;

import com.sl.common.vo.SysRole;

import java.util.List;

public interface RoleService {
    List<SysRole> getRoleByUserId(String userId);
}
