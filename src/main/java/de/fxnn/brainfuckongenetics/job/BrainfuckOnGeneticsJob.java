package de.fxnn.brainfuckongenetics.job;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import de.fxnn.brainfuck.program.Program;
import de.fxnn.brainfuckongenetics.BrainfuckOnGeneticsAlgorithmFactory;
import de.fxnn.brainfuckongenetics.BrainfuckOnGeneticsConfiguration;
import de.fxnn.genetics.GeneticAlgorithm;
import de.fxnn.genetics.generation.Generation;
import lombok.Getter;

public class BrainfuckOnGeneticsJob {

  private final BrainfuckOnGeneticsAlgorithmFactory algorithmFactory;

  private ListenableFuture<Generation<Program>> future;

  @Getter
  private Date timestampStarted;

  @Getter
  private volatile Date timestampDone;

  public BrainfuckOnGeneticsJob() {
    algorithmFactory = new BrainfuckOnGeneticsAlgorithmFactory();
  }

  public void start(BrainfuckOnGeneticsConfiguration configuration) throws JobCurrentlyRunningException {
    if (isJobRunning()) {
      throw new JobCurrentlyRunningException(timestampStarted);
    }

    ListeningExecutorService geneticAlgorithmExecutorService = configuration.getGeneticAlgorithmExecutorService();
    GeneticAlgorithm<Program> geneticAlgorithm = algorithmFactory.createGeneticAlgorithm(configuration);

    startClock();
    future = geneticAlgorithmExecutorService.submit(geneticAlgorithm);
    future.addListener(this::stopClock, MoreExecutors.directExecutor());
  }

  public void waitForCompletion() throws NoJobActiveException, InterruptedException, JobExecutionException {
    if (!isJobRunning()) {
      throw new NoJobActiveException();
    }

    try {
      future.get();
    } catch (ExecutionException e) {
      throw new JobExecutionException("The job execution was cancelled with an error: " + e.getCause().toString(), e);
    }
  }

  protected void startClock() {
    timestampDone = null;
    timestampStarted = new Date();
  }

  protected void stopClock() {
    timestampDone = new Date();
  }

  public void cancel() throws NoJobActiveException {
    if (!isJobRunning()) {
      throw new NoJobActiveException();
    }

    stopClock();
    future.cancel(true);
  }

  public Generation<Program> getLastResult()
      throws NoJobActiveException, JobCurrentlyRunningException, JobExecutionException {
    if (future == null) {
      throw new NoJobActiveException();
    }
    if (isJobRunning()) {
      throw new JobCurrentlyRunningException(timestampStarted);
    }

    try {
      return future.get();
    } catch (InterruptedException e) {
      throw new RuntimeException("Could not retrieve job result, as I was interrupted.", e);
    } catch (ExecutionException e) {
      throw new JobExecutionException("The job execution was cancelled with an error: " + e.getCause().toString(), e);
    }
  }

  public long getJobRunDurationInMilliseconds() {
    if (timestampStarted == null) {
      return 0;
    }

    if (timestampDone == null) {
      return new Date().getTime() - getTimestampStarted().getTime();
    }

    return getTimestampDone().getTime() - getTimestampStarted().getTime();
  }

  public boolean isJobRunning() {
    return future != null && !future.isDone();
  }
}
