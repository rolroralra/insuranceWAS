package com.example.insurance.model;

import com.example.insurance.subscription.model.Subscription;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class SubscriptionTests {

    @Test
    void test_hash_code() {
        Subscription any = new Subscription();
        any.setId(1L);
        Subscription other = new Subscription();
        other.setId(2L);

        Assertions.assertThat(any.hashCode()).isNotEqualTo(other.hashCode());
    }
}
