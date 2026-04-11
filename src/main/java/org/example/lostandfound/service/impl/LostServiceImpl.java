package org.example.lostandfound.service.impl;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import org.example.lostandfound.exception.ServiceException;
import org.example.lostandfound.mapper.LostMapper;
import org.example.lostandfound.pojo.Lost;
import org.example.lostandfound.service.LostService;
import org.example.lostandfound.utils.AiUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.system.ApplicationHome;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.List;
import java.util.Optional;
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
        // 调用OpenAI API获取描述
        String description;
        try {
            if(lost.getDescription().isEmpty()){
                description="请给"+lost.getName()+"添加描述,比如该物品为校园卡，可能⽤于校园⾝份认证或消费，请尽快联系失主。在15字以内";
            }else{
                description=lost.getName()+"存在描述"+lost.getDescription()+",请给添加在外观上的更详细的描述，,在15字以内";
            }
            // 调用OpenAI API获取描述
            lost.setDescription(AiUtil.getAiResponse(description));
        } catch (Exception e) {
            throw new ServiceException("获取描述失败","401");
        }
        lostMapper.addLost(lost,
                "http://localhost:8081/object/"+Uid+ fileName.substring(fileName.lastIndexOf(".")));
    }

    @Override
    public List<Lost> getLostList() {
        return lostMapper.getLostList();
    }

    @Override
    public List<Lost> getTopLostList() {
        return lostMapper.getTopLostList();
    }

    @Override
    public List<Lost> getMyLostList(int l_id) {
        return lostMapper.getMyLostList(l_id);
    }

    @Override
    public void reportLost(int id, String reportReason) {
        lostMapper.reportLost(id,"是",reportReason);
    }

    @Override
    public void noteLost(int id, String note) {
        if(note.isEmpty()){
            throw new ServiceException("留言不能为空","401");
        }
        lostMapper.noteLost(id,note);
    }

    @Override
    public void deleteLost(int id) {
        if(lostMapper.selectById(id) == null){
            throw new ServiceException("物品不存在","401");
        }
        lostMapper.deleteLost(id);
    }

    @Override
    public void updateLost(int id, Lost lost) {
        if(lostMapper.selectById(id) == null){
            throw new ServiceException("物品不存在","401");
        }
        lostMapper.updateLost(id,lost);
    }
}
