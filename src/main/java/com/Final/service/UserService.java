package com.Final.service;

import com.Final.model.User;

import java.util.List;
import java.util.Map;

/**
 * Created by tb on 2016/10/29.
 */
 public interface UserService {

    /**
     * 通过id查询用户实体
     * @param id
     * @return
     */
     User findById(String id);

    /**
     * 根据条件查询用户集合
     * @param map
     * @return
     */
     List<User> find(Map<String,Object> map);

    /**
     * 获取总记录数
     * @param map
     * @return
     */
     Long getTotal(Map<String,Object> map);

    /**
     * 修改用户
     * @param user
     * @return
     */
     int update(User user);

    /**
     * 添加用户
     * @param user
     * @return
     */
     int add(User user);

    /**
     * 删除用户
     * @param id
     * @return
     */
     int delete(String id);
}
