/**
 * Connections are thread safe so ConnectionFactories create and return a
 * singleton.  We'll fake it out by creating a Factory for each Connection.
 * 
 * TODO: change createCF to prototype bean.
 */

package com.pivotal.smd.rabbit.test;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

@Component
@Profile("connection")
public class ConnectionTest implements RabbitTest
{
    private static final Logger LOGGER = Logger.getLogger(ConnectionTest.class);

    @Value("${connection.number:10}")
    public int numConnections;
    
    @Value("${connection.create.channel:false}")
    public boolean createChannel;

    @Value("${connection.validate.number:1}")
    public int numValidation;

    @Value("${connection.validate.delay:1000}")
    public int validationDelay;
    
    @Autowired
    ApplicationContext ctx;

    @Override
    public boolean startTest()
    {
        LOGGER.info("Hello from the Connection Test");
        
        boolean retVal = false;
        
        Connection connections[] = new Connection[numConnections];
        Channel    channels[]    = new Channel[numConnections];
        
        try
        {
            LOGGER.info("Creating "+numConnections+" connections");
            for(int c=0; c<numConnections; c++)
            {
                connections[c] = (Connection)ctx.getBean("rabbitConnection");
                if(createChannel) { channels[c] = connections[c].createChannel(); }
            }
            LOGGER.info("... done");
            
            //Sleep for x seconds, then test the connections
            LOGGER.info("Validation check.  Sleeping for "+validationDelay+"ms, looping "+numValidation+" times");
            try
            {
                int openConnections=0, closedConnections=0;
                boolean fail = false;
                for(int numValidationCounter=0; numValidationCounter<numValidation; numValidationCounter++)
                {
                    Thread.sleep(validationDelay);
                    openConnections=0; closedConnections=0;
                    for(int c=0; c<numConnections; c++)
                    {
                        if(connections[c].isOpen()) { openConnections++; } else { closedConnections++; }
                    }
                    LOGGER.info("Validating connection status ["+numValidationCounter+"]: open="+openConnections+" closed="+closedConnections);
                    
                    //If we ever find any closed connections it's a failure.
                    if(closedConnections > 0) { fail = true; }
                }
                if(!fail) { retVal = true; }
            }
            catch (InterruptedException e)
            {
                LOGGER.error(e.toString());
            }
            finally
            {
                LOGGER.info("Closing the connections");
                for(int c=0; c<numConnections; c++)
                {
                    try { connections[c].close(); } catch(Exception ignore){}
                }
            }
        }
        catch(Exception e)
        {
            LOGGER.error(e.toString());
        }

        LOGGER.info("Done with connection test.  Result="+retVal);

        return retVal;
    }
}
