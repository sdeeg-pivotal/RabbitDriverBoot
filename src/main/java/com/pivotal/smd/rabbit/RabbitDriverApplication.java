/**
 * TODO: Figure out how to use built in spring-boot logger
 */

package com.pivotal.smd.rabbit;

import java.util.Arrays;
import java.util.Scanner;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.pivotal.smd.rabbit.test.RabbitTest;

@Configuration
@EnableAutoConfiguration
@ComponentScan
public class RabbitDriverApplication implements CommandLineRunner
{
    private static final Logger LOGGER = Logger.getLogger(RabbitDriverApplication.class);
    
    @Value("${beanDump:false}")
    private boolean beanDump;
    
    @Value("${autoexit:true}")
    private boolean autoExit;
    
    @Autowired
    ApplicationContext ctx;

    @Autowired
    RabbitTest rabbitTest;

    @Override
    public void run(String... arg0) throws Exception
    {
        if(beanDump)
        {
            StringBuffer sb = new StringBuffer();
            sb.append("\n=========================================================\n\n");
            System.out.println("Let's inspect the beans provided by Spring Boot:");

            String[] beanNames = ctx.getBeanDefinitionNames();
            Arrays.sort(beanNames);
            for (String beanName : beanNames)
            {
                sb.append(beanName+"\n");
            }
            sb.append("\n=========================================================\n");
            if (LOGGER.isInfoEnabled())
            {
                LOGGER.info(sb.toString());
            }
            else
            {
                System.out.println(sb.toString());
            }
        }
        
        if(rabbitTest != null)
        {
            rabbitTest.startTest();
        }
        else
        {
            LOGGER.error("rabbitTest is NULL!!!");
        }
        
        if(!autoExit)
        {
            final Scanner scanner = new Scanner(System.in);

            if (LOGGER.isInfoEnabled())
            {
                LOGGER.info("\n========================================================="
                        + "\n                                                         "
                        + "\n    press 'q + Enter' to quit the application            "
                        + "\n                                                         "
                        + "\n=========================================================");
            }

            String next = scanner.next();
            while (!next.equals("q"))
            {
                next = scanner.next();
            }
        }

        if (LOGGER.isInfoEnabled())
        {
            LOGGER.info("Exiting application...bye.");
        }
        
        //David Syer says this is bad, but the app isn't shutting down.
        System.exit(0);
    }
    
    public static void main(String... args)
    {
        SpringApplication.run(RabbitDriverApplication.class, args);
    }
}
