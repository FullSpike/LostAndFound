package org.example.lostandfound.service.impl;

import jakarta.annotation.Resource;
import org.example.lostandfound.exception.ServiceException;
import org.example.lostandfound.mapper.AdminMapper;
import org.example.lostandfound.pojo.Admin;
import org.example.lostandfound.service.AdminService;
import org.example.lostandfound.utils.Md5Util;
import org.springframework.stereotype.Service;


@Service
public class AdminServiceImpl implements AdminService {

    @Resource
    private AdminMapper adminMapper;

    @Override
    public void register(Admin admin) {
        if(admin==null){
            throw new ServiceException("管理员为空","400");
        }
        if(adminMapper.selectByEmail(admin.getEmail())!=null){
            throw new ServiceException("邮箱已存在","400");
        }
        if(adminMapper.selectByPhone(admin.getPhone())!=null){
            throw new ServiceException("手机号已存在","400");
        }
        admin.setPassword(Md5Util.getMD5String(admin.getPassword()));
        adminMapper.insert(admin);
    }
}
