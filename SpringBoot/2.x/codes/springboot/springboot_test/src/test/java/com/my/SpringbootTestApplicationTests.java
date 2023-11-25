package com.my;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
//@SpringBootTest(properties = {"test.prop=ByeBye"})
@SpringBootTest(args = {"--test.prop=See you"})
class SpringbootTestApplicationTests {

    @Value("${test.prop}")
    private String msg;

    @Test
    void contextLoads() {
        log.info(msg);
    }

}
