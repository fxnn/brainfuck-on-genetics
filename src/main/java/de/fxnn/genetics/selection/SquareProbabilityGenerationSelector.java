package de.fxnn.genetics.selection;

import java.util.DoubleSummaryStatistics;
import java.util.Random;

import de.fxnn.genetics.generation.Generation;

public class SquareProbabilityGenerationSelector implements GenerationSelector {

  /**
   * Geringere Wahrscheinlichkeit, um über die gesamte Lösungsmenge min. einmal zu iterieren.
   */
  public static final double PROBABILITY_DEGRADATION_FACTOR = 0.01;

  @Override
  public <S> void selectSolutions(Generation<S> generation, GenerationSelectorConfiguration configuration) {

    int selectionSize = (int) Math.round(generation.getSolutions().size() * configuration.getSelectionRatio());
    DoubleSummaryStatistics statistics = calculateFitnessStatistics(generation);

    Random random = new Random(configuration.getRandomSeed());
    while (!isSelectionSizeReached(selectionSize, generation)) {
      for (S solution : generation.getSolutions()) {
        if (selectSolution(solution, generation, random, statistics)) {
          generation.selectSolution(solution);

          if (isSelectionSizeReached(selectionSize, generation)) {
            break;
          }
        }
      }
    }

  }

  protected <S> boolean isSelectionSizeReached(int selectionSize, Generation<S> generation) {
    return generation.getSelectedSolutions().size() >= selectionSize;
  }

  protected <S> boolean selectSolution(S solution, Generation<S> generation, Random random,
      DoubleSummaryStatistics statistics) {
    return random.nextDouble() < PROBABILITY_DEGRADATION_FACTOR * getSquareProbability(generation.getFitness(solution),
        statistics);
  }

  protected double getSquareProbability(double fitness, DoubleSummaryStatistics statistics) {
    if (Double.isFinite(fitness)) {
      double linearProbability = (fitness - statistics.getMin()) / (statistics.getMax() - statistics.getMin());
      return linearProbability * linearProbability;
    }

    return 0;
  }

  protected <S> DoubleSummaryStatistics calculateFitnessStatistics(Generation<S> generation) {
    return generation.streamFitnessValues().filter(Double::isFinite).summaryStatistics();
  }
}
