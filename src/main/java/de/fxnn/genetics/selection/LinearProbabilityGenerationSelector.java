package de.fxnn.genetics.selection;

import java.util.DoubleSummaryStatistics;
import java.util.Random;

import de.fxnn.genetics.generation.Generation;

public class LinearProbabilityGenerationSelector implements GenerationSelector {

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
    return random.nextDouble() < getProbability(generation.getFitness(solution), statistics);
  }

  protected double getProbability(double fitness, DoubleSummaryStatistics statistics) {
    return (fitness - statistics.getMin()) / (statistics.getMax() - statistics.getMin());
  }

  protected <S> DoubleSummaryStatistics calculateFitnessStatistics(Generation<S> generation) {
    return generation.streamFitnessValues().filter(Double::isFinite).summaryStatistics();
  }
}
