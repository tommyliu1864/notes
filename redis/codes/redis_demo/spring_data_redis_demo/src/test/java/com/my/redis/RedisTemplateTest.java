package com.my.redis;

import com.my.redis.pojo.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.RedisTemplate;

@SpringBootTest
class RedisTemplateTest {

	// 去掉泛型类型，否则会注入失败
	@Autowired
	private RedisTemplate redisTemplate;
	//private RedisTemplate<String, Object> redisTemplate;

	@Test
	void testString(){
		// 写入一条string数据
		redisTemplate.opsForValue().set("name", "jason");
		// 获取string数据
		Object name = redisTemplate.opsForValue().get("name");
		System.out.println("name:"+name);
	}

	@Test
	void testUser(){
		redisTemplate.opsForValue().set("user:100", new User("韦小宝", 20));
		User user = (User) redisTemplate.opsForValue().get("user:100");
		System.out.println(user);
	}
}
