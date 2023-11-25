package com.my;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@Slf4j
@SpringBootTest
public class BookCaseTest {

    @Autowired
    private BookCase bookCase;

    @Test
    public void test() {
        log.info(bookCase.toString());
    }
}
