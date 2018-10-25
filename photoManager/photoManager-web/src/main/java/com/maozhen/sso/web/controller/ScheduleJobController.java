package com.maozhen.sso.web.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.baomidou.mybatisplus.mapper.EntityWrapper;
import com.baomidou.mybatisplus.mapper.Wrapper;
import com.baomidou.mybatisplus.plugins.Page;
import com.maozhen.sso.common.model.ResponseModel;
import com.maozhen.sso.common.utils.Constant;
import com.maozhen.sso.model.ScheduleJob;
import com.maozhen.sso.service.ScheduleJobService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/scheduleJob")
@Api(value = "任务调度模块")
public class ScheduleJobController {

    @Autowired
    private ScheduleJobService scheduleJobService;//任务调度服务接口

    @ApiOperation(value = "任务调度列表", notes = "任务调度列表接口", produces = "application/json")
    @RequestMapping(value = "/list", method = RequestMethod.POST)
    public ResponseModel list(ScheduleJob scheduleJob) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel();
        try {
            //装载查询条件
            Wrapper<ScheduleJob> entity = new EntityWrapper<ScheduleJob>();

            Page<ScheduleJob> page = scheduleJobService.selectPage(new Page<ScheduleJob>(1, 10), entity);
            ret.setData(page);
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }

    @ApiOperation(value = "任务调度新增", notes = "任务调度新增接口", produces = "application/json")
    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public ResponseModel create(ScheduleJob scheduleJob, HttpServletRequest request) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel().setMessage("新增成功");
        try {
            //设置登录用户token
            scheduleJob.setToken(request.getHeader(Constant.USER_TOKEN));
            //执行新增初始化操作
            scheduleJob.preInsert();
            boolean result = scheduleJobService.insert(scheduleJob);
            if (!result) {
                ret = ResponseModel.getFailedResponseModel().setMessage("新增失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }

    @ApiOperation(value = "任务调度修改", notes = "任务调度修改接口", produces = "application/json")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public ResponseModel update(ScheduleJob scheduleJob, HttpServletRequest request) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel().setMessage("修改成功");
        try {
            //设置登录用户token
            scheduleJob.setToken(request.getHeader(Constant.USER_TOKEN));
            //执行修改初始化操作
            scheduleJob.preUpdate();
            boolean result = scheduleJobService.updateById(scheduleJob);
            if (!result) {
                ret = ResponseModel.getFailedResponseModel().setMessage("修改失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }

    @ApiOperation(value = "任务调度删除", notes = "任务调度删除接口", produces = "application/json")
    @RequestMapping(value = "/delete", method = RequestMethod.GET)
    public ResponseModel delete(ScheduleJob scheduleJob) {
        ResponseModel ret = ResponseModel.getSuccessResponseModel().setMessage("删除成功");
        ;
        try {
            scheduleJob.setIsDeleted(1);
            boolean result = scheduleJobService.updateById(scheduleJob);
            if (!result) {
                ret = ResponseModel.getFailedResponseModel().setMessage("删除失败");
            }
        } catch (Exception e) {
            e.printStackTrace();
            ret = ResponseModel.getFailedResponseModel();
        }
        return ret;
    }

}
