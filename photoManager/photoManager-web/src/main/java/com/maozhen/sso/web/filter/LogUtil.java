package com.maozhen.sso.web.filter;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.maozhen.sso.model.Menu;
import com.maozhen.sso.model.OperateLog;
import com.maozhen.sso.service.MenuService;
import com.maozhen.sso.service.OperateLogService;

/**
 * 日志处理工具
 * Created by HuangChao on 2018/8/7.
 */
@Component
public class LogUtil {

    @Autowired
    private OperateLogService operateLogService;//操作日志服务接口

    @Autowired
    private MenuService menuService;//菜单服务接口

    /**
     * 保存日志
     *
     * @param operateLog
     */
    public void saveLog(OperateLog operateLog) {
        //获取所有的接口列表
        List<Menu> apiList = menuService.getApiList();
        for (int i = 0; i < apiList.size(); i++) {
            if (operateLog.getUri().endsWith(apiList.get(i).getUrl())) {
                operateLog.setDescription(apiList.get(i).getMenuName());
                break;
            }
        }
        operateLogService.insert(operateLog);
    }


}
