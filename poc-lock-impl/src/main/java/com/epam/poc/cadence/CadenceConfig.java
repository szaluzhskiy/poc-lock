package com.epam.poc.cadence;

import com.uber.cadence.client.WorkflowClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CadenceConfig {

  public final static String TASK_LIST = "POC_LOCK_WF";
  public static final long DELAY_SECONDS = 15;
  //just for poc
  private static WorkflowClient workflowClient;

  public static WorkflowClient getWorkflowClient() {
    return workflowClient;
  }

  @Bean
  public WorkflowClient workflowClient(CadenceProperties cadenceProperties) {
    CadenceConfig.workflowClient = WorkflowClient.newInstance(cadenceProperties.getHost(), cadenceProperties.getPort(),
        cadenceProperties.getDomain());
    return workflowClient;
  }

}
