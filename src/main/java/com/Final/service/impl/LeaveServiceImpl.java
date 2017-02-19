package com.Final.service.impl;

import com.Final.dao.LeaveDao;
import com.Final.model.Leave;
import com.Final.service.LeaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by tb on 2016/10/30.
 */
@Service("LeaveService")
public class LeaveServiceImpl implements LeaveService {

    @Autowired
    private LeaveDao leaveDao;
    public Leave findById(Integer id) {
        return leaveDao.findById(id);
    }

    public int add(Leave leave) {
        return leaveDao.add(leave);
    }

    public int update(Leave leave) {
        return leaveDao.update(leave);
    }

    public List<Leave> find(Map<String, Object> map) {
        return leaveDao.find(map);
    }

    public Long getTotal(Map<String, Object> map) {
        return leaveDao.getTotal(map);
    }
}
