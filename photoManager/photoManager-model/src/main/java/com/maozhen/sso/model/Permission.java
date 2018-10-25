package com.maozhen.sso.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 权限
 * </p>
 *
 * @author liuxiang
 * @since 2018-07-07
 */
@TableName("t_permission")
@Data
public class Permission extends DataEntity<Permission> {

    private static final long serialVersionUID = 1L;

    /**
     * 角色ID
     */
    @TableField("role_id")
    private Long roleId;
    /**
     * 菜单ID
     */
    @TableField("menu_id")
    private Long menuId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
