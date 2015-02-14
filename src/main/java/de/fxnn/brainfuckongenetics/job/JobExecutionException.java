package de.fxnn.brainfuckongenetics.job;

public class JobExecutionException extends JobException {

  public JobExecutionException() {
  }

  public JobExecutionException(String message) {
    super(message);
  }

  public JobExecutionException(String message, Throwable cause) {
    super(message, cause);
  }

  public JobExecutionException(Throwable cause) {
    super(cause);
  }

  public JobExecutionException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
