package com.cskfz.sso.service.impl;

import com.cskfz.sso.entity.SysUser;
import com.cskfz.sso.repository.SysUserRepository;
import com.cskfz.sso.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author yangzc
 * @date 2020-02-08
 */
@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private SysUserRepository sysUserRepository;

    @Override
    public SysUser getByUsername(String username) {
        return sysUserRepository.findByUsername(username);
    }
}
