package org.example.lc.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

	@Value("${queue.name.apply}")
	private String applyQueue;

	@Value("${queue.name.recovery}")
	private String recoveryQueue;

	@Value("${queue.name.quit}")
	private String quitQueue;

	@Bean
	public String applyQueueName() {
		return applyQueue;
	}

	@Bean
	public String recoveryQueueName() {
		return recoveryQueue;
	}

	@Bean
	public String quitQueueName() {
		return quitQueue;
	}

	@Bean
	public Queue initApplyQueue() {
		return new Queue(applyQueue);
	}

	@Bean
	public Queue initRecoveryQueue() {
		return new Queue(recoveryQueue);
	}

	@Bean
	public Queue initQuitQueue() {
		return new Queue(quitQueue);
	}


	@Bean
	TopicExchange exchange() {
		return new TopicExchange("exchange");
	}


}