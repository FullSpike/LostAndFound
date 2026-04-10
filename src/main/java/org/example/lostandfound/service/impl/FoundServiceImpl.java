package org.example.lostandfound.service.impl;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import org.example.lostandfound.exception.ServiceException;
import org.example.lostandfound.mapper.FoundMapper;
import org.example.lostandfound.pojo.Found;
import org.example.lostandfound.service.FoundService;
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
public class FoundServiceImpl implements FoundService {

    @Autowired
    private FoundMapper foundMapper;


    @Override
    public void addFound(Found found, MultipartFile file) {
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
        found.setIs_report("否");
        // 调用OpenAI API获取描述
        String description;
        try {
            if(found.getDescription().isEmpty()){
                description="请给"+found.getName()+"添加描述,比如该物品为校园卡，可能⽤于校园⾝份认证或消费，请尽快联系失主。在15字以内";
            }else{
                description=found.getName()+"存在描述"+found.getDescription()+",请给添加在外观上的更详细的描述，,在15字以内";
            }
            found.setDescription(AiUtil.getAiResponse(description));
        } catch (Exception e) {
            throw new ServiceException("获取描述失败","401");
        }
        foundMapper.addFound(found,
                "http://localhost:8081/object/"+Uid+ fileName.substring(fileName.lastIndexOf(".")));

    }

    @Override
    public List<Found> getFoundList() {
        return foundMapper.getFoundList();
    }

    @Override
    public List<Found> getMyFoundList(int f_id) {
        return foundMapper.getMyFoundList(f_id);
    }
}
