package com.Final.service;

import com.Final.model.MemberShip;

import java.util.Map;

/**
 * Created by tb on 2016/10/30.
 */
public  interface MemberShipService {

    /**
     * 用户登录
     * @param map
     *
     * @return
     */
    MemberShip login(Map<String,Object> map);

    /**
     * 删除指定用户所有角色
     * @param userId
     * @return
     */
    int  deleteAllGroupsByUserId(String userId);

    /**
     * 添加用户权限
     * @param memberShip
     * @return
     */
    int add(MemberShip memberShip);
}
