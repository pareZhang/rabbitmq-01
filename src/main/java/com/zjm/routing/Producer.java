package com.zjm.routing;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zjm.util.ConnectionUtil;

/**
 * @Author pareZhang
 * @Date 2020/5/17 16:59
 * 路由模式的交换机类型为：Direct
 **/
public class Producer {
    static final String DIRECT_EXCHANGE="direct_exchange";
    static final String DIRECT_QUEUE_INSERT="direct_queue_insert";
    static final String DIRECT_QUEUE_UPDATE="direct_queue_update";

    public static void main(String[] args) throws Exception {
        Connection connection=ConnectionUtil.getConnection();
        Channel channel=connection.createChannel();
        /**
         * 声明交换机
         * 参数1：交换机名称
         * 参数2：交换机类型，fanout，topic，direct，headers
         */
        channel.exchangeDeclare(DIRECT_EXCHANGE, BuiltinExchangeType.DIRECT);
        /**
         * 声明队列
         * 参数1：队列名称
         * 参数2：是否持久化
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * 参数5：队列其它参数
         */
        channel.queueDeclare(DIRECT_QUEUE_INSERT,true,false,false,null);
        channel.queueDeclare(DIRECT_QUEUE_UPDATE,true,false,false,null);
        /**
         * 队列绑定交换机
         * 参数1：要绑定的队列
         * 参数2：要绑定到哪个交换机
         * 参数3：路由键
         */
        channel.queueBind(DIRECT_QUEUE_INSERT,DIRECT_EXCHANGE,"insert");
        channel.queueBind(DIRECT_QUEUE_UPDATE,DIRECT_EXCHANGE,"update");
        //发送消息
        String message="新增了商品，路由模式；routing key为 insert";
        /**
         * 参数1：交换机名称，如果没有指定则使用默认Default Exchange
         * 参数2：路由key，简单模式可以传递队列名称
         * 参数3：消息其它属性
         * 参数4：消息内容
         */
        channel.basicPublish(DIRECT_EXCHANGE,"insert",null,message.getBytes());
        System.out.println("已发送消息："+message);

        //发送消息
        message="修改了商品，路由模式；routing key为 update";
        /**
         * 参数1：交换机名称，如果没有指定则使用默认Default Exchange
         * 参数2：路由key，简单模式可以传递队列名称
         * 参数3：消息其它属性
         * 参数4：消息内容
         */
        channel.basicPublish(DIRECT_EXCHANGE,"update",null,message.getBytes());
        System.out.println("已发送消息："+message);

        channel.close();
        connection.close();
    }
}
