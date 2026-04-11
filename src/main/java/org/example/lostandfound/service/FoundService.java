package org.example.lostandfound.service;

import org.example.lostandfound.pojo.Found;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FoundService {
    void addFound(Found found, MultipartFile file);

    List<Found> getFoundList();

    List<Found> getMyFoundList(int f_id);

    void noteFound(int id, String note);

    void reportFound(int id, String reportReason);

    void deleteFound(int id);

    void updateFound(int id, Found found);
}
