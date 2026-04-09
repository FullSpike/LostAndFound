package org.example.lostandfound.service;


import org.example.lostandfound.pojo.User;
import org.springframework.stereotype.Service;


public interface UserService {
    void updateUser(int id, User user);
}
