package com.zjm.work;

import com.rabbitmq.client.*;
import com.zjm.util.ConnectionUtil;

import java.io.IOException;

/**
 * @Author pareZhang
 * @Date 2020/5/16 11:13
 **/
public class Consumer1 {
    public static void main(String[] args) throws Exception {
        //获取连接
        Connection connection= ConnectionUtil.getConnection();
        //创建频道
        Channel channel=connection.createChannel();
        //创建队列，并设置消息处理
        /*
         * 参数一：队列名称
         * 参数二：是否定义持久化队列
         * 参数三：是否独占本次连接
         * 参数四：是否在不使用的时候自动删除队列
         * 参数五：队列其它参数
         * */
        channel.queueDeclare(Producer.QUEUE_NAME,true,false,false,null);
        //监听消息
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
                System.out.println("-----消费者1开始-----");
                //路由key
                System.out.println("路由key为："+envelope.getRoutingKey());
                //交换机
                System.out.println("交换机为："+envelope.getExchange());
                //消息id
                System.out.println("消息id为："+envelope.getDeliveryTag());
                //收到的消息
                System.out.println("接收到的消息："+new String(body,"UTF-8"));
                System.out.println("-----消费者1结束-----");
            }
        };
        /*
          监听消息
          参数一：队列名称
          参数二：是否自动确认，设置为true表示消息接收到自动向mq回复接收到了，mq接收到回复后会删除消息，设置为false则需手动确认
          参数三：消息接收到后回调
         */
        channel.basicConsume(Producer.QUEUE_NAME,true,consumer);

    }
}
