package com.dgs.transactionvalidation.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.activemq.command.ActiveMQQueue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.jms.Queue;

import static com.dgs.transactionvalidation.util.TransactionsConstants.TRANSACTIONS_QUEUE;

@Configuration
public class TransactionsConfig {

    @Bean
    public Queue queue(){
        return new ActiveMQQueue(TRANSACTIONS_QUEUE);
    }

    @Bean
    public ObjectMapper objectMapper() {
        return new ObjectMapper();
    }
}
