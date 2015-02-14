package de.fxnn.brainfuckongenetics.job;

import java.util.Date;

import lombok.Getter;

public class JobCurrentlyRunningException extends JobException {

  @Getter
  private final long runDurationInMillis;

  @Getter
  private final Date timestampStart;

  public JobCurrentlyRunningException(Date timestampStart) {
    this.timestampStart = timestampStart;
    this.runDurationInMillis = new Date().getTime() - timestampStart.getTime();
  }

}
