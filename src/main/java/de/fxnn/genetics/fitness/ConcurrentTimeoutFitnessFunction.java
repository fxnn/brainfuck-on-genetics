package de.fxnn.genetics.fitness;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class ConcurrentTimeoutFitnessFunction<Solution> implements ConcurrentFitnessFunction<Solution> {

  private final ExecutorService executorService;

  private final ScheduledExecutorService terminatingExecutorService;

  private final FitnessFunction<Solution> fitnessFunction;

  private final long delay;

  private final TimeUnit timeUnit;

  public Future<Double> computeFitness(Solution solution) {

    Future<Double> future = executorService.submit(() -> fitnessFunction.computeFitness(solution));

    startTimeout(future);

    return future;

  }

  protected ScheduledFuture<Boolean> startTimeout(Future<Double> future) {
    return terminatingExecutorService.schedule(() -> future.cancel(true), delay, timeUnit);
  }

}
