package com.maozhen.sso.common.utils;

import java.util.LinkedHashMap;
import java.util.Map;

import com.maozhen.sso.common.ex.BaseException;

/**
 * @date 2018/7/11 14:27
 */
public class Query extends LinkedHashMap<String, Object> {

    //当前页码
    private int currentPage;
    //每页条数
    private int pageSize;

    public Query(Map<String, Object> params) throws BaseException {
        this.putAll(params);
        try {
            //分页参数
            if (params.get("currentPage") != null && params.get("pageSize") != null) {
                this.currentPage = Integer.parseInt(params.get("currentPage").toString());
                this.pageSize = Integer.parseInt(params.get("pageSize").toString());
                this.put("offset", (currentPage - 1) * pageSize);
                this.put("currentPage", currentPage);
                this.put("pageSize", pageSize);
            }

            //防止SQL注入（因为sidx、order是通过拼接SQL实现排序的，会有SQL注入风险）
            if (params.get("sidx") != null && params.get("sidx") != "") {
                String sidx = params.get("sidx").toString();
                this.put("sidx", SQLFilter.sqlInject(sidx));
                if (params.get("order") != null && params.get("order") != "") {
                    String order = params.get("order").toString();
                    this.put("order", SQLFilter.sqlInject(order));
                }
            }
        } catch (Exception ex) {
            throw new BaseException("参数错误");
        }
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

}
