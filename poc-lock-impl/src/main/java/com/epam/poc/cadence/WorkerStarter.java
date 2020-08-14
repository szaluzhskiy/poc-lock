package com.epam.poc.cadence;

import com.epam.poc.cadence.workflow.ActivitySyncImpl;
import com.epam.poc.cadence.workflow.ContractWorkflowImpl;
import com.uber.cadence.worker.Worker;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import static com.epam.poc.cadence.CadenceConfig.TASK_LIST;

@Service
@RequiredArgsConstructor
public class WorkerStarter implements CommandLineRunner {

  private final CadenceProperties cadenceProperties;

  @Override
  public void run(String... strings) throws Exception {

    Worker.Factory factory = new Worker.Factory(cadenceProperties.getHost(),
        cadenceProperties.getPort(), cadenceProperties.getDomain());

    Worker wfFirstWorker = factory.newWorker(TASK_LIST);
    wfFirstWorker.registerWorkflowImplementationTypes(ContractWorkflowImpl.class);
    wfFirstWorker.registerActivitiesImplementations(new ActivitySyncImpl());

    factory.start();

  }

}

