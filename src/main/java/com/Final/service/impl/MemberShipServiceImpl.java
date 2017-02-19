package com.Final.service.impl;

import com.Final.dao.MemberShipDao;
import com.Final.model.MemberShip;
import com.Final.service.MemberShipService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Map;

/**
 * Created by tb on 2016/10/30.
 */
@Service("MemberShipService")
public class MemberShipServiceImpl implements MemberShipService {

    @Autowired
    private MemberShipDao memberShipDao;

    public MemberShip login(Map<String, Object> map) {
        return memberShipDao.login(map);
    }

    public int deleteAllGroupsByUserId(String userId) {
        return memberShipDao.deletAllGroupsById(userId);
    }

    public int add(MemberShip memberShip) {
        return memberShipDao.add(memberShip);
    }
}
