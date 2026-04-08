package org.example.lostandfound.controller;

import ch.qos.logback.core.util.MD5Util;
import jakarta.validation.constraints.Pattern;
import org.apache.ibatis.annotations.Param;
import org.example.lostandfound.common.Result;
import org.example.lostandfound.mapper.AdminMapper;
import org.example.lostandfound.mapper.UserMapper;
import org.example.lostandfound.pojo.Admin;
import org.example.lostandfound.pojo.User;
import org.example.lostandfound.utils.JwtUtil;
import org.example.lostandfound.utils.Md5Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@CrossOrigin
@RestController
@RequestMapping()
@Validated
public class WebController {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AdminMapper adminMapper;

    @PostMapping("/login")
    public Result<?> login(@RequestParam("emailOrPhone")String emailOrPhone,
                           @Pattern(regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,20}$")String password){

        //判断是否是邮箱
        if(emailOrPhone.endsWith(".com")){
            String email=emailOrPhone;
            if(userMapper.selectByEmail(email)==null&&adminMapper.selectByEmail(email)==null){
                return Result.error("用户或管理员不存在","404");
            }
            if(userMapper.selectByEmail(email)!=null){
                User user=userMapper.selectByEmail(email);
                if(!Md5Util.getMD5String(password).equals(user.getPassword())){
                    return Result.error("密码错误","400");
                }
                User user1=userLogin(user);
                return Result.success(user1);
            }
            if(adminMapper.selectByEmail(email)!=null){
                Admin admin=adminMapper.selectByEmail(email);
                if(!Md5Util.getMD5String(password).equals(admin.getPassword())){
                    return Result.error("密码错误","400");
                }
                Admin admin1=adminLogin(admin);
                return Result.success(admin1);
            }
        }else if(emailOrPhone.startsWith("1")){
            String phone=emailOrPhone;
            if(userMapper.selectByPhone(phone)==null&&adminMapper.selectByPhone(phone)==null){
                return Result.error("用户或管理员不存在","404");
            }

            if(userMapper.selectByPhone(phone)!=null){
                User user=userMapper.selectByPhone(phone);
                if(!Md5Util.getMD5String(password).equals(user.getPassword())){
                    return Result.error("密码错误","400");
                }
                User user1=userLogin(user);
                return Result.success(user1);
            }

            if(adminMapper.selectByPhone(phone)!=null){
                Admin admin=adminMapper.selectByPhone(phone);
                if(!Md5Util.getMD5String(password).equals(admin.getPassword())){
                    return Result.error("密码错误","400");
                }
                Admin admin1=adminLogin(admin);
                return Result.success(admin1);
            }
        }

        return Result.error("邮箱或手机号格式错误","400");
    }

    private Admin adminLogin(Admin admin) {
        Map<String, Object> claims=new HashMap<>();
        claims.put("username",admin.getUsername());
        claims.put("password",admin.getPassword());
        admin.setToken(JwtUtil.createToken(claims));
        return admin;
    }

    private User userLogin(User user) {
        Map<String, Object> claims=new HashMap<>();
        claims.put("username",user.getUsername());
        claims.put("password",user.getPassword());
        user.setToken(JwtUtil.createToken(claims));
        return user;
    }

}
