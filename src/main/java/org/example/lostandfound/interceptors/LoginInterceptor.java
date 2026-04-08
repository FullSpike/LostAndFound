package org.example.lostandfound.interceptors;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.example.lostandfound.exception.ServiceException;
import org.example.lostandfound.mapper.AdminMapper;
import org.example.lostandfound.mapper.UserMapper;
import org.example.lostandfound.utils.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;


import java.util.Map;

@Component
public class LoginInterceptor implements HandlerInterceptor {

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private AdminMapper adminMapper;



    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //request是不是option请求,是的话直接放行
        if(request.getMethod().equals("OPTIONS")){
            return true;
        }

        String token = request.getHeader("Authorization");
        try {
            Map<String, Object> map = JwtUtil.parseToken(token);
            String username= (String) map.get("username");
            if (username==null){
                throw new ServiceException("请登录","401");
            }
            return true;
        } catch (Exception e) {
            response.setStatus(401);
            return false;
        }
    }
}
