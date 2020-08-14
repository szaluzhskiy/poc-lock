package com.epam.poc.cadence.workflow;

import com.epam.poc.dto.WorkflowData;
import com.uber.cadence.workflow.SignalMethod;
import com.uber.cadence.workflow.WorkflowMethod;

import java.util.ArrayList;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

import static com.epam.poc.cadence.CadenceConfig.TASK_LIST;

public interface ContractWorkflow {

  @WorkflowMethod(taskList = TASK_LIST, executionStartToCloseTimeoutSeconds = 600)
  //void processContract(ArrayBlockingQueue<WorkflowData> workflowData);
  void processContract(ArrayList<WorkflowData> commandsQueue );


  @SignalMethod
  void addCommandToList(WorkflowData workflowData);
}
