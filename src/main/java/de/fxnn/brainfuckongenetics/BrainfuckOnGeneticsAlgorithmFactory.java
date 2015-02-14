package de.fxnn.brainfuckongenetics;

import de.fxnn.brainfuck.program.Program;
import de.fxnn.brainfuckongenetics.initialization.BrainfuckGenerationFactory;
import de.fxnn.genetics.GeneticAlgorithm;
import de.fxnn.genetics.TerminationCondition;
import de.fxnn.genetics.breeding.GenerationBreeder;
import de.fxnn.genetics.initialization.GenerationFactory;
import de.fxnn.genetics.selection.GenerationSelector;
import de.fxnn.genetics.selection.SquareProbabilityGenerationSelector;

public class BrainfuckOnGeneticsAlgorithmFactory {

  public GeneticAlgorithm<Program> createGeneticAlgorithm(BrainfuckOnGeneticsConfiguration configuration) {

    return new GeneticAlgorithm<>(configuration, createGenerationFactory(configuration), createGenerationSelector(),
        createGenerationBreeder(), createTerminationCondition());

  }

  protected TerminationCondition<Program> createTerminationCondition() {
    // HINT: Zunächst nach einem Durchlauf beenden (TODO)
    return generation -> !generation.getSelectedSolutions().isEmpty();
  }

  protected GenerationBreeder<Program> createGenerationBreeder() {
    // HINT: Zunächst kein Breeding (TODO)
    return generation -> generation;
  }

  protected GenerationSelector createGenerationSelector() {
    return new SquareProbabilityGenerationSelector();
  }

  protected GenerationFactory<Program> createGenerationFactory(BrainfuckOnGeneticsConfiguration configuration) {
    return new BrainfuckGenerationFactory( //
        configuration.getFitnessFunctionExecutorService(), //
        configuration.getWatchdogExecutorService(), //
        configuration.getFitnessFunctionTimeoutDuration(), //
        configuration.getFitnessFunctionTimeoutUnit());
  }

}
