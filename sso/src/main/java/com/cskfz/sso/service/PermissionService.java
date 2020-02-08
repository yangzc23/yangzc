package com.cskfz.sso.service;

import com.cskfz.sso.entity.SysPermission;

import java.util.List;

/**
 * @author yangzc
 * @date 2020-02-08
 */
public interface PermissionService {

    List<SysPermission> findByUserId(Integer userId);

}
