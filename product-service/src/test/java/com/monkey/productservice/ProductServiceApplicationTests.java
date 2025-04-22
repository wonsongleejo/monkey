package com.monkey.productservice;

import com.monkey.productservice.config.TestFeignClientConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestFeignClientConfig.class)
class ProductServiceApplicationTests {

    @Test
    void contextLoads() {
    }

}
