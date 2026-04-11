package org.example.lostandfound.service;

import org.example.lostandfound.pojo.Lost;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface LostService {
    void addLost(Lost lost, MultipartFile file);

    List<Lost> getLostList();

    List<Lost> getTopLostList();

    List<Lost> getMyLostList(int l_id);

    void reportLost(int id, String reportReason);

    void noteLost(int id, String note);

    void deleteLost(int id);

    void updateLost(int id, Lost lost);
}
