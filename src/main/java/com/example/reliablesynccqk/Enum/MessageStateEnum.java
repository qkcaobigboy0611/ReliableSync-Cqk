/**
 * @author qkcao
 * @date 2024/3/20 19:13
 */
package com.example.reliablesynccqk.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 消息状态枚举
 */
@Getter
@AllArgsConstructor
public enum MessageStateEnum {
    UNKNOWN(-1, ""),
    UNCONFIRMED(1, "待确认"),
    SENDING(2, "已发送"),
    CANCELED(3, "已取消"),
    DEAD(3, "死亡"),
    FINISHED(3, "已完成");

    private int code;
    private String desc;

    public static MessageStateEnum forValue(int code){
        for(MessageStateEnum obj : MessageStateEnum.values()){
            if(obj.getCode() == code){
                return obj;
            }
        }
        return UNKNOWN;
    }


    public static String getValueByCode(int code){
        for(MessageStateEnum obj : MessageStateEnum.values()){
            if(obj.getCode() == code){
                return obj.getDesc();
            }
        }
        return UNKNOWN.getDesc();
    }
}
