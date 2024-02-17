package com.heima.item.config;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.heima.item.pojo.Item;
import com.heima.item.pojo.ItemStock;
import com.heima.item.service.IItemService;
import com.heima.item.service.IItemStockService;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * 在容器启动之后，对数据进行redis缓存预热
 */
@Component
public class RedisHandler implements InitializingBean {

    @Resource
    private IItemService itemService;
    @Resource
    private IItemStockService itemStockService;
    @Resource
    private StringRedisTemplate stringRedisTemplate;

    private static final ObjectMapper MAPPER = new ObjectMapper();

    @Override
    public void afterPropertiesSet() throws Exception {
        // 1.查询商品信息
        List<Item> itemList = itemService.list();
        // 2.放入缓存
        for (Item item : itemList) {
            String json = MAPPER.writeValueAsString(item);
            stringRedisTemplate.opsForValue().set("item:id:" + item.getId(), json);
        }
        // 3.查询商品库存信息
        List<ItemStock> stockList = itemStockService.list();
        // 4.放入缓存
        for (ItemStock itemStock : stockList) {
            String json = MAPPER.writeValueAsString(itemStock);
            stringRedisTemplate.opsForValue().set("item:stock:id:" + itemStock.getId(), json);
        }
    }

    public void saveItem(Item item) {
        try {
            String json = MAPPER.writeValueAsString(item);
            stringRedisTemplate.opsForValue().set("item:id:" + item.getId(), json);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }

    public void deleteItemById(Long id) {
        stringRedisTemplate.delete("item:id:" + id);
    }
}
