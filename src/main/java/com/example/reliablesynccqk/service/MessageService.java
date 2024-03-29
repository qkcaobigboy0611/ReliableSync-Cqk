/**
 * @author qkcao
 * @date 2024/3/20 18:55
 */
package com.example.reliablesynccqk.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.example.reliablesynccqk.Enum.MessageStateEnum;
import com.example.reliablesynccqk.config.ReliablesyncProperties;
import com.example.reliablesynccqk.mapper.ReliableSyncMapper;
import com.example.reliablesynccqk.model.dto.MessageDtoV1;
import com.example.reliablesynccqk.model.po.Message;
import com.example.reliablesynccqk.model.vo.MessageV1;
import com.example.reliablesynccqk.mq.retry.MessageRetryItem;
import com.example.reliablesynccqk.mq.retry.MessageRetryQueue;
import com.example.reliablesynccqk.util.BeanUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.DelayQueue;

@Service
public class MessageService {
    private static final Logger LOGGER = LoggerFactory.getLogger(MessageService.class);

    @Autowired
    private ReliableSyncMapper reliableSyncMapper;

    @Autowired
    private ReliablesyncProperties reliablesyncProperties;


    /**
     * 添加消息,创建后消息的状态为 MessageState.待确认
     * @param messageDtoV1
     */
    public int addMessage(MessageDtoV1 messageDtoV1) {
        Message message = new Message();
        // 平台版本
        message.setVersion(messageDtoV1.getVersion());
        // 消息ID
        message.setMessageId(messageDtoV1.getMessageId());
        // 消息内容
        message.setMessageBody(messageDtoV1.getMessageBody());
        // 消息重试次数
        message.setMessageTryTimes(Message.DEFAULT_TRY_TIMES);
        // 队列名称
        message.setMessageQueue(messageDtoV1.getMessageQueue());
        // 消息是否死亡
        message.setMessageDead(Boolean.FALSE);
        // 查询URL(用于状态确认)
        message.setQueryURL(messageDtoV1.getQueryURL());
        // 备注
        message.setRemark(messageDtoV1.getRemark());
        message.setCtime(LocalDateTime.now());
        message.setMtime(LocalDateTime.now());
        message.setCuser(Message.DEFAULT_CUSER);
        message.setMuser(Message.DEFAULT_MUSER);
        message.setStatus(MessageStateEnum.UNCONFIRMED.getCode());
        return this.reliableSyncMapper.insert(message);
    }

    /**
     * 根据消息id将消息状态更新为已取消
     * @param messageId
     * @return
     */
    public MessageV1 updateStatusToCanceledByMessageId(String messageId) {
        int i = this.updateStatusByMessageId(messageId, MessageStateEnum.CANCELED.getCode());
        if(i > 0) {
            // 将已取消的消息从重试队列移除
            MessageRetryQueue.getInstance().remove(messageId);
            Message messageByMessageId = this.getMessageByMessageId(messageId);
            return BeanUtils.convertType(messageByMessageId, MessageV1.class);
        }
        return new MessageV1();
    }

    /**
     * 当主动方发送消息成功时，回调这个接口，将消息状态的改为已发送
     * @param messageId
     * @return
     */
    public MessageV1 updateStatusToSendingByMessageId(String messageId) {
        int i = this.updateStatusByMessageId(messageId, MessageStateEnum.SENDING.getCode());
        if(i > 0) {
            Message message = this.getMessageByMessageId(messageId);
            // 将消息添加到消息队列中 MessageTryTimes:重试的次数
            Integer messageTryTimes = message.getMessageTryTimes();
            // 计算过期时间expiredTime
            long expiredTime = 0l;
            if(messageTryTimes == 0) {
                expiredTime = System.currentTimeMillis();
            } else {
                List<Integer> retryWaitSeconds = reliablesyncProperties.getRetryWaitSeconds();
                expiredTime = retryWaitSeconds.get(messageTryTimes - 1) * 1000;
            }
            // 添加到延迟队列中
            MessageRetryQueue.getInstance().add(messageId, expiredTime);
            Message messageByMessageId = this.getMessageByMessageId(messageId);
            return BeanUtils.convertType(messageByMessageId, MessageV1.class);
        }
        return new MessageV1();
    }


    /**
     * 根据消息ID将消息状态
     * @param messageId
     * @return
     */
    public MessageV1 updateStatusToFinishedByMessageId(String messageId) {
        int i = this.updateStatusByMessageId(messageId, MessageStateEnum.FINISHED.getCode());
        if(i > 0) {
            // 将已取消的消息从重试队列移除
            MessageRetryQueue.getInstance().remove(messageId);
            Message messageByMessageId = this.getMessageByMessageId(messageId);
            return BeanUtils.convertType(messageByMessageId, MessageV1.class);
        }
        return new MessageV1();
    }




    /**
     * 根据实体id将消息修改到目标状态
     * @return
     */
    private int updateStatusByMessageId(String messageId, int status) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        Message message = reliableSyncMapper.selectOne(queryWrapper);
        //幂等,当前状态和目标状态相同
        if (message.getStatus() == status) {
            return 1;
        }
        message.setStatus(status);
        // 修改人
        message.setMuser(Message.DEFAULT_MUSER);
        // 修改时间
        message.setMtime(LocalDateTime.now());
        return reliableSyncMapper.updateById(message);
    }


    /**
     * 根据消息状态查询全部消息
     */
    public List<MessageV1> findMessageV1ByStatus(String status) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("status", status);
        List<Message> messageList = reliableSyncMapper.selectList(queryWrapper);
        List<MessageV1> messageV1List = new ArrayList<>();
        for (Message message : messageList) {

        }
        return reliableSyncMapper.updateById(message);
    }

    public Message getMessageByMessageId(String messageId) {
        QueryWrapper<Message> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("messageId", messageId);
        return reliableSyncMapper.selectOne(queryWrapper);
    }

    public MessageV1 findMessageV1ByMessageId(String messageId) {
        Message message = this.getMessageByMessageId(messageId);
        return BeanUtils.convertType(message, MessageV1.class);
    }
}
