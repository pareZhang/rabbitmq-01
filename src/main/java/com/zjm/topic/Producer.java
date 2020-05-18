package com.zjm.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zjm.util.ConnectionUtil;

/**
 * @Author pareZhang
 * @Date 2020/5/17 19:50
 **/
public class Producer {
    static final String TOPIC_EXCHANGE="topic_exchange";
    static final String TOPIC_QUEUE_1="topic_queue_1";
    static final String TOPIC_QUEUE_2="topic_queue_2";

    public static void main(String[] args) throws Exception {
        Connection connection= ConnectionUtil.getConnection();
        Channel channel=connection.createChannel();
        /**
         * 声明交换机
         * 参数1：交换机名称
         * 参数2：交换机类型，fanout，topic，direct，headers
         * 参数3：是否持久化交换机
         */
        channel.exchangeDeclare(TOPIC_EXCHANGE, BuiltinExchangeType.TOPIC,true);
        /**
         * 声明队列
         * 参数1：队列名称
         * 参数2：是否持久化
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * 参数5：队列其它参数
         */
        channel.queueDeclare(TOPIC_QUEUE_1,true,false,false,null);
        channel.queueDeclare(TOPIC_QUEUE_2,true,false,false,null);
        /**
         * 队列绑定交换机
         * 参数1：要绑定的队列
         * 参数2：要绑定到哪个交换机
         * 参数3：路由键
         */
        channel.queueBind(TOPIC_QUEUE_1,TOPIC_EXCHANGE,"item.#");
        channel.queueBind(TOPIC_QUEUE_2,TOPIC_EXCHANGE,"*.delete");

        //发送消息
        String message="新增了商品，Topic模式：routing key 为 item.insert";
        channel.basicPublish(TOPIC_EXCHANGE,"item.insert",null,message.getBytes());
        System.out.println("已发送消息："+message);

        //发送消息
        message="修改了商品，Topic模式：routing key 为 item.update";
        channel.basicPublish(TOPIC_EXCHANGE,"item.update",null,message.getBytes());
        System.out.println("已发送消息："+message);

        //发送消息
        message="删除了商品，Topic模式：routing key 为 item.delete";
        channel.basicPublish(TOPIC_EXCHANGE,"item.delete",null,message.getBytes());
        System.out.println("已发送消息："+message);

        channel.close();
        connection.close();

    }
}
