package com.zjm.ps;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zjm.util.ConnectionUtil;

/**
 * @Author pareZhang
 * @Date 2020/5/17 11:37
 **/
public class Producer {
    /**
     * 交换机名称
     */
    static final String FANOUT_EXCHANGE="fanout_exchange";
    /**
     * 队列名称
     */
    static final String FANOUT_QUEUE_1="fanout_queue_1";
    /**
     * 队列名称
     */
    static final String FANOUT_QUEUE_2="fanout_queue_2";
    private static boolean f;

    public static void main(String[] args) throws Exception {
        Connection connection= ConnectionUtil.getConnection();
        Channel channel=connection.createChannel();
        /**
         * 声明交换机
         * 参数1：交换机名称
         * 参数2：交换机类型，fanout，topic，direct，headers
         */
        channel.exchangeDeclare(FANOUT_EXCHANGE, BuiltinExchangeType.FANOUT);
        /**
         * 声明队列
         * 参数1：队列名称
         * 参数2：是否持久化
         * 参数3：是否独占本次连接
         * 参数4：是否在不使用的时候自动删除队列
         * 参数5：队列其它参数
         */
        channel.queueDeclare(FANOUT_QUEUE_1,true,false,false,null);
        channel.queueDeclare(FANOUT_QUEUE_2,true,false,false,null);

        /**
         * 队列绑定交换机
         * 参数1：要绑定的队列
         * 参数2：要绑定到哪个交换机
         * 参数3：路由键
         */
        channel.queueBind(FANOUT_QUEUE_1,FANOUT_EXCHANGE,"");
        channel.queueBind(FANOUT_QUEUE_2,FANOUT_EXCHANGE,"");

        for (int i = 1; i <= 10; i++) {
            //发送消息
            String message="你好：小兔子！发布订阅模式---"+i;
            /**
             * 参数1：交换机名称，如果没有指定则使用默认Default Exchange
             * 参数2：路由key，简单模式可以传递队列名称
             * 参数3：消息其它属性
             * 参数4：消息内容
             */
            channel.basicPublish(FANOUT_EXCHANGE,"",null,message.getBytes());
            System.out.println("已发送消息："+message);
        }
        //关闭资源
        channel.close();
        connection.close();
    }
}
