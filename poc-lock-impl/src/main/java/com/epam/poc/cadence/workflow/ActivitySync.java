package com.epam.poc.cadence.workflow;

import com.epam.poc.dto.Command;
import com.uber.cadence.activity.ActivityMethod;

public interface ActivitySync {

  @ActivityMethod(scheduleToCloseTimeoutSeconds = 60_000)
  void execute(Command command);
}
