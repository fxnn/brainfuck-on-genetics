package de.fxnn.brainfuckongenetics.job;

public class NoJobActiveException extends JobException {

  public NoJobActiveException() {
  }

  public NoJobActiveException(String message) {
    super(message);
  }

  public NoJobActiveException(String message, Throwable cause) {
    super(message, cause);
  }

  public NoJobActiveException(Throwable cause) {
    super(cause);
  }

  public NoJobActiveException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
    super(message, cause, enableSuppression, writableStackTrace);
  }
}
