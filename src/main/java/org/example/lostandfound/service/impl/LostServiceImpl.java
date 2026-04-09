package org.example.lostandfound.service.impl;

import org.example.lostandfound.exception.ServiceException;
import org.example.lostandfound.mapper.LostMapper;
import org.example.lostandfound.pojo.Lost;
import org.example.lostandfound.service.LostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.UUID;


@Service
public class LostServiceImpl implements LostService {

    @Autowired
    private LostMapper lostMapper;

    @Override
    public void addLost(Lost lost, MultipartFile file) {
        String fileName=file.getOriginalFilename();
        String Uid= UUID.randomUUID().toString();
        ApplicationHome ap=new ApplicationHome(this.getClass());
        String path_name =ap.getDir().getParentFile().getParentFile()
                .getAbsolutePath()+"//src//main//resources//static//object//"+
                Uid+
                fileName.substring(fileName.lastIndexOf("."));
        try {
            file.transferTo(new File(path_name));
        } catch (Exception e) {
            throw new ServiceException("文件上传失败","401");
        }
        lost.setIs_report("否");
        lostMapper.addLost(lost,
                "http://localhost:8081/object/"+Uid+ fileName.substring(fileName.lastIndexOf(".")));
    }
}
