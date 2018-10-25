package com.maozhen.sso.service.impl;

import org.springframework.stereotype.Service;

import com.baomidou.mybatisplus.service.impl.ServiceImpl;
import com.maozhen.sso.dao.OperateLogMapper;
import com.maozhen.sso.model.OperateLog;
import com.maozhen.sso.service.OperateLogService;

@Service
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLog> implements OperateLogService {

}
