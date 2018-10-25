package com.maozhen.sso.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.baomidou.mybatisplus.mapper.BaseMapper;
import com.maozhen.sso.model.Role;

/**
 * <p>
 * 角色 Mapper 接口
 * </p>
 *
 * @author liuxiang
 * @since 2018-07-07
 */
public interface RoleMapper extends BaseMapper<Role> {

    /**
     * 根据用户获取角色列表
     *
     * @param userId
     * @return
     */
    List<Role> getRolesByUser(@Param("userId") Long userId);

}
