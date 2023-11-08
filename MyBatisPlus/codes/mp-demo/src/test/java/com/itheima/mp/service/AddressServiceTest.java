package com.itheima.mp.service;

import com.itheima.mp.domain.po.Address;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;
import java.util.List;

@SpringBootTest
public class AddressServiceTest {

    @Resource
    private IAddressService addressService;

    @Test
    void testDeleteByLogic() {
        addressService.removeById(59L);
    }

    @Test
    void testQuery() {
        List<Address> list = addressService.list();
        list.forEach(System.out::println);
    }

}
