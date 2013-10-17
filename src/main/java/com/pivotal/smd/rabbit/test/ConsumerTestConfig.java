package com.pivotal.smd.rabbit.test;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("consumer")
@ImportResource("classpath:/META-INF/spring/consumer-integration-config.xml")
public class ConsumerTestConfig
{
    private static final Logger LOGGER = Logger.getLogger(ConsumerTestConfig.class);

    //Create the RabbitTest bean    
    @Bean
    public RabbitTest getTest()
    {
        return new ConsumerTest();
    }
    
    //Simple class to place hold doing something interesting (logging?)
    class ConsumerTest implements RabbitTest
    {
        @Override
        public boolean startTest()
        {
            LOGGER.info("Consumer test starts automatically with SpringIntegration.");
            return true;
        }
    }
}

