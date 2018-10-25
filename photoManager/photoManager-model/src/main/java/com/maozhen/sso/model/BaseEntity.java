package com.maozhen.sso.model;

import com.baomidou.mybatisplus.activerecord.Model;
import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableId;
import com.baomidou.mybatisplus.enums.IdType;
import com.maozhen.sso.common.utils.DateUtil;

/**
 * 基础实体类
 * Created by HuangChao on 2018/8/2.
 */
public abstract class BaseEntity<T extends Model> extends Model<T> {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    Long id;

    /**
     * 用户ID
     */
    @TableField("creator_user_id")
    Long creatorUserId;
    /**
     * 创建人
     */
    @TableField("creator")
    String creator;
    /**
     * 用户ID
     */
    @TableField("modified_by_user_id")
    Long modifiedByUserId;
    /**
     * 修改人
     */
    @TableField("modified_by")
    String modifiedBy;
    /**
     * 0 未删除 1删除
     */
    @TableField("is_deleted")
    Integer isDeleted;

    @TableField("gmt_create")
    String gmtCreate;//创建时间

    @TableField("gmt_modified")
    String gmtModified;//修改时间

    @TableField(exist = false)
    String token;//用户token

    @TableField(exist = false)
    int currentPage = 1;//当前页
    @TableField(exist = false)
    int pageSize = 10;//页数大小

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getCreatorUserId() {
        return creatorUserId;
    }

    public void setCreatorUserId(Long creatorUserId) {
        this.creatorUserId = creatorUserId;
    }

    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }

    public Long getModifiedByUserId() {
        return modifiedByUserId;
    }

    public void setModifiedByUserId(Long modifiedByUserId) {
        this.modifiedByUserId = modifiedByUserId;
    }

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public Integer getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(Integer isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getGmtCreate() {
        return DateUtil.format(DateUtil.strToDate(gmtCreate), DateUtil.DATE_TIME_PATTERN);
    }

    public void setGmtCreate(String gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public String getGmtModified() {
        return DateUtil.format(DateUtil.strToDate(gmtModified), DateUtil.DATE_TIME_PATTERN);
    }

    public void setGmtModified(String gmtModified) {
        this.gmtModified = gmtModified;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    /**
     * 插入之前执行方法，子类实现
     */
    abstract void preInsert();

    /**
     * 更新之前执行方法，子类实现
     */
    abstract void preUpdate();

    /**
     * 状态（0：正常；1：删除）
     */
    public static final Integer STATUS_NORMAL = 0;
    public static final Integer STATUS_DELETE = 1;

}
