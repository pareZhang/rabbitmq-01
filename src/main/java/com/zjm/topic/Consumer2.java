package com.zjm.topic;

import com.rabbitmq.client.*;
import com.zjm.util.ConnectionUtil;

import java.io.IOException;

/**
 * @Author pareZhang
 * @Date 2020/5/17 20:34
 **/
public class Consumer2 {
    public static void main(String[] args) throws Exception {
        Connection connection= ConnectionUtil.getConnection();
        Channel channel=connection.createChannel();
        Consumer consumer=new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //super.handleDelivery(consumerTag, envelope, properties, body);
                System.out.println("路由key为："+envelope.getRoutingKey());
                System.out.println("交换机为："+envelope.getExchange());
                System.out.println("消息id为："+envelope.getDeliveryTag());
                System.out.println("消费者2-接收到的消息为："+new String(body,"UTF-8"));
            }
        };
        /* 监听消息
         *参数一：队列名称
         *参数二：是否自动确认，设置为true表示消息接收到自动向mq回复接收到了，mq接收到回复后会删除消息，设置为false则需手动确认
         *参数三：收到消息后回调
         */
        channel.basicConsume(Producer.TOPIC_QUEUE_2,true,consumer);
    }
}
