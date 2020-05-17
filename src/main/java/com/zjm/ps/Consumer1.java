package com.zjm.ps;

import com.rabbitmq.client.*;
import com.zjm.util.ConnectionUtil;
import sun.security.acl.AclEntryImpl;

import java.io.IOException;

/**
 * @Author pareZhang
 * @Date 2020/5/17 12:19
 **/
public class Consumer1 {
    public static void main(String[] args) throws Exception {
        Connection connection= ConnectionUtil.getConnection();
        Channel channel=connection.createChannel();
        //声明交换机
        channel.exchangeDeclare(Producer.FANOUT_EXCHANGE, BuiltinExchangeType.FANOUT);
        //声明队列
        channel.queueDeclare(Producer.FANOUT_QUEUE_1,true,false,false,null);
        //队列绑定交换机
        channel.queueBind(Producer.FANOUT_QUEUE_1,Producer.FANOUT_EXCHANGE,"");
        DefaultConsumer consumer=new DefaultConsumer(channel){
            /**
             * consumerTag :消息者标签，再channel.basicConsumer时候可以指定
             * envelope :消息包内容，可从中获取消息id，消息routingkey,交换机，消息和重转标记（收到消息失败后是否需要重新发送）
             * properties :消息属性
             * body :消息
             */
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
        channel.basicConsume(Producer.FANOUT_QUEUE_1,true,consumer);
    }
}
