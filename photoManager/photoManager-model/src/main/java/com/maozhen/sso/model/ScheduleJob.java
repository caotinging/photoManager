package com.maozhen.sso.model;

import com.baomidou.mybatisplus.annotations.TableField;
import com.baomidou.mybatisplus.annotations.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 任务调度
 * Created by HuangChao on 2018/8/1.
 */
@TableName("t_schedule_job")
@Data
public class ScheduleJob extends DataEntity<ScheduleJob> {

    private static final long serialVersionUID = 1L;


    @TableField("job_name")
    private String jobName;//任务名称

    @TableField("job_group")
    private String jobGroup;//任务分组

    @TableField("cron")
    private String cron;//Cron表达式

    @TableField("is_concurrent")
    private String isConcurrent;//执行状态

    @TableField("bean_class")
    private String beanClass;//执行类

    @TableField("method_name")
    private String methodName;//执行方法

    @TableField("params")
    private String params;//初始化参数

    @TableField("remarks")
    private String remarks;//备注

    @Override
    protected Serializable pkVal() {
        return this.id;
    }
}
