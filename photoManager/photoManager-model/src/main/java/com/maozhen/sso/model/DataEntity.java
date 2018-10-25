package com.maozhen.sso.model;

import java.util.Date;

import com.baomidou.mybatisplus.activerecord.Model;
import com.maozhen.sso.common.utils.DateUtil;
import com.maozhen.sso.common.utils.UserUtil;

/**
 * 数据操作实体
 */
public abstract class DataEntity<T extends Model> extends BaseEntity<T> {

    public DataEntity() {
        this.isDeleted = STATUS_NORMAL;
    }

    @Override
    public void preInsert() {
        Object object = UserUtil.getLoginUser(this.token);
        if (null != object) {
            User user = (User) object;
            this.creatorUserId = user.getId();
            this.modifiedByUserId = user.getId();
            this.creator = user.getChineseName();
            this.modifiedBy = user.getChineseName();
        }
        this.gmtCreate = DateUtil.format(new Date(), DateUtil.DATE_TIME_PATTERN);
        this.gmtModified = DateUtil.format(new Date(), DateUtil.DATE_TIME_PATTERN);
    }

    @Override
    public void preUpdate() {
        Object object = UserUtil.getLoginUser(this.token);
        if (null != object) {
            User user = (User) object;
            this.modifiedByUserId = user.getId();
            this.modifiedBy = user.getChineseName();
        }
        this.gmtModified = DateUtil.format(new Date(), DateUtil.DATE_TIME_PATTERN);
    }
}
