package com.maozhen.sso.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 用户角色
 * </p>
 *
 * @author liuxiang
 * @since 2018-07-07
 */
@TableName("t_user_role")
@Data
public class UserRole extends DataEntity<UserRole> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户ID
     */
    @TableField("user_id")
    private Long userId;
    /**
     * 角色ID
     */
    @TableField("role_id")
    private Long roleId;

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
