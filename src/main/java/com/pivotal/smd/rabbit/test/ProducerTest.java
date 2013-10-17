/**
 * Creates the threads and gets Producers from the App context.
 */

package com.pivotal.smd.rabbit.test;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

@Component
@Profile("producer")
public class ProducerTest implements RabbitTest
{
    private static final Logger LOGGER = Logger.getLogger(ProducerTest.class);

    @Value("${producer.threads}")
    public int numProducerThreads;
    
    //Used for creating instances of prototype beans
    @Autowired
    ApplicationContext ctx;

    /**
     * Create a new Thread for each Producer, and start them up.  Join
     * and wait for them to finish.
     */
    public boolean startTest()
    {
        LOGGER.info("Running Producer Test");
        
        boolean retVal = false;

        try
        {
            Thread t[] = new Thread[numProducerThreads];
            for(int c=0; c<numProducerThreads; c++)
            {
                Producer p = (Producer)ctx.getBean("producer");
                p.testName = "producer-"+c;

                t[c] = new Thread(p);
                t[c].start();
            }

            for(int c=0; c<numProducerThreads; c++)
            {
                t[c].join();
            }
            
            retVal = true;
        }
        catch(Exception e)
        {
            LOGGER.error(e.toString());
        }

        return retVal;
    }
}
