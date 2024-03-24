/**
 * @author qkcao
 * @date 2024/3/20 18:42
 */
package com.example.reliablesynccqk.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.example.reliablesynccqk.model.po.Message;
import org.springframework.stereotype.Repository;

@Repository
public interface ReliableSyncMapper extends BaseMapper<Message> {
}
