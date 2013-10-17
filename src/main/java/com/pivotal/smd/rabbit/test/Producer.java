/**
 * Prototype scoped Bean.  Created by the App Context so we can use auto wiring.
 */

package com.pivotal.smd.rabbit.test;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;

@Component
@Scope("prototype")
class Producer implements Runnable
{
    private static final Logger LOGGER = Logger.getLogger(Producer.class);

    @Autowired
    private ProducerTestProps props;

    @Autowired
    protected ProducerData producerData;

    @Autowired
    protected Connection rabbitConnection;

    protected String testName = "producer-x";

    @Override
    public void run()
    {
        LOGGER.info("Starting producer test thread "+testName+" with "+producerData.rawData.length+" files");
        try
        {
            if(rabbitConnection != null)
            {
                Channel channel = rabbitConnection.createChannel();
                for(int c=0; c<props.numMessages || props.numMessages==0; c++)
                {
                    int fileNumber = producerData.getRandomFileNumber();
                    if(fileNumber >= 0)
                    {
                        channel.basicPublish(props.exchangeName, props.routingKey, null, producerData.rawData[fileNumber]);
                        //rabbitTemplate.convertAndSend(props.exchangeName, props.routingKey, producerData.rawData[fileNumber]);
                    }
                    else
                    {
                        channel.basicPublish(props.exchangeName, props.routingKey, null, "Error, no files to send".getBytes());
                        //rabbitTemplate.convertAndSend(props.exchangeName, props.routingKey, "Error, no files to send");
                    }
                    Thread.sleep(props.delay);
                }
            }
            else
            {
                LOGGER.error("rabbitTemplate is null!!!");
            }
        }
        catch (Exception e)
        {
            LOGGER.error("Thread interupted!!! ... testName=" + testName
                    + "\n" + e.toString());
        }
        
        LOGGER.info("Test thread "+testName+" done");
    }
}
