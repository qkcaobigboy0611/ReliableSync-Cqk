package com.example.reliablesynccqk;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * ReliableSync-Cqk是一个分布式事务中间件,实现了基于可靠消息的最终一致性事务控制.
 */
@SpringBootApplication
@EnableScheduling
@EnableAsync
public class ReliableSyncCqkApplication {

    public static void main(String[] args) {
        SpringApplication.run(ReliableSyncCqkApplication.class, args);
    }

}
