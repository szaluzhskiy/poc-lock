package com.epam.poc.cadence.workflow;

import static com.epam.poc.dto.WorkflowStatus.FINISHED;
import static com.epam.poc.dto.WorkflowStatus.NOT_STARTED;
import static com.epam.poc.dto.WorkflowStatus.STARTED;

import com.epam.poc.cadence.CadenceConfig;
import com.epam.poc.dto.Command;
import com.epam.poc.dto.WorkflowData;
import com.epam.poc.dto.WorkflowType;
import com.uber.cadence.WorkflowIdReusePolicy;
import com.uber.cadence.client.WorkflowClient;
import com.uber.cadence.client.WorkflowOptions;
import com.uber.cadence.workflow.Workflow;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ContractWorkflowImpl implements ContractWorkflow {

  private ArrayList<WorkflowData> commandsQueue = new ArrayList<WorkflowData>(100);

  @Override
  public void processContract(ArrayList<WorkflowData> commandsQueue) {
    log.debug("contract_workflow_started");

    WorkflowData data = commandsQueue.remove(commandsQueue.size() -1 );
    if (data != null) {
      log.info("Processing command id-{} requestId-{} date-{}", data.getCommand().getContractId(), data.getCommand().getRequestId(), data.getCommand().getDateValue());
      Workflow.sleep(30_000);
      log.debug("Continue to process size {}", commandsQueue.size());
      Workflow.continueAsNew(this.commandsQueue);
    }
  }

  @Override
  public void addCommandToList(WorkflowData workflowData) {
    commandsQueue.add(workflowData);
    log.debug("add to list method. result queue is :\n {}", commandsQueue.stream()
        .map(Object::toString)
        .collect(Collectors.joining("\n")));
  }
}
