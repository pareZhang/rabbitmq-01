package com.zjm.topic;

import com.rabbitmq.client.*;
import com.zjm.util.ConnectionUtil;

import java.io.IOException;

/**
 * @Author pareZhang
 * @Date 2020/5/17 20:34
 **/
public class Consumer1 {
    public static void main(String[] args) throws Exception {
        Connection connection= ConnectionUtil.getConnection();
        Channel channel=connection.createChannel();
        /**
         * 在生产者里已经声明过，不需要重复声明交换机，声明队列和绑定队列
         * 声明交换机
         * 参数1：交换机名称
         * 参数2：交换机类型，fanout，topic，direct，headers
         */
        //channel.exchangeDeclare(Producer.TOPIC_EXCHANGE, BuiltinExchangeType.TOPIC);
        Consumer consumer=new DefaultConsumer(channel){
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws IOException {
                //super.handleDelivery(consumerTag, envelope, properties, body);
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
        channel.basicConsume(Producer.TOPIC_QUEUE_1,true,consumer);
    }
}
