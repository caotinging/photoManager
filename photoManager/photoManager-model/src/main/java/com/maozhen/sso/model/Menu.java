package com.maozhen.sso.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * <p>
 * 菜单
 * </p>
 *
 * @author liuxiang
 * @since 2018-07-07
 */
@TableName("t_menu")
@Data
public class Menu extends DataEntity<Menu> {

    private static final long serialVersionUID = 1L;

    @TableField("menu_name")
    private String menuName;//菜单名称

    @TableField("is_leaf")
    private Integer isLeaf;//是否有子级 0没有 1有

    @TableField("parent_id")
    private Long parentId;//上级ID

    @TableField("url")
    private String url;//接口地址

    @TableField("menu_type")
    private Integer menuType;//菜单类型 1菜单 2按钮

    @TableField(exist = false)
    private List<Menu> childrenMenus;//下级菜单

    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
