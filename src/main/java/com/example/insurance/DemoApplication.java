package com.example.insurance;

import com.example.insurance.contract.model.Contract;
import com.example.insurance.contract.repository.ContractRepository;
import com.example.insurance.reward.model.Reward;
import com.example.insurance.reward.repository.RewardRepository;
import com.example.insurance.subscription.model.Subscription;
import com.example.insurance.subscription.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

	@Bean
	public CommandLineRunner loadData(@Autowired ApplicationContext applicationContext) {
		return args -> {
			applicationContext.getBean(RewardRepository.class).save(new Reward());
			applicationContext.getBean(ContractRepository.class).save(new Contract());
			applicationContext.getBean(SubscriptionRepository.class).save(new Subscription());
		};
	}
}
