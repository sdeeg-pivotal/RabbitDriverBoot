/**
 * Define beans common to the whole app
 */

package com.pivotal.smd.rabbit;

import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;

@Configuration
@ImportResource("classpath:/META-INF/spring/rabbit-config.xml")
public class RabbitDriverConfig
{
    private static final Logger LOGGER = Logger.getLogger(RabbitDriverConfig.class);
    
    @Bean
    public Random random()
    {
        Random randy = new Random();
        randy.setSeed(System.currentTimeMillis());
        return randy;
    }
}
