package com.maozhen.sso.web.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.maozhen.sso.common.model.ResponseModel;
import com.maozhen.sso.common.utils.DateUtil;
import com.maozhen.sso.model.OperateLog;
import com.maozhen.sso.service.OperateLogService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/operateLog")
@Api(value = "操作日志模块")
public class OperateLogController {

	@Autowired
    private OperateLogService operateLogService;//操作日志服务接口

    @ApiOperation(value = "操作日志列表", notes = "操作日志列表接口", produces = "application/json")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseModel list(OperateLog operateLog, String dateBegin, String dateEnd) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel();
        try {
            //装载查询条件
            Wrapper<OperateLog> entity = new EntityWrapper<OperateLog>();
            entity.eq("is_deleted", 0);
            if (null != operateLog.getCreator() && !"".equals(operateLog.getCreator())) {
                entity.like("creator", operateLog.getCreator());
            }
            if (operateLog.getLogType() != -1) {
                entity.eq("log_type", operateLog.getLogType());
            }
            if (null != dateBegin && !"".equals(dateBegin) && !"null".equals(dateBegin) && null != dateEnd && !"".equals(dateEnd) && !"null".equals(dateEnd)) {
                entity.between("gmt_create", DateUtil.strToDate(dateBegin + " 00:00:00"), DateUtil.strToDate(dateEnd + " 59:59:59"));
            }
            entity.orderBy("gmt_create desc");
            Page<OperateLog> page = operateLogService.selectPage(new Page<OperateLog>(operateLog.getCurrentPage(), operateLog.getPageSize()), entity);
            ret.setData(page);
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }

}
