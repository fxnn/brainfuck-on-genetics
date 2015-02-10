package de.fxnn.genetics;

import java.util.concurrent.Callable;

import de.fxnn.genetics.breeding.GenerationBreeder;
import de.fxnn.genetics.generation.Generation;
import de.fxnn.genetics.initialization.GenerationFactory;
import de.fxnn.genetics.selection.GenerationSelector;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class GeneticAlgorithm<Solution> implements Callable<Generation<Solution>> {

  /**
   * Aspects of the genetic algorithm that might possibly be subject to change during execution.
   */
  private final GeneticAlgorithmConfiguration<Solution> geneticAlgorithmConfiguration;

  private final GenerationFactory<Solution> generationFactory;

  private final GenerationSelector<Solution> generationSelector;

  private final GenerationBreeder<Solution> generationBreeder;

  private final TerminationCondition<Solution> terminationCondition;

  public Generation<Solution> call() {

    Generation<Solution> generation = generationFactory.initializeGeneration(geneticAlgorithmConfiguration);

    while (!terminationCondition.isTerminationConditionReached(generation)) {

      generationSelector.selectSolutions(generation);

      generation = generationBreeder.breedNewGeneration(generation);

    }

    return generation;

  }

}
