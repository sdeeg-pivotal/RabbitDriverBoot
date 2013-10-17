package com.pivotal.smd.rabbit.test;

import java.io.DataInputStream;
import java.io.FileInputStream;
import java.util.Random;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import com.rabbitmq.client.Connection;

@Component
@Lazy
public class ProducerData implements InitializingBean
{
    private static final Logger LOGGER = Logger.getLogger(ProducerData.class);
    
    @Value("${producer.data.dir}")
    public String dataDir;

    @Value("${producer.data.filename}")
    public String[] fileNames;

    @Autowired
    Random randy;
    
    //Locally initialized
    public byte[][] rawData;    
    
    /*
     * Pre-load any data files, and create the connection to Rabbit.
     */
    @Override
    public void afterPropertiesSet()
    {
        if(fileNames.length == 0)
        {
            LOGGER.error("No file names to load for data");
        }
        else
        {
            try
            {
                int c=0;
                rawData = new byte[fileNames.length][];
                for(String fileName : fileNames)
                {
                    LOGGER.info("Loading file: "+fileName);
                    DataInputStream dis = new DataInputStream(new FileInputStream(dataDir+"/"+fileName));
                    rawData[c] = new byte[dis.available()];
                    dis.read(rawData[c], 0, dis.available());
                    dis.close();
                    c++;
                }
                LOGGER.info("Loaded "+rawData.length+" files");
            }
            catch(Exception e)
            {
                System.out.println(e.toString());
            }
        }
        
        if(randy != null)
        {
            LOGGER.info("Getting ready to seed randy");
            randy.setSeed(System.currentTimeMillis());
        }
        else
        {
            LOGGER.error("randy is null!!!");
        }
    }
    
    public int getRandomFileNumber()
    {
        int retVal = -1;
        if(randy != null)
        {
            retVal = randy.nextInt(rawData.length);
        }
        else
        {
            LOGGER.error("Randy is NULL!!!");
        }
        
        return retVal;
    }
}
