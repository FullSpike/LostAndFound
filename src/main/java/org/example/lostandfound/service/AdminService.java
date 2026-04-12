package org.example.lostandfound.service;


import jakarta.validation.constraints.Pattern;
import org.example.lostandfound.pojo.Admin;
import org.springframework.web.multipart.MultipartFile;

public interface AdminService {
    void register(Admin admin);

    void updateAdmin(int id, Admin admin);

    String updateAvatar(int id, MultipartFile avatar);

    boolean checkPassword(int id, String password);

    void updatePassword(int id,  String password);
}
