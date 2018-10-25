package com.maozhen.sso.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 角色
 * </p>
 *
 * @author liuxiang
 * @since 2018-07-07
 */
@TableName("t_role")
@Data
public class Role extends DataEntity<Role> {

    private static final long serialVersionUID = 1L;

    /**
     * 角色名称
     */
    @TableField("role_name")
    private String roleName;

    @TableField(exist = false)
    private List<Menu> menus;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
