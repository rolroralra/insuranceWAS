package com.example.insurance.repository;

import com.example.insurance.contract.model.Contract;
import com.example.insurance.contract.repository.ContractRepository;
import com.example.insurance.product.model.Product;
import com.example.insurance.product.repository.ProductRepository;
import com.example.insurance.reward.model.Reward;
import com.example.insurance.reward.repository.RewardRepository;
import com.example.insurance.subscription.model.Subscription;
import com.example.insurance.subscription.repository.SubscriptionRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.data.jpa.repository.JpaRepository;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

@SpringBootTest
public class CrudRepositoryTests {
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private RewardRepository rewardRepository;
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private ApplicationContext applicationContext;

    @BeforeEach
    void setUpAll() {
        if (contractRepository.findAll().isEmpty()) {
            contractRepository.save(new Contract());
        }

        if (rewardRepository.findAll().isEmpty()) {
            rewardRepository.save(new Reward());
        }

        if (subscriptionRepository.findAll().isEmpty()) {
            subscriptionRepository.save(new Subscription());
        }

        if (productRepository.findAll().isEmpty()) {
            productRepository.save(new Product());
        }
    }

    private Object addNewInstance(Class<?> entityClass) {
        if (Reward.class.equals(entityClass)) {
            return rewardRepository.save(new Reward());
        }
        else if (Contract.class.equals(entityClass)) {
            return contractRepository.save(new Contract());
        }
        else if (Subscription.class.equals(entityClass)) {
            return subscriptionRepository.save(new Subscription());
        }
        else if (Product.class.equals(entityClass)) {
            return productRepository.save(new Product());
        }

        return null;
    }

    @ParameterizedTest
    @ValueSource(classes = {ContractRepository.class, RewardRepository.class, SubscriptionRepository.class, ProductRepository.class})
    void test_find_all(Class<?> repositoryClass) {
        JpaRepository<?, ?> repository = (JpaRepository<?, ?>) applicationContext.getBean(repositoryClass);
        repository.findAll().forEach(System.out::println);
    }

    @ParameterizedTest
    @ValueSource(classes = {ContractRepository.class, RewardRepository.class, SubscriptionRepository.class, ProductRepository.class})
    @SuppressWarnings("unchecked")
    void test_find_by_id(Class<?> repositoryClass) throws ClassNotFoundException, NoSuchFieldException {
        JpaRepository<?, Long> repository = (JpaRepository<?, Long>) applicationContext.getBean(repositoryClass);

        String entityTypeName = ((ParameterizedType)repositoryClass.getGenericInterfaces()[0]).getActualTypeArguments()[0].getTypeName();
        String entityIdTypeName = ((ParameterizedType)repositoryClass.getGenericInterfaces()[0]).getActualTypeArguments()[1].getTypeName();
        Class<?> entityClass = Class.forName(entityTypeName);
        Field idField = entityClass.getDeclaredField("id");
        idField.setAccessible(true);

        repository.findAll().forEach(entityObject ->
                {
                    try {
                        Long entityId = (Long) idField.get(entityObject);
                        Assertions.assertThat(repository.findById(entityId)).isPresent();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                        Assertions.fail("Failed");
                    }
                }
        );
    }

    @ParameterizedTest
    @ValueSource(classes = {ContractRepository.class, RewardRepository.class, SubscriptionRepository.class, ProductRepository.class})
    @SuppressWarnings("unchecked")
    void test_save(Class<?> repositoryClass) throws ClassNotFoundException, NoSuchFieldException, IllegalAccessException {
        JpaRepository<?, Long> repository = (JpaRepository<?, Long>) applicationContext.getBean(repositoryClass);

        String entityTypeName = ((ParameterizedType)repositoryClass.getGenericInterfaces()[0]).getActualTypeArguments()[0].getTypeName();
        String entityIdTypeName = ((ParameterizedType)repositoryClass.getGenericInterfaces()[0]).getActualTypeArguments()[1].getTypeName();
        Class<?> entityClass = Class.forName(entityTypeName);
        Field idField = entityClass.getDeclaredField("id");
        idField.setAccessible(true);

        int originalSize = repository.findAll().size();

        Object newInstance = addNewInstance(entityClass);
        Long entityId = (Long) idField.get(newInstance);

        Assertions.assertThat(repository.findById(entityId))
                .isPresent();

        Assertions.assertThat(originalSize + 1 == repository.findAll().size()).isTrue();
    }
}
