package org.example.lostandfound;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;
import org.example.lostandfound.utils.Md5Util;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class LostAndFoundApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(Md5Util.getMD5String("AB261216"));
    }

    @Test
    void testOpenAI(){
        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(System.getenv("DASHSCOPE_API_KEY"))
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage("你是谁")
                .model("qwen-plus")
                .build();

        try {
            ChatCompletion chatCompletion = client.chat().completions().create(params);
            System.out.println(chatCompletion.choices().get(0).message().content());
        } catch (Exception e) {
            System.err.println("Error occurred: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
