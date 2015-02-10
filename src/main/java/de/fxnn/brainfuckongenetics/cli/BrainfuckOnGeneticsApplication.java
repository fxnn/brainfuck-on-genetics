package de.fxnn.brainfuckongenetics.cli;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Iterables;
import com.google.common.collect.Multimap;
import de.fxnn.brainfuck.program.Program;
import de.fxnn.brainfuck.program.TreePrograms;
import de.fxnn.brainfuckongenetics.fitness.BrainfuckProgramFitnessFunction;
import de.fxnn.brainfuckongenetics.fitness.LinearComputationFitnessFunction;
import de.fxnn.brainfuckongenetics.initialization.BrainfuckGenerationFactory;
import de.fxnn.genetics.fitness.FitnessFunction;
import de.fxnn.genetics.generation.Generation;
import de.fxnn.genetics.generation.GenerationWithConcurrentFitnessFunction;
import de.fxnn.genetics.initialization.GenerationFactoryConfiguration;

public class BrainfuckOnGeneticsApplication {

  public static void main(String[] args) throws ExecutionException, InterruptedException {

    Generation<Program> generation = initializeGeneration(10, 1000, 100);

    printGeneration(generation);

    System.out.println("Exceptions:"); // FIXME Remove System.out
    Multimap<Class<? extends Throwable>, Throwable> throwables = ((GenerationWithConcurrentFitnessFunction) generation).getThrowables();
    for (Class<? extends Throwable> throwable : throwables.keySet()) {
      Iterables.limit(throwables.get(throwable), 5).forEach(Throwable::printStackTrace);
      System.err.flush();
    }

  }

  protected static void printGeneration(Generation<Program> generation) {
    long startMillis = System.currentTimeMillis();

    for (Program program : generation.getSolutions()) {
      double fitness = generation.getFitness(program);
      if (fitness > Double.NEGATIVE_INFINITY) {
        System.out.println("[" + fitness + "] " + TreePrograms.toString(program));
      }
    }
    long duration = System.currentTimeMillis() - startMillis;

    System.out.println("Output completed in " + duration + " ms"); // FIXME Remove System.out
  }

  protected static Generation<Program> initializeGeneration(int timeoutInSeconds, int populationSize,
      int numberOfGenerationRounds) {
    long startMillis = System.currentTimeMillis();
    Generation<Program> generation = createBrainfuckGenerationFactory(timeoutInSeconds, TimeUnit.SECONDS)
        .initializeGeneration(createGenerationFactoryConfiguration(populationSize, numberOfGenerationRounds));
    long duration = System.currentTimeMillis() - startMillis;

    System.out.println("Creating generation completed in " + duration + " ms");
    return generation;
  }

  protected static BrainfuckGenerationFactory createBrainfuckGenerationFactory(long timeoutDuration, TimeUnit timeoutTimeUnit) {

    ExecutorService executorService = Executors.newFixedThreadPool(4);
    ScheduledExecutorService terminatingExecutorService = Executors.newSingleThreadScheduledExecutor();

    return new BrainfuckGenerationFactory(executorService, terminatingExecutorService, timeoutDuration, timeoutTimeUnit);

  }

  protected static GenerationFactoryConfiguration<Program> createGenerationFactoryConfiguration(
      final int populationSize, final int numberOfGenerationRounds) {
    return new GenerationFactoryConfiguration<Program>() {

      @Override
      public FitnessFunction<Program> getFitnessFunction() {
        return new BrainfuckProgramFitnessFunction(new LinearComputationFitnessFunction());
      }

      @Override
      public int getPopulationSize() {
        return populationSize;
      }

      @Override
      public int getNumberOfGenerationRounds() {
        return numberOfGenerationRounds;
      }

      @Override
      public long getRandomSeed() {
        return 2398472349L;
      }
    };
  }

}
