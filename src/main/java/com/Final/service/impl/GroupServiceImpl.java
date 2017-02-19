package com.Final.service.impl;

import com.Final.dao.GroupDao;
import com.Final.model.Group;
import com.Final.service.GroupService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by tb on 2016/10/29.
 */
@Service("GroupService")
public class GroupServiceImpl implements GroupService {

    @Autowired
    private GroupDao groupDao;
    public Group findById(String id) {

        return groupDao.findById(id);
    }

    public List<Group> findByUserId(String userId) {

        return groupDao.findByUserId(userId);
    }

    public List<Group> find(Map<String, Object> map) {

        return groupDao.find(map);
    }

    public Long getTotal(Map<String, Object> map) {

        return groupDao.getTotal(map);
    }

    public int update(Group group) {

        return groupDao.update(group);
    }

    public int add(Group group) {

        return groupDao.add(group);
    }

    public int delete(String id) {

        return groupDao.delete(id);
    }
}
