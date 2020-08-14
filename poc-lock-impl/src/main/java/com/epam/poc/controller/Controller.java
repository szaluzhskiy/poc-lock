package com.epam.poc.controller;

import com.epam.poc.cadence.CadenceProperties;
import com.epam.poc.cadence.workflow.ContractWorkflow;
import com.epam.poc.dto.WorkflowData;
import com.epam.poc.dto.WorkflowStatus;
import com.uber.cadence.*;
import com.uber.cadence.client.*;
import com.uber.cadence.internal.common.WorkflowExecutionUtils;
import com.uber.cadence.serviceclient.IWorkflowService;
import com.uber.cadence.serviceclient.WorkflowServiceTChannel;
import com.uber.cadence.workflow.Workflow;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

@RestController
@RequestMapping(value = "controller")
@Slf4j
@RequiredArgsConstructor
public class Controller {

  private final WorkflowClient workflowClient;

  IWorkflowService cadenceService = new WorkflowServiceTChannel(
      "127.0.0.1", 7933);

  @RequestMapping(value = "send", method = RequestMethod.POST)
  public void send(@RequestBody WorkflowData workflowData) {
    String contractId = workflowData.getCommand().getContractId();
    ArrayList<WorkflowData> queue = new ArrayList<WorkflowData>(100);
    queue.add(workflowData);
    try {
      WorkflowExecution execution = new WorkflowExecution().setWorkflowId(contractId);
      WorkflowExecutionInfo wei = WorkflowExecutionUtils.describeWorkflowInstance(cadenceService, "poc_lock", execution);
      if (wei.closeStatus == null || wei.closeStatus == WorkflowExecutionCloseStatus.CONTINUED_AS_NEW || wei.closeStatus.getValue() < 0) {
        WorkflowStub stub = workflowClient.newUntypedWorkflowStub(execution, Optional.of(new String("Create contract")));
        stub.signal("ContractWorkflow::addCommandToList", workflowData);
        log.debug("Signal Command to workflow queue. CommandId {} RequestId {} " + workflowData.getCommand().getContractId(),
            workflowData.getCommand().getRequestId());
      } else {
        WorkflowOptions workflowOptions = new WorkflowOptions.Builder()
            .setWorkflowId(contractId)
            .setWorkflowIdReusePolicy(WorkflowIdReusePolicy.AllowDuplicate)
            .build();
        ContractWorkflow workflow = workflowClient.newWorkflowStub(ContractWorkflow.class,
            workflowOptions);

        WorkflowClient.start(workflow::processContract, queue);
      }
    } catch (Exception e) {
      WorkflowOptions workflowOptions = new WorkflowOptions.Builder()
          .setWorkflowId(contractId)
          .setWorkflowIdReusePolicy(WorkflowIdReusePolicy.AllowDuplicate)
          .build();
      ContractWorkflow workflow = workflowClient.newWorkflowStub(ContractWorkflow.class,
          workflowOptions);
      WorkflowClient.start(workflow::processContract, queue);
    }
  }
}
