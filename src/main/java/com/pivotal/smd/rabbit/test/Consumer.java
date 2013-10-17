package com.pivotal.smd.rabbit.test;

import org.apache.log4j.Logger;

class Consumer
{
    private static final Logger LOGGER = Logger.getLogger(Consumer.class);
    
    private long numReceivedMessages = 0;
    public void readMessage(Object message)
    {
        long id = Thread.currentThread().getId();
        
//        if(numReceivedMessages>=1000)
//        {
//            LOGGER.info("Consumer("+id+")::readMessage Received 1000 messages");
//            numReceivedMessages=0;
//        }
//        else
//        {
//            numReceivedMessages++;
//        }

//        if(message instanceof String)
//        {
//            String messageString = (String)message;
//            LOGGER.info("Consumer("+id+")::readMessage Received message of length="+messageString.length());
//            
//        }
//        else
//        {
//            LOGGER.info("Consumer("+id+")::readMessage Received message that is not a String but is of type="+message.getClass().getCanonicalName());
//        }
    }
}
