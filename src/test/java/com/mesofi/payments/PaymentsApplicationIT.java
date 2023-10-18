package com.mesofi.payments;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@SpringBootTest
@ActiveProfiles("itest")
class PaymentsApplicationIT {

    @Test
    void contextLoads() {
        log.debug("Loaded correctly!!");
    }

}
