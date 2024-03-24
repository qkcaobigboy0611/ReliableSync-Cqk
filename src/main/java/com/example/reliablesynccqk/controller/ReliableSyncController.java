/**
 * @author qkcao
 * @date 2024/3/20 15:01
 */
package com.example.reliablesynccqk.controller;


import com.example.reliablesynccqk.model.dto.MessageDtoV1;
import com.example.reliablesynccqk.model.vo.MessageV1;
import com.example.reliablesynccqk.service.MessageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
public class ReliableSyncController {

    @Autowired
    private MessageService messageService;

    /**
     * 添加消息,用于消息预存储,在主动方业务执行前调用
     */
    @PostMapping(value = "/v1/message")
    public int addMessageV1(@Validated @RequestBody MessageDtoV1 messageDtoV1) {
        return this.messageService.addMessage(messageDtoV1);
    }

    /**
     * 根据消息id将消息状态更新为已取消,当主动方业务执行失败时调用此接口
     */
    @PutMapping(value = "/v1/message/status/toCanceled")
    public MessageV1 updateStatusToCanceledByMessageIdV1(@RequestParam("messageId") String messageId) {
        // todo 需要根据具体的业务验证messageId是否合法
        return this.messageService.updateStatusToCanceledByMessageId(messageId);
    }

    /**
     * 根据消息ID将消息状态更新为已发送，当主动方业务执行成功时，回调此接口
     */
    @PutMapping(value = "/v1/message/status/toSending")
    public MessageV1 updateStatusToSendingByMessageIdV1(@RequestParam("messageId") String messageId) {
        // todo 需要根据具体的业务验证messageId是否合法
        return this.messageService.updateStatusToSendingByMessageId(messageId);
    }

    /**
     * 根据消息ID将消息状态更改为已发送，当被动方执行成功时调用此接口
     */
    @PutMapping(value = "/v1/message/status/toFinished")
    public MessageV1 updateStatusToFinishedByMessageIdV1(@RequestParam("messageId") String messageId) {
        //todo 需要根据具体的业务验证messageId是否合法
        return this.messageService.updateStatusToFinishedByMessageId(messageId);
    }


    /**
     * 根据消息id查询消息
     */
    @GetMapping(value = "/v1/message")
    public MessageV1 findMessageByMessageIdV1(@RequestParam("messageId") String messageId) {
        return this.messageService.findMessageV1ByMessageId(messageId);
    }


}
