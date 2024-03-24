/**
 * @author qkcao
 * @date 2024/3/20 19:57
 */
package com.example.reliablesynccqk.mq.retry;

import com.sun.istack.internal.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.concurrent.Delayed;
import java.util.concurrent.TimeUnit;

@Getter
@Setter
@AllArgsConstructor
public class MessageRetryItem implements Delayed {

    /**
     * 消息id
     */
    private String messageId;

    /**
     * 过期时间,预计发送消息时间戳(入队当前时间+配置的延迟时间)
     */
    private long expiredTime;


    public MessageRetryItem (String messageId) {
        this.messageId = messageId;
    }
    /**
     * 判断是否可以出队的条件,用过期时间(存活时间+创建时间)减当前时间戳
     */
    @Override
    public long getDelay(TimeUnit unit) {
        return unit.convert(this.expiredTime - System.currentTimeMillis(), TimeUnit.MILLISECONDS);
    }

    /**
     * 用于延时队列内部比较排序,当前延时时间-比较对象的延时时间,排序结果为升序
     * Params:
     * retryItem – 队列中的比较对象
     * @return
     */
    @Override
    public int compareTo(@NotNull Delayed retryItem) {
        return Long.compare(this.expiredTime, ((MessageRetryItem) retryItem).expiredTime);
    }

    /**
     * 覆盖equals方法以便使用延迟队列的remove方法
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MessageRetryItem that = (MessageRetryItem) o;
        return that.messageId.equals(this.messageId);
    }
}
