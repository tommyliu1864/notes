package com.my.common.utils;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class JwtHelperTest {

    @Test
    public void testCreateToken() {
        String token = JwtHelper.createToken(20L, "root");
        System.out.println(token);
    }

    @Test
    public void testResolveToken() {
        String token = "eyJhbGciOiJIUzUxMiIsInppcCI6IkdaSVAifQ.H4sIAAAAAAAAAKtWKi5NUrJScgwN8dANDXYNUtJRKi1OLfJMAQoaGUB5eYm5qUB-UX5-CVAktaJAycrQ3MDIyMLU2Ni0FgCxtd3oRAAAAA.jJS_hylmloc-36ovb688e_ofTUgbkuMGzl1jVdohFyFWPeRMU2R6uxeV1YlGm-sorS8-NBS9xcazouchIoIsaQ";
        Long userId = JwtHelper.getUserId(token);
        String username = JwtHelper.getUsername(token);
        System.out.println(userId + "," + username);
    }

}
