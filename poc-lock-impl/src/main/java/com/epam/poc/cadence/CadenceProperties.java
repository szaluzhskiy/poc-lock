package com.epam.poc.cadence;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
@Component
@ConfigurationProperties("app.cadence")
public class CadenceProperties {

  private String domain;
  private String host;
  private Integer port;
}
