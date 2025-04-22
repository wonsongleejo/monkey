package com.monkey.productreservationservice;

import com.monkey.productreservationservice.config.TestFeignClientConfig;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(TestFeignClientConfig.class)
class ProductReservationServiceApplicationTests {
    @Test
    void contextLoads() {
    }

}
