package com.zjm.work;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.zjm.util.ConnectionUtil;

/**
 * @Author pareZhang
 * @Date 2020/5/16 11:13
 **/
public class Producer {
    static final String QUEUE_NAME="work_queue";

    public static void main(String[] args) throws Exception {
        //创建连接
        Connection connection= ConnectionUtil.getConnection();
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
        for(int i=1;i<=30;i++){
            String message="hello，rabbitmq ！-----"+i;
            /* 参数1：交换机名称，如果没有指定则使用默认Default Exchange
             * 参数2：路由key，简单队列可以传递队列名称
             * 参数3：消息其它属性
             * 参数4：消息内容
             */
            channel.basicPublish("",QUEUE_NAME,null,message.getBytes());
            System.out.println("已发送消息"+message);
        }
        //释放资源
        channel.close();
        connection.close();

    }
}
