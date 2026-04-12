package org.example.lostandfound.service.impl;

import jakarta.annotation.Resource;
import org.example.lostandfound.exception.ServiceException;
import org.example.lostandfound.mapper.AdminMapper;
import org.example.lostandfound.pojo.Admin;
import org.example.lostandfound.pojo.User;
import org.example.lostandfound.service.AdminService;
import org.example.lostandfound.utils.Md5Util;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;


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

    @Override
    public void updateAdmin(int id, Admin admin) {
        if(adminMapper.selectById(id)==null){
            throw new ServiceException("管理员不存在","400");
        }
        adminMapper.update(id,admin);
    }

    @Override
    public String updateAvatar(int id, MultipartFile avatar) {
        if(adminMapper.selectById(id)==null){
            throw new ServiceException("管理员不存在","400");
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
        adminMapper.updateAvatar(id,avatarPath);
        return avatarPath;
    }

    @Override
    public boolean checkPassword(int id, String password) {
        Admin admin = adminMapper.selectById(id);
        if(admin==null){
            throw new ServiceException("管理员不存在","400");
        }
        String changePassword= Md5Util.getMD5String(password);
        return changePassword.equals(admin.getPassword());
    }

    @Override
    public void updatePassword(int id, String password) {
        Admin admin = adminMapper.selectById(id);
        if(admin==null){
            throw new ServiceException("管理员不存在","400");
        }
        String changePassword= Md5Util.getMD5String(password);
        admin.setPassword(changePassword);
        adminMapper.update(id,admin);
    }
}
