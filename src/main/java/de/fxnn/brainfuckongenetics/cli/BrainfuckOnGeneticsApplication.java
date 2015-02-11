package de.fxnn.brainfuckongenetics.cli;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.google.common.collect.Multimap;
import de.fxnn.brainfuck.program.Program;
import de.fxnn.brainfuckongenetics.ProgramOptimizer;
import de.fxnn.brainfuckongenetics.fitness.BrainfuckProgramFitnessFunction;
import de.fxnn.brainfuckongenetics.fitness.LinearComputationFitnessFunction;
import de.fxnn.brainfuckongenetics.initialization.BrainfuckGenerationFactory;
import de.fxnn.genetics.GeneticAlgorithmConfiguration;
import de.fxnn.genetics.fitness.FitnessFunction;
import de.fxnn.genetics.generation.Generation;
import de.fxnn.genetics.generation.GenerationWithConcurrentFitnessFunction;
import de.fxnn.genetics.selection.LinearProbabilityGenerationSelector;

public class BrainfuckOnGeneticsApplication {

  public static final ExecutorService EXECUTOR_SERVICE = Executors.newFixedThreadPool(4);

  public static final ScheduledExecutorService TERMINATING_EXECUTOR_SERVICE = Executors
      .newSingleThreadScheduledExecutor();

  public static void main(String[] args) throws ExecutionException, InterruptedException {

    GeneticAlgorithmConfiguration<Program> configuration = new GeneticAlgorithmConfiguration<Program>() {

      @Override
      public FitnessFunction<Program> getFitnessFunction() {
        return new BrainfuckProgramFitnessFunction(new LinearComputationFitnessFunction());
      }

      @Override
      public int getPopulationSize() {
        return 1000;
      }

      @Override
      public int getNumberOfGenerationRounds() {
        return 100;
      }

      @Override
      public long getRandomSeed() {
        return 2398472349L;
      }

      @Override
      public double getSelectionRatio() {
        return 0.1;
      }
    };

    Generation<Program> generation = initializeGeneration(1, configuration);

    selectSolutions(configuration, generation);

    printGeneration(generation);

    printExceptions((GenerationWithConcurrentFitnessFunction) generation);

    EXECUTOR_SERVICE.shutdown();
    TERMINATING_EXECUTOR_SERVICE.shutdown();

  }

  protected static void printExceptions(GenerationWithConcurrentFitnessFunction generation) {

    System.out.println("Exceptions:");

    Multimap<Class<? extends Throwable>, Throwable> throwables = generation.getThrowables();
    for (Class<? extends Throwable> throwableClass : throwables.keySet()) {
      System.out.println("[" + throwables.get(throwableClass).size() + "] " + throwableClass.getName());
    }

  }

  protected static void printGeneration(Generation<Program> generation) {

    long startMillis = System.currentTimeMillis();

    ProgramOptimizer programOptimizer = new ProgramOptimizer();
    for (Program program : generation.getSolutions()) {
      boolean selected = generation.getSelectedSolutions().contains(program);
      double fitness = generation.getFitness(program);
      if (fitness > Double.NEGATIVE_INFINITY) {
        System.out.println((selected ? "X" : " ") + " [" + fitness + "] " + programOptimizer.optimizeProgram(program).getProgram());
      }
    }
    long duration = System.currentTimeMillis() - startMillis;

    System.out.println("Output completed in " + duration + " ms");
    System.out.println();

  }

  protected static void selectSolutions(GeneticAlgorithmConfiguration<Program> configuration,
      Generation<Program> generation) {

    long startMillis = System.currentTimeMillis();
    new LinearProbabilityGenerationSelector().selectSolutions(generation, configuration);
    long duration = System.currentTimeMillis() - startMillis;

    System.out.println("Selection completed in " + duration + " ms");
    System.out.println();

  }

  protected static Generation<Program> initializeGeneration(int timeoutInSeconds,
      final GeneticAlgorithmConfiguration<Program> configuration) {

    long startMillis = System.currentTimeMillis();
    Generation<Program> generation = createBrainfuckGenerationFactory(timeoutInSeconds, TimeUnit.SECONDS)
        .initializeGeneration(configuration);
    long duration = System.currentTimeMillis() - startMillis;

    System.out.println("Creating generation completed in " + duration + " ms");
    System.out.println();

    return generation;

  }

  protected static BrainfuckGenerationFactory createBrainfuckGenerationFactory(long timeoutDuration,
      TimeUnit timeoutTimeUnit) {

    return new BrainfuckGenerationFactory(EXECUTOR_SERVICE, TERMINATING_EXECUTOR_SERVICE, timeoutDuration,
        timeoutTimeUnit);

  }

}
