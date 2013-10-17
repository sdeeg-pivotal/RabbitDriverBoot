package com.pivotal.smd.rabbit.test;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProducerTestProps
{
    @Value("${exchange.name}")
    public String exchangeName;

    @Value("${queue.name}")
    public String queueName;

    @Value("${routing.key}")
    public String routingKey;

    @Value("${producer.data.filename}")
    public String dataFileName;

    @Value("${producer.message.number}")
    public int numMessages;

    @Value("${producer.message.delay}")
    public int delay;
}
