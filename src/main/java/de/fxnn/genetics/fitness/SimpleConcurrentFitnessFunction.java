package de.fxnn.genetics.fitness;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public class SimpleConcurrentFitnessFunction<Solution> implements ConcurrentFitnessFunction<Solution> {

  private final ExecutorService executorService;

  private final FitnessFunction<Solution> fitnessFunction;

  @Override
  public Future<Double> computeFitness(Solution solution) {
    return executorService.submit(() -> fitnessFunction.computeFitness(solution));
  }

}
