package de.fxnn.genetics.initialization;

import de.fxnn.genetics.fitness.FitnessFunction;

public interface GenerationFactoryConfiguration<Solution> {

  FitnessFunction<Solution> getFitnessFunction();

  int getPopulationSize();

  int getNumberOfGenerationRounds();

  long getRandomSeed();

}
