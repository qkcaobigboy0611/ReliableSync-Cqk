/**
 * @author qkcao
 * @date 2024/3/20 19:54
 */
package com.example.reliablesynccqk.mq.retry;

import com.example.reliablesynccqk.service.MessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.*;

/**
 * 消息重试队列，会根据配置文件中retryWaitSeconds配置项,重试发送消息
 */
@Component
public class MessageRetryQueue {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageRetryQueue.class);
    private static final MessageRetryQueue MESSAGE_RETRY_QUEUE = new MessageRetryQueue();

    @Autowired
    private MessageService messageService;

    /**
     * 处理消息的线程池
     */
    private Executor executor = new ThreadPoolExecutor(2, 4, 120L, TimeUnit.SECONDS, new SynchronousQueue<>());
    /**
     * 创建一个最初为空的新延时队列
     */
    private DelayQueue<MessageRetryItem> messageRetryItemDelayQueue = new DelayQueue<>();


    /**
     * 防止被其他地方初始化
     */
    private MessageRetryQueue() {
    }

    /**
     * 获取实例(单例)
     */
    public static MessageRetryQueue getInstance() {
        return MESSAGE_RETRY_QUEUE;
    }

    /**
     * 取元素守护线程
     */
    Thread daemonThread;
    /**
     * 初始化重试队列
     */
    @Component
    public class Builder implements ApplicationRunner {

        @Override
        public void run(ApplicationArguments args) {
            //todo ....
        }
    }

    /**
     * 将消息从消息队列中删除
     */
    public void remove(String messageId) {
        LOGGER.info("从重试队列删除, messageId:[{}]", messageId);
        messageRetryItemDelayQueue.remove(new MessageRetryItem(messageId));
    }

    /**
     * 将消息添加到消息队列：注意有一个过期时间(出队时间戳):expiredTime
     */
    public void add(String messageId, long expiredTime) {
        LOGGER.info("加入到重试队列, messageId:[{}]", messageId);
        messageRetryItemDelayQueue.put(new MessageRetryItem(messageId, expiredTime));
    }

}
