package com.Final.service.impl;

import com.Final.dao.UserDao;
import com.Final.model.User;
import com.Final.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

/**
 * Created by tb on 2016/10/29.
 */
@Service("UserService")
public class UserServiceImpl  implements UserService{

    @Autowired
    private UserDao userDao;
    public User findById(String id) {
        return userDao.findById(id);
    }

    public List<User> find(Map<String, Object> map) {
        return userDao.find(map);
    }

    public Long getTotal(Map<String, Object> map) {
        return userDao.getTotal(map);
    }

    public int update(User user) {
        return userDao.update(user);
    }

    public int add(User user) {
        return userDao.add(user);
    }

    public int delete(String id) {
        return userDao.delete(id);
    }
}
