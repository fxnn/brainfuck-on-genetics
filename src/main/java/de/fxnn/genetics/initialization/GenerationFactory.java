package de.fxnn.genetics.initialization;

import de.fxnn.genetics.generation.Generation;

public interface GenerationFactory<Solution> {

  Generation<Solution> initializeGeneration(GenerationFactoryConfiguration<Solution> generationFactoryConfiguration);

}
