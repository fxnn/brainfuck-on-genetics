package de.fxnn.brainfuckongenetics;

import java.util.concurrent.TimeUnit;

import com.google.common.util.concurrent.ListeningExecutorService;
import com.google.common.util.concurrent.ListeningScheduledExecutorService;
import de.fxnn.brainfuck.program.Program;
import de.fxnn.genetics.GeneticAlgorithmConfiguration;
import de.fxnn.genetics.fitness.FitnessFunction;
import lombok.Data;

@Data
public class BrainfuckOnGeneticsConfiguration implements GeneticAlgorithmConfiguration<Program> {

  private ListeningExecutorService geneticAlgorithmExecutorService;

  private ListeningExecutorService fitnessFunctionExecutorService;

  private ListeningScheduledExecutorService watchdogExecutorService;

  private TimeUnit fitnessFunctionTimeoutUnit;

  private long fitnessFunctionTimeoutDuration;

  private FitnessFunction<Program> fitnessFunction;

  private int populationSize;

  private int numberOfGenerationRounds;

  private long randomSeed;

  private double selectionRatio;

}
