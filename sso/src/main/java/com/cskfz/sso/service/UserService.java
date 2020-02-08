package com.cskfz.sso.service;

import com.cskfz.sso.entity.SysUser;

/**
 * @author yangzc
 * @date 2020-02-08
 */
public interface UserService {

    SysUser getByUsername(String username);
}
