package com.zjm.routing;

import com.rabbitmq.client.*;
import com.zjm.util.ConnectionUtil;

import java.io.IOException;

/**
 * @Author pareZhang
 * @Date 2020/5/17 17:21
 **/
public class Consumer1 {
    public static void main(String[] args) throws Exception {
        Connection connection= ConnectionUtil.getConnection();
        Channel channel=connection.createChannel();
        //声明交换机
        channel.exchangeDeclare(Producer.DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT);
        //声明队列
        channel.queueDeclare(Producer.DIRECT_QUEUE_INSERT,true,false,false,null);
        //队列绑定交换机
        channel.queueBind(Producer.DIRECT_QUEUE_INSERT,Producer.DIRECT_EXCHANGE,"insert");
        //创建消费者，并设置消息处理
        Consumer consumer=new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
               // super.handleDelivery(consumerTag, envelope, properties, body);
                System.out.println("路由key为："+envelope.getRoutingKey());
                System.out.println("交换机为："+envelope.getExchange());
                System.out.println("消息id为："+envelope.getDeliveryTag());
                System.out.println("消费者1-接收到的消息为："+new String(body,"UTF-8"));
            }
        };
        /* 监听消息
         *参数一：队列名称
         *参数二：是否自动确认，设置为true表示消息接收到自动向mq回复接收到了，mq接收到回复后会删除消息，设置为false则需手动确认
         *参数三：收到消息后回调
         */
        channel.basicConsume(Producer.DIRECT_QUEUE_INSERT,true,consumer);
    }

}
