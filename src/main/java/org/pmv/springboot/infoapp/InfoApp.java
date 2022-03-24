package org.pmv.springboot.infoapp;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "info.application")
@AllArgsConstructor
@Data
@ToString
public class InfoApp {

    private String name;
    private String description;
    private String version;

}
