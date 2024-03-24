/**
 * @author qkcao
 * @date 2024/3/20 18:27
 */
package com.example.reliablesynccqk.model.po;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 消息持久层对象
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Message implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final String DEFAULT_CUSER = "SYSTEM-MAAT";
    public static final String DEFAULT_MUSER = "SYSTEM-MAAT";
    public static final Integer DEFAULT_TRY_TIMES = 0;

    /**
     * 实体ID
     */
    private String id;

    /**
     * 版本号
     */
    private String version;

    /**
     * 消息id
     */
    private String messageId;

    /**
     * 消息内容
     */
    private String messageBody;

    /**
     * 消息重试次数
     */
    private Integer messageTryTimes;

    /**
     * 消息队列名
     */
    private String messageQueue;

    /**
     * queryURL, 由主动方应用提供, 用于消息状态确认调用
     */
    private String queryURL;

    /**
     * 消息是否死亡
     */
    private Boolean messageDead;

    /**
     * 创建人
     */
    private String cuser;

    /**
     * 修改人
     */
    private String muser;

    /**
     * 创建时间
     */
    private LocalDateTime ctime;

    /**
     * 修改时间
     */
    private LocalDateTime mtime;

    /**
     * 消息状态
     */
    private int status;

    /**
     * 备注
     */
    private String remark;
}
