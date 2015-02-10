package de.fxnn.genetics.fitness;

import java.util.concurrent.Future;

public interface ConcurrentFitnessFunction<Solution> {

  Future<Double> computeFitness(Solution solution);
}
