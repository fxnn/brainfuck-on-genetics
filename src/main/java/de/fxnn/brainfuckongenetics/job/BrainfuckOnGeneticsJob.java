package de.fxnn.brainfuckongenetics.job;

import java.util.Date;
import java.util.concurrent.ExecutionException;

import javax.annotation.Nonnull;

import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.MoreExecutors;
import de.fxnn.brainfuck.program.Program;
import de.fxnn.brainfuckongenetics.BrainfuckOnGeneticsAlgorithmFactory;
import de.fxnn.brainfuckongenetics.BrainfuckOnGeneticsConfiguration;
import de.fxnn.genetics.GeneticAlgorithm;
import de.fxnn.genetics.generation.Generation;

public class BrainfuckOnGeneticsJob {

  private final BrainfuckOnGeneticsAlgorithmFactory algorithmFactory;

  private ListenableFuture<Generation<Program>> future;

  private Date timestampStarted;

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
    try {
      if (isJobRunning()) {
        return new Date().getTime() - getTimestampStarted().getTime();
      }

      return getTimestampDone().getTime() - getTimestampStarted().getTime();

    } catch (JobCurrentlyRunningException | NoJobActiveException e) {
      return 0;
    }
  }

  @Nonnull
  public Date getTimestampStarted() throws NoJobActiveException {
    if (timestampStarted == null) {
      throw new NoJobActiveException();
    }

    return timestampStarted;
  }

  @Nonnull
  public Date getTimestampDone() throws JobCurrentlyRunningException, NoJobActiveException {
    if (timestampDone == null) {
      if (isJobRunning()) {
        throw new JobCurrentlyRunningException(timestampStarted);
      }

      throw new NoJobActiveException();
    }

    return timestampDone;
  }

  public boolean isJobRunning() {
    return future != null && !future.isDone();
  }
}
