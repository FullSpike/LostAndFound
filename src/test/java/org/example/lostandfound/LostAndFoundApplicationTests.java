package org.example.lostandfound;

import org.example.lostandfound.pojo.Lost;
import org.example.lostandfound.utils.Md5Util;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;

@SpringBootTest
class LostAndFoundApplicationTests {

    @Test
    void contextLoads() {
        System.out.println(Md5Util.getMD5String("AB261216"));
    }

}
