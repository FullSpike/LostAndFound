package org.example.lostandfound.service.impl;

import ch.qos.logback.core.util.MD5Util;
import jakarta.annotation.Resource;
import org.example.lostandfound.exception.ServiceException;
import org.example.lostandfound.mapper.UserMapper;
import org.example.lostandfound.pojo.User;
import org.example.lostandfound.service.UserService;
import org.example.lostandfound.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;


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

    @Override
    public boolean checkPassword(int id, String password) {
        User user = userMapper.selectById(id);
        if(user==null){
            throw new ServiceException("用户不存在","400");
        }
        String changePassword= Md5Util.getMD5String(password);
        return changePassword.equals(user.getPassword());
    }

    @Override
    public void updatePassword(int id, String password) {
        User user = userMapper.selectById(id);
        if(user==null){
            throw new ServiceException("用户不存在","400");
        }
        String changePassword= Md5Util.getMD5String(password);
        user.setPassword(changePassword);
        userMapper.update(id,user);
    }

    @Override
    public String updateAvatar(int id, MultipartFile avatar) {
        if(avatar==null){
            throw new ServiceException("用户头像为空","400");
        }
        String fileName=avatar.getOriginalFilename();
        String Uid= UUID.randomUUID().toString();
        ApplicationHome ap=new ApplicationHome(this.getClass());
        String path_name =ap.getDir().getParentFile().getParentFile()
                .getAbsolutePath()+"//src//main//resources//static//object//"+
                Uid+
                fileName.substring(fileName.lastIndexOf("."));
        try {
            avatar.transferTo(new File(path_name));
        } catch (Exception e) {
            throw new ServiceException("文件上传失败","401");
        }
        String avatarPath="http://localhost:8081/object/"+Uid+ fileName.substring(fileName.lastIndexOf("."));
        userMapper.updateAvatar(id,
                avatarPath);
        return avatarPath;
    }
}
