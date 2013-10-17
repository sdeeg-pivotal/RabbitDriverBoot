/**
 * Connections are thread safe so ConnectionFactories create and return a
 * singleton.  We'll create a Factory for each requested so we get a unique
 * Connection for each request.
 */

package com.pivotal.smd.rabbit.test;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

@Configuration
public class RabbitConnectionUtil
{
    private static final Logger LOGGER = Logger.getLogger(RabbitConnectionUtil.class);

    @Value("${host.name}")
    public String name;

    @Value("${host.port}")
    public int port;

    @Value("${host.user}")
    public String user;

    @Value("${host.password}")
    public String password;

    @Value("${host.vhost}")
    public String vhost;

    @Value("${host.usessl:false}")
    public boolean useSSL;

    @Bean(name="rabbitConnection")
    @Scope("prototype")
    public Connection getRabbitConnection()
    {
        Connection retVal = null;

        try
        {
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost(name);
            connectionFactory.setPort(port);
            connectionFactory.setUsername(user);
            connectionFactory.setPassword(password);
            connectionFactory.setVirtualHost(vhost);
            if(useSSL) { connectionFactory.useSslProtocol(); }
            
            retVal = connectionFactory.newConnection();
        }
        catch(Exception e)
        {
            LOGGER.error(e.toString());
        }

        return retVal;
    }
}
