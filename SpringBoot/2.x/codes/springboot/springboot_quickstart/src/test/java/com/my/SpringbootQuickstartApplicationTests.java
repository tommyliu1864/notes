package com.my;

import com.my.dao.BookDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(classes = SpringbootQuickstartApplication.class)
class SpringbootQuickstartApplicationTests {

	@Autowired
	private BookDao bookDao;

	@Test
	void contextLoads() {
		bookDao.save();
	}

}
