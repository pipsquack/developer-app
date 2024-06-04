package com.example.springboot;

import static org.assertj.core.api.Assertions.assertThat;

import java.security.SecureRandom;

import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Tags;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest()
public class FlakyTest {

    SecureRandom rand = new SecureRandom();

    @Test
    @Tags({@Tag("datadog_itr_unskippable")})
    public void roll_the_dice() throws Exception {
        assertThat(rand.nextBoolean()).isTrue();
    }
}
