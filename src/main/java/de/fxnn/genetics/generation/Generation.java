package de.fxnn.genetics.generation;

import java.util.Collection;
import java.util.stream.DoubleStream;

public interface Generation<Solution> {

  /**
   * Immutable
   */
  Collection<Solution> getSolutions();

  /**
   * Immutable
   */
  Collection<Solution> getSelectedSolutions();

  double getFitness(Solution solution);

  DoubleStream streamFitnessValues();

  void addSolution(Solution solution);

  void selectSolution(Solution solution);

}
