package de.fxnn.genetics.generation;

import java.util.Collection;

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

  void addSolution(Solution solution);

  void selectSolution(Solution solution);

}
