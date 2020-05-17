package com.zjm.util;

import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

/**
 * @Author pareZhang
 * @Date 2020/5/16 12:19
 **/
public class ConnectionUtil {
    public static Connection getConnection() throws Exception{
        //创建连接工厂
        ConnectionFactory connectionFactory=new ConnectionFactory();
        //主机地址
        connectionFactory.setHost("192.168.0.10");
        //连接端口 默认为5672
        connectionFactory.setPort(5672);
        //虚拟主机名称  默认为/
        connectionFactory.setVirtualHost("/pareZhang");
        //连接用户名  默认为guest
        connectionFactory.setUsername("pareZhang");
        //连接密码
        connectionFactory.setPassword("wxfyyzy115");
        //创建连接
        return connectionFactory.newConnection();
    }
}
