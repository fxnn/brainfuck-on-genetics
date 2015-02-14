package de.fxnn.brainfuckongenetics.cli;

import java.util.Date;
import java.util.concurrent.TimeUnit;

import de.fxnn.brainfuckongenetics.BrainfuckOnGeneticsConfiguration;
import de.fxnn.brainfuckongenetics.fitness.BrainfuckProgramFitnessFunction;
import de.fxnn.brainfuckongenetics.fitness.LinearComputationFitnessFunction;

import static com.google.common.util.concurrent.MoreExecutors.listeningDecorator;
import static java.util.concurrent.Executors.*;

public class BrainfuckOnGeneticsConfigurationFactory {

  public BrainfuckOnGeneticsConfiguration fromDefaults() {
    BrainfuckOnGeneticsConfiguration result = new BrainfuckOnGeneticsConfiguration();

    result.setPopulationSize(1000);
    result.setNumberOfGenerationRounds(100);
    result.setSelectionRatio(0.1);
    result.setRandomSeed(new Date().getTime());
    result.setFitnessFunction(new BrainfuckProgramFitnessFunction(new LinearComputationFitnessFunction()));

    result.setFitnessFunctionTimeoutDuration(1);
    result.setFitnessFunctionTimeoutUnit(TimeUnit.SECONDS);

    result.setFitnessFunctionExecutorService(listeningDecorator(newFixedThreadPool(getProcessorCount())));
    result.setGeneticAlgorithmExecutorService(listeningDecorator(newSingleThreadExecutor()));
    result.setWatchdogExecutorService(listeningDecorator(newSingleThreadScheduledExecutor()));

    return result;
  }

  protected int getProcessorCount() {
    return Runtime.getRuntime().availableProcessors();
  }

}
