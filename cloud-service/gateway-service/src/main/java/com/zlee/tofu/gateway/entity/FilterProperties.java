package com.zlee.tofu.gateway.entity;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @author z-Lee
 * @date 2023-03-02-14:01
 */
@Data
@Component
@ConfigurationProperties(prefix = "tofu.filter")
public class FilterProperties {

    private List<String> routesWhitelist;
}
