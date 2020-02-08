package com.cskfz.sso.vo;

import com.cskfz.sso.entity.SysUser;
import lombok.Data;

import java.util.List;

/**
 * @author yangzc
 * @date 2020-02-08
 */
@Data
public class SysUserVO extends SysUser {

    /**
     * 权限列表
     */
    private List<String> authorityList;

}
