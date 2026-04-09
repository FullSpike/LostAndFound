package org.example.lostandfound.service;

import org.example.lostandfound.pojo.Lost;
import org.springframework.web.multipart.MultipartFile;

public interface LostService {
    void addLost(Lost lost, MultipartFile file);
}
