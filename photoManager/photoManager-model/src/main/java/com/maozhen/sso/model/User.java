package com.maozhen.sso.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 用户
 * </p>
 *
 */
@TableName("t_user")
@Data
public class User extends DataEntity<User> {

    private static final long serialVersionUID = 1L;

    /**
     * 用户名  
     */
    @TableField("username")
    private String username;
    /**
     * 密码
     */
    @TableField("password")
    private String password;
    /**
     * 状态 0启用 1停用
     */
    @TableField("status")
    private Integer status = 0;
    /**
     * 邮箱
     */
    @TableField("chinese_name")
    private String chineseName;
    /**
     * 邮箱
     */
    private String email;
    @TableField("mobile_phone")
    private String mobilePhone;
    /**
     * 头像附件地址
     */
    @TableField("head_portrait_url")
    private String headPortraitUrl;

    /**
     * 性别
     */
    @TableField("sex")
    private Integer sex;
    /**
     * 位置
     */
    @TableField("address")
    private String address;

    /**
     * 一个用户对应多个角色
     */
    @TableField(exist = false)
    private List<Role> roles;

    /**
     * 一个用户对应的菜单
     */
    @TableField(exist = false)
    private List<Menu> menus;

    //判断用户是否管理员
    @TableField(exist = false)
    private boolean isAdmin;

    public boolean isAdmin() {
        boolean flag = false;
        if (null != roles) {
            for (int i = 0; i < roles.size(); i++) {
                if (roles.get(i).getId() == 1) {
                    flag = true;
                    break;
                }
            }
        }
        return flag;
    }

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
