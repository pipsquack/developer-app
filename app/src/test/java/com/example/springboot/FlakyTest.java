package com.example.springboot;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.SecureRandom;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
public class FlakyTest {

    SecureRandom rand = new SecureRandom();

    @Test
    public void getHello() throws Exception {
        assertThat(rand.nextBoolean()).isTrue();
    }
}
