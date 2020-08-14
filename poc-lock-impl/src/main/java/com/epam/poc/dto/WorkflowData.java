package com.epam.poc.dto;

import lombok.Data;

@Data
public class WorkflowData {
  String topic;
  WorkflowType workflowType;
  Command command;

}
