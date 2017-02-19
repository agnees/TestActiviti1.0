package com.Final.service;

import com.Final.model.Leave;

import java.util.List;
import java.util.Map;

/**
 * Created by tb on 2016/10/30.
 */
public interface LeaveService {

    /**
     * 通过id查询请假单实体
     * @param id
     * @return
     */
    Leave findById(Integer id);

    /**
     * 添加请假单
     * @param leave
     * @return
     */
     int add(Leave leave);

    /**
     * 修改请假单
     * @param leave
     * @return
     */
     int update(Leave leave);

    /**
     * 根据条件查询请假单集合
     * @param map
     * @return
     */
     List<Leave> find(Map<String,Object> map);

    /**
     * 获取总记录数
     * @param map
     * @return
     */
     Long getTotal(Map<String,Object> map);
}
