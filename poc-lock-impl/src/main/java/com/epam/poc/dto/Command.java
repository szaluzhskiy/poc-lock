package com.epam.poc.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.Data;

@Data
public class Command implements Serializable {

  private String contractId;
  private String requestId;

  private String stringValue;
  private Long longValue;
  @JsonFormat(pattern = "dd.MM.yyyy")
  private Date dateValue;

  private Map<String, String> additinalProperties = new HashMap<>();

}
