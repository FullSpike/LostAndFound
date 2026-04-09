package org.example.lostandfound.service.impl;

import jakarta.annotation.Resource;
import org.example.lostandfound.exception.ServiceException;
import org.example.lostandfound.mapper.UserMapper;
import org.example.lostandfound.pojo.User;
import org.example.lostandfound.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class UserServiceImpl implements UserService {

    @Resource
    private UserMapper userMapper;

    @Override
    public void updateUser(int id, User user) {
        if(user==null){
            throw new ServiceException("用户为空","400");
        }
        userMapper.update(id,user);
    }
}
