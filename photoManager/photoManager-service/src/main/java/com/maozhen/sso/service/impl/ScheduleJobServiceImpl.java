package com.maozhen.sso.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.maozhen.sso.dao.ScheduleJobMapper;
import com.maozhen.sso.model.ScheduleJob;
import com.maozhen.sso.service.ScheduleJobService;

/**
 * 任务调度服务接口实现
 * Created by HuangChao on 2018/8/1.
 */
@Service
public class ScheduleJobServiceImpl extends ServiceImpl<ScheduleJobMapper, ScheduleJob> implements ScheduleJobService {

}
