package com.zjm.simple;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.zjm.util.ConnectionUtil;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * @Author pareZhang
 * @Date 2020/5/16 11:13
 **/
public class Producer {
    static final String QUEUE_NAME="simple_queue";

    public static void main(String[] args) throws Exception {
        //创建连接工厂
        ConnectionFactory connectionFactory=new ConnectionFactory();
        //主机地址
        connectionFactory.setHost("192.168.0.10");
        //连接端口
        connectionFactory.setPort(5672);
        //虚拟主机名称  默认为/
        connectionFactory.setVirtualHost("/pareZhang");
        //连接用户名 默认guest
        connectionFactory.setUsername("pareZhang");
        //连接密码 默认guest
        connectionFactory.setPassword("wxfyyzy115");
        //创建连接
        Connection connection=connectionFactory.newConnection();
        //创建频道 一个连接可以有多个频道
        Channel channel=connection.createChannel();
        //声明（创建）队列
        /*
        * 参数一：队列名称
        * 参数二：是否定义持久化队列
        * 参数三：是否独占本次连接
        * 参数四：是否在不使用的时候自动删除队列
        * 参数五：队列其它参数
        * */
        channel.queueDeclare(QUEUE_NAME,true,false,false,null);
        String message="hello，rabbitmq ！";
        /**
         * 参数1：交换机名称，如果没有指定则使用默认Default Exchange
         * 参数2：路由key，简单模式可以传递队列名称
         * 参数3：消息其它属性
         * 参数4：消息内容
         **/
        channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
        System.out.println("已发送消息"+message);
        //释放资源
        channel.close();
        connection.close();

    }
}
