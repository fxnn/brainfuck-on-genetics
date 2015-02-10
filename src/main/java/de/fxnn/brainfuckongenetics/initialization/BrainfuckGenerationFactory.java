package de.fxnn.brainfuckongenetics.initialization;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import de.fxnn.brainfuck.program.Program;
import de.fxnn.brainfuck.program.TreeProgram;
import de.fxnn.brainfuckongenetics.operators.mutation.BrainfuckInsertingMutationOperator;
import de.fxnn.genetics.fitness.ConcurrentTimeoutFitnessFunction;
import de.fxnn.genetics.fitness.FitnessFunction;
import de.fxnn.genetics.generation.Generation;
import de.fxnn.genetics.generation.GenerationWithConcurrentFitnessFunction;
import de.fxnn.genetics.initialization.GenerationFactory;
import de.fxnn.genetics.initialization.GenerationFactoryConfiguration;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public class BrainfuckGenerationFactory implements GenerationFactory<Program> {

  private final ExecutorService executorService;

  private final ScheduledExecutorService terminatingExecutorService;

  private final long timeout;

  private final TimeUnit timeoutTimeUnit;

  @Override
  public Generation<Program> initializeGeneration(GenerationFactoryConfiguration<Program> configuration) {
    GenerationWithConcurrentFitnessFunction<Program> generation = createEmptyGeneration(
        configuration.getPopulationSize(), configuration.getFitnessFunction());

    generateRandomSolutions(generation, configuration.getPopulationSize(), configuration.getNumberOfGenerationRounds(), configuration.getRandomSeed());

    return generation;
  }

  private void generateRandomSolutions(GenerationWithConcurrentFitnessFunction<Program> generation, int solutionCount,
      long numberOfGenerationRounds, long randomSeed) {
    Random random = new Random(randomSeed);

    for (int solutionIndex = 0; solutionIndex < solutionCount; solutionIndex++) {
      generation.addSolution(generateRandomSolution(numberOfGenerationRounds, random));
    }
  }

  private Program generateRandomSolution(long numberOfGenerationRounds, Random random) {
    TreeProgram result = new TreeProgram(new ArrayList<>());

    BrainfuckInsertingMutationOperator[] operators = BrainfuckInsertingMutationOperator.values();
    for (int i = 0; i < numberOfGenerationRounds; i++) {
      BrainfuckInsertingMutationOperator operator = operators[random.nextInt(operators.length)];
      operator.mutate(result, random);
    }

    return result;
  }

  protected GenerationWithConcurrentFitnessFunction<Program> createEmptyGeneration(int maxPopulationSize,
      FitnessFunction<Program> fitnessFunction) {
    return new GenerationWithConcurrentFitnessFunction<>(maxPopulationSize,
        new ConcurrentTimeoutFitnessFunction<>(executorService, terminatingExecutorService, fitnessFunction, timeout, timeoutTimeUnit));
  }
}
