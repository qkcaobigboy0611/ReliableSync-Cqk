/**
 * @author qkcao
 * @date 2024/3/22 17:09
 */
package com.example.reliablesynccqk.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "reliablesync")
@Validated
public class ReliablesyncProperties {

    private List<Integer> retryWaitSeconds;
}
