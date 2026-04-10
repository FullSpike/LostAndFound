package org.example.lostandfound.utils;

import com.openai.client.OpenAIClient;
import com.openai.client.okhttp.OpenAIOkHttpClient;
import com.openai.models.chat.completions.ChatCompletion;
import com.openai.models.chat.completions.ChatCompletionCreateParams;

import java.util.Optional;

public class AiUtil {

    public static String getAiResponse(String description) {
        OpenAIClient client = OpenAIOkHttpClient.builder()
                .apiKey(System.getenv("DASHSCOPE_API_KEY"))
                .baseUrl("https://dashscope.aliyuncs.com/compatible-mode/v1")
                .build();

        ChatCompletionCreateParams params = ChatCompletionCreateParams.builder()
                .addUserMessage(description)
                .model("qwen-plus")
                .build();
        ChatCompletion chatCompletion = client.chat().completions().create(params);
        Optional<String> descriptionOptional = chatCompletion.choices().get(0).message().content();
        return descriptionOptional.get();
    }
}
