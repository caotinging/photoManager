package com.maozhen.sso.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * <p>
 * 操作日志表
 * </p>
 */
@TableName("t_operate_log")
@Data
public class OperateLog extends DataEntity<OperateLog> {

    private static final long serialVersionUID = 1L;

    @TableField("uri")
    private String uri;//接口地址

    /**
     * 操作描述
     */
    @TableField("description")
    private String description;

    /**
     * 日志类型 1web 2app
     */
    @TableField("log_type")
    private Integer logType;

    @TableField("ip")
    private String ip;//记录IP地址

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
