package org.example.lostandfound.service;


import org.example.lostandfound.pojo.User;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;


public interface UserService {
    void updateUser(int id, User user);

    boolean checkPassword(int id, String password);

    void updatePassword(int id, String password);

    String updateAvatar(int id, MultipartFile avatar);

    void register(User user);

    List<User> getChatUsers(int id);

    User getUserInfo(int id);
}
