package com.java.crm.settings.service.impl;

import com.java.crm.settings.domain.User;
import com.java.crm.settings.mapper.UserMapper;
import com.java.crm.settings.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service("userService")
public class UserServiceImpl implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Override
    public User queryUserByActAndPwd(Map<String, Object> map) {
        return userMapper.selectUserByActAndPwd(map);
    }

    @Override
    public List<User> queryAllUser() {
        return userMapper.selectAllUser();
    }
}
