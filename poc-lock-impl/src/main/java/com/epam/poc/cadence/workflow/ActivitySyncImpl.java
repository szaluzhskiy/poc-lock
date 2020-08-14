package com.epam.poc.cadence.workflow;

import com.epam.poc.dto.Command;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ActivitySyncImpl implements ActivitySync {

  @Override
  public void execute(Command command) {
    log.debug("activity. comand: {}", command);
    long currentTimeMillis = System.currentTimeMillis();
    while(System.currentTimeMillis() - currentTimeMillis < 20_000) {
      //
    }
    log.debug("activity end");
  }
}
